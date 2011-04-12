/**
* Type: vjo.samples.fundamentals.CTypeExample1
* Description:
*/
vjo.ctype("syntax.abstraceClass.CTypeExample2")
.inherits("syntax.abstraceClass.AbstractExample1")
.props({
	s_compoundedRate: undefined //< public double
})
.protos({
	m_enrolled: false, 		//< protected boolean
	
	showAlert1 : function(msg){ //< public void showAlert1(String msg)
		alert("Greeting from instance method of subclass: "+msg);
	},
	
	showAlert : function(greeting){ //< public void showAlert(String greeting)
		alert("Greeting from instance method of subclass: "+greeting);
	}
})
.inits(
	function(){
		/* use this section if you require static initialization */
		this.s_compoundedRate = 0.75;
	}
)
.endType();
