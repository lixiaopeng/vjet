/*******************************************************************************
 * Copyright (c) 2005-2011 eBay Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.ebayopensource.dsf.ts.event.dispatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.ebayopensource.dsf.jst.ts.TypeSpaceConfig;
import org.ebayopensource.dsf.jst.ts.TypeSpaceLocker;
import org.ebayopensource.dsf.ts.event.ISourceEvent;
import org.ebayopensource.dsf.ts.event.ISourceEventCallback;
import org.ebayopensource.dsf.ts.event.ISourceEventListener;

/**
 * TODO:
 * 1. Synchronization logic
 * 2. Thread Pool (leverage CommandRunner)
 */
public class SourceEventDispatcher implements 
	IEventDispatcher<ISourceEvent<IEventListenerHandle>,ISourceEventListener,IEventListenerHandle,ISourceEventCallback> {
	
	private List<ISourceEventListener> m_srcEventListeners = new ArrayList<ISourceEventListener>();
	private final TypeSpaceLocker m_locker;
	private boolean m_synchronous_events = false;
	
	//
	// Constructor
	//
	/**
	 * Constructor
	 * Type space operation will be synchronized using given locker
	 * @param locker TypeSpaceLocker
	 */
	public SourceEventDispatcher(TypeSpaceLocker locker){
		if (locker == null){
			m_locker = new TypeSpaceLocker();
		}
		else {
			m_locker = locker;
		}
	}
	
	//
	// Satisfy IEventDispatcher
	//
	public synchronized void addListener(ISourceEventListener listener){
		m_srcEventListeners.add(listener);
	}
	
	public synchronized void removeListener(ISourceEventListener listener){
		m_srcEventListeners.remove(listener);
	}
	
	/**
	 * @see IEventDispatcher#dispatch(Object)
	 */
	public void dispatch(final ISourceEvent event){
		
		boolean lock = event.shouldLock();
		try {
			if (lock) {
				m_locker.lockExclusive();
			}
			for (ISourceEventListener listener: getListeners(true)){
				if (event.isAppropriateListener(listener)){
					event.dispatch(listener);
				}
			}
		}
		finally {
			if (lock) {
				m_locker.releaseExclusive();
			}
		}
	}
	
	/**
	 * @see IEventDispatcher#dispatch(Object, Object)
	 * TODO: process m_synchronous_events here and merge with the above method
	 */
	public IEventListenerHandle dispatch(final ISourceEvent<IEventListenerHandle> event, final ISourceEventCallback callback){
		
		for (ISourceEventListener listener: getListeners(true)){
			if (event.isAppropriateListener(listener)){
				run(event, listener, callback);
			}
		}
		return null;
	}
	
	//
	// Private
	//
	private synchronized List<ISourceEventListener> getListeners(boolean readOnly){
		if (readOnly){
			return Collections.unmodifiableList(m_srcEventListeners);
		}
		return m_srcEventListeners;
	}
	
	// TODO - replace with real impl.
	private IEventListenerHandle run(final ISourceEvent<IEventListenerHandle> event, 
			final ISourceEventListener listener,
			final ISourceEventCallback callback){
		
		final IEventListenerHandle handle = new IEventListenerHandle.Default();
		final boolean lock = event.shouldLock();
		
		Runnable r = new Runnable(){
			public void run(){
				try {
					if (lock) {
						m_locker.lockExclusive();
					}
					event.dispatch(listener, handle, callback);
				}
				catch(Throwable e){
					e.printStackTrace();
				}
				finally{
					if (lock) {
						m_locker.releaseExclusive();
					}
				}
			}
		};
		if (m_synchronous_events) {
			r.run();
		}
		else {
			// TODO use pool
			Thread t = new Thread(r);
			t.start();
		}
		return handle;
	}

	public void setConfig(TypeSpaceConfig config) {
		m_synchronous_events = config.isSynchronousEvents();
	}
}
