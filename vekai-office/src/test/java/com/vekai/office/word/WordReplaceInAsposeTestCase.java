//package com.vekai.office.word;
//
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import com.aspose.words.Bookmark;
//import com.aspose.words.Cell;
//import com.aspose.words.Document;
//import com.aspose.words.DocumentBuilder;
//import com.aspose.words.License;
//import com.aspose.words.Node;
//import com.aspose.words.OoxmlSaveOptions;
//import com.aspose.words.Paragraph;
//import com.aspose.words.PdfSaveOptions;
//import com.aspose.words.Row;
//import com.aspose.words.Run;
//import com.aspose.words.Table;
//
//public class WordReplaceInAsposeTestCase {
//
//    @BeforeClass
//    public static void init() throws Exception{
//        InputStream license = WordReplaceInAsposeTestCase.class.getClassLoader().getResourceAsStream("license.xml");    // license路径
//        License aposeLic = new License();
//        aposeLic.setLicense(license);
//    }
//
//    @Test
//    public void testBookmark() throws Exception{
//        InputStream inputStream = new FileInputStream("showcase/docs/apache-bookmark-replace.docx");
//        Document document = new Document(inputStream);
//        DocumentBuilder builder = new DocumentBuilder(document);
//
//        //表格替换部分，表格部分，要做一个
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
//            }
//            table.getRows().add(newRow);
//        }
//
////        builder.insertHtml("<table style='border:1px solid #F00;'><tr><td>1</td><td>2</td><td>3</td></tr><tr><td>1</td><td>2</td><td>3</td></tr></table>");
//        builder.moveToBookmark("FinaReportDate");
//        builder.write("2017/04/18");
//        builder.moveToBookmark("FinaReportUnit");
//        builder.write("万元");
//        builder.moveToBookmark("PageFooter");
//        builder.write("文档页脚");
//        builder.moveToBookmark("Remark");
//        builder.insertHtml("<span style='border:1px solid #F00;'>这是一段备注文字，测试一下，它应该是红色的</span>");
//
//        OutputStream fos = new FileOutputStream("showcase/docs/apache-bookmark-replace-result.docx");
//        document.save(fos, new OoxmlSaveOptions());
//
//        OutputStream fos1 = new FileOutputStream("showcase/docs/apache-bookmark-replace-result.pdf");
//        PdfSaveOptions pdfOptions = new PdfSaveOptions();
//        document.save(fos1, pdfOptions);
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
//}
