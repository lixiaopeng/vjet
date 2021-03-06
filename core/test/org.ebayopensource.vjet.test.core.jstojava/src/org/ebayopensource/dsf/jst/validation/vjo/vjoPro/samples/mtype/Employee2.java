/*******************************************************************************
 * Copyright (c) 2005-2011 eBay Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
/* 
 * $Id: Employee2.java.java, Jun 21, 2009, 12:20:41 AM, liama. Exp$:
 * Copyright (c) 2006-2009 Ebay Technologies. All Rights Reserved.
 * This software program and documentation are copyrighted by Ebay
 * Technologies.
 */
package org.ebayopensource.dsf.jst.validation.vjo.vjoPro.samples.mtype;
import static com.ebay.junitnexgen.category.Category.Groups.FAST;
import static com.ebay.junitnexgen.category.Category.Groups.P3;
import static com.ebay.junitnexgen.category.Category.Groups.UNIT;

import java.util.List;

import org.ebayopensource.dsf.jsgen.shared.ids.FieldProbIds;
import org.ebayopensource.dsf.jsgen.shared.ids.MethodProbIds;
import org.ebayopensource.dsf.jsgen.shared.validation.vjo.VjoSemanticProblem;
import org.ebayopensource.dsf.jst.validation.vjo.VjoValidationBaseTester;
import org.junit.Before;
import org.junit.Test;

import com.ebay.junitnexgen.category.Category;
import com.ebay.junitnexgen.category.Description;
import com.ebay.junitnexgen.category.ModuleInfo;

/**
 * Employee2.java
 * 
 * @author <a href="mailto:liama@ebay.com">liama</a>
 * @since JDK 1.5
 */
@Category( { P3, FAST, UNIT })
@ModuleInfo(value="DsfPrebuild",subModuleId="JsToJava")
public class Employee2 extends VjoValidationBaseTester {

    @Before
    public void setUp() {
        expectProblems.clear();
    }

    @Test
    @Category( { P3, FAST, UNIT })
    @Description("To test VjoPro project false positive")
    public void testEmployee2() {
//        expectProblems.add(createNewProblem(TypeProbIds.MixinExpectsMustBeSatisfied, 1, 0));
        expectProblems.add(createNewProblem(MethodProbIds.ShouldReturnValue,26,0));
        List<VjoSemanticProblem> problems = getVjoSemanticProblem(
                "vjoPro.samples.mtype.", "Employee2.js", this.getClass());
        assertProblemEquals(expectProblems, problems);
    }

    @Test
    @Category( { P3, FAST, UNIT })
    @Description("To test VjoPro project false positive")
    public void testEmployee8() {
        expectProblems.clear();
        expectProblems.add(createNewProblem(FieldProbIds.AmbiguousField, 3, 0));
        expectProblems.add(createNewProblem(MethodProbIds.AmbiguousMethod, 6, 0));
        expectProblems.add(createNewProblem(MethodProbIds.AmbiguousMethod, 13, 0));
        List<VjoSemanticProblem> problems = getVjoSemanticProblem(
                "vjoPro.samples.mtype.", "Employee8.js", this.getClass());
        assertProblemEquals(expectProblems, problems);
    }

    @Test
    @Category( { P3, FAST, UNIT })
    @Description("To test VjoPro project false positive")
    public void testEmployee9() {
        expectProblems.clear();
        List<VjoSemanticProblem> problems = getVjoSemanticProblem(
                "vjoPro.samples.mtype.", "Employee9.js", this.getClass());
        assertProblemEquals(expectProblems, problems);
    }
}
