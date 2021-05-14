package com.vekai.dataform.handler;

import com.vekai.dataform.validator.ValidateResult;
import com.vekai.dataform.model.DataForm;

import java.util.Map;

/**
 * 单一记录的查询,保存处理
 * Created by tisir yangsong158@qq.com on 2017-05-29
 */
public interface DataOneHandler<T> extends DataObjectHandler {

    /**
     * 根据DataForm创建数据对象
     *
     * @param dataForm dataForm
     * @return T
     */
    T createDataObject(DataForm dataForm);

    /**
     * 查询单个数据对象
     *
     * @param dataForm dataForm
     * @param queryParameters queryParameters
     * @return T
     */
    T query(DataForm dataForm, Map<String, ?> queryParameters);

    /**
     * 插入
     *
     * @param dataForm dataForm
     * @param object object
     * @return int
     */
    int insert(DataForm dataForm, T object);

    /**
     * 更新
     *
     * @param dataForm dataForm
     * @param object object
     * @return int
     */
    int update(DataForm dataForm, T object);

    /**
     * 保存列表数据
     *
     * @param dataForm dataForm
     * @param object object
     * @return int
     */
    int save(DataForm dataForm, T object);

    /**
     * 删除列表数据
     *
     * @param dataForm dataForm
     * @param object object
     * @return int
     */
    int delete(DataForm dataForm,T object);

    /**
     * 校验数据
     *
     * @param dataForm dataForm
     * @param object object
     * @return ValidateResult
     */
    ValidateResult validate(DataForm dataForm, T object);
}
