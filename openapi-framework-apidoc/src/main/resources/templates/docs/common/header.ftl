<!--顶部-->
<ul class="layui-nav <#if rc.requestUri?contains("/docs/index.html") || rc.requestUri?ends_with("/docs/")>transparent</#if>" lay-filter="">
    <div class="w1190">
    <li class="layui-nav-logo api-logo"><img src="${logo}" alt="" /></li>
    <li class="layui-nav-split w100">${Request['requestURL']}</li>
    <li class="layui-nav-item" tag="apischeme"><a href="/docs/scheme/index.html">API文档</a></li>
    <li class="layui-nav-item" tag="apidebug"><a href="/docs/apidebug/index.html">在线调试</a></li>
        <li class="layui-nav-item">
            <a href="javascript:;">文档中心</a>
            <dl class="layui-nav-child"> <!-- 二级菜单 -->
                <dd><a href="#">OpenApi网关架构</a></dd>
                <dd><a href="#">OpenApi服务开发</a></dd>
                <dd><a href="#">OpenApi接入开发</a></dd>
            </dl>
        </li>
    <li class="layui-nav-item">
        <a href="javascript:;">帮助中心</a>
        <dl class="layui-nav-child"> <!-- 二级菜单 -->
            <dd><a href="#">QA</a></dd>
            <dd><a href="#">联系我们</a></dd>
        </dl>
    </li>
    </div>
</ul>
<script>
    var path = window.location.pathname;
    $(".layui-nav-item").each(function(){
        if(path.indexOf($(this).attr("tag")) != -1){
            $(this).addClass("layui-this");
        }
    });
</script>
<!--顶部 end-->
