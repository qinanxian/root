package com.vekai.common.fileman.impl;

import cn.fisok.raw.kit.FileKit;
import cn.fisok.raw.kit.IOKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.kit.ValidateKit;
import com.vekai.common.fileman.entity.FileEntity;
import com.vekai.common.fileman.entity.FileEntityHist;
import com.vekai.common.fileman.service.FileStoreService;
import cn.fisok.raw.lang.BizException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 默认文件存储服务，使用本地磁盘
 */
public class FileStoreServiceImpl implements FileStoreService {
    /** 存储根目录 */
    private String storageRoot = "/data/rober/file";
    /** 本服务对应文件存储目录 */
    private String directory = "/default";
    /** 历史文件对应存储目录*/
    private String histDirectory = directory +"-hist";
    /** 文件缓冲区大小 4K */
    private int bufferSize = 1024 * 4;

    public String getStorageRoot() {
        return storageRoot;
    }

    public void setStorageRoot(String storageRoot) {
        this.storageRoot = storageRoot;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getHistDirectory() {
        return histDirectory;
    }

    public void setHistDirectory(String histDirectory) {
        this.histDirectory = histDirectory;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public int storeFile(FileEntity fileEntity, InputStream inputStream) {
        String storeContent = fileEntity.getStoredContent();
        return storeFile(inputStream, directory,storeContent);
    }

    public int storeFileHist(FileEntityHist fileEntityHist, InputStream inputStream) {
        String storeContent = fileEntityHist.getStoredContent();
        return storeFile(inputStream, histDirectory,storeContent);
    }

    public int storeFile(InputStream inputStream,String directory,String storeContent){
        String fullPath = StringKit.format("{0}{1}/{2}",storageRoot,directory,storeContent);
        File storeFile = null;
        try{
            storeFile = FileKit.touchFile(fullPath);
        } catch (IOException e) {
            throw new BizException("创建文件出错,{0}",fullPath);
        }

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(storeFile, false);
            byte[] buffer = new byte[bufferSize];
            int len;
            while ((len = inputStream.read(buffer)) >= 0) {
                if (len == 0) continue;
                outputStream.write(buffer, 0, len);
            }
        } catch (IOException ex) {
            throw new BizException("写文件出错,{0}",fullPath);
        } finally {
            IOKit.close(outputStream);
        }

        return 1;
    }

    public int deleFile(String directory,String storeContent){
        ValidateKit.notBlank(storeContent,"文件存储名不能为空");

        String fullPath = StringKit.format("{0}{1}/{2}",storageRoot,directory,storeContent);
        File file = new File(fullPath);
        if(file.exists()){
            FileKit.deleteFile(file);
            return 1;
        }else{
            return 0;
        }

    }

    public int deleFile(FileEntity fileEntity) {
        return deleFile(directory,fileEntity.getStoredContent());
    }

    public File lookupFile(String directory,String storeContent){
        ValidateKit.notBlank(storeContent,"文件存储名不能为空");
        String fullPath = StringKit.format("{0}{1}/{2}",storageRoot,directory,storeContent);
        return new File(fullPath);
    }
    public InputStream openInputStream(FileEntity fileEntity) {
        File file = lookupFile(directory,fileEntity.getStoredContent());
        if(file == null || !file.exists())throw new BizException("打开文件出错，文件:"+file.getAbsolutePath()+"不存在");
        if(file.isDirectory())throw new BizException("打开文件出错，"+file.getAbsolutePath()+"应该是一个文件，而非目录");
        try {
            return FileKit.openInputStream(file);
        } catch (IOException e) {
            throw new BizException("打开文件出错，文件:"+file.getAbsolutePath(),e);
        }
    }

    public InputStream openFileHistInputStream(FileEntityHist fileEntityHist) {
        File file = lookupFile(histDirectory,fileEntityHist.getStoredContent());
        if(file == null)return null;
        try {
            return FileKit.openInputStream(file);
        } catch (IOException e) {
            throw new BizException("打开文件出错，文件:"+file.getAbsolutePath(),e);
        }
    }

    public int deleteFileHist(FileEntityHist fileEntityHist) {
        return deleFile(histDirectory,fileEntityHist.getStoredContent());
    }
}
