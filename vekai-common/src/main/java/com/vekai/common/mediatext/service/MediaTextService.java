package com.vekai.common.mediatext.service;

import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.common.mediatext.dao.MediaTextDao;
import com.vekai.common.mediatext.entity.MediaTextEntity;
import cn.fisok.raw.kit.ValidateKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MediaTextService {
    @Autowired
    protected MediaTextDao mediaTextDao;
    @Autowired
    BeanCruder beanCruder;

    public MediaTextEntity getMediaText(String mediaTextId) {
        return mediaTextDao.getMediaText(mediaTextId);
    }

    public int saveMediaText(MediaTextEntity mediaTextEntity){
        return beanCruder.save(mediaTextEntity);
    }

    public int updateMediaTextContent(String mediaTextId,String contentRaw,String contentData){
        MediaTextEntity entity = getMediaText(mediaTextId);
        ValidateKit.notNull(entity,"媒体文本{0}不存在",mediaTextId);
        entity.setContentRaw(contentRaw);
        entity.setContentData(contentData);
        //清除掉，让系统自动填充
        entity.setUpdatedBy(null);
        entity.setUpdatedTime(null);
        return saveMediaText(entity);
    }
}
