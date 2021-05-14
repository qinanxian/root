package com.vekai.appframe.cmon.file.controller;

import cn.fisok.raw.kit.*;
import cn.fisok.web.kit.HttpKit;
import com.vekai.common.fileman.FileManConsts;
import com.vekai.common.fileman.entity.FileEntity;
import com.vekai.common.fileman.entity.FileEntityHist;
import com.vekai.common.fileman.kit.FileManKit;
import com.vekai.common.fileman.service.FileManService;
import com.vekai.office.word.kit.WordKit;
import cn.fisok.raw.lang.BizException;
import cn.fisok.sqloy.core.BeanCruder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/common/FileOperateController")
public class FileOperateController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileOperateController.class);
    private static final boolean WRITE_HIST = true;

    @Autowired
    FileManService fileManService;

    @Autowired
    BeanCruder beanCruder;

    /**
     * 简易上传文件，返回 FileEntity 对象
     *
     * @param request
     * @return
     */
    @PostMapping("/uploadFile/simple")
    @Transactional
    public FileEntity simpleUpload(StandardMultipartHttpServletRequest request) {
        return uploadAndSaveFile(request, null, null, null, false);
    }

    /**
     * 上传并更新普通附件
     *
     * @param request
     * @param objectId:指 CMON_DOCLIST_ITEM 表的 ITEM_ID
     * @param objectType
     * @param fileId
     * @return
     */
    @PostMapping("/uploadFile/{objectId}/{objectType}/{fileId}")
    @Transactional
    public String uploadAndUpdateFile(StandardMultipartHttpServletRequest request, @PathVariable("objectId") String objectId,
                                       @PathVariable("objectType") String objectType, @PathVariable("fileId") String fileId) {
        FileEntity fileEntity = uploadAndSaveFile(request, fileId, objectId, objectType, false);
        fillFileIdToDocListItem(objectId, fileEntity.getFileId());
        return fileEntity.getFileId();
    }

    /**
     * 上传并更新普通附件
     *
     * @param request
     * @param objectId:指 CMON_DOCLIST_ITEM_CHANGE 表的 ITEM_CHANGE_ID
     * @param objectType
     * @param fileId
     * @return
     */
    @PostMapping("/uploadChangeFile/{objectId}/{objectType}/{fileId}/{planChangeApplyId}")
    @Transactional
    public Integer uploadAndUpdateChangeFile(StandardMultipartHttpServletRequest request, @PathVariable("objectId") String objectId,
                                             @PathVariable("objectType") String objectType, @PathVariable("fileId") String fileId,
                                             @PathVariable("planChangeApplyId") String planChangeApplyId) {
        FileEntity fileEntity = uploadAndSaveFile(request, fileId, objectId, objectType, false);
        return fillFileIdToDocListItemChange(objectId, fileEntity.getFileId(), planChangeApplyId);
    }

//    /**
//     * 新增一个普通附件
//     *
//     * @param request
//     * @param objectId:指 CMON_DOCLIST_ITEM 表的 ITEM_ID
//     * @param objectType
//     * @param groupCode
//     * @return
//     */
//    @PostMapping("/uploadAndCreateFile/{objectId}/{objectType}/{groupCode}")
//    @Transactional
//    public Integer uploadAndCreateFile(StandardMultipartHttpServletRequest request, @PathVariable("objectId") String objectId,
//                                       @PathVariable("objectType") String objectType, @PathVariable("groupCode") String groupCode) {
//        DocListItemPO docListItemPO = docListService.createDoclistItem(objectId, objectType, groupCode);
//        FileEntity fileEntity = uploadAndSaveFile(request, null, docListItemPO.getItemId(), objectType, false);
//        return fillFileIdToDocListItem(docListItemPO.getItemId(), fileEntity.getFileId());
//    }

    /**
     * 上传并更新模板附件
     *
     * @param request
     * @param objectId:指 CONF_DOCLIST_ITEM 表的 ITEM_CODE
     * @param objectType
     * @param fileId
     * @return
     */
    @PostMapping("/uploadTplFile/{objectId}/{objectType}/{fileId}")
    @Transactional
    public Integer updateAndUploadTplFile(StandardMultipartHttpServletRequest request, @PathVariable("objectId") String objectId
            , @PathVariable("objectType") String objectType, @PathVariable("fileId") String fileId) {
        FileEntity fileEntity = uploadAndSaveFile(request, fileId, objectId, objectType, true);
        return fillFileIdToConfDocListItem(objectId, fileEntity.getFileId());
    }

    private FileEntity uploadAndSaveFile(StandardMultipartHttpServletRequest request, String fileEntityId,
                                         String objectId, String objectType, boolean isTplFile) {
        if (request.getFileMap().isEmpty()) return null;
        MultipartFile multipartFile = request.getFileMap().values().iterator().next();
        if (multipartFile.isEmpty()) return null;

        InputStream inputStream = null;
        try {

            inputStream = multipartFile.getInputStream();

            //存储文件
            final String suffix = FileManKit.getSuffix(multipartFile);
            FileEntity fileEntity = FileManKit.parseFileEntity(multipartFile);
            fileEntity.setStoreServiceName(FileManConsts.STORE_SERVICE_DEFAULT);    //指定文件用什么样的服务来保存
            fileEntity.setObjectType(objectType);
            fileEntity.setObjectId(objectId);

            fileManService.saveAndStoreFile(fileEntity, inputStream, true, fe->{
                fileEntity.setStoredContent(fileEntity.getFileId()+suffix);
            });
            return fileEntity;
        } catch (IOException e) {
            LOGGER.error("上传文件出错", e);
            throw new BizException("上传文件出错,[" + e.getMessage() + "]");
        } finally {
            IOKit.close(inputStream);
        }
    }

    private int fillFileIdToConfDocListItem(String itemCode, String fileId) {
        return beanCruder.execute("UPDATE CONF_DOCLIST_ITEM SET FILE_ID=:fileId WHERE ITEM_CODE=:itemCode",
                MapKit.mapOf("fileId", fileId, "itemCode", itemCode));
    }

    private int fillFileIdToDocListItem(String itemId, String fileId) {
        return beanCruder.execute("UPDATE CMON_DOCLIST_ITEM SET FILE_ID=:fileId WHERE ITEM_ID=:itemId",
                MapKit.mapOf("fileId", fileId, "itemId", itemId));
    }

    private int fillFileIdToDocListItemChange(String itemChangeId, String fileId, String planChangeApplyId) {
        return beanCruder.execute("UPDATE CMON_DOCLIST_ITEM_CHANGE SET FILE_ID=:fileId WHERE ITEM_CHANGE_ID=:itemChangeId",
                MapKit.mapOf("fileId", fileId, "itemChangeId", itemChangeId));
    }


    /**
     * 下载普附件
     *
     * @param fileId
     * @param response
     * @param request
     */
    @GetMapping("/downloadFile/{fileId}")
    public void downloadFile(@PathVariable("fileId") String fileId, HttpServletResponse response, HttpServletRequest request) {
        downloadFile(request, response, fileId, null, false);
    }

    /**
     * 下载普附件的历史版本
     *
     * @param fileHistId
     * @param response
     * @param request
     */
    @GetMapping("/downloadFileHist/{fileHistId}")
    public void downloadFileHist(@PathVariable("fileHistId") String fileHistId, HttpServletResponse response, HttpServletRequest request) {
        downloadFile(request, response, null, fileHistId, false);
    }

    /**
     * 下载普附件
     *
     * @param fileId
     * @param response
     * @param request
     */
    @GetMapping("/downloadTplFile/{fileId}")
    public void downloadTplFile(@PathVariable("fileId") String fileId, HttpServletResponse response, HttpServletRequest request) {
        downloadFile(request, response, fileId, null, true);
    }

    /**
     * 下载普附件的历史版本
     *
     * @param fileHistId
     * @param response
     * @param request
     */
    @GetMapping("/downloadTplFileHist/{fileHistId}")
    public void downloadTplFileHist(@PathVariable("fileHistId") String fileHistId, HttpServletResponse response, HttpServletRequest request) {
        downloadFile(request, response, null, fileHistId, true);
    }

    private void downloadFile(HttpServletRequest request, HttpServletResponse response, String fileEntityId,
                              String fileHistEntityId, boolean isTplFile) {
        if (StringKit.isBlank(fileEntityId) && StringKit.isBlank(fileHistEntityId))
            throw new BizException("fileEntityId and fileHistEntityId is empty");
        if (StringKit.isNoneBlank(fileEntityId) && StringKit.isNoneBlank(fileHistEntityId))
            throw new BizException("both fileEntityId and fileHistEntityId are not empty");
        boolean isHistFile = StringKit.isBlank(fileEntityId) && !StringKit.isBlank(fileHistEntityId);

        if (!isHistFile) {
            FileEntity fileEntity = fileManService.getFileEntity(fileEntityId);
            InputStream inputStream = fileManService.openFileInputStream(fileEntity);
            cn.fisok.web.kit.HttpKit.renderStream(response,inputStream,fileEntity.getContentType(),null);
        } else {
            FileEntityHist fileEntityHist = fileManService.getFileEntityHist(fileHistEntityId);
            InputStream inputStream = fileManService.openFileInputStream(fileEntityHist);
            cn.fisok.web.kit.HttpKit.renderStream(response,inputStream,fileEntityHist.getContentType(),null);
        }
    }

    /**
     * 在线查看
     *
     * @param fileId
     * @param response
     */
    @GetMapping("/showFile/{fileId:.+}")
    public void showFile(@PathVariable("fileId") String fileId, HttpServletResponse response) {
        showFile(fileId, response, false);
    }

    /**
     * 在线查看模板
     *
     * @param fileId
     * @param response
     */
    @GetMapping("/showTplFile/{fileId}")
    public void showTplFile(@PathVariable("fileId") String fileId, HttpServletResponse response) {
        showFile(fileId, response, true);

    }

    //todo can be optimized
    private void showFile(String fileId, HttpServletResponse response, boolean isTplFile) {
        FileEntity fileEntity = fileManService.getFileEntity(fileId);
        ValidateKit.notNull(fileEntity, "文件不存在！");

        InputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;
        try {
            inputStream = fileManService.openFileInputStream(fileEntity);


            if (fileEntity.getName().endsWith("doc") || fileEntity.getName().endsWith("DOC")
                    || fileEntity.getName().endsWith("docx")|| fileEntity.getName().endsWith("DOCX")
                    || fileEntity.getName().endsWith("txt")|| fileEntity.getName().endsWith("TXT")
                    || fileEntity.getName().endsWith("json")|| fileEntity.getName().endsWith("JSON")) {
                outputStream = new ByteArrayOutputStream();
                WordKit.wordToPdf(inputStream, outputStream);
                inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            }

            HttpKit.renderStream(response, inputStream, "octets/stream", null);
        } catch (Exception e) {
            LOGGER.error("打开文件资源失败，请检查文件资源是否存在,fileId=" + fileId, e);
            throw new BizException("打开文件资源失败，请检查文件资源是否存在,fileId=" + fileId + ",[" + e.getMessage() + "]");
        } finally {
            IOKit.close(inputStream);
            IOKit.close(outputStream);
        }
    }
}
