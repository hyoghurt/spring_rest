package com.example.spring_boot.mappers;

import com.example.spring_boot.models.Student;
import org.apache.ibatis.annotations.*;

@Mapper
public interface StudentMapper {

    @Results(id = "studentResultMap", value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "name", column = "name"),
            @Result(property = "age", column = "age"),
            @Result(property = "timeFrom", column = "time_from"),
            @Result(property = "timeTo", column = "time_to"),
            @Result(property = "course", column = "course")
    })
    @Select("SELECT * FROM students WHERE id = #{id}")
    public Student findById(Long id);

    @ResultMap("studentResultMap")
    @Insert("INSERT INTO students(name, age, time_from, time_to, course) VALUES (#{name}, #{age}, #{timeFrom}, #{timeTo}, #{course} FORMAT JSON)")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public void create(Student student);

    @ResultMap("studentResultMap")
    @Update("UPDATE students SET name=#{name}, age=#{age}, time_from=#{timeFrom}, time_to=#{timeTo}, course=#{course} FORMAT JSON WHERE id=#{id}")
    public void update(Student student);

    @ResultMap("studentResultMap")
    @Delete("DELETE FROM students WHERE id=#{id}")
    public void delete(Long id);
}
