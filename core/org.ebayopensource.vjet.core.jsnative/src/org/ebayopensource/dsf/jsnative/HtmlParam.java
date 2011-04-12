/*******************************************************************************
 * Copyright (c) 2005-2011 eBay Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.ebayopensource.dsf.jsnative;

import org.ebayopensource.dsf.javatojs.anno.AJavaOnly;
import org.ebayopensource.dsf.javatojs.anno.ARename;
import org.ebayopensource.dsf.jsnative.anno.Alias;
import org.ebayopensource.dsf.jsnative.anno.BrowserSupport;
import org.ebayopensource.dsf.jsnative.anno.BrowserType;
import org.ebayopensource.dsf.jsnative.anno.DOMSupport;
import org.ebayopensource.dsf.jsnative.anno.DomLevel;
import org.ebayopensource.dsf.jsnative.anno.JsMetatype;
import org.ebayopensource.dsf.jsnative.anno.Property;

/**
 * http://www.w3.org/TR/REC-html40/struct/objects.html#edef-PARAM
 *
 */
@Alias("HTMLParamElement")
@DOMSupport(DomLevel.ONE)
@JsMetatype
public interface HtmlParam extends HtmlElement {
	
	/** "data" */
	@AJavaOnly @ARename(name="'data'")
	String VALUE_TYPE_DATA = "data" ;
	/** "ref" */
	@AJavaOnly @ARename(name="'ref'")
	String VALUE_TYPE_REF = "ref" ;
	/** "object" */
	@AJavaOnly @ARename(name="'object'")
	String VALUE_TYPE_OBJECT = "object" ; // ignoreHtmlKeyword
	
	@Property String getName();
	@Property void setName(String name);

	@Property String getType();
	@Property void setType(String type);

	@Property String getValue();
	@Property void setValue(String value);

	@DOMSupport(DomLevel.ONE)
	@BrowserSupport({BrowserType.FIREFOX_2P, BrowserType.OPERA_9P, BrowserType.SAFARI_3P})
	@Property String getValueType();
	
	@DOMSupport(DomLevel.ONE)
	@BrowserSupport({BrowserType.FIREFOX_2P, BrowserType.OPERA_9P, BrowserType.SAFARI_3P})
	@Property void setValueType(String valueType);

}
