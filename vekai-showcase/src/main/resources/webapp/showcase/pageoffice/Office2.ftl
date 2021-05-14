<!DOCTYPE html>
<html>
<head>
    <title>${title}</title>
    <script src="${ctxPath}/plugins/jquery/1.12.4/jquery.min.js" type="text/javascript"></script>
    <script src="${ctxPath}/weboffice.js" type="text/javascript"></script>
    </script>
	<style type="text/css">
		*{margin:0;padding:0}
		html{width:100%;height:100%}
		body{width:100%;height:100%}
		.po-editor{width:100%;height:100% ;overflow:hidden;}
		object{width:100%;height:100%} 
	</style>
</head>
<body>
	<form id="PageOfficeForm" class="po-editor">${pageOfficeHtml}</form>
</body>
</html>
<script type="text/javascript">
var officeObject = WebOffice.createAt('PageOfficeForm','PageOfficeCtrl');
officeObject.addDataItem('banner','${banner}');
function save(){
	officeObject.save();
}

//			1：打开本地
//			3：另存为
//			4：打印
//			5：打印设置
//			6：文件属性
function localOpen(){
	officeObject.showDialog(1);
}
function localSaveAs(){
	officeObject.showDialog(3);
}
function doPrint(){
	officeObject.showDialog(4);
}
function pringSetup(){
	officeObject.showDialog(5);
}
function openFilePropertyDialog(){
	officeObject.showDialog(6);
}
function openHandDraw(){
	officeObject.handDraw();
}
function showHandDraw(){
	officeObject.whowHandDraw();
}
function printPreview(){
	officeObject.printPreview();
}
function fullScreen(){
	officeObject.toggleFullScreen();
}
function hiddenRevisions(){
	officeObject.hiddenRevisions();
}
function toggleRevisions(){
	officeObject.toggleRevisions();
}

</script>
