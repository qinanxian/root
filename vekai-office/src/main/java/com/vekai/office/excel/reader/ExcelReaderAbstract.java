package com.vekai.office.excel.reader;

import com.vekai.office.excel.imports.config.ExcelImportConfig;
import com.vekai.office.excel.imports.intercept.InterceptException;
import com.vekai.office.excel.imports.intercept.InterceptHelper;
import com.vekai.office.excel.imports.intercept.ExcelValidateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

abstract class ExcelReaderAbstract<R> implements ExcelReader<R> {

    protected Logger logger = LoggerFactory.getLogger(getClass());
//    protected ImportExecutor<?> runnerContext;

    /**
     * 取除Excel地址聚值方式外，其它表达式的值
     *
     * @param expr
     * @param rowData
     * @param config
     * @return
     * @throws Exception
     */
    public String getExprValue(String expr, ExcelRowData rowData, ExcelImportConfig config) throws Exception {
        String value = null;
        if (expr.startsWith("固定值:")) {    //取固定值
            value = expr.replaceFirst("固定值:", "");
        } else if (expr.startsWith("顺序号")) {    //取当前顺序号
            value = "" + rowData.getRowNo();
        } else if (expr.startsWith("属性值:")) {    //取扩展属性值
            String name = expr.replaceFirst("属性值:", "");
            value = (String) config.getProperty(name);
        }
        return value;
    }

    /**
     * 调用拦截器方法，返回是否忽略当前行
     *
     * @param rowData
     * @param config
     * @return
     * @throws ReaderException
     */
    protected boolean afterReadRow(ExcelRowData rowData, ExcelImportConfig config) throws ReaderException {
        if (config.getIntercepts().size() == 0) return false;
        //拦截器beforeReadRow方法
        try {
            InterceptHelper.afterReadRow(config.getIntercepts(), rowData, config);
        } catch (InterceptException e) {
            if (config.getExceptionProcess() == ExcelImportConfig.ExceptionProcess.Ignore
                    && e instanceof ExcelValidateException) {
                logger.debug(MessageFormat.format("数据校验未通过，忽略。校验信息:{0},数据:{1}", e.getMessage(), rowData));
                return true;
            } else {
                throw new ReaderException(e);
            }
        }
        return false;
    }

}
