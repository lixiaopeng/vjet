vjo.ctype("org.ebayopensource.dsf.jst.validation.vjo.typecheck.array.ArrayIDTester1")
.protos({

    //> public int someMethod()
    someMethod:function(){
    	var myCars = new Array(3); //< int[]
        myCars[0] = 1;
        myCars[1] = 2;
        myCars[2] = 3;
        return myCars[0];
    },
    
     //> public String accessArrayElement2()
    accessArrayElement2:function(){
        var monthArray = new Array(4); //< String[]
        monthArray[0] = "Jan";
        monthArray[1] = "Twelth";
        monthArray[3] = "April";
        return monthArray[1];
    }
})
.endType();