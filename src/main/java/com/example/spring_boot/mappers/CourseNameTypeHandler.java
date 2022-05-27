package com.example.spring_boot.mappers;

import com.example.spring_boot.models.Course;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CourseNameTypeHandler implements TypeHandler {
    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, Object o, JdbcType jdbcType) throws SQLException {
        Course course = (Course) o;
        preparedStatement.setString(i, course.getName());
    }

    @Override
    public Object getResult(ResultSet resultSet, String s) {
        return null;
    }

    @Override
    public Object getResult(ResultSet resultSet, int i) {
        return null;
    }

    @Override
    public Object getResult(CallableStatement callableStatement, int i) {
        return null;
    }
}
