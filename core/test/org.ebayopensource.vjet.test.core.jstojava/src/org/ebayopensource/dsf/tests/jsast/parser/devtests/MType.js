vjo.mtype("com.ebay.dsf.tests.jsast.parser.devtests.MType")
.props({
	//>public String s_mixinPropOne
	s_mixinPropOne : "mixinPropOne",
	
	//>public void mixinStaticMethod()
	mixinStaticMethod : function () {
		alert("mixinStaticMethod");
	}
})
.protos({
	//>public String s_mixinPropOne
	mixinPropOne : "mixin instance property",

	//>public void mixinInstanceMethod()
	mixinInstanceMethod : function () {
		alert("mixinInstanceMethod");
	}
	
}).endType();