package com.vekai.office.word;

import com.vekai.office.word.parameter.EmbedImage;
import com.vekai.office.word.parameter.EmbedTable;
import com.vekai.office.word.parameter.EmbedTableCell;
import com.vekai.office.word.parameter.EmbedTableRow;
import com.vekai.office.word.parameter.ImageParameter;
import com.vekai.office.word.parameter.NumberParameter;
import com.vekai.office.word.parameter.StringParameter;
import com.vekai.office.word.parameter.TableParameter;
import com.vekai.office.word.parameter.WordParameter;
import com.vekai.office.word.parameter.WordParameterSet;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHeight;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.List;


/**
 * WordReplace????????????Word????????????????????????
 *
 * @author yangsong
 * @since 2014???7???30???
 */
public class WordReplaceForPoi {
    private InputStream inputStream = null;
    private WordReplaceXWPFDocument document;
    private int defaultReplaceFontSize = 12;
    
    /**
     * ????????????WordReplace????????????????????????????????????????????????????????????
     * @param inputStream ?????????????????????
     */
    public WordReplaceForPoi(InputStream inputStream) {
        this.inputStream = inputStream;
    }
    
    /**
     * ????????????
     * @param parameter
     * @throws WordReplaceException
     */
    public void replace(WordParameterSet parameter) throws WordReplaceException{
        createAndReplaceDocument(parameter);
    }
    
    public int getDefaultReplaceFontSize() {
        return defaultReplaceFontSize;
    }

    public void setDefaultReplaceFontSize(int defaultReplaceFontSize) {
        this.defaultReplaceFontSize = defaultReplaceFontSize;
    }

    /**
     * ???Document????????????????????????
     * @param parameter
     * @return
     */
    protected void createAndReplaceDocument(WordParameterSet parameter) throws WordReplaceException{
            try {
            	
            	if(parameter.getAllNames().contains("*BARCODE*")){
            		String barcode = parameter.getParameter("*BARCODE*").getStringValue();
            		if(!barcode.startsWith("*")){
            			parameter.removeParameter("*BARCODE*");
            			parameter.addStringParameter("BARCODE", barcode);
            		}
            	}
            	
                document = new WordReplaceXWPFDocument(inputStream);
                if (parameter != null && parameter.size() > 0) {
                    //??????????????????
                    replaceHeaderAndFotter(parameter);
                    //?????????????????? ??? ????????????
                    WordParameterSet tableParameter = new WordParameterSet();
                    WordParameterSet textParameter = new WordParameterSet();
                    for(WordParameter p : parameter.getParameters()){
                    	if(p instanceof TableParameter){
                    		tableParameter.addParameter(p);
                    	}else{
                    		textParameter.addParameter(p);
                    	}
                    }
                    //????????????
                    List<XWPFParagraph> paragraphList = document.getParagraphs();
                    processParagraphs(paragraphList, textParameter, document);
                    //????????????
                    List<XWPFTable> tableList = document.getTables();
                    for(int i=0;i<tableList.size();i++){
                        XWPFTable table = tableList.get(i);
                        List<XWPFTableRow> rows = table.getRows();
                        for (int m=0;m<rows.size();m++) {
                            XWPFTableRow row = rows.get(m);
                            List<XWPFTableCell> cells = row.getTableCells();
                            for (int j=0;j<cells.size();j++) {
                                XWPFTableCell cell = cells.get(j);
                                List<XWPFParagraph> paragraphListTable =  cell.getParagraphs();
                                processParagraphs(paragraphListTable, textParameter, document);
                            }
                        }
                    }
                    
                    //????????????
                    processTableReplace(tableParameter);
                }
            } catch (IOException e) {
                throw new WordReplaceException(e);
            }
//          processParagraph();
    }
    
    private void replaceHeaderAndFotter(WordParameterSet parameter) throws WordReplaceException{
         List<XWPFHeader> headers = document.getHeaderList();
         List<XWPFFooter>  fotters = document.getFooterList();
         for(XWPFHeader header:headers){
             processParagraphs(header.getParagraphs(), parameter, document);
         }
         for(XWPFFooter footer:fotters){
             processParagraphs(footer.getParagraphs(), parameter, document);
         }
    }
    
//  protected void processParagraph(){
//      try {
//          FileOutputStream fos = new FileOutputStream("C:/aaa.docx");
//          document.write(fos);
//          fos.close();
//          FileInputStream fis = new FileInputStream("C:/aaa.docx");
//          HWPFDocument doc = new HWPFDocument(fis);
//          
//          Range range = doc.getRange();
//          range.replaceText("<p>", "\r");
//          fos = new FileOutputStream("C:/aaa.docx");
//          doc.write(fos);
//          fis.close();
//          fos.close();
//          document = new WordReplaceXWPFDocument(new FileInputStream("C:/aaa.docx"));
//      } catch (FileNotFoundException e) {
//          e.printStackTrace();
//      } catch (IOException e) {
//          e.printStackTrace();
//      }
//  }
    
    /**
     * ??????????????????<??????????????????????????????????????????????????????????????????>
     */
    private void processTableReplace(WordParameterSet parameter){
        replaceTable(document,parameter);
    }
    
    private void replaceTable(WordReplaceXWPFDocument doc,WordParameterSet parameter){
        List<String> names = parameter.getAllNames();
        for(int n=0;n<names.size();n++){
            String name = names.get(n);
            
            for(int i=0;i<doc.getTables().size();i++){
                XWPFTable table = doc.getTables().get(i);
                String firstCellText = getFirstCellValue(table);
                if(firstCellText==null)firstCellText = "";
                firstCellText = firstCellText.replaceAll("\\s+", "");
                
                if(firstCellText.indexOf(name)>=0){
                    WordParameter<?> value = parameter.getParameter(name);
                    if(value instanceof TableParameter){
                        fillTable(table,((TableParameter)value).getValue());
                    }
                }
            }
        }
//      XWPFTable table = doc.getTables().get(1);
//      XWPFTableCell firstCell = table.getRow(0).getCell(0);
//      System.out.println("??????????????????:"+firstCell.getText());
//      
//      for(int i=0;i<table.getRows().size();i++){
//          table.removeRow(i);
//      }
//      XWPFTableRow row = table.createRow();
//      row.createCell().setText("???1??????1???");
//      row.createCell().setText("???1??????2???");
//      row.createCell().setText("???1??????3???");
//      row.createCell().setText("???1??????4???");
//      XWPFTableRow row1 = table.createRow();
//      row1.getCell(0).setText("???2??????1???");
//      row1.getCell(1).setText("???2??????2???");
//      row1.getCell(2).setText("???2??????3???");      
    }
    
    private void fillTable(XWPFTable dscTable, EmbedTable srcTable){
        //??????????????????????????????????????????
        for(int i=0;i<dscTable.getRows().size();i++){
            dscTable.removeRow(i);
        }
        EmbedTable.TableStyle tableStyle = srcTable.getTableStyle();
        if(srcTable.getTableStyle().getWidthType()== EmbedTable.TableStyle.WidthType.DXA){
            dscTable.getCTTbl().getTblPr().getTblW().setType(STTblWidth.DXA);
            dscTable.getCTTbl().getTblPr().getTblW().setW(BigInteger.valueOf((long)(360*4*tableStyle.getWidth())));
        }else{
            dscTable.getCTTbl().getTblPr().getTblW().setType(STTblWidth.AUTO);
        }
        //???????????????
        for(int i=0;i<srcTable.getRows().size();i++){
            EmbedTableRow srcRow = srcTable.getRows().get(i);
            XWPFTableRow dscRow = dscTable.createRow();
            //????????????
            if(tableStyle.getHeight(i)!=null){
                CTTrPr trPr = dscRow.getCtRow().addNewTrPr();
                CTHeight ht = trPr.addNewTrHeight();
                ht.setVal(BigInteger.valueOf((long)(360*4*tableStyle.getHeight(i))));               
            }
            
            for(int j=0;j<srcRow.getCells().size();j++){
                EmbedTableCell srcCell = srcRow.getCells().get(j);
                XWPFTableCell dscCell = null;
                
//              ????????????????????????????????????
                if(j>dscRow.getTableCells().size()-1) dscCell = dscRow.createCell();
                else dscCell = dscRow.getCell(j);
                //????????????
                if(tableStyle.getWidth(j)!=null){
                    CTTcPr tcpr = dscCell.getCTTc().addNewTcPr();
                    CTTblWidth wd = tcpr.addNewTcW();
                    wd.setW(BigInteger.valueOf((long)(360*4*tableStyle.getWidth(j))));
                }
//              dscCell.setText(srcCell.getText());
                fillCell(srcCell,dscCell,srcCell.getText());
            }
        }
    }
    
    private void fillCell(EmbedTableCell srcCell,XWPFTableCell dscCell,String text){
        XWPFParagraph paragraph = dscCell.getParagraphs().get(0);
        XWPFRun run = paragraph.createRun();
        CTTcPr tcpr = dscCell.getCTTc().addNewTcPr();
        CTVerticalJc va = tcpr.addNewVAlign();
        CTShd ctshd = tcpr.addNewShd();
        ctshd.setColor("auto");
        ctshd.setVal(STShd.CLEAR);
        
        EmbedTableCell.CellStyle style = srcCell.getStyle();
        
        run.setBold(style.isBold());
        run.setItalic(style.isItalic()); 
        if(style.getTextPosition()>0)run.setTextPosition(run.getTextPosition());
        if(style.getFontFamily()!=null)run.setFontFamily(style.getFontFamily());
        if(style.getUnderline()!=null)run.setUnderline(UnderlinePatterns.valueOf(style.getUnderline().name()));
        if(style.getTextColor()!=null)ctshd.setFill(style.getBgColor());
        if(style.getBgColor()!=null)run.setColor(style.getTextColor());
        if(style.getFontSize()>0)run.setFontSize(style.getFontSize());
        run.setText(text==null?"":text);
        
        
        if(style.getValign()== EmbedTableCell.CellStyle.VAlign.TOP) va.setVal(STVerticalJc.TOP);
        else if(style.getValign()== EmbedTableCell.CellStyle.VAlign.CENTER) va.setVal(STVerticalJc.CENTER);
        else if(style.getValign()== EmbedTableCell.CellStyle.VAlign.BOTTOM) va.setVal(STVerticalJc.BOTTOM);
        else if(style.getValign()== EmbedTableCell.CellStyle.VAlign.BOTH) va.setVal(STVerticalJc.BOTH);
        else va.setVal(STVerticalJc.CENTER);
        
        if(style.getAlign()== EmbedTableCell.CellStyle.HAlign.LEFT)paragraph.setAlignment(ParagraphAlignment.LEFT);
        else if(style.getAlign()== EmbedTableCell.CellStyle.HAlign.CENTER)paragraph.setAlignment(ParagraphAlignment.CENTER);
        else if(style.getAlign()==EmbedTableCell.CellStyle.HAlign.RIGHT)paragraph.setAlignment(ParagraphAlignment.RIGHT);
        else if(style.getAlign()==EmbedTableCell.CellStyle.HAlign.BOTH)paragraph.setAlignment(ParagraphAlignment.BOTH);
        else paragraph.setAlignment(ParagraphAlignment.LEFT);
        
        //????????????
        if(srcCell.getStyle().getVMerge()==EmbedTableCell.CellStyle.VMerge.START){
            CTVMerge merge = tcpr.addNewVMerge();
            merge.setVal(STMerge.RESTART);
        }
        if(srcCell.getStyle().getVMerge()==EmbedTableCell.CellStyle.VMerge.CONTINUE){
            CTVMerge merge = tcpr.addNewVMerge();
            merge.setVal(STMerge.CONTINUE);
        }
        
        //????????????
        if(srcCell.getStyle().getHMerge()==EmbedTableCell.CellStyle.HMerge.START){
            CTHMerge merge = tcpr.addNewHMerge();
            merge.setVal(STMerge.RESTART);
        }
        if(srcCell.getStyle().getHMerge()==EmbedTableCell.CellStyle.HMerge.CONTINUE){
            CTHMerge merge = tcpr.addNewHMerge();
            merge.setVal(STMerge.CONTINUE);
        }
        
    }
    
    /**
     * ?????????????????????????????????????????????
     * @param table
     * @return
     */
    private String getFirstCellValue(XWPFTable table){
        String textValue = null;
        if(table==null||table.getRows().size()<1)return textValue;
        XWPFTableRow row = table.getRow(0);
        if(row==null||row.getTableCells().size()<1)return textValue;
        XWPFTableCell cell = row.getCell(0);
        textValue = cell.getText();
        return textValue;
    }

    /**
     * ????????????
     * @param paragraphList
     */
    private void processParagraphs(List<XWPFParagraph> paragraphList,WordParameterSet parameter,WordReplaceXWPFDocument doc) throws WordReplaceException{
        if(paragraphList != null && paragraphList.size() > 0){
            for(int i=0;i<paragraphList.size();i++){
                XWPFParagraph paragraph = paragraphList.get(i);
                String paragraphText = paragraph.getParagraphText();
                List<String> keyNames = parameter.getAllNames();
                for (int j=0;j<keyNames.size();j++) {
                    String key = keyNames.get(j);
                    WordParameter<?> value = parameter.getParameter(key);
                    if(paragraphText.indexOf(key)!=-1){
//                      System.out.println("KV-->"+key+"="+value+",paragraph:"+paragraphText);
                        paragraphReplace(doc,paragraph,key,value);
                    }
                }
            }
        }
    }
    
    /**
     * ??????????????????
     * @param paragraph
     * @param key
     * @param value
     */
    private void paragraphReplace(WordReplaceXWPFDocument doc,XWPFParagraph paragraph,String key,WordParameter<?> value) throws WordReplaceException{
        if(paragraph.isEmpty())return;
        List<XWPFRun> runList = paragraph.getRuns();
        XWPFRun firstRun = null;
        StringBuffer sbTmp = new StringBuffer();
        boolean doReplace = false;
        for(int i=0;i<runList.size();i++){
            XWPFRun run = runList.get(i);
            String runText = null;
            try{
            	runText = run.getText(0);
            }catch(Exception e){
            	continue;
            }

            if(runText==null)continue;
            
            //--------------------------------------------------------------------------------------------------
            //1.??????word????????????????????????????????????????????????????????????(Word2010??????????????????????????????????????????????????????????????????
            //--------------------------------------------------------------------------------------------------
            if(firstRun==null&&key.startsWith(runText)&&!key.equals(runText)){          //[A]:::?????????????????????
                sbTmp = new StringBuffer();
                firstRun = run;
                sbTmp.append(runText);
                continue;
            }
            if(firstRun!=null){                                                         //[B]:::?????????????????????
                if(key.indexOf(runText)>-1){
                    sbTmp.append(runText);
                    run.setText(null,0);
                    //rmvIdx.add(j);//?????????????????????Run???
                }else{          //???????????????????????????
                    firstRun = null;
                }
            }
            if(firstRun!=null&&key.endsWith(runText)&&key.equals(sbTmp.toString())){    //[C]:::?????????????????????
                doReplace = execTextReplace(doc,paragraph,firstRun,sbTmp.toString(),key,value);
                if(doReplace)firstRun = null;
            }
            
            //----------------------------------------------------
            //2.?????????????????????????????????????????????????????????????????????????????????????????????????????????
            //----------------------------------------------------
            if(firstRun==null&&runText.indexOf(key)>-1){
                doReplace = execTextReplace(doc,paragraph,run,runText,key,value);
            }
        }
    }
    
    private boolean execTextReplace(WordReplaceXWPFDocument doc,XWPFParagraph paragraph,XWPFRun run,String runText,String key,WordParameter<?> value) throws WordReplaceException{
        String rt = replaceValue(doc,paragraph,runText,key,value);
        if(rt!=null&&!runText.equals(rt)){
            String[] ss = rt.split("\r\n");
            if(ss.length>1){
                run.setText("",0);
                for(int i=0;i<ss.length;i++){
                    XWPFRun r = paragraph.createRun();
                    r.setText(ss[i],0);
                    copyStyle(paragraph,paragraph,run,r);
                    if(i<ss.length-1)r.addBreak();
                }
            }else{
                run.setText(rt,0);
            }
            return true;
        }
        return false;
    }
    
    private void copyStyle(XWPFParagraph refParagraph,XWPFParagraph paragraph,XWPFRun refRun,XWPFRun run){
        paragraph.setAlignment(refParagraph.getAlignment());
        paragraph.setVerticalAlignment(refParagraph.getVerticalAlignment());
        paragraph.setIndentationFirstLine(refParagraph.getIndentationFirstLine());
        paragraph.setIndentationHanging(refParagraph.getIndentationHanging());
        paragraph.setIndentationLeft(refParagraph.getIndentationLeft());
        paragraph.setIndentationRight(refParagraph.getIndentationRight());
        paragraph.setPageBreak(refParagraph.isPageBreak());
        paragraph.setStyle(refParagraph.getStyle());
        paragraph.setPageBreak(refParagraph.isPageBreak());
//      paragraph.setWordWrap(refParagraph.isWordWrap());
//      paragraph.setSpacingBeforeLines(refParagraph.getSpacingBeforeLines());
//      paragraph.setSpacingAfterLines(refParagraph.getSpacingAfterLines());
//      paragraph.setSpacingBefore(refParagraph.getSpacingBefore());
//      paragraph.setSpacingAfter(refParagraph.getSpacingAfter());
        paragraph.setBorderTop(refParagraph.getBorderTop());
        paragraph.setBorderRight(refParagraph.getBorderRight());
        paragraph.setBorderBottom(refParagraph.getBorderBottom());
        paragraph.setBorderLeft(refParagraph.getBorderLeft());
        paragraph.setBorderBetween(refParagraph.getBorderBetween());
        
        if(run!=null){
            run.setBold(refRun.isBold());
            run.setItalic(refRun.isItalic());
            run.setFontFamily(refRun.getFontFamily());
            run.setColor(refRun.getColor());
            if(refRun.getFontSize()>0){
                run.setFontSize(refRun.getFontSize());
            }
//            run.setStrike(refRun.isStrike());
            run.setUnderline(refRun.getUnderline());
        }
    }
    
    private String replaceValue(WordReplaceXWPFDocument doc,XWPFParagraph paragraph,String text,String key,WordParameter<?> value) throws WordReplaceException{
        if ((value instanceof StringParameter)          //???????????????
                ||(value instanceof NumberParameter)
                ) {
            text = text.replace(key, value.getStringValue());
//            System.out.println("Replace:"+key+"="+text);
//            String[] ss = text.split("\r\n");
//            //?????????????????????
//            XWPFParagraph refParagraph = paragraph;
//            for(int i=0;i<ss.length;i++){
//                if(i==0){
//                    text = ss[i];
//                }else{
////                    int newPIdx = doc.getParagraphs().indexOf(refParagraph)+1;
////                    List<CTP> pList = doc.getDocument().getBody().getPList();
////                    newPIdx = Math.min(newPIdx, pList.size());
//                    
//                    int newPIdx = doc.getParagraphs().indexOf(refParagraph) + 1;
//                    CTP[] pListArray = doc.getDocument().getBody().getPArray();
//                    newPIdx = Math.min(newPIdx, pListArray.length);
//                    List<CTP> pList = new ArrayList<CTP>();
//                    pList.addAll(Arrays.asList(pListArray));
//                    
//                    System.out.println("RL:"+ss[i]);
//                    if("???????????????".equals(ss[i])){
//                        System.out.println("---here-----");
//                    }
//                    //System.out.println("NewPra:"+ss[i]+","+newPIdx+"<>"+pList.size()+","+doc.getParagraphs().indexOf(refParagraph));
//                    if(newPIdx<pList.size()){
//                        XmlCursor cursor = pList.get(newPIdx).newCursor();
//                        XWPFParagraph p = doc.insertNewParagraph(cursor);
//                        XWPFRun r = p.createRun();
//                        r.setText(ss[i]);
//                        copyStyle(paragraph,p,paragraph.getRuns().get(0),r);
//                        refParagraph = p;
//                    }
//                    //????????????????????????????????????
//                    if(newPIdx==pList.size()){
//                        XmlCursor cursor = pList.get(pList.size()-1).newCursor();
//                        XWPFParagraph p = doc.insertNewParagraph(cursor);
//                        XWPFRun r = p.createRun();
//                        r.setText(ss[i]);
//                        copyStyle(paragraph,p,paragraph.getRuns().get(0),r);
//                        refParagraph = p;
//                    }
//                }
//            }
        }else if (value instanceof ImageParameter) {    //????????????
            text = text.replace(key, "");
            ImageParameter imageParameter = (ImageParameter)value;
            EmbedImage image = imageParameter.getValue();
            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(image.getData());
            try {
                //V3.8+?????????
                doc.addPictureData(byteInputStream, image.getType());
                doc.createPicture(paragraph, doc.getAllPictures().size()-1, image.getWidth(), image.getHeight(), "");
                //V3.7?????????
//              int ind = doc.addPicture(byteInputStream, picType);
//              doc.createPicture(picId, width , height,paragraph);
            } catch (Exception e) {
                throw new WordReplaceException(e);
            }
        }else{
//          text = text.replace(key, value.getStringValue());
//          System.out.println(key+"------>"+value.getStringValue());
            text = null;
        }
//        }
        return text;
    }
    
    /**
     * ???????????????????????????
     * @param outputStream
     */
    public void write(OutputStream outputStream) throws WordReplaceException{
        try {
            document.write(outputStream);
        } catch (IOException e) {
            throw new WordReplaceException(e);
        }
    }
    
    public static void addBarcodeBySystem(String fileId,String barcode){
//		WordAcceptRevisions war = new WordAcceptRevisions();
//		war.addBarcodeBySystem(fileId, barcode);
    }
    
}
