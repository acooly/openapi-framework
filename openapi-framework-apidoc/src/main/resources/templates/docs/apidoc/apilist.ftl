<!DOCTYPE html>
<html>
<head>
    <@includePage path="/docs/common/meta.html"/>
    <@includePage path="/docs/common/include.html"/>
    <script type="text/javascript" src="/plugin/baiduTemplate.js"></script>
</head>
<body>
<!--顶部-->
<@includePage path="/docs/common/header.html"/>
<!--end-->

<!--面包屑-->
<div class="crumb">
    <div class="w1190">
            <span class="layui-breadcrumb">
              <a href="/docs/index.html">文档中心</a>
              <a><cite>文档首页</cite></a>
            </span>
    </div>
</div>

<!--正文-->
<div class="container-wrapper">
    <div class="container">
        <!--左边-->
        <div class="container-left">
            <@includePage path="/docs/apischeme/menu.html?page=apilist&category=api"/>
        </div>
        <!--右边-->
        <div class="container-main">
            <div class="doc-header">
                <div class="doc-header-title"><i class="layui-icon">&#xe62a;</i> ${schemeName}</div>
            </div>
            <div class="doc-content">
                <table class="layui-table" lay-skin="line">
                    <thead>
                    <tr>
                        <th>服务码</th>
                        <th>版本</th>
                        <th>服务类型</th>
                        <th>说明</th>
                        <th><input type="checkbox" id="serviceId"> 全选</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list apis as e>
                        <tr>
                            <td><a href="/docs/apidoc/scheme/${schemeId}/${e.service}.html">${e.service}</a></td>
                            <td>${e.version}</td>
                            <td>${e.serviceType}</td>
                            <td>${e.serviceName}</td>
                            <td><input name="serviceId" value="${e.id}" type="checkbox"></td>
                        </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<!--end-->

<!--底部-->
<@includePage path="/docs/common/footer.html"/>
<!--end-->
<script>


    layui.use('element', function () {
        var element = layui.element; //导航的hover效果、二级菜单等功能，需要依赖element模块
        element.init();
    });

</script>


</body>
</html>
