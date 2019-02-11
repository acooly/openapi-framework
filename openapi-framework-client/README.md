<!-- title: OpenApiSDK组件 -->
<!-- type: openapi -->
<!-- author: qiubo,zhangpu -->
OpenApiSDK组件
====
openapi-framework-client
----

# 简介
openapi-framework-client模块是框架提供的通用开放平台客户端，这里可理解为Acooly框架内的SDK，同时也可以应用给外部，只是必须依赖Acooly框架的几个核心库。客户端Java项目可以轻松的通过几个简单配置和引入网关的messages包模块（如：XXX-PROJECT-OPENAPI-MESSAGES.jar），即可直接使用通过该工具实现本地服务器方式开发和注入网关调用。


# 集成

## 依赖

目标工程直接引入`openapi-framework-client`组件依赖即可。

```xml
<!-- OpenApi通知框架 -->
<dependency>
	<groupId>com.acooly</groupId>
	<artifactId>openapi-framework-notify</artifactId>
	<version>${openapi.version}</version>
</dependency>
```
> 当前稳定版本：4.2.0-SNAPSHOT

## 配置

client模块的参数有一些是必须配置的，以下是所有参数的默认配置和说明。

目标工程：application.peroperties中加入以下配置（默认可不配置）

```ini
acooly.openapi.client.enable=true
acooly.openapi.client.gateway=http://localhost:8089/gateway.do
acooly.openapi.client.accessKey=test
acooly.openapi.client.secretKey=06f7aab08aa2431e6dae6a156fc9e0b4
acooly.openapi.client.showLog=true
```

>注意：`acooly.openapi.client.enable`参数默认为false，加入依赖后，默认是不可用的，必须配置以上几个参数。

# 使用

## OpenApiClient

组件在启用后，会自动装配OpenApiClient类到Spring容器内，你可以在你项目中任意服务里面注入直接使用。

com.acooly.openapi.framework.client.OpenApiClient

```java
public class OpenApiClient {

    /**
     * 发送请求到网关,并对标注加密的字段自动加密
     *
     * @param request 请求对象
     * @param clazz   响应类型
     * @param <T>     响应类
     * @return 响应对象
     */
	 public <T> T send(ApiRequest request, Class<T> clazz){...}

}
```

>注意：`OpenApiClient `设计为基于框架的ApiMessage对象，包括：ApiRequest，ApiResponse等。你可以再本地项目跟进文档构建ApiMessage，也可以引用服务器端的xxx-messages.jar直接使用服务器端的报文定义。

## AbstractApiClientService

`com.acooly.openapi.framework.client.AbstractApiClientService`提供基于继承方式的客户端服务开发，可根据接口定义开发本地服务然后继承AbstractApiClientService来快速开发。

AbstractApiClientService特性：

1. 负责自动注入OpenApiClient
2. 提供request方法包装OpenApiClient.send方法，并对响应参数进行错误处理。


```java
@Slf4j
@Component
public class WithdrawApiClientService extends AbstractApiClientService {

    public WithdrawResponse withdraw(WithdrawRequest request) {
        return request(request, WithdrawResponse.class);
    }
    
    // ...
}
```





