package com.vekai.office.word.parameter;

/**
 * 图片参数
 *
 * @author yangsong
 * @since 2014年7月31日
 */
public class ImageParameter extends WordParameter<EmbedImage>{

	public ImageParameter(String name, EmbedImage value) {
		super(name, value);
	}

	private static final long serialVersionUID = 1332204556450924614L;

	public String getStringValue() {
		return null;
	}

}
