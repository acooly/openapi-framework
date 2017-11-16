<#if ssoEnable>
    <#include "*/include.ftl">
</#if>
<script type="text/javascript">
	$(function() {
		$.acooly.framework.registerKeydown('manage_appCrash_searchform', 'manage_appCrash_datagrid');
	});
</script>
<div class="easyui-layout" data-options="fit : true,border : false">
	<!-- 查询条件 -->
	<div data-options="region:'north',border:false" style="padding: 5px; overflow: hidden;" align="left">
		<form id="manage_appCrash_searchform" onsubmit="return false">
			<table class="tableForm" width="100%">
				<tr>
					<td align="left">
					应用名称:<input type="text" size="10" name="search_LIKE_appName" /> 
					应用版本:<input type="text" size="10" name="search_LIKE_appVersionCode"/> 
					用户名:<input type="text" size="10" name="search_LIKE_userName"/> 
					设备ID:<input type="text" size="10" name="search_LIKE_deviceId"/> 
					崩溃时间:<input id="search_GTE_crashDate" name="search_GTE_crashDate" size="10" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" /> 至<input id="search_LTE_crashDate" name="search_LTE_crashDate" size="10" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" /> <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:false" onclick="$.acooly.framework.search('manage_appCrash_searchform','manage_appCrash_datagrid');">查询</a>
					</td>
				</tr>
			</table>
		</form>
	</div>

	<!-- 列表和工具栏 -->
	<div data-options="region:'center',border:false">
		<table id="manage_appCrash_datagrid" class="easyui-datagrid" url="/manage/module/app/appCrash/listJson.html" toolbar="#manage_appCrash_toolbar" fit="true" border="false" fitColumns="false" pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
			<thead>
				<tr>
					<th field="showCheckboxWithId" checkbox="true" data-options="formatter:function(value, row, index){ return row.id }">编号</th>
					<th field="id">id</th>
					<th field="crashDate" formatter="formatDate">崩溃时间</th>
					<th field="appName">应用名称</th>
					<th field="userName">用户名</th>
					<th field="appVersionCode">应用版本代码</th>
					<th field="appVersionName">应用版本名称</th>
					<th field="brand">品牌</th>
					<th field="model">设备模型</th>
					<th field="platformId">平台ID</th>
					<th field="androidVersion">Android版本号</th>
					<th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_appCrash_action',value,row)}">动作</th>
				</tr>
			</thead>
		</table>

		<!-- 每行的Action动作模板 -->
		<div id="manage_appCrash_action" style="display: none;">
			<a class="line-action icon-show" onclick="$.acooly.framework.show('/manage/module/app/appCrash/show.html?id={0}',1000,600);" href="#" title="查看"></a>
		</div>

		<!-- 表格的工具栏 -->
		<div id="manage_appCrash_toolbar">
			<a href="#" class="easyui-menubutton" data-options="menu:'#manage_appCrash_exports_menu',iconCls:'icon-export'">批量导出</a>
			<div id="manage_appCrash_exports_menu" style="width: 150px;">
				<div data-options="iconCls:'icon-excel'" onclick="$.acooly.framework.exports('/manage/module/app/appCrash/exportXls.html','manage_appCrash_searchform','app_crash')">Excel</div>
				<div data-options="iconCls:'icon-csv'" onclick="$.acooly.framework.exports('/manage/module/app/appCrash/exportCsv.html','manage_appCrash_searchform','app_crash')">CSV</div>
			</div>
		</div>
	</div>

</div>
