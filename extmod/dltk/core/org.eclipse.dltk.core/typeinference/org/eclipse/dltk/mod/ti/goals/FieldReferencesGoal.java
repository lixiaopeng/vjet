/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *

 *******************************************************************************/
package org.eclipse.dltk.mod.ti.goals;

import org.eclipse.dltk.mod.ti.IContext;

/**
 * Task of this goal is to find all references to the given field or variable.
 * Evaluator for this goal is registered by default and uses standard DLTK
 * search. Cause without full type information it is impossible to get fully
 * correct results, goal will send FieldPositionVerificationGoals. So, to get
 * them working user should register appropriate evaluator.
 *
 * Result of this goal is array of ItemReference objects
 */
public class FieldReferencesGoal extends AbstractReferencesGoal {

	public FieldReferencesGoal(IContext context, String name, String parentKey) {
		super(context, name, parentKey);
	}

}
