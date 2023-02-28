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
              <a><cite>${apiScheme.name}</cite></a>
              <a><cite>${content.title}</cite></a>
            </span>
    </div>
</div>

<!--正文-->
<div class="container-wrapper">
    <div class="container">
        <!--左边-->
        <div class="container-left">
            <@includePage path="/docs/apischeme/menu.html?category=api&page=${content.id}"/>
        </div>
        <!--右边-->
        <div class="container-main">
            <div class="doc-header">
                <div class="doc-header-title"><i class="layui-icon">&#xe62a;</i> ${content.title}</div>
            </div>
            <div class="doc-content">
                ${content.contentBody.body}
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
