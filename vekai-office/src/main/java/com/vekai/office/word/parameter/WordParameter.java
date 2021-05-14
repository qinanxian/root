package com.vekai.office.word.parameter;

import java.io.Serializable;

/**
 * Word参数替换，可替换参数项
 * @author yangsong
 * @since 2014年7月31日
 */
public abstract class WordParameter<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	protected String name;
	protected T value;
	
	/**
	 * 构建一个参数项
	 * @param name
	 * @param value
	 */
	public WordParameter(String name, T value) {
		this.name = name;
		this.value = value;
	}
	
	/**
	 * 获取名称
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 设置名称
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 获取值
	 * @return
	 */
	public T getValue() {
		return value;
	}
	
	/**
	 * 设置值
	 * @param value
	 */
	public void setValue(T value) {
		this.value = value;
	}
	
	/**
	 * 以字串形式返回值
	 */
	public abstract String getStringValue();
}
