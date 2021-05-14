package com.vekai.base.dict.controller;

import com.vekai.base.dict.service.DictAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequestMapping("/devtool/dict")
public class DictAdminController {

    @Autowired
    DictAdminService dictAdminService;

    @PostMapping("/dbTransferFile")
    public String dbTransferFile(){
        File path = dictAdminService.getDictDataDirectory();
        dictAdminService.dbTransferFile(path);
        return path.getAbsolutePath();
    }

    @PostMapping("/fileTransferDb")
    public int fileTransferDb(){
        Integer result = dictAdminService.fileTransferDB();
        dictAdminService.clearDictCache();
        return result;
    }

    @PostMapping("/clearDictCache")
    public int clearDictCache(){
        dictAdminService.clearDictCache();
        return 1;
    }
}
