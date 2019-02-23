<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp"%>
<c:if test="${initParam['ssoEnable']=='true'}">
    <%@ include file="/WEB-INF/jsp/manage/common/ssoInclude.jsp" %>
</c:if>
<script type="text/javascript">
$(function() {
	$.acooly.framework.registerKeydown('manage_apiDocScheme_searchform','manage_apiDocScheme_datagrid');
});

/**
 * 打开编辑解决方案服务界面
 */
function manage_apiPartner_showSetting(id){
    $('#edit_scheme_id').dialog({
        title:'<i class="fa fa-cog fa-lg fa-fw fa-col"></i> 配置解决方案服务列表',
        href:'/manage/apidoc/apiDocScheme/settingService.html?id='+id,
        closable : true,
        modal: true,
        width: 1000,
        height: 450
    });
}
</script>
<div id="edit_scheme_id"></div>
<div class="easyui-layout" data-options="fit : true,border : false">
  <!-- 查询条件 -->
  <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;" align="left">
    <form id="manage_apiDocScheme_searchform" onsubmit="return false">
      <table class="tableForm" width="100%">
        <tr>
          <td align="left">
          	<div>
					方案编码: <input type="text" class="text" size="15" name="search_LIKE_schemeNo"/>
					标题: <input type="text" class="text" size="15" name="search_LIKE_title"/>
					作者: <input type="text" class="text" size="15" name="search_LIKE_author"/>
				    方案类型: <select name="search_EQ_schemeType" editable="false" panelHeight="auto" class="easyui-combobox"><option value="">所有</option><c:forEach var="e" items="${allSchemeTypes}"><option value="${e.key}" ${param.search_EQ_schemeType == e.key?'selected':''}>${e.value}</option></c:forEach></select>
					排序值: <input type="text" class="text" size="15" name="search_EQ_sortTime"/>
          	<a href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:false" onclick="$.acooly.framework.search('manage_apiDocScheme_searchform','manage_apiDocScheme_datagrid');"><i class="fa fa-search fa-lg fa-fw fa-col"></i>查询</a>
          	</div>
          </td>
        </tr>
      </table>
    </form>
  </div>

  <!-- 列表和工具栏 -->
  <div data-options="region:'center',border:false">
    <table id="manage_apiDocScheme_datagrid" class="easyui-datagrid" url="${pageContext.request.contextPath}/manage/apidoc/apiDocScheme/listJson.html" toolbar="#manage_apiDocScheme_toolbar" fit="true" border="false" fitColumns="false"
      pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
      <thead>
        <tr>
        	<th field="showCheckboxWithId" checkbox="true" data-options="formatter:function(value, row, index){ return row.id }">编号</th>
			<th field="id" sum="true">ID</th>
			<th field="schemeNo">方案编码</th>
			<th field="title">标题</th>
			<th field="author">作者</th>
			<th field="note">说明</th>
			<th field="schemeType" formatter="mappingFormatter">方案类型</th>
			<th field="sortTime" sum="true">排序值</th>
		    <th field="createTime" formatter="dateTimeFormatter">创建时间</th>
		    <th field="updateTime" formatter="dateTimeFormatter">修改时间</th>
			<th field="comments">备注</th>
          	<th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_apiDocScheme_action',value,row)}">动作</th>
        </tr>
      </thead>
    </table>

    <!-- 每行的Action动作模板 -->
    <div id="manage_apiDocScheme_action" style="display: none;">
        <a onclick="$.acooly.framework.edit({url:'/manage/apidoc/apiDocScheme/edit.html',id:'{0}',entity:'apiDocScheme',width:860,height:690});" href="#" title="编辑"><i class="fa fa-pencil fa-lg fa-fw fa-col"></i></a>
      <a onclick="manage_apiPartner_showSetting('{0}');" href="javascript:void(0);" title="配置服务"><i class="fa fa-cog fa-lg fa-fw fa-col"></i></a>
      <a onclick="$.acooly.framework.show('/manage/apidoc/apiDocScheme/show.html?id={0}',500,400);" href="#" title="查看"><i class="fa fa-file-o fa-lg fa-fw fa-col"></i></a>
      <a onclick="$.acooly.framework.remove('/manage/apidoc/apiDocScheme/deleteJson.html','{0}','manage_apiDocScheme_datagrid');" href="#" title="删除"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i></a>
    </div>

    <!-- 表格的工具栏 -->
    <div id="manage_apiDocScheme_toolbar">
      <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.create({url:'/manage/apidoc/apiDocScheme/create.html',entity:'apiDocScheme',width:500,height:400})"><i class="fa fa-plus-circle fa-lg fa-fw fa-col"></i>添加</a>
      <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.removes('/manage/apidoc/apiDocScheme/deleteJson.html','manage_apiDocScheme_datagrid')"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i>批量删除</a>
      <a href="#" class="easyui-menubutton" data-options="menu:'#manage_apiDocScheme_exports_menu'"><i class="fa fa-arrow-circle-o-down fa-lg fa-fw fa-col"></i>批量导出</a>
      <div id="manage_apiDocScheme_exports_menu" style="width:150px;">
        <div onclick="$.acooly.framework.exports('/manage/apidoc/apiDocScheme/exportXls.html','manage_apiDocScheme_searchform','服务方案')"><i class="fa fa-file-excel-o fa-lg fa-fw fa-col"></i>Excel</div>
        <div onclick="$.acooly.framework.exports('/manage/apidoc/apiDocScheme/exportCsv.html','manage_apiDocScheme_searchform','服务方案')"><i class="fa fa-file-text-o fa-lg fa-fw fa-col"></i>CSV</div>
      </div>
      <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.imports({url:'/manage/apidoc/apiDocScheme/importView.html',uploader:'manage_apiDocScheme_import_uploader_file'});"><i class="fa fa-arrow-circle-o-up fa-lg fa-fw fa-col"></i>批量导入</a>
    </div>
  </div>

</div>
