//package com.vekai.office.word;
//
//import java.awt.*;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.regex.Pattern;
//
//import com.aspose.words.*;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//public class WordReplaceByBookmarkTestCase {
//
//    @BeforeClass
//    public static void init() throws Exception{
//        InputStream license = WordReplaceByBookmarkTestCase.class.getClassLoader().getResourceAsStream("aspose-license.xml");    // license路径
//        License aposeLic = new License();
//        aposeLic.setLicense(license);
//    }
//
//    @Test
//    public void testTable() throws Exception {
//        ClassLoader classLoader = this.getClass().getClassLoader();
////        InputStream inputStream = classLoader.getResourceAsStream("com/amarsoft/rax/office/word/apache-bookmark-replace.docx");
////        InputStream inputStream = new FileInputStream(CommonUtils.getWebInfoPath()+"showcase/docs/A-01-数据移植方案.docx");
//        InputStream inputStream = classLoader.getResourceAsStream("com/vekai/office/word/apache-bookmark-replace.docx");
//        Document document = new Document(inputStream);
//
//        DocumentBuilder builder = new DocumentBuilder(document);
//        //Set table formating
//        //Set borders
//        builder.getCellFormat().getBorders().setLineStyle(LineStyle.SINGLE);
//        builder.getCellFormat().getBorders().setColor(Color.LIGHT_GRAY);
//
//        // etc...
//        //Move documentBuilder cursor to the bookmark
//        builder.moveToBookmark("TableData");
//        //builder.startTable();
//        //Insert some table
//        for (int i = 0; i < 5; i++)
//        {
//            for (int j = 0; j < 5; j++)
//            {
//                builder.insertCell();
//                builder.write("this is cell");
//            }
//            builder.endRow();
//        }
//        builder.endTable();
//        OutputStream fos = new FileOutputStream("D:/test/apache-bookmark-replace.docx");
//        document.save(fos, new OoxmlSaveOptions());
//    }
//
//    @Test
//    public void testBookmark() throws Exception{
//        ClassLoader classLoader = this.getClass().getClassLoader();
////        InputStream inputStream = classLoader.getResourceAsStream("com/amarsoft/rax/office/word/apache-bookmark-replace.docx");
////        InputStream inputStream = new FileInputStream(CommonUtils.getWebInfoPath()+"showcase/docs/A-01-数据移植方案.docx");
//        InputStream inputStream = classLoader.getResourceAsStream("com/vekai/office/word/apache-bookmark-replace.docx");
//        Document document = new Document(inputStream);
//
//        DocumentBuilder builder = new DocumentBuilder(document);
////        builder.moveToBookmark("FinaReportDate");
////        deleteRowByBookmark(document,"FinaReportDate");
////        builder.write("2017/04/18");
////        builder.write("AAAAAAAAAAAAAAA");
//
////        builder.startBookmark("testbk1");
////        System.out.println("XXX:["+builder.getCurrentNode().getRange().getText()+"]");
////        builder.endBookmark("testbk1");
//
//        //--------- test update content of bookmark
//        Bookmark bookmark1 = document.getRange().getBookmarks().get("RateOfReturn");
//        bookmark1.setText("RateOfReturn-version2");
//
//
//
//
//        Bookmark bookmark = document.getRange().getBookmarks().get("mytable");
//        Cell cell = (Cell)bookmark.getBookmarkStart().getParentNode().getParentNode();
//        Table table = (Table)cell.getParentNode().getParentNode();
//        int rows = table.getRows().getCount();
//        Row tplRow = table.getRows().get(1);
//        List<Row> removeRows = new ArrayList<Row>();
//        for(int i=1;i<rows;i++){
//            removeRows.add(table.getRows().get(i));
//        }
//        for(Row row : removeRows){
//            table.getRows().remove(row);
//        }
//
//        for(int i=0;i<tplRow.getCount();i++){
//            Row newRow = (Row)tplRow.deepClone(true);
//            for(int j=0;j<tplRow.getCount();j++){
//                Paragraph e = newRow.getCells().get(j).getFirstParagraph();
//                Run r = cleanOneRun(e);
//                r.setText(i+"-"+j);
////                System.out.println(i+"-"+j+"="+e.getChildNodes().get(1));
////                if(e.getFirstChild() instanceof Run){
////                    ((Run)e.getFirstChild()).setText(i+"-"+j);
////                }
////                e.getRuns().get(0).setText(i+"-"+j);
////                builder.moveTo(e);
////                builder.write();
//            }
//            table.getRows().add(newRow);
//        }
////        System.out.println("text:"+);
////        bookmark.setText("2017/04/20");
//
////        builder.getCellFormat().getBorders().setLineStyle(LineStyle.SINGLE);
////        builder.getCellFormat().getBorders().setColor(Color.RED);
////        builder.getRowFormat().setLeftIndent(100);
////        builder.moveToBookmark(bookmark1.getName());
//
//        //================================
////        builder.startTable();
////        builder.insertCell();
////        Bookmark bookmark1 = document.getRange().getBookmarks().get("Remark");
////        builder.startBookmark(bookmark1.getName());
////        builder.write("AAAAAAAAAAAA");
////        builder.endRow();
////
////        for(int i=0;i<6;i++){
////            for(int j=0;j<4;j++){
////                builder.insertCell();
////                builder.write(i+"-"+j);
////            }
////            builder.endRow();
////        }
////
////        builder.endTable();
////        builder.endBookmark(bookmark1.getName());
//        //================================
//
////        builder.insertHtml("<table style='border:1px solid #F00;'><tr><td>1</td><td>2</td><td>3</td></tr><tr><td>1</td><td>2</td><td>3</td></tr></table>");
//
//
//
//        OutputStream fos = new FileOutputStream("D:/test/apache-bookmark-replace.docx");
//        document.save(fos, new OoxmlSaveOptions());
////        OutputStream fos = new FileOutputStream(CommonUtils.getWebInfoPath()+"showcase/docs/apache-bookmark-replace.html");
////        HtmlSaveOptions options = new HtmlSaveOptions(SaveFormat.HTML);
////        options.setExportTextInputFormFieldAsText(true);
////        options.setExportImagesAsBase64(true);
////        options.setExportTextBoxAsSvg(true);
////        options.setExportPageMargins(true);
////        options.setExportPageSetup(true);
////        options.setExportTocPageNumbers(true);
////        options.setPrettyFormat(true);
////        options.setUseHighQualityRendering(true);
////        options.setExportOriginalUrlForLinkedImages(true);
////        options.setAllowNegativeIndent(true);
////        options.setExportTocPageNumbers(true);
////        options.setScaleImageToShapeSize(true);
////        document.save(fos, options);
//    }
//
//    private Run cleanOneRun(Paragraph p){
//        Run r = null;
//        if(p == null)return r;
//        int count = p.getChildNodes().getCount();
//        int i=0;
//        for(;i<count;i++){
//            Node node = p.getChildNodes().get(i);
//            if(node instanceof Run){
//                r = (Run)node;
//                break;
//            }
//        }
//        //删除其他run
//        List<Node> deleteNodes = new ArrayList<Node>();
//        for(int j=i+1;j<count;j++){
//            Node node = p.getChildNodes().get(j);
//            if(node != null){
//                deleteNodes.add(node);
//            }
//        }
//        for(Node node : deleteNodes){
//            p.getChildNodes().remove(node);
//        }
//        return r;
//    }
//
//    private void deleteRowByBookmark(Document document,String bookmarkName) throws Exception{
//        Bookmark bookmark = document.getRange().getBookmarks().get(bookmarkName);
//        if(bookmark == null)return ;
//        Row rowStart = (Row)bookmark.getBookmarkStart().getAncestor(Row.class);
//        Row rowEnd = (Row)bookmark.getBookmarkEnd().getAncestor(Row.class);
//        if(rowStart == null)return;
//        rowStart.removeAllChildren();
//        rowStart.remove();
//    }
//
//
//
//}
