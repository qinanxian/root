package com.vekai.base.detector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 检查结果消息
 */
public class DetectorMessage implements Serializable {
    /**
     * 是否通过
     */
    private boolean pass = false;

    /**
     * 消息容器
     */
    private List<String> message = null;

    /**
     * 构造函数，执行初始化工作
     */
    public DetectorMessage(){
        message = new ArrayList<String>();
        pass = false;
    }

    /**
     * 添加消息
     * @param str 消息内容
     */
    public void put(String str){
        message.add(str);
    }

    /**
     * 复位
     */
    public void reset(){
        this.pass = false;
        this.clear();
    }
    /**
     * 清除消息内容
     */
    public void clear(){
        message.clear();
    }

    public boolean isPass() {
        return pass;
    }

    public void setPass(boolean pass) {
        this.pass = pass;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }
}
