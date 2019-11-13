<!DOCTYPE html>
<html>
<head>
    <@includePage path="/openapi/test/common/meta.ftl"/>
    <@includePage path="/openapi/test/common/include.ftl"/>
</head>
<body>
<!--顶部-->
<@includePage path="/openapi/test/common/header.ftl"/>

<div class="layui-container">
    <div class="layui-row openapi-header">
        <div class="layui-col-xs4 openapi-header-active">
            <div>第一步：客户端订单支付界面</div>
        </div>
        <div class="layui-col-xs4">
            <div>第二步：服务端支付收银台</div>
        </div>
        <div class="layui-col-xs4 openapi-header-active">
            <div>第三步：客户端接收跳回结果</div>
        </div>
    </div>
</div>

<div class="layui-main">

    <blockquote class="layui-elem-quote">
        这里是Mock接入方接受服务方会跳的returnUrl，用于给客户显示支付结果。
    </blockquote>
</div>


</body>
</html>