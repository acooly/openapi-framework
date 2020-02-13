<!-- title: OpenApi服务异步通知 -->
<!-- type: openapi -->
<!-- author: zhangpu -->
<!-- date: 2020-02-09 -->

# 简介

Acooly开放平台openApi设计支持跳转和异步接口，作为可选模块提供支持，模块为：openapi-framework-notify。默认不引入该模块不启用。本模块提供异步通知框架服务。

# 特性 

* 跳转和异步接口服务开发
* 异步通知服务，对跳转和异步接口，提供统一的按有序时间（2天内，10+次）多次通知，直到成功。
* 可配置本地或远程facade通知接口服务（由内对外发起通知）
* 提供异步通知重试能力，当首次发送通知给客户端为成功（网络或客户端未处理成功返回200和success）,notify会启动定时任务拉起通知失败的纪录，反复尝试重新通知，失败重试次数为10次。具体间隔时间为（分钟）：2, 10, 30, 60, 2 * 60, 6 * 60, 6 * 60, 10 * 60, 15 * 60, 15 * 60

# 集成

## 依赖

目标工程直接引入`openapi-framework-notify`组件依赖即可。

```xml
<!-- OpenApi通知框架 -->
<dependency>
	<groupId>com.acooly</groupId>
	<artifactId>openapi-framework-notify</artifactId>
	<version>${openapi.version}</version>
</dependency>
```


## 配置

notify模块所有的参数都是可选的，默认加入依赖及可使用。以下是所有参数的默认配置和说明。

目标工程：application.peroperties中加入以下配置（默认可不配置）

```ini
## openapi notify
# [可选] 是否启动异步通知
acooly.openapi.notify.enable=true
# [可选] 异步通知 连接超时时间（ms）
acooly.openapi.notify.connTimeout=10000
# [可选] 异步通知 连读取时时间（ms）
acooly.openapi.notify.readTimeout=10000
# [可选] 异步通知 通知线程池核心大小
acooly.openapi.notify.threadPool.corePoolSize=10
# [可选] 异步通知 通知线程池最大线程数
acooly.openapi.notify.threadPool.maxPoolSize=50
# [可选] 异步通知 通知线程池队列大小
acooly.openapi.notify.threadPool.queueCapacity=100
# [可选] 异步通知 通知线程名称
acooly.openapi.notify.threadPool.namePrefix=OPENAPI-NOTIFY-TASK
# [可选:false] 接受异步通知的接口OpenApiRemoteService（facade）是否开启远程dubbo服务
acooly.openapi.notify.enableFacade=false
# 默认为false，表示不开启，则不暴露facade的dubbo接口，采用直接本地spring服务方式
acooly.openapi.notify.enableFacade=false
# [可选:5] 异步通知重发时，从数据库每次fetch的数量
acooly.openapi.notify.retryFetchSize=10
# [可选:120] 异步通知重发启动间隔（秒）
acooly.openapi.notify.retryPeriod=120
```
>特别注意：`acooly.openapi.notify.enableFacade`参数，默认为false,表示没有暴露dubbo接口，facade采用本地@Component方式在spring容器中，请直接使用@Autowired等注入使用；如果为true，则依赖dubbo的配置（acooly.dubbo.provider.enable=true,必须开启）,然后会暴露facade的dubbo接口，客户端采用:@Reference(version = "1.0")方式注入dubbo客户端。


# 开发

## 异步接口开发

异步接口由其特性，主要应对异步场景，流程如下：

1. 客户端同步请求网关的异步接口服务，必须传入notifyUrl，网关的接口收到数据后，同步返回：code=PROCESSING，success=true （请求报文：XxxxApiRequest extends ApiAsyncRequest）
2. 客户端收处理中的返回时，则标记该请求待结果通知。（响应报文：XxxxApiResponse extends ApiResponse）
3. 网关内服务异步处理完成该接口对应的业务后，有最终明确结果（SUCCESS或FAILURE）,然后根据客户端请求时的notifyUrl发送异步通知报文给客户端。（报文：XxxxApiNotify extends ApiNotify）
4. 客户端收到异步通知后，标记该请求最终结果并进行后续处理。


### 报文开发

报文开发中的字段规范全框架统一。特别的如下：

* **请求报文**：必须继承：com.acooly.openapi.framework.common.message.ApiAsyncRequest，必须填写notifyUrl用于异步通知。请注意传入可公网访问的地址URL。 
* **通知报文**：必须继承ApiNotify

### 服务开发

异步接口服务开发规范同全框架统一，以下面的接口服务代码为例，特别如下：

1. 接口服务类型标记为异步接口：responseType = ResponseType.ASNY
2. 必须覆写getApiNotifyBean()访问，返回该异步接口的异步报文接口实力。

```java
@Slf4j
@ApiDocType(code = "test", name = "测试")
@ApiDocNote("测试异步接口。<li>1、client -> 同步请求 -> gateway</li><li>2、gateway -> 异步通知（notifyUrl） -> client</li>")
@OpenApiService(name = "withdraw", desc = "测试：提现", responseType = ResponseType.ASNY, busiType = ApiBusiType.Trade, owner = "zhangpu")
public class WithdrawApiService extends BaseApiService<WithdrawRequest, WithdrawResponse> {

    /**
     * 同步处理
     *
     * @param request  已验证和组装好的请求报文，请直接享用
     * @param response 完成业务处理后，回填到response中，框架负责响应给请求客户端
     */
    @Override
    protected void doService(WithdrawRequest request, WithdrawResponse response) {
        // do any business and back filling to response
        response.setTradeNo(Ids.oid());
        response.setResult(ApiServiceResultCode.PROCESSING);
    }

    /**
     * 返回异步通知的报文bean
     * <p>
     * 下层业务处理完成后，通过GID通知OpenApi异步处理框架结果数据，
     * 框架根据这里指定的报文类型对数据进行组装和验证后，签名发送通知给客户端
     *
     * @return 异步通知的报文Bean
     */
    @Override
    public ApiNotify getApiNotifyBean() {
        return new WithdrawNotify();
    }
}
```

### 完整示例

内部：openapi-framework-test模块的withdraw接口代码。

## 跳转接口

支持两种方式的跳转接口：
1、自动redirect方式（目前不建议，在分布式结构时过程复杂，多次跳转）
2、以同步接口方式开发，网关端组织重定向地址和报文参数返回给客户端，由客户端确定采用Get或POST方式再次请求后端的服务界面。（推荐，因为灵活和简单明了），实际是转化为异步接口。







