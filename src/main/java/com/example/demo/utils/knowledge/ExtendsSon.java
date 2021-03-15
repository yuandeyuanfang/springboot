package com.example.demo.utils.knowledge;

public class ExtendsSon extends ExtendsFather{
    private int privateProperty;

    public static void main(String[] args) {
        ExtendsSon extendsSon = new ExtendsSon();
        extendsSon.privateMethod();
        extendsSon.protectedMethod();
        extendsSon.publicMethod();
        extendsSon.staticMethod();
        new ExtendsFather().protectedMethod();
        ExtendsFather extendsFather = new ExtendsSon();
        extendsFather.publicMethod();
        extendsFather.staticMethod();
    }

    public void privateMethod(){
        System.out.println("privateMethod-son");
    }

    @Override
    public void publicMethod(){
        System.out.println("publicMethod-son");
    }

    public static void staticMethod(){
        System.out.println("staticMethod-son");
    }

}
