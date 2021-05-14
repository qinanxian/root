package com.vekai.crops.formatdoc;


import com.vekai.crops.dossier.model.Dossier;
import com.vekai.crops.dossier.service.DossierService;
import com.vekai.crops.fileman.FileManController;
import com.vekai.common.fileman.service.FileManService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/common/formatdoc")
public class FormatdocController extends FileManController {
    @Autowired
    private DossierService dossierServicer;
    @Autowired
    private FileManService fileManService;

    @ApiOperation(value = "取格式化报告对象")
    @GetMapping("/byId/{dossierId}")
    public Dossier getDossier(@PathVariable("dossierId") String dossierId) {
        return dossierServicer.getDossier(dossierId);
    }

    public FileManService getFileManService() {
        return fileManService;
    }

}
