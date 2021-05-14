<!DOCTYPE>
<html>
  <head>
    <title>OFFICE-ON-LINE</title>
    <style type="text/css">
      html,body{
          width:100%;
          height:100%;
          overflow:hidden; 
          margin:0;
          padding:0;
      }

      .weboffice-header{
        height:40px;
        padding-top:5px;
        padding-left:10px;
      }
      .weboffice-header button>i{
        padding-right:5px;
      }
      .weboffice-body{
          height:calc(100% - 40px);
      }

      .weboffice-ctrl-form{
          float:left;
          width:50%;
          height:100%;
      }
      #iframe1{
        border:0px solid #F00;
        width:100%;
        height:100%;
      }
      #iframe2{
        border:0px solid #00F;
        width:100%;
        height:100%;        
      }
      
    </style>
    <link rel="stylesheet" href="${ctxPath}/plugins/bootstrap/3.3.5/css/bootstrap.min.css" />
    <script src="${ctxPath}/plugins/jquery/1.12.4/jquery.min.js" type="text/javascript"></script>
    <script src="${ctxPath}/weboffice.js" type="text/javascript"></script>
    <script src="${ctxPath}/plugins/bootstrap/3.3.5/js/bootstrap.min.js" type="text/javascript"></script>
  </head>
  <body>
    <div class="weboffice-header">
        <div class="btn-toolbar">
          <div class="btn-group">
            <button type="button" onClick="frameObjects[0].openFile()"><span>文档1-打开</span></button>
            <button type="button" onClick="frameObjects[0].officeObject.close()"><span>文档1-关闭</span></button>
            <button type="button" onClick="frameObjects[0].officeObject.refresh()"><span>文档1-刷新</span></button>
            <button type="button" onClick="frameObjects[0].officeObject.save()"><span>文档1-保存</span></button>
          </div>
          <div class="btn-group">
            <button type="button" onClick="frameObjects[1].openFile()"><span>文档2-打开</span></button>
            <button type="button" onClick="frameObjects[1].officeObject.close()"><span>文档2-关闭</span></button>
            <button type="button" onClick="frameObjects[1].officeObject.refresh()"><span>文档2-刷新</span></button>
            <button type="button" onClick="frameObjects[1].officeObject.save()"><span>文档2-保存</span></button>
          </div>          
        </div>
    </div>
    <div class="weboffice-body">
        <div class="weboffice-ctrl-form" >
          <iframe id="iframe1" src="//${host}${ctxPath}/showcase/pageoffice/C56SubPage1"></iframe>
        </div>
        <div class="weboffice-ctrl-form" >
          <iframe id="iframe2" src="//${host}${ctxPath}/showcase/pageoffice/C56SubPage2"></iframe>
        </div>
    </div>
  </body>
</html>
<script type="text/javascript">
$(document).ready(function(){
  window.frameObjects = [$('#iframe1')[0].contentWindow,$('#iframe2')[0].contentWindow];
});
</script>