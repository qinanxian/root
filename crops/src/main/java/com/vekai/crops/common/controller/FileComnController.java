package com.vekai.crops.common.controller;

import cn.fisok.sqloy.core.BeanCruder;
import cn.fisok.web.kit.HttpKit;
import com.vekai.crops.common.service.ActivityService;
import com.vekai.crops.common.service.FileGatwayServiceImpl;
import com.vekai.crops.common.service.HolidayConfigurAtionService;
import com.vekai.crops.common.service.WhiteListOfNetworkService;
import com.vekai.crops.customer.entity.CustomerInfoPO;
import com.vekai.crops.fileman.FileManController;
import com.vekai.office.excel.imports.config.ConfigException;
import com.vekai.office.excel.imports.config.ExcelImportConfig;
import com.vekai.office.excel.imports.config.ExcelImportConfigLoader;
import com.vekai.office.excel.imports.config.ExcelImportConfigLoaderFromExcel;
import com.vekai.office.excel.imports.exception.ImportExecutorException;
import com.vekai.office.excel.imports.executor.DataGridParseToDBImpl;
import com.vekai.office.excel.imports.intercept.DataProcessIntercept;
import com.vekai.office.excel.imports.intercept.DataRowConvertIntercept;
import com.vekai.office.excel.imports.intercept.InterceptException;
import com.vekai.office.excel.reader.ExcelRowData;
import cn.fisok.raw.kit.IOKit;
import cn.fisok.raw.kit.JSONKit;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.lang.BizException;
import cn.fisok.raw.lang.ContentType;
import cn.fisok.raw.lang.FisokException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Map;

@RequestMapping("/comn/file")
@RestController
public class FileComnController {
    @Autowired
    WhiteListOfNetworkService whiteListOfNetworkservice;

    protected static Logger logger = LoggerFactory.getLogger(FileManController.class);
    @Autowired
    private HolidayConfigurAtionService holidayConfigurAtionService;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    private BeanCruder beanCruder;

    @Autowired
    private FileGatwayServiceImpl fileGatwayService;

    @Autowired
    private ActivityService activityService;

    static final String CONF_FILE = "webapp/office/pageoffice/demo-import-config.xlsx";
    static final String NETWORK_FILE = "webapp/office/pageoffice/network-template.xlsx";
    static final String INSURENET_FILE = "webapp/office/pageoffice/insurenet-template.xlsx";
    static final String WHITELIST = "webapp/office/pageoffice/whiteListOfNetworkInfo-template.xlsx";
    static final String INSUREMANAGER_FILE = "webapp/office/pageoffice/insuremanager-template.xlsx";
    static final String LARGECASHWHITE_FILE = "webapp/office/pageoffice/largeCashWhite-template.xlsx";
    static final String STAFFWHITE_FILE = "webapp/office/pageoffice/staffwhite-template.xlsx";
    static final String HOLIDAY = "webapp/office/pageoffice/HolidayConfiguration-template.xlsx";
    static final String ACTIVITYF_ILE = "webapp/office/pageoffice/activityList-template.xlsx";

    /**
     * ????????????????????????
     *
     * @param fileId
     * @param request
     * @param response
     */
    @GetMapping("/downloadFile/{fileId}")
    public void downloadFile(@PathVariable("fileId") String fileId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        InputStream inputStream = fileGatwayService.downloadFile(fileId);
        String fileName = cn.fisok.web.kit.HttpKit.iso8859("??????????????????.pdf", request);

        Map<String, String> headers = MapKit.mapOf("Content-Disposition", "attachment; filename=" + fileName);
        cn.fisok.web.kit.HttpKit.renderStream(response, inputStream, ContentType.PDF, headers);
    }

    /**
     * ??????????????????
     *
     * @param fileId
     * @param response
     */
    @GetMapping("/showWord2PDF/{fileId}")
    public void showWord2PDF(@PathVariable("fileId") String fileId,
                             HttpServletRequest request, HttpServletResponse response) {
        InputStream inputStream = null;
        try {
            //1.?????????????????????
            inputStream = fileGatwayService.downloadFile(fileId);
//            inputStream = new FileInputStream("/Users/shibin/Downloads/bshi.pdf");
            cn.fisok.web.kit.HttpKit.renderStream(response, inputStream, "application/pdf", null);
        } catch (Exception e) {
            logger.error("????????????????????????????????????????????????????????????,fileId=" + fileId, e);
        } finally {
            IOKit.close(inputStream);
        }
    }

    @GetMapping("/showPicture")
    public void showPicture(HttpServletRequest request, HttpServletResponse response) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("/Users/shibin/Downloads/?????????????????????.png");
            cn.fisok.web.kit.HttpKit.renderStream(response, inputStream, "octets/stream", null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            IOKit.close(inputStream);
        }
//        return inputStream;
    }

    /**
     * ??????????????????Excel??????
     *
     * @param templateName
     * @param request
     * @param response
     */
    @RequestMapping("/downloadActivityList")
    public void downloadActivityList(String templateName, HttpServletRequest request, HttpServletResponse response) {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(ACTIVITYF_ILE);
        String fileName = cn.fisok.web.kit.HttpKit.iso8859(templateName, request);

        Map<String, String> headers = MapKit.mapOf("Content-Disposition", "attachment; filename=" + fileName);
        HttpKit.renderStream(response, inputStream, ContentType.XLSX, headers);
    }

    /**
     * ????????????Excel??????
     *
     * @param templateName
     * @param request
     * @param response
     */
    @RequestMapping("/downloadNetWork")
    public void downloadTemplate(String templateName, HttpServletRequest request, HttpServletResponse response) {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(NETWORK_FILE);
        String fileName = cn.fisok.web.kit.HttpKit.iso8859(templateName, request);

        Map<String, String> headers = MapKit.mapOf("Content-Disposition", "attachment; filename=" + fileName);
        HttpKit.renderStream(response, inputStream, ContentType.XLSX, headers);
    }

    /**
     * ??????????????????????????????Excel??????
     *
     * @param templateName
     * @param request
     * @param response
     */
    @RequestMapping("/downloadInsureNet")
    public void downloadInsureNet(String templateName, HttpServletRequest request, HttpServletResponse response) {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(INSURENET_FILE);
        String fileName = cn.fisok.web.kit.HttpKit.iso8859(templateName, request);

        Map<String, String> headers = MapKit.mapOf("Content-Disposition", "attachment; filename=" + fileName);
        HttpKit.renderStream(response, inputStream, ContentType.XLSX, headers);
    }

    /**
     * ?????????????????????Excel??????
     *
     * @param templateName
     * @param request
     * @param response
     */
    @RequestMapping("/downloadInsureManager")
    public void downloadInsureManager(String templateName, HttpServletRequest request, HttpServletResponse response) {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(INSUREMANAGER_FILE);
        String fileName = cn.fisok.web.kit.HttpKit.iso8859(templateName, request);

        Map<String, String> headers = MapKit.mapOf("Content-Disposition", "attachment; filename=" + fileName);
        HttpKit.renderStream(response, inputStream, ContentType.XLSX, headers);
    }

    /**
     * ???????????????????????????????????????Excel??????
     *
     * @param templateName
     * @param request
     * @param response
     */
    @RequestMapping("/downloadLargeCashWhite")
    public void downloadLargeCashWhite(String templateName, HttpServletRequest request, HttpServletResponse response) {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(LARGECASHWHITE_FILE);
        String fileName = cn.fisok.web.kit.HttpKit.iso8859(templateName, request);

        Map<String, String> headers = MapKit.mapOf("Content-Disposition", "attachment; filename=" + fileName);
        HttpKit.renderStream(response, inputStream, ContentType.XLSX, headers);
    }

    /**
     * ?????????????????????????????????Excel??????
     *
     * @param templateName
     * @param request
     * @param response
     */
    @RequestMapping("/downloadStaffWhite")
    public void downloadStaffWhite(String templateName, HttpServletRequest request, HttpServletResponse response) {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(STAFFWHITE_FILE);
        String fileName = cn.fisok.web.kit.HttpKit.iso8859(templateName, request);

        Map<String, String> headers = MapKit.mapOf("Content-Disposition", "attachment; filename=" + fileName);
        HttpKit.renderStream(response, inputStream, ContentType.XLSX, headers);
    }

    /**
     * ???????????????????????????Excel??????
     *
     * @param templateName
     * @param request
     * @param response
     */
    @RequestMapping("/downloadHoliday")
    public void downloadHoliday(String templateName, HttpServletRequest request, HttpServletResponse response) {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(HOLIDAY);
        String fileName = cn.fisok.web.kit.HttpKit.iso8859(templateName, request);

        Map<String, String> headers = MapKit.mapOf("Content-Disposition", "attachment; filename=" + fileName);
        HttpKit.renderStream(response, inputStream, ContentType.XLSX, headers);
    }

    /**
     * ???Excel??????????????????
     *
     * @param request
     * @param key
     * @return
     */
    @PostMapping("/uploadParseDataListToDB/{key}")
    public Integer uploadParseDataListToDB(StandardMultipartHttpServletRequest request
            , @PathVariable("key") String key) {
        String itemName = "";
        if ("person".equals(key)) {
            itemName = "??????????????????";
        } else if ("network".equals(key)) {
            itemName = "??????????????????";
        } else if ("selfbank".equals(key)) {
            itemName = "??????????????????????????????";
        } else if ("insurenet".equals(key)) {
            itemName = "??????????????????????????????";
        } else if ("insureManager".equals(key)) {
            itemName = "?????????????????????";
        } else if ("StaffWhite".equals(key)) {
            itemName = "?????????????????????????????????";
        } else if ("largeCashWhite".equals(key)) {
            itemName = "???????????????????????????????????????";
        } else if ("downloadHoliday".equals(key)) {
            itemName = "???????????????????????????";
        } else if ("downloadWhiteList".equals(key)) {
            itemName = "???????????????????????????????????????";
        }else if ("downloadActivi".equals(key)) {
            itemName = "????????????";
        }
        InputStream dataInput = getUploadInputStream(request);
        InputStream confInput = this.getClass().getClassLoader().getResourceAsStream(CONF_FILE);


        try {
            ExcelImportConfigLoader loader = new ExcelImportConfigLoaderFromExcel();
            loader.load(confInput);

            DataGridParseToDBImpl parser = new DataGridParseToDBImpl(loader, itemName);
            parser.appendIntercept(new DataRowConvertIntercept(jdbcTemplate));
            parser.appendIntercept(getDataProcessIntercept());
            parser.setJdbcTemplate(jdbcTemplate);

            int r = parser.exec(dataInput, 0);

            return r;
        } catch (ImportExecutorException e) {
            throw new FisokException(e);
        } catch (ConfigException e) {
            throw new FisokException(e);
        } finally {
            IOKit.close(confInput);
            IOKit.close(dataInput);
        }

    }

    /**
     * ?????????????????????????????????????????????Excel??????
     *
     * @param templateName
     * @param request
     * @param response
     */
    @RequestMapping("/downloadWhiteList")
    public void downloadWhiteList(String templateName, HttpServletRequest request, HttpServletResponse response) {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(WHITELIST);
        String fileName = cn.fisok.web.kit.HttpKit.iso8859(templateName, request);

        Map<String, String> headers = MapKit.mapOf("Content-Disposition", "attachment; filename=" + fileName);
        HttpKit.renderStream(response, inputStream, ContentType.XLSX, headers);
    }

    private InputStream getUploadInputStream(StandardMultipartHttpServletRequest request) {
        InputStream dataInput = null;
        for (String fileMapKey : request.getFileMap().keySet()) {

            MultipartFile multipartFile = request.getFileMap().get(fileMapKey);
            try {
                dataInput = multipartFile.getInputStream();
            } catch (IOException e) {
                logger.error("", e);
                throw new BizException("??????????????????", e);
            }
        }
        return dataInput;
    }

    private DataProcessIntercept getDataProcessIntercept() {
        return new DataProcessIntercept() {

            public void beforeReadRow(ExcelRowData rowData, ExcelImportConfig config) throws InterceptException {

            }

            public void afterReadRow(ExcelRowData rowData, ExcelImportConfig config) throws InterceptException {

            }

            public void readComplete(List<ExcelRowData> data, ExcelImportConfig config) throws InterceptException {
                String id = config.getId();
                if ("???????????????????????????".equals(id)) {
                    holidayConfigurAtionService.setHolyId(data);
                } else if ("???????????????????????????????????????".equals(id)) {
                    whiteListOfNetworkservice.getWhiteListOfNetwork(data);
                }else if ("????????????".equals(id)) {
                    activityService.setHolyId(data);
                }

            }

            public void beforeWriteRow(ExcelRowData rowData, ExcelImportConfig config) throws InterceptException {

            }

            public void afterWriteRow(ExcelRowData rowData, ExcelImportConfig config) throws InterceptException {
                System.out.println("?????????????????????" + JSONKit.toJsonString(rowData.getRowData()));
            }

            public void writeComplete(List<ExcelRowData> data, ExcelImportConfig config) throws InterceptException {

            }
        };
    }

    @GetMapping("/getCustomer")
    public String getCustomer() {
        System.out.println("???????????????????????????");
        CustomerInfoPO customerPO = new CustomerInfoPO();
        customerPO.setCertName("test01");
        int save = beanCruder.save(customerPO);
        return save + "";
    }
}
