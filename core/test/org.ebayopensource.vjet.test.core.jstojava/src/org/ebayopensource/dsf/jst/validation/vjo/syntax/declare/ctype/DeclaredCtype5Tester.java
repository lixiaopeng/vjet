/*******************************************************************************
 * Copyright (c) 2005-2011 eBay Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
/* 
 * $Id: DeclaredCtypeCorrect3Tester.java, May 31, 2009, 10:01:50 PM, liama. Exp$
 *
 * Copyright (c) 2006-2009 Ebay Technologies. All Rights Reserved.
 * This software program and documentation are copyrighted by Ebay 
 * Technologies.
 */
package org.ebayopensource.dsf.jst.validation.vjo.syntax.declare.ctype;
import com.ebay.junitnexgen.category.ModuleInfo;

import static com.ebay.junitnexgen.category.Category.Groups.FAST;
import static com.ebay.junitnexgen.category.Category.Groups.P3;
import static com.ebay.junitnexgen.category.Category.Groups.UNIT;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import org.ebayopensource.dsf.jsgen.shared.ids.MethodProbIds;
import org.ebayopensource.dsf.jsgen.shared.validation.vjo.VjoSemanticProblem;
import org.ebayopensource.dsf.jst.validation.vjo.VjoValidationBaseTester;
import com.ebay.junitnexgen.category.Category;
import com.ebay.junitnexgen.category.Description;

/**
 * Declared ctype correct include inits
 *
 * @author <a href="mailto:liama@ebay.com">liama</a>
 * @since JDK 1.5
 */
@ModuleInfo(value="DsfPrebuild",subModuleId="JsToJava")
@Category( { P3, FAST, UNIT })
public class DeclaredCtype5Tester extends VjoValidationBaseTester {

    @Before
    public void setUp(){
        expectProblems.clear();
        expectProblems.add(createNewProblem(MethodProbIds.UndefinedMethod, 8, 0));
// Due to fixes in the translator the field will exist but the section is invalid
        // if the order is changed then there will be no issue
        
        //        expectProblems.add(createNewProblem(FieldProbIds.UndefinedField, 4, 0));
    }

    @Test
    @Category( { P3, FAST, UNIT })
    @Description("Test declared ctype with blocks different sequence")
    public void testCTypes() {
        List<VjoSemanticProblem> problems = getVjoSemanticProblem(
                "syntax.declare.ctype.","CtypeSequence2.js", this.getClass());
        assertProblemEquals(expectProblems, problems);
    }
}
