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
        <div class="container-wrapper">
		<div class="container">
            <!--左边-->
            <div class="container-left">
                <@includePage path="/docs/scheme/menu.html?page=scheme&category=api"/>
            </div>
            <!--右边-->
            <div class="container-main">
                <div class="doc-header">
                    <div class="doc-header-title"><i class="layui-icon">&#xe62a;</i> ${schemeName}</div>
                </div>
                <div class="doc-content">
                    <div>
                        <form class="layui-form" action="/docs/scheme/scheme.html" style="margin-top:20px;">
                            <input type="hidden" name="schemeId" value="${schemeId}">
                            <div class="layui-form-item">
                                <div class="layui-input-inline" style="width: 600px;">
                                    <input type="text" style="width: 600px;" name="key" value="${key}"  placeholder="全局服务码，名称搜索" class="layui-input">
                                </div>
                                <button type="submit" class="layui-btn layui-btn-normal">
                                    <i class="layui-icon layui-icon-search layui-font-12"></i>
                                    查询
                                </button>
                            </div>
                        </form>
                    </div>
                    <table class="layui-table" lay-skin="line">
                        <thead>
                        <tr>
                            <th style="width: 25%;">服务码</th>
                            <th style="width: 10%;">版本</th>
                            <th style="width: 10%;">服务类型</th>
                            <th style="width: 45%;">说明</th>
                            <th style="width: 10%;">元数据</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list apis as e>
                        <tr>
                            <td><a target="_blank" href="/docs/apidoc/scheme/${schemeId}/${e.serviceNo}.html">${e.name}</a></td>
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
        </div>
		<!--end-->

		<!--底部-->
        <div style="position: fixed;bottom: 0;width: 100%;">
		<@includePage path="/docs/common/footer.html"/>
        </div>
		<!--end-->


	<script>


        layui.use('element', function(){
            var element = layui.element; //导航的hover效果、二级菜单等功能，需要依赖element模块
            element.init();
        });

	</script>



	</body>
</html>
