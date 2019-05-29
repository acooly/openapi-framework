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
              <a href="/docs/scheme/index.html">API文档</a>
              <a><cite>${schemeName}</cite></a>
            </span>
            </div>
        </div>
		
		<!--正文-->
		<div class="container">
            <!--左边-->
            <div class="container-left">
                <@includePage path="/docs/scheme/menu.html?page=scheme"/>
            </div>
            <!--右边-->
            <div class="container-main">
                <div class="doc-header">
                    <div class="doc-header-title"><i class="layui-icon">&#xe62a;</i> ${schemeName}</div>
                </div>
                <div class="doc-content">

                    <div>
                        ${apiSchemeDesc.schemeDesc}
                    </div>

                    <table class="layui-table" lay-skin="line">
                        <thead>
                        <tr>
                            <th>服务码</th>
                            <th>版本</th>
                            <th>服务类型</th>
                            <th>说明</th>
                            <th>元数据</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list apis as e>
                        <tr>
                            <td><a href="/docs/apidoc/scheme/${schemeId}/${e.serviceNo}.html">${e.name}</a></td>
                            <td>${e.version}</td>
                            <td>${e.serviceType}</td>
                            <td>${e.title}</td>
                            <td><a target="_blank" href="/docs/apidoc/metadata.html?id=${e.id}">查看</a></td>
                        </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>
            </div>
		</div>
		<!--end-->
		
		<!--底部-->
		<@includePage path="/docs/common/footer.html"/>
		<!--end-->


	<script>


        layui.use('element', function(){
            var element = layui.element; //导航的hover效果、二级菜单等功能，需要依赖element模块
            element.init();
        });

	</script>



	</body>
</html>
