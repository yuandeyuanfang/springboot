package com.example.demo.utils.knowledge;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 静态代理代理类
 */
public class ProxyStaticProxy implements ProxyStaticInterface{
    private ProxyStaticOriginal proxyStaticOriginal;

    public ProxyStaticProxy(ProxyStaticOriginal proxyStaticOriginal) {
        this.proxyStaticOriginal = proxyStaticOriginal;
    }

    /**
     * 定义一个接口及其实现类；
     * 创建一个代理类同样实现这个接口
     * 将目标对象注注入进代理类，然后在代理类的对应方法调用目标类中的对应方法。这样的话，我们就可以通过代理类屏蔽对目标对象的访问，并且可以在目标方法执行前后做一些自己想做的事情。
     */
    public static void main(String[] args) {
        ProxyStaticProxy proxyStaticProxy = new ProxyStaticProxy(new ProxyStaticOriginal());
        proxyStaticProxy.doSomeThing();
    }

    @Override
    public void doSomeThing() {
        //调用方法之前，我们可以添加自己的操作
        beforeDoSomeThing();
        proxyStaticOriginal.doSomeThing();
        //调用方法之后，我们可以添加自己的操作
        afterDoSomeThing();
        List<String> a = new ArrayList<>();
        a.add("1");
        a.add("2");
        a.add("3");
        String[] sa = a.toArray(new String[5]);
System.out.println(sa.length);
        Iterator ai = a.iterator();
        while (ai.hasNext()){
            System.out.println(ai.next());
        }
        LinkedList l = new LinkedList();
        Collections.sort(a, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.length()-o2.length();
            }
        });
       Map m =  new HashMap<String,String>();
       m.put(null,"2");
        m.put(null,null);
        int ia =1;
        int ib =2;
       System.out.println((ia=ib));
        ConcurrentHashMap<String,String> cm = new ConcurrentHashMap();
        cm.put("null","null");
    }

    public void beforeDoSomeThing() {
        System.out.println("beforeDoSomeThing");
    }

    public void afterDoSomeThing() {
        System.out.println("afterDoSomeThing");
    }

}
