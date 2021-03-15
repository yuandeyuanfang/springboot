package com.example.demo.utils.knowledge;

import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * JVM相关知识
 */
public class JvmUtil {

    static final int a = 0;
    public final static PrintStream out = null;

    public static void main(String[] args) throws InterruptedException, ExecutionException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        JvmUtil jvmUtil = new JvmUtil();
//        Set<String> uniqueNames = new HashSet<>(Arrays.asList("Larry", "Steve", "James"));
//        uniqueNames.forEach(a -> System.out.println(a+"1"));
//        new SimpleDateFormat("");

        //垃圾回收测试
//        gcTest();
        List<String> list1 = new ArrayList<String>();
        list1.add("abc");

        ArrayList<Integer> list2 = new ArrayList<Integer>();
        list2.add(123);

        System.out.println(list1.getClass());
        System.in.read();
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
