<#if ssoEnable>
    <#include "*/include.ftl">
</#if>
<script type="text/javascript">
$(function() {
	$.acooly.framework.registerKeydown('manage_appCustomer_searchform','manage_appCustomer_datagrid');
});
</script>
<div class="easyui-layout" data-options="fit : true,border : false">
  <!-- 查询条件 -->
  <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;" align="left">
    <form id="manage_appCustomer_searchform" onsubmit="return false">
      <table class="tableForm" width="100%">
        <tr>
          <td align="left">
					用户名:<input type="text" size="15" name="search_LIKE_userName" />
					创建时间:<input size="15" id="search_GTE_createTime" name="search_GTE_createTime" size="15" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
					至 <input size="15" id="search_LTE_createTime" name="search_LTE_createTime" size="15" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" /> 			
					状态:<select name="search_EQ_status" editable="false" panelHeight="auto" class="easyui-combobox" style="width:80px;"><option value="">所有</option>
                        <#list allStatuss as k,v><option value="${k}">${v}</option></#list>

              <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:false" onclick="$.acooly.framework.search('manage_appCustomer_searchform','manage_appCustomer_datagrid');">查询</a>
          </td>
        </tr>
      </table>
    </form>
  </div>

  <!-- 列表和工具栏 -->
  <div data-options="region:'center',border:false">
    <table id="manage_appCustomer_datagrid" class="easyui-datagrid" url="/manage/module/app/appCustomer/listJson.html" toolbar="#manage_appCustomer_toolbar" fit="true" border="false" fitColumns="false"
      pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
      <thead>
        <tr>
        	<th field="showCheckboxWithId" checkbox="true" data-options="formatter:function(value, row, index){ return row.id }">编号</th>
			<th field="id">主键ID</th>
			<th field="userName">用户名</th>
			<th field="accessKey">访问码</th>
			<th field="secretKey">安全码</th>
			<th field="deviceType">设备类型</th>
			<th field="deviceModel">设备型号</th>
			<th field="deviceId">设备标识</th>
		    <th field="createTime" formatter="formatDate">创建时间</th>
		    <th field="updateTime" formatter="formatDate">更新时间</th>
			<th field="status">状态</th>
          	<th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_appCustomer_action',value,row)}">动作</th>
        </tr>
      </thead>
    </table>
    
    <!-- 每行的Action动作模板 -->
    <div id="manage_appCustomer_action" style="display: none;">
      <a class="line-action icon-show" onclick="$.acooly.framework.show('/manage/module/app/appCustomer/show.html?id={0}',500,400);" href="#" title="查看"></a>
      <a class="line-action icon-disable" onclick="$.acooly.framework.confirmRequest('/manage/module/app/appCustomer/disable.html?id={0}',null,'manage_appCustomer_datagrid','禁用','确定要禁用该记录吗?');" href="#" title="禁用"></a>
    </div>
    
    <!-- 表格的工具栏 -->
    <div id="manage_appCustomer_toolbar">
      <a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="$.acooly.framework.create({url:'/manage/module/app/appCustomer/create.html',entity:'appCustomer',width:500,height:400})">添加</a> 
      <a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="$.acooly.framework.removes('/manage/module/app/appCustomer/deleteJson.html','manage_appCustomer_datagrid')">批量删除</a>
      <a href="#" class="easyui-menubutton" data-options="menu:'#manage_appCustomer_exports_menu',iconCls:'icon-export'">批量导出</a>
      <div id="manage_appCustomer_exports_menu" style="width:150px;">
        <div data-options="iconCls:'icon-excel'" onclick="$.acooly.framework.exports('/manage/module/app/appCustomer/exportXls.html','manage_appCustomer_searchform','app_customer')">Excel</div>  
        <div data-options="iconCls:'icon-csv'" onclick="$.acooly.framework.exports('/manage/module/app/appCustomer/exportCsv.html','manage_appCustomer_searchform','app_customer')">CSV</div> 
      </div>
      <a href="#" class="easyui-linkbutton" iconCls="icon-import" plain="true" onclick="$.acooly.framework.imports({url:'/manage/module/app/appCustomer/importView.html',uploader:'manage_appCustomer_import_uploader_file'});">批量导入</a> 
    </div>
  </div>

</div>
