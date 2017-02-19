OpenApi-Framework
=================

## 简介
OpenApi综合解决方案外部版本，用于集团内非易极付环境内部的OpenApi服务运营场景。可独立单进程运行和管理，也可以通过接口扩展实现集成到目标体系中。

### 特性能力

* OpenAPI核心服务框架
* [OpenAPI异步通知服务](http://gitlab.yiji/zhangpu/openapi-framework/tree/master/openapi-framework-nms#README)
* [OpenApi运营管理服务](http://gitlab.yiji/zhangpu/openapi-framework/tree/master/openapi-framework-manage#README)
* OpenApi开放平台服务
* OpenApi运营知识库
* OpenApi自动化SDK

### 开发规范

* OpenApi服务开发规范
* <a href="http://gitlab.yiji/zhangpu/openapi-framework/tree/master/openapi-framework-core#README">OpenApi商户开发规范</a>

### 演示和Demo

* 网关端Demo: openapi-demo-server
* 接入方Demo: openapi-demo-client


### 目标应用场景

本项目可以独立运行也可以综合集成,

* 金服平台(完整应用)
* 贷款平台
* 理存平台（OK）
* 易生活
* 易交易（方案确定，需求确定中）

## 特性介绍

### OpenApi服务框架

OpenApi服务框架主要由三个部分组成, 包括：核心服务框架，通知服务和运营管理服务。

* 核心框架(openapi-framework-core)提供了完整的api基础服务体系和能力，主要支持api服务的执行和处理;
* 通知服务(openapi-framework-nms)是内嵌的外部异步通知服务，专门负责异步接口的外部结果通知发送；
* 运营管理(openapi-framework-manage)主要提供openapi运营相关的管理能力，主要包括查询，设置等服务和pportal。


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

下面介绍OpenAPI框架实现的nms异步通知发送服务的集成方案请参考：[openapi-framework-nms/README.md](http://gitlab.yiji/zhangpu/openapi-framework/tree/master/openapi-framework-nms#README)

### OpenAPI管理功能集成

请参考：[OpenAPI管理服务](http://gitlab.yiji/zhangpu/openapi-framework/tree/master/openapi-framework-manage#README)


## 版本说明

### v1.3.2

* 2016-11-24 - 修复openapi boss 后台bug 1、修复通知记录查询页面，保存通知响应时将响应报文HtmlUtils.htmlEscape，转移特殊字符 2、修复订单查询页面，修复跳转查询按request_No和merchOrderNo查询异常，将服务名拆分为服务码和版本和查询条件统一 3、修复接入管理页面，查看详情异常 - [志客] efb9ed1 
* 2016-11-22 - 将openapi服务分为通用服务、查询服务、管理服务和交易服务类型，目前只分为了查询和交易类型，增加枚举管理服务（Manage）,开放平台在解析的时候默认所有服务都属于通用服务，然后再根据openapi标注的服务类型将服务分为交易、查询和管理类型服务，默认为交易类型。并将此枚举类移动到openapi-framework-common模块中 - [志客] 3eed5b3
* 2016-11-22 - 删除doc和sdk模块，已迁移到独立工程。


### v1.3.1

* 2016-11-16 - 修复当商户传入协议类型（protocol）不为空且不等于HTTP_FORM_JSON的时候，由于在组装响应报文的时候根据此值获取ResponseMarshall对象为null,导致调用ResponseMarshall方法时报空指针异常，响应给商户的resultMessage=“内部错误”无法定位错误所在，增加协议类型（protocol)校验 - [zhike] 2bb164c


### v1.3.0

升级二级版本号为3，对框架提供大模块功能的变动。主要是提供了远程BOSS管理集成能力。详细情况如下：

* 2016-11-14 - 提供openApi服务集成的三种方式: allinone表示网关服务和本地管理功能; gateway表示纯粹的网关服务+管理功能的provider; manage表示远程管理功能的consumer - [zhangpu] d2cae77
* 2016-11-14 - 新增boss管理功能的provider和consumer模块,实现可以远程方式管理boss功能 - [zhangpu] cb4fd5d
* 2016-11-14 - 重构框架基础服务模块,包括分离domain模块;分离出现的service接口模块,新增service本地持久化模块等 - [zhangpu] 7919c72


### v1.2.3

* 2016-10-25 - 优化sdk - [志客] c5f7af6
* 2016-10-25 - 独立导入Openapi-Framwork-sdk模块用到的jar版本 - [志客] f3778f9
* 2016-10-25 - 重构Openapi-Framwork-sdk模块，调整相关模块代码 - [志客] a12a387

### v1.2.2

* 2016-10-21 - 将ApiServiceException和OrderCheckException移到common模块中去 - [志客] e32db03

### v1.2.1

* 2016-10-18 - 优化orderInfo管理查询界面,删除不需要的参数,调整显示结构 - [zhangpu] 2b32afa
* 2016-10-18 - [issue#7]增加条件可选annotaion: OpenApiFiledCondition,用于标注每个报文项为条件可选,并要求必须填写条件说明。 - [zhangpu] cfc3aba
* 2016-10-18 - 增加管理功能的权限资源菜单配置和更新初始化SQL - [zhangpu] 9b37654
* 2016-07-31 - 添加2个冗余的配置文件,用于方便模块开发测试 - [zhangpu] aa719c6


### v1.2.0

* 2016-07-31 - 完成openapi-framework服务层所有基础开发工程,并完成文档编写,升级为1.2.0版本 - [zhangpu] c4a1162
* 2016-07-31 - 完成manage模块的integration开发,如果加入manage模块,则可以使用manage对应的authRealm实现。 - [zhangpu] 00cecff
* 2016-07-31 - 完成manage模块的integration开发,如果加入manage模块,则可以使用manage对应的authRealm实现。 - [zhangpu] 54f2b9b
* 2016-07-31 - 增加openapi-framework-servlet.xml用于管理controller配置,需要在springMVCDispathServlet中加入配置 - [zhangpu] d3d756e
* 2016-07-31 - 异步通知发送模块解耦重构,通过代理模式实现外部注入通知发送的实现。 - [zhangpu] 97c8a61
* 2016-07-30 - 完成基础管理功能的开发,包括:接入方管理, 服务类型和服务管理,接入方权限管理等 - [zhangpu] 8260709

### v1.1.3


* [new] 更新api_notify_message的初始化DDL, 增加resp_info, requestNo, merchOrderNo三个字段 
* [new] 完成异步通知的管理功能,提供查询和修改(人工方式出发重新发送,免于数据订正) 
* [mod] 调整nms的异步通知扫描频率为2分钟,后续计划修改为可配置,测试环境1分钟,生成环境2-5分钟
* [fix] 重构nms包结构,修复了异步通知中,失败通知发送失败BUG。 - 2016-07-27 04:08:11

>注意：本版本需要升级数据库。

升级脚本：

MYSQL:

```sql
ALTER TABLE `api_notify_message` 
ADD COLUMN `resp_info` VARCHAR(128) NULL COMMENT '通知响应' AFTER `content`,
ADD COLUMN `request_no` VARCHAR(40) NULL COMMENT '请求序号' AFTER `partner_id`,
ADD COLUMN `merch_order_no` VARCHAR(40) NULL COMMENT '商户订单号' AFTER `request_no`;
```

管理功能：

新增了随包发布的管理功能，主要功能：通知记录查询和人工触发通知（修改状态，通知时间和URL），请直接在BOSS中增加下面的资源并角色授权即可使用。

异步通知记录：/manage/openapi/notifyMessage/index.html


### v1.1.2
* [add] 新增特性,支持ORACLE数据库(core和nms)。 
* [fix] nms组件首次通知更新数据库状态是不BUG。 - 2016-07-13 03:07:54 +0800 (3 days ago)
* [add] 添加.gitignore文件,配置工程项目忽略文件策略 - 2016-07-11 14:00:21 +0800 


### v1.1.1

* 610b22a - zhangpu - [mod] PageApiRequest的基类从AppReqeust修改为ApiRequest - 2016-07-06 11:35:24 +0800 (2 days ago)
* 5494599 - zhangpu - SDK部分瘦身和提供商户端常用工具类 - 2016-07-06 11:33:20 +0800 (2 days ago)


### v1.1.1-SNOTSHAP.20160627

* dc9cfd8 - 给orderInfoDao的查询实现增加debug级别的sql及参数日志 (29 minutes ago)
* cac2db5 - 增加openApi商户开发规范文档(未完成) (36 minutes ago)
* b9c5c26 - 为默认的API测试基类增加加密方法 (2 days ago)
* dd926d5 - 修改请求号重复时错误消息内容,从"订单号不唯一"修改为"请求号重复" (2 days ago)
* bfaa134 - 增加对signType, version, potocol默认值为设置的逻辑测试的的单元测试验证。 (2 days ago)
* 06a8a42 - 对报文默认值进行处理, 包括:signType,version和protocol, 解决则三个参数默认未传入时的签名认证正确处理和后续相关逻辑行为按默认值处理 (2 days ago)
* e6b55c4 - 异步通知时，对数据库的orderNo特别处理回填。数据库为orderNo,报文为merchOrderNo (7 days ago)
* 9429309 - 添加acooly的部分工具类到common包 (7 days ago)
* beaa1a1 - fix:GID幂等时仍然使用orderNo的bug,修正为根据merchOrderNo (7 days ago)
* 475f3f0 - 升级版本为v1.1.1-SN..., 调整common的依赖个工具,为去除acooly-core做准备 (10 days ago)
* 22d9fb2 - 修改acooly-core的依赖为依赖parent定义版本 (12 days ago)
* f4c2844 - cleanup (12 days ago)
* 84887cf - 框架依赖的acooly-core跳转为与acooly-parent一致~ (12 days ago)


### v1.1.0

发布时间：2016-06-15

* 升级兼容requestNo/orderNo
* 完成自动化SDK特性开发


### v1.0.0

1. 基础版本发布,以openApi-arch为基础,重构几乎所有组件的实现(采用spring3方式的接口发现注入方式),大部分模块可以在集成的工作方便的扩展和自定义.
2. 修改原有的orderNo为requestNo,但兼容orderNo请求,兼容内部openApi全兼容切换.
3. 可用模块为core和nms,单进程运行.