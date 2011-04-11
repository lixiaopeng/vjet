/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.mod.internal.core;

import org.eclipse.dltk.mod.core.IModelElement;
import org.eclipse.dltk.mod.core.IModelStatus;
import org.eclipse.dltk.mod.core.IModelStatusConstants;
import org.eclipse.dltk.mod.core.ISourceReference;
import org.eclipse.dltk.mod.core.ModelException;
import org.eclipse.dltk.mod.internal.core.util.Messages;

/**
 * This operation renames elements.
 * 
 * <p>
 * Notes:
 * <ul>
 * <li>Resource rename is not supported - this operation only renames elements
 * contained in compilation units.
 * <li>When a main type is renamed, its compilation unit and constructors are
 * renamed.
 * <li>Constructors cannot be renamed.
 * </ul>
 */
public class RenameElementsOperation extends MoveElementsOperation {
	/**
	 * When executed, this operation will rename the specified elements with the
	 * given names in the corresponding destinations.
	 */
	public RenameElementsOperation(IModelElement[] elements, IModelElement[] destinations, String[] newNames, boolean force) {
		// a rename is a move to the same parent with a new name specified
		// these elements are from different parents
		super(elements, destinations, force);
		setRenamings(newNames);
	}

	/**
	 * @see MultiOperation
	 */
	protected String getMainTaskName() {
		return Messages.operation_renameElementProgress;
	}

	/**
	 * @see CopyElementsOperation#isRename()
	 */
	protected boolean isRename() {
		return true;
	}

	/**
	 * @see MultiOperation
	 */
	protected IModelStatus verify() {
		IModelStatus status = super.verify();
		if (!status.isOK())
			return status;
		if (this.renamingsList == null || this.renamingsList.length == 0)
			return new ModelStatus(IModelStatusConstants.NULL_NAME);
		return ModelStatus.VERIFIED_OK;
	}

	/**
	 * @see MultiOperation
	 */
	protected void verify(IModelElement element) throws ModelException {
		if( element == null ) {
			error(IModelStatusConstants.NULL_NAME, null);
		}
		int elementType = element.getElementType();
		if (element == null || !element.exists())
			error(IModelStatusConstants.ELEMENT_DOES_NOT_EXIST, element);
		if (element.isReadOnly())
			error(IModelStatusConstants.READ_ONLY, element);
		if (!(element instanceof ISourceReference))
			error(IModelStatusConstants.INVALID_ELEMENT_TYPES, element);
		if (elementType < IModelElement.TYPE /*|| elementType == IModelElement.INITIALIZER*/)
			error(IModelStatusConstants.INVALID_ELEMENT_TYPES, element);
		verifyRenaming(element);
	}
}