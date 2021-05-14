package com.vekai.office.word;

import com.vekai.office.word.parameter.EmbedImage;
import com.vekai.office.word.parameter.EmbedTable;
import com.vekai.office.word.parameter.EmbedTableCell;
import com.vekai.office.word.parameter.EmbedTableRow;
import com.vekai.office.word.parameter.ImageParameter;
import com.vekai.office.word.parameter.MoneyParameter;
import com.vekai.office.word.parameter.NumberParameter;
import com.vekai.office.word.parameter.StringParameter;
import com.vekai.office.word.parameter.TableParameter;
import com.vekai.office.word.parameter.WordParameterSet;
import cn.fisok.raw.kit.IOKit;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;



public class WordReplaceTestCase {

    @Test
    public void testReplace() throws IOException {
        WordParameterSet parameter = new WordParameterSet();
        parameter.addParameter(new NumberParameter<Double>("RateOfReturn", 4.38));
        parameter.addParameter(new StringParameter("InComeAssignDate", "5月20日\r\n第二行\r\n第三行"));
        parameter.addParameter(new StringParameter("Remark", "本规定所称债权投资计划，\r\n是指保险资产管理公司\r\n等专业管理机构（以下简称专业管理机构）作为受托人，根据《管理办法》和本规定，面向委托人发行受益凭证，募集资金以债权方式投资基础设施项目，按照约定支付预期收益并兑付本金的金融产品"));
        parameter.addParameter(new StringParameter("FinaReportUnit", "万元"));
        parameter.addParameter(new StringParameter("FinaReportDate", "2014/07"));
        
        parameter.addParameter(new MoneyParameter("RItem11", 1000012.00));
        parameter.addParameter(new MoneyParameter("RItem12", 300.00));
        parameter.addParameter(new MoneyParameter("RItem81", 90025.00));
        parameter.addParameter(new MoneyParameter("RItem92", 20012.00));
        parameter.addParameter(new StringParameter("RItem21", "这是第一行\r\n这是第二行"));
        
        parameter.addParameter(new StringParameter("页眉变量", "测试001"));
        parameter.addParameter(new StringParameter("页脚变量", "测试002"));
        
//      initTableData(parameter);
        initTableData1(parameter);
        
        InputStream fis = null;
        OutputStream fos = null;
        try {
            //图片
            EmbedImage image = new EmbedImage();
            image.setWidth(400);
            image.setHeight(300);
            image.setPicType("gif");
            image.setData(IOKit.convertToBytes(this.getClass().getClassLoader().getResourceAsStream("com/amarsoft/rax/office/word/flying.gif"),true));
            parameter.addParameter(new ImageParameter("ImageData",image));
            
            fis = this.getClass().getClassLoader().getResourceAsStream("com/amarsoft/rax/office/word/WORD-RP-TPL01.docx");
            fos = new FileOutputStream("d:/tmp/TPL-R.docx");
            
            WordReplaceForPoi wordReplace = new WordReplaceForPoi(fis);
            wordReplace.replace(parameter);
            wordReplace.write(fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (WordReplaceException e) {
            e.printStackTrace();
        } finally{
            if(fis!=null){
                try {
                    fis.close();
                } catch (IOException e) {
                    System.err.println(e);
                }
            }
            if(fos!=null){
                try {
                    fos.close();
                } catch (IOException e) {
                    System.err.println(e);
                }
            }
        }
    }
    
    public void initTableData(WordParameterSet parameter){
        EmbedTable table = new EmbedTable();
        
        EmbedTableRow row = null;
        
        row = table.createRow();
        row.createCell().setText("第1行第1列");
        row.createCell().setText("第1行第2列");
        
        row = table.createRow();
        EmbedTableCell cell21 = row.createCell();
        EmbedTableCell cell22 = row.createCell();
        EmbedTableCell cell23 = row.createCell();
        EmbedTableCell cell24 = row.createCell();
        EmbedTableCell cell25 = row.createCell();
        cell21.setText("第2行第1列");
        cell22.setText("第2行第2列");
        cell23.setText("第2行第3列");
        cell24.setText("第2行第4列");
        cell25.setText("第2行第5列");
        
        row = table.createRow();
        row.createCell().setText("第3行第1列");
        EmbedTableCell cell32 = row.createCell();
        EmbedTableCell cell33= row.createCell();
        cell32.setText("第3行第2列");
        cell33.setText("第3行第3列");
        
        cell32.getStyle().setBgColor("000000");
        cell32.getStyle().setTextColor("FF0000");
        
        cell33.getStyle().setFontSize(18);
        cell33.getStyle().setBold(true);
        cell33.getStyle().setItalic(true);
        cell33.getStyle().setUnderline(EmbedTableCell.CellStyle.Underline.WORDS);
        
        //合并测试
        row = table.createRow();
        EmbedTableCell cell41 = row.createCell();
        EmbedTableCell cell42 = row.createCell();
        EmbedTableCell cell43 = row.createCell();
        EmbedTableCell cell44 = row.createCell();
        EmbedTableCell cell45 = row.createCell();
        
        row = table.createRow();
        EmbedTableCell cell51 = row.createCell();
        EmbedTableCell cell52 = row.createCell();
        EmbedTableCell cell53 = row.createCell();
        EmbedTableCell cell54 = row.createCell();
        EmbedTableCell cell55 = row.createCell();
        
        row = table.createRow();
        EmbedTableCell cell61 = row.createCell();
        EmbedTableCell cell62 = row.createCell();
        EmbedTableCell cell63 = row.createCell();
        EmbedTableCell cell64 = row.createCell();
        EmbedTableCell cell65 = row.createCell();
        
        row = table.createRow();
        EmbedTableCell cell71 = row.createCell();
        EmbedTableCell cell72 = row.createCell();
        EmbedTableCell cell73 = row.createCell();
        EmbedTableCell cell74 = row.createCell();
        EmbedTableCell cell75 = row.createCell();
        
        cell42.getStyle().mergeX(2);    //向右吃掉2个
        cell51.getStyle().mergeY(1);    //向下吃掉1个
        cell52.getStyle().mergeX(1);    //向右吃掉1个
        cell52.getStyle().mergeY(2);    //向下吃掉2个
        cell42.setText("向右吃掉2个");
        cell51.setText("向下吃掉1个");
        cell52.setText("向右吃掉1个,向下吃掉2个");
        
        cell63.getStyle().mergeRect(3,2);
        cell63.setText("合并出一个2行3列的矩形区域");
        
        
        parameter.addParameter(new TableParameter("TableData",table));
    }
    
    public void initTableData1(WordParameterSet parameter){
        EmbedTable table = new EmbedTable(7,5);
        
        
        table.getCell(0, 0).setText("第1行第1列");
        table.getCell(0, 1).setText("第1行第2列");
        
        table.getCell(1, 0).setText("第2行第1列");
        table.getCell(1, 1).setText("第2行第2列");
        table.getCell(1, 2).setText("第2行第3列");
        table.getCell(1, 3).setText("第2行第4列");
        table.getCell(1, 4).setText("第2行第5列");
        
        table.getCell(2, 0).setText("第3行第1列");
        table.getCell(2, 1).setText("第3行第2列");
        table.getCell(2, 2).setText("第3行第3列");
        
        table.getCell(2, 1).getStyle().setBgColor("000000");
        table.getCell(2, 1).getStyle().setTextColor("FF0000");
        
        table.getCell(2, 2).getStyle().setFontSize(18);
        table.getCell(2, 2).getStyle().setBold(true);
        table.getCell(2, 2).getStyle().setItalic(true);
        table.getCell(2, 2).getStyle().setUnderline(EmbedTableCell.CellStyle.Underline.WORDS);
        
        //合并测试
        
        table.getCell(3, 1).getStyle().mergeX(2);   //向右吃掉2个
        table.getCell(4, 0).getStyle().mergeY(1);   //向下吃掉1个
        table.getCell(4, 1).getStyle().mergeX(1);   //向右吃掉1个
        table.getCell(4, 1).getStyle().mergeY(2);   //向下吃掉2个
        table.getCell(3, 1).setText("向右吃掉2个");
        table.getCell(4, 0).setText("向下吃掉1个");
        table.getCell(4, 1).setText("向右吃掉1个,向下吃掉2个");
        
        table.getCell(5, 2).getStyle().mergeRect(3,2);
        table.getCell(5, 2).setText("合并出一个2行3列的矩形区域");
        
        table.getTableStyle().setWidth(0,2);        //设置第1列两英寸宽
        table.getTableStyle().setHeight(2,1.5); //第3行1.5英寸高
        
        parameter.addParameter(new TableParameter("TableData",table));
    }
}
