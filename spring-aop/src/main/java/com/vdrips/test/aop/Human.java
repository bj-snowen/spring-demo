package com.vdrips.test.aop;

/**
 * Created by baixf on 2017/3/14.
 */
public class Human implements Sleepable {

    @Override
    public void sleep() {
        System.out.println("人睡觉");
    }
}
