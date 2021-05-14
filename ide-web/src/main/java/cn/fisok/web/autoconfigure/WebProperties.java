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
package cn.fisok.web.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2020/1/10
 * @desc : WEB运行支持属性配置类
 */
@ConfigurationProperties(prefix = "cn.fisok.web", ignoreUnknownFields = true)
public class WebProperties {
    private long multipartMaxFileSize = 1024L * 1024L * 100;//最大上传文件大小，100M
    private String multipartTempLocation = "/tmp/fisok";
    private Cross cross = new Cross();

    public long getMultipartMaxFileSize() {
        return multipartMaxFileSize;
    }

    public void setMultipartMaxFileSize(long multipartMaxFileSize) {
        this.multipartMaxFileSize = multipartMaxFileSize;
    }

    public String getMultipartTempLocation() {
        return multipartTempLocation;
    }

    public void setMultipartTempLocation(String multipartTempLocation) {
        this.multipartTempLocation = multipartTempLocation;
    }

    public Cross getCross() {
        return cross;
    }

    public void setCross(Cross cross) {
        this.cross = cross;
    }

    /**
     * 跨域配置信息
     */
    public class Cross {
        private boolean corsEnable = false;
        private String pathPattern = "/**";
        private String allowedHeaders = "*";
        private String[] allowedMethods = new String[] {"*"};
        private String allowedOrigins = "*";

        public boolean isCorsEnable() {
            return corsEnable;
        }

        public void setCorsEnable(boolean corsEnable) {
            this.corsEnable = corsEnable;
        }

        public String getPathPattern() {
            return pathPattern;
        }

        public void setPathPattern(String pathPattern) {
            this.pathPattern = pathPattern;
        }

        public String getAllowedHeaders() {
            return allowedHeaders;
        }

        public void setAllowedHeaders(String allowedHeaders) {
            this.allowedHeaders = allowedHeaders;
        }

        public String[] getAllowedMethods() {
            return allowedMethods;
        }

        public void setAllowedMethods(String[] allowedMethods) {
            this.allowedMethods = allowedMethods;
        }

        public String getAllowedOrigins() {
            return allowedOrigins;
        }

        public void setAllowedOrigins(String allowedOrigins) {
            this.allowedOrigins = allowedOrigins;
        }
    }
}
