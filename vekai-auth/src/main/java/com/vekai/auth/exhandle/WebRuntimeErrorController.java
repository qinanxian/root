package com.vekai.auth.exhandle;

import cn.fisok.web.exhandle.ExceptionModel;
import com.vekai.auth.AuthConsts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class WebRuntimeErrorController implements ErrorController {
    private final static Logger LOGGER = LoggerFactory.getLogger(WebRuntimeErrorController.class);
    public static final String ERROR_PATH = "/error";

    @Autowired
    ErrorAttributes errorAttributes;

    @RequestMapping(path = {WebRuntimeErrorController.ERROR_PATH}, produces = "text/html")
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        return null; // todo
    }

    @RequestMapping(path = {ERROR_PATH})
    @ResponseBody
    public ExceptionModel handleError(HttpServletRequest request, HttpServletResponse response) {
        WebRequest webRequest = new ServletWebRequest(request);
        LOGGER.error("unexpected error occurs", errorAttributes.getError(webRequest));
        ExceptionModel em =  new ExceptionModel();
        em.setType(ExceptionModel.EXCEPTION_TYPE_SYS);
        em.setMessage("error");
        em.setCode(""+response.getStatus());
        return em;
    }

    @RequestMapping(path = {AuthConsts.NOP_HANDLER})
    @ResponseBody
    public void nop(){
    }

    public String getErrorPath() {
        return ERROR_PATH;
    }
}
