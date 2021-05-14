/**
 * Created by geweimin on 17/4/12.
 */
var UIManager = {
  showMsg: function (processInstanceId, taskId) {
    ActivitiRestDefer.getSignMsg(processInstanceId, taskId, function (data) {
      console.log('data:' + data);
      var jsonContent = $.parseJSON(data.jsonContent);
      var approved = {
        "category": jsonContent.approved == true ? '同意' : '不同意',
        "content": jsonContent.comments,
        "color": jsonContent.approved == true ? '#2B97E0' : '#be3738'
      };
      var LAYER_CONTENT = '<div style="padding:50px; ">' +
        '<span>意见类型：</span><span style="color: {color};">{category}</span>' +
        '<p style="text-indent: 2em;">{content}</p>' +
        '</div>';
      layer.open({
        type: 1 //Page层类型
        , area: ['500px', '300px']
        , title: '查看签署意见'
        , closeBtn: 1
        , shade: 0.6 //遮罩透明度
        , anim: 1 //0-6的动画形式，-1不开启
        , content: Lang.sub(LAYER_CONTENT, approved)
//          '<div style="padding: 30px;">' +
//          '<span>意见类型：</span><span>' + approved.category + '</span>' +
//          '<p style="text-indent: 2em">' + approved.content + '意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：意见类型：</p>' +
//          '</div>'
      });
    });
  },

  UICompleted: function(processInstanceId) {
    var mousex = 0, mousey = 0;
    var divLeft, divTop;
    var timeStamp = 0;
    $('.diagramHolder').mousewheel(function (event, delta) {
      if (delta === 1) {
        if (event.timeStamp - timeStamp > 600) {
          timeStamp = event.timeStamp;
          ProcessDiagramGenerator.zoomClick('/');
        }
      } else if (delta === -1) {
        if (event.timeStamp - timeStamp > 600) {
          timeStamp = event.timeStamp;
          ProcessDiagramGenerator.zoomClick('*');
        }
      }
      // console.log("delta::::" + delta + "::::eventPhase" + event.eventPhase + "::::timeStamp" + event.timeStamp);
      return false;
    });
    $('.diagramHolder').mousedown(function (e) {
      var offset = $(this).offset();
      divLeft = parseInt($("#diagramHolderBox").scrollLeft(), 10);
      divTop = parseInt($("#diagramHolderBox").scrollTop(), 10);
      mousey = e.pageY;
      mousex = e.pageX;
      // console.log("divLeft", divLeft);
      // console.log("divTop", divTop);
      $(this).bind('mousemove', dragElement);
    });

    function dragElement(event) {
      var left = divLeft - (event.pageX - mousex);
      var top = divTop - (event.pageY - mousey);
      $("#diagramHolderBox").scrollTop(top);
      $("#diagramHolderBox").scrollLeft(left);
      // console.log("left", left);
      // console.log("top", top);
      return false;
    }

    $(document).mouseup(function () {
      $('.diagramHolder').unbind('mousemove');
    });
    // 流程图拖动时间  结束

    ProcessDiagramGenerator.drawHighLights(processInstanceId);
    for(var i = 0; i < ProcessDiagramGenerator.options.taskHistoryList.length; i++){
      if(ProcessDiagramGenerator.options.taskHistoryList[i].isLast && ProcessDiagramGenerator.options.taskHistoryList[i].activityName){
        // console.log(ProcessDiagramGenerator.options.taskHistoryList[i])
        $("#" + ProcessDiagramGenerator.options.taskHistoryList[i].activityId).click();
      }
    }
    // $("#" + ProcessDiagramGenerator.options.highlightActivity[ProcessDiagramGenerator.options.highlightActivity.length -1]).click();
    // $("#" + ProcessDiagramGenerator.options.highlightActivity).click();
    var diagramActivityInfo = $("#diagramActivityInfo");
    var diagramActivityInfoBtn = $("#diagramActivityInfoBtn");
    $("#diagramActivityInfoBtn").hover(function () {
      if (diagramActivityInfo.width() == 0) {
        diagramActivityInfo.stop().animate({width: '16'}, 50);
      }
    }, function () {
      if (diagramActivityInfo.width() == 16) {
        diagramActivityInfo.stop().animate({width: '0'}, 50);
      }
    });

    diagramActivityInfoBtn.on('click', function () {
      if (diagramActivityInfo.width() == 0 || diagramActivityInfo.width() == 16) {
        $("#activityList").css({display: 'block'});
        $("#diagramHolderBox").stop().animate({width: "76%"}, 200);
        $("#activityList").animate({visibility: true}, 200);
        diagramActivityInfo.animate({width: '24%'}, 200, function () {
          diagramActivityInfoBtn.stop().animate({top: 0}, 300, function () {
            $("#burger").addClass('burgerClose');
          });
        });
      } else {
        diagramActivityInfoBtn.stop().animate({top: ($("#contentBox").height() - 16) / 2}, 300, function () {
          diagramActivityInfo.stop().animate({width: '0'}, 200, function () {
            $("#activityList").css({display: 'none'});
            $("#burger").removeClass('burgerClose');
          });
          $("#diagramHolderBox").animate({width: "100%"}, 200);
        });
      }
    });
  },

  elementClick: function(canvas, element, contextObject, taskHistoryList) {
    // 当前任务定义key
    var id = contextObject.id;

    // 当前任务列表(多实例任务多个,历史任务多个)
    var taskList = [];
    taskHistoryList.forEach(function (item, index) {
      if (item.activityId == id && !item.rejected) {
        taskList.push($.extend({index}, {
          "processInstanceId":item.processInstanceId,
          "taskDefinitionKey":item.activityId,
          "taskName":item.activityName,
          "taskType":item.activityType,
          "assigneeName":item.assignee || '',
          "roleName":item.roleName,
          "startTime":item.startTime,
          "endTime":item.endTime,
          "assigneeId":item.executionId,
          "isLast":item.isLast,
          "taskId":item.taskId,
        }));

      }

      if(item.activityId && item.taskId){
        UIManager.taskHistoryList = UIManager.taskHistoryList || {};
        UIManager.taskHistoryList[item.activityId] = item;
      }


    });

    var loading = '<div class="spinner">'+
    '<div class="bounce1"></div>'+
    '<div class="bounce2"></div>'+
    '<div class="bounce3"></div>'+
    '</div>';



    // console.log(taskList)

    // taskList.map(function (item, index) {
    //   if (item.index < taskHistoryList.length - 1) {
    //     var afterIndex = item.index + 1;
    //     $.extend(item, {
    //       afterAssigneeName: taskHistoryList[afterIndex].assigneeName,
    //       afterRoleName: taskHistoryList[afterIndex].roleName,
    //       afterDeptName: taskHistoryList[afterIndex].deptName,
    //     })
    //   } else {
    //     $.extend(item, {
    //       afterAssigneeName: '',
    //       afterRoleName: '',
    //       afterDeptName: '',
    //     })
    //   }

    //   if (item.index > 0) {
    //     var frontIndex = item.index - 1;
    //     $.extend(item, {
    //       frontAssigneeName: taskHistoryList[frontIndex].assigneeName,
    //       frontRoleName: taskHistoryList[frontIndex].roleName,
    //       frontDeptName: taskHistoryList[frontIndex].deptName,
    //     })
    //   } else {
    //     $.extend(item, {
    //       frontAssigneeName: '',
    //       frontRoleName: '',
    //       frontDeptName: '',
    //     })
    //   }

    //   item.startTime = (new Date(item.startTime)).toLocaleString().substr(0, 9);
    //   item.endTime = item.endTime ? (new Date(item.endTime)).toLocaleString().substr(0, 9) : '';
    //   return item;
    // });

    var TPL_ACTIVITY_INFO = '<div id="activityInfoContent"><div class="activity-info-title">进度流转情况</div>';
    var timeLine = [];
    var radius = 5;
    if (taskList.length == 0) {
      // TPL_ACTIVITY_INFO += '<div class="activity-info-empty">暂无进度流转！</div></div>';
      TPL_ACTIVITY_INFO +=
      '<div class="activity-info-item-card" style="margin-top:10px;padding-left:10px;border: 1px solid #C1C1C1; border-radius: 4px; margin-bottom: 4px; width:calc(100% - 62px);margin-left:40px">'+
        '<div style="line-height: 28px;">节点名：'+contextObject.properties.name+'</div>' +
        '<div style="line-height: 28px;">任务类型：'+contextObject.properties.type+'</div>' +
      '</div>'
    } else {
      TPL_ACTIVITY_INFO +=
        '<div class="activity-info-item-card" style="margin-top:10px;padding-left:10px;border: 1px solid #C1C1C1; border-radius: 4px; margin-bottom: 4px; width:calc(100% - 62px);margin-left:40px">'+
          '<div style="line-height: 28px;">节点名：{taskName}</div>' +
          '<div style="line-height: 28px;">任务类型：{taskType}</div>' +
        '</div>'
      TPL_ACTIVITY_INFO += '<div style="display:flex">' +
        '<div id="canvasBox">' +
        '<canvas id="timeLineCanvas" width="40px"/>' +
        '</div>' +
        '<div style="width: calc(100% - 40px)">';

      // 取得最后一个任务(已完成历史任务去除掉)
      taskList = [taskList[taskList.length-1]];

      for (var i = 0; i < taskList.length; i++) {
        var taskListItem = taskList[i];
        timeLine.push({type: "circle", y: 240 * i + 14});
        if (taskListItem.endTime) {
          timeLine.push({
            type: "line",
            startY: timeLine[timeLine.length - 1].y + radius,
            endY: timeLine[timeLine.length - 1].y + 184 + 14 + 14 - radius
          });
          timeLine.push({type: "circle", y: timeLine[timeLine.length - 1].endY + radius});
        } else {
          timeLine.push({
            type: "line",
            startY: timeLine[timeLine.length - 1].y + radius,
            endY: timeLine[timeLine.length - 1].y + 240 - 14
          });
        }


        TPL_ACTIVITY_INFO += '<div class="activity-info-item" style="display:flex;flex-direction:column;justify-content:space-between">' +
          '<span id="start-time-container" style="line-height: 28px;"><b></b>{startTime}  任务创建</span>' +
          '<div id="info-container">' ;

        TPL_ACTIVITY_INFO += 
          '<div id="assignee-container" class="activity-info-item-card" style="padding-left:10px;line-height:28px;border: 1px solid #C1C1C1; border-radius: 4px;">' +
          '<div style="line-height: 28px;">处理人：</div>' +
          '<div style="line-height: 28px;font-weight:bold">{assigneeName}</div>' +
          '</div>';
        TPL_ACTIVITY_INFO += 
          '<div class="activity-info-item-card" id="candidate-container" style="margin-top:10px;display:none;padding-left:10px;line-height:28px;border: 1px solid #C1C1C1; border-radius: 4px;">'+
          '<div style="line-height: 28px;">候选人：</div>' +
          '<div style="line-height: 28px;max-height:400px;overflow:auto" id="candidate-content"></div>' +
          '</div>';

        TPL_ACTIVITY_INFO +=
          '</div>' +
          '<div id="end-time" style="line-height:28px;align-items: center;justify-content: space-between">';
        if (taskListItem.endTime) {
          TPL_ACTIVITY_INFO += '<span id="end-time-container">{endTime}  任务完成</span>';
        }
        if (taskListItem.endTime && taskListItem.frontAssigneeName) {
          TPL_ACTIVITY_INFO +=
            '<div style="align-items: center">' +
            '<img style="width: 18px; height: 18px" src="./images/deployer/massage.svg" />' +
            '<a class="msg-is-exist" onclick="UIManager.showMsg({processInstanceId}, {taskId});">查看意见</a>' +
            '</div>';
        }
        TPL_ACTIVITY_INFO +=
          '</div></div>';
        TPL_ACTIVITY_INFO = Lang.sub(TPL_ACTIVITY_INFO, taskList[i]);
      }
      TPL_ACTIVITY_INFO += '</div></div>';
    }

    TPL_ACTIVITY_INFO += '</div>';
    var activityList = $("#activityList");
    activityList.html("");
    activityList.html(TPL_ACTIVITY_INFO);

    if(taskListItem && taskListItem.taskDefinitionKey && taskListItem.taskId){
      if(UIManager.taskHistoryList[taskListItem.taskDefinitionKey].createTime){
        $('#start-time-container').html("<b>"+UIManager.taskHistoryList[taskListItem.taskDefinitionKey].createTime+"</b>  任务创建");
      }else{
        $('#start-time-container').html(loading);

        // 查询历史任务对象
        ActivitiRest.getHisTaskInstByTaskId(taskListItem.taskId).done(function(data){
          if(data.createTime){
            UIManager.taskHistoryList[taskListItem.taskDefinitionKey].createTime = data.createTime;
            $('#start-time-container').html("<b>"+data.createTime+"</b>  任务创建");
          }
        })
      }
    }


    if(taskListItem && taskListItem.isLast){
      $('#end-time-container').hide();
      var assignee = UIManager.taskHistoryList[taskListItem.taskDefinitionKey].assignee;
      var candidateUsers = UIManager.taskHistoryList[taskListItem.taskDefinitionKey].candidateUsers;
      if(assignee){
        $('#assignee-container').children()[1].innerHTML = assignee;
      }else if(candidateUsers && candidateUsers !== null){
        $('#candidate-container').show();
        $('#candidate-content').html('<b>'+candidateUsers.join(', ')+'</b>');
      }else{
        $('#candidate-container').show();
        $('#candidate-content').html(loading);
        ActivitiRest.getInstanceTasks(id).done(function(data){
          if(data.assignee){
            UIManager.taskHistoryList[taskListItem.taskDefinitionKey].assignee = data.assignee;
            $('#assignee-container').children()[1].innerHTML = data.assignee;
          }else{
            var candidateUsers = data.candidateUsers;
            candidateUsers = candidateUsers || null;
            UIManager.taskHistoryList[taskListItem.taskDefinitionKey].candidateUsers = candidateUsers;
            if(candidateUsers && candidateUsers !== null){
              $('#candidate-content').html('<b>'+candidateUsers.join(', ')+'</b>');
            }else{
              $('#candidate-container').hide();
            }
          }
        });
      }
    }



    // if(taskListItem && taskListItem.isLast && !taskListItem.assigneeName){
    //   $('#end-time-container').hide();
    //   var candidateUsers = UIManager.taskHistoryList[taskListItem.taskDefinitionKey].candidateUsers;
    //   if(candidateUsers && candidateUsers !== null){
    //     $('#candidate-container').show();
    //     $('#candidate-content').html('<b>'+candidateUsers.join(', ')+'</b>');
    //   }else{
    //     $('#candidate-container').show();
    //     $('#candidate-content').html(loading);
    //     ActivitiRest.getInstanceTasks(id).done(function(data){
    //       var candidateUsers = data.body.candidateUsers;
    //       candidateUsers = candidateUsers || null;
    //       UIManager.taskHistoryList[taskListItem.taskDefinitionKey].candidateUsers = candidateUsers;

    //       if(candidateUsers && candidateUsers !== null){
    //         $('#candidate-content').html('<b>'+candidateUsers.join(', ')+'</b>');
    //       }else{
    //         $('#candidate-container').hide();
    //       }
    //     });
    //   }
    // }

    if (timeLine.length > 0) {
      var timeLineCanvas = document.getElementById("timeLineCanvas");
      var ctx = timeLineCanvas.getContext("2d");
      ctx.canvas.width = $("#canvasBox").width();
      $("#canvasBox").css("height", $("#activityInfoContent").height());
      ctx.canvas.height = $("#canvasBox").height();
      var lineArr = [];

      for (var j = 0; j < timeLine.length; j++) {
        if (timeLine[j].type == 'circle') {
//              drawDot(20, timeLine[j].y, 0)
          ctx.beginPath();
          ctx.arc(20, timeLine[j].y, radius, 0, 2 * Math.PI);
          ctx.fillStyle = '#2B97E0';
          ctx.strokeStyle = '#2B97E0';
          ctx.fill();
          ctx.closePath();
        } else {
          lineArr.push(timeLine[j]);
        }
      }
      for (var k = 0; k < lineArr.length; k++) {
        ctx.beginPath();
        ctx.strokeStyle = '#CCCCCC';
        ctx.moveTo(20, lineArr[k].startY);
        ctx.lineTo(20, lineArr[k].endY);
        ctx.stroke();
        ctx.closePath();
      }
    }
    if (activityList.width() == 16 || activityList.width() == 0) {
      activityList.css({display: 'block'});
      $("#diagramHolderBox").stop().animate({width: "76%"}, 200);
      activityList.animate({visibility: true}, 200);
      $("#diagramActivityInfo").animate({width: '24%'}, 200, function () {
        $("#diagramActivityInfoBtn").stop().animate({top: 0}, 300, function () {
          $("#burger").addClass('burgerClose');
        });
      });
    }
  }
};
