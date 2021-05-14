package com.vekai.office.word.parameter;

import java.io.Serializable;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

/**
 * 内嵌图片,主要用于图片替换标签的处理
 *
 * @author yangsong
 * @since 2014年7月31日
 */
public class EmbedImage implements Serializable{

	private static final long serialVersionUID = -9184546764671182449L;

	private int width;
	private int height;
	private int type;
	private byte[] data;
	
	/**
	 * 获取图片宽度
	 * @return
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * 设置图片宽度
	 * @param width
	 */
	public void setWidth(int width) {
		this.width = width;
	}
	
	/**
	 * 获取图片高度
	 * @return
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * 设置图片高度
	 * @param height
	 */
	public void setHeight(int height) {
		this.height = height;
	}
	
	/**
	 * 获取图片类型<图片类型值，参考:XWPFDocument.PICTURE_TYPE_XXX>
	 * @return
	 */
	public int getType() {
		return type;
	}
	
	/**
	 * 获取图片二进制数据
	 * @return
	 */
	public byte[] getData() {
		return data;
	}
	
	/**
	 * 设置图片二进制数据
	 * @param data
	 */
	public void setData(byte[] data) {
		this.data = data;
	}
	
	/**
	 * 获取图片大小，单位为字节
	 * @return
	 */
	public int size(){
		if(data==null)return 0;
		return data.length;
	}
	
	/**
	 * 设置图片类型，取得对应的图片类型代码
	 * @param picType
	 * @return int
	 */
	public void setPicType(String suffix){
		type = XWPFDocument.PICTURE_TYPE_PICT;
		if(suffix != null){
			if(suffix.equalsIgnoreCase("png")){
				type = XWPFDocument.PICTURE_TYPE_PNG;
			}else if(suffix.equalsIgnoreCase("dib")){
				type = XWPFDocument.PICTURE_TYPE_DIB;
			}else if(suffix.equalsIgnoreCase("emf")){
				type = XWPFDocument.PICTURE_TYPE_EMF;
			}else if(suffix.equalsIgnoreCase("jpg") || suffix.equalsIgnoreCase("jpeg")){
				type = XWPFDocument.PICTURE_TYPE_JPEG;
			}else if(suffix.equalsIgnoreCase("wmf")){
				type = XWPFDocument.PICTURE_TYPE_WMF;
			}else if(suffix.equalsIgnoreCase("gif")){
				type = XWPFDocument.PICTURE_TYPE_GIF;
			}
		}
	}
}
