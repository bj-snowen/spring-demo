package com.vdrips.test.cglib;

import net.sf.cglib.proxy.CallbackFilter;

import java.lang.reflect.Method;

/**
 * Created by baixf on 2017/11/21.
 */
public class AuthProxyFilter implements CallbackFilter {

    public int accept(Method method) {
        if(!"query".equalsIgnoreCase(method.getName()))
            return 0;
        return 1;
    }

}
