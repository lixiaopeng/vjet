/*******************************************************************************
 * Copyright (c) 2005-2011 eBay Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.ebayopensource.dsf.jst.validation.vjo.rt.otype;
import static com.ebay.junitnexgen.category.Category.Groups.FAST;
import static com.ebay.junitnexgen.category.Category.Groups.P1;
import static com.ebay.junitnexgen.category.Category.Groups.P2;
import static com.ebay.junitnexgen.category.Category.Groups.P3;
import static com.ebay.junitnexgen.category.Category.Groups.UNIT;

import java.util.List;

import org.ebayopensource.dsf.jsgen.shared.ids.MethodProbIds;
import org.ebayopensource.dsf.jsgen.shared.ids.TypeProbIds;
import org.ebayopensource.dsf.jsgen.shared.ids.VjoSyntaxProbIds;
import org.ebayopensource.dsf.jsgen.shared.validation.vjo.VjoSemanticProblem;
import org.ebayopensource.dsf.jst.validation.vjo.VjoValidationBaseTester;
import org.junit.Test;

import com.ebay.junitnexgen.category.Category;
import com.ebay.junitnexgen.category.Description;
import com.ebay.junitnexgen.category.ModuleInfo;

@Category({P1,FAST,UNIT})
@ModuleInfo(value="DsfPrebuild",subModuleId="JsToJava")
public class VjoOTypeTest extends VjoValidationBaseTester{
	List<VjoSemanticProblem> actualProblems = null;
	
	@Test @Category({P1,FAST,UNIT})
	@Description("Sanity Test, OType defined properly should not produce validation error/warning")
	public void testOType() throws Exception {
		expectProblems.clear();
		actualProblems = getVjoSemanticProblem("org.ebayopensource.dsf.jst.validation.vjo.rt.otype.", "OType.js", this.getClass());
		assertProblemEquals(expectProblems, actualProblems);
	}
	
	
	@Test @Category({P3,FAST,UNIT})
	@Description("Test OType can have only defs and endType sections")
	public void testBadOType1() throws Exception {
		expectProblems.clear();
		expectProblems.add(createNewProblem(MethodProbIds.UndefinedMethod,2,0)); 
		actualProblems = getVjoSemanticProblem("org.ebayopensource.dsf.jst.validation.vjo.rt.otype.", "BadOType1.js", this.getClass());
		assertProblemEquals(expectProblems, actualProblems);
	}
	
	@Test @Category({P3,FAST,UNIT})
	@Description("Test OType can have only defs and endType sections")
	public void testBadOType2() throws Exception {
		expectProblems.clear();
		expectProblems.add(createNewProblem(MethodProbIds.UndefinedMethod,2,0)); 
		actualProblems = getVjoSemanticProblem("org.ebayopensource.dsf.jst.validation.vjo.rt.otype.", "BadOType2.js", this.getClass());
		assertProblemEquals(expectProblems, actualProblems);
	}
	
	@Test @Category({P3,FAST,UNIT})
	@Description("Test OType can have only defs and endType sections")
	public void testBadOType3() throws Exception {
		expectProblems.clear();
		expectProblems.add(createNewProblem(MethodProbIds.UndefinedMethod,2,0)); 
		expectProblems.add(createNewProblem(VjoSyntaxProbIds.OTypeWithNoneObjLiteralProperty,3,0)); 
		expectProblems.add(createNewProblem(TypeProbIds.UnusedActiveNeeds,1,0));
		actualProblems = getVjoSemanticProblem("org.ebayopensource.dsf.jst.validation.vjo.rt.otype.", "BadOType3.js", this.getClass());
		assertProblemEquals(expectProblems, actualProblems);
	}
	
	@Test @Category({P3,FAST,UNIT})
	@Description("Test OType can have only defs and endType sections")
	public void testBadOType4() throws Exception {
		expectProblems.clear();
		expectProblems.add(createNewProblem(MethodProbIds.UndefinedMethod,2,0)); 
		actualProblems = getVjoSemanticProblem("org.ebayopensource.dsf.jst.validation.vjo.rt.otype.", "BadOType4.js", this.getClass());
		assertProblemEquals(expectProblems, actualProblems);
	}
	
	@Test @Category({P3,FAST,UNIT})
	@Description("Test OType can have only defs and endType sections")
	public void testBadOType5() throws Exception {
		expectProblems.clear();
		expectProblems.add(createNewProblem(MethodProbIds.UndefinedMethod,2,0)); 
		actualProblems = getVjoSemanticProblem("org.ebayopensource.dsf.jst.validation.vjo.rt.otype.", "BadOType5.js", this.getClass());
		assertProblemEquals(expectProblems, actualProblems);
	}
		
	@Test @Category({P3,FAST,UNIT})
	@Description("Test OType can have only defs and endType sections")
	public void testBadOType7() throws Exception {
		expectProblems.clear();
		expectProblems.add(createNewProblem(MethodProbIds.UndefinedMethod,2,0)); 
		actualProblems = getVjoSemanticProblem("org.ebayopensource.dsf.jst.validation.vjo.rt.otype.", "BadOType7.js", this.getClass());
		assertProblemEquals(expectProblems, actualProblems);
	}
	
	@Test @Category({P3,FAST,UNIT})
	@Description("Test OType can have only defs and endType sections")
	public void testBadOType8() throws Exception {
		expectProblems.clear();
		expectProblems.add(createNewProblem(MethodProbIds.UndefinedMethod,2,0)); 
		actualProblems = getVjoSemanticProblem("org.ebayopensource.dsf.jst.validation.vjo.rt.otype.", "BadOType8.js", this.getClass());
		assertProblemEquals(expectProblems, actualProblems);
	}
	
	@Test @Category({P3,FAST,UNIT})
	@Description("Test OType can have only defs and endType sections")
	public void testBadOType9() throws Exception {
		expectProblems.clear();
		expectProblems.add(createNewProblem(MethodProbIds.UndefinedMethod,2,0)); 
		actualProblems = getVjoSemanticProblem("org.ebayopensource.dsf.jst.validation.vjo.rt.otype.", "BadOType9.js", this.getClass());
		assertProblemEquals(expectProblems, actualProblems);
	}

	@Test @Category({P1,FAST,UNIT})
	@Description("Sanity Test, OType should be allowed to be nested in other meta types, except for otype themselves")
	public void testOTypeNested() throws Exception {
		expectProblems.clear();
		expectProblems.add(createNewProblem(VjoSyntaxProbIds.OTypeWithInnerTypes, 1, 0));
		
		actualProblems = getVjoSemanticProblem("org.ebayopensource.dsf.jst.validation.vjo.rt.otype.", "OTypeNested.js", this.getClass());
		assertProblemEquals(expectProblems, actualProblems);
	}
	
	@Test @Category({P2,FAST,UNIT})
	@Description("Test OType can have only defs and endType sections")
	public void testOTypeUser() throws Exception {
		expectProblems.clear();
		expectProblems.add(createNewProblem(TypeProbIds.IncompatibleTypesInEqualityOperator, 32, 0));
		expectProblems.add(createNewProblem(TypeProbIds.IncompatibleTypesInEqualityOperator, 33, 0));
		expectProblems.add(createNewProblem(TypeProbIds.IncompatibleTypesInEqualityOperator, 40, 0));
		expectProblems.add(createNewProblem(TypeProbIds.IncompatibleTypesInEqualityOperator, 48, 0));
		expectProblems.add(createNewProblem(TypeProbIds.IncompatibleTypesInEqualityOperator, 55, 0));
		expectProblems.add(createNewProblem(TypeProbIds.IncompatibleTypesInEqualityOperator, 67, 0));
		
		actualProblems = getVjoSemanticProblem("org.ebayopensource.dsf.jst.validation.vjo.rt.otype.", "OTypeUser.js", this.getClass());
		assertProblemEquals(expectProblems, actualProblems);
	}
	
	@Test @Category({P2,FAST,UNIT})
	@Description("Test nested otypes, callbacks")
	public void testOTypeAsCallbacks() throws Exception {
		expectProblems.clear();
		expectProblems.add(createNewProblem(MethodProbIds.UndefinedMethod, 8, 0));
		expectProblems.add(createNewProblem(MethodProbIds.ParameterMismatch, 9, 0));
		expectProblems.add(createNewProblem(MethodProbIds.UndefinedMethod, 14, 0));
		expectProblems.add(createNewProblem(MethodProbIds.ParameterMismatch, 15, 0));
		
		actualProblems = getVjoSemanticProblem("org.ebayopensource.dsf.jst.validation.vjo.rt.otype.", "CallbackClient.js", this.getClass());
		assertProblemEquals(expectProblems, actualProblems);
	}
}
