/**
 * Created by geweimin on 17/3/16.
 */
var ActivitiRestDefer = {

  getTaskHistory: function(args, setTaskHistory, callback) {
    var url = Lang.sub(ActivitiRest.options.taskHistoryUrl, {processInstanceId: ActivitiRest.options.processInstanceId});
    $.ajax({
      type: "POST",
      url: url,
      dataType: 'json',
      success: function(msg){
        if (!msg || msg.msgCode) {
          ActivitiRest.openLayer(msg && msg.msgContent);
          return;
        }
        console.log(JSON.stringify(msg));
        setTaskHistory(msg);
        args.push(msg);
        callback.apply(event, args);
      },
      error: function(jqXHR, textStatus, error) {
        ActivitiRest.openLayer(textStatus);
      }
    });

    // $.post(url, function(msg) {
    //   alert( "Data Saved: " + msg );
    // });
  },


  getSignMsg: function(processInstanceId, taskId, callback) {

    // $.post(this.options.signMsgUrl, {processInstanceId, taskId}, function(data) {
    //   callback(data);
    // }, 'json');

    $.ajax({
      type: "POST",
      url: ActivitiRest.options.signMsgUrl,
      data: JSON.stringify({'processInstanceId': processInstanceId, 'taskId': taskId}),
      headers: {'Content-Type': 'application/json'},
      success: function(msg){
        if (!msg || msg.msgCode) {
          ActivitiRest.openLayer(msg && msg.msgContent);
          return;
        }
        callback(msg);
      },
      error: function(jqXHR, textStatus, error) {
        ActivitiRest.openLayer(textStatus);
      }
    });
  },

};
