<div class="doc-menu">
    <div class="doc-menu-header">
        ${apiScheme.title}<s>（${apidocs?size}）</s>
        <i class="icon_menu_switch"></i>
    </div>
    <ul>
        <#list apidocs as e>
            <li id="apidoc_api_${e.id}" <#if e.serviceNo == serviceNo>class="item-this"</#if>>
                <a href="/docs/apidoc/scheme/${apiScheme.id}/${e.serviceNo}.html" title="${e.title}">
                    <div>${e.name}</div>
                    <div>${e.title}</div>
                </a>
            </li>
        </#list>


        <li id="apidoc_api_1">
            <a href="/docs/apidoc/scheme/1/auth_1.0.html" title="验签认证服务">
                <div>auth1</div>
                <div>验签认证服务</div>
            </a>
        </li>
        <li id="apidoc_api_2">
            <a href="/docs/apidoc/scheme/1/login_1.0.html" title="用户登录">
                <div>login1</div>
                <div>用户登录</div>
            </a>
        </li>
        <li id="apidoc_api_23">
            <a href="/docs/apidoc/scheme/1/demoGoodsList_1.0.html" title="商品列表">
                <div>demoGoodsList1</div>
                <div>商品列表</div>
            </a>
        </li>
        <li id="apidoc_api_28">
            <a href="/docs/apidoc/scheme/1/demoWithdraw_1.0.html" title="测试：提现异步接口">
                <div>demoWithdraw1</div>
                <div>测试：提现异步接口</div>
            </a>
        </li>
        <li id="apidoc_api_27">
            <a href="/docs/apidoc/scheme/1/demoTreeNodeList_1.0.html" title="测试：树形结构查询">
                <div>demoTreeNodeList1</div>
                <div>测试：树形结构查询</div>
            </a>
        </li>
        <li id="apidoc_api_25">
            <a href="/docs/apidoc/scheme/1/demoOrderCreate_1.0.html" title="测试：创建订单服务">
                <div>demoOrderCreate1</div>
                <div>测试：创建订单服务</div>
            </a>
        </li>
        <li id="apidoc_api_24">
            <a href="/docs/apidoc/scheme/1/demoOrderCashierPay_1.0.html" title="测试：订单收银台支付">
                <div>demoOrderCashierPay1</div>
                <div>测试：订单收银台支付</div>
            </a>
        </li>

        <li id="apidoc_api_1">
            <a href="/docs/apidoc/scheme/1/auth_1.0.html" title="验签认证服务">
                <div>auth1</div>
                <div>验签认证服务</div>
            </a>
        </li>
        <li id="apidoc_api_2">
            <a href="/docs/apidoc/scheme/1/login_1.0.html" title="用户登录">
                <div>login1</div>
                <div>用户登录</div>
            </a>
        </li>
        <li id="apidoc_api_23">
            <a href="/docs/apidoc/scheme/1/demoGoodsList_1.0.html" title="商品列表">
                <div>demoGoodsList1</div>
                <div>商品列表</div>
            </a>
        </li>
        <li id="apidoc_api_28">
            <a href="/docs/apidoc/scheme/1/demoWithdraw_1.0.html" title="测试：提现异步接口">
                <div>demoWithdraw1</div>
                <div>测试：提现异步接口</div>
            </a>
        </li>
        <li id="apidoc_api_27">
            <a href="/docs/apidoc/scheme/1/demoTreeNodeList_1.0.html" title="测试：树形结构查询">
                <div>demoTreeNodeList1</div>
                <div>测试：树形结构查询</div>
            </a>
        </li>
        <li id="apidoc_api_25">
            <a href="/docs/apidoc/scheme/1/demoOrderCreate_1.0.html" title="测试：创建订单服务">
                <div>demoOrderCreate1</div>
                <div>测试：创建订单服务</div>
            </a>
        </li>
        <li id="apidoc_api_24">
            <a href="/docs/apidoc/scheme/1/demoOrderCashierPay_1.0.html" title="测试：订单收银台支付">
                <div>demoOrderCashierPay1</div>
                <div>测试：订单收银台支付</div>
            </a>
        </li>
        <li id="apidoc_api_1">
            <a href="/docs/apidoc/scheme/1/auth_1.0.html" title="验签认证服务">
                <div>auth1</div>
                <div>验签认证服务</div>
            </a>
        </li>
        <li id="apidoc_api_2">
            <a href="/docs/apidoc/scheme/1/login_1.0.html" title="用户登录">
                <div>login1</div>
                <div>用户登录</div>
            </a>
        </li>
        <li id="apidoc_api_23">
            <a href="/docs/apidoc/scheme/1/demoGoodsList_1.0.html" title="商品列表">
                <div>demoGoodsList1</div>
                <div>商品列表</div>
            </a>
        </li>
        <li id="apidoc_api_28">
            <a href="/docs/apidoc/scheme/1/demoWithdraw_1.0.html" title="测试：提现异步接口">
                <div>demoWithdraw1</div>
                <div>测试：提现异步接口</div>
            </a>
        </li>
        <li id="apidoc_api_27">
            <a href="/docs/apidoc/scheme/1/demoTreeNodeList_1.0.html" title="测试：树形结构查询">
                <div>demoTreeNodeList1</div>
                <div>测试：树形结构查询</div>
            </a>
        </li>
        <li id="apidoc_api_25">
            <a href="/docs/apidoc/scheme/1/demoOrderCreate_1.0.html" title="测试：创建订单服务">
                <div>demoOrderCreate1</div>
                <div>测试：创建订单服务</div>
            </a>
        </li>
        <li id="apidoc_api_24">
            <a href="/docs/apidoc/scheme/1/demoOrderCashierPay_1.0.html" title="测试：订单收银台支付">
                <div>demoOrderCashierPay1</div>
                <div>测试：订单收银台支付</div>
            </a>
        </li>

        <li id="apidoc_api_1">
            <a href="/docs/apidoc/scheme/1/auth_1.0.html" title="验签认证服务">
                <div>auth1</div>
                <div>验签认证服务</div>
            </a>
        </li>
        <li id="apidoc_api_2">
            <a href="/docs/apidoc/scheme/1/login_1.0.html" title="用户登录">
                <div>login1</div>
                <div>用户登录</div>
            </a>
        </li>
        <li id="apidoc_api_23">
            <a href="/docs/apidoc/scheme/1/demoGoodsList_1.0.html" title="商品列表">
                <div>demoGoodsList1</div>
                <div>商品列表</div>
            </a>
        </li>
        <li id="apidoc_api_28">
            <a href="/docs/apidoc/scheme/1/demoWithdraw_1.0.html" title="测试：提现异步接口">
                <div>demoWithdraw1</div>
                <div>测试：提现异步接口</div>
            </a>
        </li>
        <li id="apidoc_api_27">
            <a href="/docs/apidoc/scheme/1/demoTreeNodeList_1.0.html" title="测试：树形结构查询">
                <div>demoTreeNodeList1</div>
                <div>测试：树形结构查询</div>
            </a>
        </li>
        <li id="apidoc_api_25">
            <a href="/docs/apidoc/scheme/1/demoOrderCreate_1.0.html" title="测试：创建订单服务">
                <div>demoOrderCreate1</div>
                <div>测试：创建订单服务</div>
            </a>
        </li>
        <li id="apidoc_api_24">
            <a href="/docs/apidoc/scheme/1/demoOrderCashierPay_1.0.html" title="测试：订单收银台支付">
                <div>demoOrderCashierPay1</div>
                <div>测试：订单收银台支付</div>
            </a>
        </li>

        <li id="apidoc_api_1">
            <a href="/docs/apidoc/scheme/1/auth_1.0.html" title="验签认证服务">
                <div>auth1</div>
                <div>验签认证服务</div>
            </a>
        </li>
        <li id="apidoc_api_2">
            <a href="/docs/apidoc/scheme/1/login_1.0.html" title="用户登录">
                <div>login1</div>
                <div>用户登录</div>
            </a>
        </li>
        <li id="apidoc_api_23">
            <a href="/docs/apidoc/scheme/1/demoGoodsList_1.0.html" title="商品列表">
                <div>demoGoodsList1</div>
                <div>商品列表</div>
            </a>
        </li>
        <li id="apidoc_api_28">
            <a href="/docs/apidoc/scheme/1/demoWithdraw_1.0.html" title="测试：提现异步接口">
                <div>demoWithdraw1</div>
                <div>测试：提现异步接口</div>
            </a>
        </li>
        <li id="apidoc_api_27">
            <a href="/docs/apidoc/scheme/1/demoTreeNodeList_1.0.html" title="测试：树形结构查询">
                <div>demoTreeNodeList1</div>
                <div>测试：树形结构查询</div>
            </a>
        </li>
        <li id="apidoc_api_25">
            <a href="/docs/apidoc/scheme/1/demoOrderCreate_1.0.html" title="测试：创建订单服务">
                <div>demoOrderCreate1</div>
                <div>测试：创建订单服务</div>
            </a>
        </li>
        <li id="apidoc_api_24">
            <a href="/docs/apidoc/scheme/1/demoOrderCashierPay_1.0.html" title="测试：订单收银台支付">
                <div>demoOrderCashierPay1</div>
                <div>测试：订单收银台支付</div>
            </a>
        </li>

    </ul>
</div>

<script>

    $('.doc-menu ul li').click(function () {
        $('.doc-menu ul li').removeClass("item-this");
        $(this).addClass("item-this");
    });

    $('.doc-menu .doc-menu-header').click(function () {
        if ($(this).next('ul').is(':hidden')) {
            $(this).next('ul').show();
        } else {
            $(this).next('ul').hide();
        }
    });

</script>
