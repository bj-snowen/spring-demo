# Spring MVC 多视图协商配置(json、jsp、xml、freemarker(ftl))

1. spring MVC REST改如何决定采用何种方式(视图)展示内容呢？
* 第一种：根据http request header中的Accept
```
Accept: text/css,\*/\*;q=0.1               //返回css格式数据
Accept: application/xml                  //返回xml格式数据
Accept: application/json                 //返回json格式数据
```
缺点：
```
chrome:
Accept:application/xml,application/xhtml+xml,textml;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5

firefox:
Accept:text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8

IE8:
Accept:image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/x-silverlight, application/x-ms-application, application/x-ms-xbap, application/vnd.ms-xpsdocument, application/xaml+xml, */*
```
用户直接通过浏览器访问，由于各个浏览器发送的Accept的不一致，将导致服务器不知要返回什么格式的数据。

* 第二种：根据扩展名
```
http://www.sxrczx.com/rest.xml           //将返回xml格式数据
http://www.sxrczx.com/rest.json          //将返回json格式数据
http://www.sxrczx.com/rest.htm           //将返回html格式数据
```
缺点：
不能统一通过同一URL实现多种展示视图。

* 第三种：根据format参数
```
http://www.sxrczx.com/rest?format=xml    //将返回xml格式数据
http://www.sxrczx.com/rest?format=json   //将返回json格式数据
http://www.sxrczx.com/rest?format=htm    //将返回html格式数据
```
缺点：
需要额外的传递format参数，URL变得冗余，繁琐，缺少了REST的简洁风范。

* 第四种 配置视图文件 使用哪种方式配置 响应类型的文件即可 按照order顺序优先匹配
配置：web.xml
```
<!-- spring mvc -->
<servlet>
    <servlet-name>SpringMVC</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
       <param-name>contextConfigLocation</param-name>
         <param-value>/WEB-INF/config/spring/appContext.xml</param-value>
      </init-param>
    <load-on-startup>1</load-on-startup>
  </servlet>
<servlet-mapping>
    <servlet-name>SpringMVC</servlet-name>
    <url-pattern>*.htm</url-pattern>
    <url-pattern>*.json</url-pattern>
    <!-- <url-pattern>*.xml</url-pattern> -->
</servlet-mapping>
```


Spring配置applicationContext.xml(无扩展名(jsp作为视图)/json/xml)
```
<!-- 根据客户端的不同的请求决定不同的view进行响应, 如 http://www.sxrczx.com/rest.json http://www.sxrczx.com/rest.xml -->
<bean class="org.springframework.web.servlet.view.ContentNegotiatingViewResolver">
    <!-- 设置为true以忽略对Accept Header的支持-->
    <property name="ignoreAcceptHeader" value="true"/>
    <!-- 在没有扩展名时即: "http://www.sxrczx.com/rest" 时的默认展现形式 -->
    <property name="defaultContentType" value="text/html"/>
    <!-- 扩展名至mimeType的映射,即 http://www.sxrczx.com/rest.json 映射为 application/json -->
    <property name="mediaTypes">
        <map>
            <entry key="json" value="application/json" />
            <entry key="xml" value="application/xml" />
        </map>
    </property>
    <!-- 用于开启 http://www.sxrczx.com/rest?format=json 的支持 -->
    <property name="favorParameter" value="false"/>
    <property name="viewResolvers">
        <list>
            <bean class="org.springframework.web.servlet.view.BeanNameViewResolver" />
            <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
                <property name="viewClass" value="org.springframework.web.servlet.view.JstlView"/>
                <property name="prefix" value="/pages"/>
                <property name="suffix" value=".jsp"></property>
            </bean>
        </list>
    </property>
    <property name="defaultViews">
        <list>
            <!-- json格式数据支持 -->
            <bean class="org.springframework.web.servlet.view.json.MappingJacksonJsonView" />
            <!-- xml 格式数据支持 -->
            <bean class="org.springframework.web.servlet.view.xml.MarshallingView" >
                <property name="marshaller">
                    <bean class="org.springframework.oxm.xstream.XStreamMarshaller"/>
                </property>
            </bean>
        </list>
    </property>
</bean>
```

Spring配置applicationContext.xml(freemarker/json)
```
<bean class="org.springframework.web.servlet.view.ContentNegotiatingViewResolver">
    <!--这里是视图解析器的执行顺序，0优先级最高-->
    <property name="order" value="1" />
    <!--
    这里是是否启用扩展名支持默认就是true
    例如  /user/{userid}.json
    -->
    <property name="favorPathExtension" value="true"/>
    <!--
    这里是是否启用参数支持，默认就是true
    例如  /user/{userid}?format=json
    -->
    <property name="favorParameter" value="false" />
    <!--
    这里是否忽略掉accept header，默认就是false
    例如     GET /user HTTP/1.1
    Accept:application/json
    -->
    <property name="ignoreAcceptHeader" value="true" />
    <!-- 如果所有的mediaType都没匹配上，就会使用defaultContentType -->
    <property name="defaultContentType" value="application/json" />
    <property name="mediaTypes">
        <map>
            <entry key="json" value="application/json" />
        </map>
    </property>
    <property name="defaultViews">
        <list>
            <bean class="org.springframework.web.servlet.view.json.MappingJacksonJsonView" />
        </list>
    </property>
</bean>

<!-- Freemarker 视图解析器-->
<bean id="freeMarkerViewResolver" class="org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver">
    <property name="cache" value="true" />
    <property name="order" value="0" />
    <property name="prefix" value="" />
    <property name="suffix" value=".htm" />
    <property name="contentType" value="text/html;charset=utf-8" />
    <!-- 把contextPath暴露给freemaker，前端可以通过￥{request.getContextPath()} 来获取上下文路径
    <property name="requestContextAttribute" value="request"/>
    -->
    <property name="viewClass" value="org.springframework.web.servlet.view.freemarker.FreeMarkerView" />
</bean>
<!-- ftl(Freemarker) 参数配置 -->
<bean id="freemarkerConfiguration" class="org.springframework.beans.factory.config.PropertiesFactoryBean">
    <property name="location" value="/WEB-INF/config/ftl/freemarker.properties"/>
</bean>
<bean id="freemarkerConfig" class="org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer">
    <property name="defaultEncoding" value="utf-8"/>
    <property name="templateLoaderPath" value="/WEB-INF/template"/>
    <property name="freemarkerSettings" ref="freemarkerConfiguration"/>
</bean>
```

freemarker.properties 属性文件
```
datetime_format=yyyy-MM-dd HH:mm:ss
date_format=yyyy-MM-dd
time_format=HH:mm:ss
number_format=0.######;
boolean_format=true,false
#auto_import="/common/index.ftl" as ui
whitespace_stripping=true
default_encoding=UTF-8
tag_syntax=square_bracket
url_escaping_charset=UTF-8
#开启默认容错，既null时，默认使用""代替
classic_compatible=true
```


# 总结
### freemarker mavn 需添加lib
```
 <!--freemarker start-->
    <dependency>
        <groupId>org.freemarker</groupId>
        <artifactId>freemarker</artifactId>
        <version>2.3.20</version>
    </dependency>
    <dependency>
        <groupId>commons-fileupload</groupId>
        <artifactId>commons-fileupload</artifactId>
        <version>1.3.1</version>
    </dependency>
<!--freemarker end-->
```

### mvc json数据格式返回
需配置json 库
```
<!-- json -->
<dependency>
    <groupId>com.fasterxml.jackson.jr</groupId>
    <artifactId>jackson-jr-all</artifactId>
    <version>2.4.3</version>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-annotations</artifactId>
    <version>2.5.4</version>
</dependency>

<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.4.3</version>
</dependency>

<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>fastjson</artifactId>
    <version>1.2.7</version>
</dependency>
<!--json end-->
```

### servlet启动异常
```
当web.xml配置 为：
<servlet-mapping>
        <servlet-name>spring</servlet-name>
        <url-pattern>*</url-pattern>
    </servlet-mapping>
 时报错
ContainerBase.addChild: start:
org.apache.catalina.LifecycleException: Failed to start component [StandardEngine[Catalina].StandardHost[localhost].StandardContext[]]

修改为
<servlet-mapping>
        <servlet-name>spring</servlet-name>
        <url-pattern>／*</url-pattern>
    </servlet-mapping>
    正常
```