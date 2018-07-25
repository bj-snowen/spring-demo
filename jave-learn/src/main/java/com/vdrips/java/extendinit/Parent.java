package com.vdrips.java.extendinit;

/**
 * Created by baixf on 2018/7/25.
 */
public class Parent {

    private int i = 2;

    public Parent(){
        displayPublic();
        displayPrivate();
    }

    public void displayPublic() {
        System.out.println("public parent i=" + i);
    }

    private void displayPrivate() {
        System.out.println("private parent i=" + i);
    }

}
