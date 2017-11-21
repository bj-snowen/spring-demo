package com.vdrips.test.cglib;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by baixf on 2017/11/21.
 * 权限验证的方法拦截器
 */
public class AuthProxy implements MethodInterceptor {

    private String name ;
    //传入用户名称
    public AuthProxy(String name){
        this.name = name;
    }

    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        //用户进行判断
        if(!"张三".equals(name)){
            System.out.println("你没有权限！");
            return null;
        }

        return methodProxy.invokeSuper(o, objects);
    }
}
