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

</head>
<body>
<h1>Hello.v.2</h1>
<div style="width:100%;height:90%">${page_office}</div>
</body>
</html>
