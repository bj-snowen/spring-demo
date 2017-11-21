CGlib是什么？  CGlib是一个强大的,高性能,高质量的Code生成类库。它可以在运行期扩展Java类与实现Java接口。  当然这些实际的功能是asm所提供的，asm又是什么？Java字节码操控框架，具体是什么大家可以上网查一查，毕竟我们这里所要讨论的是cglib，  cglib就是封装了asm，简化了asm的操作，实现了在运行期动态生成新的class。  可能大家还感觉不到它的强大，现在就告诉你。  实际上CGlib为spring aop提供了底层的一种实现;为hibernate使用cglib动态生成VO/PO (接口层对象)。   下面我们将通过一个具体的事例来看一下CGlib体验一下CGlib。  * CGlib 2.13  * ASM 2.23  以一个实例在简单介绍下cglib的应用。  我们模拟一个虚拟的场景，模拟对表的操作。  1. 开始我们对表提供了CRUD方法。  我们现在创建一个对Table操作的DAO类。  
Java代码  
	1.	public class TableDAO {  
	2.	    public void create(){  
	3.	        System.out.println("create() is running !");  
	4.	    }  
	5.	    public void query(){  
	6.	        System.out.println("query() is running !");  
	7.	    }  
	8.	    public void update(){  
	9.	        System.out.println("update() is running !");  
	10.	    }  
	11.	    public void delete(){  
	12.	        System.out.println("delete() is running !");  
	13.	    }  
	14.	}  
 OK，它就是一个javaBean，提供了CRUD方法的javaBean。  下面我们创建一个DAO工厂，用来生成DAO实例。  
Java代码  
	1.	public class TableDAOFactory {  
	2.	    private static TableDAO tDao = new TableDAO();  
	3.	    public static TableDAO getInstance(){  
	4.	        return tDao;  
	5.	    }  
	6.	}  
 接下来我们创建客户端，用来调用CRUD方法。  
Java代码  
	1.	public class Client {  
	2.	  
	3.	    public static void main(String[] args) {  
	4.	        TableDAO tableDao = TableDAOFactory.getInstance();  
	5.	        doMethod(tableDao);  
	6.	    }  
	7.	    public static void doMethod(TableDAO dao){  
	8.	        dao.create();  
	9.	        dao.query();  
	10.	        dao.update();  
	11.	        dao.delete();  
	12.	    }  
	13.	}  
 OK，完成了，CRUD方法完全被调用了。当然这里并没有CGlib的任何内容。问题不会这么简单的就结束，新的需求来临了。  2. 变化随之而来，Boss告诉我们这些方法不能开放给用户，只有“张三”才有权使用。阿~！怎么办，难道我们要在每个方法上面进行判断吗？  好像这么做也太那啥了吧，对了对了Proxy可能是最好的解决办法。jdk的代理就可以解决了。 好了我们来动手改造吧。等等jdk的代理需要实现接口，这样，  我们的dao类需要改变了。既然不想改动dao又要使用代理，我们这就请出CGlib。  我们只需新增一个权限验证的方法拦截器。  
Java代码  
	1.	public class AuthProxy implements MethodInterceptor {  
	2.	    private String name ;  
	3.	    //传入用户名称  
	4.	    public AuthProxy(String name){  
	5.	        this.name = name;  
	6.	    }  
	7.	    public Object intercept(Object arg0, Method arg1, Object[] arg2,  
	8.	            MethodProxy arg3) throws Throwable {  
	9.	        //用户进行判断  
	10.	        if(!"张三".equals(name)){  
	11.	            System.out.println("你没有权限！");  
	12.	            return null;  
	13.	        }  
	14.	        return arg3.invokeSuper(arg0, arg2);  
	15.	    }  
	16.	}  
 当然不能忘了对我们的dao工厂进行修改，我们提供一个使用代理的实例生成方法  
Java代码  
	1.	public static TableDAO getAuthInstance(AuthProxy authProxy){  
	2.	    Enhancer en = new Enhancer();  
	3.	    //进行代理  
	4.	    en.setSuperclass(TableDAO.class);  
	5.	    en.setCallback(authProxy);  
	6.	    //生成代理实例  
	7.	    return (TableDAO)en.create();  
	8.	}  
 我们这就可以看看客户端的实现了。添加了两个方法用来验证不同用户的权限。  
Java代码  
	1.	public static void haveAuth(){  
	2.	    TableDAO tDao = TableDAOFactory.getAuthInstance(new AuthProxy("张三"));  
	3.	    doMethod(tDao);  
	4.	}  
	5.	public static void haveNoAuth(){  
	6.	    TableDAO tDao = TableDAOFactory.getAuthInstance(new AuthProxy("李四"));  
	7.	    doMethod(tDao);  
	8.	}  
 OK,"张三"的正常执行，"李四"的没有执行。  看到了吗？简单的aop就这样实现了  难道就这样结束了么？  3. Boss又来训话了，不行不行，现在除了"张三"其他人都用不了了，现在不可以这样。他们都来向我反映了，必须使用开放查询功能。  哈哈，现在可难不倒我们了，因为我们使用了CGlib。当然最简单的方式是去修改我们的方法拦截器，不过这样会使逻辑变得复杂，且  不利于维护。还好CGlib给我们提供了方法过滤器（CallbackFilter）,CallbackFilte可以明确表明，被代理的类中不同的方法，  被哪个拦截器所拦截。下面我们就来做个过滤器用来过滤query方法。  
Java代码  
	1.	public class AuthProxyFilter implements CallbackFilter{  
	2.	    public int accept(Method arg0) {  
	3.	        if(!"query".equalsIgnoreCase(arg0.getName()))  
	4.	            return 0;  
	5.	        return 1;  
	6.	    }  
	7.	  
	8.	}  
 OK,可能大家会对return 0 or 1感到困惑，用到的时候就会讲解，当然下面就会用到了。  我们在工场中新增一个使用了过滤器的实例生成方法。  
Java代码  
	1.	public static TableDAO getAuthInstanceByFilter(AuthProxy authProxy){  
	2.	    Enhancer en = new Enhancer();  
	3.	    en.setSuperclass(TableDAO.class);  
	4.	    en.setCallbacks(new Callback[]{authProxy,NoOp.INSTANCE});  
	5.	    en.setCallbackFilter(new AuthProxyFilter());  
	6.	    return (TableDAO)en.create();  
	7.	}  
 看到了吗setCallbacks中定义了所使用的拦截器，其中NoOp.INSTANCE是CGlib所提供的实际是一个没有任何操作的拦截器，  他们是有序的。一定要和CallbackFilter里面的顺序一致。明白了吗？上面return返回的就是返回的顺序。也就是说如果调用query方法就使用NoOp.INSTANCE进行拦截。  现在看一下客户端代码。  
Java代码  
	1.	public static void haveAuthByFilter(){  
	2.	    TableDAO tDao = TableDAOFactory.getAuthInstanceByFilter(new AuthProxy("张三"));  
	3.	    doMethod(tDao);  
	4.	  
	5.	    tDao = TableDAOFactory.getAuthInstanceByFilter(new AuthProxy("李四"));  
	6.	    doMethod(tDao);  
	7.	}  
 ok,现在"李四"也可以使用query方法了，其他方法仍然没有权限。  哈哈，当然这个代理的实现没有任何侵入性，无需强制让dao去实现接口。 


