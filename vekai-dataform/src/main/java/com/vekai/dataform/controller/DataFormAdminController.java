package com.vekai.dataform.controller;

import com.vekai.dataform.dto.CloneDataFormBean;
import com.vekai.dataform.model.DataForm;
import com.vekai.dataform.model.DataFormElement;
import com.vekai.dataform.service.DataFormAdminService;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.kit.ValidateKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/devtool")
public class DataFormAdminController {

    @Autowired
    private DataFormAdminService dataFormAdminService;

    @GetMapping("/dataform")
    public List<DataForm> getDataForms(){
        return dataFormAdminService.getDataForms();
    }

    @GetMapping("/dataform/{id}")
    public DataForm getDataForm(@PathVariable("id")String id) {
        return dataFormAdminService.getDataForm(id);
    }

    @GetMapping("/dataform/{dataFormId}/{elementCode}")
    public DataFormElement getDataFormElementDetail(@PathVariable("dataFormId")String dataformId,
                                                    @PathVariable("elementCode")String code) {
        return dataFormAdminService.getDataFormElementDetail(dataformId,code);
    }

    @PostMapping(value = "/dataform/{dataFormId}/element")
    @ResponseBody
    public DataFormElement saveDataFormElement(@RequestBody DataFormElement dataFormElement,
                                               @PathVariable("dataFormId")String dataformId) {
        return dataFormAdminService.saveDataFormElement(dataFormElement,dataformId);
    }

    @PostMapping(value = {"/dataform/{id}","/dataform"})
    @ResponseBody
    public DataForm saveDataForm(@RequestBody DataForm dataForm,
                                 @PathVariable(required = false, name = "id")String id) {
        return dataFormAdminService.saveDataForm(dataForm, id);
    }

    @PostMapping(value = "/dataform/clone")
    @ResponseBody
    public DataForm cloneDataForm(@RequestBody CloneDataFormBean cloneDataFormBean) {
        return dataFormAdminService.cloneDataForm(cloneDataFormBean);
    }

    @DeleteMapping(value = "/dataform/{id}")
    public void deleteDataForm(@PathVariable("id")String id) {
         dataFormAdminService.deleteDataForm(id);
    }

    @GetMapping("/dataform/{id}/from-table-elements/{tables}")
    public List<DataFormElement> getDataFormElementFromTable(@PathVariable String id,@PathVariable("tables") String tables){
        ValidateKit.notBlank(tables,"根据数据表生成元素，tables参数不能为空");
        tables = StringKit.trim(tables);

        return dataFormAdminService.parseElementsFromTables(id,tables.split(","));
    }

    @PostMapping("/dataform/dbTransferToJsonFile")
    public String dbTransferToJsonFile(){
        return dataFormAdminService.dbTransferToJsonFile();
    }

    @PostMapping("/dataform/clearDataformCache")
    public Integer clearDataformCache(){
        dataFormAdminService.clearCacheAll();
        return 1;
    }

}
