package com.vekai.auth.mapper;

import com.vekai.auth.entity.Role;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Created by tisir<yangsong158@qq.com> on 2017-05-08
 */
@Repository
//@Mapper
public interface RoleMapper {
    public int insert(Role role);

    public int deleteById(String id);

    public int update(Role user);

    public Role selectById(String id);

    public Role selectByCode(String code);

    //@Select("select R.* from AUTH_ROLE R where exists(select 1 from AUTH_USER_ROLE UR where R.ID=UR.ROLE_ID and UR.USER_ID=#{userId}) order by CODE asc")
    @Select("select R.* from AUTH_ROLE R inner join AUTH_USER_ROLE UR on R.ID = UR.ROLE_ID where UR.USER_ID=#{userId} order by CODE")
//    @ResultMap("RoleResultMap")
    public Set<Role> selectRolesByUser(String userId);
}
