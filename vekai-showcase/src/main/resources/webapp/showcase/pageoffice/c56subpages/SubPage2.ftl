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
officeObject.addDataItem('stock','300380');
function openFile(){
	officeObject.openURL('${openURL2}','docNormalEdit','安硕用户2');
}
</script>
