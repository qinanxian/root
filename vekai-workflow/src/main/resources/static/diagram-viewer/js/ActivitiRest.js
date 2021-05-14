var ActivitiRest = {
	options: {},
  openLayer: function(data) {
    layer.open({
      type: 1 //Page层类型
      ,area: ['300px', '160px']
      ,title: '提示'
      ,closeBtn: 1
      ,shade: 0.6 //遮罩透明度
      ,anim: 1 //0-6的动画形式，-1不开启
      ,content: '<div style="padding: 30px;">'+ (data ? data : '网络请求失败！') +'</div>'
    });
  },

  getHighLightsFlow: function(){
    var url = Lang.sub(this.options.processFlowHighLightsUrl, {processDefinitionId: this.options.processDefinitionId});
    var ret = {};
    $.ajax({
      url: url,
      type: 'GET',
      cache: false,
      async: false,
      dataType: 'json',
      success: function(data) {
        ret = data;
      }
    });
    return ret;
  },

  getHighLightsInstance: function(){
    var url = Lang.sub(this.options.processInstanceHighLightsUrl, {processDefinitionId: this.options.processDefinitionId});
    var ret = {};
    $.ajax({
      url: url,
      type: 'GET',
      cache: false,
      async: false,
      dataType: 'json',
      success: function(data) {
        ret = data;
      }
    });
    return ret;
  },


  getGatewaysCondition: function(){
    var url = Lang.sub(this.options.getGatewaysConditionUrl, {processDefinitionId: this.options.processDefinitionId});
    var ret = {};
    $.ajax({
      url: url,
      type: 'GET',
      cache: false,
      async: false,
      dataType: 'json',
      success: function(data) {
        ret = data;
      }
    });
    return ret;
  },

  highListSourceMerge: function(highLightInsts){
    highLightInsts = highLightInsts || [];
    var obj = this;
    var activiLength = obj.activities.length;
    var highLightInstsMap = {};
      highLightInsts.sort(function(a,b){
      return a.time>b.time?1:-1;
    })

    highLightInsts.forEach(function(item,index){
      for(var i=0; i<activiLength; i++){
        if(item.activityId === obj.activities[i].activityId){
          obj.highLightedActivities.push(item.activityId);
          highLightInstsMap[item.activityId] = item;
          break;
        }
      }
    })

    obj.sequenceFlows.forEach(function(item){
      var flow = item.flow;
      flow = flow.match(/\([^\)]*\)/g);
      flow[0] = flow[0].substring(1,flow[0].length-1);
      flow[1] = flow[1].substring(1,flow[1].length-1);

      item.sourceActivityId = flow[0];
      item.destinationActivityId = flow[1];

      if(highLightInstsMap[flow[0]] && highLightInstsMap[flow[1]]){
        highLightInstsMap[flow[0]].next = highLightInstsMap[flow[0]].next || [];
        highLightInstsMap[flow[0]].next.push(highLightInstsMap[flow[1]]);
        highLightInstsMap[flow[1]].flow = highLightInstsMap[flow[1]].flow || [];
        highLightInstsMap[flow[1]].flow.push(item);
      }      
    })

    treenavIterator(highLightInsts[0],function(node){
      node.flow.forEach(function(f){
        f.isHighLighted = true;
      });

      !node.next && (node.isLast = true);
    })

    highLightInsts.forEach(function(h){
      // !h.next && (h.isLast = true);
      if(h.flow && h.flow.length>1){
        for(var i=0; i<h.flow.length; i++){
          var hliMap = highLightInstsMap[h.flow[i].sourceActivityId]
          if(hliMap.activityType === "exclusiveGateway" && hliMap.next.length>1){
            h.flow[i].isHighLighted = false;
          }
          if(hliMap.activityType === "parallelGateway"){
            hliMap.flow.forEach(function(f){
              f.isHighLighted = true;
            })
          }

        }
      }
    })

    //过滤掉驳回状态下多余的高亮节点
    var lastNode = highLightInsts[highLightInsts.length-1];
    for(var i=0; i<highLightInsts.length; i++){
      if(highLightInsts[i].activityId === lastNode.activityId && highLightInsts[i].taskId !== lastNode.taskId){
        treenavIterator(lastNode,function(node){
          node.flow.forEach(function(f){
            f.isHighLighted = false;
          });
          node.rejected = true;
        })
        break;
      }
    }
    lastNode.isLast = true;
    if(lastNode.activityType === 'parallelGateway'){
      lastNode.flow.forEach(function(f){
        highLightInstsMap[f.sourceActivityId].isLast = true;
      })
    }


    function treenavIterator(data,attr,value){
        function ti(data,index){
            var node = data;
            var next = node.next;
            if(!(next && next.length)){
                return;
            }

            while(index < next.length){
                if(typeof attr === 'function'){
                    attr(next[index]);
                }else{
                    next[index][attr] = value;   
                }
                ti(next[index],0);
                index++;
            }   
        }
        ti(data,0);
    }

    return highLightInstsMap;

  },


  getInstanceTasks: function(taskKey){
    var url = Lang.sub(this.options.processInstanceTasks, {processDefinitionId: this.options.processDefinitionId,taskKey:taskKey});
    var ret = {};
    var dtd = $.Deferred();
    $.ajax({
      url: url,
      type: 'GET',
      cache: false,
      async: true,
      dataType: 'json',
      success: function(data) {
        dtd.resolve(data);
      },
      error: function(jqXHR, textStatus, error){
        dtd.reject(error);
      }
    });
    return dtd.promise();
  },

  getHisTaskInstByTaskId: function(taskId){
    var url = Lang.sub(this.options.historyTaskInstanceUrl, {taskId:taskId});
    // console.log(url)
    var ret = {};
    var dtd = $.Deferred();
    $.ajax({
      url: url,
      type: 'GET',
      cache: false,
      async: true,
      dataType: 'json',
      success: function(data) {
        dtd.resolve(data);
      },
      error: function(jqXHR, textStatus, error){
        dtd.reject(error);
      }
    });
    return dtd.promise();
  },

  getProcessDefinitionByInstanceId: function(processInstanceId, callback, processDefinitionIdcallback) {
    var url = Lang.sub(this.options.processInstanceUrl, {processInstanceId: processInstanceId});
    var self = this;
    var highLightInsts = ActivitiRest.getHighLightsInstance();
    // console.warn(highLightInsts)

    $.ajax({
      url: url,
      type: 'GET',
      cache: false,
      async: true,
      dataType: 'json',
      success: function(data) {
        data.highLightedActivities = [];
        var flowsLength = data.sequenceFlows.length;
        var activiLength = data.activities.length;
        // var test1 = ActivitiRest.getGatewaysCondition();
        // console.log(test1)
        data.highLightInstsMap = ActivitiRest.highListSourceMerge.call(data,highLightInsts);

        var processDefinitionDiagramLayout = data;
        if (!data || data.msgCode) {
          self.openLayer(data && data.msgContent);
          return;
        }

        if (!processDefinitionDiagramLayout) {
          console.error("Process definition diagram layout '" + processDefinitionId + "' not found");
          return;
        } else {
          ProcessDiagramGenerator.options.highlightActivity = data.highLightedActivities;
          ProcessDiagramGenerator.options.processDefinitionId = data.processDefinition.id;
          ProcessDiagramGenerator.options.taskHistoryList = highLightInsts;
          processDefinitionIdcallback.apply({processDefinitionId: processDefinitionDiagramLayout.processDefinition.id});
          callback.apply({processDefinitionDiagramLayout: processDefinitionDiagramLayout, processDefinitionId: processDefinitionDiagramLayout.processDefinition.id});
        }
      },
      error: function(jqXHR, textStatus, error) {
        self.openLayer(textStatus);
      }
    }).done(function(data, textStatus) {
      // console.log("ajax done");
    });
  }
};
