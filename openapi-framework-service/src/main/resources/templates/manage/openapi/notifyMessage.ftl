<#if ssoEnable>
    <#include "/manage/common/ssoInclude.ftl">
</#if>
<script type="text/javascript">
    $(function () {
        $.acooly.framework.initPage('manage_apiNotifyMessage_searchform', 'manage_apiNotifyMessage_datagrid');
    });
</script>
<div class="easyui-layout" data-options="fit : true,border : false">
    <!-- 列表和工具栏 -->
    <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;" align="left">
        <form id="manage_apiNotifyMessage_searchform" class="form-inline ac-form-search" onsubmit="return false">
            <div class="form-group">
                <label class="col-form-label">GID：</label>
                <input type="text" class="form-control form-control-sm" style="width: 150px;" name="search_LLIKE_gid"/>
            </div>
            <div class="form-group">
                <label class="col-form-label">请求号(外部)：</label>
                <input type="text" class="form-control form-control-sm" style="width: 150px;" name="search_LLIKE_requestNo"/>
            </div>
            <div class="form-group">
                <label class="col-form-label">partnerId：</label>
                <input type="text" class="form-control form-control-sm" name="search_EQ_partnerId"/>
            </div>
            <div class="form-group">
                <label class="col-form-label">首次通知时间：</label>
                <input type="text" class="form-control form-control-sm" id="search_GTE_createTime" name="search_GTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
                <span class="mr-1 ml-1">至</span> <input type="text" class="form-control form-control-sm" id="search_LTE_createTime" name="search_LTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
            </div>
            <div class="form-group">
                <label class="col-form-label">通知状态：</label>
                <select name="search_EQ_status" class="form-control input-sm select2bs4">
                    <option value="">所有</option><#list allStatuss as k,v>
                    <option value="${k}">${k}:${v}</option></#list></select>
            </div>
            <div class="form-group">
                <button class="btn btn-sm btn-primary" type="button" onclick="$.acooly.framework.search('manage_apiNotifyMessage_searchform','manage_apiNotifyMessage_datagrid');"><i class="fa fa-search fa-lg fa-fw fa-col"></i> 查询</button>
            </div>
        </form>
    </div>

    <div data-options="region:'center',border:false">
        <table id="manage_apiNotifyMessage_datagrid" class="easyui-datagrid" url="/manage/openapi/notifyMessage/listJson.html" fit="true" border="false" fitColumns="false"
               pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
            <thead>
            <tr>
                <th field="id">id</th>
                <th field="gid">GID</th>
                <th field="partnerId" data-options="formatter:function(v,r){  var s=v; if(r.requestNo){ s+='<div style=\'margin-top:2px;\'>'+r.requestNo+'</div>'} return s; }">接入方ID|请求号</th>
                <th field="service" data-options="formatter:function(v,r){ return v + '_' + r.version }">服务名</th>
                <th field="sendCount">已通知次数</th>
                <th field="nextSendTime">下次通知时间</th>
                <th field="status" formatter="mappingFormatter">通知状态</th>
                <th field="executeStatus" formatter="mappingFormatter">执行状态</th>
                <th field="createTime">首次通知时间</th>
                <th field="updateTime">最后通知时间</th>
                <th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_apiNotifyMessage_action',value,row)}">动作</th>
            </tr>
            </thead>
        </table>

        <!-- 每行的Action动作模板 -->
        <div id="manage_apiNotifyMessage_action" style="display: none;">
            <a onclick="$.acooly.framework.confirmSubmit('/manage/openapi/notifyMessage/retrySend.html','{0}','manage_apiNotifyMessage_datagrid','发送通知','请求确定立即发送异步通知');" href="#" title="立即发送"><i class="fa fa-play fa-lg fa-fw fa-col"></i></a>
            <a onclick="$.acooly.framework.edit({url:'/manage/openapi/notifyMessage/edit.html',id:'{0}',entity:'apiNotifyMessage',width:500,height:450});" href="#" title="编辑"><i class="fa fa-pencil fa-lg fa-fw fa-col"></i></a>
        </div>
    </div>

</div>


<script type="text/javascript">
    $(function () {

        var formatJson = function (json, options) {
            var reg = null,
                keyStyle = 'color:#92278f;',
                valStyle = 'color:#3ab54a;',
                formatted = '',
                pad = 0,
                PADDING = '    '; // one can also use '\t' or a different number of spaces


            // optional settings
            options = options || {};
            // remove newline where '{' or '[' follows ':'
            options.newlineAfterColonIfBeforeBraceOrBracket = (options.newlineAfterColonIfBeforeBraceOrBracket === true) ? true : false;
            // use a space after a colon
            options.spaceAfterColon = (options.spaceAfterColon === false) ? false : true;

            // begin formatting...
            if (typeof json !== 'string') {
                // make sure we start with the JSON as a string
                json = JSON.stringify(json);
            } else {
                // is already a string, so parse and re-stringify in order to remove extra whitespace
                json = JSON.parse(json);
                json = JSON.stringify(json);
            }

            // add newline before and after curly braces
            reg = /([\{\}])/g;
            json = json.replace(reg, '\r\n$1\r\n');

            // add newline before and after square brackets
            reg = /([\[\]])/g;
            json = json.replace(reg, '\r\n$1\r\n');

            // add newline after comma
            reg = /(\,)/g;
            json = json.replace(reg, '$1\r\n');

            // remove multiple newlines
            reg = /(\r\n\r\n)/g;
            json = json.replace(reg, '\r\n');

            // remove newlines before commas
            reg = /\r\n\,/g;
            json = json.replace(reg, ',');

            // optional formatting...
            if (!options.newlineAfterColonIfBeforeBraceOrBracket) {
                reg = /\:\r\n\{/g;
                json = json.replace(reg, ':{');
                reg = /\:\r\n\[/g;
                json = json.replace(reg, ':[');
            }
            if (options.spaceAfterColon) {
                reg = /\:/g;
                json = json.replace(reg, ': ');
            }

            $.each(json.split('\r\n'), function (index, node) {
                var i = 0,
                    indent = 0,
                    padding = '';

                if (node.match(/\{$/) || node.match(/\[$/)) {
                    indent = 1;
                } else if (node.match(/\}/) || node.match(/\]/)) {
                    if (pad !== 0) {
                        pad -= 1;
                    }
                } else {
                    if (node && node != '') {
                        var pair = node.split(':');
                        node = '<span style="' + keyStyle + '">' + pair[0] + '</span>:<span style="' + valStyle + '">' + pair[1] + '</span>';
                    }
                    indent = 0;
                }

                for (i = 0; i < pad; i++) {
                    padding += PADDING;
                }

                formatted += padding + node + '\r\n';
                pad += indent;
            });

            return formatted;
        };

        $('#manage_apiNotifyMessage_datagrid').datagrid({
            view: detailview,
            detailFormatter: function (index, row) {
                return '<div class="ddv" style="padding:10px 5px"></div>';
            },
            onExpandRow: function (index, row) {
                var html = '        <div class="row">\n' +
                    '            <div class="col-md-12">\n' +
                    '                <div class="callout callout-info">\n' +
                    '                    <h5>通知地址</h5>\n' +
                    '                    <p>'+row.url+'</p>\n' +
                    '                </div>\n' +
                    '\n' +
                    '                <div class="callout callout-success">\n' +
                    '                    <h5>Notify Request | 通知请求报文</h5>\n' +
                    '                    <div><pre style="background-color: #fff; border: 0;">'+formatJson(row.content)+'</pre></div>\n' +
                    '                </div>\n' +
                    '                <div class="callout callout-danger">\n' +
                    '                    <h5>Notify Response | 通知响应报文</h5>\n' +
                    '                    <div>'+row.respInfo+'</div>\n' +
                    '                </div>\n' +
                    '            </div>\n' +
                    '        </div>';

                var ddv = $(this).datagrid('getRowDetail', index).find('div.ddv');
                ddv.panel({
                    border: false,
                    cache: false,
                    content: html
                });
                $('#manage_apiNotifyMessage_datagrid').datagrid('fixDetailRowHeight', index);
            }
        });
    });
</script>
