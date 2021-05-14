package com.vekai.showcase.detector.executor;

import com.vekai.base.detector.DetectorContext;
import com.vekai.base.detector.DetectorItemExecutor;
import com.vekai.base.detector.DetectorMessage;
import org.springframework.stereotype.Component;

@Component
public class DemoExecutor1 implements DetectorItemExecutor {
    public DetectorMessage exec(DetectorContext ctx) {
        DetectorMessage ret = new DetectorMessage();

        ret.setPass(true);

        return ret;
    }
}
