package com.example.demo.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Slf4j
public class Scheduler {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    /**
     * 每隔3分钟执行一次
     */
    @Scheduled(fixedRate = 180000)
    public void JvmInfoTask() {
        log.info("每三分钟执行一次,date={}", dateFormat.format(new Date()));
        // 虚拟机内存情况查询
        int byteToMb = 1024 * 1024;
        Runtime rt = Runtime.getRuntime();
        long vmTotal = rt.totalMemory() / byteToMb;
        long vmFree = rt.freeMemory() / byteToMb;
        long vmMax = rt.maxMemory() / byteToMb;
        long vmUse = vmTotal - vmFree;
        log.info("JVM内存已用的空间为：" + vmUse + " MB");
        log.info("JVM内存的空闲空间为：" + vmFree + " MB");
        log.info("JVM总内存空间为：" + vmTotal + " MB");
        log.info("JVM最大内存空间为：" + vmMax + " MB");
    }

}