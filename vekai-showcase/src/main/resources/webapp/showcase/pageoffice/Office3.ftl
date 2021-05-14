<!DOCTYPE>
<html>
  <head>
  	<base href="${ctxPath}">
    <title>OFFICE-ON-LINE</title>
    <style type="text/css">
      html,body{
          width:100%;
          height:100%;
          overflow:hidden;
          margin:0;
          padding:0;
      }

      .officeObject-header{
        height:40px;
        padding-top:5px;
        padding-left:10px;
      }
      .officeObject-header button>i{
        padding-right:5px;
      }
      .officeObject-body{
          height:calc(100% - 40px);
      }

      .officeObject-ctrl-form{
          float:left;
          width:50%;
          height:100%;
      }
      
    </style>
    <link rel="stylesheet" href="${ctxPath}/plugins/bootstrap/3.3.5/css/bootstrap.min.css" />
    <script src="${ctxPath}/plugins/jquery/1.12.4/jquery.min.js" type="text/javascript"></script>
    <script src="${ctxPath}/weboffice.js" type="text/javascript"></script>
    <script src="${ctxPath}/plugins/bootstrap/3.3.5/js/bootstrap.min.js" type="text/javascript"></script>
  </head>
  <body>
    <div class="officeObject-header">
        <div class="btn-toolbar">
          <div class="btn-group">
            <button type="button" onClick="open1()"><span>文档1-打开</span></button>
            <button type="button" onClick="officeObject1.close()"><span>文档1-关闭</span></button>
            <button type="button" onClick="officeObject1.refresh()"><span>文档1-刷新</span></button>
            <button type="button" onClick="officeObject1.save()"><span>文档1-保存</span></button>
          </div>
          <div class="btn-group">
            <button type="button" onClick="open2()"><span>文档2-打开</span></button>
            <button type="button" onClick="officeObject2.close()"><span>文档2-关闭</span></button>
            <button type="button" onClick="officeObject2.refresh()"><span>文档2-刷新</span></button>
            <button type="button" onClick="officeObject2.save()"><span>文档2-保存</span></button>
          </div>          
        </div>
    </div>
    <div class="officeObject-body">
        <form id="PageOfficeForm1" class="weboffice-ctrl-form">${pageOfficeHtml1}</form>
        <form id="PageOfficeForm2" class="weboffice-ctrl-form">${pageOfficeHtml2}</form>
    </div>
  </body>
</html>
<script type="text/javascript">
var openURL1 = '${openURL1}';
var openURL2 = '${openURL2}';

var officeObject1 = WebOffice.createAt('PageOfficeForm1','PageOfficeCtrl1');
var officeObject2 = WebOffice.createAt('PageOfficeForm2','PageOfficeCtrl2');

officeObject1.addDataItem('stock','300380');
officeObject2.addDataItem('stock','300380');

function open1(){
  officeObject1.openURL(openURL1,'docNormalEdit','打开用户1');
};
//打开第一个之后再打开第二个
function onDocument1Opened(){
    open2();
};
function open2(){
    officeObject2.openURL(openURL2,'docNormalEdit','打开用户2');
};

window.onload = function(){
  //alert(open1);
  //做个半秒的延时，要不然，有时候会自动打不开
  window.setTimeout(function(){
    open1();
  },500);
  
};
</script>
