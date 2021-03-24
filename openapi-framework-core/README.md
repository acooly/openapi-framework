<!-- title: OpenApi服务开发指南 -->
<!-- type: openapi -->
<!-- author: qiubo,zhangpu -->
<!-- date: 2020-02-10 -->

# 简介
`openapi-framework-core`是开放平台网关的核心，实现了网关的核心架构和能力。结构上网关核心包括服务执行层和服务实现层，能力上包括：认证，授权，加解密，日志，事件，协议等。

# 集成及配置
OpenApi服务框架核心提供API服务的统一处理和执行能力，只需在目标工程整合该模块即可实现网关服务。

## 依赖
请在目标工程的pom.xml中添加该模块的依赖，如下：

```xml
<dependency>
	<groupId>com.acooly</groupId>
	<artifactId>openapi-framework-core</artifactId>
	<version>${openapi-framework.version}</version>
</dependency>
```

>注意：openapi-framework.version，请根据具体情况选择，一般推荐选择最新发布版本。

### 版本说明

* 1.4.x : acoolyV3版本	 -> YIJI/HTD
* 4.0.x : acoolyV4.0版本 -> cnevx
* 4.2.1 : acoolyV4.2.x 版本 (支持JSON协议）
* 4.2.2 : acoolyv4.2.x 版本（v4最新版本：支持JSON和HTTP_FORM_JOSN老协议）
* 5.0.x : acoolyV5.x.x 


## 配置

网关的以下核心配置所有都是可选的，默认可以不配置，以下的配置案例中的参数值都是默认的参数值，你可以根据项目需求，修改配置。

<del>
```
 acooly.openapi.enablePerfLog=true 
```



```ini
# 网关性能日志开关
acooly.openapi.enablePerfLog=true
# 网关查询日志是否分文件
acooly.openapi.queryLogSeparationEnable=false
# 网关是否在存储非查询类请求
acooly.openapi.saveOrder=true
# 缓存配置（秘钥和权限）
acooly.openapi.auth-info-cache.enable=true
acooly.openapi.auth-info-cache.defaultTimeout=7200000
# 网关匿名访问配置，默认开启，可访问登录(login)接口.
acooly.openapi.anonymous.enable=true
acooly.openapi.anonymous.accessKey=anonymous
acooly.openapi.anonymous.secretKey=anonymouanonymou
acooly.openapi.anonymous.permissions.login=*:login
# 网关动态登录认证接口，登录认证逻辑由目标项目实现com.acooly.openapi.framework.service.service.LoginApiService
acooly.openapi.login.enable=true
acooly.openapi.login.secretKeyDynamic=true
```
>注意：流控的功能和配置在openapi-framework-extensions模块组件中，这里不重复介绍。

## 特性

### 日志脱敏
日志脱敏特性设计为在报文进入（请求）后和报文发出（响应/通知）前对目标报文（例如JSON字符串）进行脱敏处理。框架采用正则匹配替换模式对目标属性值进行脱敏处理，脱敏的方式包括忽略和Mask两种方式。

* 参数开关支持（`acooly.openapi.logSafety=false|true`,默认false关闭）
* 支持全局参数配置忽略和Mask（`acooly.openapi.logSafetyIgnores/logSafetyMasks=xxx,yyy,zzz`）
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

### 流控

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

# 开发模式

## 工程规划

如果目标项目需要提供网关服务，目前情况下，加载依赖配置即可正常开发openApi服务提供服务。这里推荐一个工程结构规划，编译项目管理和报文复用。

```java
xxxx-project
	|--xxxx-project-assemble
	|--xxxx-project-common
	|--xxxx-project-core
	|--xxxx-project-openapi
		|--xxxx-project-openapi-service	// 独立的openApi服务
		|--xxxx-project-openapi-message	// 可复用的openApi报文定义
	|--xxxx-project-....
	|--xxxx-project-test
```

>独立的`xxxx-project-openapi-message`可以打包后，结合openapi-framework-client提供java版本的SDK。

依赖设计：

1. common：底层，不依赖任何模块，提取项目中的公共domain，dto，enum，constants，exceptions等公用。
2. core：核心业务和逻辑服务  --（依赖）--> common
3. openapi-service 网关服务实现模块（OpenApi服务开发） --（依赖）--> core和openapi-message
4. openapi-message --（依赖）--> common

## 服务模式

### 单服务模式

### SOA微服务


# 开发说明

openapi框架提供的Api服务开发模式比较简单，基于接口报文定义，由框架完成报文（请求，响应，通知等）的解析，组装，认证，授权等，开发人员定义具体服务后，框架会提供组装好的客户端请求对象，开发人员按需注入服务进行逻辑处理后，回填数据到定义的响应对象就完成接口开发工作，由框架完成后续的签名，组装报文并响应/发送给客户端请求方。

## 同步服务开发

### 报文定义

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

    @OpenApiField(desc = "业务类型", demo = "BUSI2",ordinal = 5)
    private BusiTypeEnum busiType = BusiTypeEnum.BUSI2;
    
    //...
}    
```

### 接口开发

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
* 

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

## 单元测试

完成openapi服务的开发后，我们需要进行单元测试，以保障开发的质量可控。框架提供专用的单元测试基类方便的实现单元测试。

单元测试基类：com.acooly.openapi.framework.core.test.AbstractApiServieTests

>单元测试基类提供了对OpenApiClient的封装，并可在子类中覆写请求的基础参数，包括：gatewayUrl，partnerId，accessKey，secretKey等。

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

## 异步和跳转


# 相关工具



