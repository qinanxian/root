package com.vekai.crops.common.service;

import com.vekai.crops.util.ApiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class FileGatwayServiceImpl {
    protected static Logger logger = LoggerFactory.getLogger(FileGatwayServiceImpl.class);
    @Value("${mrapi.file.update}")
    String updatePath;
    @Value("${mrapi.file.down}")
    String downPath;
    @Autowired
    private ApiUtil apiUtil;

    private static final String DEFAULT_ENCODING = "utf-8";//编码
    private static final int PROTECTED_LENGTH = 51200;// 输入流保护 50KB

    public String readInfoStream(InputStream input) throws Exception {
        String sReturn = "";
        if (input == null) {
            throw new Exception("输入流为null");
        }
        //字节数组
        byte[] bcache = new byte[2048];
        int readSize = 0;//每次读取的字节长度
        int totalSize = 0;//总字节长度
        ByteArrayOutputStream infoStream = new ByteArrayOutputStream();
        try {
            //一次性读取2048字节
            while ((readSize = input.read(bcache)) > 0) {
                totalSize += readSize;
                if (totalSize > PROTECTED_LENGTH) {
                    throw new Exception("输入流超出50K大小限制");
                }
                //将bcache中读取的input数据写入infoStream
                infoStream.write(bcache,0,readSize);
            }
            sReturn = infoStream.toString(DEFAULT_ENCODING);
        } catch (IOException e1) {
            throw new Exception("输入流读取异常");
        } finally {
            try {
                //输入流关闭
                input.close();
            } catch (IOException e) {
                throw new Exception("输入流关闭异常");
            }
            infoStream.close();
        }

        return sReturn;
    }

    /**
     * 网关上传文件
     * @param inputStream
     * @param fileId
     */
    public String uploadFile(InputStream inputStream,String fileId) throws Exception{
        String s = apiUtil.putCephObject(updatePath, inputStream, fileId);
        return s;
    }

    /**
     * 网关下载文件
     * @param fileId
     */
    public InputStream downloadFile(String fileId) throws Exception{
        InputStream is = apiUtil.getCephObject(downPath, fileId);
        return is;
    }


}
