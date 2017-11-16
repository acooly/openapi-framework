<#if ssoEnable>
    <#include "*/include.ftl">
</#if>
<script type="text/javascript">
	$(function() {
		$.acooly.framework.registerKeydown('manage_appVersion_toolbar', 'manage_appVersion_datagrid');
	});
</script>
<div class="easyui-layout" data-options="fit : true,border : false">
	<!-- 列表和工具栏 -->
	<div data-options="region:'center',border:false">
		<table id="manage_appVersion_datagrid" class="easyui-datagrid" url="/manage/module/app/appVersion/listJson.html" toolbar="#manage_appVersion_toolbar" fit="true" border="false" fitColumns="false" pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
			<thead>
				<tr>
					<th field="showCheckboxWithId" checkbox="true" data-options="formatter:function(value, row, index){ return row.id }">编号</th>
					<th field="id">ID</th>
					<th field="appCode">应用编码</th>
					<th field="appName">应用名称</th>
					<th field="deviceType">设备类型</th>
					<th field="versionName">版本号</th>
					<th field="versionCode">版本编码</th>
					<th field="subject" formatter="formatContent">版本说明</th>
					<th field="url" formatter="formatContent">下载URL</th>
					<th field="path" formatter="formatContent">文件</th>
					<th field="pubTime" formatter="formatDate">发布时间</th>
					<th field="forceUpdate" data-options="formatter:function(value){ return formatRefrence('manage_appVersion_datagrid','allForceUpdates',value);} ">强制更新</th>
					<th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_appVersion_action',value,row)}">动作</th>
				</tr>
			</thead>
		</table>

		<!-- 每行的Action动作模板 -->
		<div id="manage_appVersion_action" style="display: none;">
			<a class="line-action icon-edit" onclick="$.acooly.framework.edit({url:'/manage/module/app/appVersion/edit.html',id:'{0}',entity:'appVersion',width:650,height:463});" href="#" title="编辑"></a> <a class="line-action icon-show" onclick="$.acooly.framework.show('/manage/module/app/appVersion/show.html?id={0}',700,400);" href="#" title="查看"></a> <a class="line-action icon-delete"
				onclick="$.acooly.framework.remove('/manage/module/app/appVersion/deleteJson.html','{0}','manage_appVersion_datagrid');" href="#" title="删除"></a>
		</div>

		<!-- 表格的工具栏 -->
		<div id="manage_appVersion_toolbar">
			设备类型:<select style="width: 80px;" name="search_EQ_deviceType" editable="false" panelHeight="auto" class="easyui-combobox"><option value="">所有</option>
        <#list allDeviceTypes as k,v><option value="${k}">${v}</option></#list>

        </select>
			版本号:<input type="text" size="15" name="search_LIKE_versionName" /> <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="$.acooly.framework.search('manage_appVersion_toolbar','manage_appVersion_datagrid');">查询</a>
			<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="$.acooly.framework.create({url:'/manage/module/app/appVersion/create.html',entity:'appVersion',width:650,height:463})">添加</a>
		</div>
	</div>

</div>
