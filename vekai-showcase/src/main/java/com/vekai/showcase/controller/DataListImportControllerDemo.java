package com.vekai.showcase.controller;


import cn.fisok.raw.lang.MapObject;
import cn.fisok.web.kit.HttpKit;
import com.vekai.office.excel.imports.config.ConfigException;
import com.vekai.office.excel.imports.config.ExcelImportConfig;
import com.vekai.office.excel.imports.config.ExcelImportConfigLoader;
import com.vekai.office.excel.imports.config.ExcelImportConfigLoaderFromExcel;
import com.vekai.office.excel.imports.exception.ImportExecutorException;
import com.vekai.office.excel.imports.executor.DataFreeFormParseImpl;
import com.vekai.office.excel.imports.executor.DataGridParseToDBImpl;
import com.vekai.office.excel.imports.executor.DataGridParserImpl;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/showcase/DataListImportControllerDemo")
public class DataListImportControllerDemo {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    static final String CONF_FILE = "com/vekai/showcase/office/demo-import-config.xlsx";

    @RequestMapping("/downloadTemplate")
    public void downloadTemplate(HttpServletRequest request,HttpServletResponse response){
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(CONF_FILE);
        String fileName = cn.fisok.web.kit.HttpKit.iso8859("配置-映射模板.xlsx",request);

        Map<String,String> headers = MapKit.mapOf("Content-Disposition","attachment; filename=" + fileName);
        cn.fisok.web.kit.HttpKit.renderStream(response,inputStream, ContentType.XLSX,headers);


    }

    @RequestMapping("/downloadTestDataList")
    public void downloadTestDataList(HttpServletRequest request,HttpServletResponse response){
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("com/vekai/showcase/office/demo-person-data-list.xlsx");
        String fileName = cn.fisok.web.kit.HttpKit.iso8859("导入-案例数据.xlsx",request);

        Map<String,String> headers = MapKit.mapOf("Content-Disposition","attachment; filename=" + fileName);
        cn.fisok.web.kit.HttpKit.renderStream(response,inputStream, ContentType.XLSX,headers);


    }

    private InputStream getUploadInputStream(StandardMultipartHttpServletRequest request){
        InputStream dataInput = null;
        for (String fileMapKey : request.getFileMap().keySet()) {

            MultipartFile multipartFile = request.getFileMap().get(fileMapKey);
            try {
                dataInput = multipartFile.getInputStream();
            } catch (IOException e) {
                logger.error("", e);
                throw new BizException("上传文件出错", e);
            }
        }
        return dataInput;
    }

    @PostMapping("/uploadParseDataList/{key}")
    public List<MapObject> uploadParseDataList(StandardMultipartHttpServletRequest request
            , @PathVariable("key") String key){

//        InputStream dataInput = this.getClass().getClassLoader().getResourceAsStream("com/vekai/showcase/office/demo-person-data-list.xlsx");
        InputStream dataInput = getUploadInputStream(request);
        InputStream confInput = this.getClass().getClassLoader().getResourceAsStream(CONF_FILE);

        try {
            ExcelImportConfigLoader loader = new ExcelImportConfigLoaderFromExcel();
            loader.load(confInput);

            DataGridParserImpl parser = new DataGridParserImpl(loader,"用户信息列表");
            parser.appendIntercept(new DataRowConvertIntercept(jdbcTemplate));

            List<MapObject> rows = parser.exec(dataInput,0);
            return rows;
        } catch (ImportExecutorException e) {
            throw new FisokException(e);
        } catch (ConfigException e) {
            throw new FisokException(e);
        } finally {
            IOKit.close(confInput);
            IOKit.close(dataInput);
        }

    }

    @RequestMapping("/downloadTestDataInfo")
    public void downloadTestDataInfo(HttpServletRequest request,HttpServletResponse response){
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("com/vekai/showcase/office/demo-person-data-info.xlsx");
        String fileName = cn.fisok.web.kit.HttpKit.iso8859("导入-案例数据-详情.xlsx",request);

        Map<String,String> headers = MapKit.mapOf("Content-Disposition","attachment; filename=" + fileName);
        HttpKit.renderStream(response,inputStream, ContentType.XLSX,headers);
    }

    @PostMapping("/uploadParseDataListToDB/{key}")
    public Integer uploadParseDataListToDB(StandardMultipartHttpServletRequest request
            , @PathVariable("key") String key){

//        InputStream dataInput = this.getClass().getClassLoader().getResourceAsStream("com/vekai/showcase/office/demo-person-data-list.xlsx");
        InputStream dataInput = getUploadInputStream(request);
        InputStream confInput = this.getClass().getClassLoader().getResourceAsStream(CONF_FILE);


        try {
            ExcelImportConfigLoader loader = new ExcelImportConfigLoaderFromExcel();
            loader.load(confInput);

            DataGridParseToDBImpl parser = new DataGridParseToDBImpl(loader,"用户信息列表-数据表");
            parser.appendIntercept(new DataRowConvertIntercept(jdbcTemplate));
            parser.appendIntercept(getDataProcessIntercept());
            parser.setJdbcTemplate(jdbcTemplate);

            int r = parser.exec(dataInput,0);

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


    @PostMapping("/uploadParseDataInfo/{key}")
    public Map<String,Object> uploadParseDataInfo(StandardMultipartHttpServletRequest request
            , @PathVariable("key") String key){

        InputStream confInput = this.getClass().getClassLoader().getResourceAsStream(CONF_FILE);
        InputStream dataInput = getUploadInputStream(request);


        try {
            ExcelImportConfigLoader loader = new ExcelImportConfigLoaderFromExcel();
            loader.load(confInput);

            DataFreeFormParseImpl parser = new DataFreeFormParseImpl(loader,"用户详细信息");
            parser.appendIntercept(new DataRowConvertIntercept(jdbcTemplate));

            Map<String,Object> row = parser.exec(dataInput,0);

            return row;

        } catch (ImportExecutorException e) {
            throw new FisokException(e);
        } catch (ConfigException e) {
            throw new FisokException(e);
        }finally{
            IOKit.close(confInput);
            IOKit.close(dataInput);
        }


    }

    private DataProcessIntercept getDataProcessIntercept(){
        return new DataProcessIntercept(){

            public void beforeReadRow(ExcelRowData rowData, ExcelImportConfig config) throws InterceptException {

            }

            public void afterReadRow(ExcelRowData rowData, ExcelImportConfig config) throws InterceptException {

            }

            public void readComplete(List<ExcelRowData> data, ExcelImportConfig config) throws InterceptException {

            }

            public void beforeWriteRow(ExcelRowData rowData, ExcelImportConfig config) throws InterceptException {

            }

            public void afterWriteRow(ExcelRowData rowData, ExcelImportConfig config) throws InterceptException {
                System.out.println("写入记录成功："+ JSONKit.toJsonString(rowData.getRowData()));
            }

            public void writeComplete(List<ExcelRowData> data, ExcelImportConfig config) throws InterceptException {

            }
        };
    }

}
