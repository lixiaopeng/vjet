/*******************************************************************************
 * Copyright (c) 2005-2011 eBay Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.ebayopensource.vjet.eclipse.internal.ui.typehierarchy;

import org.eclipse.dltk.mod.ui.DLTKPluginImages;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.custom.BusyIndicator;

/**
 * Action enable / disable member filtering
 */
public class EnableMemberFilterAction extends Action {

	private VJOTypeHierarchyViewPart fView;	
	
	public EnableMemberFilterAction(VJOTypeHierarchyViewPart v, boolean initValue) {
		super(TypeHierarchyMessages.EnableMemberFilterAction_label); 
		setDescription(TypeHierarchyMessages.EnableMemberFilterAction_description); 
		setToolTipText(TypeHierarchyMessages.EnableMemberFilterAction_tooltip); 
		
		DLTKPluginImages.setLocalImageDescriptors(this, "impl_co.gif"); //$NON-NLS-1$

		fView= v;
		setChecked(initValue);
		
		//PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IJavaHelpContextIds.ENABLE_METHODFILTER_ACTION);
		
	}

	/*
	 * @see Action#actionPerformed
	 */		
	public void run() {
		BusyIndicator.showWhile(fView.getSite().getShell().getDisplay(), new Runnable() {
			public void run() {
				fView.enableMemberFilter(isChecked());
			}
		});
	}
}
