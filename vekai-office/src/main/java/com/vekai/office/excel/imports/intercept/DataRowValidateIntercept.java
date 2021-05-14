package com.vekai.office.excel.imports.intercept;

import com.vekai.office.excel.reader.ExcelRowData;
import com.vekai.office.excel.imports.config.ExcelImportConfig;
import com.vekai.office.excel.imports.config.ExcelImportConfig.ColumnItem;
import com.vekai.office.excel.imports.config.ExcelImportConfig.ExceptionProcess;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.lang.ValueObject;
import cn.fisok.raw.lang.ValueObject.ValueType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 数据校验拦截器。校验数据是否必需，格式是否正确
 *
 * @author yangsong
 * @since 2014/04/03
 */
public class DataRowValidateIntercept implements DataProcessIntercept {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public void beforeReadRow(ExcelRowData rowData, ExcelImportConfig config)
            throws InterceptException {

    }

    /**
     * 在读取到一行数据后，就进行数据校验
     */
    public void afterReadRow(ExcelRowData rowData, ExcelImportConfig config)
            throws InterceptException {
        List<String> nameList = rowData.getNameList();
        for (int i = 0; i < nameList.size(); i++) {
            String filedName = nameList.get(i);
            ValueObject element = rowData.getDataElement(filedName);
            ColumnItem configItem = config.getColumnItem(filedName);
            ExcelImportConfig.ColumnItem columnItem = config.getColumnItem(filedName);
            if (columnItem == null) {
                throw new InterceptException(MessageFormat.format("配置数据异常，找不到字段[{0}]对应的配置项", filedName));
            }
            //格式校验项校验
            validateRequired(rowData, element, config, configItem);
            validateFormat(rowData, element, config, configItem);

        }
    }

    /**
     * 必需项校验
     *
     * @param rowData    行数据对象
     * @param element    当前数据项
     * @param config     整体配置对象
     * @param configItem 当前项配置对象
     * @throws InterceptException
     */
    private void validateRequired(ExcelRowData rowData, ValueObject element, ExcelImportConfig config, ColumnItem configItem) throws InterceptException {
        if (configItem.isRequired()) {
            if (element.isNull() || element.isBlank()) {
                String msg = MessageFormat.format("[{0}]数据不允许为空,数据地址[{1}]", configItem.getComment(), element.getProperty("addr"));
                handValidateError(msg, config);
            }
        }
    }

    /**
     * 格式校验
     *
     * @param rowData
     * @param element
     * @param config
     * @param configItem
     * @throws InterceptException
     */
    private static DecimalFormat doubleFormat = new DecimalFormat("##############################.##");
    private static DecimalFormat intFormat = new DecimalFormat("##############################");

    private void validateFormat(ExcelRowData rowData, ValueObject element, ExcelImportConfig config, ColumnItem configItem) throws InterceptException {
        if(element==null)return ;
        if (StringKit.isEmpty(configItem.getFormatValidate())) return;
        if (element.isBlank()) return;

        String stringValue = null;


        ValueType valueType = element.getValueType();
        if (valueType == ValueType.Double) {
            stringValue = doubleFormat.format(element.doubleValue());
        } else if (valueType == ValueType.Integer) {
            stringValue = intFormat.format(element.intValue());
        } else if (valueType == ValueType.Long) {
            stringValue = intFormat.format(element.longValue());
        } else {
            stringValue = element.strValue();
        }

        Pattern pattern = Pattern.compile(configItem.getFormatValidate());
        Matcher matcher = pattern.matcher(stringValue);
        if (!matcher.find()) {
            String msg = MessageFormat.format("[{0}]数据格式错误", element.getProperty("addr"));
            handValidateError(msg, config);
        }
    }

    /**
     * 根据配置项对非法数据的处理级别，决定是否抛出错误
     *
     * @param msgText
     * @param config
     * @throws InterceptException
     */
    private void handValidateError(String msgText, ExcelImportConfig config) throws InterceptException {
        if (config.getExceptionProcess() == ExceptionProcess.Warn) {    //警告则直接提示
            logger.warn(msgText);
        } else if (config.getExceptionProcess() == ExceptionProcess.Ignore) {//忽略，则抛出校验异常，读取程序自动跳过
            throw new ExcelValidateException(msgText);
        } else if (config.getExceptionProcess() == ExceptionProcess.Break) {//中断，则抛出拦截器异常，读取程序中断
            throw new InterceptException(msgText);
        }
    }


    public void readComplete(List<ExcelRowData> data, ExcelImportConfig config)
            throws InterceptException {

    }

    public void beforeWriteRow(ExcelRowData rowData, ExcelImportConfig config)
            throws InterceptException {

    }

    public void afterWriteRow(ExcelRowData rowData, ExcelImportConfig config)
            throws InterceptException {

    }


    public void writeComplete(List<ExcelRowData> data, ExcelImportConfig config)
            throws InterceptException {

    }

}
