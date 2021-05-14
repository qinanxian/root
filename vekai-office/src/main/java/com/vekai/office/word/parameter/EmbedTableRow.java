package com.vekai.office.word.parameter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 表格行
 *
 * @author yangsong
 * @since 2014年7月31日
 */
public class EmbedTableRow implements Serializable{

	private static final long serialVersionUID = -8486908926473209964L;

	private EmbedTable table;
	private List<EmbedTableCell> cellList ;

	public EmbedTableRow(){
		cellList = new ArrayList<EmbedTableCell>();
	}
	public EmbedTableRow(EmbedTable table) {
		this();
		this.table = table;
	}
	
	public EmbedTable getTable(){
		return this.table;
	}
	public void setTable(EmbedTable table) {
		this.table = table;
	}
	
	/**
	 *  创建一个单元格
	 * @return
	 */
	public EmbedTableCell createCell(){
		EmbedTableCell cell = new EmbedTableCell(this);
		cellList.add(cell);
		return cell;
	}
	
	/**
	 * 获取所有的单元格
	 * @return
	 */
	public List<EmbedTableCell> getCells(){
		return cellList;
	}
	
	public int getCellIndex(EmbedTableCell cell){
		if(cellList==null)return -1;
		return cellList.indexOf(cell);
	}
	
	/**
	 * 获取当前行之前(上)的所有行
	 * @return
	 */
	public List<EmbedTableRow> getUpRows(){
		List<EmbedTableRow> rows = new ArrayList<EmbedTableRow>();
		if(table!=null){
			int curRowIdx = table.getRowIndex(this);
			if(curRowIdx>0)rows.addAll(rows.subList(0, curRowIdx));
		}
		return rows;
	}
	
	/**
	 * 获取当前行之后(下)的所有行
	 * @return
	 */
	public List<EmbedTableRow> getDownRows(){
		List<EmbedTableRow> rows = new ArrayList<EmbedTableRow>();
		if(table!=null){
			int curRowIdx = table.getRowIndex(this)+1;
			int endIndex = table.getRows().size();
			if(curRowIdx<endIndex)rows.addAll(table.getRows().subList(curRowIdx, endIndex));
		}		
		return rows;
	}
}
