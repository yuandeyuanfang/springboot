package com.example.demo.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 资源密集型程序模拟,提高程序的CPU和内存使用率
 * 1. 模拟CPU负载：通过不断执行计算操作来占用CPU资源。
 * 2. 模拟内存使用：分配指定大小的内存块，模拟内存占用。
 * 3. 配置文件支持：通过读取配置文件来动态调整CPU和内存使用量。
 */
public class ResourceIntensiveProgram {

    private static int cpuLoadPercentage = 15; // 默认CPU负载百分比
    private static int memoryUsageMB = 100;    // 默认内存使用量（MB）
    private static final String CONFIG_FILE_PATH = "/usr/config.properties"; // 配置文件路径
//nohup java -Xmx3048m -Xms1024m ResourceIntensiveProgram  > apl.log 2>&1 &
//nohup java ResourceIntensiveProgram  > apl.log 2>&1 &
    //javac  -encoding UTF-8  ResourceIntensiveProgram.java

//    cpuLoadPercentage=70
//    memoryUsageMB=200
    public static void main(String[] args) throws InterruptedException {
        // 启动定时任务，每隔1秒读取配置文件
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(ResourceIntensiveProgram::loadConfig, 0, 1, TimeUnit.MINUTES);

        // 模拟CPU和内存占用
        while (true) {
            DateFormat df = new SimpleDateFormat("HH");
            int hour = Integer.parseInt(df.format(new Date()));
            System.out.println(hour);
            if(hour >= 18 || hour <= 8){
                simulateCpuLoad();
                simulateMemoryUsage();
            }else{
                Thread.sleep(60000);
            }

        }
    }

    /**
     * 从配置文件加载CPU和内存使用率配置
     */
    private static void loadConfig() {
        Properties props = new Properties();
        File configFile = new File(CONFIG_FILE_PATH);

        if (configFile.exists()) {
            try (FileInputStream fis = new FileInputStream(configFile)) {
                props.load(fis);
                cpuLoadPercentage = Integer.parseInt(props.getProperty("cpuLoadPercentage", "15"));
                memoryUsageMB = Integer.parseInt(props.getProperty("memoryUsageMB", "100"));
                System.out.println("配置已更新: CPU负载=" + cpuLoadPercentage + "%, 内存使用=" + memoryUsageMB + "MB");
            } catch (IOException e) {
                System.err.println("读取配置文件失败: " + e.getMessage());
            }
        } else {
            System.err.println("配置文件不存在: " + CONFIG_FILE_PATH);
        }
    }

    /**
     * 模拟CPU负载
     */
    private static void simulateCpuLoad() {
        long startTime = System.currentTimeMillis();
        // 根据配置的CPU负载百分比计算需要占用的时间
        long busyTime = (long) (100 * cpuLoadPercentage / 100.0);
        long idleTime = 100 - busyTime;

        while (System.currentTimeMillis() - startTime < busyTime) {
            // 模拟CPU计算
            double result = Math.sqrt(Math.random() * Math.random());
        }

        try {
            if(cpuLoadPercentage <= 15){
                Thread.sleep(idleTime*10);
            }
            Thread.sleep(idleTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 模拟内存使用
     */
    private static void simulateMemoryUsage() {
        // 分配指定大小的内存
        byte[] memoryBlock = new byte[memoryUsageMB * 1024 * 1024];
        System.out.println("已分配内存: " + memoryUsageMB + "MB");

        // 模拟内存使用
        for (int i = 0; i < memoryBlock.length; i++) {
            memoryBlock[i] = (byte) (i % 256);
        }
    }
}