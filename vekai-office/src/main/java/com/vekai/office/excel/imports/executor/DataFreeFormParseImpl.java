package com.vekai.office.excel.imports.executor;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.vekai.office.excel.reader.ExcelRowData;
import com.vekai.office.excel.reader.FreeFormFormReader;
import com.vekai.office.excel.reader.ReaderException;
import com.vekai.office.excel.utils.ExcelUtils;
import com.vekai.office.excel.imports.ImportExecutorAbstract;
import com.vekai.office.excel.imports.config.ExcelImportConfigLoader;
import com.vekai.office.excel.imports.exception.ImportExecutorException;
import cn.fisok.raw.lang.ValueObject;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class DataFreeFormParseImpl extends ImportExecutorAbstract<Map<String,Object>> {

    public DataFreeFormParseImpl(ExcelImportConfigLoader configLoader, String itemName) {
        super(configLoader, itemName);
    }



    @Override
    public Map<String, Object> exec(InputStream dataInputStream, int sheetIdx) throws ImportExecutorException {
        Map<String,Object> rowData = new LinkedHashMap<String,Object>();
        init();

        FreeFormFormReader reader = new FreeFormFormReader();
        Workbook workBook=null;
        try {
            workBook = ExcelUtils.openWorkbook(dataInputStream);
            Sheet sheet = workBook.getSheetAt(sheetIdx);
            ExcelRowData row = reader.readSheet(sheet,importConfig);
            List<String> nameList = row.getNameList();
            for(int j=0;j<nameList.size();j++){
                String fieldName = nameList.get(j);
                ValueObject fieldValue = row.getDataElement(fieldName);
                rowData.put(fieldName, fieldValue.value());
            }
        } catch (IOException e) {
            throw new ImportExecutorException("读取数据文件出错",e);
        } catch (ReaderException e) {
            throw new ImportExecutorException(MessageFormat.format("读取数据文件的第[{0}]个sheet页数据出错",sheetIdx),e);
        }
        return rowData;
    }
}
