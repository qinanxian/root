package com.vekai.crops.obiz.application.dector;

import com.vekai.crops.obiz.application.entity.ObizApplication;
import com.vekai.base.detector.DetectorContext;
import com.vekai.base.detector.DetectorItemExecutor;
import com.vekai.base.detector.DetectorMessage;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 检查申请信息的完整性
 */
@Component
public class ApplicationCompleteCheck implements DetectorItemExecutor {
    public DetectorMessage exec(DetectorContext ctx) {
        DetectorMessage ret = new DetectorMessage();

        ObizApplication obizApplication = ctx.getParam("application").objectVal(ObizApplication.class);
        if("Y".equals(obizApplication.getIsDraft())){
            ret.setPass(false);
            ret.setMessage(Arrays.asList("请填写完申请信息表"));
        }else{
            ret.setPass(true);
        }

        return ret;
    }
}
