package com.vdrips.java.extendinit;

/**
 * Created by baixf on 2018/7/25.
 */
public class Test {

    public static void main(String[] args) {
        new Child();

//        打印结果 ：
//        child public parent i=0
//        private parent i=2
        //说明：new 子类，先到父类里边，执行父类的 i =2, 父类的构造方法，但是display会走向子类child的display，
        // 子类的变量i此时还没有加载，默认值为0，所以输出child = 0
    }
}
