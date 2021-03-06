package com.vekai.office.word;

import com.vekai.office.word.parameter.*;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


/**
 * WordReplace，实现对Word模板进行替换功能
 *
 * @author yangsong
 * @since 2014年7月30日
 */
public class WordReplaceNew {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private InputStream inputStream = null;
    private WordReplaceXWPFDocument document;
    private int defaultReplaceFontSize = 12;

    /**
     * 构建一个WordReplace对象，用于使用一个特定的模板进行替换操作
     * 
     * @param inputStream
     *            模板对象输入流
     */
    public WordReplaceNew(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    /**
     * 执行替换
     * 
     * @param parameter
     * @throws WordReplaceException
     */
    public void replace(WordParameterSet parameter) throws WordReplaceException {
        createAndReplaceDocument(parameter);
    }

    public int getDefaultReplaceFontSize() {
        return defaultReplaceFontSize;
    }

    public void setDefaultReplaceFontSize(int defaultReplaceFontSize) {
        this.defaultReplaceFontSize = defaultReplaceFontSize;
    }

    /**
     * 对Document对象进行替换操作
     * 
     * @param parameter
     * @return
     */
    protected void createAndReplaceDocument(WordParameterSet parameter) throws WordReplaceException {
        try {
            document = new WordReplaceXWPFDocument(inputStream);
            if (parameter != null && parameter.size() > 0) {

                // 处理页眉页脚
                replaceHeaderAndFotter(parameter);
                // 处理段落
                List<XWPFParagraph> paragraphList = document.getParagraphs();
                processParagraphs(paragraphList, parameter, document);

                // 处理表格
                List<XWPFTable> tableList = document.getTables();
                for (int i = 0; i < tableList.size(); i++) {
                    XWPFTable table = tableList.get(i);
                    List<XWPFTableRow> rows = table.getRows();
                    for (int m = 0; m < rows.size(); m++) {
                        XWPFTableRow row = rows.get(m);
                        List<XWPFTableCell> cells = row.getTableCells();
                        for (int j = 0; j < cells.size(); j++) {
                            XWPFTableCell cell = cells.get(j);
                            List<XWPFParagraph> paragraphListTable = cell.getParagraphs();
                            processParagraphs(paragraphListTable, parameter, document);
                        }
                    }
                }

                // 表格替换
                processTableReplace(parameter);
            }
        } catch (IOException e) {
            throw new WordReplaceException(e);
        }
        // processParagraph();
    }

    private void replaceHeaderAndFotter(WordParameterSet parameter) throws WordReplaceException {
        List<XWPFHeader> headers = document.getHeaderList();
        List<XWPFFooter> fotters = document.getFooterList();
        for (XWPFHeader header : headers) {
            processParagraphs(header.getParagraphs(), parameter, document);
        }
        for (XWPFFooter footer : fotters) {
            processParagraphs(footer.getParagraphs(), parameter, document);
        }
    }

    // protected void processParagraph(){
    // try {
    // FileOutputStream fos = new FileOutputStream("C:/aaa.docx");
    // document.write(fos);
    // fos.close();
    // FileInputStream fis = new FileInputStream("C:/aaa.docx");
    // HWPFDocument doc = new HWPFDocument(fis);
    //
    // Range range = doc.getRange();
    // range.replaceText("<p>", "\r");
    // fos = new FileOutputStream("C:/aaa.docx");
    // doc.write(fos);
    // fis.close();
    // fos.close();
    // document = new WordReplaceXWPFDocument(new
    // FileInputStream("C:/aaa.docx"));
    // } catch (FileNotFoundException e) {
    // e.printStackTrace();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }

    /**
     * 处理表格替换<表格替换，非表格内部替换，是整个表格替换功能>
     */
    private void processTableReplace(WordParameterSet parameter) {
        replaceTable(document, parameter);
    }

    private void replaceTable(WordReplaceXWPFDocument doc, WordParameterSet parameter) {
        List<String> names = parameter.getAllNames();
        for (int n = 0; n < names.size(); n++) {
            String name = names.get(n);

            for (int i = 0; i < doc.getTables().size(); i++) {
                XWPFTable table = doc.getTables().get(i);
                String firstCellText = getFirstCellValue(table);
                if (firstCellText == null)
                    firstCellText = "";
                firstCellText = firstCellText.replaceAll("\\s+", "");

                if (firstCellText.indexOf(name) >= 0) {
                    WordParameter<?> value = parameter.getParameter(name);
                    if (value instanceof TableParameter) {
                        fillTable(table, ((TableParameter) value).getValue());
                    }
                }
            }
        }
        // XWPFTable table = doc.getTables().get(1);
        // XWPFTableCell firstCell = table.getRow(0).getCell(0);
        // System.out.println("第一个单元格:"+firstCell.getText());
        //
        // for(int i=0;i<table.getRows().size();i++){
        // table.removeRow(i);
        // }
        // XWPFTableRow row = table.createRow();
        // row.createCell().setText("第1行第1列");
        // row.createCell().setText("第1行第2列");
        // row.createCell().setText("第1行第3列");
        // row.createCell().setText("第1行第4列");
        // XWPFTableRow row1 = table.createRow();
        // row1.getCell(0).setText("第2行第1列");
        // row1.getCell(1).setText("第2行第2列");
        // row1.getCell(2).setText("第2行第3列");
    }

    private void fillTable(XWPFTable dscTable, EmbedTable srcTable) {
        // 先清除标签所在表中的所有数据
        for (int i = 0; i < dscTable.getRows().size(); i++) {
            dscTable.removeRow(i);
        }
        EmbedTable.TableStyle tableStyle = srcTable.getTableStyle();
        if (srcTable.getTableStyle().getWidthType() == EmbedTable.TableStyle.WidthType.DXA) {
            dscTable.getCTTbl().getTblPr().getTblW().setType(STTblWidth.DXA);
            dscTable.getCTTbl().getTblPr().getTblW().setW(BigInteger.valueOf((long) (360 * 4 * tableStyle.getWidth())));
        } else {
            dscTable.getCTTbl().getTblPr().getTblW().setType(STTblWidth.AUTO);
        }
        // 开始填充表
        for (int i = 0; i < srcTable.getRows().size(); i++) {
            EmbedTableRow srcRow = srcTable.getRows().get(i);
            XWPFTableRow dscRow = dscTable.createRow();
            // 设置行高
            if (tableStyle.getHeight(i) != null) {
                CTTrPr trPr = dscRow.getCtRow().addNewTrPr();
                CTHeight ht = trPr.addNewTrHeight();
                ht.setVal(BigInteger.valueOf((long) (360 * 4 * tableStyle.getHeight(i))));
            }

            for (int j = 0; j < srcRow.getCells().size(); j++) {
                EmbedTableCell srcCell = srcRow.getCells().get(j);
                XWPFTableCell dscCell = null;

                // 如果单元格不存在，则创建
                if (j > dscRow.getTableCells().size() - 1)
                    dscCell = dscRow.createCell();
                else
                    dscCell = dscRow.getCell(j);
                // 设置宽度
                if (tableStyle.getWidth(j) != null) {
                    CTTcPr tcpr = dscCell.getCTTc().addNewTcPr();
                    CTTblWidth wd = tcpr.addNewTcW();
                    wd.setW(BigInteger.valueOf((long) (360 * 4 * tableStyle.getWidth(j))));
                }
                // dscCell.setText(srcCell.getText());
                fillCell(srcCell, dscCell, srcCell.getText());
            }
        }
    }

    private void fillCell(EmbedTableCell srcCell, XWPFTableCell dscCell, String text) {
        CTTcPr tcpr = dscCell.getCTTc().addNewTcPr();
        CTVerticalJc va = tcpr.addNewVAlign();
        CTShd ctshd = tcpr.addNewShd();
        ctshd.setColor("auto");
        ctshd.setVal(STShd.CLEAR);

        EmbedTableCell.CellStyle style = srcCell.getStyle();
        
        //表格内的换行符无效问题，对换行符进行拆分，对内容设置独立段落实现换行
        String[] subContent = (null == text ? "" : text).split("\r\n");
        int count = 0;
        for (String str : subContent) {
            count++;
            XWPFParagraph paragraph = (count == 1) ? dscCell.getParagraphs().get(0) : dscCell.addParagraph();
            XWPFRun run = paragraph.createRun();
            run.setBold(style.isBold());
            run.setItalic(style.isItalic());
            if (style.getTextPosition() > 0)
                run.setTextPosition(run.getTextPosition());
            if (style.getFontFamily() != null)
                run.setFontFamily(style.getFontFamily());
            if (style.getUnderline() != null)
                run.setUnderline(UnderlinePatterns.valueOf(style.getUnderline().name()));
            if (style.getTextColor() != null)
                ctshd.setFill(style.getBgColor());
            if (style.getBgColor() != null)
                run.setColor(style.getTextColor());
            if (style.getFontSize() > 0)
                run.setFontSize(style.getFontSize());
            run.setText(null == str ? "" : str);

            if (style.getAlign() == EmbedTableCell.CellStyle.HAlign.LEFT)
                paragraph.setAlignment(ParagraphAlignment.LEFT);
            else if (style.getAlign() == EmbedTableCell.CellStyle.HAlign.CENTER)
                paragraph.setAlignment(ParagraphAlignment.CENTER);
            else if (style.getAlign() == EmbedTableCell.CellStyle.HAlign.RIGHT)
                paragraph.setAlignment(ParagraphAlignment.RIGHT);
            else if (style.getAlign() == EmbedTableCell.CellStyle.HAlign.BOTH)
                paragraph.setAlignment(ParagraphAlignment.BOTH);
            else
                paragraph.setAlignment(ParagraphAlignment.LEFT);
        }
        // run.setText(text==null?"":text);

        if (style.getValign() == EmbedTableCell.CellStyle.VAlign.TOP)
            va.setVal(STVerticalJc.TOP);
        else if (style.getValign() == EmbedTableCell.CellStyle.VAlign.CENTER)
            va.setVal(STVerticalJc.CENTER);
        else if (style.getValign() == EmbedTableCell.CellStyle.VAlign.BOTTOM)
            va.setVal(STVerticalJc.BOTTOM);
        else if (style.getValign() == EmbedTableCell.CellStyle.VAlign.BOTH)
            va.setVal(STVerticalJc.BOTH);
        else
            va.setVal(STVerticalJc.CENTER);

        // 水平合并
        if (srcCell.getStyle().getVMerge() == EmbedTableCell.CellStyle.VMerge.START) {
            CTVMerge merge = tcpr.addNewVMerge();
            merge.setVal(STMerge.RESTART);
        }
        if (srcCell.getStyle().getVMerge() == EmbedTableCell.CellStyle.VMerge.CONTINUE) {
            CTVMerge merge = tcpr.addNewVMerge();
            merge.setVal(STMerge.CONTINUE);
        }

        // 垂直合并
        if (srcCell.getStyle().getHMerge() == EmbedTableCell.CellStyle.HMerge.START) {
            CTHMerge merge = tcpr.addNewHMerge();
            merge.setVal(STMerge.RESTART);
        }
        if (srcCell.getStyle().getHMerge() == EmbedTableCell.CellStyle.HMerge.CONTINUE) {
            CTHMerge merge = tcpr.addNewHMerge();
            merge.setVal(STMerge.CONTINUE);
        }

    }

    /**
     * 获取表格第一行第一列单元格的值
     * 
     * @param table
     * @return
     */
    private String getFirstCellValue(XWPFTable table) {
        String textValue = null;
        if (table == null || table.getRows().size() < 1)
            return textValue;
        XWPFTableRow row = table.getRow(0);
        if (row == null || row.getTableCells().size() < 1)
            return textValue;
        XWPFTableCell cell = row.getCell(0);
        textValue = cell.getText();
        return textValue;
    }

    /**
     * 处理段落
     * 
     * @param paragraphList
     */
    public void processParagraphs(List<XWPFParagraph> paragraphList, WordParameterSet parameter,
            WordReplaceXWPFDocument doc) throws WordReplaceException {
        if (paragraphList != null && paragraphList.size() > 0) {
            for (int i = 0; i < paragraphList.size(); i++) {
                XWPFParagraph paragraph = paragraphList.get(i);
                String paragraphText = paragraph.getParagraphText();
                List<String> keyNames = parameter.getAllNames();
                for (int j = 0; j < keyNames.size(); j++) {
                    String key = keyNames.get(j);
                    WordParameter<?> value = parameter.getParameter(key);
                    if (paragraphText.indexOf(key) != -1) {
                        // System.out.println("KV-->"+key+"="+value+",paragraph:"+paragraphText);
                        paragraphReplace(doc, paragraph, key, value);
                    }
                }
            }
        }
    }

    /**
     * 段落替换功能
     * 
     * @param paragraph
     * @param key
     * @param value
     */
    private void paragraphReplace(WordReplaceXWPFDocument doc, XWPFParagraph paragraph, String key,
            WordParameter<?> value) throws WordReplaceException {
        if (paragraph.isEmpty())
            return;
        List<XWPFRun> runList = paragraph.getRuns();
        XWPFRun firstRun = null;
        StringBuffer sbTmp = new StringBuffer();
        List<Integer> rmvIdx = new ArrayList<Integer>();
        boolean doReplace = false;
        for (int i = 0; i < runList.size(); i++) {
            XWPFRun run = runList.get(i);
            String runText = run.getText(0);
            // System.out.println("RunText===>"+runText);
            if (runText == null)
                continue;

            // --------------------------------------------------------------------------------------------------
            // 1.由于word的某种原因，导致一个单词被拆分到多个块中(Word2010有这个问题），那么需要对多个块进行拼接后替换
            // --------------------------------------------------------------------------------------------------
            if (firstRun == null && key.startsWith(runText) && !key.equals(runText)) { // [A]:::开始位置的处理
                sbTmp = new StringBuffer();
                firstRun = run;
                sbTmp.append(runText);
                continue;
            }
            if (firstRun != null) { // [B]:::中间位置的处理
                if (key.indexOf(runText) > -1) {
                    sbTmp.append(runText);
                    rmvIdx.add(i);// 记下需要移除的Run块
                } else { // 如果不连续，则放弃
                    firstRun = null;
                }
            }
            if (firstRun != null && key.endsWith(runText) && key.equals(sbTmp.toString())) { // [C]:::末尾位置的处理
                doReplace = execTextReplace(doc, paragraph, firstRun, sbTmp.toString(), key, value);
            }

            // ----------------------------------------------------
            // 2.在一个小块内，本身就存在当前变量的情况，这种模式最简单，直接替换即可。
            // ----------------------------------------------------
            if (firstRun == null && runText.indexOf(key) > -1) {
                doReplace = execTextReplace(doc, paragraph, run, runText, key, value);
            }
        }

        if (doReplace) {
            int rmvCount = 0;
            for (int p : rmvIdx) {
                // System.out.println("RMV-----:"+paragraph.getRuns().get(p));
                try{
                    paragraph.removeRun(p + rmvCount); // 由于需要合并到第一个小块内，所以需要把当前Run移除掉
                    rmvCount--;
                }catch(Exception e){}
            }
        }
        rmvIdx.clear();
    }

    private boolean execTextReplace(WordReplaceXWPFDocument doc, XWPFParagraph paragraph, XWPFRun run, String runText,
            String key, WordParameter<?> value) throws WordReplaceException {
        String rt = replaceValue(doc, paragraph, runText, key, value);
        if (rt != null && !runText.equals(rt)) {
            run.setText("", 0);
            return true;
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    private void copyStyle(XWPFParagraph refParagraph, XWPFParagraph paragraph, XWPFRun refRun, XWPFRun run) {
        paragraph.setAlignment(refParagraph.getAlignment());
        paragraph.setVerticalAlignment(refParagraph.getVerticalAlignment());
        paragraph.setIndentationFirstLine(refParagraph.getIndentationFirstLine());
        paragraph.setIndentationHanging(refParagraph.getIndentationHanging());
        paragraph.setIndentationLeft(refParagraph.getIndentationLeft());
        paragraph.setIndentationRight(refParagraph.getIndentationRight());
        paragraph.setPageBreak(refParagraph.isPageBreak());
        paragraph.setStyle(refParagraph.getStyle());
        paragraph.setPageBreak(refParagraph.isPageBreak());
        // paragraph.setWordWrap(refParagraph.isWordWrap());
        // paragraph.setSpacingBeforeLines(refParagraph.getSpacingBeforeLines());
        // paragraph.setSpacingAfterLines(refParagraph.getSpacingAfterLines());
        // paragraph.setSpacingBefore(refParagraph.getSpacingBefore());
        // paragraph.setSpacingAfter(refParagraph.getSpacingAfter());
        paragraph.setBorderTop(refParagraph.getBorderTop());
        paragraph.setBorderRight(refParagraph.getBorderRight());
        paragraph.setBorderBottom(refParagraph.getBorderBottom());
        paragraph.setBorderLeft(refParagraph.getBorderLeft());
        paragraph.setBorderBetween(refParagraph.getBorderBetween());

        if (run != null) {
            run.setBold(refRun.isBold());
            run.setItalic(refRun.isItalic());
            run.setFontFamily(refRun.getFontFamily());
            run.setColor(refRun.getColor());
//            System.out.println(refRun.getText(0)+"-Size:"+refRun.getFontSize());
            if(refRun.getFontSize()<0){
                run.setFontSize(this.defaultReplaceFontSize);    //处理空字符引起的-1带来文字无限大问题
            }else{
                run.setFontSize(refRun.getFontSize());
            }
            run.setStrike(refRun.isStrike());
            run.setUnderline(refRun.getUnderline());
        }
    }

    private String replaceValue(WordReplaceXWPFDocument doc, XWPFParagraph paragraph, String text, String key,
            WordParameter<?> value) throws WordReplaceException {
        if ((value instanceof StringParameter) // 字串或数字
                || (value instanceof NumberParameter)) {
            text = text.replace(key, value.getStringValue());
            logger.debug("Replace:" + key + "=" + text);
            String[] ss = text.split("\r\n");
            // 处理新段落问题
            XWPFParagraph refParagraph = paragraph;
//            XWPFParagraph p = paragraph;
            for (int i = 0; i < ss.length; i++) {
//                if(ss.length>1)System.out.println(i+"-LINE:"+ss[i]+"-:"+ss.length);
                
//                XWPFRun r = p.createRun();
//                r.setText(ss[i]);
//                if(paragraph.getRuns().size()>1){
//                    copyStyle(refParagraph, paragraph, paragraph.getRuns().get(1), r);
//                }
//                if(i<ss.length-1)r.addBreak();
                
                if (i == 0) {
                    text = ss[i];
                } else {
                    int newPIdx = doc.getParagraphs().indexOf(refParagraph) + 1;
                    CTP[] pList = doc.getDocument().getBody().getPArray();
                    newPIdx = Math.min(newPIdx, pList.length);

                    // System.out.println("NewPra:"+ss[i]+","+newPIdx+"<>"+pList.length+","+doc.getParagraphs().indexOf(refParagraph));
                    if (newPIdx < pList.length) {
//                        p = doc.getParagraph(pList[newPIdx]);
//                        p = doc.createParagraph();
//                        XWPFRun r = p.createRun();
//                        r.setText(ss[i]);
//                        r.addBreak();
//                        copyStyle(paragraph, p, paragraph.getRuns().get(0), r);
                        XmlCursor cursor = pList[newPIdx].newCursor();
                        XWPFParagraph p = doc.insertNewParagraph(cursor);
                        XWPFRun r = p.createRun();
                        r.setText(ss[i]);
                        copyStyle(paragraph, p, paragraph.getRuns().get(0), r);
                        refParagraph = p;
                    }
                    // 如果是最后一段，特殊处理
                    if (newPIdx == pList.length) {
                        XmlCursor cursor = pList[pList.length - 1].newCursor();
                        XWPFParagraph p = doc.insertNewParagraph(cursor);
                        XWPFRun r = p.createRun();
                        r.setText(ss[i]);
                        copyStyle(paragraph, p, paragraph.getRuns().get(0), r);
                        refParagraph = p;
                    }
                }
            }
        } else if (value instanceof ImageParameter) { // 图片替换
            text = text.replace(key, "");
            ImageParameter imageParameter = (ImageParameter) value;
            EmbedImage image = imageParameter.getValue();
            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(image.getData());
            try {
                // V3.8+的写法
                doc.addPictureData(byteInputStream, image.getType());
                doc.createPicture(paragraph, doc.getAllPictures().size() - 1, image.getWidth(), image.getHeight(), "");
                // V3.7的写法
                // int ind = doc.addPicture(byteInputStream, picType);
                // doc.createPicture(picId, width , height,paragraph);
            } catch (Exception e) {
                throw new WordReplaceException(e);
            }
        } else {
            // text = text.replace(key, value.getStringValue());
            // System.out.println(key+"------>"+value.getStringValue());
            text = null;
        }
        return text;
    }

    /**
     * 输出内容到输出流中
     * 
     * @param outputStream
     */
    public void write(OutputStream outputStream) throws WordReplaceException {
        try {
            document.write(outputStream);
        } catch (IOException e) {
            throw new WordReplaceException(e);
        }
    }

}
