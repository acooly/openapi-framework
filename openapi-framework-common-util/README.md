<!-- title: OpenApi接入工具 -->
<!-- type: openapi -->
<!-- author: zhangpu -->
<!-- date: 2020-02-01 -->
## 1. 简介
提供最小依赖的OpenApi网关接入客户端工具包，以轻量化应用于商户系统中，帮助商户快速完成OpenApi服务的接入工作。
工具包只有提供一个专用的工具类`OpenApiTools`,提供接入OpenApi网关时，客户端需要的所有基础功能和静态方法，配合OpenApi开放平台的ApiDoc快速对接。
主要包括的功能有：通讯，参数验证，签名验签加解密，解析和反解析报文，同步，异步和调整接口的处理等。

特性：

* 最小依赖建立OpenApi客户端工具
* 支持同步，异步和调整三种模式的通讯接入
* 支持三种接收通知和模式模式：全自动，半自动（全自动组装对象+手动回执），手动（接收，解析，验签，解密，组装对象，发送回执）
* [todo] 根据开放平台Apidoc文档自动生成报文对象。

## 2. 集成

### 2.1 Maven工程POM配置

请拷贝工具的坐标到你工程pom.xml文件的dependencies节点下。

```xml
<dependency>
    <groupId>com.acooly</groupId>
    <artifactId>openapi-framework-common-util</artifactId>
    <version>5.0.1-SNAPSHOT</version>
</dependency>
```

>支持版本号：5.0.0-SNAPSHOT和5.0.1-SNAPSHOT(临时开发版本)

### 2.2 Maven Setting设置

因为工具包没有上传到maven中央仓库，需要你调整下你的Maven的conf目录下的setting.xml文件配置。

如果你的setting.xml中配置配置mirror，建议你配置阿里镜像（如果你在中国，这样会快些），请在mirrors节点下添加以下配置：

```xml
<mirror>
   <id>aliyun-repo</id>
   <mirrorOf>*,!acooly-repo</mirrorOf>
   <name>aliyun maven repository</name>
   <url>https://maven.aliyun.com/repository/public</url>
</mirror>
```

>这里的重点是mirrorOf的配置，`*,!acooly-repo`,如果你有mirror配置，则在你的配置后面加上`!acooly-repo`。这里是表示除开`acooly-repo`的repository外都用你的mirror配置，而使用你的工程pom.xml里面的配置`acooly-repo`仓库。

### 2.3 上传到你的nexus

请直接下周最新版本的jar包，然后上传到你的nexus。

>注意，这种方式，请自行添加相关依赖。

**相关Jar包依赖：**

* acooly-common-type:(同本工具版本)：acooly的基础类型和初级工具。（本身只依赖：validation-api）
* servlet-api:4.0.1：Servlet容器
* commons-codec:1.11及以上：签名/编码
* slf4j-api:1.7.26及以上：日志
* github:http-request:6.0：http客户端工具
* fastjson:1.2.48.sec06及以上：JSON工具
* lombok:1.18.8：动态生成getter/setter。

>你可以通过`openapi-framework-common-util`jar包内的pom.xml查看详细的依赖配置。

## 3. 应用

OpenApiTools工具核心就是提供一个OpenApiTools的客户端工具类，解决客户端接入访问网关的所有工作帮助。

工具类名：`com.acooly.openapi.framework.common.OpenApiTools`

OpenApiTools是非静态工具类，需要在你的工程根据场景需要进行实例化。

### 3.1 实例化

**直接New方式**

直接new的方式，一般建议使用在简单场景或单元测试中。

```java
OpenApiTools openApiTools = new OpenApiTools(gateway, accessKey, secretKey);

// 同步请求
openApiTools.send(...)
// 跳转页面请求
openApiTools.redirectSend(...)
// 接收网关通知
openApiTools.notice(...)

```

>你可以参考示例工程`openapi-client-demo`的单元测试[com.acooly.openapi.client.demo.OpenApiToolsTest](https://gitlab.acooly.cn/acoolys/openapi/openapi-client-demo/blob/master/src/test/java/com/acooly/openapi/client/demo/OpenApiToolsTest.java)


**Spring方式**

如果你采用Spring方式实例化，一般用于整个服务（工程）共享OpenApiTools工具和配置。以下是springboot的实例化方式。

1. 在你是工程的主配置内增加`openapi`的配置参数(包括：网络连接参数，网关地址，认证相关等)，一般可采用内置类。请参考：[OpenApiClientDemoProperties.java](https://gitlab.acooly.cn/acoolys/openapi/openapi-client-demo/blob/master/src/main/java/com/acooly/openapi/client/demo/OpenApiClientDemoProperties.java)

2. 新增或在你现有的@Configuration类中，通过代码方式为容器创建一个openApiTools实例。请参考：[OpenApiClientDemoConfiguration.java](https://gitlab.acooly.cn/acoolys/openapi/openapi-client-demo/blob/master/src/main/java/com/acooly/openapi/client/demo/OpenApiClientDemoConfiguration.java)

```java
    /**
     * 注入你的目标工程自己配置化的OpenApiTools客户端参数，以适应多环境感知配置
     */
    @Autowired
    private OpenApiClientDemoProperties openApiClientDemoProperties;
    
    /**
     * 如果你是spring-boot环境，可以通过springboot方式创建一个容器共用的OpenApiTools实例，直接注入使用。
     *
     * @return
     */
    @Bean
    public OpenApiTools openApiTools() {
        OpenApiClientDemoProperties.Openapi openapi = openApiClientDemoProperties.getOpenapi();
        OpenApiTools openApiTools = new OpenApiTools(openapi.getGateway(), openapi.getAccessKey(), openapi.getSecretKey());
        openApiTools.setCharset(openapi.getCharset());
        openApiTools.setConnTimeout(openapi.getConnTimeout());
        openApiTools.setReadTimeout(openapi.getReadTimeout());
        openApiTools.setShowLog(openapi.isShowLog());
        return openApiTools;
    }
```

### 3.2 客户端报文

OpenApiTools工具要求通讯过程中的报文必须是对象(POJO)，并需要继承工具包中提供的基类`ApiMessage`或其子类。

* [必须] `ApiMessage`: 提供公共报文的基类，你无需处理公共报文。请参考：[公共报文定义](http://acooly.cn/docs/component/openapi-framework-common-util.html#toc317)
* `ApiRequest`: 通用请求报文基类
* `ApiResponse`: 通用响应报文基类
* `ApiAsyncRequest`: 如果是异步接口，请使用异步基类，增加了：returnUrl和notifyUrl支持。
* `PageApiRequest`: 分页查询请求基类
* `PageApiResponse`: 分页查询响应基类

>注意：以上基类，除了`ApiMessage`是推荐使用，但不是必须的，你可以根据文档自行构建报文对象。

**构建报文对象的方式？**

1. 你可以通过服务提供商的开放平台的在线API文档及元数据，自由构建你工程下任意包路径的报文对象。你可以收到复制/粘贴，也可以自己写程序解析元数据。
2. [todo] 官方下个版本会发布基于在线文档的自动生成报文的工具。你只需传入服务名则自动为你的工程指定包路径下生成对应的报文对象。

>小彩蛋：请求报文对象的命名请以：服务名+ApiRequest模式，如果你遵循该小约定，OpenApiTools会帮你猜猜当前服务名称，无需你手动`apiRequest.setService()`.

>OK，你已拿到文档，并根据文档生成了Api服务对应的报文对象，也许只生成一个先试试。下一节正式进入开发...

### 3.3 使用

#### 3.3.1 同步接口

下面是`OpenApiTools`工具可的同步接口请求工具方法

```java
    /**
     * [同步接口] 发送同步请求到网关.
     * 并对标注加密的字段自动加密
     *
     * @param request 请求对象
     * @param clazz   响应类型
     * @param <T>     响应类
     * @return 响应对象
     */
    public <T extends ApiMessage> T send(ApiRequest request, Class<T> clazz) {
    	// ...
    }
```

**案例：**

```java
// 登录认证
LoginApiRequest apiRequest = new LoginApiRequest("zhangpu",openApiTools.encrypt("Ab112121212"));
LoginApiResponse apiResponse = openApiTools.send(apiRequest, LoginApiResponse.class);
```

#### 3.3.2 分页查询

```java
GoodsListApiRequest apiRequest = new GoodsListApiRequest(1,5);
GoodsListApiResponse apiResponse = openApiTools.send(apiRequest, GoodsListApiResponse.class);
```

#### 3.3.3 异步接口

异步接口的处理分两个部分

1. 组装同步请求（包含notifyUrl），网关返回收到，code为PROCESSING处理中。
2. 网关通过你请求时的notifyUrl异步通知你处理最终结果。

第1步与同步接口的处理方式完全一致。第2步这里重点说明。

**异步通知的接收**：注意notifyUrl必须是公网可访问的地址，如果你工程CSRF防御或其他认证，请忽略该URL，该URL的安全认证通过验证签名方式处理。

**异步通知的处理**：异步通知的处理步骤逻辑为：接受通知数据，验证签名，组装通知报文对象(POJO), 解密字段数据（根据文档按需），本地业务逻辑处理，回写接收结果给网关。`OpenApiTools`提供三种方式的通知处理。

具体实现请参考demo工程的：[WithdrawClientDemoController](https://gitlab.acooly.cn/acoolys/openapi/openapi-client-demo/blob/master/src/main/java/com/acooly/openapi/client/demo/web/WithdrawClientDemoController.java)

* 全自动：自动完成所有的接收和处理工作。参考方法：`notifyUrlAuto`
* 半自动：完成接收和解析处理（组装为通知对象），不包含回写接收结果。参考方法：`notifyUrlSemiAuto`
* 全手动：接收和处理逻辑中所有的步骤逐步完成。参考方法：`notifyUrlManual`


#### 3.3.4 跳转接口

调整接口是客户端界面通过POST/GET方式跳转到开放平台的网关视图界面进行业务处理，完成后从网关界面调回通知结果的接口方式，接口的处理逻辑包含：

1. 收集和组装调整请求报文对象（	ApiRequest的子类），并对报文进行序列化和签名
2. 通过POS/GET方式跳转发送请求到网关。
	* 服务端直接跳转：直接在controller层组装报文，采用307方式重定向到网关。`openApiTools.redirectSend`
	* 前后端分离模式：完成组装报文后，响应给前端，有前端组装POST/GET方式跳转到网关。`openApiTools.redirectParse`方法返回`ApiMessageContext`对象的`buildRedirectUrl`方法，包含请求网关的所有参数。
3. 同步通知（returnUrl）是网关界面处理完后，跳行到你的界面的操作，一般用于给用户显示处理结果（非最终确定结果）。（处理逻辑与异步通知一致）
4. 异步通知（notifyUrl）是网关业务处理完成后，后端通知你的服务最终处理的结果。

参考案例：[OrderCashierPayClientDemoController](https://gitlab.acooly.cn/acoolys/openapi/openapi-client-demo/blob/master/src/main/java/com/acooly/openapi/client/demo/web/OrderCashierPayClientDemoController.java)

### 3.4 异常处理

OpenApiTools工具对外只负责抛出通讯和解析相关的异常，业务相关的异常处理由您自行处理。

`ApiResponse`基类（你的所有响应和通知报文都应该是它的子类）中定义了code，message和detai三元的结果码，足以支持你进行相关的业务结果处理。无需定义单独的错误码列表。

结果判断通过code的值：

* SUCCESS: 表示成功
* PROCESSING: 表示处理中(正常状态)
* 其他: 对应的错误码

你也可以通过ApiResponse对象提供的isSuccess()方法直接判断服务器端是否处理成功。

关于详细的错误码和处理处理，请参考OpenApi接入开发指南的：[错误码与错误处理](http://acooly.cn/docs/component/openapi-framework-common-util.html#toc222)



## 4. 演示工程

本文涉及的所有内容都在演示工程中通过工程代码呈现，请移步查看或拉取后运行。

Demo工程采用springboot2.x构建。

[openapi-client-demo](https://gitlab.acooly.cn/acoolys/openapi/openapi-client-demo)

使用参考：

1. 演示工程的所有演示，都通过单元测试方式：`OpenApiToolsTest`
2. 异步和调整接口的演示依赖容器（需要接受通知），请直接启动该工程。
3. 参数配置说明，请直接参见工程内的`application.properties`,都有参数说明。

>演示工程内每个板块都有详细的javadoc和参数说明，这里不做重复描述。

完成配置启动工程后，你可以通过`OpenApiToolsTest`的单元测试方法逐个验证。