package com.vekai.office.word.parameter;

import java.text.DecimalFormat;

/**
 * 数字型参数
 *
 * @author yangsong
 * @since 2014年7月31日
 */
public class NumberParameter<T extends Number> extends WordParameter<T>{
	private static final long serialVersionUID = 2127653193052317754L;
	
	private DecimalFormat decimalFormat = null;

	public NumberParameter(String name, T value){
		this("###,###,###,###,###,###,###,###,###,###.##",name,value);
	}

	public NumberParameter(String numberFormat,String name, T value) {
		super(name, value);
		this.decimalFormat = new DecimalFormat(numberFormat);
	}


	public String getStringValue() {
		if(value!=null){
			return decimalFormat.format(value);
		}
		return "";
	}
	
}
