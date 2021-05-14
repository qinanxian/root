package com.vekai.crops.obiz.application.dector;


import com.vekai.crops.obiz.application.entity.ObizApplication;
import com.vekai.base.detector.DetectorContext;
import com.vekai.base.detector.DetectorItemExecutor;
import com.vekai.base.detector.DetectorMessage;
import cn.fisok.raw.kit.StringKit;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 产品配置信息完整性检查
 */
@Component
public class PolicyCompleteCheck implements DetectorItemExecutor {
    public DetectorMessage exec(DetectorContext ctx) {
        DetectorMessage ret = new DetectorMessage();

        //1.显示模板
        //2.资料清单
        //3.合同模板
        //4.调查报告
        //4.业务流程
        //4.业务里程碑

        ObizApplication obizApplication = ctx.getParam("application").objectVal(ObizApplication.class);
        List<String> msgList = new ArrayList<String>();

        if(StringKit.isBlank(obizApplication.getDataformId()))msgList.add("显示模板数据缺失");
        if(StringKit.isBlank(obizApplication.getDossierId()))msgList.add("资料清单数据缺失");
//        if(StringKit.isBlank(obizApplication.getContractDocDefKey()))msgList.add("合同模板数据缺失");
        if(StringKit.isBlank(obizApplication.getInquireDocId()))msgList.add("调查报告数据缺失");
        if(StringKit.isBlank(obizApplication.getLandmarkId()))msgList.add("业务里程碑数据缺失");

        if (msgList.size() == 0) {
            ret.setPass(true);
        } else {
            ret.setPass(false);
            ret.setMessage(msgList);
        }

        return ret;
    }
}
