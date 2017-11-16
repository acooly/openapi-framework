## 1. 组件介绍

提供app访问的openapi接口

1. app通过匿名帐号，访问匿名服务服务。参考showcase:`com.ac.test.openapi.AnonymousAppApiServiceTest#testBannerList`
2. 访问非匿名服务时，先放问登录接口，下发动态密钥，再通过动态密钥访问服务。参考showcase:`com.ac.test.openapi.AnonymousAppApiServiceTest#testLogin`

## 2. 配置

1. 需要配置匿名账户相关信息，用于app匿名访问登录或其他服务
2. 应用需要实现`com.acooly.module.appopenapi.support.AppApiLoginService`,提供app登录认证需求
3. 配置异步通知超时时间
   `acooly.appopenapi.notifysender.connectionTimeout=7000
       acooly.appopenapi.notifysender.socketTimeout=8000`

## 3. 使用

客户端需要设置请求参数中`appClient`为true
