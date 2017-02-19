<script type="text/javascript">
$(function() {
	$.acooly.framework.registerKeydown('manage_apiScheme_searchform','manage_apiScheme_datagrid');
});
</script>
<div class="easyui-layout" data-options="fit : true,border : false">
  <!-- 查询条件 -->
  <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;" align="left">
    <form id="manage_apiScheme_searchform" onsubmit="return false">
      <table class="tableForm" width="100%"><tr><td align="left">
          	<div>
                名称: <input type="text" class="text" size="15" name="search_LIKE_name"/>
                创建时间: <input size="15" class="text" id="search_GTE_createTime" name="search_GTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
                至<input size="15" class="text" id="search_LTE_createTime" name="search_LTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
                <a href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:false" onclick="$.acooly.framework.search('manage_apiScheme_searchform','manage_apiScheme_datagrid');"><i class="fa fa-search fa-lg fa-fw fa-col"></i>查询</a>
          	</div>
      </td></tr></table>
    </form>
  </div>

  <!-- 列表和工具栏 -->
  <div data-options="region:'center',border:false">
    <table id="manage_apiScheme_datagrid" class="easyui-datagrid" url="/manage/openapi/apiScheme/listJson.html" toolbar="#manage_apiScheme_toolbar" fit="true" border="false" fitColumns="false"
      pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
      <thead>
        <tr>
        	<th field="showCheckboxWithId" checkbox="true" data-options="formatter:function(value, row, index){ return row.id }">编号</th>
			<th field="id">ID</th>
			<th field="name">名称</th>
		    <th field="createTime" formatter="dateTimeFormatter">创建时间</th>
		    <th field="updateTime" formatter="dateTimeFormatter">更新时间</th>
			<th field="comments">备注</th>
          	<th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_apiScheme_action',value,row)}">动作</th>
        </tr>
      </thead>
    </table>
    
    <!-- 每行的Action动作模板 -->
    <div id="manage_apiScheme_action" style="display: none;">
      <a onclick="$.acooly.framework.edit({url:'/manage/openapi/apiScheme/edit.html',id:'{0}',entity:'apiScheme',width:500,height:400});" href="#" title="编辑"><i class="fa fa-pencil fa-lg fa-fw fa-col"></i></a>
      <a onclick="$.acooly.framework.show('/manage/openapi/apiScheme/show.html?id={0}',500,400);" href="#" title="查看"><i class="fa fa-file-o fa-lg fa-fw fa-col"></i></a>
      <a onclick="$.acooly.framework.remove('/manage/openapi/apiScheme/deleteJson.html','{0}','manage_apiScheme_datagrid');" href="#" title="删除"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i></a>
    </div>
    
    <!-- 表格的工具栏 -->
    <div id="manage_apiScheme_toolbar">
      <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.create({url:'/manage/openapi/apiScheme/create.html',entity:'apiScheme',width:500,height:400})"><i class="fa fa-plus-circle fa-lg fa-fw fa-col"></i>添加</a> 
      <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.removes('/manage/openapi/apiScheme/deleteJson.html','manage_apiScheme_datagrid')"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i>批量删除</a>
      <a href="#" class="easyui-menubutton" data-options="menu:'#manage_apiScheme_exports_menu'"><i class="fa fa-arrow-circle-o-down fa-lg fa-fw fa-col"></i>批量导出</a>
      <div id="manage_apiScheme_exports_menu" style="width:150px;">
        <div onclick="$.acooly.framework.exports('/manage/openapi/apiScheme/exportXls.html','manage_apiScheme_searchform','服务方案')"><i class="fa fa-file-excel-o fa-lg fa-fw fa-col"></i>Excel</div>  
        <div onclick="$.acooly.framework.exports('/manage/openapi/apiScheme/exportCsv.html','manage_apiScheme_searchform','服务方案')"><i class="fa fa-file-text-o fa-lg fa-fw fa-col"></i>CSV</div> 
      </div>
    </div>
  </div>

</div>
