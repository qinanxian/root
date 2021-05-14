package com.vekai.auth.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Created by tisir<yangsong158@qq.com> on 2017-05-14
 */
@Repository
//@Mapper
public interface UserRoleMapper  {

    public int insert(Map<String, Object> userRole);

    public int delete(Map<String, Object> userRole);

    @Delete("delete from AUTH_USER_ROLE where USER_ID=${userId} and ROLE_ID=${roleId}")
    public int delete(@Param("userId") String userId, @Param("roleId") String roleId);

    @Select("select ifnull(max(ID),0) as MAX_ID from AUTH_USER_ROLE")
    @ResultType(Long.class)
    public int selectMaxId();

    public int selectCountByUserIdAndRoleId(@Param("userId") String userId, @Param("roleId") String roleId);
    public int selectCountByUserCodeAndRoleCode(@Param("userCode") String userCode, @Param("roleCode") String roleCode);

}
