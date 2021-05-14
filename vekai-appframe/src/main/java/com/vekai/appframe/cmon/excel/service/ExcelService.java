//package com.vekai.appframe.cmon.excel.service;
//
//import com.vekai.office.excel.imports.config.ConfigException;
//import com.vekai.office.excel.imports.config.ExcelImportConfigLoader;
//import com.vekai.office.excel.imports.config.ExcelImportConfigLoaderFromExcel;
//import com.vekai.office.excel.imports.executor.DataGridParserImpl;
//import com.vekai.office.excel.imports.intercept.DataRowConvertIntercept;
//import com.vekai.runtime.lang.BizException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
//
//import java.io.IOException;
//import java.io.InputStream;
//
///**
// * Created by luyu on 2018/5/7.
// */
//@Service
//public class ExcelService {
//
//    public static final String CONF_FILE = "com/amarsoft/amix/office/ipo-import-config.xlsx";
//
//    @Autowired
//    JdbcTemplate jdbcTemplate;
//
//    public InputStream getUploadInputStream(StandardMultipartHttpServletRequest request){
//        InputStream dataInput = null;
//        for (String fileMapKey : request.getFileMap().keySet()) {
//
//            MultipartFile multipartFile = request.getFileMap().get(fileMapKey);
//            try {
//                dataInput = multipartFile.getInputStream();
//            } catch (IOException e) {
//                throw new BizException("上传文件出错", e);
//            }
//        }
//        return dataInput;
//    }
//
//    public DataGridParserImpl buildParser(InputStream confInput, String itemName) throws ConfigException {
//        ExcelImportConfigLoader loader = new ExcelImportConfigLoaderFromExcel();
//        loader.load(confInput);
//
//        DataGridParserImpl parser = new DataGridParserImpl(loader,itemName);
//        parser.appendIntercept(new DataRowConvertIntercept(jdbcTemplate));
//        return parser;
//    }
//}
