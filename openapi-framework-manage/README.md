OpenAPI运营管理服务
====

## 简介

OpenAPI体系，除了服务框架能力外，还需要对应的运营管理功能，包括：请求订单的查询，异步通知消息的查询及管理（人工出发重发等），服务分类及服务管理，接入方及秘钥管理，接入方服务权限管理等。

这里功能统一由openapi-framework-manage模块提供支持，该模块的所有管理界面试图方案采用acooly框架的boss后台解决方案，采用freemarker编写并谁该模块jar包发布。

## 集成

### 添加依赖
请在目标工程的pom.xml中添加该模块的依赖，如下：

```xml
<dependency>
	<groupId>com.acooly</groupId>
	<artifactId>openapi-framework-manage</artifactId>
	<version>${openapi-framework.version}</version>
</dependency>
```

### 初始化数据库

本模块依赖的数据表的DDL随发布jar包一起发布，请使用解压工具解压后在目标工程数据库执行DDL。路径：db/mysql.sql

### 导入模块

请在目标工程中，使用spring的import配置，导入本模块的配置文件. 如下：

```xml
<import resource="classpath*:spring/openapi/openapi-framework-manage.xml "/>
```
注意：本模块依赖目标工程的dataSource服务，请确保目标工程中的spring环境中存在dataSource对象。


请在目标工程中，在springMVC配置中（SpringMVCDispatchServlet）加入管理服务的控制层配置文件。
classpath:spring/openapi/openapi-framework-servlet.xml

```xml
	<!-- Spring MVC Servlet -->
	<servlet>
		<servlet-name>springServlet</servlet-name>
		<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
		<init-param>
			<param-name>contextConfigLocation</param-name>
			<param-value>
				classpath*:spring/acooly/acooly-springmvc.xml
				classpath*:spring/**/*-servlet.xml
			</param-value>
		</init-param>
		<load-on-startup>1</load-on-startup>
	</servlet>
	<servlet-mapping>
		<servlet-name>springServlet</servlet-name>
		<url-pattern>*.html</url-pattern>
	</servlet-mapping>
```
