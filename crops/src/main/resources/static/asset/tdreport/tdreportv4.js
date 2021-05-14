/**
 * Created by Session on 18/01/16.
 */
$.extend({
  showReport: function (data) {
    var displayName = {
      "ANTIFRAUD": "贷前反欺诈",
      "AUTHENTICATION": "信息核验",
      "CREDIT": "授信定价",
      "WITHDRAWALS": "提现",
      "PREFILTER": "预筛",
      "REGISTER": "注册",
      "LOGIN": "登录",
      "PRECREDIT": "预授信",
      "LOANINGQUERY": "贷中检查",
      "ADJUSTAMOUNT": "调额",
      "CREDITTRADE": "信用交易",
      "INFOANALYSIS": "信息解析",
      "ENTPRELOAN": "企业贷前审核",
      "RENT": "租赁",
      "SURETY": "担保",
      "ACCOUNT": "开户",
      "INSURE": "投保",
      "BACKGROUNDCHECK": "员工背调",
      "MERCHANTPERMISSION": "商户准入",
      "DEBTASSESS": "债权评估",
      "ACTIVATE": "激活"
    };
    var templateData = dealData(data);

    function dealData(apiData) {
      var resData = {};//最后的数据
      var module_report = [];//存放多个报告
      $.each(apiData, function (index, item) {
        if (!resData.id && item.id) {
          resData.id = item.id;
        }
        if (item.result_desc) {
          for (var result in item.result_desc) {
            if (result == "INFOANALYSIS") {
              $.extend(resData, {"INFOANALYSIS": item.result_desc.INFOANALYSIS});
            } else {
              if (displayName[result] && item.result_desc[result].final_decision) {
                var tmp = {};
                if (item.result_desc[result].final_decision == "PASS") {
                  tmp.final_score = 0;
                }
                tmp.report_name = result;
                tmp.report_display_name = displayName[result];
                $.extend(tmp, item.result_desc[result]);
                //改变output_fields的结构
                if (item.result_desc[result].output_fields) {
                  var fields = [];//存放改变接口以后的
                  var output_fields = item.result_desc[result].output_fields;
                  if (Array.isArray(output_fields)) {
                    $.each(output_fields, function (i, v) {
                      for (var vField in v) {
                        fields.push({"field_name": vFiled, "field_value": v[vFiled]});
                      }
                    });
                  } else {
                    for (var field in output_fields) {
                      fields.push({"field_name": field, "field_value": output_fields[field]});
                    }
                  }
                  $.extend(tmp, {"output_fields": fields});
                }
                module_report.push(tmp);
              }
            }
          }
        }
      });
      $.extend(resData, {"module_report": module_report});
      return resData;
    }

    // 加载css样式
    addCSSFile();

    function addCSSFile() {
      var isAdd = false;
      $.each($('link'), function (index, item) {
        var href = $(item).prop('href');
        if (href.indexOf('/tdstyle.1.0.css') > -1) {
          isAdd = true;
        }
      });
      if (!isAdd) {
        var hrefLink;
        $.each($('script'), function (index, item) {
          var js_index = $(item).prop('src').indexOf('tdreportv4');
          if (js_index > -1) {
            hrefLink = $(item).prop('src').substring(0, js_index) + '/tdstyle.1.0.css?r=' + (new Date()).getTime();
          }
        });
        $('head').append('<link rel="stylesheet" href="' + hrefLink + '"/>');
      }
    }

    var sourceReportTemplate = '<div class="report-container"><div class="report-container-container"><div class="left-nav"> {{if INFOANALYSIS}} <div class="nav-tab person-info-nav"><span data-report="person-info-div"><a>个人报告</a></span></div> {{/if}} {{each module_report as sub_report sub_report_nav_index}} <div class="nav-tab"><span data-report="{{sub_report.report_name}}"><a>{{sub_report.report_display_name}}报告</a></span></div> {{/each}} </div><div class="report-a-close" style="display: none" id="report-a-close"><a href="javascript:void(0)">X</a></div><div class="header"><h1 class="header-title"></h1><div class="header-report-application"><span>保镖ID:</span><span>{{id}}</span></div></div> {{if INFOANALYSIS}} <div class="inner person-info-div"> {{if INFOANALYSIS.address_detect}} <div class="module"><div class="address-analysis"><div class="module-title"><h2>归属地解析</h2></div><div class="rpi-subitem"> {{if INFOANALYSIS.address_detect.id_card_address}} <p class="p-hidden"><span class="label">身份证所属地: <span class="label-span"> {{INFOANALYSIS.address_detect.id_card_address}} </span></span></p> {{/if}} {{if INFOANALYSIS.address_detect.mobile_address}} <p class="p-hidden"><span class="label">手机所属地: <span class="label-span"> {{INFOANALYSIS.address_detect.mobile_address}} </span></span></p> {{/if}} {{if INFOANALYSIS.address_detect.true_ip_address}} <p class="p-hidden"><span class="label">ip所属地: <span class="label-span"> {{INFOANALYSIS.address_detect.true_ip_address}} </span></span></p> {{/if}} {{if INFOANALYSIS.address_detect.wifi_address}} <p class="p-hidden"><span class="label">wifi所属地: <span class="label-span"> {{INFOANALYSIS.address_detect.wifi_address}} </span></span></p> {{/if}} {{if INFOANALYSIS.address_detect.cell_address}} <p class="p-hidden"><span class="label">基站所属地: <span class="label-span"> {{INFOANALYSIS.address_detect.cell_address}} </span></span></p> {{/if}} {{if INFOANALYSIS.address_detect.bank_card_address}} <p class="p-hidden"><span class="label">银行卡所属地: <span class="label-span"> {{INFOANALYSIS.address_detect.bank_card_address}} </span></span></p> {{/if}} </div></div></div> {{/if}} {{if INFOANALYSIS.geoip_info}} <div class="module"><div class="address-analysis"><div class="module-title"><h2>IP解析</h2></div><div class="rpi-subitem"> {{if INFOANALYSIS.geoip_info.position}} <p class="p-hidden"><span class="label">地址: <span class="label-span"> {{INFOANALYSIS.geoip_info.position}} </span></span></p> {{/if}} {{if INFOANALYSIS.geoip_info.longitude}} <p class="p-hidden"><span class="label">经度: <span class="label-span">{{INFOANALYSIS.geoip_info.longitude}}</span></span></p> {{/if}} {{if INFOANALYSIS.geoip_info.isp}} <p class="p-hidden"><span class="label">互联网提供商: <span class="label-span">{{INFOANALYSIS.geoip_info.isp}}</span></span></p> {{/if}} {{if INFOANALYSIS.geoip_info.latitude}} <p class="p-hidden"><span class="label">纬度: <span class="label-span">{{INFOANALYSIS.geoip_info.latitude}}</span></span></p> {{/if}} {{each INFOANALYSIS.geoip_info.proxy_info as proxyInfo proxyIndex}} {{if proxyInfo.port}} <p class="p-hidden"><span class="label">代理端口: <span class="label-span">{{proxyInfo.port}}</span></span></p> {{/if}} {{if proxyInfo.proxyProtocol}} <p class="p-hidden"><span class="label">代理协议: <span class="label-span">{{proxyInfo.proxyProtocol}}</span></span></p> {{/if}} {{if proxyInfo.proxyType}} <p class="p-hidden "><span class="label">代理类型: <span class="label-span">{{proxyInfo.proxyType}}</span></span></p> {{/if}} {{/each}} </div></div></div> {{/if}} {{if INFOANALYSIS.geotrueip_info}} <div class="module"><div class="address-analysis"><div class="module-title"><h2>真实IP解析</h2></div><div class="rpi-subitem"> {{if INFOANALYSIS.geotrueip_info.position}} <p class="p-hidden"><span class="label">地址: <span class="label-span"> {{INFOANALYSIS.geotrueip_info.position}}</span></span></p> {{/if}} {{if INFOANALYSIS.geotrueip_info.longitude}} <p class="p-hidden"><span class="label">经度: <span class="label-span"> {{INFOANALYSIS.geotrueip_info.longitude}}</span></span></p> {{/if}} {{if INFOANALYSIS.geotrueip_info.isp}} <p class="p-hidden"><span class="label">互联网提供商: <span class="label-span">{{INFOANALYSIS.geotrueip_info.isp}}</span></span></p> {{/if}} {{if INFOANALYSIS.geotrueip_info.latitude}} <p class="p-hidden"><span class="label">纬度: <span class="label-span"> {{INFOANALYSIS.geotrueip_info.latitude}}</span></span></p> {{/if}} </div></div></div> {{/if}} {{if INFOANALYSIS.device_info && !INFOANALYSIS.device_info.error}} <div class="module"><div id="rp-device-info" class="rp-item rp-device-info"><div class="module-title"><h2>设备信息</h2></div><div style="margin: 20px 0px 20px 24px;"><a href="javascript:void(0)" class="jz">详情 (点击查看)</a></div><div class="table-wrap j-rpi-toggle-target"><table class="device-info"> {{if INFOANALYSIS.device_info.appOs.toLowerCase() == \'web\'}} <tr><td class="device-title">启用Cookie</td><td class="device-value"> {{#INFOANALYSIS.device_info.cookieEnabled | dealDisplay }} </td><td class="device-title">操作系统</td><td class="device-value"> {{#INFOANALYSIS.device_info.os | dealDisplay}} </td></tr><tr><td class="device-title">真实IP</td><td class="device-value"> {{#INFOANALYSIS.device_info.trueIp | dealDisplay}} </td><td class="device-title">集成sdk版本号</td><td class="device-value"> {{#INFOANALYSIS.device_info.fpVersion | dealDisplay}} </td></tr><tr><td class="device-title">tokenId</td><td class="device-value"> {{#INFOANALYSIS.device_info.tokenId | dealDisplay}}</td><td class="device-title">设备ID</td><td class="device-value"> {{#INFOANALYSIS.device_info.deviceId | dealDisplay}}</td></tr><tr><td class="device-title">浏览器</td><td class="device-value"> {{#INFOANALYSIS.device_info.userAgent | dealDisplay}}</td><td class="device-title">智能ID</td><td class="device-value"> {{#INFOANALYSIS.device_info.smartId | dealDisplay}}</td></tr><tr><td class="device-title">帆布指纹</td><td class="device-value"> {{#INFOANALYSIS.device_info.canvas | dealDisplay}}</td><td class="device-title">计算机语言</td><td class="device-value"> {{#INFOANALYSIS.device_info.languageRes | dealDisplay}}</td></tr><tr><td class="device-title">屏幕分辨率</td><td class="device-value"> {{#INFOANALYSIS.device_info.screenRes | dealDisplay}}</td><td class="device-title">插件列表</td><td class="device-value"> {{#INFOANALYSIS.device_info.pluginListHash | dealDisplay}}</td></tr><tr><td class="device-title">启用Flash</td><td class="device-value"> {{#INFOANALYSIS.device_info.flashEnabled | dealDisplay}}</td><td class="device-title">请求来源</td><td class="device-value"> {{#INFOANALYSIS.device_info.referer | dealDisplay}}</td></tr><tr><td class="device-title">浏览器类型</td><td class="device-value"> {{#INFOANALYSIS.device_info.browserType | dealDisplay}}</td><td class="device-title">TCP协议栈特征对应的操作系统</td><td class="device-value"> {{#INFOANALYSIS.device_info.tcpOs | dealDisplay}}</td></tr><tr><td class="device-title">浏览器版本</td><td class="device-value"> {{#INFOANALYSIS.device_info.browserVersion | dealDisplay}}</td><td class="device-title">字体</td><td class="device-value"> {{#INFOANALYSIS.device_info.fontListHash | dealDisplay}}</td></tr><tr><td class="device-title">浏览器header</td><td class="device-value"> {{#INFOANALYSIS.device_info.accept | dealDisplay}}</td><td class="device-title">浏览器header编码类型</td><td class="device-value"> {{#INFOANALYSIS.device_info.acceptEncoding | dealDisplay}}</td></tr><tr><td class="device-title">浏览器header语言</td><td class="device-value"> {{#INFOANALYSIS.device_info.acceptLanguage | dealDisplay}}</td><td class="device-title">时区</td><td class="device-value"> {{#INFOANALYSIS.device_info.timeZone | dealDisplay}}</td></tr><tr><td class="device-title">开启Debug模式</td><td class="device-value"> {{#INFOANALYSIS.device_info.webDebuggerStatus | dealDisplay}}</td><td class="device-title">启用js</td><td class="device-value"> {{#INFOANALYSIS.device_info.enabledJs | dealDisplay}}</td></tr><tr><td class="device-title">错误原因</td><td class="device-value"> {{#INFOANALYSIS.device_info.error | dealDisplay}}</td><td></td><td></td></tr> {{else if INFOANALYSIS.device_info.appOs.toLowerCase() == \'ios\'}} <tr><td class="device-title">集成sdk版本号</td><td class="device-value"> {{#INFOANALYSIS.device_info.fpVersion | dealDisplay}}</td><td class="device-title">tokenId</td><td class="device-value"> {{#INFOANALYSIS.device_info.tokenId | dealDisplay}}</td></tr><tr><td class="device-title">设备ID</td><td class="device-value"> {{#INFOANALYSIS.device_info.deviceId | dealDisplay}}</td><td class="device-title">操作系统</td><td class="device-value"> {{#INFOANALYSIS.device_info.os | dealDisplay}}</td></tr><tr><td class="device-title">iOS系统版本</td><td class="device-value"> {{#INFOANALYSIS.device_info.osVersion | dealDisplay}}</td><td class="device-title">广告追踪标识</td><td class="device-value"> {{#INFOANALYSIS.device_info.idfa | dealDisplay}}</td></tr><tr><td class="device-title">厂商追踪标识</td><td class="device-value"> {{#INFOANALYSIS.device_info.idfv | dealDisplay}}</td><td class="device-title">生效客户端ID</td><td class="device-value"> {{#INFOANALYSIS.device_info.uuid | dealDisplay}}</td></tr><tr><td class="device-title">开机时刻</td><td class="device-value"> {{#INFOANALYSIS.device_info.bootTime | dealTime}}</td><td class="device-title">当前时间戳</td><td class="device-value"> {{#INFOANALYSIS.device_info.currentTime | dealTime}}</td></tr><tr><td class="device-title">运行时间</td><td class="device-value"> {{#INFOANALYSIS.device_info.upTime | dealTimeLong}}</td><td class="device-title">存储空间</td><td class="device-value"> {{#INFOANALYSIS.device_info.totalSpace | dealMemory}}</td></tr><tr><td class="device-title">可用空间</td><td class="device-value"> {{#INFOANALYSIS.device_info.freeSpace | dealMemory}}</td><td class="device-title">内存大小</td><td class="device-value"> {{#INFOANALYSIS.device_info.memory | dealMemory}}</td></tr><tr><td class="device-title">蜂窝网络ip</td><td class="device-value"> {{#INFOANALYSIS.device_info.cellIp | dealDisplay}}</td><td class="device-title">wifi IP</td><td class="device-value"> {{#INFOANALYSIS.device_info.wifiIp | dealDisplay}}</td></tr><tr><td class="device-title">wifi 子网掩码</td><td class="device-value"> {{#INFOANALYSIS.device_info.wifiNetmask | dealDisplay}}</td><td class="device-title">wifi mac</td><td class="device-value"> {{#INFOANALYSIS.device_info.mac | dealDisplay}}</td></tr><tr><td class="device-title">wifi</td><td class="device-value"> {{#INFOANALYSIS.device_info.ssid | dealDisplay}}</td><td class="device-title">wifi BSSID</td><td class="device-value"> {{#INFOANALYSIS.device_info.bssid | dealDisplay}}</td></tr><tr><td class="device-title">VPN IP</td><td class="device-value"> {{#INFOANALYSIS.device_info.vpnIp | dealDisplay}}</td><td class="device-title">VPN 子网掩码</td><td class="device-value"> {{#INFOANALYSIS.device_info.vpnNetmask | dealDisplay}}</td></tr><tr><td class="device-title">网络类型</td><td class="device-value"> {{#INFOANALYSIS.device_info.networkType | dealDisplay}}</td><td class="device-title">Wifi代理类型</td><td class="device-value"> {{#INFOANALYSIS.device_info.proxyType | dealDisplay}}</td></tr><tr><td class="device-title">Wifi代理地址</td><td class="device-value"> {{#INFOANALYSIS.device_info.proxyUrl | dealDisplay}}</td><td class="device-title">设备型号</td><td class="device-value"> {{#INFOANALYSIS.device_info.platform | dealDisplay}}</td></tr><tr><td class="device-title">设备名称</td><td class="device-value"> {{#INFOANALYSIS.device_info.deviceName | dealDisplay}}</td><td class="device-title">屏幕亮度</td><td class="device-value"> {{#INFOANALYSIS.device_info.brightness | dealDisplay}}</td></tr><tr><td class="device-title">运营商</td><td class="device-value"> {{#INFOANALYSIS.device_info.carrier | dealDisplay}}</td><td class="device-title">ISO标准国家码</td><td class="device-value"> {{#INFOANALYSIS.device_info.countryIso | dealDisplay}}</td></tr><tr><td class="device-title">移动网络码</td><td class="device-value"> {{#INFOANALYSIS.device_info.mnc | dealDisplay}}</td><td class="device-title">移动国家码</td><td class="device-value"> {{#INFOANALYSIS.device_info.mcc | dealDisplay}}</td></tr><tr><td class="device-title">网络通讯制式</td><td class="device-value"> {{#INFOANALYSIS.device_info.radioType | dealDisplay}}</td><td class="device-title">应用的BundleId</td><td class="device-value"> {{#INFOANALYSIS.device_info.bundleId | dealDisplay}}</td></tr><tr><td class="device-title">应用的版本</td><td class="device-value"> {{#INFOANALYSIS.device_info.appVersion | dealDisplay}}</td><td class="device-title">时区</td><td class="device-value"> {{#INFOANALYSIS.device_info.timeZone | dealDisplay}}</td></tr><tr><td class="device-title">_CodeSignature MD5</td><td class="device-value"> {{#INFOANALYSIS.device_info.signMd5 | dealDisplay}}</td><td class="device-title">采集信息耗时</td><td class="device-value"> {{#INFOANALYSIS.device_info.timeCost | dealDisplay}}</td></tr><tr><td class="device-title">语言列表</td><td class="device-value"> {{#INFOANALYSIS.device_info.languages | dealDisplay}}</td><td class="device-title">充电状态</td><td class="device-value"> {{#INFOANALYSIS.device_info.batteryStatus | dealDisplay}}</td></tr><tr><td class="device-title">电量</td><td class="device-value"> {{#INFOANALYSIS.device_info.batteryLevel | dealDisplay}}</td><td class="device-title">内核版本</td><td class="device-value"> {{#INFOANALYSIS.device_info.kernelVersion | dealDisplay}}</td></tr><tr><td class="device-title">最近一次定位</td><td class="device-value"> {{#INFOANALYSIS.device_info.gpsLocation | dealDisplay}}</td><td class="device-title">GPS开关状态</td><td class="device-value"> {{#INFOANALYSIS.device_info.gpsSwitch | dealDisplay}}</td></tr><tr><td class="device-title">GPS授权状态</td><td class="device-value"> {{#INFOANALYSIS.device_info.gpsAuthStatus | dealDisplay}}</td><td class="device-title">通过环境变量注入的动态库</td><td class="device-value"> {{#INFOANALYSIS.device_info.env | dealDisplay}}</td></tr><tr><td class="device-title">越狱后注入进程的插件库</td><td class="device-value"> {{#INFOANALYSIS.device_info.attached | dealDisplay}} </td><td class="device-title">真实IP</td><td class="device-value"> {{#INFOANALYSIS.device_info.trueIp | dealDisplay}}</td></tr><tr><td class="device-title">错误原因</td><td class="device-value"> {{#INFOANALYSIS.device_info.error | dealDisplay}} </td><td class="device-title"></td><td class="device-value"></td></tr> {{else if INFOANALYSIS.device_info.appOs.toLowerCase() == \'android\'}} <tr><td class="device-title">集成sdk版本号</td><td class="device-value"> {{#INFOANALYSIS.device_info.fpVersion | dealDisplay}}</td><td class="device-title">tokenId</td><td class="device-value"> {{#INFOANALYSIS.device_info.tokenId | dealDisplay}} </td></tr><tr><td class="device-title">设备ID</td><td class="device-value"> {{#INFOANALYSIS.device_info.deviceId | dealDisplay}}</td><td class="device-title">操作系统</td><td class="device-value"> {{#INFOANALYSIS.device_info.os | dealDisplay}} </td></tr><tr><td class="device-title">系统版本</td><td class="device-value"> {{#INFOANALYSIS.device_info.sdkVersion | dealDisplay}}</td><td class="device-title">发行版本</td><td class="device-value"> {{#INFOANALYSIS.device_info.releaseVersion | dealDisplay}}</td></tr><tr><td class="device-title">设备型号</td><td class="device-value"> {{#INFOANALYSIS.device_info.model | dealDisplay}}</td><td class="device-title">产品内部代码</td><td class="device-value"> {{#INFOANALYSIS.device_info.product | dealDisplay}}</td></tr><tr><td class="device-title">品牌</td><td class="device-value"> {{#INFOANALYSIS.device_info.brand | dealDisplay}}</td><td class="device-title">序列号</td><td class="device-value"> {{#INFOANALYSIS.device_info.serialNo | dealDisplay}}</td></tr><tr><td class="device-title">固件编号</td><td class="device-value"> {{#INFOANALYSIS.device_info.display | dealDisplay}}</td><td class="device-title">编译ROM的主机</td><td class="device-value"> {{#INFOANALYSIS.device_info.host | dealDisplay}}</td></tr><tr><td class="device-title">设备名称</td><td class="device-value"> {{#INFOANALYSIS.device_info.deviceName | dealDisplay}}</td><td class="device-title">硬件平台名称或者代号</td><td class="device-value"> {{#INFOANALYSIS.device_info.hardware | dealDisplay}}</td></tr><tr><td class="device-title">ROM标签</td><td class="device-value"> {{#INFOANALYSIS.device_info.tags | dealDisplay}}</td><td class="device-title">多个信息</td><td class="device-value"> {{#INFOANALYSIS.device_info.telephonyInfo | dealDisplay}}</td></tr><tr><td class="device-title">SVN号</td><td class="device-value"> {{#INFOANALYSIS.device_info.deviceSVN | dealDisplay}}</td><td class="device-title">wifi IP</td><td class="device-value"> {{#INFOANALYSIS.device_info.wifiIp | dealDisplay}}</td></tr><tr><td class="device-title">wifi mac地址</td><td class="device-value"> {{#INFOANALYSIS.device_info.wifiMac | dealDisplay}}</td><td class="device-title">wifi</td><td class="device-value"> {{#INFOANALYSIS.device_info.ssid | dealDisplay}}</td></tr><tr><td class="device-title">wifi BSSID</td><td class="device-value"> {{#INFOANALYSIS.device_info.bssid | dealDisplay}}</td><td class="device-title">网关</td><td class="device-value"> {{#INFOANALYSIS.device_info.gateway | dealDisplay}}</td></tr><tr><td class="device-title">子网掩码</td><td class="device-value"> {{#INFOANALYSIS.device_info.wifiNetmask | dealDisplay}}</td><td class="device-title">HTTP代理IP和端口</td><td class="device-value"> {{#INFOANALYSIS.device_info.proxyInfo | dealDisplay}}</td></tr><tr><td class="device-title">DNS</td><td class="device-value"> {{#INFOANALYSIS.device_info.dnsAddress | dealDisplay}}</td><td class="device-title">VPN IP</td><td class="device-value"> {{#INFOANALYSIS.device_info.vpnIp | dealDisplay}}</td></tr><tr><td class="device-title">VPN 子网掩码</td><td class="device-value"> {{#INFOANALYSIS.device_info.vpnNetmask | dealDisplay}}</td><td class="device-title">数据网络IP</td><td class="device-value"> {{#INFOANALYSIS.device_info.cellIp | dealDisplay}}</td></tr><tr><td class="device-title">网络类型</td><td class="device-value"> {{#INFOANALYSIS.device_info.networkType | dealDisplay}}</td><td class="device-title">当前时间</td><td class="device-value"> {{#INFOANALYSIS.device_info.currentTime | dealTime}}</td></tr><tr><td class="device-title">运行时间</td><td class="device-value"> {{#INFOANALYSIS.device_info.upTime | dealTimeLong}}</td><td class="device-title">开机时刻</td><td class="device-value"> {{#INFOANALYSIS.device_info.bootTime | dealTime}}</td></tr><tr><td class="device-title">是否ROOT</td><td class="device-value"> {{#INFOANALYSIS.device_info.root | dealDisplay}}</td><td class="device-title">应用包名</td><td class="device-value"> {{#INFOANALYSIS.device_info.packageName | dealDisplay}}</td></tr><tr><td class="device-title">应用版本号</td><td class="device-value"> {{#INFOANALYSIS.device_info.apkVersion | dealDisplay}}</td><td class="device-title">SDK core文件的md5</td><td class="device-value"> {{#INFOANALYSIS.device_info.sdkMd5 | dealDisplay}}</td></tr><tr><td class="device-title">APK签名文件的md5</td><td class="device-value"> {{#INFOANALYSIS.device_info.signMD5 | dealDisplay}}</td><td class="device-title">APK文件的md5</td><td class="device-value"> {{#INFOANALYSIS.device_info.apkMD5 | dealDisplay}}</td></tr><tr><td class="device-title">时区</td><td class="device-value"> {{#INFOANALYSIS.device_info.timeZone | dealDisplay}}</td><td class="device-title">语言</td><td class="device-value"> {{#INFOANALYSIS.device_info.language | dealDisplay}}</td></tr><tr><td class="device-title">屏幕亮度</td><td class="device-value"> {{#INFOANALYSIS.device_info.brightness | dealDisplay}}</td><td class="device-title">充电状态</td><td class="device-value"> {{#INFOANALYSIS.device_info.batteryStatus | dealDisplay}}</td></tr><tr><td class="device-title">电量</td><td class="device-value"> {{#INFOANALYSIS.device_info.batteryLevel | dealDisplay}}</td><td class="device-title">电池温度</td><td class="device-value"> {{#INFOANALYSIS.device_info.batteryTemp | dealDisplay}}</td></tr><tr><td class="device-title">屏幕分辨率</td><td class="device-value"> {{#INFOANALYSIS.device_info.screenRes | dealDisplay}}</td><td class="device-title">字体列表HASH</td><td class="device-value"> {{#INFOANALYSIS.device_info.fontHash | dealDisplay}}</td></tr><tr><td class="device-title">蓝牙MAC地址</td><td class="device-value"> {{#INFOANALYSIS.device_info.blueMac | dealDisplay}}</td><td class="device-title">系统初始化ID</td><td class="device-value"> {{#INFOANALYSIS.device_info.andriodId | dealDisplay}}</td></tr><tr><td class="device-title">CPU最大频率</td><td class="device-value"> {{#INFOANALYSIS.device_info.cpuFrequency | dealDisplay}}</td><td class="device-title">CPU硬件架构</td><td class="device-value"> {{#INFOANALYSIS.device_info.cpuHardware | dealDisplay}}</td></tr><tr><td class="device-title">CPU型号或者平台</td><td class="device-value"> {{#INFOANALYSIS.device_info.cpuType | dealDisplay}}</td><td class="device-title">内存大小</td><td class="device-value"> {{#INFOANALYSIS.device_info.totalMemory | dealMemory}}</td></tr><tr><td class="device-title">可用内存大小</td><td class="device-value"> {{#INFOANALYSIS.device_info.availableMemory | dealMemory}}</td><td class="device-title">基带版本</td><td class="device-value"> {{#INFOANALYSIS.device_info.basebandVersion | dealDisplay}}</td></tr><tr><td class="device-title">底层Linux内核版本</td><td class="device-value"> {{#INFOANALYSIS.device_info.kernelVersion | dealDisplay}}</td><td class="device-title">GPS坐标</td><td class="device-value"> {{#INFOANALYSIS.device_info.gpsLocation | dealDisplay}}</td></tr><tr><td class="device-title">启用位置模拟</td><td class="device-value"> {{#INFOANALYSIS.device_info.allowMockLocation | dealDisplay}}</td><td class="device-title">真实IP</td><td class="device-value"> {{#INFOANALYSIS.device_info.trueIp | dealDisplay}}</td></tr><tr><td class="device-title">设备类型</td><td class="device-value"> {{#INFOANALYSIS.device_info.appOs | dealDisplay}}</td><td class="device-title">错误原因</td><td class="device-value"> {{#INFOANALYSIS.device_info.error | dealDisplay}}</td></tr> {{/if}} </table></div></div></div> {{/if}} </div> {{/if}} {{each module_report as sub_report sub_report_index}} <div class="inner {{sub_report.report_name}}"><div class="module"><div class="risk-score"><div class="fl result-score-wrap"><div class="result-score-canvas-main"><canvas class="canvas-main"></canvas></div><div class="result-score-canvas-bg"><canvas class="canvas-bg"></canvas></div><div class="result-score-text"><div id="result-score" class="result-score"> {{sub_report.final_score}} </div><div class="result-cat"> {{if sub_report.final_decision =="PASS"}} 建议通过 {{else if sub_report.final_decision == "REJECT"}} 建议拒绝 {{else if sub_report.final_decision == "REVIEW"}} 建议审核 {{/if}} </div></div></div><div class="fl result-text"> {{if sub_report.final_decision == "PASS"}} <p class="tip">申请用户未检出高危风险，建议通过</p> {{else if sub_report.final_decision == "REJECT"}} <p class="tip">申请用户检测出高危风险，建议拒绝</p> {{else if sub_report.final_decision == "REVIEW"}} <p class="tip">申请用户存在较大风险，建议进行人工审核</p> {{/if}} <p class="result">共发现<a class="risk-count" href="javascript:void(0)"> {{if sub_report.risk_items}} {{sub_report.risk_items.length}} {{else}}0{{/if}}</a>异常信息 </p></div></div></div> {{if sub_report.output_fields}} <div class="module"><div class="output-field"><div class="module-title"><h2>自定义指标</h2></div><div class="rpi-subitem"> {{each sub_report.output_fields as output_field output_field_index}} <p class="p-field"><span class="p-field-title">{{output_field.field_name}}：</span><span class="p-field-value">{{output_field.field_value}}</span></p> {{/each}} </div></div></div> {{/if}} <div class="module"><div class="rp-item risk-items"><div class="module-title"><h2>{{sub_report.report_display_name}}风险情况</h2></div> {{if sub_report.risk_items && sub_report.risk_items.length>0}} <div class="table-wrap"><table class="risk-table"><thead><tr><th class="risk-col1">规则名称</th><th class="risk-col2">规则详情</th></tr></thead><tbody> {{each sub_report.risk_items as risk_item risk_item_index}} {{if risk_item}} <tr class="risk-items-flag border-bottom-2"><td class="risk-col1">{{risk_item.risk_name}}</td><td class="risk-col2"> {{#risk_item.risk_detail | riskDetailDisplay}} </td></tr> {{/if}} {{/each}} </tbody></table></div> {{else}} <div class="table-wrap" style="padding-left: 20px;">无风险</div> {{/if}} </div></div></div> {{/each}} </div></div>';

    var ruleDetailTemplate = '{{if risk_details && risk_details.length>0}} {{each risk_details as risk_detail risk_detail_index}} {{if risk_detail.type=="discredit_count"}} {{if risk_detail.description}} <div class="description"><span class="rule-desc">规则描述</span><span>{{risk_detail.description}}</span></div> {{/if}} <ul class="table-mark overdue-ul"> {{if risk_detail.discredit_times}} <li>逾期次数: {{risk_detail.discredit_times}}</li> {{if risk_detail.overdue_details && risk_detail.overdue_details.length>0}} {{each risk_detail.overdue_details as overdue overdue_index}} <li>逾期金额: {{overdue.overdue_amount_range}} 逾期笔数: {{overdue.overdue_count}} 逾期天数: {{overdue.overdue_day_range}} 逾期入库时间: {{overdue.overdue_time}} </li> {{/each}} {{/if}} {{/if}} </ul> {{/if}} {{if risk_detail.type=="custom_list"}} {{if risk_detail.description}} <div class="description"><span class="rule-desc">规则描述</span><span>{{risk_detail.description}}</span></div> {{/if}} <ul class="table-mark"> {{if risk_detail.high_risk_areas && risk_detail.high_risk_areas.length>0}} <li>高风险较为集中地区:{{#risk_detail.high_risk_areas | dealArray}} </li> {{else if risk_detail.hit_list_datas && risk_detail.hit_list_datas.length>0}} <li>命中列表数据:{{#risk_detail.hit_list_datas | dealArray}} </li> {{/if}} </ul> {{/if}} {{if risk_detail.type=="platform_detail"}} {{if risk_detail.description}} <div class="description"><span class="rule-desc">规则描述</span><span>{{risk_detail.description}}</span></div> {{/if}} <ul class="table-mark platform-ul"> {{if risk_detail.platform_detail}} <li style="list-style: none; margin-left: -16px;"> 总个数：{{risk_detail.platform_count}} </li> {{each risk_detail.platform_detail as platform platform_index}} <li>{{platform.industry_display_name}}：{{platform.count}}</li> {{/each}} {{/if}} {{if risk_detail.platform_detail_dimension && risk_detail.platform_detail_dimension.length>0}} <div class="dimension-section"><span class="dimension-title">各维度多头详情</span><ul class="dimension-list"> {{each risk_detail.platform_detail_dimension as dimension dimension_index}} <li class="dimension-item"><span class="dimension-item-title">{{dimension.dimension}}：</span><ul class="dimension-sub-list"><li>总个数：{{dimension.count}}</li> {{each dimension.detail as item item_index}} <li class="dimension-sub-item"> {{item.industry_display_name}}：{{item.count}} </li> {{/each}} </ul></li> {{/each}} </ul></div> {{/if}} </ul> {{/if}} {{if risk_detail.type==\'frequency_detail\'}} {{if risk_detail.description}} <div class="description"><span class="rule-desc">规则描述</span><span>{{risk_detail.description}}</span></div> {{/if}} <ul class="table-mark"> {{if risk_detail.frequency_detail_list && risk_detail.frequency_detail_list.length>0}} <div class="risk-detail-section"><span class="risk-detail-title">频度规则详情</span><ul class="risk-detail-list"> {{each risk_detail.frequency_detail_list as detail detail_index}} <li class="risk-detail-item"><span class="risk-detail-item-title"> {{detail.detail}} </span><ul class="risk-detail-sub-list"> {{each detail.data as data data_index}} <li class="risk-detail-sub-item">{{data}}</li> {{/each}} </ul></li> {{/each}} </ul></div> {{/if}} </ul> {{/if}} {{if risk_detail.type=="cross_frequency_detail"}} {{if risk_detail.description}} <div class="description"><span class="rule-desc">规则描述</span><span>{{risk_detail.description}}</span></div> {{/if}} <ul class="table-mark"> {{if risk_detail.cross_frequency_detail_list && risk_detail.cross_frequency_detail_list.length>0}} <div class="risk-detail-section"><span class="risk-detail-title">跨事件频度规则详情</span><ul class="risk-detail-list"> {{each risk_detail.cross_frequency_detail_list as detail defail_index}} <li class="risk-detail-item"><span class="risk-detail-item-title"> {{detail.detail}} </span></li><ul class="risk-detail-sub-list"> {{each detail.data as data data_index}} <li class="risk-detail-sub-item">{{data}}</li> {{/each}} </ul> {{/each}} </ul></div> {{/if}} </ul> {{/if}} {{if risk_detail.type=="suspected_team"}} {{if risk_detail.description}} <div class="description"><span class="rule-desc">规则描述</span><span>{{risk_detail.description}}</span></div> {{/if}} <ul class="table-mark"> {{if risk_detail.suspect_team_detail_list && risk_detail.suspect_team_detail_list.length>0}} <div class="dimension-section"><span class="dimension-title">复杂网络各维度详情</span> {{each risk_detail.suspect_team_detail_list as suspect_team_detail s_t_d_index}} <ul class="dimension-list"> {{if suspect_team_detail.dim_value}} <li class="dimension-item"><span><span class="dimension-item-title">匹配字段值:</span><span class="dimension-sub-item"> {{suspect_team_detail.dim_value}} </span></span></li> {{/if}} {{if suspect_team_detail.group_id}} <li class="dimension-item"><span class="dimension-item-title">疑似风险群体编号：</span><span class="dimension-sub-item"> {{suspect_team_detail.group_id}} </span></li> {{/if}} {{if suspect_team_detail.total_cnt}} <li class="dimension-item"><span class="dimension-item-title">疑似风险群体成员数：</span><span class="dimension-sub-item"> {{suspect_team_detail.total_cnt}} </span></li> {{/if}} {{if suspect_team_detail.node_dist}} <li class="dimension-item"><span class="dimension-item-title">成员分布:</span><span class="dimension-sub-item"> {{suspect_team_detail.node_dist}} </span></li> {{/if}} {{if suspect_team_detail.black_cnt}} <li class="dimension-item"><span class="dimension-item-title">风险名单成员数:</span><span class="dimension-sub-item"> {{suspect_team_detail.black_cnt}} </span></li> {{/if}} {{if suspect_team_detail.black_rat}} <li class="dimension-item"><span class="dimension-item-title">风险名单占比:</span><span class="dimension-sub-item"> {{suspect_team_detail.black_rat}} </span></li> {{/if}} {{if suspect_team_detail.fraud_dist}} <li class="dimension-item"><span class="dimension-item-title">风险名单分布:</span><span class="dimension-sub-item"> {{suspect_team_detail.fraud_dist}} </span></li> {{/if}} {{if suspect_team_detail.grey_cnt}} <li class="dimension-item"><span class="dimension-item-title">关注名单成员数:</span><span class="dimension-sub-item"> {{suspect_team_detail.grey_cnt}} </span></li> {{/if}} {{if suspect_team_detail.grey_rat}} <li class="dimension-item"><span class="dimension-item-title">关注名单占比:</span><span class="dimension-sub-item"> {{suspect_team_detail.grey_rat}} </span></li> {{/if}} {{if suspect_team_detail.degree}} <li class="dimension-item"><span class="dimension-item-title">一度关联节点个数:</span><span class="dimension-sub-item"> {{suspect_team_detail.degree}} </span></li> {{/if}} {{if suspect_team_detail.total_cnt_two}} <li class="dimension-item"><span class="dimension-item-title">二度关联节点个数:</span><span class="dimension-sub-item"> {{suspect_team_detail.total_cnt_two}} </span></li> {{/if}} {{if suspect_team_detail.black_cnt_one}} <li class="dimension-item"><span class="dimension-item-title">一度风险名单个数:</span><span class="dimension-sub-item"> {{suspect_team_detail.black_cnt_one}} </span></li> {{/if}} {{if suspect_team_detail.fraud_dist_one}} <li class="dimension-item"><span class="dimension-item-title">一度风险名单分布:</span><span class="dimension-sub-item"> {{suspect_team_detail.fraud_dist_one}} </span></li> {{/if}} {{if suspect_team_detail.black_cnt_two}} <li class="dimension-item"><span class="dimension-item-title">二度风险名单个数:</span><span class="dimension-sub-item"> {{suspect_team_detail.black_cnt_two}} </span></li> {{/if}} {{if suspect_team_detail.fraud_dist_two}} <li class="dimension-item"><span class="dimension-item-title">二度风险名单分布:</span><span class="dimension-sub-item"> {{suspect_team_detail.fraud_dist_two}} </span></li> {{/if}} {{if suspect_team_detail.black_dst}} <li class="dimension-item"><span class="dimension-item-title">风险节点距离:</span><span class="dimension-sub-item"> {{suspect_team_detail.black_dst}} </span></li> {{/if}} {{if suspect_team_detail.core_dst}} <li class="dimension-item"><span class="dimension-item-title">核心节点距离:</span><span class="dimension-sub-item"> {{suspect_team_detail.core_dst}} </span></li> {{/if}} {{if suspect_team_detail.node_score}}<li class="dimension-item"><span><span class="dimension-item-title">关联风险分:</span><span class="dimension-sub-item">{{suspect_team_detail.node_score}}</span></span></li>{{/if}}</ul><br/> {{/each}} </div> {{/if}} </ul> {{/if}} {{if risk_detail.type=="cross_event_detail"}} {{if risk_detail.description}} <div class="description"><span class="rule-desc">规则描述</span><span>{{risk_detail.description}}</span></div> {{/if}} <ul class="table-mark"> {{if risk_detail.cross_event_detail_list && risk_detail.cross_event_detail_list.length>0}} <div class="dimension-section"><span class="dimension-title">各维度详情</span><ul class="dimension-list"> {{each risk_detail.cross_event_detail_list as fieldDetail fieldDetail_index}} <li class="dimension-item"><span class="dimension-sub-item"> {{fieldDetail.detail}} </span></li> {{/each}} </ul></div> {{/if}} </ul> {{/if}} {{if risk_detail.type==\'grey_list\'}} {{if risk_detail.description}} <div class="description"><span class="rule-desc">规则描述</span><span>{{risk_detail.description}}</span></div> {{/if}} <ul class="name-list-ul"> {{if risk_detail.fraud_type_display_name}} <li><span>风险类型：</span><span class="name-list-value">{{risk_detail.fraud_type_display_name}}</span></li> {{/if}} {{if risk_detail.hit_type_display_name}} <li><span class="name-detail-em">匹配字段：</span><span class="name-list-value">{{risk_detail.hit_type_display_name}}</span></li> {{/if}} </ul> {{/if}} {{if risk_detail.type==\'black_list\'}} {{if risk_detail.description}} <div class="description"><span class="rule-desc">规则描述</span><span>{{risk_detail.description}}</span></div> {{/if}} <ul class="name-list-ul"> {{if risk_detail.fraud_type_display_name}} <li><span class="name-detail-em">风险类型：</span><span class="name-list-value">{{risk_detail.fraud_type_display_name}}</span></li> {{/if}} {{if risk_detail.hit_type_display_name}} <li><span class="name-detail-em">匹配字段：</span><span class="name-list-value">{{risk_detail.hit_type_display_name}}</span></li> {{/if}} {{if risk_detail.court_details && risk_detail.court_details.length>0}} <li style="color: #2ea5ff;"><a class=\'table-href\' href=\'javascript:void(0)\'> 法院详情(点击查看) </a><div class="a-detail"><div class="detail-a-close"><a href="javascript:void(0)" class="detail-close-x" id="detail-close"> X </a></div><div class="detail-table"> {{each risk_detail.court_details as court_detail court_detail_index}} <table> {{if court_detail.executed_name}} <tr><td class="col1">被执行人姓名:</td><td class="col2"> {{#court_detail.executed_name | dealDisplay}} </td></tr> {{/if}} {{if court_detail.gender}} <tr><td class="col1">性别:</td><td class="col2"> {{#court_detail.gender | dealDisplay}} </td></tr> {{/if}} {{if court_detail.age}} <tr><td class="col1">年龄:</td><td class="col2"> {{#court_detail.age | dealDisplay}} </td></tr> {{/if}} {{if court_detail.fraud_type_display_name}} <tr><td class="col1">风险类型:</td><td class="col2"> {{#court_detail.fraud_type_display_name | dealDisplay}} </td></tr> {{/if}} {{if court_detail.value}} <tr><td class="col1">命中的属性值:</td><td class="col2"> {{#court_detail.value | dealDisplay}} </td></tr> {{/if}} {{if court_detail.execute_court}} <tr><td class="col1">执行法院:</td><td class="col2"> {{#court_detail.execute_court | dealDisplay}} </td></tr> {{/if}} {{if court_detail.province}} <tr><td class="col1">省份:</td><td class="col2"> {{#court_detail.province | dealDisplay}} </td></tr> {{/if}} {{if court_detail.execute_code}} <tr><td class="col1">执行依据文号:</td><td class="col2"> {{#court_detail.execute_code | dealDisplay}} </td></tr> {{/if}} {{if court_detail.case_date}} <tr><td class="col1">立案时间:</td><td class="col2"> {{#court_detail.case_date | dealDisplay}} </td></tr> {{/if}} {{if court_detail.case_code}} <tr><td class="col1">案号:</td><td class="col2"> {{#court_detail.case_code | dealDisplay}} </td></tr> {{/if}} {{if court_detail.execute_subject}} <tr><td class="col1">执行标的:</td><td class="col2"> {{#court_detail.execute_subject | dealDisplay}} </td></tr> {{/if}} {{if court_detail.execute_status}} <tr><td class="col1">执行状态:</td><td class="col2"> {{#court_detail.execute_status | dealDisplay}} </td></tr> {{/if}} {{if court_detail.evidence_court}} <tr><td class="col1">做出依据执行法院:</td><td class="col2"> {{#court_detail.evidence_court | dealDisplay}} </td></tr> {{/if}} {{if court_detail.term_duty}} <tr><td class="col1">生效法律文书确定的义务:</td><td class="col2"> {{#court_detail.term_duty | dealDisplay}} </td></tr> {{/if}} {{if court_detail.carry_out}} <tr><td class="col1">被执行人履行情况:</td><td class="col2"> {{#court_detail.carry_out | dealDisplay}} </td></tr> {{/if}} {{if court_detail.specific_circumstances}} <tr><td class="col1">信贷逾期被执行人行为具体情形:</td><td class="col2"> {{#court_detail.specific_circumstances | dealDisplay}} </td></tr> {{/if}} </table> {{/each}} </div></div></li> {{/if}} </ul> {{/if}} {{if risk_detail.type=="fuzzy_black_list"}} {{if risk_detail.description}} <div class="description"><span class="rule-desc">规则描述</span><span>{{risk_detail.description}}</span></div> {{/if}} <ul class="name-list-ul"> {{if risk_detail.fuzzy_list_details && risk_detail.fuzzy_list_details.length>0}} {{each risk_detail.fuzzy_list_details as fuzzy_list fuzzy_list_index}} {{if fuzzy_list.fraud_type_display_name}} <li><span class="name-detail-em">风险类型：</span><span class="name-list-value"> {{fuzzy_list.fraud_type_display_name}} </span></li> {{/if}} {{if fuzzy_list.fuzzy_name}} <li><span class="name-detail-em">姓名：</span><span class="name-list-value"> {{fuzzy_list.fuzzy_name}} </span></li> {{/if}} {{if fuzzy_list.fuzzy_id_number}} <li><span class="name-detail-em">模糊身份证：</span><span class="name-list-value"> {{fuzzy_list.fuzzy_id_number}} </span></li> {{/if}} {{/each}} {{/if}} </ul> {{/if}} {{if (risk_detail_index+1) < risk_details.length}} <hr class="split-line"></hr> {{/if}} {{/each}} {{/if}}';
    //处理设备信息中没有数据或者是boolean 型的数据
    template.helper('dealDisplay', function (value) {
      var res = '';
      if (typeof value == 'boolean') {
        res = value ? '是' : '否';
      } else {
        res = value ? value : '-';
      }
      return res;
    });
    template.helper('dealTime', function (value) {
      var format;
      if (value) {
        value = value.toString();
        var dealVal = value.substr(0, value.length - 3);
        var date = new Date(parseInt(dealVal));
        var year = date.getFullYear();
        var month = date.getMonth() + 1;
        var day = date.getDate();
        var hour = date.getHours();
        var minute = date.getMinutes();
        var second = date.getSeconds();
        var monthFormat = (month.toString().length == 1) ? ("0" + month) : month;
        var dayFormat = (day.toString().length == 1) ? ("0" + day) : day;
        var hourFormat = (hour.toString().length == 1) ? ("0" + hour) : hour;
        var minuteFormat = (minute.toString().length == 1) ? ("0" + minute) : minute;
        var secondFormat = (second.toString().length == 1) ? ("0" + second) : second;
        format = year + '-' + monthFormat + '-' + dayFormat + ' ' + hourFormat + ':' + minuteFormat + ':' + secondFormat;
      } else {
        format = "-";
      }
      return format;
    });

    template.helper('dealTimeLong', function (value) {
      var time;
      if (value) {
        var days = value / 1000 / 60 / 60 / 24;
        var daysRound = Math.floor(days);
        var hours = value / 1000 / 60 / 60 - (24 * daysRound);
        var hoursRound = Math.floor(hours);
        var minutes = value / 1000 / 60 - (24 * 60 * daysRound) - (60 * hoursRound);
        var minutesRound = Math.floor(minutes);
        var seconds = value / 1000 - (24 * 60 * 60 * daysRound) - (60 * 60 * hoursRound) - (60 * minutesRound);
        var secondsRound = Math.floor(seconds);
        time = daysRound + "天" + hoursRound + "时" + minutesRound + "分" + secondsRound + "秒";
      } else {
        time = '-';
      }
      return time;
    });

    template.helper('dealMemory', function (value) {
      var rom;
      if(value){
        rom = (value / 1024 / 1024 / 1024).toFixed(2)+'GB';
      }else{
        rom='-'
      }

      return rom;
    });

    //规则详情
    template.helper('riskDetailDisplay', function (value) {
      if (!!value) {
        var render = template.compile(ruleDetailTemplate);
        var html = render({'risk_details': value});
        return html;
      } else {
        return '';
      }
    });
    template.helper('dealArray', function (value) {
      return value.join('，');
    });

    var render = template.compile(sourceReportTemplate);
    var html = render(templateData);
    if ($('body #tdReportContainer').length == 0) {
      $('body').append('<div id="tdReportContainer"></div>');
    }
    $('#tdReportContainer').html(html);

    //报告中的交互事件
    initShowModule();
    $('#tdReportContainer').show(200);

    //初始化
    function initShowModule() {
      //导航栏切换
      $('.nav-tab').on('click', function () {
        //每次点击都回到顶部
        $('.report-container').animate({'scrollTop': '0'}, 0);
        var _this = $(this);
        var otherTab = _this.siblings('.nav-tab');
        var childSpan = _this.children('span');
        var dataReport = childSpan.data('report');
        //修改标题
        $('.header-title').text(childSpan.text());
        //设置导航栏的颜色切换
        otherTab.removeClass('current-tab');
        _this.addClass('current-tab');
        //当前模块展示，其他模块关闭
        $('.' + dataReport).css('display', 'inline-block');
        $('.inner:not(".' + dataReport + '")').css('display', 'none');
        //绘制分数圆圈
        // $('canvas:not(".canvas-main, .canvas-bg")').remove();
        if ($('.' + dataReport + ' .canvas-main').length > 0) {
          var canvasMain = $('.' + dataReport + ' .canvas-main')[0];
          var mainCtx = canvasMain.getContext('2d');
          var score = parseInt($('.' + dataReport + ' .result-score').text(), 10);
          var resultCat = $('.' + dataReport + ' .result-cat').text();
          var i = 1;
          if (score > 0 && score < 100) {
            i = (100 - score) / 100;
          }
          if (score === 0) {
            i = 1;
          }
          mainCtx.rotate(-Math.PI / 2);
          mainCtx.lineWidth = 8;
          if (resultCat.indexOf('拒绝') > -1) {
            $('.' + dataReport + ' .result-cat').addClass('reject');
            mainCtx.strokeStyle = '#ff6c5c';
          } else if (resultCat.indexOf('通过') > -1) {
            $('.' + dataReport + ' .result-cat').addClass('accept');
            mainCtx.strokeStyle = '#8cdb65';
          } else if (resultCat.indexOf('审核') > -1) {
            $('.' + dataReport + ' .result-cat').addClass('review');
            mainCtx.strokeStyle = '#f8d436';
          }
          mainCtx.beginPath();
          mainCtx.arc(-75, 75, 69, 0, Math.PI * (i * 2), true);
          mainCtx.stroke();
          var canvasBg = $('.' + dataReport + ' .canvas-bg')[0];
          var bgCtx = canvasBg.getContext('2d');
          bgCtx.fillStyle = '#fafafa';
          bgCtx.beginPath();
          bgCtx.arc(61, 61, 52, 0, Math.PI * 2, true);
          bgCtx.closePath();
          bgCtx.fill();
        }
      });

      //初始化展示的inner
      $('.left-nav').children(':first').addClass('current-tab');
      var firstSpan = $('.current-tab').children('span');
      $('.header-title').text(firstSpan.text());
      var showReportClass = firstSpan.data('report');
      $('.inner:not(".' + showReportClass + '")').css('display', 'none');
      $('.left-nav').children('.nav-tab:first').trigger('click');

      //点击设备详情
      $('.jz').on('click', function () {
        $('.j-rpi-toggle-target').toggle(200);
      });
      //跳转
      $(".risk-count").on("click", function () {
        var _this = $(this);
        var innerParent = _this.parents('.inner');
        var riskItem = innerParent.find('.risk-items');
        if (riskItem.length > 0) {
          $('.report-container').animate({scrollTop: 0}, 0);
          $(".report-container").animate({scrollTop: riskItem.offset().top}, 300);
        }
      });

      //点击右上角X
      $('#report-a-close').on('click', function (e) {
        e.preventDefault();
        e.stopPropagation();
        // $('#tdReportContainer').hide(200);
      });

      // 点击空白处，报告关闭
      $('.report-container').on('click', function () {
        // $('#report-a-close').trigger('click');
      });
      $('.report-container-container').on('click', function (e) {
        e.preventDefault();
        e.stopPropagation();
      });

      // 多头详情点击下拉查看
      $('.dimension-title').on('click', function () {
        var dimensionList = $(this).next('.dimension-list');
        if (dimensionList.length) {
          dimensionList.slideToggle(200);
        }
      });

      //点击查看详情
      $(".risk-table").on("click", ".table-href", function () {
        $(".a-detail").hide();
        $(this).parents('ul').find('.a-detail').show(100);
      });

      $('.risk-detail-title').on('click', function () {
        var detailList = $(this).next('.risk-detail-list');
        if (detailList.length) {
          detailList.slideToggle(200);
        }
        //数据库中会出现这个※,换成*,主要原因太丑了
        var li = $(this).next('.risk-detail-list').find('.risk-detail-sub-list').children('li')
        $.each(li, function (index, value) {
          var text = $(value).text().replace(/[※]/g, '*');
          $(value).text(text);
        });
      });

      //详情关闭
      $(".detail-close-x").on("click", function (e) {
        $(this).parent().parent().hide(100);
      });
    }
  }
});


!function () {
  function a(a) {
    return a.replace(t, "").replace(u, ",").replace(v, "").replace(w, "").replace(x, "").split(y)
  }

  function b(a) {
    return "'" + a.replace(/('|\\)/g, "\\$1").replace(/\r/g, "\\r").replace(/\n/g, "\\n") + "'"
  }

  function c(c, d) {
    function e(a) {
      return m += a.split(/\n/).length - 1, k && (a = a.replace(/\s+/g, " ").replace(/<!--[\w\W]*?-->/g, "")), a && (a = s[1] + b(a) + s[2] + "\n"), a
    }

    function f(b) {
      var c = m;
      if (j ? b = j(b, d) : g && (b = b.replace(/\n/g, function () {
          return m++, "$line=" + m + ";"
        })), 0 === b.indexOf("=")) {
        var e = l && !/^=[=#]/.test(b);
        if (b = b.replace(/^=[=#]?|[\s;]*$/g, ""), e) {
          var f = b.replace(/\s*\([^\)]+\)/, "");
          n[f] || /^(include|print)$/.test(f) || (b = "$escape(" + b + ")")
        } else b = "$string(" + b + ")";
        b = s[1] + b + s[2]
      }
      return g && (b = "$line=" + c + ";" + b), r(a(b), function (a) {
        if (a && !p[a]) {
          var b;
          b = "print" === a ? u : "include" === a ? v : n[a] ? "$utils." + a : o[a] ? "$helpers." + a : "$data." + a, w += a + "=" + b + ",", p[a] = !0
        }
      }), b + "\n"
    }

    var g = d.debug, h = d.openTag, i = d.closeTag, j = d.parser, k = d.compress, l = d.escape, m = 1,
      p = {$data: 1, $filename: 1, $utils: 1, $helpers: 1, $out: 1, $line: 1}, q = "".trim,
      s = q ? ["$out='';", "$out+=", ";", "$out"] : ["$out=[];", "$out.push(", ");", "$out.join('')"],
      t = q ? "$out+=text;return $out;" : "$out.push(text);",
      u = "function(){var text=''.concat.apply('',arguments);" + t + "}",
      v = "function(filename,data){data=data||$data;var text=$utils.$include(filename,data,$filename);" + t + "}",
      w = "'use strict';var $utils=this,$helpers=$utils.$helpers," + (g ? "$line=0," : ""), x = s[0],
      y = "return new String(" + s[3] + ");";
    r(c.split(h), function (a) {
      a = a.split(i);
      var b = a[0], c = a[1];
      1 === a.length ? x += e(b) : (x += f(b), c && (x += e(c)))
    });
    var z = w + x + y;
    g && (z = "try{" + z + "}catch(e){throw {filename:$filename,name:'Render Error',message:e.message,line:$line,source:" + b(c) + ".split(/\\n/)[$line-1].replace(/^\\s+/,'')};}");
    try {
      var A = new Function("$data", "$filename", z);
      return A.prototype = n, A
    } catch (B) {
      throw B.temp = "function anonymous($data,$filename) {" + z + "}", B
    }
  }

  var d = function (a, b) {
    return "string" == typeof b ? q(b, {filename: a}) : g(a, b)
  };
  d.version = "3.0.0", d.config = function (a, b) {
    e[a] = b
  };
  var e = d.defaults = {openTag: "<%", closeTag: "%>", escape: !0, cache: !0, compress: !1, parser: null},
    f = d.cache = {};
  d.render = function (a, b) {
    return q(a, b)
  };
  var g = d.renderFile = function (a, b) {
    var c = d.get(a) || p({filename: a, name: "Render Error", message: "Template not found"});
    return b ? c(b) : c
  };
  d.get = function (a) {
    var b;
    if (f[a]) b = f[a]; else if ("object" == typeof document) {
      var c = document.getElementById(a);
      if (c) {
        var d = (c.value || c.innerHTML).replace(/^\s*|\s*$/g, "");
        b = q(d, {filename: a})
      }
    }
    return b
  };
  var h = function (a, b) {
    return "string" != typeof a && (b = typeof a, "number" === b ? a += "" : a = "function" === b ? h(a.call(a)) : ""), a
  }, i = {"<": "&#60;", ">": "&#62;", '"': "&#34;", "'": "&#39;", "&": "&#38;"}, j = function (a) {
    return i[a]
  }, k = function (a) {
    return h(a).replace(/&(?![\w#]+;)|[<>"']/g, j)
  }, l = Array.isArray || function (a) {
    return "[object Array]" === {}.toString.call(a)
  }, m = function (a, b) {
    var c, d;
    if (l(a)) for (c = 0, d = a.length; d > c; c++) b.call(a, a[c], c, a); else for (c in a) b.call(a, a[c], c)
  }, n = d.utils = {$helpers: {}, $include: g, $string: h, $escape: k, $each: m};
  d.helper = function (a, b) {
    o[a] = b
  };
  var o = d.helpers = n.$helpers;
  d.onerror = function (a) {
    var b = "Template Error\n\n";
    for (var c in a) b += "<" + c + ">\n" + a[c] + "\n\n";
    "object" == typeof console && console.error(b)
  };
  var p = function (a) {
      return d.onerror(a), function () {
        return "{Template Error}"
      }
    }, q = d.compile = function (a, b) {
      function d(c) {
        try {
          return new i(c, h) + ""
        } catch (d) {
          return b.debug ? p(d)() : (b.debug = !0, q(a, b)(c))
        }
      }

      b = b || {};
      for (var g in e) void 0 === b[g] && (b[g] = e[g]);
      var h = b.filename;
      try {
        var i = c(a, b)
      } catch (j) {
        return j.filename = h || "anonymous", j.name = "Syntax Error", p(j)
      }
      return d.prototype = i.prototype, d.toString = function () {
        return i.toString()
      }, h && b.cache && (f[h] = d), d
    }, r = n.$each,
    s = "break,case,catch,continue,debugger,default,delete,do,else,false,finally,for,function,if,in,instanceof,new,null,return,switch,this,throw,true,try,typeof,var,void,while,with,abstract,boolean,byte,char,class,const,double,enum,export,extends,final,float,goto,implements,import,int,interface,long,native,package,private,protected,public,short,static,super,synchronized,throws,transient,volatile,arguments,let,yield,undefined",
    t = /\/\*[\w\W]*?\*\/|\/\/[^\n]*\n|\/\/[^\n]*$|"(?:[^"\\]|\\[\w\W])*"|'(?:[^'\\]|\\[\w\W])*'|\s*\.\s*[$\w\.]+/g,
    u = /[^\w$]+/g, v = new RegExp(["\\b" + s.replace(/,/g, "\\b|\\b") + "\\b"].join("|"), "g"),
    w = /^\d[^,]*|,\d[^,]*/g, x = /^,+|,+$/g, y = /^$|,+/;
  e.openTag = "{{", e.closeTag = "}}";
  var z = function (a, b) {
    var c = b.split(":"), d = c.shift(), e = c.join(":") || "";
    return e && (e = ", " + e), "$helpers." + d + "(" + a + e + ")"
  };
  e.parser = function (a) {
    a = a.replace(/^\s/, "");
    var b = a.split(" "), c = b.shift(), e = b.join(" ");
    switch (c) {
      case"if":
        a = "if(" + e + "){";
        break;
      case"else":
        b = "if" === b.shift() ? " if(" + b.join(" ") + ")" : "", a = "}else" + b + "{";
        break;
      case"/if":
        a = "}";
        break;
      case"each":
        var f = b[0] || "$data", g = b[1] || "as", h = b[2] || "$value", i = b[3] || "$index", j = h + "," + i;
        "as" !== g && (f = "[]"), a = "$each(" + f + ",function(" + j + "){";
        break;
      case"/each":
        a = "});";
        break;
      case"echo":
        a = "print(" + e + ");";
        break;
      case"print":
      case"include":
        a = c + "(" + b.join(",") + ");";
        break;
      default:
        if (/^\s*\|\s*[\w\$]/.test(e)) {
          var k = !0;
          0 === a.indexOf("#") && (a = a.substr(1), k = !1);
          for (var l = 0, m = a.split("|"), n = m.length, o = m[l++]; n > l; l++) o = z(o, m[l]);
          a = (k ? "=" : "=#") + o
        } else a = d.helpers[c] ? "=#" + c + "(" + b.join(",") + ");" : "=" + a
    }
    return a
  }, "function" == typeof define ? define(function () {
    return d
  }) : "undefined" != typeof exports ? module.exports = d : this.template = d
}();