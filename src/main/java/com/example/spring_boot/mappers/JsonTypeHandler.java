package com.example.spring_boot.mappers;

import com.example.spring_boot.models.Course;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(Course.class)
public class JsonTypeHandler implements TypeHandler {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, Object o, JdbcType jdbcType) throws SQLException {
        try {
            preparedStatement.setString(i, objectMapper.writeValueAsString(o));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Course getResult(ResultSet resultSet, String s) throws SQLException {
        try {
            String val = resultSet.getString(s);
            if (val == null) return null;
            return objectMapper.readValue(val, Course.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Course getResult(ResultSet resultSet, int i) throws SQLException {
        try {
            String val = resultSet.getString(i);
            if (val == null) return null;
            return objectMapper.readValue(val, Course.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Course getResult(CallableStatement callableStatement, int i) throws SQLException {
        try {
            String val = callableStatement.getString(i);
            if (val == null) return null;
            return objectMapper.readValue(val, Course.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
