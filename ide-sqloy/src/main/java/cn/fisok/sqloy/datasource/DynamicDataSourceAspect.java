/*
 * Copyright 2019-2029 FISOK(www.fisok.cn).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.fisok.sqloy.datasource;

import cn.fisok.raw.kit.StringKit;
import cn.fisok.sqloy.core.SqlConsts;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2020/1/5
 * @desc : 动态数据源切换器,只要方法使用了UseDataSource注解，就切换
 */
@Aspect
@Component
public class DynamicDataSourceAspect implements Ordered {

    /**
     * within在类上设置,annotation在方法上进行设置
     */
//    @Pointcut("@annotation(cn.fisok.sql.datasource.UseDataSource)")
//    public void dataSourcePointCut() {
//
//    }

//    @Around("dataSourcePointCut()")

    /**
     * within在类上设置,annotation在方法上进行设置
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("@annotation(cn.fisok.sqloy.datasource.UseDataSource)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        UseDataSource anno = method.getAnnotation(UseDataSource.class);
        if(anno == null){
            String dsKey = StringKit.nvl(anno.value(), SqlConsts.DS_MASTER);
            DynamicDataSourceHolder.setDataSourceKey(dsKey);
        }else {
            DynamicDataSourceHolder.setDataSourceKey(anno.value());
        }

        try {
            return point.proceed();
        } finally {
//            DynamicDataSourceHolder.clear();
        }
    }

    public int getOrder() {
        return 1;
    }
}
