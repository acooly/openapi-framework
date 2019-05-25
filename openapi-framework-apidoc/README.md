<!-- title: OpenApi文档自动化 -->
<!-- type: openapi -->
<!-- author: zhangpu -->
<!-- date: 2019-02-04 -->
OpenApi文档自动化
====
openapi-framework-apidoc
----

## 简介

文档自动化是OpenApi开放平台的核心功能，实现根据Api服务代码，自动生成Api文档和开发平台网站的功能。

访问地址：http://{host}/docs/

[演示地址](http://showcase.acooly.cn/docs)

## 特性

* 开发平台网站，可配置网站基本信息（logo，copyright等）
* 根据OpenApi服务代码自动解析生成文档并展示文档。
* QA常见问题功能。
* [待完善] 在线调试接口
* [待开发] 菜单配置，自定义菜单，可结合CMS提供自定义界面。

## 配置

```
## [可选] 是否写入所有的服务到默认的通用scheme（方案/菜单）
acooly.openapi.apidoc.defaultSchemeEnable=true
## [可选] 是否显示默认的通用scheme
acooly.openapi.apidoc.defaultSchemeShow=true
## [可选] 生产网关地址
acooly.openapi.apidoc.gateway=http://api.xxx.com/gateway.do
## [可选] 测试网关地址
acooly.openapi.apidoc.testGateway=http://127.0.0.1:8089/gateway.do
## [可选] 开发平台网站标题
acooly.openapi.apidoc.portal.title=Acooly-OpenApi 开放平台
## [可选] 开发平台网站版权信息
acooly.openapi.apidoc.portal.copyright=@Copyright Acooly 2019
## [可选] 开发平台网站logo
acooly.openapi.apidoc.portal.logo=/portal/images/logo/logo-acooly-white.png
```
