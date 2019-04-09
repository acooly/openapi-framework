<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp" %>
<style type="text/css">

    .grid {
        font: 12px arial, helvetica, sans-serif;
        border: 1px solid #8DB2E3
    }

    .grid td {
        font: 100% arial, helvetica, sans-serif;
        height: 24px;
        padding: 5px
    }

    .grid {
        width: 100%;
        border-collapse: collapse
    }

    .grid th {
        background: #E7F3FE;
        height: 27px;
        line-height: 27px;
        border: 1px solid #8DB2E3;
        padding-left: 5px
    }

    .grid td {
        border: 1px solid #8DB2E3;
        padding-left: 5px
    }

    div.dashed {border-style: dashed;border-width: 2px}

    div.ridge {border-style:dotted none dotted none;border-width: 1px}
</style>
<script>
    $(function () {
        $.acooly.framework.registerKeydown('manage_apiItemDoc_searchform', 'manage_apiItemDoc_datagrid');
    });
</script>

<div class="demo-info" style="margin-bottom:10px" id="servcieBaseInfo">
    <script id="serviceInfo-template" type="text/html">
        <form id="serviceBaseForm">
            <table class="tableForm" width="98%" align="center">
                <tr>
                    <th style="width: 80px">服务码：</th>
                    <td>{{entity.serviceNo}}</td>
                </tr>
                <tr>
                    <th style="width: 80px">服务名称：</th>
                    <td>{{entity.title}}</td>
                </tr>
                <tr>
                    <th style="width: 80px">服务版本：</th>
                    <td>{{entity.version}}</td>
                </tr>
            </table>
            {{{entity.note}}}
        </form>
        {{#each entity.apiDocMessages}}
        <h3>{{transParamType messageType}}</h3>
        <div class="dashed">
            <table class="grid">
                <tbody>
                <tr>
                    <th style="width: 150px;">参数</th>
                    <th style="width: 150px;">参数名称</th>
                    <th style="width: 100px;">类型（字节）</th>
                    <th style="width: 80px;">状态</th>
                    <th style="width: 80px;">加密</th>
                    <th style="width: 150px;">参数说明</th>
                    <th style="width: 100px;">示例</th>
                </tr>
                {{#each apiDocItems}}
                <tr>
                    {{#if children}}
                        <td><a href="#+{{@index}}{{name}}">{{name}}</a></td>
                    {{else}}
                        <td>{{name}}</td>
                    {{/if}}
                    <td>{{title}}</td>
                    <td>{{dataType}}</td>
                    <td>{{status}}</td>
                    <td>{{encryptstatus}}</td>
                    <td>{{descn}}</td>
                    <td>{{demo}}</td>
                </tr>
                {{/each}}
                </tbody>
            </table>

            {{#each apiDocItems}}
                {{#if children}}
                    <div id="+{{@index}}{{name}}">{{name}}结构</div>
                    <table class="grid">
                        <tbody>
                        <tr>
                            <th style="width: 150px;">参数</th>
                            <th style="width: 150px;">参数名称</th>
                            <th style="width: 100px;">类型（字节）</th>
                            <th style="width: 80px;">状态</th>
                            <th style="width: 80px;">加密</th>
                            <th style="width: 150px;">参数说明</th>
                            <th style="width: 100px;">示例</th>
                        </tr>
                        {{#each children}}
                        <tr>
                            <td>{{name}}</td>
                            <td>{{title}}</td>
                            <td>{{dataType}}</td>
                            <td>{{status}}</td>
                            <td>{{encryptstatus}}</td>
                            <td>{{descn}}</td>
                            <td>{{demo}}</td>
                        </tr>
                        {{/each}}
                        </tbody>
                    </table>
                {{/if}}
            {{/each}}
        </div>
        <br>
        <br>
        <br>
        {{/each}}
    </script>
</div>
<script src="/plugin/handlebars-v4.0.5.js"></script>
<script type="text/javascript">

    $(document).ready(function () {
        serviceInfo();
    });

    var serviceInfoSource = $("#serviceInfo-template").html();
    var serviceInfoTemplate = Handlebars.compile(serviceInfoSource);
    var serviceInfoContext;
    function serviceInfo() {
        $.ajax({
            type: "POST",
            data: {id: ${id}},
            url: "/manage/apidoc/apiDocService/servcieBaseInfo.html",
            error: function (data) {
                alert("Connection error");
                return false;
            },
            success: function (data) {
                serviceInfoContext = data;
                var serviceInfoHtml = serviceInfoTemplate(serviceInfoContext);
                $('#servcieBaseInfo').html(serviceInfoHtml);
            },
            dataType: "json"
        });
    }
    ;

    //判断服务类型
    Handlebars.registerHelper("transParamType", function (code) {
        return ({
            Request: '请求报文',
            Response: '响应报文',
            Redirect: '跳转报文(与异步报文一致)',
            Notify: '异步报文'
        })[code];
    });
</script>
