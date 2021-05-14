package com.vekai.crops.dossier.service;

import cn.fisok.raw.kit.*;
import cn.fisok.web.kit.HttpKit;
import com.vekai.crops.constant.BizConst;
import com.vekai.crops.dossier.dao.DossierDao;
import com.vekai.crops.dossier.model.Dossier;
import com.vekai.crops.dossier.model.DossierItem;
import com.vekai.appframe.conf.dossier.dao.ConfDossierDao;
import com.vekai.appframe.conf.dossier.model.ConfDossier;
import com.vekai.appframe.conf.dossier.model.ConfDossierItem;
import com.vekai.common.fileman.FileManConsts;
import com.vekai.common.fileman.entity.FileEntity;
import com.vekai.common.fileman.kit.FileManKit;
import com.vekai.common.fileman.service.FileManService;
import cn.fisok.raw.lang.BizException;
import cn.fisok.sqloy.core.BeanCruder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DossierService {

    @Autowired
    private ConfDossierDao confDossierDao;
    @Autowired
    private FileManService fileManService;
    @Autowired
    private DossierDao dossierDao;


    @Autowired
    private BeanCruder beanCruder;


    public FileEntity getFileEntity(String fileEntityId) {
        return fileManService.getFileEntity(fileEntityId);
    }

    /**
     * 创建资料清单对象
     *
     * @param dosserDefKey
     * @param objectType
     * @param objectId
     * @return
     */
    public Dossier createDossier(String dosserDefKey, String objectType, String objectId) {
        ValidateKit.notEmpty(dosserDefKey, "参数dosserDefKey不能为空");
        ValidateKit.notEmpty(objectType, "参数objectType不能为空");
        ValidateKit.notEmpty(objectId, "参数objectId不能为空");

        //1. 查模板
        ConfDossier confDossier = confDossierDao.queryConfDossier(dosserDefKey);
        List<ConfDossierItem> confDossierItems = confDossierDao.queryConfDossierItems(dosserDefKey);
        if (confDossier == null) throw new BizException("资料清单模板{0}不存在", dosserDefKey);
        if (confDossierItems == null || confDossierItems.size() == 0)
            throw new BizException("资料清单模板{0}没有资料明细项", dosserDefKey);

        //2.创建实例
        final Dossier dossier = new Dossier();
        BeanKit.copyProperties(confDossier, dossier);
        dossier.setObjectType(objectType);
        dossier.setObjectId(objectId);
        //清除这几个变量，重新填充
        dossier.setCreatedBy(null);
        dossier.setCreatedTime(null);
        dossier.setUpdatedBy(null);
        dossier.setUpdatedTime(null);

        List<DossierItem> dossierItems = new ArrayList<DossierItem>();
        for (ConfDossierItem confDossierItem : confDossierItems) {
            DossierItem dossierItem = new DossierItem();
            dossierItems.add(dossierItem);
            BeanKit.copyProperties(confDossierItem, dossierItem);
            //清除这几个变量，重新填充
            dossierItem.setCreatedBy(null);
            dossierItem.setCreatedTime(null);
            dossierItem.setUpdatedBy(null);
            dossierItem.setUpdatedTime(null);
        }
        dossier.setItemList(dossierItems);

        //3. 保存实例
        beanCruder.save(dossier);
        dossierItems.forEach(dossierItem -> {
            dossierItem.setDossierId(dossier.getDossierId());
        });
        beanCruder.save(dossierItems);

        return dossier;
    }


    /**
     * 查询一个资料清单对象
     *
     * @param dossierId
     * @return
     */
    public Dossier getDossier(String dossierId) {
        Dossier dossier = null;

        //1.查对象
        dossier = dossierDao.queryDossier(dossierId);
        //2.查明细
        if (dossier != null) {
            List<DossierItem> dossierItems = dossierDao.queryDossierItems(dossierId);
            dossier.setItemList(dossierItems);

            //3.查文件
            List<FileEntity> fileEntities = dossierDao.queryFileEntities(dossierId);

            //4.组装对象
            Optional.ofNullable(dossier.getItemList())
                    .orElseGet(() -> new ArrayList<DossierItem>())
                    .forEach(dossierItem -> {
                        //找到这个子项下的所有文件
                        List<FileEntity> itemFileEntities = fileEntities
                                .stream()
                                .filter(fileEntity -> BizConst.DOSSER_ITEM.equals(fileEntity.getObjectType())&&dossierItem.getItemId().equals(fileEntity.getObjectId()))
                                .collect(Collectors.toList());

                        dossierItem.setFileEntities(itemFileEntities);
                    });
        }

        return dossier;
    }

    public Dossier queryDossierByObjectType(String objectType, String objectId) {
        return dossierDao.queryDossierByObjectType(objectType, objectId);
//        String sql = "SELECT * FROM CMON_DOSSIER WHERE OBJECT_TYPE=:objectType AND OBJECT_ID=:objectId";
//        Map<String,String> params = MapKit.mapOf("objectType",objectType,"objectId",objectId);
//        return beanCruder.selectOne(Dossier.class,sql,params);
    }

    /**
     * 建立文档清单明细项和文件的关联关系
     *
     * @param dossierItemId
     * @param fileEntityId
     * @return
     */
    public int addFileToItem(String fileEntityId, String dossierItemId) {
        FileEntity fileEntity = fileManService.getFileEntity(fileEntityId);
        if (fileEntity == null) return 0;

        fileEntity.setObjectType(BizConst.DOSSER_ITEM);
        fileEntity.setObjectId(dossierItemId);

        return beanCruder.update(fileEntity);
    }

    /**
     * 删除文件和明细项的关联
     * @param fileId
     * @param dossierItemId
     * @return
     */
    public int removeFileFromItem(String fileId,String dossierItemId) {
        String sql = "UPDATE CMON_FILE SET SHOW_CODE=:dossierItemId,OBJECT_ID='' where FILE_ID=:fileId";
        Map<String, String> params = MapKit.mapOf("fileId", fileId,"dossierItemId",dossierItemId);
        return beanCruder.execute(sql, params);
    }

    /**
     * 删除文件和明细项的关联
     * @param fileIdList
     * @param dossierItemId
     * @return
     */
    public int removeFileListFromItem(List<String> fileIdList,String dossierItemId) {
        String sql = "UPDATE CMON_FILE SET SHOW_CODE=:dossierItemId,OBJECT_ID='' where FILE_ID IN (:fileIdList)";
        Map<String, Object> params = MapKit.mapOf("fileIdList", fileIdList,"dossierItemId",dossierItemId);
        return beanCruder.execute(sql, params);
    }

    /**
     * 文件上传处理
     * @param dossierItemId
     * @param request
     * @return
     */
    public FileEntity execUpload(String dossierItemId, StandardMultipartHttpServletRequest request) {
        if (request.getFileMap().isEmpty()) return null;
        MultipartFile multipartFile = request.getFileMap().values().iterator().next();
        if (multipartFile.isEmpty()) return null;

        InputStream inputStream = null;
        try {
            inputStream = multipartFile.getInputStream();

            //存储文件
            final String suffix = FileManKit.getSuffix(multipartFile);
            FileEntity fileEntity = FileManKit.parseFileEntity(multipartFile);
            fileEntity.setStoreServiceName(FileManConsts.STORE_SERVICE_DOSSIER);    //指定文件用什么样的服务来保存
            fileEntity.setObjectType("DOSSER_ITEM");
            fileEntity.setObjectId(dossierItemId);

            //直接根据文件ID保存文件内容
            fileManService.saveAndStoreFile(fileEntity, inputStream, false, fe->{
                fileEntity.setStoredContent(fileEntity.getFileId()+suffix);
            });
            return fileEntity;
        } catch (IOException e) {
            throw new BizException("上传文件出错,[" + e.getMessage() + "]");
        } finally {
            IOKit.close(inputStream);
        }
    }


    public void renderOutputStream(String showCode, HttpServletResponse response) {
        FileEntity fileEntity = fileManService.getFileEntityByShowCode(showCode);
        ValidateKit.notNull(fileEntity, "showCode={0}文件不存在！",showCode);

        renderOutputStream(fileEntity,response);
    }

    public void renderOutputStream(FileEntity fileEntity, HttpServletResponse response) {
        ValidateKit.notNull(fileEntity, "文件不存在！",fileEntity);

        InputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;
        try {
            inputStream = fileManService.openFileInputStream(fileEntity);
            String contentType = StringKit.nvl(fileEntity.getContentType(),"octets/stream");
            HttpKit.renderStream(response, inputStream, contentType, null);
        } catch (Exception e) {
            throw new BizException("打开文件资源失败，请检查文件资源是否存在,fileId=" + fileEntity.getFileId() + ",[" + e.getMessage() + "]");
        } finally {
            IOKit.close(inputStream);
            IOKit.close(outputStream);
        }
    }

}
