package com.example.demo.utils.knowledge;

public class ExtendsFather {
    private int privateProperty;

    public static void main(String[] args) {
        ExtendsFather extendsFather = new ExtendsFather();
        System.out.println(EnumDemo.PAYSTATUS0.val);
        System.out.println(EnumDemo.PAYSTATUS0.txt);
    }

    private void privateMethod(){
        System.out.println("privateMethod");
    }

    public void publicMethod(){
        System.out.println("publicMethod");
    }

    protected void protectedMethod(){
        System.out.println("protectedMethod");
    }

    public static void staticMethod(){
        System.out.println("staticMethod");
    }

}
