<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp" %>
<div style="padding: 5px;font-family:微软雅黑;">
    <style>
        pre {
            outline: 1px solid #ccc;
            padding: 5px;
            margin: 5px;
        }

        .string {
            color: green;
        }

        .number {
            color: darkorange;
        }

        .boolean {
            color: blue;
        }

        .null {
            color: magenta;
        }

        .key {
            color: red;
        }
        pre{
            margin:1px;
            padding:1px;
        }
        .tableForm th{
            padding:1px;
        }
    </style>
    <script>
        function syntaxHighlight(json) {
            json = json.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
            return json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g, function (match) {
                var cls = 'number';
                if (/^"/.test(match)) {
                    if (/:$/.test(match)) {
                        cls = 'key';
                    } else {
                        cls = 'string';
                    }
                } else if (/true|false/.test(match)) {
                    cls = 'boolean';
                } else if (/null/.test(match)) {
                    cls = 'null';
                }
                return '<span class="' + cls + '">' + match + '</span>';
            });
        }

        $("#respSpan").html("<pre>" + syntaxHighlight(JSON.stringify(${apiMock.response}, undefined, 2)) + "</pre>");
        $("#expSpan").html("<pre>" + syntaxHighlight(JSON.stringify(${apiMock.expect}, undefined, 2)) + "</pre>");

    </script>
    <table class="tableForm" width="100%">
        <tr>
            <th>id:</th>
            <td>${apiMock.id}</td>
        </tr>
        <tr>
            <th width="30%">服务名称:</th>
            <td>${apiMock.serviceName}</td>
        </tr>
        <tr>
            <th>版本号:</th>
            <td>${apiMock.version}</td>
        </tr>
        <tr>
            <th>期望参数:</th>
            <td id="expSpan">${apiMock.expect}</td>
        </tr>
        <tr>
            <th>同步响应:</th>
            <td id="respSpan"></td>
        </tr>
        <tr>
            <th>异步响应:</th>
            <td>${apiMock.notify}</td>
        </tr>
        <tr>
            <th>创建时间:</th>
            <td><fmt:formatDate value="${apiMock.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
        </tr>
        <tr>
            <th>修改时间:</th>
            <td><fmt:formatDate value="${apiMock.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
        </tr>
    </table>
</div>
