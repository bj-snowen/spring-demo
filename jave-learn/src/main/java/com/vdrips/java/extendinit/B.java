package com.vdrips.java.extendinit;

/**
 * Created by baixf on 2018/7/25.
 */
public class B extends A {
    int c = C.prt(2);
    static int d = C.prt(3);

    B() {
        System.out.println("constructor of B");
    }

    @Override
    public void test() {
        System.out.println("begin B...");
    }

    public static void staticTest() {
        System.out.println("static begin B...");
    }
}
