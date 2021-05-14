package com.vekai.auth.interceptor;

import com.vekai.auth.AuthConsts;
import com.vekai.auth.audit.model.AuditDataObject;
import com.vekai.auth.entity.User;
import com.vekai.auth.holder.AuditHolder;
import com.vekai.auth.holder.AuthHolder;
import cn.fisok.raw.kit.StringKit;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

public class AuthHolderInterceptor implements HandlerInterceptor {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public  String uuid() {
        String uuid = UUID.randomUUID().toString();	//获取UUID并转化为String对象
        uuid = uuid.replace("-", "");				//因为UUID本身为32位只是生成时多了“-”，所以将它们去点就可
        return uuid;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        AuthHolder.clear();
        User user = null == subject.getPrincipals() ? null : subject.getPrincipals().oneByType(User.class);
        if (user != null) {
            AuthHolder.setUser(user);
        }

        //记录请求跟踪号
        HttpSession session = request.getSession(false);
        if (null == session) return true;
        String requestId = StringKit.nvl(request.getAttribute("REQUEST_ID"),"");

        String sessionId = session.getId();

        AuditDataObject auditDataObject = new AuditDataObject();
        auditDataObject.setRequestId(requestId);
        auditDataObject.setSessionId(sessionId);
        AuditHolder.setDataObject(auditDataObject);

        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = AuthHolder.getUser();
        if(user==null){
            user = new User();
            user.setCode("anon");
            user.setName("匿名用户");
        }
        if(modelAndView!=null){
            modelAndView.getModelMap().put(AuthConsts.SESSION_USER,user);
        }
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        AuthHolder.clear();//资源清理
        AuditHolder.clear();
    }
}
