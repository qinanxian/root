/*
 * Copyright 2019-2029 FISOK(www.fisok.cn).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.fisok.raw.kit;

import cn.fisok.raw.lang.RawException;
import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/12/28 23:24
 * @desc :
 */
public abstract class PacketFileKit {
    private static Logger logger = LoggerFactory.getLogger(PacketFileKit.class);
    public static String[] ZIP_RETRY_CHARSET = new String[]{"GBK","UTF-8"};

    private static File touchOutFile(File dir, String name, boolean isFile) throws IOException {
        File outFile = new File(dir.getAbsolutePath()+"/"+name);
        if(isFile){
            FileKit.touchFile(outFile,true);
        }else{
            outFile.mkdirs();
        }
        return outFile;
    }
    public static List<File> unzipExec(File file, File destDir, boolean recursion, Charset charset){
        if (!file.getName().toLowerCase().endsWith(".zip")) {
            throw new RawException("文件{0}不是一个ZIP文件",file.getAbsolutePath());
        }

        Charset packFileCharset = charset==null?Charset.defaultCharset():charset;
        List<File> fileList = new ArrayList<>();
        //文件不存在，则创建他
        if(!destDir.exists()) destDir.mkdirs();

        try {
            ZipFile zip = new ZipFile(file,ZipFile.OPEN_READ, packFileCharset);
            for(Enumeration entries = zip.entries(); entries.hasMoreElements();){
                ZipEntry entry = (ZipEntry)entries.nextElement();
                String zipEntryName = entry.getName();
                InputStream inputStream = null;
                OutputStream outputStream = null;

                try{
                    File outFile = touchOutFile(destDir,zipEntryName,!entry.isDirectory());
                    logger.debug("UNZIP FILE -> "+outFile.getAbsolutePath());

                    if(entry.isDirectory())continue;

                    inputStream = zip.getInputStream(entry);
                    outputStream = new FileOutputStream(outFile);

                    byte[] buffer = new byte[4096];
                    int len;
                    while((len=inputStream.read(buffer))>0){
                        outputStream.write(buffer,0,len);
                    }
                    outputStream.flush();
                    IOKit.close(outputStream);

                    //如果有子压缩包，也解压他
                    if(recursion){
                        recursionUnPack(fileList,outFile,charset,recursion);
                    }

                    fileList.add(outFile);
                }catch(IOException e){
                    throw new RawException(e);
                }finally {
                    IOKit.close(inputStream);
                    IOKit.close(outputStream);
                }
            }
        } catch (IOException e) {
            throw new RawException(e);
        }

        return uniqueFiles(fileList);
    }

    private static void recursionUnPack(List<File> fileList,File outFile,Charset charset,boolean recursion){
        String fullPath = outFile.getAbsolutePath();
        String extName = StringKit.nvl(FileKit.getSuffix(outFile.getName()),"").toLowerCase();
        if("rar".equals(extName)||"zip".equals(extName)){
            int lastDotIdx = fullPath.lastIndexOf(".");
            File fileAsFolder = new File(fullPath.substring(0,lastDotIdx));
            List<File> subList = unpack(outFile,fileAsFolder,recursion,charset);
            if(subList != null && !subList.isEmpty()){
                fileList.addAll(subList);
            }
        }
    }
    /**
     * 解压ZIP
     *
     * @param file RAR文件
     * @param destDir 解压目标目录
     * @param recursion 是否递归解压压缩包中的压缩文件
     * @param charset ZIP文件字符集
     * @return list
     */
    public static List<File> unzip(File file, File destDir, boolean recursion,Charset charset) {
        List<File> fileList = null;
        boolean retry = false;
        try{
            fileList = unzipExec(file,destDir,recursion,charset);
        }catch (Exception e) {
            //如果是字符集问题，多使用几个字符集试一下
            if ("MALFORMED".equals(e.getMessage())) {
                retry = true;
                for(int i=0;i<ZIP_RETRY_CHARSET.length&&retry;i++){
                    try{
                        Charset retryCharset = Charset.forName(ZIP_RETRY_CHARSET[i]);
                        logger.warn("用字符集["+charset+"]解压文件["+file+"]出错，换字符集["+retryCharset+"]重试");
                        fileList = unzipExec(file,destDir,recursion,retryCharset);
                        retry = false;
                    }catch (Exception e1){
                        retry = "MALFORMED".equals(e.getMessage());
                    }
                }
            }
        }
        return fileList;
    }

    public static List<File> unzip(File file, File destDir){
        return unzip(file,destDir,false,Charset.defaultCharset());
    }

    /**
     * 解压RAR
     *
     * @param file RAR文件
     * @param destDir 解压目标目录
     * @param recursion 是否递归解压压缩包中的压缩文件
     * @param charset charset
     * @return list
     */
    public static List<File> unrar(File file, File destDir,boolean recursion,Charset charset) {
        if (!file.getName().toLowerCase().endsWith(".rar")) {
            throw new RawException("文件{0}不是一个RAR文件",file.getAbsolutePath());
        }

        Charset packFileCharset = charset==null?Charset.defaultCharset():charset;
        List<File> fileList = new ArrayList<>();
        //文件不存在，则创建他
        if(!destDir.exists()) destDir.mkdirs();

        InputStream packFileInputStream = null;
        try {
            packFileInputStream = FileKit.openInputStream(file);
            Archive archive = new Archive(packFileInputStream);
            if(archive.isEncrypted()){
                throw new RawException(file.getAbsolutePath() + " IS ENCRYPTED!");
            }
            String packFileName = FileKit.getFileName(file,false);

            List<FileHeader> files =  archive.getFileHeaders();
            for (FileHeader fileHeader : files) {
                String fileName = StringKit.nvl(fileHeader.getFileNameW(),fileHeader.getFileNameString());

                fileName = fileName.replaceAll("\\\\","/");
                if(StringKit.isBlank(fileName))continue;
                if(fileHeader.isEncrypted()){
                    throw new Exception(file.getAbsolutePath()+"!"+fileName + " IS ENCRYPTED!");
                }

                if(fileName.equals(packFileName))continue;
                if(fileName.startsWith(packFileName+"/")){
                    fileName = fileName.substring(packFileName.length()+1);
                }

                OutputStream outputStream = null;
                File outFile = touchOutFile(destDir,fileName,!fileHeader.isDirectory());
                logger.debug("UNRAR FILE -> "+outFile.getAbsolutePath());

                if(fileHeader.isDirectory())continue;
                try{
                    outputStream = new FileOutputStream(outFile);
                    archive.extractFile(fileHeader, outputStream);
                    outputStream.flush();

                    if(recursion){
                        recursionUnPack(fileList,outFile,charset,recursion);
                    }

                    fileList.add(outFile);
                }catch(RarException e){
                    throw new RawException(e);
                }catch(IOException e){
                    throw new RawException(e);
                }finally {
                    IOKit.close(outputStream);
                }

            }
        } catch (IOException e) {
            throw new RawException(e);
        } catch (RarException e) {
            throw new RawException(e);
        } catch (Exception e) {
            throw new RawException(e);
        }



        return uniqueFiles(fileList);
    }

    private static List<File> uniqueFiles(List<File> files){
        List<String> fileNameList = files.stream().map(File::getAbsolutePath).collect(Collectors.toList());
        Set<String> fileNameSet = new HashSet<>();
        fileNameSet.addAll(fileNameList);

        return fileNameList.stream().map(File::new).collect(Collectors.toList());


    }

    /**
     * 根据文件清单列表创建压缩文件
     *
     * @param sourceFiles 文件清单列表
     * @param zipFile 压缩目标文件
     */
    public static void makeZip(List<File> sourceFiles,File zipFile){
//        throw new UnsupportedOperationException("文件打包压缩功能正在开发中...");
        FileOutputStream fos = null;
        ZipOutputStream zipOutputStream = null;
        try {
            FileKit.touchFile(zipFile,true);

            fos = new FileOutputStream(zipFile);
            zipOutputStream = new ZipOutputStream(fos);
            for(File file:sourceFiles){
                recursionZip(file, zipOutputStream, "");
            }
        }catch (Exception e){
            if(e instanceof RawException){
                throw (RawException)e;
            }else{
                throw new RawException(e);
            }
        } finally {
            IOKit.close(zipOutputStream);
            IOKit.close(fos);
        }
    }

    private static void recursionZip(File file,ZipOutputStream zipOut,String baseDir) throws IOException {
        logger.trace("MAKE ZIP FILE  <-- "+file.getAbsolutePath());
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File fileSec : files) {
                recursionZip(fileSec, zipOut, baseDir + file.getName() + File.separator);
            }
        } else {
            byte[] buf = new byte[4096];
            InputStream input = new FileInputStream(file);
            zipOut.putNextEntry(new ZipEntry(baseDir + file.getName()));
            int len;
            while ((len = input.read(buf)) != -1) {
                zipOut.write(buf, 0, len);
            }
            input.close();
        }
    }



    /**
     * 解压文件，自动根据文件名称辨别解压类型
     *
     * @param file file
     * @param destDir destDir
     * @param recursion recursion
     * @param charset charset
     * @return list
     */
    public static List<File> unpack(File file, File destDir,boolean recursion,Charset charset){
        String fileName = file.getName();
        if(fileName.indexOf(".")<=0){
            throw new RawException("文件没有扩展名，无法辨别其压缩文件类型");
        }
        String type = fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase();
        if(type.equals("zip")){
            return unzip(file,destDir,recursion,charset);
        }else if(type.equals("rar")){
            return unrar(file,destDir,recursion,charset);
        }else{
            throw new RawException("只支持zip和rar格式的压缩包！");
        }
    }
}
