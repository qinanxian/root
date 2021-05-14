package com.vekai.office.excel.imports;

import com.vekai.office.excel.imports.config.ExcelImportConfig;
import com.vekai.office.excel.imports.config.ExcelImportConfigLoader;
import com.vekai.office.excel.imports.exception.ImportExecutorException;
import com.vekai.office.excel.imports.intercept.DataProcessIntercept;
import com.vekai.office.excel.imports.intercept.DataRowSpecialProcessIntercept;
import com.vekai.office.excel.imports.intercept.DataRowValidateIntercept;
import cn.fisok.raw.kit.ClassKit;
import cn.fisok.raw.kit.StringKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 把通用部分剥离出来
 * @param <T>
 */
public abstract class ImportExecutorAbstract<T> implements ImportExecutor<T> {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    protected ExcelImportConfig importConfig;

    public ImportExecutorAbstract(ExcelImportConfigLoader configLoader, String itemName) {
        this.importConfig = configLoader.getExcelImportConfig(itemName);
    }

    public void init() throws ImportExecutorException {
        initIntercept(importConfig);
    }

    public ImportExecutorAbstract appendIntercept(DataProcessIntercept intercept){
        importConfig.getIntercepts().add(intercept);
        return this;
    }

    /**
     * 初始化数据拦截处理器
     * @param config
     * @throws ImportExecutorException
     */
    protected void initIntercept(ExcelImportConfig config) throws ImportExecutorException {
        //校验类
        appendIntercept(new DataRowValidateIntercept());
        //值转换器
//        config.getIntercepts().add(new DataRowConvertIntercept(jdbcTemplate));
        //特殊处理
        appendIntercept(new DataRowSpecialProcessIntercept());

        //自定义拦截器
        String className = config.getIntercept();
        if(!StringKit.isEmpty(className)){
            Object inst = ClassKit.newInstance(className);
            appendIntercept((DataProcessIntercept)inst);
        }
    }
}
