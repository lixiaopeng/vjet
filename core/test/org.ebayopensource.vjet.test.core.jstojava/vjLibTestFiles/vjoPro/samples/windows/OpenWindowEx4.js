vjo.ctype('vjoPro.samples.windows.OpenWindowEx4') //< public
.needs('vjoPro.dsf.window.utils.VjWindow')
.props({
/**
* @return boolean
* @access public
*
*/
//> public boolean openWin()
openWin:function(){
vjoPro.dsf.window.utils.VjWindow.open("http://www.ebay.com", "testWin", "width=400,height=600,location=no,menubar=no,scrollbars=no,status=yes", false, true, 400, 600);
return false;
}

})
.endType();
