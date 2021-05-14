package com.vekai.auth.tags;

import com.vekai.auth.entity.User;
import com.vekai.auth.service.AuthService;
import freemarker.core.Environment;
import freemarker.template.*;
import cn.fisok.raw.holder.ApplicationContextHolder;
import cn.fisok.raw.kit.StringKit;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class UserTag implements TemplateDirectiveModel{
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        Writer out = env.getOut();

        //将模版里的数字参数转化成int类型的方法，，其它类型的转换请看freemarker文档
        TemplateModel paramValue = (TemplateModel) params.get("userId");
        if(paramValue instanceof SimpleScalar){
            String userId = ((SimpleScalar)paramValue).getAsString();
            if(StringKit.isNotBlank(userId)){
                AuthService authService = ApplicationContextHolder.getBean(AuthService.class);
                User user = authService.getUser(userId);
                if(user!=null){
                    out.write(user.getName());
                }
            }
        }
//        int num = ((TemplateNumberModel) paramValue).getAsNumber().intValue();
//        User user = AuthHolder.getUser();
//        out.write("这是一段文字");

        if (body != null) {
            body.render(env.getOut());
        }
    }
}
