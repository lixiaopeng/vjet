/*******************************************************************************
 * Copyright (c) 2005-2011 eBay Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
/*
 * ChildSelectorImpl.java
 *
 * Modified from Open-Source CSS Parser (Steady State Software) under
 * GNU Lesser General Public License.
 * 
 */

package org.ebayopensource.dsf.css.parser.selectors;

import java.io.Serializable;

import org.ebayopensource.dsf.css.sac.IDescendantSelector;
import org.ebayopensource.dsf.css.sac.ISelector;
import org.ebayopensource.dsf.css.sac.ISimpleSelector;

public class DChildSelector implements IDescendantSelector, Serializable {

	private ISelector m_parent;
	private ISimpleSelector m_simpleSelector;

	public DChildSelector(ISelector parent, ISimpleSelector simpleSelector) {
		m_parent = parent;
		m_simpleSelector = simpleSelector;
	}

	public short getSelectorType() {
		return ISelector.SAC_CHILD_SELECTOR;
	}

	public ISelector getAncestorSelector() {
		return m_parent;
	}
	
	public void setAncestorSelector(ISelector parent) {
		m_parent = parent;
	}

	public ISimpleSelector getSimpleSelector() {
		return m_simpleSelector;
	}

	public String toString() {
		return m_parent.toString() + ">" + m_simpleSelector.toString();
	}
	
	public Object clone() throws CloneNotSupportedException {
		DChildSelector clone = (DChildSelector)super.clone();
		clone.m_parent = (ISelector)m_parent.clone();
		clone.m_simpleSelector = (ISimpleSelector)m_simpleSelector.clone();
		return clone;
	}
}
