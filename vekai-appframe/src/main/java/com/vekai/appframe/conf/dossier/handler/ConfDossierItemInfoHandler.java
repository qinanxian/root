package com.vekai.appframe.conf.dossier.handler;

import com.vekai.appframe.conf.dossier.dao.ConfDossierDao;
import com.vekai.appframe.conf.dossier.model.ConfDossierItem;
import com.vekai.dataform.handler.impl.BeanDataOneHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.lang.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ConfDossierItemInfoHandler extends BeanDataOneHandler<ConfDossierItem> {

    @Autowired
    ConfDossierDao confDossierDao;

    @Override
    @Transactional
    public int save(DataForm dataForm, ConfDossierItem confDossierItem) {
        String itemDefKey = confDossierItem.getItemDefKey();
        if(StringKit.isNotBlank(itemDefKey)){
            //父类定义号相同
            ConfDossierItem item = confDossierDao.queryConfDossierItemByDefKey(confDossierItem.getDossierDefKey(),itemDefKey);
            //和数据库已有的itemDefKey相同，ID不同，
            boolean exists = (item!=null
                    &&!StringKit.equals(item.getItemDefId(),confDossierItem.getItemDefId())
            );
            if(exists){
                throw new BizException("定义KEY={0}已经存在",itemDefKey);
            }
        }
        return super.save(dataForm, confDossierItem);
    }
}

