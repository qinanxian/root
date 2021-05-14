//package com.vekai.common;
//
//import com.vekai.common.fileman.entity.FileEntity;
//import com.vekai.common.fileman.service.FileManageService;
//import com.vekai.runtime.kit.IOKit;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//
//import java.io.IOException;
//import java.io.InputStream;
//
//public class FileManageServiceTest extends BaseTest {
//
//    @Autowired
//    @Qualifier(CommonConsts.IMG_FILE_SERVICE_NAME)
//    protected FileManageService imgFileManageService;
//
//
//    @Test
//    public void testFileSave() throws IOException {
//        InputStream inputStream = this.getClass().getClassLoader().getResource("military-ranks.jpg").openStream();
//        FileEntity fileEntity = new FileEntity();
//        fileEntity.setFileId("jtc01");
//        fileEntity.setStoredContent("jtc01.jpg");
//        //imgFileManageService.saveFile(fileEntity,inputStream,false);
//        IOKit.close(inputStream);
//        imgFileManageService.deleteFileEntity("jtc01");
//    }
//}
