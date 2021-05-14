package com.vekai.office.excel.imports.executor;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.*;

import cn.fisok.raw.lang.MapObject;
import com.vekai.office.excel.reader.DataGridReader;
import com.vekai.office.excel.reader.ExcelRowData;
import com.vekai.office.excel.reader.ReaderException;
import com.vekai.office.excel.utils.ExcelUtils;
import com.vekai.office.excel.imports.ImportExecutorAbstract;
import com.vekai.office.excel.imports.config.ExcelImportConfigLoader;
import com.vekai.office.excel.imports.exception.ImportExecutorException;
import cn.fisok.raw.kit.IOKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.lang.ValueObject;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * DataGrid风格的数据解析转换
 * @author 杨松<syang@amarsoft.com>
 * @date 2017年3月21日
 */
public class DataGridParserImpl extends ImportExecutorAbstract<List<MapObject>> {

    private String configFile = null;
    private Properties properties =  new Properties();
    
    public DataGridParserImpl(ExcelImportConfigLoader configLoader, String itemName) {
        super(configLoader, itemName);
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    private List<Map<String, Object>> cleanEmptyRow(List<MapObject> rows){
    	List<Map<String, Object>> resultData = new ArrayList<Map<String, Object>>();
    	if(rows!=null&&rows.size()>0){
    		for(Map<String,Object> map:rows){
    			boolean emptyRow = true;
    			for(Map.Entry<String,Object> m:map.entrySet()){
    				Object v = m.getValue();
                    emptyRow = emptyRow && v == null;
                    if(v instanceof String){
                        String sv = (String)v;
                        emptyRow = emptyRow && StringKit.isBlank(sv);
                    }
    			}
    			if(!emptyRow){
    				resultData.add(map);
    			}
    		}
    	}
    	return resultData;
    }


    @Override
    public List<MapObject> exec(InputStream dataInputStream, int sheetIdx) throws ImportExecutorException {
        DataGridReader reader = new DataGridReader();
        reader.setImportExecutor(this);
        //初始化处理
        init();

        //读取数据文件
        Workbook workBook=null;
        List<MapObject> retList = null;
        try {
            workBook = ExcelUtils.openWorkbook(dataInputStream);
            Sheet sheet = workBook.getSheetAt(sheetIdx);
            List<ExcelRowData> rows = reader.readSheet(sheet,importConfig);
            retList = new ArrayList<>(rows.size());
//            //转成List数组
            for(int i=0;i<rows.size();i++){
                ExcelRowData row = rows.get(i);
                MapObject rowData = new MapObject();
                rowData.putAll(row.getRowData());
                List<String> nameList = row.getNameList();
                for(int j=0;j<nameList.size();j++){
                    String fieldName = nameList.get(j);
                    ValueObject fieldValue = row.getDataElement(fieldName);
                    rowData.put(fieldName, fieldValue.value());
                }
                retList.add(rowData);
            }
        } catch (IOException e) {
            throw new ImportExecutorException("读取数据文件出错",e);
        } catch (ReaderException e) {
            throw new ImportExecutorException(MessageFormat.format("读取数据文件的第[{0}]个sheet页数据出错",sheetIdx),e);
        } catch (Exception e) {
            throw new ImportExecutorException("未预料的异常",e);
        } finally{
            IOKit.close(workBook);
        }
        cleanEmptyRow(retList);
        return retList;
    }
}
