<!--顶部-->
<ul class="layui-nav">
<li class="layui-nav-logo"><a href="/openapi/test/index.html"><img src="${logo}" style="height: 30px;" alt=""/></a></li>
<li class="layui-nav-item">
    <a href="javascript:;">测试接口</a>
    <dl class="layui-nav-child">
        <dd><a href="javascript:;">收银台(跳转接口)</a></dd>
        <dd><a href="javascript:;">提现(异步接口)</a></dd>
    </dl>
</li>
</ul>
<script>

    layui.use('element', function () {
        var util = layui.util
        var element = layui.element; //导航的hover效果、二级菜单等功能，需要依赖element模块
        element.init();
    });

</script>
<!--顶部 end-->
