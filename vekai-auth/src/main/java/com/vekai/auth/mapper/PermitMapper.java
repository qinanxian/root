package com.vekai.auth.mapper;


import com.vekai.auth.entity.Permit;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * The mapper is named PermitMapper, and all the methods in the mapper return {@link Permit} Set.
 * It has historical reasons, and for changing java codes as few as possible, the mapper name is not modified,
 * and the return type is not modified to String Set.
 */
public interface PermitMapper {

    //==================================== special ============================================
    @Select("SELECT UP.PERMIT_CODE AS code FROM AUTH_USER_PERMIT UP WHERE UP.USER_ID = #{userId} " +
    "UNION SELECT RP.PERMIT_CODE FROM AUTH_ROLE_PERMIT RP INNER JOIN AUTH_USER_ROLE UR " +
    "ON RP.ROLE_ID = UR.ROLE_ID WHERE UR.USER_ID = #{userId}")
    Set<Permit> selectAllPermissionsByUserId(String userId);

    @Select("SELECT RP.PERMIT_CODE AS code FROM AUTH_ROLE_PERMIT RP WHERE RP.ROLE_ID = #{roleId}")
    Set<Permit> selectPermissionsByRoleId(String roleId);

    @Select("SELECT RP.PERMIT_CODE AS code FROM AUTH_ROLE_PERMIT RP INNER JOIN AUTH_ROLE R " +
            "ON RP.ROLE_ID = R.ID WHERE R.CODE = #{roleCode}")
    Set<Permit> selectPermissionsByRoleCode(String roleCode);
    //========================================================================================


    @Select("SELECT ID, CODE, NAME FROM AUTH_PERMIT")
    List<Permit> selectAllUserDefinedPermits();

}
