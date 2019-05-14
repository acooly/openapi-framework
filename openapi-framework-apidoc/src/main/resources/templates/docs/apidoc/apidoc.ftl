<!DOCTYPE html>
<html>
	<head>
		<@includePage path="/docs/common/meta.html"/>
		<@includePage path="/docs/common/include.html"/>
        <link rel="stylesheet" type="text/css" href="/portal/style/css/api.css"/>
        <link href="/plugin/tooltipster/css/tooltipster.bundle.css" rel="stylesheet">
        <link href="/plugin/tooltipster/css/plugins/tooltipster/sideTip/themes/tooltipster-sideTip-shadow.min.css" rel="stylesheet">
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
                <a><cite>${apiScheme.title}</cite></a>
            </span>
            </div>
        </div>

		<!--end-->

		<!--正文-->
        <div class="container">
            <div class="container-left">
                <!--左边-->
                <@includePage path="/docs/apidoc/menu.html?schemeId=${apiScheme.id}&schemeNo=${apiScheme.schemeNo}"/>
            </div>

            <!--右边-->
            <div id="dataCont" class="container-main"></div>

		</div>
		<!--end-->

        <!-- 隐藏:metadata元数据渲染容器 -->
        <div class="apidoc-metadata" style="display: none;" id="apidoc_metadata_container"></div>
        <!--右侧定位导航-->
        <div id="apidoc_content_menu" class="doc-content-dir"></div>

		<!--底部-->
		<@includePage path="/docs/common/footer.html"/>
		<!--end-->

<script id="apidoc_template" type="text/html">
    <div class="doc-header">
        <div class="doc-header-title"><i class="layui-icon">&#xe62a;</i> <%=entity.title%></div>
        <div class="doc-header-more">
            <#--<button onclick="location.href='/docs/apidebug/index.html?service=<%=entity.name%>'" class="layui-btn layui-btn-small layui-btn-radius">-->
                <#--<i class="layui-icon">&#xe64e;</i> 点此联调-->
            <#--</button>-->
            <button onclick="showMetaData('<%=entity.id%>','<%=entity.serviceNo%>')" class="layui-btn layui-btn-small layui-btn-radius">
                <i class="layui-icon">&#xe63c;</i> 元数据
            </button>
        </div>
    </div>
    <div class="doc-subject">
        <span>服务名：<%=entity.name%></span>
        <span class="ml20">版本号：<span class="layui-badge layui-bg-gray"><%=entity.version%></span></span>
        <span class="ml20">通讯模式：<a href="javascript:;"><%=data.allServiceTypes[entity.serviceType]%></a></span>
    </div>
    <div class="doc-content">
            <div class="switch">
                <div class="switch-title" id="cs">
                    <a name="apidoc_subject">接口说明</a>
                </div>
                <div class="switch-content">
                    <%=entity.note%>
                </div>
            </div>

            <% for(var i=0;i<entity.apiDocMessages.length;i++){ var e=entity.apiDocMessages[i]; if(e.messageType != 'Redirect'){%>
            <div class="switch">
                <div class="switch-title" id="cs">
                    <a name="apidoc_<%=e.messageType%>"><%=data.allMessageTypes[e.messageType]%> <%=e.messageType%></a>
                </div>
                <div class="switch-content">
                    <table class="layui-table">
                        <thead>
                        <tr>
                            <th style="width:120px;">参数</th>
                            <th style="width:120px;">参数名称</th>
                            <th style="width:60px;">类型</th>
                            <th style="width:50px;">必填</th>
                            <th style="width:50px;">加密</th>
                            <th style="width:200px;">参数说明</th>
                            <th>示例</th>
                        </tr>
                        </thead>
                        <tr>
                            <%
                            var anchor;
                            if(e.messageType == 'Request'){ anchor = 'toc421'; }else if(e.messageType == 'Response'){ anchor = 'toc422'; }else{ anchor = 'toc423'; }
                            %>
                            <td colspan="7">公共参数: <a href="http://acooly.cn/docs/component/openapi-framework-common.html#<%=anchor%>" target="_blank">公共<%=e.messageType%>报文</a></td>
                        </tr>
                        <%
                        if(e.apiDocItems && e.apiDocItems.length > 0){
                          for(var j=0;j<e.apiDocItems.length;j++){
                            var item = e.apiDocItems[j];
                        %>
                        <tr>
                            <td>
                              <% if(item.children != null && item.children.length > 0){ %>
                                <a href="javascript:;" onclick="openSubMessage('<%=item.name%>','<%=item.name%>_<%=item.id%>')"><%=item.name%></a>
                                <div style="display: none;" id="<%=item.name%>_<%=item.id%>">
                                    <div style="padding: 0 10px;">
                                    <table class="layui-table">
                                        <thead>
                                        <tr>
                                            <th style="width:120px;">参数</th>
                                            <th style="width:120px;">参数名称</th>
                                            <th style="width:60px;">类型</th>
                                            <th style="width:50px;">必填</th>
                                            <th style="width:50px;">加密</th>
                                            <th style="width:200px;">参数说明</th>
                                            <th>示例</th>
                                        </tr>
                                        </thead>
                                        <%
                                        for(var k=0; k<item.children.length; k++){
                                            var subItem = item.children[k];
                                        %>
                                        <tr>
                                            <td><%=subItem.name%></td>
                                            <td><%=subItem.title%></td>
                                            <td><%=dataTypeFormatter(subItem) %></td>
                                            <td><%=data.allStatuss[subItem.status] %></td>
                                            <td><%=securityFormatter(subItem) %></td>
                                            <td class="hover-details"><%=descnFormatter(subItem)%></td>
                                            <td><%=demoFormatter(subItem)%></td>
                                        </tr>
                                        <% } %>
                                    </table>
                                    </div>
                                </div>
                              <% }else{ %>
                                <%=item.name%>
                              <% } %>
                            </td>
                            <td><%=item.title%></td>
                            <td><%=dataTypeFormatter(item) %></td>
                            <td><%=data.allStatuss[item.status] %></td>
                            <td><%=securityFormatter(item) %></td>
                            <td class="hover-details"><%=descnFormatter(item)%></td>
                            <td><%=demoFormatter(item)%></td>
                        </tr>

                        <% }} %>
                    </table>
                    <% if(e.messageType == 'Notify' || e.messageType == 'Return') { %>
                    <div class="layui-elem-quote layui-quote-nm">注意：默认情况下，异步通知报文（notifyUrl）和同步跳转通知报文（returnUrl）结构一致。</div>
                    <% } %>
                </div>
            </div>
            <% }} %>


            <div class="switch">
                <div class="switch-title">
                    <a name="apidoc_demo">示例报文</a>
                </div>
                <div class="switch-content" id="apidoc_demo_container"></div>
            </div>
    </div>
</script>

<script id="apidoc_demo_template" type="text/html">
<% for(var i=0;i<rows.length;i++){ var e=rows[i]; %>
<div class="api-demo-message">
    <div class="title"><%=e.messageType%></div>
    <pre><%=e.header%></pre>
    <div class="body" id="apidoc_demo_<%=e.messageType%>"></div>
</div>
<%}%>

</script>

<!-- 右侧浮动菜单模板 -->
<script id="apidoc_content_menu_template" type="text/html">
<ul>
    <li><a href="#apidoc_subject">接口说明</a></li>
    <% for(var i=0;i<entity.apiDocMessages.length;i++){ var e=entity.apiDocMessages[i]; if(e.messageType != 'Redirect'){%>
    <li><a href="#apidoc_<%=e.messageType%>"><%=data.allMessageTypes[e.messageType]%></a></li>
    <% }} %>
    <li><a href="#apidoc_demo">示例报文</a></li>
</ul>
</script>

<!-- 字段可选项视图模板 -->
<script id="apidoc_options_template" type="text/html">
    <a href="javascript:;" class="fn-tooltip" data-tooltip-content="#<%=id%>"><%=content%></a>
    <div style="display: none;">
        <table id="<%=id%>" class="layui-table">
            <thead>
            <tr>
                <th style="text-align: right;">可选值</th>
                <th>说明</th>
            </tr>
            </thead>
            <% for(var key in context){var value=context[key];%>
            <tr>
                <td align="right"><%=key%></td>
                <td><%=value%></td>
            </tr>
            <% } %>
        </table>
    </div>
</script>

<!-- demo是json格式时弹出显示模板 -->
<script id="apidoc_demo_json_template" type="text/html">
    <a href="javascript:;" class="fn-tooltip" data-tooltip-content="#<%=id%>">示例</a>
    <div style="display: none;">
        <pre id="<%=id%>" style="display: block;max-width: 800px;min-width: 300px;"><%=value%></pre>
    </div>
</script>

<script type="text/javascript" src="/plugin/baiduTemplate.js"></script>
<script type="text/javascript" src="/portal/script/apidoc.js" ></script>
<script type="text/javascript" src="/plugin/jsonview/jquery.jsonview.min.js" ></script>
<script src="/plugin/tooltipster/js/tooltipster.bundle.js"></script>
<script src="/plugin/tooltipster/js/tooltipster-scrollableTip.js"></script>

<script>
    $(function(){
        var defApidocId = "${apidocId}";
        if(!defApidocId || defApidocId == ''){
            defApidocId = "${apiDocs[0].id}";
        }
        loadData(defApidocId);
    });

</script>



	</body>
</html>
