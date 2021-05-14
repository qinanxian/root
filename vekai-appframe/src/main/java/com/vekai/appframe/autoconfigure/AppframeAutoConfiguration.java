package com.vekai.appframe.autoconfigure;


import com.vekai.appframe.dataform.SessionEnvConst;
import com.vekai.auth.entity.User;
import com.vekai.auth.holder.AuthHolder;
import com.vekai.auth.service.OrgService;
import com.vekai.dataform.context.ContextEnvFetcher;
import cn.fisok.raw.kit.ListKit;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.kit.SQLKit;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@EnableConfigurationProperties(AppframeProperties.class)
@ComponentScan(basePackages={"com.vekai.appframe"})
public class AppframeAutoConfiguration {

    private AppframeProperties properties;

    public AppframeAutoConfiguration(AppframeProperties properties) {
        this.properties = properties;
    }

    /**
     * 定义一个取上下文环境变量的获取器，主要用来给DataForm注入当前会话用户，机构等信息
     * @return
     */
    @Bean
    public ContextEnvFetcher getContextEnvFetcher(OrgService orgService){
        ContextEnvFetcher fetcher = new ContextEnvFetcher(){
            public Map<String, Object> fetchContextEnvParams() {
                Map<String,Object> params = MapKit.newLinkedHashMap();
                params.put(SessionEnvConst.CUR_USER,SessionEnvConst.NULL_HOLDER);
                params.put(SessionEnvConst.CUR_USER_ORG,SessionEnvConst.NULL_HOLDER);
                params.put(SessionEnvConst.CUR_USER_SUBORG_LIST,SessionEnvConst.NULL_LIST_HOLDER);
                params.put(SessionEnvConst.CUR_USER_OFFICE_ORG_LIST,SessionEnvConst.NULL_LIST_HOLDER);
                params.put(SessionEnvConst.CUR_USER_OFFICE_SUBORG_LIST,SessionEnvConst.NULL_LIST_HOLDER);

                //计算当前用户及当前机构
                User user = AuthHolder.getUser();
                if(user != null){
                    params.put(SessionEnvConst.CUR_USER,user.getId());
                    params.put(SessionEnvConst.CUR_USER_ORG,user.getOrgId());
                }

                SQLKit.LOG_SQL_CTX_PARAM_LIST = ListKit.listOf(
                        SessionEnvConst.CUR_USER,
                        SessionEnvConst.CUR_USER_ORG,
                        SessionEnvConst.CUR_USER_SUBORG_LIST,
                        SessionEnvConst.CUR_USER_OFFICE_ORG_LIST,
                        SessionEnvConst.CUR_USER_OFFICE_SUBORG_LIST
                );

                return params;
            }
        };
        return fetcher;
    }
}
