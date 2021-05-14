package com.vekai.workflow.model.enums;

/**
 * 流程资源类型
 * @author 杨松 <syang@amarsoft.com>
 * @date 20170105
 */
public enum TaskResourceType {
    Button,Link,Fieldset,Tab;
    
    public static boolean isDefined(String type){
		for(TaskResourceType typeEnum : TaskResourceType.values()){
			if(typeEnum.name().equals(type)){
				return true;
			}
		}
		return false;
	}
}
