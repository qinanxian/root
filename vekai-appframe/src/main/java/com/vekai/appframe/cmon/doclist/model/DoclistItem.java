package com.vekai.appframe.cmon.doclist.model;

import com.vekai.appframe.cmon.doclist.entity.CmonDoclistItemPO;

import java.util.List;

public class DoclistItem extends CmonDoclistItemPO {

    /**
     * 文件查看码列表，根据文件查看码，生成文件查看URL地址
     */
    List<String> fileViewCodeList = null;

    public List<String> getFileViewCodeList() {
        return fileViewCodeList;
    }

    public void setFileViewCodeList(List<String> fileViewCodeList) {
        this.fileViewCodeList = fileViewCodeList;
    }
}
