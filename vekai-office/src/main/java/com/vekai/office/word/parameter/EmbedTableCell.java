package com.vekai.office.word.parameter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 表格单元格
 *
 * @author yangsong
 * @since 2014年7月31日
 */
public class EmbedTableCell implements Serializable{
	private static final long serialVersionUID = -6425444202816369788L;

	private EmbedTableRow row;
	private String text;
	private CellStyle style;

	public EmbedTableCell(){
		style = new CellStyle(this);
	}
	
	public EmbedTableCell(EmbedTableRow row) {
		this();
		this.row = row;
	}

	public EmbedTableRow getRow() {
		return row;
	}
	public void setRow(EmbedTableRow row) {
		this.row = row;
	}

	/**
	 * 获取样式
	 * @return
	 */
	public CellStyle getStyle() {
		return style;
	}

	/**
	 * 获取单元格文本
	 * @return
	 */
	public String getText() {
		return text;
	}

	/**
	 * 设置单元格文本
	 * @param text
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * 获取上方的所有单元格列表
	 * @return
	 */
	public List<EmbedTableCell> getUpCells(){
		List<EmbedTableCell> cells = new ArrayList<EmbedTableCell>();
		if(row!=null){
			int cellIdx = row.getCellIndex(this);
			List<EmbedTableRow> upRows = row.getUpRows();
			for(int i=0;i<upRows.size();i++){
				if(cellIdx<upRows.get(i).getCells().size()){
					cells.add(upRows.get(i).getCells().get(cellIdx));
				}
			}
		}		
		return cells;
	}
	
	/**
	 * 获取右边的单元格
	 * @return
	 */
	public List<EmbedTableCell> getRightCells(){
		List<EmbedTableCell> cells = new ArrayList<EmbedTableCell>();
		if(row!=null){
			int startIdx = row.getCellIndex(this)+1;
			int endIdx = row.getCells().size();
			if(startIdx>0)cells.addAll(row.getCells().subList(startIdx, endIdx));
		}
		return cells;
	}
	
	/**
	 * 获取上方的所有单元格列表
	 * @return
	 */
	public List<EmbedTableCell> getDownCells(){
		List<EmbedTableCell> cells = new ArrayList<EmbedTableCell>();
		if(row!=null){
			int cellIdx = row.getCellIndex(this);
			List<EmbedTableRow> downRows = row.getDownRows();
			for(int i=0;i<downRows.size();i++){
				if(cellIdx<downRows.get(i).getCells().size()){
					cells.add(downRows.get(i).getCells().get(cellIdx));
				}
			}
		}		
		return cells;
	}
	
	/**
	 * 获取左边的单元格
	 * @return
	 */
	public List<EmbedTableCell> getLeftCells(){
		List<EmbedTableCell> cells = new ArrayList<EmbedTableCell>();
		if(row!=null){
			int cellIdx = row.getCellIndex(this);
			if(cellIdx>0)cells.addAll(row.getCells().subList(0, cellIdx));
		}
		return cells;
	}	
	
	/**
	 * 单元格样式
	 */
	public static class CellStyle implements Serializable{
		private static final long serialVersionUID = 2517235887604438484L;
		
		public enum VMerge{NONE,START,CONTINUE};
		public enum HMerge{NONE,START,CONTINUE};
		public enum VAlign{TOP,CENTER,BOTTOM,BOTH};
		public enum HAlign{LEFT,CENTER,RIGHT,BOTH};
		public enum Underline{NGLE,WORDS,DOUBLE,THICK,DOTTED,DOTTED_HEAVY,DASH,DASHED_HEAVY,DASH_LONG,DASH_LONG_HEAVY,DOT_DASH,DASH_DOT_HEAVY,DOT_DOT_DASH,DASH_DOT_DOT_HEAVY,WAVE,WAVY_HEAVY,WAVY_DOUBLE,NONE};
		
		private EmbedTableCell cell;
		public String textColor = null;
		private String bgColor = null;
		private String fontFamily = "华文细黑";
		private int fontSize = 0;
		private boolean bold = false;
		private boolean italic = false;
		private Underline underline = Underline.NONE;
		private int textPosition = 0;
		private VAlign valign = VAlign.CENTER;
		private HAlign align = HAlign.LEFT;
		private int mergeX;
		private int mergeY;
		private VMerge vMerge = VMerge.NONE;
		private HMerge hMerge = HMerge.NONE;
		
		protected CellStyle(EmbedTableCell cell){
			this.cell = cell;
		}

		/**
		 * 获取样式所在的单元格
		 * @return
		 */
		public EmbedTableCell getCell(){
			return this.cell;
		}
		
		/**
		 * 取文本颜色
		 * @return
		 */
		public String getTextColor() {
			return textColor;
		}

		/**
		 * 设置本颜色
		 * @param textColor
		 */
		public void setTextColor(String textColor) {
			this.textColor = textColor;
		}

		/**
		 * 取背景填充色
		 * @return
		 */
		public String getBgColor() {
			return bgColor;
		}

		/**
		 * 设置背景填充色
		 * @param bgColor
		 */
		public void setBgColor(String bgColor) {
			this.bgColor = bgColor;
		}

		/**
		 * 取字体
		 * @return
		 */
		public String getFontFamily() {
			return fontFamily;
		}

		/**
		 * 设置字体
		 * @param fontFamily
		 */
		public void setFontFamily(String fontFamily) {
			this.fontFamily = fontFamily;
		}

		/**
		 * 取字号
		 * @return
		 */
		public int getFontSize() {
			return fontSize;
		}

		/**
		 * 设置字号
		 * @param fontSize
		 */
		public void setFontSize(int fontSize) {
			this.fontSize = fontSize;
		}

		/**
		 * 是否加粗
		 * @return
		 */
		public boolean isBold() {
			return bold;
		}

		/**
		 * 设置加粗
		 * @param bold
		 */
		public void setBold(boolean bold) {
			this.bold = bold;
		}

		/**
		 * 是否倾斜
		 * @return
		 */
		public boolean isItalic() {
			return italic;
		}

		/**
		 * 设置是否倾斜
		 * @param italic
		 */
		public void setItalic(boolean italic) {
			this.italic = italic;
		}


		/**
		 * 获取下划线
		 * @return
		 */
		public Underline getUnderline() {
			return underline;
		}

		/**
		 * 设置下划线
		 * @param underline
		 */
		public void setUnderline(Underline underline) {
			this.underline = underline;
		}

		/**
		 * 获取文本位置
		 * @return
		 */
		public int getTextPosition() {
			return textPosition;
		}
		/**
		 * 设置文本位置
		 * @param textPosition
		 */
		public void setTextPosition(int textPosition) {
			this.textPosition = textPosition;
		}


		/**
		 * 获取在X方向上，合并了多少个单元
		 * @return
		 */
		public int getMergeY(){
			return this.mergeY;
		}
		
		/**
		 * 设置在X方向上，合并了多少个单元
		 * @param rowSpan
		 */
		public void mergeY(int mergeY) {
			this.mergeY = mergeY;
			if(mergeY>0){
				this.vMerge = VMerge.START;
				List<EmbedTableCell> downCells = cell.getDownCells();
				int maxRowSpan = Math.min(mergeY, downCells.size());
				for(int i=0;i<maxRowSpan;i++){
					EmbedTableCell cell = downCells.get(i);
					cell.style.vMerge = VMerge.CONTINUE;
				}
			}
		}

		/**
		 * 获取Y方面上，合并了多少个单元格
		 * @return
		 */
		public int getMergeX(){
			return this.mergeX;
		}
		
		/**
		 * 设置竖跨多少列
		 * @param rowSpan
		 */
		public void mergeX(int mergeX) {
			if(mergeX>0){
				this.hMerge = HMerge.START;
				List<EmbedTableCell> rightCells = cell.getRightCells();
				int maxColSpan = Math.min(mergeX, rightCells.size());
				for(int i=0;i<maxColSpan;i++){
					EmbedTableCell cell = rightCells.get(i);
					cell.style.hMerge = HMerge.CONTINUE;
				}
			}
		}

		/**
		 * 合并一个矩形区域
		 * @param x
		 * @param y
		 */
		public void mergeRect(int x,int y){
			x = x-1;
			y = y-1;
			if(x>0&&y<=0)mergeX(x);	//同mergeX
			if(x<=0&&y>0)mergeY(y);	//同mergeY
			
			if(x>0&&y>0){
				List<EmbedTableCell> dcList = cell.getDownCells();
				int downSize = Math.min(dcList.size(),y);
				List<EmbedTableCell> rcList = new ArrayList<EmbedTableCell>();
				rcList.add(cell);
				rcList.addAll(dcList.subList(0, downSize));
				
				//从第一行的第一个单元格开始，查找其后单元格
				for(int i=0;i<rcList.size();i++){
					EmbedTableCell curCell = rcList.get(i);
					List<EmbedTableCell> rightCells = curCell.getRightCells();
					//每行的第一个单元格在X方向一定要是START
					curCell.style.hMerge = HMerge.START;
					//第一行的单元格在Y方向上，一定要是START,其他行的为CONTINUE
					if(i==0) curCell.style.vMerge = VMerge.START;
					else curCell.style.vMerge = VMerge.CONTINUE;
					//第一个单元格之后的单元格处理
					int endIdx = Math.min(rightCells.size(), x);
					for(int j=0;j<endIdx;j++){
						if(i==0) rightCells.get(j).style.vMerge = VMerge.START;	////第一行的单元格在Y方向上
						else rightCells.get(j).style.vMerge = VMerge.CONTINUE;
						rightCells.get(j).style.hMerge = HMerge.CONTINUE;
					}
				}
			}
		}
		
		/**
		 * 获取垂直对齐方式
		 * @return
		 */
		public VAlign getValign() {
			return valign;
		}

		/**
		 * 设置垂直对齐方式
		 * @param valign
		 */
		public void setValign(VAlign valign) {
			this.valign = valign;
		}

		/**
		 * 获取水平对齐方式
		 * @return
		 */
		public HAlign getAlign() {
			return align;
		}

		/**
		 * 设置水平对齐方式
		 * @param align
		 */
		public void setAlign(HAlign align) {
			this.align = align;
		}

		/**
		 * 获取垂直合并
		 * @return
		 */
		public VMerge getVMerge() {
			return vMerge;
		}

		/**
		 * 获取水平合并
		 * @return
		 */
		public HMerge getHMerge() {
			return hMerge;
		}
		

	}

}
