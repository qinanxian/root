package com.vekai.office.word.parameter;

/**
 * 字串型参数
 *
 * @author yangsong
 * @since 2014年7月31日
 */
public class StringParameter extends WordParameter<String>{
	private static final long serialVersionUID = -7646193102466738047L;

	public StringParameter(String name, String value) {
		super(name, value);
	}

	public String getStringValue() {
		if(value==null)return "";
		return value;
	}

}
