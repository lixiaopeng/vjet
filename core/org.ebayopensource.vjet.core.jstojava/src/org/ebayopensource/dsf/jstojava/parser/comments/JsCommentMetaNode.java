/*******************************************************************************
 * Copyright (c) 2005-2011 eBay Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.ebayopensource.dsf.jstojava.parser.comments;

import org.ebayopensource.dsf.jst.meta.BaseJsCommentMetaNode;
import org.ebayopensource.dsf.jst.traversal.IJstNodeVisitor;

/**
 * 
 *
 */
public class JsCommentMetaNode 
	extends BaseJsCommentMetaNode<IJsCommentMeta> {

	private static final long serialVersionUID = 1L;
	
	@Override
	public void accept(IJstNodeVisitor visitor){
		visitor.visit(this);
	}
}
