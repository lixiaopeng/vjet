vjo.ctype('syntax.generic.superType.SubType30') //< public
//>needs(syntax.generic.Collection)
//>needs(syntax.generic.ArrayList)
//>needs(syntax.generic.AbstractList)
.props({  
})
.protos({
	
	//>public void test(Collection<? super AbstractList>) 
	test : function(c){
		var s1 = null;//<vjo.Object         
		this.sort(c);         
	},                 
	
	//>public void sort(Collection<? super ArrayList>) 
	sort : function(c){
		  
	}
}) 
.endType();