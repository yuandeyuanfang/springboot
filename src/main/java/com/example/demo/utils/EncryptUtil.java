package com.example.demo.utils;

import org.jasypt.util.text.BasicTextEncryptor;

/**
 * 加密解密通用类
 */
public class EncryptUtil {

    public static void main(String[] args) {
        //springboot配置文件加密
        new EncryptUtil().EncryptProperties();
    }

    /**
     *springboot配置文件加密
     */
    public void EncryptProperties() {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        //加密所需的salt(盐)
        textEncryptor.setPassword("Y6M9fAJQdU7jNp5MN");
        //要加密的数据（数据库的用户名或密码）
        String username = textEncryptor.encrypt("gits901");
        String password = textEncryptor.encrypt("root123");
        System.out.println("username:"+username);
        System.out.println("password:"+password);
    }

}