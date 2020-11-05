package com.example.demo.vo;

import lombok.Data;

@Data
public class ParamInfoVO {

    private String paramName;//参数名

    private String paramValue;//参数值

    private String paramMemo;//参数描述

    private int paramInuse;//是否启用1：启用0：禁用

}