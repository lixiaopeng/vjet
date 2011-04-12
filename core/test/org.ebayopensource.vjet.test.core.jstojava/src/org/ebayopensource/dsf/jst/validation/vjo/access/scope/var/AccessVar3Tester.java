/*******************************************************************************
 * Copyright (c) 2005-2011 eBay Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
/* 
 * $Id: AccessVarCorrect1Tester.java, May 26, 2009, 9:18:36 PM, liama. Exp$
 *
 * Copyright (c) 2006-2009 Ebay Technologies. All Rights Reserved.
 * This software program and documentation are copyrighted by Ebay 
 * Technologies.
 */
package org.ebayopensource.dsf.jst.validation.vjo.access.scope.var;

import static com.ebay.junitnexgen.category.Category.Groups.FAST;
import static com.ebay.junitnexgen.category.Category.Groups.P1;
import static com.ebay.junitnexgen.category.Category.Groups.UNIT;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import org.ebayopensource.dsf.jsgen.shared.ids.FieldProbIds;
import org.ebayopensource.dsf.jsgen.shared.ids.VarProbIds;
import org.ebayopensource.dsf.jsgen.shared.validation.vjo.VjoSemanticProblem;
import org.ebayopensource.dsf.jst.validation.vjo.VjoValidationBaseTester;
import com.ebay.junitnexgen.category.Category;
import com.ebay.junitnexgen.category.Description;

/**
 * Access var correct 1tester
 *
 * @author <a href="mailto:liama@ebay.com">liama</a>
 * @since JDK 1.5
 */
@Category( { P1, FAST, UNIT })
public class AccessVar3Tester extends VjoValidationBaseTester {

    @Before
    public void setUp() {
        expectProblems.clear();
        expectProblems.add(createNewProblem(VarProbIds.UndefinedName, 17, 0));
        expectProblems.add(createNewProblem(FieldProbIds.UndefinedField, 18, 0));
        expectProblems.add(createNewProblem(FieldProbIds.UndefinedField, 19, 0));
        expectProblems.add(createNewProblem(VarProbIds.UndefinedName, 20, 0));
    }
    
    @Test
    @Category( { P1, FAST, UNIT })
    @Description("Test access different socpe's variable")
    public void testAccesVisible1() {
        List<VjoSemanticProblem> problems = getVjoSemanticProblem(
                "access.scope.var.","AccessVar3Tester.js", this
                        .getClass());
        assertProblemEquals(expectProblems, problems);
    }
    
}

