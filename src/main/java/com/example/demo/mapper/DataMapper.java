package com.example.demo.mapper;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataMapper {
    int testData();

    List<Object> testSql(String sql);

}