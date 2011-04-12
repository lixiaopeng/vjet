/*******************************************************************************
 * Copyright (c) 2005-2011 eBay Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.ebayopensource.dsf.css.dom;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DOMException;

/**
 *  This interface allows the DOM user to create a <code>CSSStyleSheet</code> 
 * outside the context of a document. There is no way to associate the new 
 * <code>CSSStyleSheet</code> with a document in DOM Level 2. 
 * <p>See also the <a href='http://www.w3.org/TR/2000/REC-DOM-Level-2-Style-20001113'>Document Object Model (DOM) Level 2 Style Specification</a>.
 * @since DOM   Level 2
 */
public interface IDomImplementationCss extends DOMImplementation {
    /**
     * Creates a new <code>CSSStyleSheet</code>.
     * @param title The advisory title. See also the  section. 
     * @param media The comma-separated list of media associated with the new 
     *   style sheet. See also the  section. 
     * @return A new CSS style sheet.
     * @exception DOMException
     *    SYNTAX_ERR: Raised if the specified media string value has a syntax 
     *   error and is unparsable. 
     */
    ICssStyleSheet createCSSStyleSheet(String title, String media)
		throws DOMException;
}
