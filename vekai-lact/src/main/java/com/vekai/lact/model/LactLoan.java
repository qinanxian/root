package com.vekai.lact.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vekai.lact.entity.LactLoanEntity;
import com.vekai.lact.exception.LactException;
import cn.fisok.raw.kit.ListKit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 核算主业务对象
 */
public class LactLoan extends LactLoanEntity implements Serializable,Cloneable{

    protected List<LactLoanSegment> segmentList = new ArrayList<LactLoanSegment>();

    public List<LactLoanSegment> getSegmentList() {
        return segmentList;
    }

    public void setSegmentList(List<LactLoanSegment> segmentList) {
        this.segmentList = segmentList;
    }

    public Integer getSegments() {
        return segmentList!=null?segmentList.size():0;
    }

    public void setSegments(Integer segments) {
        super.setSegments(getSegments());
    }

    public void setOnlyOneSegment(LactLoanSegment segment){
        segmentList = ListKit.listOf(segment);
    }

    /**
     * 仅有一段的情况下，通过方法直接获取
     * @return
     */
    @JsonIgnore
    public LactLoanSegment getOnlyOneSegment(){
        if(segmentList!=null&&segmentList.size()==1){
            return segmentList.get(0);
        }else if(segmentList!=null&&segmentList.size()>=1){
            throw new LactException("数据异常，调用此方法，要求分段数量必需有且仅有一段，目前存在段数为:{0}",getSegments());
        }else{
            throw new LactException("数据异常，调用此方法，要求分段数量必需有且仅有一段，目前没有分段数据");
        }
    }


}
