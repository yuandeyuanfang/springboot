package com.example.demo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 日志文件解析
 */
@Data
public class ParseLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * sql执行时间
     */
    private Date sqlTime;

    /**
     * 解析出的sql语句
     */
    private String sqlStr;

    /**
     * 解析关键字
     */
    private String keyWord;

}