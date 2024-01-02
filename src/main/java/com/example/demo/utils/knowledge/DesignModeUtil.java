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

        //单例模式
        getInstance();
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


    //单例模式
    //需要volatile关键字的原因是，在并发情况下，如果没有volatile关键字，在第5行会出现问题。instance = new TestInstance();可以分解为3行伪代码
    //上面的代码在编译运行时，可能会出现重排序从a-b-c排序为a-c-b。在多线程的情况下会出现以下问题。当线程A在执行第5行代码时，B线程进来执行到第2行代码。假设此时A执行的过程中发生了指令重排序，即先执行了a和c，没有执行b。那么由于A线程执行了c导致instance指向了一段地址，所以B线程判断instance不为null，会直接跳到第6行并返回一个未初始化的对象。
    private volatile static DesignModeUtil instance;

    /**
     * 单例模式
     * @return
     */
    public static DesignModeUtil getInstance() {
        if (instance == null) {
            synchronized (DesignModeUtil.class) {
                if (instance == null) {
                    instance = new DesignModeUtil();
                }
            }
        }
        return instance;
    }



}
