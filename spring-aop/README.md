### 目标
* 掌握Spring AOP基本用法
* 使用Spring AOP完成日志记录功能

### 什么是AOP
AOP是Aspect Oriented Programming的缩写，意思是面向方面编程，AOP实际是GoF设计模式的延续

### 关于Spring AOP的一些术语
* 切面（Aspect）：在Spring AOP中，切面可以使用通用类或者在普通类中以@Aspect 注解（@AspectJ风格）来实现
* 连接点（Joinpoint）：在Spring AOP中一个连接点代表一个方法的执行
* 通知（Advice）：在切面的某个特定的连接点（Joinpoint）上执行的动作。通知有各种类型，其中包括"around"、"before”和"after"等通知。许多AOP框架，包括Spring，都是以拦截器做通知模型， 并维护一个以连接点为中心的拦截器链
* 切入点（Pointcut）：定义出一个或一组方法，当执行这些方法时可产生通知，Spring缺省使用AspectJ切入点语法。

### 通知类型
* 前置通知（@Before）：在某连接点（join point）之前执行的通知，但这个通知不能阻止连接点前的执行（除非它抛出一个异常）
* 返回后通知（@AfterReturning）：在某连接点（join point）正常完成后执行的通知：例如，一个方法没有抛出任何异常，正常返回
* 抛出异常后通知（@AfterThrowing）：方法抛出异常退出时执行的通知
* 后通知（@After）：当某连接点退出的时候执行的通知（不论是正常返回还是异常退出）
* 环绕通知（@Around）：包围一个连接点（join point）的通知，如方法调用。这是最强大的一种通知类型，环绕通知可以在方法调用前后完成自定义的行为，它也会选择是否继续执行连接点或直接返回它们自己的返回值或抛出异常来结束执行

``` code
Spring支持五种类型的通知：
Before(前)  org.apringframework.aop.MethodBeforeAdvice
After-returning(返回后) org.springframework.aop.AfterReturningAdvice
After-throwing(抛出后) org.springframework.aop.ThrowsAdvice
Arround(周围) org.aopaliance.intercept.MethodInterceptor
Introduction(引入) org.springframework.aop.IntroductionInterceptor
```

### @AspectJ风格的AOP配置
Spring AOP配置有两种风格：
* XML风格 = 采用声明形式实现Spring AOP
* AspectJ风格 = 采用注解形式实现Spring AOP