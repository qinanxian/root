package com.vekai.office.word.parameter;

import cn.fisok.raw.kit.StringKit;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;



/**
 * 参数集合
 *
 * @author yangsong
 * @since 2014年7月31日
 */
public class WordParameterSet {
	private Map<String,WordParameter<?>> parameters ;
	private String varPrefix = "【※";
	private String varSufix = "】";
	
	public WordParameterSet(){
		parameters = new LinkedHashMap<String, WordParameter<?>>();
	}
	
	/**
	 * 获取一个参数
	 * @param name
	 * @param wrap
	 * @return
	 */
	public WordParameter<?> getParameter(String name,boolean wrap){
		if(wrap)return parameters.get(getVarName(name));
		else return parameters.get(name);
	}
	/**
	 * 获取一个参数
	 * @param name
	 * @return
	 */
	public WordParameter<?> getParameter(String name){
		return getParameter(name,false);
	}
	
	/**
	 * 添加一个参数
	 * @param parameter
	 */
	public void addParameter(WordParameter<?> parameter){
		parameters.put(getVarName(parameter.getName()), parameter);
	}
	
	/**
	 * 添加一个字串参数
	 * @param name 参数名
	 * @param value 参数值
	 */
	public void addStringParameter(String name,String value){
		addParameter(new StringParameter(name, value));
	}
	
	/**
	 * 添加一个数字参数
	 * @param name 参数名
	 * @param value 参数值
	 */
	public void addNumberParameter(String name,Number value){
		addParameter(new NumberParameter<Number>(name, value));
	}
	
	/**
	 * 添加一个整数参数
	 * @param name 参数名
	 * @param value 参数值
	 */
	public void addIntParameter(String name,Integer value){
		addParameter(new IntParameter(name, value));
	}
	
	/**
	 * 添加一个金额参数
	 * @param name 参数名
	 * @param value 参数值
	 */
	public void addMoneyParameter(String name,Double value){
		addParameter(new MoneyParameter(name, value));
	}
	
	/**
	 * 添加一个图片参数
	 * @param name 参数名
	 * @param value 参数值
	 */
	public void addImageParameter(String name,EmbedImage value){
		addParameter(new ImageParameter(name, value));
	}
	
	/**
	 * 添加一个表格参数
	 * @param name 参数名
	 * @param value 参数值
	 */
	public void addTableParameter(String name,EmbedTable value){
		addParameter(new TableParameter(name, value));
	}
	
	/**
	 * 移除参数
	 * @param name
	 * @return
	 */
	public WordParameter<?> removeParameter(String name){
		return parameters.remove(getVarName(name));
	}
	
	/**
	 * 获取所有参数名称列表
	 * @return
	 */
	public List<String> getAllNames(){
		List<String> list = new ArrayList<String>();
		list.addAll(parameters.keySet());
		return list;
	}
	
	/**
	 * 获取所有参数列表
	 * @return
	 */
	public List<WordParameter<?>> getParameters(){
		List<WordParameter<?>> list = new ArrayList<WordParameter<?>>();
		list.addAll(parameters.values());
		return list;
	}
	
	/**
	 * 获取变量名
	 * @param name
	 * @return
	 */
	public String getVarName(String name){
		if("*BARCODE*".equals(name)||"BARCODE".equals(name))return name;
		return varPrefix+name+varSufix;
	}

	/**
	 * 获取变量前缀
	 * @return
	 */
	public String getVarPrefix() {
		return varPrefix;
		}

	/**
	 * 设置变量前缀
	 * @param varPrefix
	 */
	public void setVarPrefix(String varPrefix) {
		this.varPrefix = StringKit.nvl(varPrefix,"");
	}

	/**
	 * 获取变量后缀
	 * @return
	 */
	public String getVarSufix() {
		return varSufix;
	}

	/**
	 * 设置变量后缀
	 * @param varSufix
	 */
	public void setVarSufix(String varSufix) {
		this.varSufix = StringKit.nvl(varSufix,"");
	}

	/**
	 * 获取参数个数
	 * @return
	 */
	public int size(){
		return parameters.size();
	}

	public String toString() {
		return parameters.toString();
	}
	
	
	
}
