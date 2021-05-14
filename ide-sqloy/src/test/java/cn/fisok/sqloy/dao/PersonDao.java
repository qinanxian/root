package cn.fisok.sqloy.dao;

import cn.fisok.sqloy.annotation.*;
import cn.fisok.sqloy.entity.DemoPerson;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

//@SQLDao("classpath:abc/def/xyz.sql.md")
@SQLDao
public interface PersonDao {

    DemoPerson getPerson(@Param("id") Long id);

    List<DemoPerson> getPersonList(@Param("gender") String gender);

    @Select("SELECT * FROM DEMO_PERSON WHERE CODE=:code")
    DemoPerson getPersonByCode(String code);

    @Select("SELECT * FROM DEMO_PERSON WHERE NAME=:name")
    DemoPerson getPersonByName(@SqlParam("name") String name);

    @Insert
    int insertPerson(DemoPerson demoPerson);

    @Update("UPDATE DEMO_PERSON SET HEIGHT=:height WHERE ID=:id")
    int updatePersonHeight(@SqlParam("height") int height, @SqlParam("id") Long id);

    @Update("UPDATE DEMO_PERSON SET GRADUATED_FROM=:graduatedFrom WHERE ID=:id")
    int updatePersonGraduatedFrom(@SqlParam("graduatedFrom") String graduatedFrom, @SqlParam("id") Long id);

    @Update
    int updatePersonBirth(@SqlParam("birth") Date birth, @SqlParam("id") Long id);

    @Update
    int updatePerson(DemoPerson demoPerson);

    @Save
    int savePerson(DemoPerson demoPerson);

    @Delete("DELETE FROM DEMO_PERSON WHERE ID=:id")
    int deletePerson(@SqlParam("id") Long id);
}
