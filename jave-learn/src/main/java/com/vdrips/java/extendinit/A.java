package com.vdrips.java.extendinit;

/**
 * Created by baixf on 2018/7/25.
 */
public class A {
    int a = C.prt(0);
    static int b = C.prt(1);

    A() {
        System.out.println("constructor of A");
    }

    public void test() {
        System.out.println("begin A...");
    }

    public static void staticTest() {
        System.out.println("static begin A...");
    }
}
