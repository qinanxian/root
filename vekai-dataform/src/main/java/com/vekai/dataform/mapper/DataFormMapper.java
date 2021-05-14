package com.vekai.dataform.mapper;

import com.vekai.dataform.model.DataForm;
import com.vekai.dataform.model.DataFormElement;

import java.util.List;

/**
 * DatForm对象操作的一些基础服务
 * Created by tisir yangsong158@qq.com on 2017-05-22
 */
public interface DataFormMapper {

    void clearCacheAll();

    void clearCacheItem(String formId);

    boolean exists(String id);

    DataForm getDataForm(String id);

    DataForm getDataForm(String pack,String code);

    List<DataForm> getDataFormsByPack(String pack);

    List<DataForm> getAllDataForms();

    int save(DataForm dataForm);

    int saveDataFormElement(DataFormElement element);

    int delete(String id);

    int delete(String pack,String code);

    int deleteAll();
}
