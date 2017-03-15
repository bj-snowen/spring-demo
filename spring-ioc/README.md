IOC 控制反转 spring框架核心基于ioc原理

### 什么是控制反转？

控制反转是一种将组件依赖关系的创建和管理置于程序外部的技术。由容器控制程序之间的关系，而不是由代码直接控制，由于控制权由代码转向了容器，所以称为反转

对象与对象之间的关系可以简单的理解为对象之间的依赖关系：
依赖关系：在 A 类需要类 B 的一个实例来进行某些操作，比如在类 A 的方法中需要调用类 B 的方法来完成功能，叫做 A 类依赖于 B 类。

一个需要特定的依赖的组件一般会涉及一个依赖对象，在 IOC 的概念中叫做目标 (target) 。换句话说， IOC 提供了这样的服务，使一个组件能够在它的整个生命周期中访问它的依赖和服务，用这种方法与它的依赖进行交互。总的来说， _IOC 能够被分解为两种子类型_：依赖注入和依赖查找。

## (1)依赖查找
比如使用 JNDI 注册一个数据库连接池的示例中，代码中从注册处获得依赖关系的 JNDI 查找 (JNDI lookups) ：

```
initContext = new InitialContext();
// 获取数据源
DataSource ds = (DataSource) initContext.lookup("java:comp/env/jdbc/mysql");
```

## (2) 依赖注入
(Dependency Injection) 依赖注入：两个对象之间的依赖关系在程序运行时由外部容器动态的注入依赖行为方式称为依赖注入 (DI) 。 DI 是 IOC 的一种形式。

IoC 在应用开发中是一个非常有力的概念。如 Martin Flower 所述， IoC 的一种表现形式就是依赖性注射。依赖性注射用的是好莱坞原则， " 不要找我，我会找你的。 " 。换句来说，你的类不会去查找或是实例化它们所依赖的类。控制恰好是反过来的，某种容器会设置这种依赖关系。使用 IoC 常常使代码更加简洁，并且为相互依赖的类提供一种很好的方法。

### 依赖注入的三种实现类型：接口注入、 Setter注入、构造器注入。
#### <1> 接口注入 (Type1)
```
public class ClassA {
private InterfaceB clzB;
public void doSomething() {
Ojbect obj =Class.forName(Config.BImplementation).newInstance();
clzB = (InterfaceB)obj;
clzB.doIt()
}
……
}
```
上面的代码中， ClassA 依赖于 InterfaceB 的实现，如何获得 InterfaceB 实现类的实例？传统的方法是在代码中创建 InterfaceB 实现类的实例，并将起赋予 clzB 。
而这样一来， ClassA 在编译期即依赖于 InterfaceB 的实现。为了将调用者与实现者在编译期分离，于是有了下面的代码.
我们根据预先在配置文件中设定的实现类的类名 (Config.BImplementation) ，动态加载实现类，并通过 InterfaceB 强制转型后为 ClassA 所用。这就是接口注入的一个最原始的雏形。
而对于一个 Type1 型 IOC 容器而言，加载接口实现并创建其实例的工作由容器完成，见如下代码。

```
public class ClassA {
private InterfaceB clzB;
public Object doSomething(InterfaceB b) {
clzB = b;
return clzB.doIt();
}
    ……
}
```

在运行期， InterfaceB 实例将由容器提供。
Type1 型 IOC 发展较早（有意或无意），在实际中得到了普遍应用，即使在 IOC 的概念尚未确立时，这样的方法也已经频繁出现在我们的代码中。
下面的代码大家应该非常熟悉：

```
public class MyServlet extends HttpServlet {
public void doGet(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {     …… }   }
```
这也是一个 Type1 型注入， HttpServletRequest 和 HttpServletResponse 实例由 Servlet Container 在运行期动态注入。
#### <2>Setter( 设值 ) 注入 (Type2)
各种类型的依赖注入模式中，设值注入模式在实际开发中得到了最广泛的应用。
```
public class ClassA {
private InterfaceB clzB;
public void setClzB(InterfaceB clzB) {
this. clzB = clzB;   }
……
}
```
#### <3> 构造器注入 (Type3)
依赖关系是通过类构造函数建立的，容器通过调用类的构造方法将其所需的依赖关系注入其中
```
public class DIByConstructor {   private final DataSource dataSource;
public DIByConstructor(DataSource ds) {
this.dataSource = ds;
    }
……
}
```
#### <4> 三种依赖注入方式的比较
接口注入：
接口注入模式因为历史较为悠久，在很多容器中都已经得到应用。但由于其在灵活性、易用性上不如其他两种注入模式，因而在 IOC 的专题世界内并不被看好。

Setter 注入：
对于习惯了传统 javabean 开发的程序员，通过 setter 方法设定依赖关系更加直观。
如果依赖关系较为复杂，那么构造子注入模式的构造函数也会相当庞大，而此时设值注入模式则更为简洁。
如果用到了第三方类库，可能要求我们的组件提供一个默认的构造函数，此时构造子注入模式也不适用。

构造器注入：
在构造期间完成一个完整的、合法的对象。
所有依赖关系在构造函数中集中呈现。
依赖关系在构造时由容器一次性设定，组件被创建之后一直处于相对“不变”的稳定状态。
只有组件的创建者关心其内部依赖关系，对调用者而言，该依赖关系处于“黑盒”之中。

## 总结：
可见， Type3 和 Type2 模式各有千秋，而 spring 、 PicoContainer 都对 Type3 和 Type2 类型的依赖注入机制提供了良好支持。这也就为我们提供了更多的选择余地。理论上，以 Type3 类型为主，辅之以 Type2 类型机制作为补充，可以达到最好的依赖注入效果，不过对于基于 Spring Framework 开发的应用而言， Type2 使用更加广泛。

