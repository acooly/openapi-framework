<#if ssoEnable>
    <#include "*/include.ftl">
</#if>
<script type="text/javascript">
$(function() {
	$.acooly.framework.registerKeydown('manage_appBanner_searchform','manage_appBanner_datagrid');
});
</script>
<div class="easyui-layout" data-options="fit : true,border : false">
  <!-- 查询条件 -->
  <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;" align="left">
    <form id="manage_appBanner_searchform" onsubmit="return false">
      <table class="tableForm" width="100%">
        <tr>
          <td align="left">
					标题:<input type="text" size="15" name="search_LIKE_title"/>
					创建时间:<input size="15" id="search_GTE_createTime" name="search_GTE_createTime" size="15" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
					至<input size="15" id="search_LTE_createTime" name="search_LTE_createTime" size="15" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" /> 			
          	<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:false" onclick="$.acooly.framework.search('manage_appBanner_searchform','manage_appBanner_datagrid');">查询</a> 
          </td>
        </tr>
      </table>
    </form>
  </div>

  <!-- 列表和工具栏 -->
  <div data-options="region:'center',border:false">
    <table id="manage_appBanner_datagrid" class="easyui-datagrid" url="${pageContext.request.contextPath}/manage/module/app/appBanner/listJson.html" toolbar="#manage_appBanner_toolbar" fit="true" border="false" fitColumns="false"
      pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
      <thead>
        <tr>
        	<th field="showCheckboxWithId" checkbox="true" data-options="formatter:function(value, row, index){ return row.id }">编号</th>
			<th field="id">主键ID</th>
			<th field="title">标题</th>
			<th field="mediaUrl">广告栏图片</th>
			<th field="link" data-options="formatter:function(value){return formatLink(value,'查看'); }">内容链接</th>
		    <th field="createTime">创建时间</th>
		    <th field="updateTime">更新时间</th>
			<th field="comments" formatter="formatContent">备注</th>
          	<th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_appBanner_action',value,row)}">动作</th>
        </tr>
      </thead>
    </table>
    
    <!-- 每行的Action动作模板 -->
    <div id="manage_appBanner_action" style="display: none;">
      <a class="line-action icon-edit" onclick="$.acooly.framework.edit({url:'/manage/module/app/appBanner/edit.html',id:'{0}',entity:'appBanner',width:500,height:400});" href="#" title="编辑"></a>
      <a class="line-action icon-show" onclick="$.acooly.framework.show('/manage/module/app/appBanner/show.html?id={0}',700,415);" href="#" title="查看"></a>
      <a class="line-action icon-movetop" onclick="$.acooly.framework.move('/manage/module/app/appBanner/moveTop.html',{0},'manage_appBanner_datagrid');" href="#"></a>
      <a class="line-action icon-moveup" onclick="$.acooly.framework.move('/manage/module/app/appBanner/moveUp.html',{0},'manage_appBanner_datagrid');" href="#"></a>
      <a class="line-action icon-delete" onclick="$.acooly.framework.remove('/manage/module/app/appBanner/deleteJson.html','{0}','manage_appBanner_datagrid');" href="#" title="删除"></a>
    </div>
    
    <!-- 表格的工具栏 -->
    <div id="manage_appBanner_toolbar">
      <a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="$.acooly.framework.create({url:'/manage/module/app/appBanner/create.html',entity:'appBanner',width:500,height:400})">添加</a> 
    </div>
  </div>

</div>
