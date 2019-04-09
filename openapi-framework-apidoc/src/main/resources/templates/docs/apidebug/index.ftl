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
              <a href="/docs/index.html">在线联调</a>
              <a><cite>使用说明</cite></a>
            </span>
            </div>
        </div>

		<!--正文-->

		<div class="container">

            <div class="doc-header">
                <div class="doc-header-title"><i class="layui-icon">&#xe62a;</i> 在线调试</div>
            </div>


            <form id="debugForm" class="layui-form" action="#" method="post">
            <!-- 核心参数 -->
            <div style="background-color: #f4f5f9;border-bottom: 1px solid #eee; padding: 2px 0;">
                <div class="layui-row">
                    <div class="layui-col-md3">
                        <label class="layui-form-label">环境：</label>
                        <div class="layui-input-block">
                            <input type="radio" name="env" value="snet" title="联调" checked>
                            <input type="radio" name="env" value="online" title="生产">
                        </div>
                    </div>
                    <div class="layui-col-md3">
                        <label class="layui-form-label">商户号：</label>
                        <div class="layui-input-block"><input type="text" name="partnerId" required  lay-verify="required" placeholder="请输入商户号" autocomplete="off" class="layui-input"></div>
                    </div>
                    <div class="layui-col-md3">
                        <label class="layui-form-label">秘钥：</label>
                        <div class="layui-input-block"><input type="text" name="secretKey" required  lay-verify="required" placeholder="请输入商户秘钥" autocomplete="off" class="layui-input"></div>
                    </div>
                    <div class="layui-col-md3">
                        <label class="layui-form-label">签名：</label>
                        <div class="layui-input-block">
                            <select name="signType" lay-verify="required">
                                <option value="MD5">MD5</option>
                                <option value="SHA1">SHA1</option>
                                <option value="SHA2">SHA2</option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>

            <div style="margin-top: 10px;min-height: 650px;">

                <div class="layui-row debug" style="padding: 0;">
                    <div class="layui-col-md12 console" style="padding: 0;">
                        <div style="margin-left: 300px;">
                            <label class="layui-form-label" >服务：</label>
                            <div class="layui-input-inline" style="width: 200px"><input type="text" name="request_service" id="service" placeholder="请输入服务名" required  lay-verify="required" autocomplete="off" class="layui-input"></div>
                            <div class="layui-input-inline" style="width: 50px"><input type="text" name="request_version" id="version" value="1.0" placeholder="版本号" autocomplete="off" class="layui-input"></div>
                            <button class="layui-btn layui-btn-small layui-btn-normal" type="button" onclick="loadApidoc()">加载</button>
                            <div class="layui-input-inline"></div>
                        </div>
                    </div>
                </div>

                <div id="dymicForm" class="layui-row" style="display: none;">
                    <!-- 左侧表单 -->
                    <div class="layui-col-md6 debug">
                        <div class="console">
                        <div>
                            <div class="title"><i class="layui-icon">&#xe61a;</i> 业务参数</div>
                            <div id="dymicForm_container"></div>
                        </div>
                        <div>
                            <div class="title"><a href="javascript:;" onclick="debugToggle(this,$(this).parent().next())"><i class="layui-icon">&#xe61a;</i> 基础参数</a></div>
                            <div style="display: none;">
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><a href="javascript:;" onclick="$.acooly.layui.tips('网关会完全返回你传入的一致数据',this)">会话参数：</a></label>
                                    <div class="layui-input-inline"><input type="text" name="context"  autocomplete="off" class="layui-input"></div>
                                </div>
                            </div>
                        </div>
                        </div>
                    </div>

                    <!-- 右侧表单 -->
                    <div class="layui-col-md6 debug">
                        <div class="console">
                            <div>
                                <div class="title"><i class="layui-icon">&#xe61a;</i> 请求报文</div>
                                <div class="code" id="requestMessage"></div>
                            </div>

                            <div class="mt20">
                                <div class="title"><i class="layui-icon">&#xe61a;</i> 响应报文</div>
                                <div class="code" >
                                    <pre class="layui-code" id="responseMessage"></pre>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
            </form>
        </div>
        <!-- container end-->

		<!--底部-->
		<@includePage path="/docs/common/footer.html"/>
		<!--end-->

<!-- 动态表单模板 -->
<script id="dymicForm_template" type="text/html">
    <div class="layui-form-item">
        <label class="layui-form-label"><a href="javascript:;" onclick="$.acooly.layui.tips('16-40字节不重复编号，每次请求唯一',this)">requestNo：</a></label>
        <div class="layui-input-inline" style="width:310px">
            <input type="text" name="request_requestNo" id="requestNo" placeholder="请求号：16-40字节不重复编号，每次请求唯一" required  lay-verify="required" autocomplete="off" class="layui-input">
        </div>
        <button onclick="generateIds('r')" type="button" class="layui-btn layui-btn-small layui-btn-normal" style="height: 28px;"><i class="layui-icon">ဂ</i></button>
        <span style="height: 28px;line-height:28px; margin-left: 5px;color: #999!important;">*</span>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label"><a href="javascript:;" onclick="$.acooly.layui.tips('订单号: 交易订单号，交易唯一，幂等',this)">merchOrderNo：</a></label>
        <div class="layui-input-inline" style="width:310px">
            <input type="text" name="request_merchOrderNo" id="merchOrderNo" placeholder="订单号: 交易订单号，交易唯一，幂等。" autocomplete="off" class="layui-input">
        </div>
        <button onclick="generateIds('o')" type="button" class="layui-btn layui-btn-small layui-btn-normal" style="height: 28px;"><i class="layui-icon">ဂ</i></button>
    </div>

<%
for(var i=0;i<entity.messageDocs.length;i++){
  var e=entity.messageDocs[i];
  if(e.messageType == 'Request'){
    for(var j=0;j<e.apiItems.length;j++){
        var m=e.apiItems[j];
        if(m.children == null || m.children.length == 0){
%>
<div class="layui-form-item">
    <label class="layui-form-label">
        <a href="javascript:;" onclick="$.acooly.layui.tips('<%=parseDescn(m)%>',this)"><%=m.name%></a>：
    </label>
    <div class="layui-input-inline">
        <%
        var options = parseOption(m);
        if(options != null){ %>
        <select name="request_<%=m.name%>">
            <%for(var key in options){ var val = options[key];%>
            <option value="<%=val%>"><%=key%>:<%=val%></option>
            <%}%>
        </select>
        <%}else{%>
            <input type="text" name="request_<%=m.name%>" value="<%=m.demo%>" placeholder="<%=parseDescn(m)%>" size="<%=m.dataLength%>" <%if(m.status == 'M'){%>required  lay-verify="required"<%}%>  autocomplete="off" class="layui-input">
        <%}%>
    </div>
    <div class="layui-form-mid layui-word-aux"><%if(m.status == 'M'){%>*<%}%></div>
</div>
<%}else{%>
<div class="layui-form-item">
    <label class="layui-form-label"><a href="javascript:;" onclick="$.acooly.layui.tips('<%=parseDescn(m)%>',this)"><%=m.name%></a>：</label>
    <div class="layui-form-mid layui-word-aux">如果是子对象集合，简化为单个子对象。 <%if(m.status == 'M'){%>*<%}else{%><button type="button" class="layui-btn layui-btn-normal layui-btn-mini" onclick="debugToggle(this,$(this).parent().next())"><i class="layui-icon">&#xe61a;</i></button><%}%></div>
    <div class="layui-inline subform" <%if(m.status != 'M'){%>style="display:none;"<%}%>>
        <%for(var x=0;x<m.children.length;x++){ var n=m.children[x];%>
        <div class="layui-form-item">
            <label class="layui-form-label"><a href="javascript:;" onclick="$.acooly.layui.tips('<%=n.descn%>',this)"><%=n.title%></a>：</label>
            <div class="layui-input-inline">
                <% var options = parseOption(n);
                if(options != null){ %>
                <select name="request_<%=m.name%>_<%=n.name%>">
                    <%for(var key in options){ var val = options[key];%>
                    <option value="<%=val%>"><%=key%>:<%=val%></option>
                    <%}%>
                </select>
                <%}else{%>
                <input type="text" name="request_<%=m.name%>_<%=n.name%>" value="<%=n.demo%>" placeholder="<%=parseDescn(n)%>" size="<%=n.dataLength%>" <%if(n.status == 'M'){%>required  lay-verify="required"<%}%>  autocomplete="off" class="layui-input">
                <%}%>
            </div>
            <div class="layui-form-mid layui-word-aux"><%if(n.status == 'M'){%>*<%}%></div>
        </div>
        <%}%>
    </div>
</div>
<% }}}} %>

<%if(entity.serviceType == 'REDIRECT'){%>
<div class="layui-form-item">
    <label class="layui-form-label">returnUrl：</label>
    <div class="layui-input-inline"><input type="text" name="request_returnUrl" autocomplete="off" class="layui-input"></div>
</div>
<%}%>
<%if(entity.serviceType == 'REDIRECT'){%>
<div class="layui-form-item">
    <label class="layui-form-label">notifyUrl：</label>
    <div class="layui-input-inline"><input type="text" name="request_notifyUrl" autocomplete="off" class="layui-input"></div>
</div>
<%}%>

<div class="layui-form-item ac mt10">
<button class="layui-btn layui-btn-normal w300" lay-submit lay-filter="rquestBtn">提交请求</button>
</div>
</script>


<script>

    function debugToggle(obj,container){
        $(container).toggle();
        $(obj).children('.layui-icon').html($(container).is(":hidden") ? '&#xe61a;':'&#xe619;');
    }

    function loadApidoc(){
        var service = $('#service').val();
        var version = $('#version').val();
        if(!version){
            version = "1.0";
        }
        var serviceNo = service + "_" + version;
        var url = '/docs/apidoc/apidoc.html';
        var template = 'dymicForm_template';
        var renderTo = 'dymicForm_container';
        var jsonData = {serviceNo: serviceNo};
        baidu.template.ESCAPE = false;
        $.acooly.portal.ajaxRender(url, jsonData, renderTo, template, null, function (result) {
            $('#dymicForm').show();
            generateIds();
            layui.form.render();
        });
    }

    /**
     * 生成ID编码
     * @param type (r:请求，o:订单，null，both）
     */
    function generateIds(type){

        $.acooly.portal.ajax('/docs/apidebug/ids.html',null,function(result){
            if(type == null || type == 'r'){
                $('#requestNo').val(result.data.requestNo);
            }
            if(type == null || type == 'o'){
                $('#merchOrderNo').val(result.data.merchOrderNo);
            }
        });
    }


    function request(){
        var requestData = $("#debugForm").serializeJson();
        $.acooly.portal.ajax('/docs/apidebug/request.html',requestData,function(result){
            $("#requestDataContainer").html(result.data.requestData);
            $("#requestMessage").html(result.data.requestMessage);
            $("#responseMessage").html(result.data.responseMessage);
        });
    }


    function parseOption(item) {
        var descn = item.descn;
        try {
            var itemData = JSON.parse(descn);
            return itemData.data;
        } catch (e) {
            return null;
        }
    }

    function parseDescn(item) {
        var descn = item.descn;
        try {
            var itemData = JSON.parse(descn);
            descn = itemData.name;
        } catch (e) {}
        //descn  = descn + " " + item.
        return descn;
    }

    layui.use(['form','element'], function(){
        var element = layui.element;
        var form = layui.form;
        element.init();
        form.render();

        //监听提交
        form.on('submit(rquestBtn)', function(data){
            request();
            return false;
        });

    });


    $(function(){
        var initService = "${service}";
        if(initService != null && initService != ''){
            $("#service").val(initService);
            loadApidoc();
        }
    });


</script>



	</body>
</html>
