package com.vekai.auth.mapper;


import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;

public interface RolePermitMapper {

    @Delete("DELETE FROM AUTH_ROLE_PERMIT WHERE ROLE_ID = #{roleId}")
    void deleteRoleOwnedPermit(String roleId);

//    @Insert()
//    void insertRolePermit(String roleId, String )
}
