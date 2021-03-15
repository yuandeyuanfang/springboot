package com.example.demo.utils.knowledge;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 动态代理代理类
 */
public class ProxyDynamicProxy implements InvocationHandler {
    private ProxyDynamicOriginal proxyDynamicOriginal;

    public ProxyDynamicProxy(ProxyDynamicOriginal proxyDynamicOriginal) {
        this.proxyDynamicOriginal = proxyDynamicOriginal;
    }

    /**
     * 定义一个接口及其实现类；
     * 自定义 InvocationHandler 并重写invoke方法，在 invoke 方法中我们会调用原生方法（被代理类的方法）并自定义一些处理逻辑；
     * 通过 Proxy.newProxyInstance(ClassLoader loader,Class<?>[] interfaces,InvocationHandler h) 方法创建代理对象；
     */
    public static void main(String[] args) {
        ProxyDynamicOriginal proxyDynamicOriginal = new ProxyDynamicOriginal();
        ProxyDynamicInterface proxyDynamicInterface = (ProxyDynamicInterface) Proxy.newProxyInstance(proxyDynamicOriginal.getClass().getClassLoader(),proxyDynamicOriginal.getClass().getInterfaces(),new ProxyDynamicProxy(proxyDynamicOriginal));
        proxyDynamicInterface.doSomeThing();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //调用方法之前，我们可以添加自己的操作
        beforeDoSomeThing();
        method.invoke(proxyDynamicOriginal,args);
        //调用方法之后，我们可以添加自己的操作
        afterDoSomeThing();
        return null;
    }

    public void beforeDoSomeThing() {
        System.out.println("beforeDoSomeThing");
    }

    public void afterDoSomeThing() {
        System.out.println("afterDoSomeThing");
    }


}
