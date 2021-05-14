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
	<#if impFlag??>
		<div style="color:Red">请导入“/docs/CASE-D40.doc”下的ImportWord.doc文档查看演示效果。</div>
	</#if>
	<form id="PageOfficeForm" class="po-editor">${pageOfficeHtml}</form>
</body>
</html>
<script type="text/javascript">
var officeObject = WebOffice.createAt('PageOfficeForm','PageOfficeCtrl');
function showDocR1() {
    document.getElementById("PageOfficeCtrl").Document.ActiveWindow.View.ShowRevisionsAndComments = false;
    document.getElementById("PageOfficeCtrl").Document.ActiveWindow.View.RevisionsView = 1;
}
function showDocR2() {
    document.getElementById("PageOfficeCtrl").Document.ActiveWindow.View.ShowRevisionsAndComments = false;
    document.getElementById("PageOfficeCtrl").Document.ActiveWindow.View.RevisionsView = 0;
}
function showDocCompare() {
    document.getElementById("PageOfficeCtrl").Document.ActiveWindow.View.ShowRevisionsAndComments = true;
    document.getElementById("PageOfficeCtrl").Document.ActiveWindow.View.RevisionsView = 0;
}
function importData() {
	document.getElementById("PageOfficeCtrl").WordImportDialog();
}

function submitData() {
	document.getElementById("PageOfficeCtrl").WebSave();

}
</script>
