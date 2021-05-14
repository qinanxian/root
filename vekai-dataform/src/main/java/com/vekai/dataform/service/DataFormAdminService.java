package com.vekai.dataform.service;

import com.vekai.dataform.dto.CloneDataFormBean;
import com.vekai.dataform.model.DataForm;
import com.vekai.dataform.model.DataFormElement;

import java.util.List;


public interface DataFormAdminService {

    List<DataForm> getDataForms();

    DataForm getDataForm(String id);

    DataForm cloneDataForm(CloneDataFormBean cloneDataFormBean);

    DataFormElement getDataFormElementDetail(String dataformId, String code);

    DataForm saveDataForm(DataForm dataForm, String oldDataFormId);

    DataFormElement saveDataFormElement(DataFormElement element, String dataformId);

    void deleteDataForm(String dataFormId);

    List<DataFormElement> parseElementsFromTables(String dataFromId,String... tables);

    void clearCacheAll();

    String dbTransferToJsonFile();

}
