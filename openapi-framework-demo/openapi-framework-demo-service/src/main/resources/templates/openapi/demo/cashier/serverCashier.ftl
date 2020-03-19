<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="Keywords" content=""/>
    <meta name="Description" content=""/>
    <meta name="X-CSRF-TOKEN" content="${Request["org.springframework.security.web.csrf.CsrfToken"].token}"/>
    <title>Acooly OpenApi V5.x 测试案例</title>
    <link rel="stylesheet" href="/plugin/layui/css/layui.css">
    <link rel="stylesheet" href="/css/openapi.css">
    <script src="https://cdn.bootcss.com/jquery/3.4.1/jquery.min.js"></script>
    <script src="/plugin/layui/layui.all.js"></script>
    <script type="text/javascript">
        /**
         * CSRF-Token 客户端支持脚本。
         */
        var token = $("meta[name='X-CSRF-TOKEN']").attr("content");// 从meta中获取token
        $(document).ajaxSend(function(e, xhr, options) {
            if (!options.crossDomain) {
                xhr.setRequestHeader("X-CSRF-TOKEN", token);// 每次ajax提交请求都会加入此token
            }
        });
        $(function() {
            // CSRF自动为当前页码的所有表单添加hidden(csrfToken)
            $("form").each(function() {
                if ($(this).attr('enctype') == 'multipart/form-data') {
                    var action = $(this).attr('action');
                    action += (action.indexOf('?') != -1 ? '&' : '?');
                    action += "_csrf=${Request['org.springframework.security.web.csrf.CsrfToken'].token}";
                    $(this).attr('action', action);
                } else {
                    $(this).append("<input type=\"hidden\" name=\"_csrf\" value=\"${Request['org.springframework.security.web.csrf.CsrfToken'].token}\"/>");
                }
            });
        });
    </script>
</head>
<body>

<div class="layui-container">
    <!--顶部-->
    <ul class="layui-nav">
        <li class="layui-nav-logo" style="font-size: 18px">支付收银台</li>
    </ul>
    <script>
        layui.use('element', function () {
            var util = layui.util
            var element = layui.element; //导航的hover效果、二级菜单等功能，需要依赖element模块
            element.init();
        });
    </script>
    <!--顶部 end-->
</div>

<div class="layui-container">
    <!--顶部-->
    <div class="layui-row openapi-header">
        <div class="layui-col-xs4 openapi-header-title">
            <div>网关支付收银台（演示）</div>
        </div>
    </div>
</div>

<div class="layui-main">
    <div class="openapi-block">
    <form class="layui-form" action="/openapi/demo/orderCashierPay/server/cashierPay.html" method="post" enctype="application/x-www-form-urlencoded">
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
</div>
<!--底部-->
<div class="layui-footer footer footer-index">
    <div class="layui-main">
        <div>© 2020 open.cnvex.cn ,open.cartechfin.cn</div>
        <div class="d1">重庆汽摩交易所&车云数科</div>
        <div>技术支持：Acooly</div>
    </div>
</div>
<!--end-->
</body>
</html>