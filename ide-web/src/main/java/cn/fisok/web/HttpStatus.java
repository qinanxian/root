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
package cn.fisok.web;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2020/8/1
 * @desc :
 * 200 - 确定。客户端请求已成功。
 * 201 - 已创建。
 * 202 - 已接受。
 * 203 - 非权威性信息。
 * 204 - 无内容。
 * 205 - 重置内容。
 * 206 - 部分内容。
 *
 * 302 - 对象已移动。
 * 304 - 未修改。
 * 307 - 临时重定向。
 *
 *
 * 400 - 请求无效
 * 401.1 - 未授权：登录失败
 * 401.2 - 未授权：服务器配置问题导致登录失败
 * 401.3 - ACL 禁止访问资源
 * 401.4 - 未授权：授权被筛选器拒绝
 * 401.5 - 未授权：ISAPI 或 CGI 授权失败
 * 403 - 禁止访问
 * 403 - 对 Internet 服务管理器 的访问仅限于 Localhost
 * 403.1 禁止访问：禁止可执行访问
 * 403.2 - 禁止访问：禁止读访问
 * 403.3 - 禁止访问：禁止写访问
 * 403.4 - 禁止访问：要求 SSL
 * 403.5 - 禁止访问：要求 SSL 128
 * 403.6 - 禁止访问：IP 地址被拒绝
 * 403.7 - 禁止访问：要求客户证书
 * 403.8 - 禁止访问：禁止站点访问
 * 403.9 - 禁止访问：连接的用户过多
 * 403.10 - 禁止访问：配置无效
 * 403.11 - 禁止访问：密码更改
 * 403.12 - 禁止访问：映射器拒绝访问
 * 403.13 - 禁止访问：客户证书已被吊销
 * 403.15 - 禁止访问：客户访问许可过多
 * 403.16 - 禁止访问：客户证书不可信或者无效
 * 403.17 - 禁止访问：客户证书已经到期或者尚未生效 HTTP 404.1 -
 *
 * 404- 无法找到文件
 * 405 - 资源被禁止
 * 406 - 无法接受
 * 407 - 要求代理身份验证
 * 410 - 永远不可用
 * 412 - 先决条件失败
 * 414 - 请求 - URI 太长
 *
 * 500 - 内部服务器错误
 * 500.100 - 内部服务器错误
 * 500-11 服务器关闭
 * 500-12 应用程序重新启动
 * 500-13 - 服务器太忙
 * 500-14 - 应用程序无效
 * 500-15 - 不允许请求 global.asa
 * 501 - 未实现
 * 502 - 网关错误
 */
public enum HttpStatus {
    OK("200", "OK","客户端请求已成功"),

    UNAUTHORIZED_1("401.1", "Unauthorized 1","登录失败"),
    FORBIDDEN("403", "Forbidden","禁止访问"),
    FORBIDDEN_1("403.1", "Forbidden 1","没有访问权限"),
    FORBIDDEN_7("403.7", "Forbidden 7","非法的访问，没有令牌"),
    FORBIDDEN_16("403.16", "Forbidden 16","非法的访问，访问令牌无效"),
    FORBIDDEN_17("403.17", "Forbidden 17","会话超时"),
    NOT_FOUND("404", "Not Found","无法找到文件"),
    METHOD_NOT_ALLOWED("405", "Method Not Allowed","资源被禁止"),
    INTERNAL_SERVER_ERROR("500.100", "Internal Server Error","内部服务器错误"),
    ;
    private final String value;
    private final String reasonPhrase;
    private final String explainText;

    HttpStatus(String value, String reasonPhrase, String explainText) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
        this.explainText = explainText;
    }

    public String getValue() {
        return value;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public String getExplainText() {
        return explainText;
    }
}
