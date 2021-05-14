package com.vekai.auth.mapper;

import com.vekai.auth.entity.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

/**
 * Created by tisir<yangsong158@qq.com> on 2017-05-07
 */
@Repository
//@Mapper
public interface UserMapper {
    @Insert("insert into AUTH_USER(ID," +
            "CODE," +
            "NAME," +
            "AVATAR," +
            "ORG_ID," +
            "EMAIL," +
            "PHONE," +
            "STATUS," +
            "REVISION,CREATED_BY,CREATED_TIME,UPDATED_BY,UPDATED_TIME) " +
            "values(" +
            "#{id}," +
            "#{code}," +
            "#{name}," +
            "#{avatar}," +
            "#{orgId}," +
            "#{email}," +
            "#{phone}," +
            "#{status}," +
            "#{revision},#{createdBy},#{createdTime},#{updatedBy},#{updatedTime})")
    public int insert(User user);

    public int update(User user);

    @Update("update AUTH_USER set PASSWORD=#{password} where ID=#{id}")
    public int updatePassword(@Param("id") String id, @Param("password") String password);

    @Update("update AUTH_USER set PASSWORD=#{password} AND HASH_SALT = #{hashSalt} where ID=#{id}")
    public int updateSaltedPwd(@Param("id") String id, @Param("password") String password, String hashSalt);

    @Delete("delete from AUTH_USER where ID = #{id}")
    public int deleteById(String id);

    @Select("select * from AUTH_USER where ID = #{id}")
    @Options(useCache = true, timeout = 10000)
    @Results(value = {
            @Result(id = true, property = "id", column = "ID", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "code", column = "CODE"),
            @Result(property = "name", column = "NAME"),
            @Result(property = "password", column = "PASSWORD"),
            @Result(property = "avatar", column = "AVATAR"),
            @Result(property = "orgId", column = "ORG_ID"),
            @Result(property = "email", column = "EMAIL"),
            @Result(property = "phone", column = "PHONE"),
            @Result(property = "status", column = "STATUS"),
            @Result(property = "revision", column = "REVISION"),
            @Result(property = "createdBy", column = "CREATED_BY"),
            @Result(property = "createdTime", column = "CREATED_TIME"),
            @Result(property = "updatedBy", column = "UPDATED_BY"),
            @Result(property = "updatedTime", column = "UPDATED_TIME"),
    })
//    @ResultMap("com.vekai.auth.mapper.UserMapper.UserMap")
    @ResultMap("UserMap")
    public User selectById(String id);

    @Select("select * from AUTH_USER where CODE = #{code}")
//    @ResultMap("com.vekai.auth.mapper.UserMapper.UserResultMap")
    @ResultMap("UserMap")
    public User selectByCode(String code);

}
