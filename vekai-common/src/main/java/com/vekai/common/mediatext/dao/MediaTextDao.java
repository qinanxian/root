package com.vekai.common.mediatext.dao;

import cn.fisok.sqloy.annotation.SqlParam;
import com.vekai.common.mediatext.entity.MediaTextEntity;
import cn.fisok.sqloy.annotation.SQLDao;

@SQLDao
public interface MediaTextDao {
    MediaTextEntity getMediaText(@SqlParam("mediaTextId") String mediaTextId);
}
