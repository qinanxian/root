package com.vekai.office.word.parameter;

/**
 * 表格参数
 *
 * @author yangsong
 * @since 2014年7月31日
 */
public class TableParameter extends WordParameter<EmbedTable>{
	private static final long serialVersionUID = -8533896714805217612L;

	public TableParameter(String name, EmbedTable value) {
		super(name, value);
	}


	public String getStringValue() {
		return null;
	}

}
