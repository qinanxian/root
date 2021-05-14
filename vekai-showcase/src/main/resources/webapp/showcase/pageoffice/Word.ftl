<!DOCTYPE html>
<html>
<head>
    <title>Hello World!</title>
    <script type="text/javascript">
        function Save() {
            document.getElementById("PageOfficeCtrl").WebSave();
        }

        function SaveAsPDF() {
            document.getElementById("PageOfficeCtrl").WebSaveAsPDF();
        }

        function PrintSet() {
            document.getElementById("PageOfficeCtrl").ShowDialog(5);
        }

        function PrintFile() {
            document.getElementById("PageOfficeCtrl").ShowDialog(4);
        }

        function Close() {
            window.external.close();
        }

        function IsFullScreen() {
            document.getElementById("PageOfficeCtrl").FullScreen = !document.getElementById("PageOfficeCtrl").FullScreen;
        }

        //文档关闭前先提示用户是否保存
        function BeforeBrowserClosed() {
            if (document.getElementById("PageOfficeCtrl").IsDirty) {
                if (confirm("提示：文档已被修改，是否继续关闭放弃保存 ？")) {
                    return true;

                } else {

                    return false;
                }

            }

        }
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
	<div class="po-editor">${pageOfficeHtml}</div>
</body>
</html>
