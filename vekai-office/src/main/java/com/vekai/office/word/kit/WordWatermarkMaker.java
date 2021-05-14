//package com.vekai.office.word.kit;
//
//import java.awt.Color;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//import com.aspose.words.Document;
//import com.aspose.words.HeaderFooter;
//import com.aspose.words.HeaderFooterType;
//import com.aspose.words.HorizontalAlignment;
//import com.aspose.words.OoxmlSaveOptions;
//import com.aspose.words.Paragraph;
//import com.aspose.words.RelativeHorizontalPosition;
//import com.aspose.words.RelativeVerticalPosition;
//import com.aspose.words.SaveOptions;
//import com.aspose.words.Section;
//import com.aspose.words.Shape;
//import com.aspose.words.ShapeType;
//import com.aspose.words.VerticalAlignment;
//import com.aspose.words.WrapType;
//
///**
// * 水印制做类，项目可以根据自己的需要调整相关参数
// * @author 杨松<syang@amarsoft.com>
// * @date 2017年4月5日
// */
//public class WordWatermarkMaker {
//    private OutputStream outputStream;
//    private Document document;
//    private List<Shape> watermarks;
//
//    public WordWatermarkMaker(InputStream inputStream, OutputStream outputStream) throws Exception {
//        WordKit.loadLicense();
//
//        this.document = new Document(inputStream);
//        this.outputStream = outputStream;
//        this.watermarks = new ArrayList<Shape>();
//    }
//
//    /**
//     * 添加文字水印
//     * @param text
//     * @return
//     * @throws Exception
//     */
//    public WordWatermarkMaker appendTextWatermark(String text) throws Exception{
//        Shape watermark = new Shape(document,ShapeType.TEXT_PLAIN_TEXT);
//        watermark.getTextPath().setText(text);
//
//        watermark.setWidth(500);
//        watermark.setHeight(100);
//        watermark.getTextPath().setFontFamily("Arial");
//        watermark.getFill().setColor(Color.GRAY);
//
//        watermark.setRotation(-60); // Text will be directed from the bottom-left to the top-right corner.
//        watermark.getFill().setColor(Color.GRAY); // Try LightGray to get more Word-style watermark
//        watermark.setStrokeColor(Color.GRAY); // Try LightGray to get more Word-style watermark
//
//        // Place the watermark in the page center.
//        watermark.setRelativeHorizontalPosition(RelativeHorizontalPosition.PAGE);
//        watermark.setRelativeVerticalPosition(RelativeVerticalPosition.PAGE);
//        watermark.setWrapType(WrapType.NONE);
//        watermark.setVerticalAlignment(VerticalAlignment.CENTER);
//        watermark.setHorizontalAlignment(HorizontalAlignment.CENTER);
//
//        watermarks.add(watermark);
//
//        return this;
//    }
//    /**
//     * 添加图片水印
//     * @param stream
//     * @return
//     * @throws Exception
//     */
//    public WordWatermarkMaker appendImageWatermark(InputStream stream) throws Exception{
//        Shape watermark = new Shape(document,ShapeType.IMAGE);
//        watermark.getImageData().setImage(stream);
//
//        watermark.setWidth(100);
//        watermark.setHeight(100);
//        watermark.getTextPath().setFontFamily("Arial");
//        watermark.getFill().setColor(Color.GRAY);
//
//        watermark.getFill().setColor(Color.GRAY); // Try LightGray to get more Word-style watermark
//        watermark.setStrokeColor(Color.GRAY); // Try LightGray to get more Word-style watermark
//
//        // Place the watermark in the page center.
//        watermark.setWrapType(WrapType.NONE);
//        watermark.setVerticalAlignment(VerticalAlignment.BOTTOM);
//        watermark.setHorizontalAlignment(HorizontalAlignment.RIGHT);
////        watermark.setRelativeHorizontalPosition(RelativeHorizontalPosition.PAGE);
//        watermark.setRelativeVerticalPosition(RelativeVerticalPosition.PAGE);
//
//        watermarks.add(watermark);
//
//        return this;
//    }
//
//    private static void insertWatermarkIntoHeader(Paragraph watermarkPara, Section sect, int headerType)
//            throws Exception {
//        HeaderFooter header = sect.getHeadersFooters().getByHeaderFooterType(headerType);
//
//        if (header == null) {
//            // There is no header of the specified type in the current section,
//            // create it.
//            header = new HeaderFooter(sect.getDocument(), headerType);
//            sect.getHeadersFooters().add(header);
//        }
//
//        // Insert a clone of the watermark into the header.
//        header.appendChild(watermarkPara.deepClone(true));
//    }
//
//    public void make(int startSection) throws Exception{
//        // Create a new paragraph and append the watermark to this paragraph.
//        Paragraph watermarkPara = new Paragraph(document);
//        for(Shape watermark : watermarks){
//            watermarkPara.appendChild(watermark);
//        }
//
//        for(int i=startSection;i<document.getSections().getCount();i++){
//            Section sect = document.getSections().get(i);
//            insertWatermarkIntoHeader(watermarkPara, sect, HeaderFooterType.HEADER_PRIMARY);
////            insertWatermarkIntoHeader(watermarkPara, sect, HeaderFooterType.HEADER_FIRST);
////            insertWatermarkIntoHeader(watermarkPara, sect, HeaderFooterType.HEADER_EVEN);
//        }
//
//        SaveOptions saveOptions = new OoxmlSaveOptions();
////        SaveOptions saveOptions = new com.aspose.words.PdfSaveOptions();
//        document.save(outputStream, saveOptions);
//    }
//
//}
