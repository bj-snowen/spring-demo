# 简介
Logback，一个“可靠、通用、快速而又灵活的Java日志框架”
要在工程里面使用logback需要以下jar文件：

```
slf4j-api-1.6.1.jar
logback-access-0.9.29.jar
logback-classic-0.9.29.jar
logback-core-0.9.29.jar
```

## 在工程src/resource目录下建立logback.xml  

1. logback首先会试着查找logback.groovy文件;
2. 当没有找到时，继续试着查找logback-test.xml文件;
3. 当没有找到时，继续试着查找logback.xml文件;
4. 如果仍然没有找到，则使用默认配置（打印到控制台）  

> org.slf4j.LoggerFactory实例化logger   

## logback.xml说明

* **根节点<configuration>包含的属性：**  

scan:
当此属性设置为true时，配置文件如果发生改变，将会被重新加载，默认值为true。  

scanPeriod:
设置监测配置文件是否有修改的时间间隔，如果没有给出时间单位，默认单位是毫秒。当scan为true时，此属性生效。默认的时间间隔为1分钟。  

debug:
当此属性设置为true时，将打印出logback内部日志信息，实时查看logback运行状态。默认值为false。  

```
<configuration scan="true" scanPeriod="60 seconds" debug="false">  
      <!-- 其他配置省略-->  
</configuration>
```
* **根节点<configuration>的子节点：**  

1. 设置上下文名称：\<contextName>  
2. 设置变量： \<property> 定义变量后，可以使“${}”来使用变量。  
3. 获取时间戳字符串：\<timestamp>

```
<configuration scan="true" scanPeriod="60 seconds" debug="false"> 
	<property name="APP_Name" value="myAppName" />  
   <timestamp key="bySecond" datePattern="yyyyMMdd'T'HHmmss"/>   
   <contextName>${bySecond}</contextName>  
    <!-- 其他配置省略-->  
</configuration>  
```
*  **设置loger**  

<loger>
用来设置某一个包或者具体的某一个类的日志打印级别、以及指定<appender>。<loger>仅有一个name属性，一个可选的level和一个可选的addtivity属性。
name:
用来指定受此loger约束的某一个包或者具体的某一个类。
level:
用来设置打印级别，大小写无关：TRACE, DEBUG, INFO, WARN, ERROR, ALL 和 OFF，还有一个特俗值INHERITED或者同义词NULL，代表强制执行上级的级别。
如果未设置此属性，那么当前loger将会继承上级的级别。
addtivity:
是否向上级loger传递打印信息。默认是true。
<loger>可以包含零个或多个<appender-ref>元素，标识这个appender将会添加到这个loger。
 
<root>
也是<loger>元素，但是它是根loger。只有一个level属性，应为已经被命名为"root".
level:
用来设置打印级别，大小写无关：TRACE, DEBUG, INFO, WARN, ERROR, ALL 和 OFF，不能设置为INHERITED或者同义词NULL。
默认是DEBUG。
<root>可以包含零个或多个<appender-ref>元素，标识这个appender将会添加到这个loger。

```
1	<configuration>   
2	   <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">   
3	    <!-- encoder 默认配置为PatternLayoutEncoder -->   
4	    <encoder>   
5	      <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>   
6	    </encoder>   
7	  </appender>   
8	   
9	  <!-- logback为java中的包 -->   
10	  <logger name="logback"/>   
11	  <!--logback.LogbackDemo：类的全路径 -->   
12	  <logger name="logback.LogbackDemo" level="INFO" additivity="false">  
13	    <appender-ref ref="STDOUT"/>  
14	  </logger>   
15	    
16	  <root level="ERROR">             
17	    <appender-ref ref="STDOUT" />   
18	  </root>     
19	</configuration> 
```


* **appender**  

\<appender>：
\<appender>是\<configuration>的子节点，是负责写日志的组件。
\<appender>有两个必要属性name和class。name指定appender名称，class指定appender的全限定名。


1.ConsoleAppender:
把日志添加到控制台，有以下子节点：
\<encoder>：对日志进行格式化。（具体参数稍后讲解 ）
\<target>：字符串 System.out 或者 System.err ，默认 System.out;

2.FileAppender:
把日志添加到文件，有以下子节点：
\<file>：被写入的文件名，可以是相对目录，也可以是绝对目录，如果上级目录不存在会自动创建，没有默认值。
\<append>：如果是 true，日志被追加到文件结尾，如果是 false，清空现存文件，默认是true。
\<encoder>：对记录事件进行格式化。（具体参数稍后讲解 ）
\<prudent>：如果是 true，日志会被安全的写入文件，即使其他的FileAppender也在向此文件做写入操作，效率低，默认是 false。

3.RollingFileAppender:
滚动记录文件，先将日志记录到指定文件，当符合某个条件时，将日志记录到其他文件。有以下子节点：
\<file>：被写入的文件名，可以是相对目录，也可以是绝对目录，如果上级目录不存在会自动创建，没有默认值。
\<append>：如果是 true，日志被追加到文件结尾，如果是 false，清空现存文件，默认是true。
\<encoder>：对记录事件进行格式化。（具体参数稍后讲解 ）
\<rollingPolicy>:当发生滚动时，决定 RollingFileAppender 的行为，涉及文件移动和重命名。
\<triggeringPolicy >: 告知 RollingFileAppender 合适激活滚动。
\<prudent>：当为true时，不支持FixedWindowRollingPolicy。支持TimeBasedRollingPolicy，但是有两个限制，1不支持也不允许文件压缩，2不能设置file属性，必须留空。
 
rollingPolicy：
 
TimeBasedRollingPolicy： 最常用的滚动策略，它根据时间来制定滚动策略，既负责滚动也负责出发滚动。有以下子节点：
\<fileNamePattern>:
必要节点，包含文件名及“%d”转换符， “%d”可以包含一个 java.text.SimpleDateFormat指定的时间格式，如：%d{yyyy-MM}。如果直接使用 %d，默认格式是 yyyy-MM-dd。 RollingFileAppender 的file字节点可有可无，通过设置file，可以为活动文件和归档文件指定不同位置，当前日志总是记录到file指定的文件（活动文件），活动文件的名字不会改变；如果没设置file，活动文件的名字会根据fileNamePattern 的值，每隔一段时间改变一次。“/”或者“\”会被当做目录分隔符。
 
\<maxHistory>:
可选节点，控制保留的归档文件的最大数量，超出数量就删除旧文件。假设设置每个月滚动，且 \<maxHistory>是6，则只保存最近6个月的文件，删除之前的旧文件。注意，删除旧文件是，那些为了归档而创建的目录也会被删除。
 
 
FixedWindowRollingPolicy： 根据固定窗口算法重命名文件的滚动策略。有以下子节点：
\<minIndex>:窗口索引最小值
\<maxIndex>:窗口索引最大值，当用户指定的窗口过大时，会自动将窗口设置为12。
\<fileNamePattern >:
必须包含“%i”例如，假设最小值和最大值分别为1和2，命名模式为 mylog%i.log,会产生归档文件mylog1.log和mylog2.log。还可以指定文件压缩选项，例如，mylog%i.log.gz 或者 没有log%i.log.zip
 
 
triggeringPolicy:
 
SizeBasedTriggeringPolicy： 查看当前活动文件的大小，如果超过指定大小会告知 RollingFileAppender 触发当前活动文件滚动。只有一个节点:
\<maxFileSize>:这是活动文件的大小，默认值是10MB。

\<encoder>：
负责两件事，一是把日志信息转换成字节数组，二是把字节数组写入到输出流。
目前PatternLayoutEncoder 是唯一有用的且默认的encoder ，有一个<pattern>节点，用来设置日志的输入格式。使用“%”加“转换符”方式，如果要输出“%”，则必须用“\”对“\%”进行转义。  


* **\<filter>:**  

过滤器，执行一个过滤器会有返回个枚举值，即DENY，NEUTRAL，ACCEPT其中之一。返回DENY，日志将立即被抛弃不再经过其他过滤器；返回NEUTRAL，有序列表里的下个过滤器过接着处理日志；返回ACCEPT，日志会被立即处理，不再经过剩余过滤器。
过滤器被添加到\<Appender> 中，为\<Appender> 添加一个或多个过滤器后，可以用任意条件对日志进行过滤。\<Appender> 有多个过滤器时，按照配置顺序执行。

下面是几个常用的过滤器：
 
LevelFilter： 级别过滤器，根据日志级别进行过滤。如果日志级别等于配置级别，过滤器会根据onMath 和 onMismatch接收或拒绝日志。有以下子节点：
\<level>:设置过滤级别
\<onMatch>:用于配置符合过滤条件的操作
\<onMismatch>:用于配置不符合过滤条件的操作
 
例如：将过滤器的日志级别配置为INFO，所有INFO级别的日志交给appender处理，非INFO级别的日志，被过滤掉。


