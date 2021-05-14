package com.vekai.dataform.handler;

import com.vekai.dataform.validator.ValidateResult;
import com.vekai.dataform.model.DataForm;
import cn.fisok.sqloy.core.PaginResult;

import java.util.List;
import java.util.Map;

/**
 * 列表数据的查询,保存处理
 * Created by tisir yangsong158@qq.com on 2017-05-29
 */
public interface DataListHandler<T> extends DataObjectHandler {


    
    /**
     * 查询列表数据
     *
     * @param dataForm dataForm
     * @param queryParameters 查询条件参数
     * @param filterParameters 筛选条件参数
     * @param sortMap 排序参数
     * @param pageSize 分页大小
     * @param pageIndex 分页索引
     * @return PaginationData
     */
    PaginResult<T> query(DataForm dataForm, Map<String, ?> queryParameters, Map<String, ?> filterParameters, Map<String, ?> sortMap, int pageSize, int pageIndex);

    /**
     * 插入
     *
     * @param dataForm dataForm
     * @param dataList dataList
     * @return int
     */
    public int insert(DataForm dataForm, List<T> dataList);

    /**
     * 更新
     *
     * @param dataForm dataForm
     * @param dataList dataList
     * @return int
     */
    public int update(DataForm dataForm, List<T> dataList);

    /**
     * 保存列表数据
     *
     * @param dataForm dataForm
     * @param dataList dataList
     * @return int
     */
    int save(DataForm dataForm,List<T> dataList);

    /**
     * 删除列表数据
     *
     * @param dataForm dataForm
     * @param dataList dataList
     * @return int
     */
    Integer delete(DataForm dataForm,List<T> dataList);

    /**
     * 校验数据
     *
     * @param dataForm dataForm
     * @param dataList dataList
     * @return list
     */
    List<ValidateResult> validate(DataForm dataForm, List<T> dataList);

}
