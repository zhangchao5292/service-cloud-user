package com.service.cloud.common.util;

public class JavaClassInitDemo {

    public static void main(String[] args) {
        System.out.println("main app run..");
        B b = new B();
//		B b = new B(22);
        b.methodA();
    }
}

class A {

    int a1 = 8;
    int a2 = getA2();

    {
        int a3 = 9;
        System.out.println("4-----top of A() a1=" + a1 + " a2=" + a2 + "  a3=" + a3);
    }

    public A() {
        this(66);
        System.out.print("A 构造函数\n---------6");
    }

    {
        System.out.println("below A()..has start--------5");
    }

    public A(int num) {
        System.out.print("7-------A 带参数构造函数: " + num + "\n");
    }

    static {
        System.out.println("I`m a static {} from class A..-------1");
    }

    int getA2() {
        System.out.println("getA2..----------3");
        return 7;
    }

    public void methodA() {
        System.out.println("methodA");
    }

}

class B extends A {

    int b1 = 0;
    int b2 = getB2();
    {
        int b3 = 5;
        System.out.println("9-------top of B() b1=" + b1 + " b2=" + b2 + " b3=" + b3);

    }

    public B() {
        this(33);
        // super(44);//添加super语句，会导致实例化时直接执行父类带参数的构造函数
        System.out.print("B 构造函数\n------12");
    }

    public B(int num) {
        // 添加super语句，会导致实例化时直接执行父类带参数的构造函数
        // 前提是带参数的构造函数B会被运行（new实例化或this）
        // super(77);

        System.out.print("B 带参数构造函数:" + num + "\n-----11");
    }

    {
        System.out.println("below B()..has start-------10");
    }
    static {
        System.out.println("I`m a static {} from class B..------2");
    }

    int getB2() {
        System.out.println("getB2..--------8");
        return 33;

    }

    @Override
    public void methodA() {
        System.out.println("methoaA int class B");
        super.methodA();

    }

}