package com.vekai.office.word.parameter;

/**
 * 整数型参数
 * @author yangsong
 * @since 2014年7月31日
 */
public class IntParameter extends NumberParameter<Integer> {
	private static final long serialVersionUID = 3467532420829654187L;
	
	public static final String NUMBER_FORMAT = "###,###,###,###,###,###,###,###,###,##0";

	public IntParameter(String name, Integer value) {
		super(NUMBER_FORMAT, name, value);
	}

}
