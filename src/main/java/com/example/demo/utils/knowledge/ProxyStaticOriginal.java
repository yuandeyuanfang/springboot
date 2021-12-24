package com.example.demo.utils.knowledge;

/**
 * 静态代理目标类
 */
public class ProxyStaticOriginal implements ProxyStaticInterface{
    @Override
    public void doSomeThing() {
        System.out.println("doSomeThing");
    }

}
