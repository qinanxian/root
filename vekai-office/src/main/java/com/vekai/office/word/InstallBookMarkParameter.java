//package com.vekai.office.word;
//
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//import com.vekai.runtime.kit.IOKit;
//import org.apache.commons.io.output.ByteArrayOutputStream;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.aspose.words.Bookmark;
//import com.aspose.words.BookmarkCollection;
//import com.aspose.words.Cell;
//import com.aspose.words.ControlChar;
//import com.aspose.words.Document;
//import com.aspose.words.License;
//import com.aspose.words.Node;
//import com.aspose.words.NodeType;
//import com.aspose.words.OoxmlSaveOptions;
//import com.aspose.words.Paragraph;
//import com.aspose.words.Row;
//import com.aspose.words.Run;
//import com.aspose.words.Table;
//
///**
// * 书签参数替换（主要处理word表格动态处理）
// * @author FANGDAFA231
// *
// */
//public abstract class InstallBookMarkParameter {
//	protected Logger logger = LoggerFactory.getLogger(this.getClass());
//
//	protected Document document = null;
//	static {
//		 	InputStream license = InstallBookMarkParameter.class.getClassLoader().getResourceAsStream("license.xml");    // license路径
//	        License aposeLic = new License();
//	        try {
//				aposeLic.setLicense(license);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//	}
//	public InputStream getInstallParameterStream(InputStream fis){
//		try {
//			document = new Document(fis);
//			installParams();
//			ByteArrayOutputStream out = new ByteArrayOutputStream();
//			document.save(out, new OoxmlSaveOptions());
//			return IOKit.convertToInputStream(out.toByteArray());
//		} catch (Exception e) {
//			e.printStackTrace();
//			return fis;
//		}
//	}
//	/**
//	 * 组装参数
//	 */
//	protected void installParams(){};
//
//
//	protected void deleteRow(String markName){
//			Bookmark bookmark = document.getRange().getBookmarks().get(markName);//拿到书签
//			if(bookmark==null)return;
//		 	Cell cell = (Cell)bookmark.getBookmarkStart().getParentNode().getParentNode();//拿到单元格
//	        cell.getParentRow().remove();//删除行
//	}
//
//
//	protected void deletePage(String markName){
//		BookmarkCollection bms = document.getRange().getBookmarks();
//		int count = bms.getCount();
//		List<Paragraph> pList = new ArrayList<Paragraph>();
//		Table table = null;
//		for(int i=0;i<count;i++){
//			Bookmark bookmark = bms.get(i);
//			if(bookmark.getName().startsWith(markName)){
//				if(markName.equals(bookmark.getName())){
//					try{
//						table = ((Table)bookmark.getBookmarkStart().getParentNode().getParentNode().getParentNode().getParentNode());
//						continue;
//					}catch(Exception e){
//						e.printStackTrace();
//					}
//				}
//				Paragraph paragraph = (Paragraph)bookmark.getBookmarkStart().getAncestor(NodeType.PARAGRAPH);
//				pList.add(paragraph);
//			}
//		}
//		if(table!=null)table.remove();
//		for(Paragraph p: pList){
//			try{
//				if (p.getParagraphFormat().getPageBreakBefore())
//					p.getParagraphFormat().setPageBreakBefore(false);
//				for(Run r : p.getRuns()){
//					if(r.getText().contains(ControlChar.PAGE_BREAK)){
//						r.setText(r.getText().replace(ControlChar.PAGE_BREAK, ""));
//					}
//				}
//				p.remove();
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//		}
//	}
//
//	/**
//	 * 插入表格(动态增加列暂未考虑)
//	 * @param markName
//	 * @param rows
//	 * @throws Exception
//	 */
//	protected void insertTable(String markName,List<Object> rows) throws Exception{
//		Bookmark bookmark = document.getRange().getBookmarks().get(markName);//拿到书签
//		Node remarkRow = bookmark.getBookmarkStart().getParentNode().getParentNode().getParentNode();//获取书签的所在行原始对象。
//		Table table = (Table)remarkRow.getParentNode();//获取书签所在表格对象
//		int insertRowNum = table.getRows().indexOf(remarkRow)+1;//获取书签所在行数
//		for(Object rowRuns : rows){
//			List<String> rowRun = (List<String>)rowRuns;
//			Node insertRow = remarkRow.deepClone(true);//复制一行
//			for(int i=0;i<rowRun.size();i++){
//				insertCellText((Row)insertRow, i, rowRun.get(i));
//			}
//			table.getRows().insert(insertRowNum, insertRow);//插入行
//			insertRowNum++;//注意计数
//		}
//		remarkRow.remove();
//	};
//
//	/**
//	 * 插入单元格数据
//	 * @param row
//	 * @param col
//	 * @param text
//	 * @throws Exception
//	 */
//	protected void insertCellText(Row row,int col,String text) throws Exception{
//		String[] runs = text.split("\r\n");
//		Cell cell = row.getCells().get(col);
//		Paragraph p =cell.getFirstParagraph();
//		for(int i=0;i<runs.length;i++ ){
//			Node cp = p.deepClone(true);
//			((Paragraph)cp).getRuns().get(0).setText(runs[i]);
//			cell.getParagraphs().insert(i+2, cp);
//		}
//		p.remove();
//	}
//
//}
