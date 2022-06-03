package com.example.spring_boot.mappers;

import com.example.spring_boot.models.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO users VALUES (#{name}, #{password}, #{roles})")
    void saveUser(User user);

    @Select("SELECT * FROM users WHERE name = #{name}")
    User getByNameUser(String name);
}
