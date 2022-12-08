<!DOCTYPE html>
<html>
<head>
		<@includePage path="/docs/common/meta.html"/>
		<@includePage path="/docs/common/include.html"/>
    <link rel="stylesheet" type="text/css" href="/portal/style/css/api.css"/>
</head>
<body>
<!--顶部-->
		<@includePage path="/docs/common/header.html"/>
<!--end-->

<!--面包屑-->
<div class="crumb">
    <div class="w1190">
            <span class="layui-breadcrumb">
              <a href="/docs/scheme/index.html">API文档</a>
              <a><cite>解决方案</cite></a>
            </span>
    </div>
</div>

<!--正文-->

<div class="container">
    <!--左边-->
    <div class="container-left">
                <@includePage path="/docs/scheme/menu.html?page=index&category=api"/>
    </div>
    <!--右边-->
    <div id="dataCont" class="container-main">
        <div class="doc-header">
            <div class="doc-header-title"><i class="layui-icon">&#xe62a;</i> 使用说明</div>
        </div>
        <div class="doc-content">

            <div class="switch">
                <div class="switch-title">
                    <a name="apidoc_subject">简介</a>
                </div>
                <div class="switch-content">
                    <div>
                        开放平台致力于为商户提供完整的支付解决方案，并提供在线联调的功能。通过开放平台，接入商户可以快速了解接入方式，操作步骤。商户开发人员可以方便的找到《开发规范》和所有OpenAPI的详细文档。
                    </div>
                    <ul>
                        <li><span>生产环境地址：</span><a target="_blank" href="${gateway}">${gateway}</a></li>
                        <li><span>测试环境地址：</span><a target="_blank" href="${testGateway}">${testGateway}</a></li>
                    </ul>
                </div>
            </div>

            <div class="switch">
                <div class="switch-title">
                    <a name="apidoc_subject">功能描述</a>
                </div>
                <div class="switch-content">
                    <ul>
                        <li>完善的商户开发规范文档</li>
                        <li>产品解决方案归类和API列表</li>
                        <li>OpenAPI文档及报文详细</li>
                        <li>商户账户信息（商户ID、商户密钥、服务列表、下载服务文档和下载数字证书）</li>
                        <li>商户在线联调接口</li>
                        <li>商户接入SDK和工具下载</li>
                        <li>参加问题查询和QA列表</li>
                        <li>意见反馈</li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>
<!--end-->

<!--底部-->
<div style="position: fixed;bottom: 0;width: 100%;">
    <@includePage path="/docs/common/footer.html"/>
</div>
<!--end-->

<script>

    layui.use('element', function () {
        var element = layui.element; //导航的hover效果、二级菜单等功能，需要依赖element模块
        element.init();
    });

</script>


</body>
</html>
