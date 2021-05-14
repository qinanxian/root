//package com.vekai.office.word;
//
//
//import com.aspose.words.*;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import java.io.*;
//
//public class WordTocTest {
//
//    @BeforeClass
//    public static void init() throws Exception{
//        InputStream license = WordReplaceByBookmarkTestCase.class.getClassLoader().getResourceAsStream("aspose-license.xml");    // license路径
//        License aposeLic = new License();
//        aposeLic.setLicense(license);
//    }
//
//    @Test
//    public void testGenerateTOC() throws Exception {
//        ClassLoader classLoader = this.getClass().getClassLoader();
//        InputStream inputStream = classLoader.getResourceAsStream("com/vekai/office/word/apache-bookmark-replace.docx");
//        Document document = new Document(new BufferedInputStream(inputStream));
//        DocumentBuilder builder = new DocumentBuilder(document);
//        builder.moveToBookmark("TOC");
//        //设置目录的格式
//        //“目录”两个字居中显示、加粗、搜宋体
//        builder.getCurrentParagraph().getParagraphFormat().setAlignment(ParagraphAlignment.CENTER);
//        builder.setBold(true);
//        builder.getFont().setName("宋体");
//        builder.writeln("目录");
//        //清清除所有样式设置
//        builder.getParagraphFormat().clearFormatting();
//        //目录居左
//        builder.getParagraphFormat().setAlignment(ParagraphAlignment.LEFT);
//        builder.insertTableOfContents("\\o \"1-3\" \\h \\z \\u");
//        document.updateFields();
//        OutputStream fos = new FileOutputStream("/Users/cytsir/Documents/tmp/gen-toc.docx");
//        document.save(fos, new OoxmlSaveOptions());
//    }
//
//    @Test
//    public void testUpdateTOCWhenContentChange() throws Exception{
//        ClassLoader classLoader = this.getClass().getClassLoader();
//        InputStream inputStream = new BufferedInputStream(new FileInputStream("D:/test/apache-bookmark-replace.docx"));
//        Document document = new Document(inputStream);
//        DocumentBuilder builder = new DocumentBuilder(document);
//        Bookmark bookmark1 = document.getRange().getBookmarks().get("RateOfReturn");
//        bookmark1.setText("RateOfReturn-version2:BigString-" +
//                "ajipsf]aipja]dfjidajifjaijfaij11111111111111111111111111111111111111111111111111111111111111111111" +
//                "11--------------------------------------\r\n111\r\n222\r\n" +
//                "222\r\n111\r\n111\r\n111\r\n111\r\n111\r\n111\r\n\r\n\r\n222\r\n111\r\n111\r\n111\r\n");
//        document.updateFields();
//        OutputStream fos = new FileOutputStream("D:/test/apache-bookmark-replace.docx");
//        document.save(fos, new OoxmlSaveOptions());
//    }
//}
