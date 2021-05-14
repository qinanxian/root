package com.vekai.office.word.kit;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;

/**
 * 
 * @author 杨松<syang@amarsoft.com>
 * @date 2017年3月14日
 */
public class WordWaterMaskTestCase {

////    @Test
//    public void test01() throws IOException, XmlException{
//        String baseDir = "D:/develop/ws-set/FLS-CBS/FLS-CBS-EA/WebContent/WEB-INF/showcase/docs";
//        InputStream inputStream = new FileInputStream(baseDir+"/CASE-D51.docx");
//        OutputStream outputStream = new FileOutputStream(baseDir+"/CASE-D51-water.docx");
//
//        XWPFDocument document = new WordReplaceXWPFDocument(inputStream);
//
//        CTSectPr sectPr = document.getDocument().getBody().addNewSectPr();
//        XWPFHeaderFooterPolicy headerFooterPolicy = new XWPFHeaderFooterPolicy(document,sectPr);
//        headerFooterPolicy.createWatermark("上海安硕");
////        document.getDocument().addNewBackground();
////        background.selectAttribute("K1", "测试值1");
//
//
//        document.write(outputStream);
//    }
    
//    @Test
//    public void test03() throws Exception{
//        String baseDir = "D:/develop/ws-set/FLS-CBS/FLS-CBS-EA/WebContent/WEB-INF/showcase/docs";
//        InputStream inputStream = new FileInputStream(baseDir+"/CASE-D51.docx");
////        OutputStream outputStream = new FileOutputStream(baseDir+"/CASE-D51-water.docx");
//        OutputStream outputStream = new FileOutputStream(baseDir+"/CASE-D51-water.pdf");
//
//
//        WordWatermarkMaker maker = new WordWatermarkMaker(inputStream,outputStream);
//        maker.appendTextWatermark("上海安硕RAX-1.0");
////        maker.appendImageWatermark(this.getClass().getClassLoader().getResourceAsStream("com/amarsoft/rax/office/word/amarsoft-seal-demo.png"));
//        maker.appendImageWatermark(this.getClass().getClassLoader().getResourceAsStream("com/vekai/showcase/docs/amarsoft-seal-demo.png"));
//
//        maker.make(0);
//    }
}
