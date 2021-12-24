package com.example.demo.utils.knowledge;

/**
 * 动态代理目标类
 */
public class ProxyDynamicOriginal implements ProxyDynamicInterface{
    @Override
    public void doSomeThing() {
        System.out.println("doSomeThing");
    }

}
