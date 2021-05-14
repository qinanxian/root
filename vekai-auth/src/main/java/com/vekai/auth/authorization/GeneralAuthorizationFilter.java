package com.vekai.auth.authorization;


import com.vekai.auth.AuthConsts;
import com.vekai.auth.autoconfigure.AuthProperties;
import com.vekai.auth.shiro.CustomWildcardPermission;
import com.vekai.auth.shiro.ReadWriteType;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.apache.shiro.web.servlet.ProxiedFilterChain;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class GeneralAuthorizationFilter extends AuthorizationFilter implements Ordered{

    final Logger logger = LoggerFactory.getLogger(getClass());

    public final static String DATA_FORM_PREFIX = "dataform";
    public final static String DEV_TOOLS_PREFIX = "devtool";
    public final static String ADMIN_ROLE = "admin";

    public final static String[] DATA_FORM_PATH_SEGMENTS =
            { DATA_FORM_PREFIX, "meta", "data", "list", "one", "save", "delete", "invoke", "validate",
                    "text", "excel", "element-dict-tree", "text-data"};

    static {
        Arrays.sort(DATA_FORM_PATH_SEGMENTS);
    }

    @Autowired
    private AuthProperties authProperties;

    @Autowired(required = false)
    private List<AfterAuthorizationListener> afterAuthorizationListeners;

    @Override
    public void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        HttpMethod httpMethod = HttpMethod.valueOf(((HttpServletRequest)request).getMethod());
        if (HttpMethod.OPTIONS == httpMethod || !authProperties.isAuthzFilterEnabled()) {
            chain.doFilter(request, response);
            return;
        }
        if (!(chain instanceof ProxiedFilterChain)) {
            request.removeAttribute(getAlreadyFilteredAttributeName());
            chain.doFilter(request, response);
        }  else {
            super.doFilterInternal(request, response, chain);
        }
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        String url = WebUtils.getPathWithinApplication(httpRequest);
        if (null == url) return true;
        if (url.startsWith("/"))
            url = url.substring(1);
        Subject subject = SecurityUtils.getSubject();
        boolean res;
        if (isDevTool(url)) {
            res = processDevTools(url, subject);
        } else if (isDataForm(url)) {
            res = processDataForm(url, subject);
        } else {
            res = processGeneralWebApi(url, subject);
        }
        if (null != afterAuthorizationListeners && !afterAuthorizationListeners.isEmpty()) {
            AfterAuthorizationListener.AuthValidateEvent event = new AfterAuthorizationListener.AuthValidateEvent(res);
            for (AfterAuthorizationListener listener : afterAuthorizationListeners) {
                try {
                    listener.afterAuthValidate(event);
                } catch (Throwable throwable) {
                    logger.error("execute AfterAuthorizationListener exception", throwable);
                }
            }
        }
        return res;
    }


    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(AuthConsts.AUTH_FAIL_STATUS_CODE);
        httpResponse.addHeader(AuthConsts.AUTH_FAIL_HEADER, getAuthFailReason(request));
        try {
            request.getRequestDispatcher(AuthConsts.NOP_URI).forward(request, response);
        } catch (ServletException ex) {
            logger.warn(ex.getMessage());
        }
        return false;

    }

    protected String getAuthFailReason(ServletRequest request) {
        return AuthConsts.UNAUTHORIZATION;
    }

    /**
     *
     * @param url
     * @return true indicate is allowed to access, otherwise forbidden
     */
    private boolean processDataForm(String url, Subject subject) {
        String[] pathSegs = url.split("/");
        String dataFormId = null;
        for (String pathSeg : pathSegs) {
            int index = Arrays.binarySearch(DATA_FORM_PATH_SEGMENTS, pathSeg);
            if (index < 0) {
                dataFormId = pathSeg;
                break;
            }
        }
        if (null == dataFormId) {
            logger.warn("canot find dataformId from dataform url {}", url);
            return true;
        }
        logger.debug("check dataformId: {}", dataFormId);
        String dataFormResourceCode = dataFormId.replace('-', '/');
        return subject.isPermitted(CustomWildcardPermission.constructPermit(
                ReadWriteType.All, PermissionResourceType.DataForm, dataFormResourceCode));
    }

    private boolean processGeneralWebApi(String url, Subject subject) {
        return subject.isPermitted(CustomWildcardPermission.constructPermit(
                ReadWriteType.All, PermissionResourceType.Rest, url));
    }

    private boolean processDevTools(String url, Subject subject) {
        return subject.hasRole(ADMIN_ROLE);
    }

    private boolean isDataForm(String url) {
        return url.startsWith(DATA_FORM_PREFIX);
    }

    private boolean isDevTool(String url) {
        return url.startsWith(DEV_TOOLS_PREFIX);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    public boolean isEnabled() {
        return authProperties.isAuthzFilterEnabled();
    }
}
