package com.vekai.dataform.service;

import cn.fisok.raw.kit.*;
import com.vekai.dataform.autoconfigure.DataFormProperties;
import com.vekai.dataform.exception.DataFormException;
import com.vekai.dataform.mapper.DataFormMapper;
import com.vekai.dataform.model.DataForm;
import com.vekai.dataform.model.DataFormElement;
import com.vekai.dataform.model.types.ElementDataEditStyle;
import com.vekai.dataform.model.types.FormDataModelType;
import cn.fisok.raw.lang.PairBond;
import cn.fisok.sqloy.converter.impl.UnderlinedNameConverter;
import cn.fisok.sqloy.core.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * DataForm功能实现
 * Created by tisir yangsong158@qq.com on 2017-05-30
 */
@Service
public class DataFormService {
    @Autowired
    private DataFormMapper dataFormMapper;
    @Autowired
    private DataFormProperties dataFormProperties;

    public DataFormMapper getDataFormMapper() {
        return dataFormMapper;
    }

    public void setDataFormMapper(DataFormMapper dataFormMapper) {
        this.dataFormMapper = dataFormMapper;
    }

    public void clearCacheAll() {
        dataFormMapper.clearCacheAll();
    }

    public void clearCacheItem(String formId) {
        dataFormMapper.clearCacheItem(formId);
    }

    private boolean fieldExists(String field,DataForm dataForm){
        List<DataFormElement> elements = dataForm.getElements();
        for(DataFormElement element : elements){
            if(field.equalsIgnoreCase(element.getCode())||field.equalsIgnoreCase(element.getColumn())){
                return true;
            }
        }
        return false;
    }

    private void touchMustElement(DataForm dataForm,String field){
        //添加元素
        DataFormElement newElement = new DataFormElement();
        DataFormElement.FormElementUIHint newUIHint = newElement.getElementUIHint();
        newElement
                .setDataformId(dataForm.getId())
                .setName(field)
                .setCode(field)
                .setColumn(StringKit.camelToUnderline(field))
                .setGroup("")
                .setGroupI18nCode("")
                .setSubGroup("")
                .setSubGroupI18nCode("")
                .setSortCode("ZZ")
                .setTable(null)
                .setUpdateable(false)
                .setPersist(false)
        ;
        newUIHint
                .setEditStyle(ElementDataEditStyle.Label)
                .setReadonly(true)
                .setReading(true)
                .setVisible(false)
                .setHtmlStyle("")
        ;

        newElement.setElementUIHint(newUIHint);
        dataForm.getElements().add(newElement);

        //补一下SQLQuery对象
        SqlQuery query = dataForm.getQuery();
        List<PairBond> items = query.getSelectItems();
        if(items == null || items.isEmpty())return;
        PairBond pairBond = new PairBond();
        pairBond.setLeft(field);
        pairBond.setRight(StringKit.camelToUnderline(field));
        items.add(pairBond);


    }
    /**
     * 自动补充必需要填充的字段
     *
     * @param dataForm dataForm
     */
    public void touchMustTouchFields(DataForm dataForm){
        String dataFormId = dataForm.getId();
        Map<String, String> mustTouchFields = dataFormProperties.getMustTouchFields();
        if(mustTouchFields == null || mustTouchFields.isEmpty())return;
        Iterator<Map.Entry<String,String>> iterator = mustTouchFields.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String,String> entry = iterator.next();
            Pattern pattern = Pattern.compile(entry.getKey());
            //如果显示模板匹配上了，则主动添加字段
            if(pattern.matcher(dataFormId).find()){
                String strFields = entry.getValue();
                if(StringKit.isBlank(strFields))continue;
                String[] fields = strFields.split(",");
                for(String field : fields){
                    field = StringKit.trim(field);
                    if(StringKit.isBlank(field))continue;
                    boolean exists = fieldExists(field,dataForm);
                    if(!exists){
                        touchMustElement(dataForm,field);
                    }
                }
            }
        }

    }
    /**
     * 仅取显示模板目录列表
     *
     * @return list
     */
    public List<DataForm> getBriefDataFormList(){
        List<DataForm> dataForms = dataFormMapper.getAllDataForms();
        return dataForms.stream().filter(dataForm -> StringKit.isNotBlank(dataForm.getId())).collect(Collectors.toList());
    }

    public DataForm getDataForm(String dataform){
        int lastDotIdx = dataform.lastIndexOf("-"); //URL路径中,多个点号传输会出问题
        DataForm dataForm = null;
        if(lastDotIdx>0){
            String pack = dataform.substring(0,lastDotIdx);
            String formId = dataform.substring(lastDotIdx+1);
            dataForm = dataFormMapper.getDataForm(pack,formId);
        }else{
            dataForm = dataFormMapper.getDataForm("",dataform);
        }
        ValidateKit.notNull(dataForm, "DataForm不存在,form=" + dataform);

        fillSelectedItems(dataForm);

        return dataForm;
    }

    /**
     * 根据显示模板列出的字段，填写select语句
     *
     * @param dataForm dataform
     */
    public void fillSelectedItems(DataForm dataForm){
        SqlQuery query = dataForm.getQuery();
        List<PairBond> items = query.getSelectItems();
        if(items==null) items = new ArrayList<PairBond>();
        query.setSelectItems(items);
        if(items.size()>0)return ;
        List<DataFormElement> elements = dataForm.getElements();
        UnderlinedNameConverter converter = new UnderlinedNameConverter();

        Set<String> scannedCols = new HashSet<String>();
        for(DataFormElement element : elements){
            String column = element.getColumn();
            String columnFixed = column ;
            String alias = element.getCode();
            if(StringKit.isBlank(column)&&StringKit.isBlank(alias))continue;

            //如果是虚字段，并且字段别名不为空的情况下，把虚字段AS一下
            if(SQLKit.isConstColumn(column)&&StringKit.isNotBlank(alias)){
                column = String.join(" AS ",column,StringKit.camelToUnderline(alias));
            //如果字段名重复，也要作AS,否则，在select * (Name,Name 。。。）时，会出SQL语法错误
            }else{
                if(StringKit.isBlank(column)){
                    column = converter.getColumnName(alias);
                }
                if (!StringKit.isBlank(element.getTable()))  {
                    column = String.join(".",element.getTable(),column);
                }
                //名称重复字段的处理
                if(scannedCols.contains(columnFixed) && StringKit.isNotBlank(alias)){
                    column = String.join(" AS ",column,StringKit.camelToUnderline(alias));
                }

                if(StringKit.isBlank(alias)){
                    alias = converter.getPropertyName(column);
                }
                //如果字段是另一个select语句，则处理一下
                if(SQLKit.isSubSelect(column)){
                    String lowerColumn = column.toLowerCase();
                    int asIndex = lowerColumn.lastIndexOf(") AS ");
                    if(asIndex>0){
                        throw new DataFormException("字段{0}是一个子查询，请不要使用[（select * from table_name) AS column],直接使用[select count(1) from table_name]即可",alias);
                    }
                    column = StringKit.format("({0}) AS {1}",column,StringKit.camelToUnderline(alias));
                }
            }
            items.add(new PairBond<String,String>(alias,column));
            scannedCols.add(columnFixed);
        }

        /**
         * 如果是JavaBean，则补一下其他在显示模板上不存在的字段，保持JavaBean的完整性
         * !!!后来取消了，自动补充字段，如果两表关联查询时，极易导致添加的字段，不明确在哪个数据表上
         * !!!保存时字段不全的问题，通过调整保存逻辑来完成
         */
//        if(dataForm.getDataModelType() == FormDataModelType.JavaBean){
//            fillSelectItemsWithBeanProperties(items,dataForm);
//        }
    }

    /**
     * 如果是JavaBean，则把JavaBean上没有的字段也要查出来
     * @param items
     * @param dataForm
     */
    protected void fillSelectItemsWithBeanProperties(List<PairBond> items,DataForm dataForm){
        Set<String> existsFields = new HashSet<>();
        items.stream().forEach(item -> {
            existsFields.add((String)item.getLeft());
        });
        if(dataForm.getDataModelType() == FormDataModelType.JavaBean){
            Class<?> clazz = ClassKit.forName(dataForm.getDataModel());
            Map<String,String> column2fieldMap = JpaKit.getMappedFields(clazz);
            Iterator<String> columnIterator = column2fieldMap.keySet().iterator();
            while (columnIterator.hasNext()){
                String column = columnIterator.next();
                String alias = StringKit.nvl(column2fieldMap.get(column),StringKit.underlineToCamel(column));
                if(existsFields.contains(alias))continue;
                items.add(new PairBond<String,String>(alias,column));

                //为了满足Mapper的使用，这里把Bean中没有的字段给补到显示模板上去，不显示
                //DataFormUtils.addBeanPropertyToDataForm(clazz,alias,column,dataForm);
            }

        }

    }

}
