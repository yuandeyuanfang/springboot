package com.example.demo.utils.knowledge;

/**
 *枚举
 */
public enum EnumDemo {
    PAYSTATUS0(0,"未支付"),
    PAYSTATUS1(1,"支付中"),
    PAYSTATUS2(2,"支付成功"),
    PAYSTATUS3(3,"退款中"),
    PAYSTATUS4(4,"已退款"),
    PAYSTATUS5(5,"支付失败"),
    PAYSTATUS6(6,"退款失败"),
    PAYSTATUS9(9,"作废");

    public int val; //值
    public String txt; //描述

    EnumDemo(int val, String txt){
        this.val = val;
        this.txt = txt;
    }

}
