package com.vekai.crops.consumenow.controller;

import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.lang.ContentType;
import cn.fisok.web.kit.HttpKit;
import com.vekai.crops.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.Map;

@Controller
@RequestMapping("/maxf/consumenow")
public class ReportExportController {
    protected static Logger logger = LoggerFactory.getLogger(ReportExportController.class);


    /**
     * 马上消费，报表导出
     *
     * @param startDate
     * @param endDate
     */
    @ResponseBody
    @GetMapping(value = "/doFile/{startDate}/{endDate}")
    public void msxfFile(HttpServletResponse response, HttpServletRequest request,
                         @PathVariable("startDate") String startDate, @PathVariable("endDate") String endDate) {
        logger.info("开始请求报表导出");
        try {
            String url="http://18.10.18.33:18000/exprot/excel?startDate="+startDate+"&endDate="+endDate;
            logger.info(url+"请求的URL路径");
            InputStream inputStream = HttpUtil.doGetStream(url);
            String fileName = cn.fisok.web.kit.HttpKit.iso8859("决策报表.xls",request);
            Map<String,String> headers = MapKit.mapOf("Content-Disposition","attachment; filename=" + fileName);
            HttpKit.renderStream(response,inputStream, ContentType.XLS,headers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}