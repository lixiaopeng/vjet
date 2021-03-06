
vjo.ctype('vjo.java.lang.IllegalArgumentException') //< public
.needs('vjo.java.lang.Throwable')
.inherits('vjo.java.lang.RuntimeException')
.props({
    serialVersionUID:-5365630128856068164 //< private final long
})
.protos({
    //> public constructs()
    //> public constructs(String s)
    //> public constructs(String message,Throwable cause)
    //> public constructs(Throwable cause)
    constructs:function(){
        if(arguments.length===0){
            this.constructs_0_0_IllegalArgumentException_ovld();
        }else if(arguments.length===1){
            if(arguments[0] instanceof String || typeof arguments[0]=="string"){
                this.constructs_1_0_IllegalArgumentException_ovld(arguments[0]);
            }else if(vjo.java.lang.Throwable.clazz.isInstance(arguments[0])){
                this.constructs_1_1_IllegalArgumentException_ovld(arguments[0]);
            }
        }else if(arguments.length===2){
            this.constructs_2_0_IllegalArgumentException_ovld(arguments[0],arguments[1]);
        }
    },
    //> private constructs_0_0_IllegalArgumentException_ovld()
    constructs_0_0_IllegalArgumentException_ovld:function(){
        this.base();
    },
    //> private constructs_1_0_IllegalArgumentException_ovld(String s)
    constructs_1_0_IllegalArgumentException_ovld:function(s){
        this.base(s);
    },
    //> private constructs_2_0_IllegalArgumentException_ovld(String message,Throwable cause)
    constructs_2_0_IllegalArgumentException_ovld:function(message,cause){
        this.base(message,cause);
    },
    //> private constructs_1_1_IllegalArgumentException_ovld(Throwable cause)
    constructs_1_1_IllegalArgumentException_ovld:function(cause){
        this.base(cause);
    }
})
.endType();