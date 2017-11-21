package com.vdrips.test.cglib;

/**
 * Created by baixf on 2017/11/21.
 * CGlib是一个强大的,高性能,高质量的Code生成类库。它可以在运行期扩展Java类与实现Java接口。cglib就是封装了asm，简化了asm的操作，实现了在运行期动态生成新的class。
 * 实际上CGlib为spring aop提供了底层的一种实现;为hibernate使用cglib动态生成VO/PO (接口层对象)。
 * 创建客户端，用来调用CRUD方法
 */
public class Client {

    public static void main(String[] args){
        TableDao tableDao = TableDaoFactory.getInstance();

//        doMethod(tableDao);

        //Boss告诉我们这些方法不能开放给用户，只有“张三”才有权使用。阿~！怎么办，难道我们要在每个方法上面进行判断吗？
//        haveAuth();
//        haveNoAuth();


//        Boss又来训话了，不行不行，现在除了"张三"其他人都用不了了，现在不可以这样。他们都来向我反映了，必须使用开放查询功能。
//        哈哈，现在可难不倒我们了，因为我们使用了CGlib。当然最简单的方式是去修改我们的方法拦截器，不过这样会使逻辑变得复杂，且
//        不利于维护。还好CGlib给我们提供了方法过滤器（CallbackFilter）,CallbackFilte可以明确表明，被代理的类中不同的方法，
//        被哪个拦截器所拦截。下面我们就来做个过滤器用来过滤query方法。
        haveAuthByFilter();
//        ,现在"李四"也可以使用query方法了，其他方法仍然没有权限
//        当然这个代理的实现没有任何侵入性，无需强制让dao去实现接口。
    }

    public static void doMethod(TableDao tableDao){
        tableDao.create();
        tableDao.query();
        tableDao.update();
        tableDao.delete();
    }

//   添加了方法用来验证不同用户的权限
    public static void haveAuth(){
        System.out.println("用户 张三");
        TableDao tDao = TableDaoFactory.getAuthInstance(new AuthProxy("张三"));
        doMethod(tDao);
//        "张三"的正常执行
    }
//  添加了方法用来验证不同用户的权限
    public static void haveNoAuth(){
        System.out.println("用户 李四");
        TableDao tDao = TableDaoFactory.getAuthInstance(new AuthProxy("李四"));
        doMethod(tDao);
//        "李四"的没有执行
    }

    public static void haveAuthByFilter(){
        TableDao tDao = TableDaoFactory.getAuthInstanceByFilter(new AuthProxy("张三"));
        doMethod(tDao);

        tDao = TableDaoFactory.getAuthInstanceByFilter(new AuthProxy("李四"));
        doMethod(tDao);
    }
}
