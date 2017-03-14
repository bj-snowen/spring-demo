## spring事件传播

![原理图](http://img.my.csdn.net/uploads/201212/26/1356523451_1229.png)

**当applicationContext.publishEvent(event)方法调用时，所有的ApplicationListener接口实现都会被激发，在每个ApplicationListener可以根据事件的类型判断是自己需要处理的事件。**

事件类：ActionEvent.Java
事件监听类：ActionListener.java
逻辑处理类：ActionLogic.java
还有一个Bean：PersonBean.java
测试类：TestMain.java

* 首先来看ActionEvent类：

```
package com.spring.test;

import org.springframework.context.ApplicationEvent;

public class ActionEvent extends ApplicationEvent{

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 646140097162842368L;

    public ActionEvent(Object source){
       super(source);
    }
}
```
这个类必须继承Spring的 ApplicationEvent,这里做为一个简单的例子，就用构造方法直接调用父类的构造方法就可以了。

* ActionListener类

```
package com.spring.test;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

public class ActionListener implements ApplicationListener{

    public void onApplicationEvent(ApplicationEvent event){
       if(event instanceof ActionEvent){
           PersonBean person = (PersonBean) event.getSource();
           System.out.println("Name:" + person.getName());
           System.out.println("Password:" + person.getPassword());
       }
    }
}
```

事件监听类实现了ApplicationListener接口，必须实现onApplicationEvent方法，在这个方法中，我们可以根据业务要求，根据
applicationContext.publishEvent(event)中发布的不同事件，进行相应的处理。

* 逻辑处理类ActionLogic

```
package com.spring.test;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ActionLogic implements ApplicationContextAware{

    private ApplicationContext applicationContext;

    /**
     * @param applicationContext the applicationContext to set
     */
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
       this.applicationContext = applicationContext;
    }
    public void execute(){
       PersonBean person = new PersonBean();
       person.setName("fengyun");
        person.setPassword("123456");
       ActionEvent event = new ActionEvent(person);

       this.applicationContext.publishEvent(event);

    }

}
```

必须实现ApplicationContextAware接口,这里最关键就是applicationContext的publishEvent方法，它起到发布事件，通知Listener的目的。

接下来就是简单的PersonBean了，里面只有2个属性，name和password，这里代码就不贴出来了。

* 测试类：TestMain

```
package com.spring.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class TestMain {

    /**
     * @param args
     */
    public static void main(String[] args) {
       // TODO Auto-generated method stub
       ApplicationContext ctx = new FileSystemXmlApplicationContext("applicationContext.xml");

       ActionLogic logic = (ActionLogic) ctx.getBean("actionLogic");

       logic.execute();
    }
}
```
最后就是配置文件了，很简单，只要在applicationContext.xml中定义ActionListener和ActionLogic就可以了。如下所示：

``` xml
<bean id="actionLogic" class="com.spring.test.ActionLogic"/>
<bean id="listener" class="com.spring.test.ActionListener"/>
```

运行TestMain就可以看到控制台打出以下结果：
```
Name:fengyun
Password:123456
```

我们可以看到，我们运行logic.execute()方法时，自动调用了ActionListener的onApplicationEvent方法。也许我们现在还觉得很奇怪，我们并没有调用ActionListener类啊，这是因为在ApplicationContext会自动在当前所有的Bean中寻找ApplicationListener接口的实现，并将其作为事件接收的对象。当applicationContext.publishEvent(event)方法调用时，所有的ApplicationListener接口实现都会被激发，在每个ApplicationListener可以根据事件的类型判断是自己需要处理的事件。这就是我们在ActionListener的onApplicationEvent方法中的如下处理：
```
if(event instanceof ActionEvent){
           PersonBean person = (PersonBean) event.getSource();
           System.out.println("Name:" + person.getName());
           System.out.println("Password:" + person.getPassword());
}
```

判断event是不是自己发布的事件，如果是，则进行相应的处理。在onApplicationEvent中可以得到在ActionEvent中传入的对象person, 这个对象可以根据我们的业务处理，传入不同的对象了。

这就是一个简单的事件传播处理机制，我们可以用他来进行对某些事件的监听，比如当某个Bean发生异常, 数据库连接异常等等，我们只要定义自己的Event，和实现一个ApplicationListener的接口，然后根据不同的业务进行相应的处理，使用Spring的事件传播机制就可以很简单的实现了。

在以上内容中最重要的就是要理解，当applicationContext.publishEvent(event)方法调用时，所有的ApplicationListener接口实现都会被激发，在每个ApplicationListener可以根据事件的类型判断是自己需要处理的事件。

