OpenApi服务框架集成
=================

## 简介
本文已工程Demo方式详细说明OpenApi服务框架的集成。

### 依赖模块

* OpenApi服务框架(core)
* OpenApi通知服务(nms)
* OpenApi运营管理(manage)

### 演示和Demo

* 服务器Demo: openapi-demo-server
* 客户端Demo: openapi-demo-client


### 目标应用场景

本项目可以独立运行也可以综合集成,

* 金服平台(OK)
* 贷款平台
* 理存平台（OK）
* 易生活
* 易交易

## 特性介绍

### OpenApi服务框架

OpenApi服务框架主要由三个部分组成, 包括：核心服务框架，通知服务和运营管理服务。

* 核心框架(openapi-framework-core)提供了完整的api基础服务体系和能力，主要支持api服务的执行和处理;
* 通知服务(openapi-framework-nms)是内嵌的外部异步通知服务，专门负责异步接口的外部结果通知发送；
* 运营管理(openapi-framework-manage)主要提供openapi运营相关的管理能力，主要包括查询，设置等服务和pportal。


### OpenApi自动SDK


### OpenApi自动文档






## 集成

### OpenApi服务框架集成

OpenApi服务框架提供API服务的统一处理和执行能力。主要由openapi-framework-core模块提供支持，如果只集成API服务，只需在目标工程整合该模块即可。

#### 添加依赖
请在目标工程的pom.xml中添加该模块的依赖，如下：

```xml
<dependency>
	<groupId>com.yiji</groupId>
	<artifactId>openapi-framework-core</artifactId>
	<version>${openapi-framework.version}</version>
</dependency>
```

注意：openapi-framework.version，请根据具体情况选择，一般推荐选择最新发布版本。


#### 初始化数据库

本模块会为每次请求都记录原始的请求订单数据，需要的表主要是api_order_info.

完成依赖的引入后，应该在你的工程libraries中可以找到jar包依赖：openapi-framework-core-${version}.jar。通过解压工具，解开jar包后，里面有本模块依赖的数据库脚本 db/mysql.sql和db/oracle.sql, 根据你的目标数据库选择初始化的DDL。

#### 导入模块

请在目标工程中，使用spring的import配置，导入本模块的配置文件. 如下：

```xml
<import resource="classpath*:spring/openapi/openapi-framework-core.xml "/>
```

注意：本模块依赖目标工程的dataSource服务，请确保目标工程中的spring环境中存在dataSource对象。


### OpenAPI异步通知发送服务集成

OpenAPI框架（core）的异步通知模块设计为可插拔模式，可插入服务的API接口为：com.yiji.framework.openapi.core.notify.ApiNotifySender 

```java
/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2016-07-30 23:07 创建
 */
package com.yiji.framework.openapi.core.notify;

import com.yiji.framework.openapi.core.notify.domain.NotifySendMessage;

/**
 * 异步通知发送服务接口
 * <p>
 * 默认为直接发送,如果需要采用外部实现,请直接实现该接口并放入spring容器中。
 *
 * @author acooly
 */
public interface ApiNotifySender {

    void send(NotifySendMessage notifySendMessage);

}

```

默认情况下，如果不引入任何异步通知的发送服务实现，在openapi-framework-core中，会使用默认的实现：DefaultApiNotifySender，我们可以选择引入OpenAPI框架自带的nms发送服务或在目标工程自定义。

**扩展自定义通知发送服务的方法**：实现ApiNotifySender接口的自定义bean，然后加入到目标工程的Spring容器中，注意请不要引入多个ApiNotifySender的自定义实现，否则框架无法确定使用哪个具体的自定义实现。

下面介绍OpenAPI框架实现的nms异步通知发送服务的集成方案请参考：[openapi-framework-nms/README.md](openapi-framework-nms/README.md)


### OpenAPI管理功能集成


### OpenApi SDK集成


## 特性说明



## 版本说明

### v1.1.0

发布时间：2016-06-15

* 升级兼容requestNo/orderNo
* 完成自动化SDK特性开发


### v1.0.0

1. 基础版本发布,以openApi-arch为基础,重构几乎所有组件的实现(采用spring3方式的接口发现注入方式),大部分模块可以在集成的工作方便的扩展和自定义.
2. 修改原有的orderNo为requestNo,但兼容orderNo请求,兼容内部openApi全兼容切换.
3. 可用模块为core和nms,单进程运行.