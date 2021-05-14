package com.vekai.workflow.controller;

import cn.fisok.web.holder.WebHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Base拆分后， 为了保持兼容性而实现
 */
@Controller
@Deprecated
public class CompatibleController {
    @Deprecated
    @RequestMapping(path = "/base/profile.js",method = RequestMethod.GET)
    public void profile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Content-Type","application/javascript");
        StringBuffer scriptBuffer = new StringBuffer();
        scriptBuffer.append("var $vekaiProfile={").append("\n");
        scriptBuffer.append("    contextPath:\"").append(WebHolder.getServletContext().getContextPath()).append("\"\n");
        scriptBuffer.append("};").append("\n");

        PrintWriter writer = response.getWriter();
        writer.print(scriptBuffer);
        writer.flush();
    }
}
