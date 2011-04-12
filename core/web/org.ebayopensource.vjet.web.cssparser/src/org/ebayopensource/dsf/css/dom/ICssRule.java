/*******************************************************************************
 * Copyright (c) 2005-2011 eBay Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.ebayopensource.dsf.css.dom;

import org.w3c.dom.DOMException;

/**
 *  The <code>CSSRule</code> interface is the abstract base interface for any 
 * type of CSS statement. This includes both rule sets and at-rules. An 
 * implementation is expected to preserve all rules specified in a CSS style 
 * sheet, even if the rule is not recognized by the parser. Unrecognized 
 * rules are represented using the <code>CSSUnknownRule</code> interface. 
 * <p>See also the <a href='http://www.w3.org/TR/2000/REC-DOM-Level-2-Style-20001113'>Document Object Model (DOM) Level 2 Style Specification</a>.
 * @since DOM Level 2
 */
public interface ICssRule {
    // RuleType
    /**
     * The rule is a <code>CSSUnknownRule</code>.
     */
    short UNKNOWN_RULE = 0;
    /**
     * The rule is a <code>CSSStyleRule</code>.
     */
    short STYLE_RULE = 1;
    /**
     * The rule is a <code>CSSCharsetRule</code>.
     */
    short CHARSET_RULE = 2;
    /**
     * The rule is a <code>CSSImportRule</code>.
     */
    short IMPORT_RULE  = 3;
    /**
     * The rule is a <code>CSSMediaRule</code>.
     */
    short MEDIA_RULE = 4;
    /**
     * The rule is a <code>CSSFontFaceRule</code>.
     */
    short FONT_FACE_RULE = 5;
    /**
     * The rule is a <code>CSSPageRule</code>.
     */
    short PAGE_RULE = 6;
    /**
     * The rule is a <code>CSSNamespaceRule</code>.
     */
    short NAMESPACE_RULE = 7;

    /**
     *  The type of the rule, as defined above. The expectation is that 
     * binding-specific casting methods can be used to cast down from an 
     * instance of the <code>CSSRule</code> interface to the specific 
     * derived interface implied by the <code>type</code>. 
     */
    short getType();

    /**
     *  The parsable textual representation of the rule. This reflects the 
     * current state of the rule and not its initial value. 
     * @exception DOMException
     *   SYNTAX_ERR: Raised if the specified CSS string value has a syntax 
     *   error and is unparsable.
     *   <br>INVALID_MODIFICATION_ERR: Raised if the specified CSS string 
     *   value represents a different type of rule than the current one.
     *   <br>HIERARCHY_REQUEST_ERR: Raised if the rule cannot be inserted at 
     *   this point in the style sheet.
     *   <br>NO_MODIFICATION_ALLOWED_ERR: Raised if the rule is readonly.
     */
    String getCssText();
    ICssRule setCssText(String cssText) throws DOMException;

    /**
     *  The style sheet that contains this rule. 
     */
    ICssStyleSheet getParentStyleSheet();

    /**
     *  If this rule is contained inside another rule (e.g. a style rule 
     * inside an @media block), this is the containing rule. If this rule is 
     * not nested inside any other rules, this returns <code>null</code>. 
     */
    ICssRule getParentRule();
}
