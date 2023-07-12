<!-- title: OpenApi服务开发指南 -->
<!-- type: openapi -->
<!-- author: qiubo,zhangpu -->
<!-- date: 2020-02-10 -->

# 1. 简介

`openapi-framework-core`是开放平台网关的核心，实现了网关的核心架构和能力。结构上网关核心包括服务执行层和服务实现层，能力上包括：多租户，认证，授权，加解密，日志，事件，协议等。

# 2. 集成

OpenApi服务框架核心提供API服务的统一处理和执行能力，只需在目标工程整合该模块即可实现网关服务。

## 2.1. 服务端依赖

请在目标工程的pom.xml中添加该模块的依赖，如下：

```xml
<!-- 核心网关 -->
<dependency>
    <groupId>com.acooly</groupId>
    <artifactId>openapi-framework-core</artifactId>
    <version>${openapi-framework.version}</version>
</dependency>

        <!-- 异步/跳转通知 -->
<dependency>
<groupId>com.acooly</groupId>
<artifactId>openapi-framework-notify</artifactId>
<version>${openapi-framework.version}</version>
</dependency>

        <!-- 自动文档化 -->
<dependency>
<groupId>com.acooly</groupId>
<artifactId>openapi-framework-apidoc</artifactId>
<version>${openapi-framework.version}</version>
</dependency>
```

> 注意：openapi-framework.version，请根据具体情况选择，一般推荐选择最新发布版本。

## 2.2. 版本说明

* 1.4.x : acoolyV3版本 -> YIJI/HTD
* 4.0.x : acoolyV4.0版本 -> cnevx
* 4.2.1 : acoolyV4.2.x 版本 (支持JSON协议）
* 4.2.2 : acoolyv4.2.x 版本（v4最新版本：支持JSON和HTTP_FORM_JOSN老协议）
* 5.0.x : acoolyV5.x.x
* 5.2.x : acoolyV5.2.x 

# 3 特性

网关服务的所有核心配置所有都是可选的，默认可以不配置，以下的配置案例中的参数值都是默认的参数值，你可以根据项目需求，修改配置。

> 注意：以下所有配置都依赖`openapi-framework-core`核心模块

## 3.1. 日志配置

```ini
## 日志配置
# [可选] 网关性能日志开关
acooly.openapi.log.perf-log-enable=true
# [可选] 网关查询日志是否分文件
acooly.openapi.log.mult-file-enable=false
# [可选] 网关是否在存储非查询类请求
acooly.openapi.save-order=true
```

> 请特别注意`acooly.openapi.save-order=true`参数，虽然设置为true后，会大大提高效率（查询请求不写数据库），但会造成报文请求号(requestNo)可重复的问题，请各位在配置选择时，斟酌选择。


以下是日志配置的参数废弃和替换说明

```ini
# 网关查询日志是否分文件
@Deprecated
acooly.openapi.queryLogSeparationEnable =false  ==>  acooly.openapi.log.multFileEnable=false

# 网关性能日志开关
@Deprecated
acooly.openapi.enablePerfLog=false  ==>  acooly.openapi.log.perf-log-enable=false
```

## 3.2. 日志脱敏

为保证敏感数据的安全，在日志中提供策略配置通用日志脱敏。同时日志数据的脱敏也兼容在报文体上通过Acooly框架的annotaion标签进行脱敏配置。

### 3.2.1. 配置

```ini
## 日志脱敏
# [可选] 开关
acooly.openapi.log-safety=false
# [可选] 需要隐藏(不打印)的字段，多个逗号分隔。例如：password,pswd
acooly.openapi.log-safety-ignores=amount,name
# [可选] 需要Mask(隐藏部分数据)的字段，多个逗号分隔。例如：mobileNo,certNo
acooly.openapi.log-safety-masks=payeeUserId,buyeryMobileNo,buyerCertNo
```

注意：本次更新日志脱敏参数全部迁移到`acooly.openapi.log`名字空间下。

```ini
# 日志脱敏(默认关闭)
@Deprecated
acooly.openapi.logSafety=false ==> acooly.openapi.log.safety-enable=false

# 日志脱敏，需要隐藏(不打印)的字段，多个逗号分隔。例如：password,pswd
@Deprecated
acooly.openapi.logSafetyIgnores=password,pswd  ==> acooly.openapi.log.safety-ignores=password,pswd

# 日志脱敏，需要Mask(隐藏部分数据)的字段，多个逗号分隔。例如：mobileNo,certNo
@Deprecated
acooly.openapi.logSafetyMasks=mobileNo,certNo  ==> acooly.openapi.log.safety-masks=mobileNo,certNo
```

### 3.2.2. 应用

日志脱敏特性设计为在报文进入（请求）后和报文发出（响应/通知）前对目标报文（例如JSON字符串）进行脱敏处理。框架采用正则匹配替换模式对目标属性值进行脱敏处理，脱敏的方式包括忽略和Mask两种方式。

* 参数开关支持（`acooly.openapi.log.safety-enable=false|true`,默认false关闭）
* 支持全局参数配置忽略和Mask（`acooly.openapi.log.safety-ignores/safety-masks=xxx,yyy,zzz`）
* 支持在报文上字段上配置忽略(`@ToString.Invisible`)或Mask（`@ToString.Maskable`）,忽略优先。
* 支持多级属性脱敏（子对象，子集合）

全局配置案例：

```ini
acooly.openapi.logSafety=true
acooly.openapi.logSafetyIgnores=amount,name
acooly.openapi.logSafetyMasks=payeeUserId,buyeryMobileNo,buyerCertNo
```

报文配置案例：

```java
@NotEmpty
@Size(max = 64)
@OpenApiField(desc = "标题", demo = "特色牛肉干", ordinal = 2)
@ToString.Maskable
private String title;

@NotEmpty
@ToString.Invisible
@Size(min = 20, max = 20)
@OpenApiField(desc = "卖家用户ID", demo = "201603080912340002", ordinal = 9)
private String payeeUserId;
```

## 3.3. 缓存配置

缓存配置主要用于秘钥，ACL权限，IP白名单，租户信息等核心高频率使用数据，按请求AccessKey为单位进行缓存，以提高处理效率。

```ini
## 缓存配置
# [可选] 缓存总开关，如果开启，自动启动二级缓存（redis）。
acooly.openapi.auth-info-cache.enable=true
# [可选] 二级缓存（redis）缓存信息过期时间,单位：ms,默认10分钟,二级缓存是强制开启的
acooly.openapi.auth-info-cache.defaultTimeout=600000
# [可选] 一级本地缓存是否开启（注意：一级缓存为本地缓存，多节点时无法通过管理界面变更设置其他节点自动失效，只能通过到期时间）
acooly.openapi.auth-info-cache.levelOneEnable=true
# [可选] 一级本地缓存缓存信息过期时间,单位：ms,默认10分钟
acooly.openapi.auth-info-cache.levelOneTimeout=600000
```

特别注意 > 
1. 最新版本已提供通过后台BOSS界面对缓存清除功能, 但是一级缓存在多节点时，无法通过后台管理界面变更设置其他节点自动失效，只能通过到期时间`acooly.openapi.auth-info-cache.levelOneTimeout`。
2. 子秘钥的缓存不提供清除功能，提供直接删除秘钥方式同时会清除缓存。


## 3.4. 动态秘钥

动态秘钥一般用于与App端的直接集成和服务场景，App通过配置的匿名秘钥访问名字为login的openApi接口，在通过认证（通过`com.acooly.openapi.framework.service.service.LoginApiService`接口实现）后，动态分配后续请求的访问秘钥。

```ini
## 动态认证和匿名访问（通过匿名身份访问login接口动态获取会话秘钥）
# [可选] 网关匿名访问配置，默认开启，可访问登录(login)接口.
acooly.openapi.anonymous.enable=true
acooly.openapi.anonymous.access-key=anonymous
acooly.openapi.anonymous.secret-key=anonymouanonymou
acooly.openapi.anonymous.permissions.login=*:login
# [可选] 网关动态登录认证接口，登录认证逻辑由目标项目实现接口:AppApiLoginService
acooly.openapi.login.enable=true
# [可选] 每次访问是否动态生成秘钥
acooly.openapi.login.secret-key-dynamic=true
# [必选] 登录后下发的动态秘钥所属的父秘钥。1、该秘钥是在后台配置好的秘钥对，并配置的合理的权限；2、根据该主秘钥生产的动态子秘钥继承该父秘钥的权限
acooly.openapi.login.parent-access-key=test
```

>注意：
> 1. 动态秘钥的权限继承于父秘钥，所以父秘钥的权限必须配置合理，否则会导致动态秘钥权限不足。
> 2. 每次`login`请求后是否生成动态秘钥的开关`acooly.openapi.login.secret-key-dynamic`默认调整为true。

## 3.5. 流控

OpenApi框架对流控的支持模式为多级（parentId,service两级）控流（非整流模式），当流量超过流控配置则拒绝请求。

* 支持多组配置，每个请求需要通过所有组的check才能通过流控正常请求。
* 策略配置支持： “*”

具体配置如下：

```ini
## 全局所有服务秒级流控1000请求
acooly.openapi.rates[0].partner-id=*
acooly.openapi.rates[0].method=*
acooly.openapi.rates[0].interval=1000
acooly.openapi.rates[0].max-requests=1000

## 全局queryUser服务秒级流控100请求
acooly.openapi.rates[1].partner-id=*
acooly.openapi.rates[1].method=queryUser
acooly.openapi.rates[1].interval=1000
acooly.openapi.rates[1].max-requests=100

## 商户(2019089021212230001)的queryUser服务秒级流控100请求
acooly.openapi.rates[2].partner-id=2019089021212230001
acooly.openapi.rates[2].method=queryUser
acooly.openapi.rates[2].interval=1000
acooly.openapi.rates[2].max-requests=10
```

> 注意，流控需要引入模块依赖: `openapi-framework-extensions`

## 3.6. 异步通知

异步通知是OpenApi框架内独立的异步通知服务模块，在提供异步或跳转接口时，最后接口的通知由该框架进行统一处理，包括重发机制。目前重发的机制分自动和手动。

详情请参考： [OpenApi异步通知服务](https://acooly.cn/docs/component/openapi-framework-notify.html)

## 3.7. 文档自动化

Acooly-OpenApi框架推崇的报文文档方式是：设计及呈现方式，由Api开发者在设计和开发阶段，通过@Annotaion方式对服务和报文字段进行标记，然后通过自动文档化框架自动生成Api文档和网站，提供给接入方使用。

详情请参考：[OpenApi文档自动化](https://acooly.cn/docs/component/openapi-framework-apidoc.html)

## 3.8. 多租户

OpenApi框架提供了多租户的能力集成支撑，但本身不管理和配置，而只是提供请求与租户身份的绑定，并提供下传多租户体系内部的能力。

1. 通过扩展接口注入租户列表信息（请参考：扩展章节的"多租户扩展"）
2. 通过OpenApi后台的`接入管理`模块设置接入方(partnerId)与租户(tenantId)的绑定关系
3. 外部请求接口时候，框架通过请求的accessKey -（匹配）-> partnerId -(匹配)-> tenantId
4. 在Api服务内部，通过`tenantId()`方法获取当前请求对应的租户ID(tenantId),也可以通过`ApiContextHolder.getContext().getTenantId()`静态工具方法获取当前线程对应的租户ID。

## 3.9 接口MOCK

OpenApi支持接口服务按需MOCK，mock的接口以@OpenApiField.demo作为mock的数据响应请求。

1. 需要打开全局mock开关：`acooly.openapi.mock.enable=true`
2. 对OpenApi服务的@OpenApiService的mock属性设置为true：`@OpenApiService(mock=true)`

## 3.10 网关多入口URLs配置

从`v5.2.0-SNAPSHOT`开始，支持配置网关的多个入口URL地址，默认不配置则保持原有的`/gateway.do`。

```properties
acooly.openapi.gateways[0]=/gateway1
acooly.openapi.gateways[1]=/gateway2.html
acooly.openapi.gateways[2]=/a/b/gateway.do
```

# 4. 服务开发

## 4.1. 工程规划

如果目标项目需要提供网关服务，目前情况下，加载依赖配置即可正常开发openApi服务提供服务。这里推荐一个工程结构规划，编译项目管理和报文复用。

```java
xxxx-project
        |--xxxx-project-assemble
        |--xxxx-project-common
        |--xxxx-project-core
        |--xxxx-project-openapi
        |--xxxx-project-openapi-service    // 独立的openApi服务
        |--xxxx-project-openapi-message    // 可复用的openApi报文定义
        |--xxxx-project-....
        |--xxxx-project-test
```

> 独立的`xxxx-project-openapi-message`可以打包后，结合openapi-framework-client提供java版本的SDK。

依赖设计：

1. common：底层，不依赖任何模块，提取项目中的公共domain，dto，enum，constants，exceptions等公用。
2. core：核心业务和逻辑服务 --（依赖）--> common
3. openapi-service 网关服务实现模块（OpenApi服务开发） --（依赖）--> core和openapi-message
4. openapi-message --（依赖）--> common

## 4.2. 开发说明

openapi框架提供的Api服务开发模式比较简单，基于接口报文定义，由框架完成报文（请求，响应，通知等）的解析，组装，认证，授权等，开发人员定义具体服务后，框架会提供组装好的客户端请求对象，开发人员按需注入服务进行逻辑处理后，回填数据到定义的响应对象就完成接口开发工作，由框架完成后续的签名，组装报文并响应/发送给客户端请求方。

### 4.3. 报文定义

服务开发的第一步是根据业务需求分析和设计，完成接口服务的报文定义，根据接口类型的不同，我们可能会定义请求报文，响应报文，通知报文等。定义报文通用的规则是相同的。具体主要规则如下：

* 所有的报文必须继承ApiMessage或其子类，作为报文体的公共报文头.
    * 常规请求报文需继承ApiRequest(extends ApiMessage)
    * App的请求报文需继承AppRequest(extends AppRequest),但增对App手机了设备信息
    * 分页查询请求报文需继承PageApiRequest(extends AppRequest)，增加页号和页大小
    * 异步接口请求报文需继承ApiAsyncRequest(extends AppRequest)，增加回调地址和通知地址
* 报文内定义的所有数据项必须编写@OpenApiField文档，用于生成自动文档，否则框架不会序列化并启动时警告。
    * desc：[必选] 表示字段中文名称，必填；**（你不填试试，除非你不想用这个接口）**
    * constraint：表示字段说明，可选，如果为空则为解析为desc
    * demo: [必选] 字段demo，必填 **（你不填试试，除非你不想用这个接口）**
    * ordinal: [必选] 文档顺序，必填**（你不填试试，除非你不想用这个接口）**
    * security：是否加密字段数据，可选，默认为false
* 报文内定义的所有数据项必须编写JSR303的验证注释，否则框架不会序列化并启动时警告
* 推荐使用的数据项数据类型为：String, Integer, Long, Money(金额或2位小数), Enum。其他类型不推荐但可以支持（如：decimal,boolean等）。

案例如下：

```java
/**
 * @author zhangpu
 * @date 2014年7月29日
 */
@Getter
@Setter
public class WithdrawRequest extends ApiAsyncRequest {

    @NotEmpty
    @Size(max = 64)
    @OpenApiField(desc = "订单号", constraint = "商户订单号，唯一标志一笔交易", demo = "20912213123sdf", oridnal = 1)
    private String merchOrderNo;

    @NotEmpty
    @Length(max = 20, min = 20, message = "提现用户ID为必选项，长度为20字符")
    @OpenApiField(desc = "提现用户ID", constraint = "提现用户ID", demo = "20198982938272827232", oridnal = 2)
    private String userId;

    @NotNull(message = "提现金额是必选项，格式为保留两位小数的元，如: 2.00,200.05")
    @OpenApiField(desc = "提现金额", constraint = "提现金额，格式为保留两位小数的元，如: 2.00,200.05", demo = "200.15", oridnal = 3)
    private Money amount;

    @OpenApiField(desc = "到账方式", constraint = "可选值：<li>0: T+0</li><li>1: T+1</li><li>2: T+2</li>", demo = "1", ordinal = 4)
    @Max(1)
    private Integer delay = 1;

    @OpenApiField(desc = "业务类型", demo = "BUSI2", ordinal = 5)
    private BusiTypeEnum busiType = BusiTypeEnum.BUSI2;

    //...
}    
```

### 3.4. 接口开发

所有的openapi接口开发由框架统一提供基类和泛型约定开发模式，每个类作为一个api服务的实现。具体规范如下：

* **类定义：**必须继承：`com.acooly.openapi.framework.core.service.base.BaseApiService`,并通过泛型制定该接口的请求和响应报文，notify报文请实现getApiNotifyBean()方法提供。
* **服务标记：**openapi服务必须使用专用的annotation标注：@OpenApiService。
    * name：[必选] 服务名称（服务码），英文唯一编码，请使用名字前置方式定义，比如：orderCreate，orderPay，orderCancel，已便于代码和文档的排序。
    * desc：[必选] 服务标题（中文名）
    * version: 服务版本，默认：1.0
    * responseType: 服务类型，默认：SYN,可选为SYN,ASYN和REDIRECT（不推荐）
    * busiType: 业务类型，可选：Trade（默认），Manage和Query，框架默认情况下，Query类型不持久化请求数据，以提高效率。
    * owner: 标记服务提供者或开发人员，便于管理。
* **文档标记：**主要用于文档自动化生成。包括：@ApiDocType：标记文档的scheme分类方案，@ApiDocNote接口说明，支持HTML。
* **逻辑实现：**接口的逻辑实现请覆写doService方法实现，调用逻辑处理。回填response.
* **异常处理：**doService方法内部可选进行异常处理，因为框架已提供了统一的基于BusinessException的异常处理。也就是说你的内容业务服务或openapi服务内如果手动抛出异常，请使用BusinessExcetion或其子类。

案例：

```java
/**
 * @author zhangpu
 * @date 2014年7月29日
 */
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

### 3.5. 单元测试

完成openapi服务的开发后，我们需要进行单元测试，以保障开发的质量可控。框架提供专用的单元测试基类方便的实现单元测试。

单元测试基类：com.acooly.openapi.framework.core.test.AbstractApiServieTests

> 单元测试基类提供了对OpenApiClient的封装，并可在子类中覆写请求的基础参数，包括：gatewayUrl，partnerId，accessKey，secretKey等。

案例：

```java
public class OrderOpenApiTest extends AbstractApiServieTests {

    @Test
    public void testOrderCreateSync() throws Exception {
        OrderCreateRequest request = new OrderCreateRequest();
        request.setRequestNo(Ids.getDid());
        request.setMerchOrderNo(Ids.getDid());
        // fill request datas
        OrderCreateResponse response = request(request, OrderCreateResponse.class);
        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getContext()).isEqualTo(content);
    }
```

# 5. 扩展

## 5.1 事件扩展

在具体集成项目中，一些特殊的全局或布局的扩展可以通过OpenApi提供事件处理机制，在服务执行时候进行拦截处理。

### 5.1.1 OpenApi事件定义

在每次服务执行时都会执行，按执行顺序包括：

1. BeforeServiceExecuteEvent：服务认证和预处理完成，调用服务执行前事件
2. ServiceExceptionEvent：服务成功执行异常
3. AfterServiceExecuteEvent：服务执行完成，可能是成功或失败

### 5.1.2 OpenApi事件监听

我们可以根据需求，自定义事件监听，处理OpenApi发布的服务执行事件。

关键点：

1. 事件必须继承`com.acooly.openapi.framework.core.listener.SmartListenter`基类。
2. 事件处理类需要使用@OpenApiListener标记。主要设置范围（global）和同/异步（asyn），我们建议采用同步方式（异步方式的线程池大小目前是内部写定的，没有对外公开配置参数）

### 5.1.3 案例

下面的代码是通过OpenApi限制IP段访问的临时扩展

```java

@Slf4j
@OpenApiListener(global = true, asyn = false)
public class IpBlockLimitApiListener extends AbstractListener<BeforeServiceExecuteEvent> {

    @Autowired
    private IpSearchService ipSearchService;

    @Override
    public void onOpenApiEvent(BeforeServiceExecuteEvent event) {
        String requestIp = event.getApiContext().getRequestIp();
        if (Strings.isNotBlank(requestIp) && IPUtil.isPublicIpv4(requestIp)) {
            if (ipSearchService.isChinaIp(requestIp)) {
                throw new BusinessException(CommonErrorCodes.UNAUTHORIZED_ERROR, "限制IP访问");
            }
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}

```

> 注意：`ipSearchService.isChinaIp`的工具服务来自组件`acooly-component-data-ip`

## 5.2 认证扩展

在对接App/前端客户端时，采用登录认证+动态秘钥方式保证安全。每个具体集成系统的认证可通过接口方式注入到spring容器中进行扩展。具体配置请参考：`动态秘钥`部分.

### 5.2.1 相关配置：

```ini
# [可选] 网关动态登录认证接口，登录认证逻辑由目标项目实现com.acooly.openapi.framework.service.service.LoginApiService
acooly.openapi.login.enable=true
# [可选] 每次访问是否动态生成秘钥
acooly.openapi.login.secret-key-dynamic=true
```

### 5.2.2 扩展开发

你需要在你的目标集成工程中实现：`com.acooly.openapi.framework.service.service.AppApiLoginService`接口，并根据自身业务逻辑完成认证逻辑的开发。例如:

```java

@Slf4j
@Component
@Primary
public class customLoginApiServiceImpl implements AppApiLoginService {
    @Override
    public LoginDto login(LoginRequest loginRequest, ApiContext apiContext) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        String requestIp = apiContext.getRequestIp();
        // do auth...
        // String customerId = yourAuthService.auth(username,password,requestIp);
        LoginDto loginDto = new LoginDto();
        loginDto.setCustomerId("000000");
        loginDto.ext("x", "o");
        return loginDto;
    }
}
```

> 注意：这里通过`@Primary`配置你的认证实现为主实现，否则框架会使用内置默认实现（默认实现全通过）

## 5.3 租户扩展

针对多租户体系，框架提供了扩展租户信息的接口：`com.acooly.openapi.framework.service.service.tenant.ApiTenantLoaderService`

* OpenApi层默认不建议提供租户的管理，该接口实现可以由集成系统实现接口返回租户相关数据，用于Api层配置。
* OpenApi框架提供默认实现，集成系统实现该接口后，通过@Primary标记注入

下面是实现的案例：

```java

@Slf4j
@Component
@Primary
public class ApiTenantLoaderServiceTestImpl implements ApiTenantLoaderService {

    @Override
    public List<ApiTenant> load() {
        // 直接数据MOCK进行测试。
        // 实际应用场景中：由集成项目通过读取数据或则facade获取管理或配置的数据
        List<ApiTenant> apiTenants = Lists.newArrayList();
        apiTenants.add(new DefaultApiTenant("10000000001000000001", "车云宝"));
        apiTenants.add(new DefaultApiTenant("10000000001000000002", "心愿宝"));
        apiTenants.add(new DefaultApiTenant("10000000001000000003", "韦小宝"));
        return apiTenants;
    }
}
```

