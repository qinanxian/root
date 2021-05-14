package com.vekai.crops.formatdoc.aspect;

import com.vekai.crops.dossier.model.Dossier;
import com.vekai.crops.dossier.model.DossierItem;
import com.vekai.auth.entity.User;
import com.vekai.auth.service.AuthService;
import com.vekai.common.fileman.entity.FileEntity;
import cn.fisok.raw.kit.BeanKit;
import cn.fisok.raw.kit.StringKit;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

@Aspect
@Component
public class FormatdocUserNameAspect {
    @Autowired
    private AuthService authService;

    @AfterReturning(pointcut = "(execution(* com.vekai.crops.formatdoc.FormatdocController.getDossier(..)))", returning = "retData")
    public void doAfterQueryMeta(JoinPoint joinPoint, Object retData){
        if(retData instanceof Dossier){
            Dossier dossier = (Dossier)retData;
            List<DossierItem> itemList = dossier.getItemList();
            if(itemList==null||itemList.size()==0)return;
            itemList.forEach(dossierItem -> {
                List<? extends FileEntity> fileEntities = dossierItem.getFileEntities();

                if(fileEntities!=null&&fileEntities.size()>0){
                    List<FileEntityWrapper> fileEntityWrapperList = new ArrayList<FileEntityWrapper>();
                    fileEntities.forEach(fileEntity -> {
                        FileEntityWrapper fileEntityWrapper = new FileEntityWrapper();
                        BeanKit.copyProperties(fileEntity,fileEntityWrapper);

                        if(StringKit.isNotBlank(fileEntityWrapper.getCreatedBy())){
                            User user = authService.getUser(fileEntityWrapper.getCreatedBy());
                            if(user!=null)fileEntityWrapper.setCreatedByName(user.getName());
                        }
                        if(StringKit.isNotBlank(fileEntityWrapper.getUpdatedBy())){
                            User user = authService.getUser(fileEntityWrapper.getUpdatedBy());
                            if(user!=null)fileEntityWrapper.setUpdatedByName(user.getName());
                        }
                        fileEntityWrapperList.add(fileEntityWrapper);
                    });

                    dossierItem.setFileEntities(fileEntityWrapperList);
                }
            });
        }

    }

    public static class FileEntityWrapper extends FileEntity{
        @Transient
        private String createdByName ;
        @Transient
        private String updatedByName ;

        public String getCreatedByName() {
            return createdByName;
        }

        public void setCreatedByName(String createdByName) {
            this.createdByName = createdByName;
        }

        public String getUpdatedByName() {
            return updatedByName;
        }

        public void setUpdatedByName(String updatedByName) {
            this.updatedByName = updatedByName;
        }
    }
}
