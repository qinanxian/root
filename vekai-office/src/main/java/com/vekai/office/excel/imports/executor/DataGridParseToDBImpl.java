package com.vekai.office.excel.imports.executor;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.*;

import com.vekai.office.excel.imports.config.ExcelImportConfigLoader;
import com.vekai.office.excel.imports.exception.ImportExecutorException;
import com.vekai.office.excel.reader.DataGridReader;
import com.vekai.office.excel.reader.ExcelRowData;
import com.vekai.office.excel.reader.ReaderException;
import com.vekai.office.excel.writer.DataWriter;
import com.vekai.office.excel.writer.DataWriterToTable;
import com.vekai.office.excel.writer.WriterException;
import com.vekai.office.excel.imports.ImportExecutorAbstract;
import com.vekai.office.excel.imports.config.ExcelImportConfig;
import cn.fisok.raw.kit.IOKit;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.jdbc.core.JdbcTemplate;


/**
 * 列表解析并保存到数据库中
 */
public class DataGridParseToDBImpl extends ImportExecutorAbstract<Integer> {
    private ExcelImportConfigLoader loader = null;
    private Properties properties = new Properties();
    private JdbcTemplate jdbcTemplate = null;

    public DataGridParseToDBImpl(ExcelImportConfigLoader configLoader, String itemName) {
        super(configLoader, itemName);
    }

    public void addProperties(String name, String value) {
        properties.setProperty(name, value);
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public ExcelImportConfigLoader getLoader() {
        return loader;
    }

    public void setLoader(ExcelImportConfigLoader loader) {
        this.loader = loader;
    }


    protected DataWriter.WriteResult writeDataToDB(List<ExcelRowData> data, ExcelImportConfig config) throws ReaderException, WriterException {
        DataWriterToTable writer = new DataWriterToTable();
        writer.setJdbcTemplate(jdbcTemplate);
        writer.write(data, config);
        return writer.getResult();
    }

    /**
     * 初始化自定义参数
     */
    @SuppressWarnings("unchecked")
    private void reloadProperties(ExcelImportConfig config) {
        Iterator<Object> iterator = properties.keySet().iterator();
        while(iterator.hasNext()){
            String name = (String)iterator.next();
            String value = properties.getProperty(name);
            config.setProperty(name, value);
        }

    }

    public Integer exec(InputStream dataInputStream, int sheetIdx) throws ImportExecutorException {
        int ret = 0;
        DataGridReader reader = new DataGridReader();
        reader.setImportExecutor(this);

        Map<String, Object> result = new HashMap<String, Object>();
//		//把业务基础要素属性中注入

        reloadProperties(importConfig); //加载自定义参数
        initIntercept(importConfig);

        //读取数据文件
        Workbook workBook = null;
        InputStream newIs = null;
        try {
            newIs = IOKit.convertToByteArrayInputStream(dataInputStream);
            try {
                workBook = new XSSFWorkbook(OPCPackage.open(newIs));
            } catch (OfficeXmlFileException e) {
                newIs.reset();
                workBook = new HSSFWorkbook(newIs);
            }

            Sheet sheet = workBook.getSheetAt(sheetIdx);
            //读出
            List<ExcelRowData> rows = reader.readSheet(sheet, importConfig);
            //写入
            DataWriter.WriteResult writeResult = writeDataToDB(rows, importConfig);
            ret = writeResult.getWriteRows();
        } catch (IOException e) {
            throw new ImportExecutorException("读取数据文件出错", e);
        } catch (ReaderException e) {
            throw new ImportExecutorException(MessageFormat.format("读取数据文件的第[{0}]个sheet页数据出错", sheetIdx), e);
        } catch (WriterException e) {
            throw new ImportExecutorException("写入数据出错", e);
        } catch (Exception e) {
            throw new ImportExecutorException("未预料的异常", e);
        } finally {
            IOKit.close(newIs);
            IOKit.close(workBook);
        }

        return ret;
    }
}
