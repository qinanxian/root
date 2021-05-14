package com.vekai.office.excel.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.fisok.raw.kit.StringKit;

/**
 * Excel地址转换操作的API
 * @author syang
 * @date 2013年6月，宁波银行项目组
 * @history 2014/02/25 集成最新版taskext并整合到东莞农商行项目中
 *
 */
public class ExcelAddressConvert {
	private static Pattern cellAddrPattern = Pattern.compile("^[a-zA-Z]+[0-9]*$");
	/**
	 * 判断指定地址表达式是否为一个合法的Excel单元格地址
	 * @param expr
	 * @return
	 */
	public static boolean isCellAddress(String expr){
		if(StringKit.isEmpty(expr))return false;
		Matcher matcher = cellAddrPattern.matcher(expr);
		return matcher.find();
	}	
	/**
	 * 读取第一个字母结束索引
	 * @param adr
	 * @return
	 */
	public static int getFirstEndCharIndex(String adr){
		int preIndex=0;
		for(int i=0;i<adr.length();i++){
			Character ch=adr.charAt(i);
			ch=Character.toUpperCase(ch);
			if(ch>='A'&&ch<='Z'){
				preIndex=i;
			}else{//只要找到一个不是字母的，就结束掉
				break;
			}
		}
		return preIndex;
	}
	
	/**
	 * 读取列地址名称
	 * @param adr  excel地址
	 * @return
	 */
	public static String getColumnName(String adr){
		 int firstEndCharIdx=getFirstEndCharIndex(adr);
		 return adr.substring(0,firstEndCharIdx+1).toUpperCase();
	}
	
	/**
	 * 根据列索引，读取Excel列地址
	 * @param columnIndex
	 * @return
	 */
	public static String getColumnName(int columnIndex){
		return convert10To26(columnIndex);
	}
	
	/**
	 * 读取行地址名称
	 * @param adr excel地址
	 * @return
	 */
	public static String getRowName(String adr){
		int firstEndCharIdx=getFirstEndCharIndex(adr);
		return adr.substring(firstEndCharIdx+1);
	}
	
	/**
	 * 根据行索引，读取Excel行地址
	 * @param rowIndex
	 * @return
	 */
	public static String getRowName(int rowIndex){
		return ""+(rowIndex+1);
	}
	
	/**
	 * 根据行列取地址
	 * @param colIndex
	 * @param rowIndex
	 * @return
	 */
	public static String getCellAddress(int colIndex,int rowIndex){
		return getColumnName(colIndex)+getRowName(rowIndex);
	}
	
	/**
	 * 获取列索引
	 * @param adr excel地址
	 * @return
	 */
	public static int getColumnIndex(String adr){
		String columnAdr=getColumnName(adr);
		return convert26To10(columnAdr);
		
	}
	
	/**
	 * 获取行索引
	 * @param adr
	 * @return
	 */
	public static int getRowIndex(String adr){
		String rowAdr=getRowName(adr);
		return Integer.parseInt(rowAdr)-1;
	}
	
	/**
	 * 26进制转换为10进制
	 * @param str
	 * @return
	 */
	public static int convert26To10(String str){
		String sq="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		char[] letter=str.toCharArray();
		int reNum=0;
		int power=1;                  //用于次方算值
		int times=1;                  //最高位需要加1
		int num=letter.length;        //得到字符串个数
		reNum+=sq.indexOf(letter[num-1]);  //得到最后一个字母的尾数值
		//得到除最后一个字母的所有值，多于两位才执行这个函数
		if(num>=2){
			for(int i=num-1;i>0;i--){
				power=1;                                //置1，用于下一次循环使用次方计算
				for(int j=0;j<i;j++) power*=26;         //幂，j次方，应该有函数
					reNum+=(power*(sq.indexOf(letter[num-i-1])+times));//最高位需要加1，中间位数不需要加1
					times=0;
			}
		}
		return reNum;
	}
	
	/**
	 * 10进制转换为26进制
	 * @param value
	 * @return
	 */
	public static String convert10To26(int value){
		String sq="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		//此处判断输入的是否是正确的数字，略（正则表达式判断）
		StringBuilder sb=new StringBuilder();
		int remainder=value%26;
		int front=(value-remainder)/26;
		if(front<26){
			if(front>0) sb.append(sq.charAt(front-1));
			return sb.append(sq.charAt(remainder)).toString();
		}
		else return convert10To26(front)+sq.charAt(remainder);
	}
	
}
