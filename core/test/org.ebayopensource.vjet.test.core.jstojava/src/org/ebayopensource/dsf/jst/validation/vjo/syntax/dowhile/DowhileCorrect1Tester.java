/*******************************************************************************
 * Copyright (c) 2005-2011 eBay Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
/* 
 * $Id: DowhileCorrect1Tester.java, May 18, 2009, 6:14:16 PM, liama. Exp$
 *
 * Copyright (c) 2006-2009 Ebay Technologies. All Rights Reserved.
 * This software program and documentation are copyrighted by Ebay 
 * Technologies.
 */
package org.ebayopensource.dsf.jst.validation.vjo.syntax.dowhile;
import com.ebay.junitnexgen.category.ModuleInfo;

import static com.ebay.junitnexgen.category.Category.Groups.FAST;
import static com.ebay.junitnexgen.category.Category.Groups.P3;
import static com.ebay.junitnexgen.category.Category.Groups.UNIT;

import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import org.ebayopensource.dsf.jsgen.shared.validation.vjo.VjoSemanticProblem;
import org.ebayopensource.dsf.jst.validation.vjo.VjoValidationBaseTester;
import com.ebay.junitnexgen.category.Category;

/**
 * For test do while correct 1
 *
 * @author <a href="mailto:liama@ebay.com">liama</a>
 * @since JDK 1.5
 */
@ModuleInfo(value="DsfPrebuild",subModuleId="JsToJava")
@Category( { P3, FAST, UNIT })
public class DowhileCorrect1Tester extends VjoValidationBaseTester{

    @Before
    public void setUp(){
        expectProblems.clear();
    }

    @Test
    @Ignore
    @Category( { P3, FAST, UNIT })
    public void testIfstatement1() {
        List<VjoSemanticProblem> problems = getVjoSemanticProblem(
                "syntax.dowhile/",
                "dowhileCorrect1.js", this.getClass());
        assertProblemEquals(expectProblems, problems);
    }
    
}
