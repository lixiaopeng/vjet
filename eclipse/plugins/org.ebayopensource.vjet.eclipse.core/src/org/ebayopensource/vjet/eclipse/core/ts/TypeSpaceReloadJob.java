/*******************************************************************************
 * Copyright (c) 2005-2011 eBay Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.ebayopensource.vjet.eclipse.core.ts;

import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.dltk.mod.core.DLTKCore;

import org.ebayopensource.vjo.tool.typespace.TypeSpaceMgr;

/**
 * Job to reloading all types to the type space
 * 
 * 
 *
 */
public class TypeSpaceReloadJob extends WorkspaceJob {

	private static final String TYPE_SPACE_RELOAD = "type space reload";

	public TypeSpaceReloadJob() {
		super(TYPE_SPACE_RELOAD);
	}

	@Override
	public IStatus runInWorkspace(IProgressMonitor monitor)
			throws CoreException {
		TypeSpaceMgr mgr = TypeSpaceMgr.getInstance();
		try {
			TypeSpaceTracer.loadReloadEvent(mgr);
			mgr.reload(new EclipseTypeLoadMonitor(monitor), null);
		} catch (Exception e) {
			DLTKCore.error(e.getMessage(), e);
		}
		return Status.OK_STATUS;
	}

}
