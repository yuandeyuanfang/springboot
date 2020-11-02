package com.example.demo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class TradeLogDTO implements Serializable {

    public static String RESULT_TRUE = "TRUE";
    public static String RESULT_FALSE = "FALSE";

    public static String MODEL_SYSTEM = "SYSTEM";//本系统
    public static String MODEL_OA = "OA";//OA
    public static String MODEL_SSM = "SSM";//自助机

    //OA
    public static String TYPE_OA_insertParamInfo = "OA_insertParamInfo";//新增参数

    private Long id;

    private String tradeId;//交易发送报文ID

    private String model;//交易渠道（THIRDSYSTEM-第三方系统）

    private String tradeType;//交易类型

    private String extra;//交易附加报文

    private String tradeResult;//交易结果

    private String tradeBegain;//交易开始时间

    private String tradeEnd;//交易结束时间

    private Long tradeCost;//交易花费时间（毫秒）

    private String tradeDate;//交易日期

    private String input;//输入报文

    private String output;//输出报文

    private long startTime;//开始毫秒数

    private String des;//解密后报文

}