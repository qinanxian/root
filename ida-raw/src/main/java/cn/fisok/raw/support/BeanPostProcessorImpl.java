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
package cn.fisok.raw.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/12/28 23:42
 * @desc :
 */
public class BeanPostProcessorImpl implements BeanPostProcessor {

    public BeanPostProcessorImpl() {

    }
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof RequestMappingHandlerMapping) {
            //当它被设置为true后，总是使用当前servlet上下文中的全路径进行URL查找，否则使用当前servlet映射内的路径。默认为false。
            ((RequestMappingHandlerMapping) bean).setAlwaysUseFullPath(false);
        }
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        /*if(bean instanceof BeanSelfAware){
            BeanSelfAware myBean = (BeanSelfAware)bean;
            myBean.setSelf((BeanSelfAware)bean);
            return myBean;
        }*/
        return bean;
    }
}