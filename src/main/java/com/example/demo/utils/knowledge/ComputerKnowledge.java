package com.example.demo.utils.knowledge;

import java.util.Arrays;

/**
 * 计算机相关知识
 */
public class ComputerKnowledge {
    public static void main(String[] args) {
        ComputerKnowledge computerKnowledge = new ComputerKnowledge();
        //左位移
        computerKnowledge.leftShift();
        //右位移
        computerKnowledge.rightShift();
        //无符号右位移
        computerKnowledge.unsignedRightShift();

        //float（单精度浮点数）类型转二进制
        System.out.println(computerKnowledge.floatToBinaryString(3.2f));
        //double（双精度浮点数）类型转二进制
        System.out.println(computerKnowledge.doubleToBinaryString(1.2));
        //二进制转float（单精度浮点数）类型
        System.out.println(computerKnowledge.floatByBinaryString("111111111111111111111111111111"));
        System.out.println(computerKnowledge.floatByBinaryString("111111100000000000000000000000"));
        System.out.println(computerKnowledge.floatByBinaryString("111111100000000000000000000011"));
        //二进制转double（双精度浮点数）类型
        System.out.println(computerKnowledge.doubleByBinaryString("11111111110011001100110011001100110011001100110011001100110011"));
        System.out.println(computerKnowledge.doubleByBinaryString("11111111110000000000000000000000100000000000000000000000000000"));
        System.out.println(computerKnowledge.doubleByBinaryString("11111111110000000000000000000001100000000000000000000000000000"));

        int[] numbers = new int[]{102, 338, 62, 9132, 580, 666};
        Arrays.sort(numbers);
    }

    /**
     * 左位移，补码向左移动，右边补0
     */
    public void leftShift(){
        int a = 1;
        System.out.println("正数"+a+"向左位移1位："+(a << 1));
        System.out.println("正数"+a+"向左位移3位："+(a << 3));
        a = -1;
        System.out.println("负数"+a+"向左位移1位："+(a << 1));
        System.out.println("负数"+a+"向左位移3位："+(a << 3));
    }

    /**
     * 右位移，补码向右移动，正数左边补0，负数左边补1
     */
    public void rightShift(){
        int a = 1;
        System.out.println("正数"+a+"向右位移1位："+(a >> 1));
        System.out.println("正数"+a+"向右位移3位："+(a >> 3));
        a = -1;
        System.out.println("负数"+a+"向右位移1位："+(a >> 1));
        System.out.println("负数"+a+"向右位移3位："+(a >> 3));
    }

    /**
     * 无符号右位移，补码向右移动，左边补0
     */
    public void unsignedRightShift(){
        int a = 1;
        System.out.println("正数"+a+"无符号向右位移1位："+(a >>> 1));
        System.out.println("正数"+a+"无符号向右位移3位："+(a >>> 3));
        a = -1;
        System.out.println("负数"+a+"无符号向右位移1位："+(a >>> 1));
        System.out.println("负数"+a+"无符号向右位移3位："+(a >>> 3));
    }

    /**
     * float（单精度浮点数）类型转二进制，返回结果32位（有可能不足32位，左边补0即可；1位符号位，8位指数位，23位小数位）
     * @param num
     * @return
     */
    public String floatToBinaryString(float num){
        return Integer.toBinaryString(Float.floatToIntBits(num));
    }

    /**
     * double（双精度浮点数）类型转二进制，返回结果64位（有可能不足64位，左边补0即可；1位符号位，11位指数位，52位小数位）
     * @param num
     * @return
     */
    public String doubleToBinaryString(double num){
        return Long.toBinaryString(Double.doubleToLongBits(num));
    }

    /**
     * 二进制转float（单精度浮点数）类型
     * @param num
     * @return
     */
    public float floatByBinaryString(String num){
        return Float.intBitsToFloat(Integer.valueOf(num,2));
    }

    /**
     * 二进制转double（双精度浮点数）类型
     * @param num
     * @return
     */
    public double doubleByBinaryString(String num){
        return Double.longBitsToDouble(Long.valueOf(num,2));
    }

}
