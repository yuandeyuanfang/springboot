package com.example.demo.utils.knowledge;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * 设计模式相关知识
 */
public class DesignModeUtil {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        DesignModeUtil designModeUtil = new DesignModeUtil();
        Set<String> uniqueNames = new HashSet<>(Arrays.asList("Larry", "Steve", "James"));
        uniqueNames.forEach(a -> System.out.println(a+"1"));

        //垃圾回收测试
//        gcTest();
    }

    /**
     * 垃圾回收测试,jvm参数设置 -XX:+PrintGCDetails
     */
    public static void gcTest() {
        byte[] allocation1, allocation2;
        allocation1 = new byte[30900*1024];
//        allocation2 = new byte[9000*1024];
//        System.out.println(allocation1.length);
//        System.out.println(allocation2.length);
        new Thread(() -> {
            try {
                Socket socket = new Socket("127.0.0.1", 3333);
                while (true) {
                    try {
                        socket.getOutputStream().write((new Date() + ": hello world").getBytes());
                        Thread.sleep(2000);
                    } catch (Exception e) {
                    }
                }
            } catch (IOException e) {
            }
        }).start();
    }



}
