var fileSize=4;
$(document).ready(function () {
  var loadingImg = document.getElementById('loadingImg');
  if (navigator.userAgent.indexOf("MSIE")!=-1) {
    // 如果是ie浏览器则使用gif动画
    loadingImg.style.opacity = 1;
    loadingImg.src = 'loading.gif';
  } else {
    loadingImg.style.opacity = 1;
    loadingImg.src = 'loading.gif';
  }
  var process = document.getElementById('process');
  process.style.width = '0px';
  var loadFlag = false;
  // 1毫秒
  var count = 1;
// 启动iframe定时器
  var countIframe = 0;
  var timerIframe = setInterval(function () {
    countIframe++;
    // 每过100毫秒显示一次进度条
    // 每秒获取的数据量（KB）
    var data = ((((38 / count) * countIframe * 100) / ((fileSize || 4) * 1024)) * 100);
    if (loadFlag) {
      // 重置所有数据
      clearInterval(timerIframe);
      count = 1;
      countIframe = 0;
      $('#precent').text('100%');
      process.style.width = '400px';
    } else {
      if (data >= 99) {
        process.style.width = (99 * 4) + 'px';
        $('#precent').text('99%');
      } else {
        process.style.width = (data.toFixed(2) * 4) + 'px';
        $('#precent').text(data.toFixed(2) + '%');
      }
    }
  }, 100);
// 开始请求
  // 开始请求的时间戳
  count = new Date().getTime();
  $.ajax({
    url: 'loading.gif',
    cache: false,
    xhrFields: {
      withCredentials: true,
    },
    type: 'get',
    error: function(xhr, status, err) {
// 请求结束
      count = new Date().getTime() - count;
    },
    success: function(result, status, xhr) {
// 请求结束
      count = new Date().getTime() - count;
    },
  });
  var iframe = document.getElementById("main");
  if (iframe.attachEvent) {
    iframe.attachEvent("onload", function () {
      loadFlag = true;
      $('#precent').text('100%');
      process.style.width = '400px';
      window.location.href = 'main.html';
    });
  } else {
    iframe.onload = function () {
      loadFlag = true;
      $('#precent').text('100%');
      process.style.width = '400px';
      window.location.href = 'main.html';
    };
  }
});
