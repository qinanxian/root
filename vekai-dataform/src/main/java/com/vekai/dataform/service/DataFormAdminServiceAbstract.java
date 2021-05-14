package com.vekai.dataform.service;

import com.vekai.dataform.DataFormConsts;
import com.vekai.dataform.exception.DataFormException;
import com.vekai.dataform.mapper.DataFormDBRowMapper;
import com.vekai.dataform.mapper.DataFormElementDBRowMapper;
import com.vekai.dataform.mapper.DataFormFilterDBRowMapper;
import com.vekai.dataform.model.DataForm;
import com.vekai.dataform.model.DataFormElement;
import com.vekai.dataform.model.DataFormFilter;
import com.vekai.dataform.model.DataFormStamp;
import com.vekai.dataform.model.types.ElementDataEditStyle;
import com.vekai.dataform.model.types.ElementDataFormat;
import com.vekai.dataform.model.types.ElementDataType;
import cn.fisok.raw.kit.FileKit;
import cn.fisok.raw.kit.IOKit;
import cn.fisok.raw.kit.JSONKit;
import cn.fisok.raw.kit.ListKit;
import cn.fisok.raw.kit.SQLKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.BeanQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Created by luyu on 2018/5/31.
 */
public abstract class DataFormAdminServiceAbstract implements DataFormAdminService {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    DataFormPublicService dataFormPublicService;
    @Autowired
    private BeanQuery dataQuery;
    @Autowired
    private DataFormDBRowMapper dataFormDBRowMapper;
    @Autowired
    private DataFormElementDBRowMapper dataFormElementDBRowMapper;
    @Autowired
    private DataFormFilterDBRowMapper dataFormFilterDBRowMapper;

    /**
     * 清空缓存
     */
    @CacheEvict(value= DataFormConsts.CACHE_KEY,allEntries=true,beforeInvocation=true)
    public void clearCacheAll(){

    }

    @Override
    /**
     * 把数据库中的显示模板提取到JSON文件中
     */
    public String dbTransferToJsonFile(){
        String directory = dataFormPublicService.getDataformDataDirectory().getAbsolutePath();


        List<DataForm> dataForms = this.getCompleteDataForms();
        dataForms.forEach(dataForm -> {
            String pack = dataForm.getPack();
            String code = dataForm.getCode();
//            File curDir = new File(directory+"/"+pack);
            File curDir = FileKit.getFile(directory,pack);
            if(!curDir.exists())curDir.mkdirs();
//            File jsonFile = new File(curDir.getAbsoluteFile()+"/"+code+".json");
            File jsonFile = FileKit.getFile(curDir,code+".json");
            if(!jsonFile.exists()){
                try {
                    FileKit.touchFile(jsonFile);
                } catch (IOException e) {
                    throw new DataFormException("创建json文件失败",e);
                }
            }

            String jsonText = JSONKit.toJsonString(dataForm,true);
            try {
//                System.out.println(jsonText);
                FileKit.write(jsonFile,jsonText, Charset.defaultCharset(),false);
            } catch (IOException e) {
                throw new DataFormException("写入json文件失败",e);
            }
        });
        return directory;
    }

    private List<DataForm> getCompleteDataForms() {
        List<DataForm> dataForms = dataQuery.exec(dataFormDBRowMapper,()->{
            return dataQuery.selectList(DataForm.class, "select * from FOWK_DATAFORM ORDER BY ID");
        });
        if (null == dataForms || dataForms.isEmpty()) return dataForms;


        List<DataFormElement> dataFormElements = dataQuery.exec(dataFormElementDBRowMapper,()->{
            return dataQuery.selectList(DataFormElement.class,
                    "SELECT * FROM FOWK_DATAFORM_ELEMENT ORDER BY DATAFORM_ID, SORT_CODE");
        });
        fillDataForm(dataForms, dataFormElements, (df, dfElement) -> df.addElement((DataFormElement) dfElement));


        List<DataFormFilter> dataFormFilters = dataQuery.exec(dataFormFilterDBRowMapper,()->{
            return dataQuery.selectList(DataFormFilter.class,
                    "SELECT * FROM FOWK_DATAFORM_FILTER ORDER BY DATAFORM_ID, SORT_CODE");
        });

        fillDataForm(dataForms, dataFormFilters, (df, dfFilter) -> df.addFilter((DataFormFilter) dfFilter));
        return dataForms;
    }

    private void fillDataForm(List<DataForm> dataForms, List<? extends DataFormStamp> properties,
                              BiConsumer<DataForm, ? super DataFormStamp> action) {
        DataForm currentDf;

        int lastJ = -1;
        for (int i = 0, j = lastJ + 1; i < dataForms.size(); ++i) {
            currentDf = dataForms.get(i);
            boolean dataformRefresh = true;
            for (; j < properties.size(); ++j) {
                String id = properties.get(j).getDataformId();
                if (null == id || id.isEmpty()) ;
                else if (id.equals(currentDf.getId())) {
                    lastJ = j;
                    action.accept(currentDf, properties.get(j));
                    dataformRefresh = false;
                } else {
                    if (!dataformRefresh)
                        break;
                }
            }
            if (j == properties.size() && j - lastJ > 1)
                j = lastJ + 1;
        }
    }


    @Override
    public List<DataFormElement> parseElementsFromTables(String dataFromId, String... tables) {
        List<DataFormElement> retList = ListKit.newArrayList();
        boolean singleTable = tables.length==1;
        for (String table : tables) {
            Connection connection = null;
            String tableName = table;
            String tableAlias = "";

            String tableExpr = StringKit.trim(table);
            String[] tableExprs = tableExpr.split("\\s+");
            if(tableExprs.length==2){
                tableName = tableExprs[0];
                tableAlias = tableExprs[1];
            }
            if(tableExprs.length==3){
                tableName = tableExprs[0];
                tableAlias = tableExprs[2];
            }

            try {
                connection = jdbcTemplate.getDataSource().getConnection();


                List<MapObject> dataList = SQLKit.getTableMeta(connection,tableName);

                //ORACLE使用SQL查询字段信息
                String dbName = connection.getMetaData().getDatabaseProductName();
                if("ORACLE".equalsIgnoreCase(dbName)){
                    String sql = "select  TABLE_NAME,COLUMN_NAME,COMMENTS from USER_COL_COMMENTS where TABLE_NAME = ?";
                    List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql,tableName);
                    Map<String, String> map = new HashMap<>();
                    mapList.forEach(row->{
                        map.put(row.get("COLUMN_NAME").toString(),row.get("COMMENTS").toString());
                    });

                    dataList.forEach(row -> {
                        String columnName = row.get("columnName",false).strValue();
                        String columnComment = row.get("comment",false).strValue();
                        String columnUpper = columnName.toUpperCase();
                        String comment = map.get(columnUpper);
                        if(StringKit.isNotBlank(comment)){
                            row.put("comment",comment);
                        }
                    });
                }
                fillElement(dataFromId,tableAlias,retList,dataList);

            } catch (SQLException e) {
                throw new DataFormException(e);
            } finally {
                IOKit.close(connection);
            }
        }

        //重新处理排序号
        DecimalFormat decimalFormat = new DecimalFormat("0000");
        if(retList.size()>100){
            decimalFormat = new DecimalFormat("00000");
        }
        for(int i=0;i<retList.size();i++){
            DataFormElement element = retList.get(i);
            int sortNumber = i * 10;
            String sortCode = decimalFormat.format(sortNumber);
//            System.out.println(sortCode);
            element.setSortCode(sortCode);
        }

        return retList;
    }

    private void fillElement(String dataFromId,String tableAlias,List<DataFormElement> retList,List<MapObject> dataList) throws SQLException {
        for(int i=0;i<dataList.size();i++){
            MapObject row = dataList.get(i);

            DataFormElement element = new DataFormElement();
            element.setTable(tableAlias);

            String columnName = row.getValue("columnName").strValue();
            String comment = row.getValue("comment").strValue();
            int dataType = row.getValue("dataType").intValue();

            element.setDataformId(dataFromId);
            element.setColumn(columnName.toUpperCase());
            element.setCode(StringKit.underlineToCamel(columnName));
            element.setName(comment);
            fillElementDataType(element,dataType);

            retList.add(element);
        }
    }

    /**
     * 填充数据类型，数据格式，UI样式
     *
     * @param element element
     * @param columnType columnType
     */
    private void fillElementDataType(DataFormElement element,int columnType){
        element.setDataType(ElementDataType.String);
        element.getElementUIHint().setDataFormat(ElementDataFormat.String);

        switch (columnType){
            case Types.VARCHAR:
            case Types.CHAR:
            case Types.NCHAR:
            case Types.LONGNVARCHAR:
            case Types.LONGVARBINARY:
            case Types.NVARCHAR:
            case Types.CLOB:
            case Types.NCLOB:
            case Types.NULL:
                break;
            case Types.BOOLEAN:
                element.setDataType(ElementDataType.Boolean);break;
            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
                element.setDataType(ElementDataType.Date);
                element.getElementUIHint().setEditStyle(ElementDataEditStyle.DatePicker);
                element.getElementUIHint().setDataFormat(ElementDataFormat.Date);
                break;
            case Types.INTEGER:
            case Types.TINYINT:
            case Types.SMALLINT:
            case Types.BIGINT:
                element.setDataType(ElementDataType.Integer);
                element.getElementUIHint().setEditStyle(ElementDataEditStyle.Integer);
                element.getElementUIHint().setDataFormat(ElementDataFormat.Integer);
                break;
            case Types.DOUBLE:
            case Types.DECIMAL:
            case Types.NUMERIC:
            case Types.FLOAT:
                element.setDataType(ElementDataType.Double);
                element.getElementUIHint().setEditStyle(ElementDataEditStyle.Double);
                element.getElementUIHint().setDataFormat(ElementDataFormat.Double);
                break;
        }
    }
}
