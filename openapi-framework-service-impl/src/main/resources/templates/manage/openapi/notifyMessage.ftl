<#if ssoEnable>
    <#include "*/include.ftl">
</#if>
<script type="text/javascript">
$(function() {
	$.acooly.framework.registerKeydown('manage_apiNotifyMessage_toolbar','manage_apiNotifyMessage_datagrid');
});
</script>
<div class="easyui-layout" data-options="fit : true,border : false">
  <!-- 列表和工具栏 -->
  <div data-options="region:'center',border:false">
    <table id="manage_apiNotifyMessage_datagrid" class="easyui-datagrid" url="/manage/openapi/notifyMessage/listJson.html" toolbar="#manage_apiNotifyMessage_toolbar" fit="true" border="false" fitColumns="false"
      pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
      <thead>
        <tr>
			<th field="id">id</th>
			<th field="gid" data-options="formatter:function(v,r){  var s=v; if(r.merchOrderNo){ s+='<div style=\'margin-top:2px;\'>'+r.merchOrderNo+'</div>'} return s; }">GID|订单号</th>
			<th field="partnerId" data-options="formatter:function(v,r){  var s=v; if(r.requestNo){ s+='<div style=\'margin-top:2px;\'>'+r.requestNo+'</div>'} return s; }">接入方ID|请求号</th>
            <th field="service" data-options="formatter:function(v,r){ return v + '_' + r.version }">服务名</th>
			<th field="respInfo" formatter="contentFormatter">通知响应</th>
			<th field="sendCount">已通知次数</th>
		    <th field="nextSendTime">下次通知时间</th>
			<th field="status">通知状态</th>
			<th field="executeStatus">执行状态</th>
		    <th field="createTime" >创建时间</th>
		    <th field="updateTime" >更新时间</th>
          	<th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_apiNotifyMessage_action',value,row)}">动作</th>
        </tr>
      </thead>
    </table>
    
    <!-- 每行的Action动作模板 -->
    <div id="manage_apiNotifyMessage_action" style="display: none;">
      <a onclick="$.acooly.framework.edit({url:'/manage/openapi/notifyMessage/edit.html',id:'{0}',entity:'apiNotifyMessage',width:500,height:450});" href="#" title="编辑"><i class="fa fa-pencil fa-lg fa-fw fa-col"></i></a>
    </div>
    
    <!-- 表格的工具栏 -->
    <div id="manage_apiNotifyMessage_toolbar">
            GID: <input type="text" class="text" size="15" name="search_LLIKE_gid"/>
            接入方ID: <input type="text" class="text" size="15" name="search_EQ_partnerId"/>
            请求号: <input type="text" class="text" size="15" name="search_LLIKE_requestNo"/>
            商户订单号: <input type="text" class="text" size="15" name="search_LLIKE_merchOrderNo"/>
            通知状态: <select style="width:80px;height:27px;" name="search_EQ_status" editable="false" panelHeight="auto" class="easyui-combobox"><option value="">所有</option>
                        <#list allStatus as k,v><option value="${k}">${v}</option></#list>

    </select>
            <a href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:false" onclick="$.acooly.framework.search('manage_apiNotifyMessage_toolbar','manage_apiNotifyMessage_datagrid');"><i class="fa fa-search fa-lg fa-fw fa-col"></i>查询</a>
    </div>
  </div>

</div>



<script type="text/javascript">
    $(function() {

        var formatJson = function(json, options) {
            var reg = null,
                    keyStyle='color:#92278f;',
                    valStyle='color:#3ab54a;',
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

            $.each(json.split('\r\n'), function(index, node) {
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
                    if(node && node != ''){
                        var pair = node.split(':');
                        node = '<span style="'+keyStyle+'">'+pair[0]+'</span>:<span style="'+valStyle+'">'+ pair[1]+'</span>';
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
            view : detailview,
            detailFormatter : function(index, row) {
                return '<div class="ddv" style="padding:10px 5px"></div>';
            },
            onExpandRow : function(index, row) {

                var htmlContent = '<div><b>通知地址:</b> '+row.url+'</div><div><div style="float:left;width:500px;">' +
                            '<h3 style="border-bottom: 1px solid #dddddd;">Notify Request:</h3>' +
                            '<div><pre>'+formatJson(row.content)+'</pre></div></div>' +
                            '<div style="float:left;">'+
                            '<h3 style="border-bottom: 1px solid #dddddd;">Notify Response:</h3><div>'+row.respInfo+'</div>' +
                            '</div><div style="clear: both;"></div> </div>';

                var ddv = $(this).datagrid('getRowDetail', index).find('div.ddv');
                ddv.panel({
                    border : false,
                    cache : false,
                    content: htmlContent
                });
                $('#manage_apiNotifyMessage_datagrid').datagrid('fixDetailRowHeight', index);
            }
        });
    });
</script>
