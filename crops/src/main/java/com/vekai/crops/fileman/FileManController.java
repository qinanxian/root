package com.vekai.crops.fileman;

import cn.fisok.raw.kit.*;
import cn.fisok.web.kit.HttpKit;
import com.vekai.common.fileman.entity.FileEntity;
import com.vekai.common.fileman.service.FileManService;
import com.vekai.office.word.kit.WordKit;
import cn.fisok.raw.io.ByteOutputStream;
import cn.fisok.raw.lang.BizException;
import cn.fisok.raw.lang.ValueObject;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 文件通用操作，抽象类
 */
public abstract class FileManController {

    public abstract FileManService getFileManService();

    @ApiOperation(value = "保存格式化报告")
    @PostMapping("/saveContent/{showCode}")
    public FileEntity saveContent(@PathVariable("showCode") String showCode, StandardMultipartHttpServletRequest request) {
        FileManService fileManService = getFileManService();

        FileEntity fileEntity = getFileManService().getFileEntityByShowCode(showCode);
        ValidateKit.notNull(fileEntity, "文件不存在！",fileEntity);

        if (request.getFileMap().isEmpty()) return null;
        MultipartFile multipartFile = request.getFileMap().values().iterator().next();
        if (multipartFile.isEmpty()) return null;

        InputStream inputStream = null;
        try {
            inputStream = multipartFile.getInputStream();
            fileManService.saveAndStoreFile(fileEntity, inputStream,true,fe->{
                int versionCode = ValueObject.valueOf(fe.getVersionCode()).intValue(1);
                versionCode = versionCode+1;
                fe.setVersionCode(""+versionCode);
            });
        } catch (IOException e) {
            throw new BizException("上传文件出错,[" + e.getMessage() + "]");
        } finally {
            IOKit.close(inputStream);
        }
        return fileEntity;
    }



    @ApiOperation(value = "读取格式化流")
    @GetMapping("/getContent/{showCode}")
    public void getContent(@PathVariable("showCode") String showCode, HttpServletRequest request, HttpServletResponse response) {
        FileManService fileManService = getFileManService();

        FileEntity fileEntity = fileManService.getFileEntityByShowCode(showCode);
        ValidateKit.notNull(fileEntity, "文件不存在！",fileEntity);

        InputStream inputStream = null;
        try {
            inputStream = fileManService.openFileInputStream(fileEntity);
            String contentType = StringKit.nvl(fileEntity.getContentType(),"octets/stream");
            String fileName = cn.fisok.web.kit.HttpKit.iso8859(fileEntity.getName(),request);
            Map<String,String> headers = MapKit.mapOf("Content-Disposition","attachment; filename=" + fileName);
            cn.fisok.web.kit.HttpKit.renderStream(response, inputStream, contentType, headers);
        } catch (Exception e) {
            throw new BizException("打开文件资源失败，请检查文件资源是否存在,fileId=" + fileEntity.getFileId() + ",[" + e.getMessage() + "]");
        } finally {
            IOKit.close(inputStream);
        }
    }

    @ApiOperation(value = "读取格式化，并转PDF")
    @GetMapping("/getContentPDF/{showCode}")
    public void getContentPDF(@PathVariable("showCode") String showCode, HttpServletRequest request,HttpServletResponse response) {
        FileManService fileManService = getFileManService();

        FileEntity fileEntity = fileManService.getFileEntityByShowCode(showCode);
        ValidateKit.notNull(fileEntity, "文件不存在！",fileEntity);

        InputStream inputStream = null;
        ByteOutputStream outputStream = null;
        try {
            inputStream = fileManService.openFileInputStream(fileEntity);
            String fileName = StringKit.nvl(fileEntity.getName(),"");
            Pattern pattern = Pattern.compile(".*(.doc|.docx)$",Pattern.CASE_INSENSITIVE);

//            if (fileName.matches("\\w+(\\.doc|\\.docx)$")) {
            if (pattern.matcher(fileName).find()) {
                outputStream = new ByteOutputStream();
                WordKit.wordToPdf(inputStream, outputStream);
                inputStream = new ByteArrayInputStream(outputStream.toBytes());

                String contentType = "application/pdf";
//                String downFileName = HttpKit.iso8859(fileEntity.getName(),request);
//                Map<String,String> headers = MapKit.mapOf("Content-Disposition","attachment; filename=" + downFileName);
                HttpKit.renderStream(response, inputStream, contentType, null);
            }
        } catch (Exception e) {
            throw new BizException("打开文件资源失败，请检查文件资源是否存在,fileId=" + fileEntity.getFileId() + ",[" + e.getMessage() + "]");
        } finally {
            IOKit.close(inputStream);
        }
    }
}
