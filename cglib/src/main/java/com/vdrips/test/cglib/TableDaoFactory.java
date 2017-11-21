package com.vdrips.test.cglib;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

/**
 * Created by baixf on 2017/11/21.
 * DAO工厂，用来生成DAO实例
 */
public class TableDaoFactory {

    private static TableDao tableDao = new TableDao();

    public static TableDao getInstance(){
        return tableDao;
    }

//    使用代理的实例生成方法
    public static TableDao getAuthInstance(AuthProxy authProxy){
        Enhancer en = new Enhancer();
        //进行代理
        en.setSuperclass(TableDao.class);
        en.setCallback(authProxy);
        //生成代理实例
        return (TableDao)en.create();
    }

//    新增一个使用了过滤器的实例生成方法

//    看到了吗setCallbacks中定义了所使用的拦截器，其中NoOp.INSTANCE是CGlib所提供的实际是一个没有任何操作的拦截器，
//    他们是有序的。一定要和CallbackFilter里面的顺序一致。明白了吗？上面return返回的就是返回的顺序。也就是说如果调用query方法就使用NoOp.INSTANCE进行拦截。
    public static TableDao getAuthInstanceByFilter(AuthProxy authProxy){
        Enhancer en = new Enhancer();
        en.setSuperclass(TableDao.class);
        en.setCallbacks(new Callback[]{authProxy, NoOp.INSTANCE});
        en.setCallbackFilter(new AuthProxyFilter());
        return (TableDao)en.create();
    }
}
