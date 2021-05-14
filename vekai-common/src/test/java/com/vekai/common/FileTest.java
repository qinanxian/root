//package com.vekai.common;
//
//import com.vekai.common.fileman.entity.FileEntity;
//import com.vekai.common.fileman.entity.FileEntityHist;
//import com.vekai.common.fileman.service.FileManageService;
//import com.vekai.runtime.kit.IOKit;
//import org.junit.Assert;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//
//import java.io.*;
//import java.util.List;
//
//public class FileTest extends BaseTest{
//    @Autowired
//    @Qualifier(CommonConsts.DOSSIER_FILE_SERVICE_NAME)
//    protected FileManageService docFileManageService;
//
//    @Autowired
//    @Qualifier(CommonConsts.IMG_FILE_SERVICE_NAME)
//    protected FileManageService imgFileManageService;
//
//    @Test
//    public void testFileSaveDoc() throws IOException {
//        InputStream inputStream = this.getClass().getClassLoader().getResource("demo01.txt").openStream();
//        FileEntity fileEntity = new FileEntity();
//        fileEntity.setStoredContent("newTest.txt");
//        //FileEntity result = docFileManageService.saveFile(fileEntity, inputStream, true);
//        IOKit.close(inputStream);
//        //Assert.assertNotNull(result);
//        //Assert.assertEquals("demo05",result.getFileId());
//    }
//
//    @Test
//    public void testFileSaveDocCover() throws IOException {
//        InputStream inputStream = this.getClass().getClassLoader().getResource("demo01.txt").openStream();
//        FileEntity fileEntity = docFileManageService.getFileEntity("0186");
//        //FileEntity result = docFileManageService.saveFile(fileEntity, inputStream, true);
//        IOKit.close(inputStream);
//        //Assert.assertNotNull(result);
//        //Assert.assertEquals("demo05",result.getFileId());
//    }
//
//    @Test
//    public void testFileSaveImg() throws IOException {
//        InputStream inputStream = this.getClass().getClassLoader().getResource("军队分级.jpg").openStream();
//        FileEntity fileEntity = new FileEntity();
//        //fileEntity.setFileId("0022");
//        fileEntity.setStoredContent("test.jpg");
//        //FileEntity result = docFileManageService.saveFile(fileEntity, inputStream, true);
//        IOKit.close(inputStream);
//        //Assert.assertNotNull(result);
//    }
//
//    @Test
//    public void testHistSave() throws IOException {
//        InputStream inputStream = this.getClass().getClassLoader().getResource("demo01.txt").openStream();
//        FileEntity fileEntity = docFileManageService.getFileEntity("0064");
//        //docFileManageService.saveFile(fileEntity, inputStream, true);
//        IOKit.close(inputStream);
//    }
//
//    @Test
//    public void testHistSaveImg() throws IOException {
//        InputStream inputStream = this.getClass().getClassLoader().getResource("军队分级.jpg").openStream();
//        FileEntity fileEntity = docFileManageService.getFileEntity("0062");
//        //docFileManageService.saveFile(fileEntity, inputStream, true);
//        IOKit.close(inputStream);
//    }
//
//    @Test
//    public void testFileDelete() throws  IOException{
//        docFileManageService.deleteFileEntity("demo03");
//    }
//
//    @Test
//    public void testDeleteFileEntityHist(){
//        docFileManageService.deleteFileEntityHist("0838");
//    }
//
//    @Test
//    public void testGetFileEntity(){
//        FileEntity result = docFileManageService.getFileEntity("test1");
//        Assert.assertNotNull(result);
//        Assert.assertEquals("test1.jpg",result.getStoredContent());
//    }
//
//    @Test
//    public void testGetFileEntityHist(){
//        FileEntityHist result = docFileManageService.getFileEntityHist("0815");
//        Assert.assertNotNull(result);
//        Assert.assertEquals("demo02",result.getFileId());
//    }
//
//    @Test
//    public void testGetFileHistList(){
//        List<FileEntityHist> result = docFileManageService.getFileHistList("demo02");
//        Assert.assertNotNull(result);
//        Assert.assertEquals(2,result.size());
//        Assert.assertEquals("0816",result.get(1).getFileHistId());
//    }
//
//   /* @Test
//    public void testGetFile() throws IOException{
//        File file = docFileManageService.getFile("0011");
//        Reader reader = new InputStreamReader(new FileInputStream(file));
//        int tempchar;
//        while ((tempchar = reader.read()) != -1) {
//            if (((char) tempchar) != '\r') {
//                System.out.print((char) tempchar);
//            }
//        }
//        reader.close();
//    }
//
//    @Test
//    public void testGetHistFile() throws Exception{
//        File file = docFileManageService.getHistFile("0852");
//        Reader reader = new InputStreamReader(new FileInputStream(file));
//        int tempchar;
//        while ((tempchar = reader.read()) != -1) {
//            if (((char) tempchar) != '\r') {
//                System.out.print((char) tempchar);
//            }
//        }
//        reader.close();
//    }*/
//}
