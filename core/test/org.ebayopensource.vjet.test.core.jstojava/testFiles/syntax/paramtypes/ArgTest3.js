vjo.ctype('syntax.paramtypes.ArgTest3')
.protos({
	m_name:undefined, //< private String

	//> private void setName(String... name)
	setName: function(name) {
		this.m_name = name;
	},
	
		
	//> public void init1()
	init1 : function(){
		var pros = new Array(10,20,30);
		this.setName(pros);
	}
			
})
.endType();