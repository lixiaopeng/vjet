/*******************************************************************************
 * Copyright (c) 2005-2011 eBay Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.ebayopensource.dsf.jsnative;

import org.ebayopensource.dsf.jsnative.anno.Alias;
import org.ebayopensource.dsf.jsnative.anno.DOMSupport;
import org.ebayopensource.dsf.jsnative.anno.DomLevel;
import org.ebayopensource.dsf.jsnative.anno.JsMetatype;

/**
 * http://www.htmlref.com/Reference/AppA/tag_wbr.htm
 *
 */
@Alias("HTMLWbrElement")
@DOMSupport(DomLevel.ONE)
@JsMetatype
public interface HtmlWbr extends HtmlElement {

}
