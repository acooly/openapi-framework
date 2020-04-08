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
        <div class="layui-col-xs4 openapi-header-active" >
            <div>第一步：客户端订单支付界面</div>
        </div>
        <div class="layui-col-xs4">
            <div>第二步：服务端支付收银台</div>
        </div>
        <div class="layui-col-xs4">
            <div>第三步：客户端接收跳回结果</div>
        </div>
    </div>
</div>

<div class="layui-main">
    <form class="layui-form" action="/openapi/test/orderCashierPay/client/pay.html" method="post" enctype="application/x-www-form-urlencoded">
            <input type="hidden" name="_csrf" value="${Request["org.springframework.security.web.csrf.CsrfToken"].token}">
            <div class="layui-form-item">
                <label class="layui-form-label">订单号</label>
                <div class="layui-input-block">
                    <input type="text" name="merchOrderNo" value="${merchOrderNo}" lay-verify="required" lay-reqtext="订单号是必填项。" autocomplete="off" placeholder="请输入客户端订单号" class="layui-input">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">订单标题</label>
                <div class="layui-input-block">
                    <input type="text" name="title" value="就是一个测试支付订单" lay-verify="required" lay-reqtext="订单号是必填项。" autocomplete="off" placeholder="请输入标题" class="layui-input">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">订单额</label>
                <div class="layui-input-block">
                    <input type="number" name="amount" lay-verify="required" lay-reqtext="金额是必填项。" placeholder="请输入订单金额。" autocomplete="off" class="layui-input">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">买家用户</label>
                <div class="layui-input-block">
                    <input type="text" name="payerUserId" value="12345678900123456" lay-verify="required" lay-reqtext="买家userId必须的。" autocomplete="off" placeholder="请输入用户标志" class="layui-input">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">买家手机</label>
                <div class="layui-input-block">
                    <input type="tel" value="13787647630" name="buyeryMobileNo" lay-verify="required|phone" autocomplete="off" class="layui-input">
                </div>
            </div>

            <div class="layui-form-item">
                <div class="layui-input-block">
                    <button type="submit" class="layui-btn" lay-submit lay-filter="formClientPay">立即提交</button>
                    <button type="reset" class="layui-btn layui-btn-primary">重置</button>
                </div>
            </div>
        </form>

    <blockquote class="layui-elem-quote">
        这里是Mock接入方的订单支付请求界面。该界面和功能由接入方实现，主要做以下操作：
        <ul>
            <li>1、准备数据并调用：orderCreate接口创建订单号，注意，这里需要设置returnUrl和notifyUrk，并记录：merchOrderNo</li>
            <li>2、准备调用收银台支付数据，调用orderCashierPay跳转接口（这里可以设置新的returnUrl和notifyUrk覆盖orderCreate的设置），跳转到OpenApi网关。</li>
            <li>3、开发returnUrl界面和notifyUrl处理</li>
        </ul>
        具体实现请参考：OrderCashierPayClientTestController
    </blockquote>
</div>



</body>
</html>