vjo.ctype('org.ebayopensource.dsf.jst.validation.vjo.typecheck.array.CorrectArray1')
.props({
	//> public void main(String[] args)
	main:function(args){
		var days = new String[]{"1","2","3"};
		document.writeln('Demonstrating for loop');
		//snippet.forloop.begin
		for(var i in days)
		{
			document.writeln('Day at ' + i + ' is ' ) ;
		}
		//snippet.forloop.end
		
		document.writeln('Demonstrating for each loop');
	}
})
.endType();