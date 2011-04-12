vjo.ctype("org.ebayopensource.dsf.jst.validation.vjo.vjNS.CTypeCorrect")
.needs(["org.ebayopensource.dsf.jst.validation.vjo.vjNS.ITypeCorrect"])
.satisfies("org.ebayopensource.dsf.jst.validation.vjo.vjNS.ITypeCorrect")
.protos({
	
    property : 10, //<int

	//> public constructs()
	constructs: function(){
		this.property = 20;
	},

	//> public void doIt()
	doIt : function () {
	   alert(this.property);
	   
	   var clz = this.vj$.ITypeCorrect;
	}
})
.endType();