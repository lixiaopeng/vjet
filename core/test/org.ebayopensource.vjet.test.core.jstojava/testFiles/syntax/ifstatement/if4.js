vjo.ctype('syntax.ifstatement.if4')
.protos({

x : 0, //< public int
//> public Number
y : undefined, 

z : undefined,//<public String

//> public void getIsle(int val1, int val2)
getIsle : function (val1, val2)
{
if (val1 > val2)
{
}
else if (val1 < val2)
{
}
},
//> public int getX()
getX : function()
{
return this.x;
},
//> public Number getY()
getY : function()
{
return this.y;
},


//> public String getZ()
getZ : function()
{
return this.z;
}
})
.endType();