package com.vekai.common.fileman.impl;

import cn.fisok.raw.holder.ApplicationContextHolder;
import cn.fisok.raw.kit.*;
import cn.fisok.raw.lang.BizException;
import cn.fisok.sqloy.core.BeanCruder;
import cn.fisok.sqloy.serialnum.finder.SerialNumberGeneratorFinder;
import cn.fisok.sqloy.serialnum.generator.SerialNumberGenerator;
import com.vekai.common.fileman.FileManConsts;
import com.vekai.common.fileman.dao.FileManDao;
import com.vekai.common.fileman.entity.FileEntity;
import com.vekai.common.fileman.entity.FileEntityHist;
import com.vekai.common.fileman.kit.FileManKit;
import com.vekai.common.fileman.service.FileManService;
import com.vekai.common.fileman.service.FileStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.GeneratedValue;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Consumer;

@Service
public class FileManServiceImpl implements FileManService {

    @Autowired
    SerialNumberGeneratorFinder serialNumberGeneratorFinder;
    @Autowired(required = false)
    protected FileManDao fileManDao;
    @Autowired
    BeanCruder beanCruder;

    public SerialNumberGeneratorFinder getSerialNumberGeneratorFinder() {
        return serialNumberGeneratorFinder;
    }

    public void setSerialNumberGeneratorFinder(SerialNumberGeneratorFinder serialNoGeneratorFinder) {
        this.serialNumberGeneratorFinder = serialNoGeneratorFinder;
    }

    public FileManDao getFileManDao() {
        if(fileManDao == null){
            this.fileManDao = ApplicationContextHolder.getBean(FileManDao.class);
        }
        return fileManDao;
    }

    public void setFileManDao(FileManDao fileManDao) {
        this.fileManDao = fileManDao;
    }

    public BeanCruder getbeanCruder() {
        return beanCruder;
    }

    public void setbeanCruder(BeanCruder beanCruder) {
        this.beanCruder = beanCruder;
    }

    public FileEntity getFileEntity(String fileId) {
        return getFileManDao().queryFileEntity(fileId);
    }

    public FileEntity getFileEntityByShowCode(String showCode) {
        return getFileManDao().queryFileEntityByShowCode(showCode);
    }

    public FileEntityHist getFileEntityHist(String fileHistId) {
        return getFileManDao().queryFileEntityHist(fileHistId);
    }

    public List<FileEntityHist> getFileEntityHistList(String fileId) {
        return getFileManDao().queryFileEntityHistList(fileId);
    }

    public int deleteFileEntity(String fileId) {
        FileEntity fileEntity = getFileEntity(fileId);
        if(fileEntity == null)return 0;
        int ret = 0;

        //1.????????????
        FileStoreService storeService = lookupStoreService(fileEntity);
        ret += storeService.deleFile(fileEntity);
        ret += beanCruder.delete(fileEntity);

        //2.??????????????????
        List<FileEntityHist> histList = getFileEntityHistList(fileId);
        for(FileEntityHist fileEntityHist : histList){
            FileStoreService histStoreService = lookupStoreService(fileEntityHist);
            ret += histStoreService.deleteFileHist(fileEntityHist);
        }
        ret += beanCruder.delete(histList);

        return ret;
    }

    public FileStoreService lookupStoreService(FileEntity fileEntity) {
        return lookupStoreService(fileEntity.getStoreServiceName());
    }
    public FileStoreService lookupStoreService(FileEntityHist fileEntityHist) {
        return lookupStoreService(fileEntityHist.getStoreServiceName());
    }

    public FileStoreService lookupStoreService(String storeServiceName) {
        String serviceName = StringKit.nvl(storeServiceName, FileManConsts.STORE_SERVICE_DEFAULT);
        FileStoreService service = null;
        try{
            service = ApplicationContextHolder.getBean(serviceName,FileStoreService.class);
        }catch (Exception e){
            throw new BizException("????????????????????????????????????????????????StoreServiceName="+storeServiceName,e);
        }
        return service;
    }

    public InputStream openFileInputStream(FileEntity fileEntity) {
        FileStoreService storeService = lookupStoreService(fileEntity);
        return storeService.openInputStream(fileEntity);
    }

    public InputStream openFileHistInputStream(FileEntityHist fileEntityHist) {
        FileStoreService storeService = lookupStoreService(fileEntityHist);
        return storeService.openInputStream(fileEntityHist);
    }


    public void fillSerialNo(Object object) {
        List<Field> fields = JpaKit.getGeneratedValueFields(object.getClass());
        for (Field field : fields) {
            Object fieldValue = BeanKit.getPropertyValue(object, field.getName());
            if (fieldValue == null || StringKit.isBlank(fieldValue.toString())) {
                GeneratedValue gv = field.getAnnotation(GeneratedValue.class);
                String generatorId = StringKit.isBlank(gv.generator()) ? object.getClass().getName() + "." + field.getName() : gv.generator();
                SerialNumberGenerator<String> serialNoGenerator = serialNumberGeneratorFinder.find(generatorId);
                String serialNo = serialNoGenerator.next(generatorId, object);
                BeanKit.setPropertyValue(object, field.getName(), serialNo);
            }
        }
    }

    public int saveAndStoreFile(FileEntity fileEntity, InputStream inputStream, boolean writeHist, Consumer<FileEntity> beforeSave) {
        int ret = 0;
        FileStoreService storeService = lookupStoreService(fileEntity);

        //1. ????????????????????????????????????
        fillSerialNo(fileEntity);

        //2.????????????????????????????????????????????????
        if(writeHist&&StringKit.isNotBlank(fileEntity.getFileId())){
            //????????????????????????
            FileEntityHist fileEntityHist = new FileEntityHist();
            BeanKit.copyProperties(fileEntity,fileEntityHist);
            //???????????????
            fillSerialNo(fileEntityHist);
            //?????????????????????????????????????????????
            String suffix = FileManKit.getSuffix(fileEntity.getStoredContent());
            fileEntityHist.setStoredContent(fileEntityHist.getFileHistId()+suffix);
            if(StringKit.isBlank(fileEntityHist.getVersionCode())){
                fileEntityHist.setVersionCode("1");
            }
            ret += beanCruder.save(fileEntityHist);

            //???????????????????????????
            InputStream in = null;
            try{
                in = storeService.openInputStream(fileEntity);
                if( in != null){
                    ret += storeService.storeFileHist(fileEntityHist,in);
                }
            }catch(Exception e){
                LogKit.error("??????????????????????????????",e);
            }finally {
                IOKit.close(in);
            }
        }

        //3.????????????
        if(beforeSave != null){
            beforeSave.accept(fileEntity);
        }
        beanCruder.save(fileEntity);
        //5.????????????
        ret += storeService.storeFile(fileEntity,inputStream);

        return ret;
    }
}
