package com.example.demo.vo;

import lombok.Data;

/**
 * 通用返回类型
 * @param <T>
 */
@Data
public class ResultVO<T> {
    private T result;
    private boolean isSuccess = false;
    private String code;
    private String resultDes;

}

