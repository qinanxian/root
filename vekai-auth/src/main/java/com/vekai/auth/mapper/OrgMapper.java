package com.vekai.auth.mapper;

import com.vekai.auth.entity.Org;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * Created by tisir<yangsong158@qq.com> on 2017-05-12
 */
@Repository
public interface OrgMapper{

    @Select("SELECT * FROM AUTH_ORG WHERE ID = #{orgId}")
    Org selectOne(String orgId);
}
