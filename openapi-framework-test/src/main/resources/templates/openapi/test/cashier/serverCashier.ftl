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
        <div class="layui-col-xs4">
            <div>第一步：客户端订单支付界面</div>
        </div>
        <div class="layui-col-xs4 openapi-header-active">
            <div>第二步：服务端支付收银台</div>
        </div>
        <div class="layui-col-xs4">
            <div>第三步：客户端接收跳回结果</div>
        </div>
    </div>
</div>

<div class="layui-main">
    <form class="layui-form" action="/openapi/test/orderCashierPay/server/cashierPay.html" method="post" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="_csrf" value="${Request["org.springframework.security.web.csrf.CsrfToken"].token}">
        <input type="hidden" name="gid" value="${gid}">
        <input type="hidden" name="partnerId" value="${partnerId}">
        <div class="layui-form-item">
            <label class="layui-form-label">订单号</label>
            <div class="layui-input-block">
                <input type="text" name="merchOrderNo" value="${entity.merchOrderNo}" lay-verify="required" lay-reqtext="订单号是必填项。" autocomplete="off" placeholder="请输入客户端订单号" class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">支付金额</label>
            <div class="layui-input-block">
                <input type="number" name="amount" value="${entity.amount}" lay-verify="required" lay-reqtext="金额是必填项。" placeholder="请输入订单金额。" autocomplete="off" class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">买家用户</label>
            <div class="layui-input-block">
                <input type="text" name="payerUserId" value="${entity.payerUserId}" lay-verify="required" lay-reqtext="买家userId必须的。" autocomplete="off" placeholder="请输入用户标志" class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">支付结果</label>
            <div class="layui-input-block">
                <select name="payStatus" lay-verify="required">
                    <option value="processing">处理中</option>
                    <option value="success">支付成功</option>
                    <option value="failure">支付失败</option>
                </select>
            </div>
        </div>
        <div class="layui-form-item">
            <div class="layui-input-block">
                <button type="submit" class="layui-btn" lay-submit lay-filter="formClientPay">立即提交</button>
                <button type="reset" class="layui-btn layui-btn-primary">重置</button>
            </div>
        </div>
    </form>

    <script>
        //Demo
        layui.use('form', function(){
            var form = layui.form;
            form.render();
        });
    </script>
</div>

</body>
</html>