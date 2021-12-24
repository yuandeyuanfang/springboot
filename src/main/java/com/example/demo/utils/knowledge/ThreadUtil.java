package com.example.demo.utils.knowledge;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 线程相关知识
 */
public class ThreadUtil {

    //可重入锁
    static ReentrantLock lock = new ReentrantLock();

    //ThreadLocal用法
    private static ThreadLocal<SimpleDateFormat>  simpleDateFormat = new ThreadLocal<SimpleDateFormat>(){
        @Override
        protected SimpleDateFormat initialValue()
        {
            return new SimpleDateFormat("yyyyMMdd HHmm");
        }
    };

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ThreadUtil threadUtil = new ThreadUtil();

        //同一实例同一时刻只能有一个同步实例方法在执行，但可以有一个同步实例方法和一个同步类方法同时执行；ThreadLocal用法示例
//        ThreadMethA threadMethA = new ThreadMethA();
//        threadMethA.setThreadUtil(threadUtil);
//        Thread threadSyncMethA = new Thread(threadMethA);
//        threadSyncMethA.start();
//        ThreadMethB threadMethB = new ThreadMethB();
//        threadMethB.setThreadUtil(threadUtil);
//        Thread threadSyncMethB = new Thread(threadMethB);
//        threadSyncMethB.start();
//        ThreadMethC threadMethC = new ThreadMethC();
//        threadMethC.setThreadUtil(threadUtil);
//        Thread threadSyncMethC = new Thread(threadMethC);
//        threadSyncMethC.start();

        //打印java线程信息
//        printThread();

        //创建线程的几种方式
//        threadUtil.createThread();

        //线程池创建线程，
//        threadUtil.ThreadPoolExecutorDemo();

        //CountDownLatch使用实例
//        threadUtil.countDownLatchTest();

        //ReentrantLock测试
//        Thread reentrantLockThreadA = new Thread(new ReentrantLockThread());
//        reentrantLockThreadA.start();
//        Thread reentrantLockThreadB = new Thread(new ReentrantLockThread());
//        reentrantLockThreadB.start();
//        Thread reentrantLockThreadC = new Thread(new ReentrantLockThreadExample(threadUtil));
//        reentrantLockThreadC.start();
//        Thread reentrantLockThreadD = new Thread(new ReentrantLockThreadExample(threadUtil));
//        reentrantLockThreadD.start();

        StringBuffer a = new StringBuffer("xyz");
        StringBuffer b = a;
        System.out.println(" a = " + a);
        System.out.println(" b = " + b);

        a.append("qwe");
        System.out.println(" a = " + a);
        System.out.println(" b = " + b);
    }

    /**
     * 打印java线程信息
     */
    public static void printThread() {
        // 获取 Java 线程管理 MXBean
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        // 不需要获取同步的 monitor 和 synchronizer 信息，仅获取线程和线程堆栈信息
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(false, false);
        // 遍历线程信息，仅打印线程 ID 和线程名称信息
        for (ThreadInfo threadInfo : threadInfos) {
            System.out.println("[" + threadInfo.getThreadId() + "] " + threadInfo.getThreadName());
        }
    }

    /**
     * 线程A
     */
    static class ThreadMethA implements Runnable{
        public void setThreadUtil(ThreadUtil threadUtil) {
            this.threadUtil = threadUtil;
        }

        ThreadUtil threadUtil;
        @Override
        public void run() {
            threadUtil.syncMethA();
        }

    }

    /**
     * 线程B
     */
    static class ThreadMethB implements Runnable{
        public void setThreadUtil(ThreadUtil threadUtil) {
            this.threadUtil = threadUtil;
        }

        ThreadUtil threadUtil;
        @Override
        public void run() {
            threadUtil.syncMethB();
        }

    }

    /**
     * 线程C
     */
    static class ThreadMethC implements Runnable{
        public void setThreadUtil(ThreadUtil threadUtil) {
            this.threadUtil = threadUtil;
        }

        ThreadUtil threadUtil;
        @Override
        public void run() {
            threadUtil.syncMethC();
        }

    }

    /**
     *方法A-同步方法
     */
    public static synchronized void syncMethA(){
        System.out.println("syncMethA-start");
        simpleDateFormat.set(new SimpleDateFormat("yyyyMMdd"));
        try {
            Thread.sleep(5000);
            System.out.println(simpleDateFormat.get().format(new Date()));
            simpleDateFormat.remove();
            System.out.println(simpleDateFormat.get().format(new Date()));
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("syncMethA-end");
    }

    /**
     *方法B-同步方法
     */
    public synchronized void syncMethB(){
        System.out.println("syncMethB-start");
        simpleDateFormat.set(new SimpleDateFormat("yyyy:MM:dd"));
        try {
            Thread.sleep(3000);
            System.out.println(simpleDateFormat.get().format(new Date()));
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("syncMethB-end");
    }

    /**
     *方法C-同步代码块
     */
    public void syncMethC(){
        synchronized (this.getClass()){
            System.out.println("syncMethC-start");
            simpleDateFormat.set(new SimpleDateFormat("yyyy:MM:dd"));
            try {
                Thread.sleep(4000);
                System.out.println(simpleDateFormat.get().format(new Date()));
            }catch (Exception e){
                e.printStackTrace();
            }
            System.out.println("syncMethC-end");
        }
    }

    /**
     * 测试线程池
     */
    class MyRunnable implements Runnable {

        private String command;

        public MyRunnable(String s) {
            this.command = s;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " Start. Time = " + new Date()+"-"+command);
            processCommand();
            System.out.println(Thread.currentThread().getName() + " End. Time = " + new Date()+"-"+command);
        }

        private void processCommand() {
            try {
                Thread.sleep(Integer.parseInt(command)*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String toString() {
            return this.command;
        }
    }

    /**
     * 测试线程池Callable
     */
    class MyCallable implements Callable {

        private String command;

        public MyCallable(String s) {
            this.command = s;
        }

        @Override
        public String call() {
            System.out.println(Thread.currentThread().getName() + " Start. Time = " + new Date()+"-"+command);
            processCommand();
            System.out.println(Thread.currentThread().getName() + " End. Time = " + new Date()+"-"+command);
            return command;
        }

        private void processCommand() {
            try {
                Thread.sleep(Integer.parseInt(command)*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String toString() {
            return this.command;
        }
    }

    private static final int CORE_POOL_SIZE = 5;
    private static final int MAX_POOL_SIZE = 10;
    private static final int QUEUE_CAPACITY = 100;
    private static final Long KEEP_ALIVE_TIME = 1L;

    /**
     * 通过线程池创建线程
     */
    public void ThreadPoolExecutorDemo() throws ExecutionException, InterruptedException {
        //使用阿里巴巴推荐的创建线程池的方式
        //通过ThreadPoolExecutor构造函数自定义参数创建
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,new LinkedBlockingDeque<Runnable>(QUEUE_CAPACITY),new ThreadPoolExecutor.CallerRunsPolicy());

        for (int i = 0; i < 10; i++) {
            //创建WorkerThread对象（WorkerThread类实现了Runnable 接口）
            Runnable worker = new MyRunnable("" + (i+1));
            //执行Runnable
            executor.execute(worker);
        }
        //终止线程池
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        System.out.println("Finished all threads");

        executor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,new LinkedBlockingDeque<Runnable>(QUEUE_CAPACITY),new ThreadPoolExecutor.CallerRunsPolicy());
        List<FutureTask<String>> futureTaskList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            //创建WorkerThread对象（WorkerThread类实现了Runnable 接口）
            Callable<String> worker = new MyCallable("" + (i+1));
            //执行Runnable
            FutureTask<String> futureTask = (FutureTask<String>) executor.submit(worker);
            futureTaskList.add(futureTask);
        }
        for (FutureTask<String> futureTask: futureTaskList) {
            System.out.println(futureTask.get());
        }
        //终止线程池
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        System.out.println("Finished all threads");
    }

    /**
     * CountDownLatch使用实例
     */
    class Worker implements Runnable {
        private final CountDownLatch startSignal;
        private final CountDownLatch doneSignal;

        Worker(CountDownLatch startSignal, CountDownLatch doneSignal) {
            this.startSignal = startSignal;
            this.doneSignal = doneSignal;
        }

        public void run() {
            try {
                startSignal.await();
                System.out.println("doWork……");
                doneSignal.countDown();//线程执行完毕，计数器减一
            } catch (InterruptedException ex) {
            }
        }
    }

    /**
     * CountDownLatch使用实例
     */
    public void countDownLatchTest() throws InterruptedException {
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(5);

        for (int i = 0; i < 5; ++i) // create and start threads
            new Thread(new Worker(startSignal, doneSignal)).start();
        System.out.println("doSomethingElse……");
        Thread.sleep(1000);
        startSignal.countDown();      // let all threads proceed
        Thread.sleep(1000);
        System.out.println("doSomethingElse……");
        doneSignal.await();           // wait for all to finish
        Thread.sleep(1000);
        System.out.println("doneSignal finish");
    }

    /**
     * 创建线程的几种方式
     */
    public void createThread(){

    }

    /**
     * ReentrantLock测试-类方法
     */
    static class ReentrantLockThread implements Runnable {

        public void run() {
            try {
                reentrantLockTest();
            } catch (InterruptedException ex) {
            }
        }
    }

    /**
     * ReentrantLock测试-实例方法
     */
    static class ReentrantLockThreadExample implements Runnable {
        ReentrantLockThreadExample(ThreadUtil threadUtil){
            this.threadUtil = threadUtil;
        }

        private ThreadUtil threadUtil;

        public void run() {
            try {
                threadUtil.reentrantLockTestExample();
            } catch (InterruptedException ex) {
            }
        }
    }

    /**
     * ReentrantLock测试-类方法
     */
    public static void reentrantLockTest() throws InterruptedException {
        System.out.println("ReentrantLock start...");
        lock.lock();
        System.out.println("ReentrantLock lock...");
        Thread.sleep(5000);
        lock.unlock();
        System.out.println("ReentrantLock end...");
    }

    /**
     * ReentrantLock测试-实例方法
     */
    public void reentrantLockTestExample() throws InterruptedException {
        System.out.println("reentrantLockTestExample start...");
        lock.lock();
        System.out.println("reentrantLockTestExample lock...");
        Thread.sleep(5000);
        lock.unlock();
        System.out.println("reentrantLockTestExample end...");
    }

}
