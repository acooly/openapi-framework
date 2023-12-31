<!-- title: OpenApi框架简介 -->
<!-- name: openapi-framework-common -->
<!-- type: openapi -->
<!-- author: zhangpu -->
<!-- date: 2020-02-13 -->
## 1. 简介

OpenApi开放平台框架提供完善的网关服务开放平台的完整解决方案。

## 2. 文档

### 2.1 服务开发

* [OpenApi服务开发指南](https://acooly.cn/docs/component/openapi-framework-core.html)
* [OpenApi服务异步通知](https://acooly.cn/docs/component/openapi-framework-notify.html)
* [OpenApi文档自动化](https://acooly.cn/docs/component/openapi-framework-apidoc.html)

### 2.2 接入开发

* [OpenApi接入开发指南](https://acooly.cn/docs/component/openapi-framework-client.html)
* [OpenApiClient工具](https://acooly.cn/docs/component/openapi-framework-common-util.html)

## 3. changelog

### v5.2.0-SNAPSHOT.20230712

* 2023-07-12 - 后台BOSS提供清除缓存的功能，但目前针对一级缓存的清除无法做到所有节点，需要等待过期。 - [zhangpu] 5817fe3
* 2023-07-12 - 后台BOSS秘钥列表的展示脱敏，提供直接复制秘钥及安全秘钥对信息的功能。 - [zhangpu] 5817fe4
* 2023-07-12 - 完成动态秘钥功能的拆分和优化，子秘钥独立为新的模块，可以通过主秘钥和子秘钥的客户标志进行查询定位，同时可以直接删除清除缓存。 - [zhangpu] 2255248
* 2023-07-12 - 登录（`login`）接口获取的动态秘钥加入了对所属父秘钥（参数配置的）状态检查的功能。
* 2023-07-10 - 登录接口获取的秘钥，默认调整为动态秘钥。即：每次登录返回的秘钥都是新生成的。 - [zhangpu] b4ef2f8

 >注意: 为分离动态秘钥，需要配置新功能（动态秘钥）BOSS资源和权限，请已线上系统运行升级SQL:`openapi-framework-service`模块（jar内）的 `META-INF/database/openapi/mysql/openapi-v5.2.0_20230712_SNAPSHOT-upgrade.sql`

### v5.2.0-SNAPSHOT.20230523

* 2023-05-23 - [动态秘钥] 优化：匿名访问登录接口后，返回的动态秘钥的父秘必须采用参数配置（`acooly.openapi.login.parent-acess-key`）代替原有的随意通过请求的parentId指定；同事优化回传parentId为动态父秘钥的parentId。详情请参考最新的`OpenAPI服务端开发指南的3.4.动态秘钥`部分 - [zhangpu] 210e034
* 2023-05-23 - [动态秘钥] 修正：修正匿名秘钥对访问（login）时因找不到对应的租户造成的BUG。 - [zhangpu] 5bdcded
* 2023-04-08 - [网关服务] 优化：解决OpenApiField时候，增加trimToEmpty,解决开发人员写入接口名称时前面加入空格，造成初始化权限ACLs错误问题。 - [zhangpu] f1b61a7
* 2023-04-08 - [网关服务] 优化：增加权限初始化时，检查权重字符串格式的错误消息输出错误权限字符串的功能。 - [zhangpu] be8c010
* 2023-04-01 - [网关服务] 优化：重构javabean验证的错误消息处理模式，调整为：1、如果是标准错误消息模板（例如：@NotBank等），则返回报文中文名称+错误消息（例如：标题不能为空，其中"标题"是"title"属性的中文名称，"不
能为空"是模板标准消息）；2、如果是自定义消息，则直接返回开发人员自定义的消息；3、多个字段的错误消息间使用英文逗号分隔。 - [zhangpu] 2b9c8d2
* 2023-04-01 - [权限管理] 修正：认证对象的accessKey调整为不能修改，只能添加和删除，防止在修改accessKey时，造成以认证编码关联的ACLs错乱无效。 - [zhangpu] 221dc64
* 2023-03-28 - [权限管理] 优化：为API权限的ACLs管理增加全选功能；优化scheme的后台管理；修正和优化部分展示和体验问题 - [zhangpu] 52c7ef7
* 2023-02-27 - [API文档] 优化：增加全局API搜索功能；在不调整原有逻辑基础上，去除低custom类型scheme的合并；自定义的API-Scheme重启呗删除问题；修正顶部菜单中的OpenApi文档链接地址 - [zhangpu] eda6732


### v5.2.0-SNAPSHOT.20221201

2022-12-01 - 新增特性：可配置网关的多个入口地址`acooly.openapi.gateways[0~n]`，如果不配置，则默认为:/gateway.do。例如配置：`acooly.openapi.gateways[0]=/a/b/gateway.x` - [zhangpu] 0f5cf29

### v5.2.0-SNAPSHOT.20221025

* 2022-10-25 - 迁移ACL设置的按钮到顶部统一的工具栏，节省一行空间。 - [zhangpu] 591921e
* 2022-10-25 - 优化认证授权对象列表的展示和查询，支持树形结构展示父子秘钥；可通过子秘钥的部分字符串查询；优化秘钥删除功能，删除时候，同时级联删除子对象，级联删除对应的ACL权限 - [zhangpu] 3b9e27f
* 2022-10-25 - 修正：档接入认证对象或密码状态不正确（未认证状态下），响应对应错误的进行签名的BUG - [zhangpu] baafefd
* 2022-10-25 - 修正login接口登录认证时BUG，调整subAccessKey的生成逻辑为：mainAccessKey#customerId，以便于不用查询数据库关系直接获取当前操作的接入方下用户标志 - [zhangpu] 33c2da3
* 2022-10-25 - 根据约定(subAccessKey=AccessKey#CustomerId)，封装AccessKeys获取主AccessKey和CustomerId的静态工具类，并在ApiContext中提供访问方法，在OpenApi服务或执行链（集成扩展时）执行时，可通过静态方法直接访问:`ApiContextHolder.getContext().getgetCanonicalAccessKey()`,`ApiContextHolder.getContext().getCustomerId()`,请参考：`com.acooly.openapi.framework.demo.service.api.DemoOrderCreateApiService` - [zhangpu] 851a2d4
* 2022-09-22 - 完成OpenApi的按需MOCK（使用文档的demo作为响应数据）功能开发， - [zhangpu] 0210b5f

### v5.2.0

* 从5.1.4升级为5.2.0，准备启动spring-cloud的feign方式调用支持和Demo编写

### v5.1.4

2022-04-14 - 客户端的序列化调整为通用JSON序列化,解决：客户端清洗序列化时，把字符串空值(null)序列化为空字符串("")，造成JSR303验证无法正常通过。 - [zhangpu] cb0f5da

### v5.1.3

* 2021-10-09 - 优化OpenApi服务端开发文档，同时增加事件处理的扩展说明和案例 - [zhangpu] ce0a1ae
* 2021-10-09 - 重构流控能力特性，从事件监听的流控处理模式迁移为主控filter处理，放置到认证授权后，进行流控认证，确保过滤掉无效请求。 - [zhangpu] b55418f
* 2021-10-09 - 优化OpenApi的事件实体，开发和整理事件处理的listener案例：Ip段限制监听扩展。 - [zhangpu] 801d485
* 2021-09-02 - 删除无效工具类 - [zhangpu] 17f3198

### v5.1.2

2021-07-21

* 2021-07-21 - 调整认证对象管理逻辑，删除认证对象时，直接级联删除对应的ACLs权限 - [zhangpu] 01885dd
* 2021-07-21 - 修正在认证成功前产生的部分未归类异常，造成响应时签名错误的BUG（issue#10）。 
* 认证处理优化：在认证实现中如果认证成功则设置`context.authenticated=true`,不用进行权限验证。 
* 异常处理优化：所有异常都可以正常处理，不用针对那些异常进行特殊判断是否对响应报文进行签名（不再设置：`context.signResponse`参数，废弃） 
* 响应处理优化：在响应filter中，对响应报文是否加密和签名，直接根据`context.authenticated=true`进行判断 - [zhangpu] bf288b8

### v5.1.1

2021-06-07

* 2021-06-07 - 修正：apidoc的文档浏览界面的左侧菜单选择无效问题，调整左侧菜单采用服务端URL访问模式。 - [zhangpu] 08f4d32
* 2021-06-07 - 优化：优化apidoc的服务列表展示效果，升级为最新ftl界面，同时查看功能调整为直接跳转apidoc前端通用界面。 - [zhangpu] cdfb4db
* 2021-06-07 - 新增：@ApidocHide标签，用于标记openapi服务外部不可见（不加入任何scheme中），不再前端apidoc文件列表中可见，但仍然生成apidoc数据，在后台可查，前台手动输入URL可访问 - [zhangpu] 44ff714

### v5.1.0

2021-03-30

* 2021-03-30 - 为OpenApi调用facade提供专用工具：`OpenApiFacades` - [zhangpu] f2434eb
* 2021-03-26 - 提交本次变更特性的文档说明，重构服务端开发文档说明 - [zhangpu] f2434eb
* 2021-03-26 - 更新openapi初始SQL(openapi-v5_SNAPSHOT-upgrade-v5.1.0_release.sql) - [zhangpu] 6b4a38f
* 2021-03-24 - 优化：认证管理模块界面显示，视图调整为bootstrap模式 - [zhangpu] c3df174
* 2021-03-24 - 优化：OpenApi订单查询，URL和长数据字段使用detailView方式显示 - [zhangpu] e2f15b1
* 2021-03-23 - 优化：认证管理编辑界面，动态生成accessKey和secretKey后，自动完成表单验证（去除未验证的提示） - [zhangpu] bc626c2
* 2021-03-23 - 优化：调整OpenApi本地缓存时间默认值，从60分钟调整为10分钟，新增一级本地缓存开关（默认开启）和一级缓存过期时间（默认10分钟） - [zhangpu] 290ab3c
* 2021-03-23 - 优化：为认证逻辑增加partnerId和accesskey存在和状态的检测 - [zhangpu] c4cb86e
* 2021-03-23 - 优化：为认证对象管理添加tip说明 - [zhangpu] 3dd2e38
* 2021-03-22 - 优化：ApiAuth对象新增状态管理能力 - [zhangpu] 8de163c
* 2021-03-22 - 优化：日志相关的配置文件，废弃acooly.openapi.下相关的日志的配置（兼容），全部迁移到acooly.openapi.log.xxx下。 - [zhangpu] 7088614
* 2021-03-22 - 多租户特性：增加ApiTenant接口，支持外部接口扩展方式（ApiTenantLoaderService.load）实现租户类别信息的集成，集成后可在接入管理中选择租户与partnerId关联。 - [zhangpu] 9516522
* 2021-03-23 - 多租户特性：新增tenantId集成到OpenApi请求会员ApiContext中，同时支持在Api服务中通过tenantId()方法获取 - [zhangpu] 22a7d9d
* 2021-03-19 - 在openapi-framework-client中还原商户接入指南，恢复`商户接入指南`标准文档。 - [zhangpu] 2df9d82

### v5.0.0-SNAPSHOT(2020-06-26)

2020-06-26

* 在BOSS界面的异步通知模块增加手动立即通知按钮功能。 - [zhangpu] ae1c91e
* 发起手动通知时，不占用总的通知次数（当前sendCount - 1）
* 无论原有主状态是否success，都更新为Waiting，等待定时任务触发发送
* 下次发送时间立即调整为现在。
* 下次定时任务自动触发发送该记录（目前默认是2分钟）
* 修复异步通知的查询条件。 - [zhangpu] 22b70e3
* GID：全站跟踪唯一请求。
* requestNo：从外部查询唯一请求。

### v5.0.0-SNAPSHOT(2020-05-30)

* 2020-05-30 - issue #14: 完成IP白名单的能力开发，默认不开启，可通过后台针对每个AccessKey配置开启和设置 - [zhangpu] b9724fa
* 2020-05-30 - 优化OpenApi后台功能的体验 - [zhangpu] b9724fa
* 2020-05-09 - BUGFIX-修复code忽略大小写SUCCESS判断 - [xiyang] 5bba455
* 2020-04-25 - Issue #1： 清理apidoc的冗余日志，跳转明细日志为debug - [zhangpu] bf14b8d

### v5.0.0-SNAPSHOT(2020-02-13)

2020-02-13

* 完成权限认证改造，模型结构升级。`partner(接入方)`-(1-n)->`apiAuth(accessKey)`-(1-n)->apiAuthAcl(服务权限列表)。
* 支持配置方式权限（原有perm）和ACL两种默认。你可以在后台`认证授权`->`访问权限`中配置特殊权限（`*:*`模式），也可以通过`认证授权`->`设置权限`配置ACL权限（1.0版本的功能），你配置的两种权限OpenApi会合并并集使用。
* 在目标库执行升级SQL: [openapi-v4-upgrade-v5.sql](https://gitlab.acooly.cn/acoolys/openapi/openapi-framework/raw/master/openapi-framework-service/src/main/resources/META-INF/database/openapi/mysql/openapi-v4-upgrade-v5.sql)
* 启动登录后台BOSS，在`接入管理`和`认证授权`模块中，手动更新部分字段内容。包括：商户号等
* 请求登录接口（login）使用的accessKey作为父AccessKey。每次login用户/密码认证通过后，后返回子动态秘钥：accessKey#username及对应的secretKey。
* 如果开启动态密码，secretKey为每次登录成功后新生成。该动态密码在该username下次登录前一直有效；否则以首次登录成功时生成的动态密码不变（暂时不支持秘钥有效期，应用场景不对，管理困难）。
* 访问码：accessKey#username的权限同父accessKey的权限配置。 注意：原有逻辑是登录成功后，动态秘钥的权限是所有服务（\*:\*），是非常安全的。
* 重构缓存实现，废弃config组件方案（存在持久化到数据库），采用二级缓存（本地+redis），调整默认缓存过期时间为10分钟。
* 修正：多套OpenApi服务公用redis时，缓存key重复不能隔离的问题（添加key前缀为：AppName）
* 在管理系统中任何对权限和秘钥变更（updae和delete）的操作通过事件刷新缓存。(遗留问题，事件通知暂未实现多节点同步，多节点时，需 要等待缓存失效，默认10分钟)
* 升级注意： 基于以上修正的逻辑，请所有5.0以上为App/客户端提供服务的内部OpenApi新增专用的客户端使用accessKey，配置对应的权限用于登录（login）

### v5.0.0-SNAPSHOT

2019-09-01

* 与v4.2.2-SNAPSHOT同步维护，支持acoolyV5框架
* 从acoolys/openapi-framework迁移到/acoolys/openapi/openapi-framework

### v4.2.2-SNAPSHOT

2018-08-01

* filter方式重构处理核心
* 同时支持JSON和FORM_JSON两种协议

### v4.2.0-SNAPSHOT

2018-06-01

* 支持请求参数序列化为json，放在http body中传输
* 支持Content-Type=application/x-www-form-urlencoded，请求数据放在form表单body参数中
* 安全校验相关参数支持放在http header或者url中
* 同步响应：响应安全校验信息放在http header中，响应体为json
* 跳转响应：服务端响应http code=302，响应相关信息在http header的Location参数中，其中body参数为响应体json
* 通知响应：异步响应遵循同步响应规范
* 使用匿名账户登录
* 账户信息校验成功后，下发新的accessKey\secretKey
* 后续请求使用新的accessKey\secretKey

### v4.2.2-SNAPSHOT

2018-05-01

* 在4.1基础上重构openapi的核心实现，采用filterChian方式。
* 支付双协议：JSON（4.1及以上）和HTTP_FORM_JSON（4.0及以下）

### v4.0.0-SNAPSHOT

2018-01-01

* spring-boot化，全面升级为acooly4版本支持。

### v1.3.2

2016-11-24

* 2016-11-24 - 修复openapi boss 后台bug 1、修复通知记录查询页面，保存通知响应时将响应报文HtmlUtils.htmlEscape，转移特殊字符 2、修复订单查询页面，修复跳转查询按request_No和merchOrderNo查询异常，将服务名拆分为服务码和版本和查询条件统一 3、修复接入管理页面，查看详情异常 - [志客] efb9ed1
* 2016-11-22 - 将openapi服务分为通用服务、查询服务、管理服务和交易服务类型，目前只分为了查询和交易类型，增加枚举管理服务（Manage）,开放平台在解析的时候默认所有服务都属于通用服务，然后再根据openapi标注的服务类型将服务分为交易、查询和管理类型服务，默认为交易类型。并将此枚举类移动到openapi-framework-common模块中 - [志客] 3eed5b3
* 2016-11-22 - 删除doc和sdk模块，已迁移到独立工程。

### v1.3.1

2016-11-16

* 2016-11-16 - 修复当商户传入协议类型（protocol）不为空且不等于HTTP_FORM_JSON的时候，由于在组装响应报文的时候根据此值获取ResponseMarshall对象为null,导致调用ResponseMarshall方法时报空指针异常，响应给商户的resultMessage=“内部错误”无法定位错误所在，增加协议类型（protocol)校验 - [zhike] 2bb164c

### v1.3.0

2016-11-14

* 升级二级版本号为3，对框架提供大模块功能的变动。主要是提供了远程BOSS管理集成能力。
* 2016-11-14 - 提供openApi服务集成的三种方式: allinone表示网关服务和本地管理功能; gateway表示纯粹的网关服务+管理功能的provider; manage表示远程管理功能的consumer - [zhangpu] d2cae77
* 2016-11-14 - 新增boss管理功能的provider和consumer模块,实现可以远程方式管理boss功能 - [zhangpu] cb4fd5d
* 2016-11-14 - 重构框架基础服务模块,包括分离domain模块;分离出现的service接口模块,新增service本地持久化模块等 - [zhangpu] 7919c72

### v1.2.3

2016-10-25

* 2016-10-25 - 优化sdk - [志客] c5f7af6
* 2016-10-25 - 独立导入Openapi-Framwork-sdk模块用到的jar版本 - [志客] f3778f9
* 2016-10-25 - 重构Openapi-Framwork-sdk模块，调整相关模块代码 - [志客] a12a387

### v1.2.2

* 2016-10-21 - 将ApiServiceException和OrderCheckException移到common模块中去 - [志客] e32db03

### v1.2.1

2016-10-18

* 2016-10-18 - 优化orderInfo管理查询界面,删除不需要的参数,调整显示结构 - [zhangpu] 2b32afa
* 2016-10-18 - [issue#7]增加条件可选annotaion: OpenApiFiledCondition,用于标注每个报文项为条件可选,并要求必须填写条件说明。 - [zhangpu] cfc3aba
* 2016-10-18 - 增加管理功能的权限资源菜单配置和更新初始化SQL - [zhangpu] 9b37654
* 2016-07-31 - 添加2个冗余的配置文件,用于方便模块开发测试 - [zhangpu] aa719c6

### v1.2.0

2016-07-31

* 2016-07-31 - 完成openapi-framework服务层所有基础开发工程,并完成文档编写,升级为1.2.0版本 - [zhangpu] c4a1162
* 2016-07-31 - 完成manage模块的integration开发,如果加入manage模块,则可以使用manage对应的authRealm实现。 - [zhangpu] 00cecff
* 2016-07-31 - 完成manage模块的integration开发,如果加入manage模块,则可以使用manage对应的authRealm实现。 - [zhangpu] 54f2b9b
* 2016-07-31 - 增加openapi-framework-servlet.xml用于管理controller配置,需要在springMVCDispathServlet中加入配置 - [zhangpu] d3d756e
* 2016-07-31 - 异步通知发送模块解耦重构,通过代理模式实现外部注入通知发送的实现。 - [zhangpu] 97c8a61
* 2016-07-30 - 完成基础管理功能的开发,包括:接入方管理, 服务类型和服务管理,接入方权限管理等 - [zhangpu] 8260709

### v1.1.3

2016-07-27

* [new] 更新api_notify_message的初始化DDL, 增加resp_info, requestNo, merchOrderNo三个字段
* [new] 完成异步通知的管理功能,提供查询和修改(人工方式出发重新发送,免于数据订正)
* [mod] 调整nms的异步通知扫描频率为2分钟,后续计划修改为可配置,测试环境1分钟,生成环境2-5分钟
* [fix] 重构nms包结构,修复了异步通知中,失败通知发送失败BUG。 - 2016-07-27 04:08:11

### v1.1.2

2016-07-13

* [add] 新增特性,支持ORACLE数据库(core和nms)。
* [fix] nms组件首次通知更新数据库状态是不BUG。 - 2016-07-13 03:07:54 +0800 (3 days ago)
* [add] 添加.gitignore文件,配置工程项目忽略文件策略 - 2016-07-11 14:00:21 +0800

### v1.1.1

2016-07-06

* 610b22a - zhangpu - [mod] PageApiRequest的基类从AppReqeust修改为ApiRequest - 2016-07-06 11:35:24 +0800 (2 days ago)
* 5494599 - zhangpu - SDK部分瘦身和提供商户端常用工具类 - 2016-07-06 11:33:20 +0800 (2 days ago)

### v1.1.1-SNOTSHAP.20160627

2016-06-27

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

2016-06-15

* 升级兼容requestNo/orderNo
* 完成自动化SDK特性开发

### v1.0.0

2016-04-01

* 基础版本发布,以openApi-arch为基础,重构几乎所有组件的实现(采用spring3方式的接口发现注入方式),大部分模块可以在集成的工作方便的扩展和自定义.
* 修改原有的orderNo为requestNo,但兼容orderNo请求,兼容内部openApi全兼容切换.
* 可用模块为core和nms,单进程运行.
