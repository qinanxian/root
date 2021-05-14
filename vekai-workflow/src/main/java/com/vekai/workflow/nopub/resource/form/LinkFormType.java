package com.vekai.workflow.nopub.resource.form;

import org.activiti.engine.form.AbstractFormType;

public class LinkFormType extends AbstractFormType {
	private static final long serialVersionUID = 1888507742562774929L;

	public String getName() {
		return "Link";
	}

	public String getMimeType() {
		return "text/plain";
	}

	public Object convertFormValueToModelValue(String propertyValue) {
		return propertyValue;
	}

	public String convertModelValueToFormValue(Object modelValue) {
		return (String) modelValue;
	}

}
