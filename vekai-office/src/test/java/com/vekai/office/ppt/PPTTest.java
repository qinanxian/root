package com.vekai.office.ppt;

import org.apache.poi.xslf.usermodel.SlideLayout;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTable;
import org.apache.poi.xslf.usermodel.XSLFTableCell;
import org.apache.poi.xslf.usermodel.XSLFTableRow;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhulifeng on 17-12-20.
 */
public class PPTTest {

    @Test
    public void testReplace() throws IOException {
        File file = new File("src/test/resources/ppt/before_replace.pptx");
        XMLSlideShow ppt = new XMLSlideShow(new FileInputStream(file));

        Map<String, Object> map = createMap();

        List<XSLFSlide> slides = ppt.getSlides();//获取每一页的幻灯片
        for (XSLFSlide slide : slides) {
            SlideLayout slideLayout = slide.getSlideLayout().getType();
            System.out.println("Current layout = " + slideLayout);
            List<XSLFShape> shapes = slide.getShapes();//获取幻灯片中的形状
            for (XSLFShape shape : shapes) {
                if (shape instanceof XSLFTextShape) {//文本区域
                    XSLFTextShape title = (XSLFTextShape) shape;
                    String text = title.getText();
                    System.out.println("title = " + text);
                    title.setText(replace(text, map));
                }

                if (shape instanceof XSLFTable) {//表格区域
                    XSLFTable table = (XSLFTable) shape;
                    List<XSLFTableRow> rows = table.getRows();
                    for (XSLFTableRow row : rows) {
                        List<XSLFTableCell> cells = row.getCells();
                        for (XSLFTableCell cell : cells) {
                            String text = cell.getText();
                            System.out.println("cellText = " + text);
                            cell.setText(replace(text, map));
                        }
                    }
                }
            }
        }

        FileOutputStream out =
            new FileOutputStream(new File("src/test/resources/ppt/after_replace.pptx"));
        ppt.write(out);
        out.close();
    }

    private Map<String, Object> createMap() {
        Map<String, Object> map = new HashMap();
        map.put("companyName", "安硕");
        map.put("name", "Tom");
        map.put("age", 20);
        map.put("mobileNo", "13812345678");
        map.put("email", "tom@gmail.com");
        map.put("address", "江苏省苏州市虎丘区");

        return map;
    }


    private String replace(String text, Map<String, Object> map) {
        String regex = "\\$\\s*\\{(.*?)\\}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            matcher.appendReplacement(sb, String.valueOf(map.get(matcher.group(1).trim())));
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

}
