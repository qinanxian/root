/**
 * Created by geweimin on 17/4/11.
 */
var DiagramGenerator = {};
var pb1;
$(document).ready(function () {
  layer.config({
    extend: 'myskin/style.css', //加载您的扩展样式
    skin: 'layer-ext-myskin'
  });
  var query_string = {};
  var query = window.location.search.substring(1);
  // query = window.atob(query.substring(2));
  var vars = query.split("&");
  for (var i = 0; i < vars.length; i++) {
    var pair = vars[i].split("=");
    query_string[pair[0]] = pair[1];
  }

  var processDefinitionId = query_string["processInstanceId"];
  var processInstanceId = query_string["processDefinitionId"];

  pb1 = new $.ProgressBar({
    boundingBox: '#pb1',
    label: 'Progressbar!',
    on: {
      complete: function () {
        // 流程图拖动时间  开始
        UIManager.UICompleted(processInstanceId);
      },
      valueChange: function (e) {
        this.set('label', e.newVal + '%');
      }
    },
    value: 0
  });

  ProcessDiagramGenerator.options = {
    diagramBreadCrumbsId: "diagramBreadCrumbs",
    diagramHolderId: "diagramHolder",
    activityListId: "activityList",
    taskHistoryList: [],
    activeActivity: null,
    highlightActivity: null,
    zoomNum: 0,
    on: {
      click: function (canvas, element, contextObject, taskHistoryList) {
        UIManager.elementClick(canvas, element, contextObject, taskHistoryList);
      },
      rightClick: function (canvas, element, contextObject) {

      },
      over: function (canvas, element, contextObject) {

      },
      out: function (canvas, element, contextObject) {

      }
    }
  };

  // var baseUrl = "http://localhost:3003";
  // var baseUrl = window.document.location.protocol + "//" + window.document.location.host;
  // var baseUrl = window.___Host;


  var path = ACTIVITI.CONFIG.contextRoot;

  ActivitiRest.options = {
    processInstanceHighLightsUrl: path + "/process/hisActinsts/{processDefinitionId}?"+ACTIVITI.CONFIG.TOKEN,
    processInstanceTasks: path + "/process/latestTasksCandidates/{processDefinitionId}/{taskKey}?"+ACTIVITI.CONFIG.TOKEN,
    historyTaskInstanceUrl: path + "/process/histTask/{taskId}?"+ACTIVITI.CONFIG.TOKEN,
    getGatewaysConditionUrl:path + "/WorkflowTaskRestCtrl/getProcVarsByProcInstId?procInstId={processDefinitionId}?"+ACTIVITI.CONFIG.TOKEN,
    processFlowHighLightsUrl: path + "/diagram/process-instance/{processDefinitionId}/highlights?callback=?&"+ACTIVITI.CONFIG.TOKEN,
    processInstanceUrl: path + "/diagram/process-definition/{processInstanceId}/diagram-layout?callback=?&"+ACTIVITI.CONFIG.TOKEN,
    processActiveTaskUrl: path + "/getCandidates/{taskId}?"+ACTIVITI.CONFIG.TOKEN,
    processInstanceId: processInstanceId,
    processDefinitionId: processDefinitionId,
  };


  if (processInstanceId) {
    ProcessDiagramGenerator.drawDiagram(processInstanceId);
  } else {
    alert("processInstanceId parameter is required");
  }
});
