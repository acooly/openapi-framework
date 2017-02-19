OpenAPI framework 异步消息发送服务
====

## 简介

为OpenAPI framework框架提供内置的异步通知发送服务实现，负责对外所有异步通知消息的统一管理和发送。负责接收主框架发送给外部异步通知的请求，由该模块负责通知报文的持久化管理，查询和发送，如果发送失败，会根据策略定时重发多次，最大限度让消息接收方成功接收业务的异步通知消息。

## 特性

* OpenAPI framework的异步通知服务（ApiNotifySender）的标准实现。
* 接收内部的异步通知发送需求，并先持久化，再多次发送，直接接收方成功接收。
* 默认由模块内管理重复多次通知的调用，采用spring task实现，每2分支触发一次check是否有需要发送的数据。
* 可扩展外部的调用服务用语触发发送任务，自动发送服务负责读取持久化的待发送通知，启动异步线程发送给接收方。
* 当通知失败时，重复多次通知的策略为：通知间隔时间,第一次隔两分钟，然后隔N分钟再次发送， 一共通知11次。时间间隔（分钟）为：2, 10, 30, 60, 2 * 60, 6 * 60, 6 * 60, 10 * 60, 15 * 60, 15 * 60

## 集成

### 添加依赖
请在目标工程的pom.xml中添加该模块的依赖，如下：

```xml
<dependency>
	<groupId>com.yiji</groupId>
	<artifactId>openapi-framework-nms</artifactId>
	<version>${openapi-framework.version}</version>
</dependency>
```

注意：openapi-framework.version，请根据具体情况选择，一般推荐选择最新发布版本。

### 初始化数据库

本模块仅需要异步通知报文数据表：api_notify_message,该表的DDL存在于openapi-framework-core-${version}.jar包中，与core的初始化DDL集成在一起，如果你在集成core的时候执行了完整的DDL，这里可以忽略。

### 导入模块

请在目标工程中，使用spring的import配置，导入本模块的配置文件. 如下：

```xml
<import resource="classpath*:spring/openapi/openapi-framework-nms.xml "/>
```

注意：本模块依赖目标工程的dataSource服务，请确保目标工程中的spring环境中存在dataSource对象。

