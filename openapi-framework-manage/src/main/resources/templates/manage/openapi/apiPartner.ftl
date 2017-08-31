<#if ssoEnable>
    <#include "*/include.ftl">
</#if>
<script type="text/javascript">
$(function() {
	$.acooly.framework.registerKeydown('manage_apiPartner_searchform','manage_apiPartner_datagrid');
});

/**
 * 打开权限设置界面
 */
function manage_apiPartner_showSetting(){
    var row = $.acooly.framework.getSelectedRow('manage_apiPartner_datagrid');
    if(!row){
        $.messager.alert('提示','请先选中操作的接入方记录');
        return;
    }

    $('<div/>').dialog({
        href:'/manage/openapi/apiPartnerService/setting.html?id='+row.id,
        width: 1000,
        height: 450,
        modal: true,
        title: '设置接入方权限: '+row.partnerId,
        buttons: [{
            text: '关闭',
            iconCls: 'icon-cancel',
            handler: function () {
                var d = $(this).closest('.window-body');
                d.dialog('close');
            }
        }],
        onClose: function () {
            $(this).dialog('destroy');
        }
    });

}

</script>
<div class="easyui-layout" data-options="fit : true,border : false">
  <!-- 查询条件 -->
  <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;" align="left">
    <form id="manage_apiPartner_searchform" onsubmit="return false">
      <table class="tableForm" width="100%">
        <tr>
          <td align="left">
          	<div>
                接入方编码: <input type="text" class="text" size="15" name="search_LIKE_partnerId"/>
                接入方名称: <input type="text" class="text" size="15" name="search_LIKE_partnerName"/>
                安全方案: <select style="width:80px;height:27px;" name="search_EQ_secretType" editable="false" panelHeight="auto" class="easyui-combobox"><option value="">所有</option>
            <#list allSecretTypes as k,v><option value="${k}">${v}</option></#list>
            </select>
                签名类型: <select style="width:80px;height:27px;" name="search_EQ_signType" editable="false" panelHeight="auto" class="easyui-combobox"><option value="">所有</option>
            <#list allSignTypes as k,v><option value="${k}">${v}</option></#list>
            </select>
                创建时间: <input size="15" class="text" id="search_GTE_createTime" name="search_GTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
                至 <input size="15" class="text" id="search_LTE_createTime" name="search_LTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
                <a href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:false" onclick="$.acooly.framework.search('manage_apiPartner_searchform','manage_apiPartner_datagrid');"><i class="fa fa-search fa-lg fa-fw fa-col"></i>查询</a>
          	</div>
          </td>
        </tr>
      </table>
    </form>
  </div>

  <!-- 列表和工具栏 -->
  <div data-options="region:'center',border:false">
    <table id="manage_apiPartner_datagrid" class="easyui-datagrid" url="/manage/openapi/apiPartner/listJson.html" toolbar="#manage_apiPartner_toolbar" fit="true" border="false" fitColumns="false"
      pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true" nowrap="false">
      <thead>
        <tr>
        	<th field="showCheckboxWithId" checkbox="true" data-options="formatter:function(value, row, index){ return row.id }">编号</th>
			<th field="id">ID</th>
			<th field="partnerId">合作方编码</th>
			<th field="partnerName">合作方名称</th>
			<th field="secretType" data-options="formatter:function(value){ return formatRefrence('manage_apiPartner_datagrid','allSecretTypes',value);} ">安全方案</th>
			<th field="signType" data-options="formatter:function(value){ return formatRefrence('manage_apiPartner_datagrid','allSignTypes',value);} ">签名类型</th>
			<th field="secretKey">秘钥</th>
		    <th field="createTime" formatter="dateTimeFormatter">创建时间</th>
		    <th field="updateTime" formatter="dateTimeFormatter">更新时间</th>
			<th field="comments">备注</th>
          	<th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_apiPartner_action',value,row)}">动作</th>
        </tr>
      </thead>
    </table>

    <!-- 每行的Action动作模板 -->
    <div id="manage_apiPartner_action" style="display: none;">
      <a onclick="$.acooly.framework.edit({url:'/manage/openapi/apiPartner/edit.html',id:'{0}',entity:'apiPartner',width:500,height:400});" href="#" title="编辑"><i class="fa fa-pencil fa-lg fa-fw fa-col"></i></a>
      <a onclick="$.acooly.framework.show('/manage/openapi/apiPartner/show.html?id={0}',500,400);" href="#" title="查看"><i class="fa fa-file-o fa-lg fa-fw fa-col"></i></a>
      <a onclick="$.acooly.framework.remove('/manage/openapi/apiPartner/deleteJson.html','{0}','manage_apiPartner_datagrid');" href="#" title="删除"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i></a>
    </div>

    <!-- 表格的工具栏 -->
    <div id="manage_apiPartner_toolbar">
      <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.create({url:'/manage/openapi/apiPartner/create.html',entity:'apiPartner',width:500,height:400})"><i class="fa fa-plus-circle fa-lg fa-fw fa-col"></i>添加</a>
      <#--<a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.removes('/manage/openapi/apiPartner/deleteJson.html','manage_apiPartner_datagrid')"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i>批量删除</a>-->
      <a href="#" class="easyui-menubutton" data-options="menu:'#manage_apiPartner_exports_menu'"><i class="fa fa-arrow-circle-o-down fa-lg fa-fw fa-col"></i>批量导出</a>
      <div id="manage_apiPartner_exports_menu" style="width:150px;">
        <div onclick="$.acooly.framework.exports('/manage/openapi/apiPartner/exportXls.html','manage_apiPartner_searchform','合作方管理')"><i class="fa fa-file-excel-o fa-lg fa-fw fa-col"></i>Excel</div>
        <div onclick="$.acooly.framework.exports('/manage/openapi/apiPartner/exportCsv.html','manage_apiPartner_searchform','合作方管理')"><i class="fa fa-file-text-o fa-lg fa-fw fa-col"></i>CSV</div>
      </div>
      <a href="#" class="easyui-linkbutton" iconCls="icons-resource-access-user" plain="true" onclick="manage_apiPartner_showSetting();">设置权限</a>
    </div>
  </div>

</div>
