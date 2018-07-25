package com.vdrips.java.extendinit;

/**
 * Created by baixf on 2018/7/25.
 */
public class Child extends Parent {
    private int i = 22;

    public Child() {
        i = 222;
    }

    @Override
    public void displayPublic() {
        System.out.println("child public parent i=" + i);
    }

    private void displayPrivate() {
        System.out.println("child private parent i=" + i);
    }

}
