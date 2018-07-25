package com.vdrips.java.extendinit;

/**
 * Created by baixf on 2018/7/25.
 */
public class OrderTest {
    public static void main(String[] args) {
        B b = new B();
//        A b = new B();
        b.test();
        b.staticTest();//调用各自的静态方法
    }
}
