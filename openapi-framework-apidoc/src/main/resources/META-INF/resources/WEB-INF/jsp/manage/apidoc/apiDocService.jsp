<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp"%>
<c:if test="${initParam['ssoEnable']=='true'}">
    <%@ include file="/WEB-INF/jsp/manage/common/ssoInclude.jsp" %>
</c:if>
<script type="text/javascript">
$(function() {
	$.acooly.framework.registerKeydown('manage_apiDocService_searchform','manage_apiDocService_datagrid');
});

/**
 * 查询服务详细信息
 */
function showServiceInfo(id){
    $('#layout_center_tabs').tabs('close', "查看服务基本信息");
    $('#dialog_div_id').dialog({
        title:'查看服务基本信息',
        href:'/manage/apidoc/apiDocService/showServiceInfo.html?id='+id,
        closable : true,
        modal: true,
        width: 1000,
        height: 450
    });
}
</script>
<div id="dialog_div_id"></div>
<div class="easyui-layout" data-options="fit : true,border : false">
  <!-- 查询条件 -->
  <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;" align="left">
    <form id="manage_apiDocService_searchform" onsubmit="return false">
      <table class="tableForm" width="100%">
        <tr>
          <td align="left">
          	<div>
                    服务名称: <input type="text" class="text" size="15" name="search_LIKE_name"/>
                    服务标题: <input type="text" class="text" size="15" name="search_LIKE_title"/>
					<%--服务版本: <input type="text" class="text" size="15" name="search_LIKE_version"/>--%>
					<%--所属系统: <input type="text" class="text" size="15" name="search_LIKE_owner"/>--%>
				    服务类型: <select style="width:80px;height:27px;" name="search_EQ_serviceType" editable="false" panelHeight="auto" class="easyui-combobox"><option value="">所有</option><c:forEach var="e" items="${allServiceTypes}"><option value="${e.key}" ${param.search_EQ_serviceType == e.key?'selected':''}>${e.value}</option></c:forEach></select>
				    业务类型: <select style="width:80px;height:27px;" name="search_EQ_busiType" editable="false" panelHeight="auto" class="easyui-combobox"><option value="">所有</option><c:forEach var="e" items="${allBusiTypes}"><option value="${e.key}" ${param.search_EQ_busiType == e.key?'selected':''}>${e.value}</option></c:forEach></select>
					<%--排序值: <input type="text" class="text" size="15" name="search_EQ_sortTime"/>--%>
					<%--创建时间: <input size="15" class="text" id="search_GTE_createTime" name="search_GTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />--%>
					<%--至<input size="15" class="text" id="search_LTE_createTime" name="search_LTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />--%>
					<%--修改时间: <input size="15" class="text" id="search_GTE_updateTime" name="search_GTE_updateTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />--%>
					<%--至<input size="15" class="text" id="search_LTE_updateTime" name="search_LTE_updateTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />--%>
          	<a href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:false" onclick="$.acooly.framework.search('manage_apiDocService_searchform','manage_apiDocService_datagrid');"><i class="fa fa-search fa-lg fa-fw fa-col"></i>查询</a>
          	</div>
          </td>
        </tr>
      </table>
    </form>
  </div>

  <!-- 列表和工具栏 -->
  <div data-options="region:'center',border:false">
    <table id="manage_apiDocService_datagrid" class="easyui-datagrid" url="${pageContext.request.contextPath}/manage/apidoc/apiDocService/listJson.html" toolbar="#manage_apiDocService_toolbar" fit="true" border="false" fitColumns="false"
      pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
      <thead>
        <tr>
        	<th field="showCheckboxWithId" checkbox="true" data-options="formatter:function(value, row, index){ return row.id }">编号</th>
			<th field="id" sum="true">id</th>
			<th field="serviceNo">服务编号</th>
			<th field="name">服务名称</th>
			<th field="version">服务版本</th>
			<th field="title">服务标题</th>
			<th field="owner">所属系统</th>
			<th field="note" formatter="contentFormatter">服务说明</th>
			<th field="manualNote" formatter="contentFormatter">手工说明</th>
			<th field="serviceType" formatter="mappingFormatter">服务类型</th>
			<th field="busiType" formatter="mappingFormatter">业务类型</th>
<%--			<th field="sortTime" sum="true">排序值</th>--%>
			<th field="comments">备注</th>
		    <th field="createTime" formatter="dateTimeFormatter">创建时间</th>
		    <th field="updateTime" formatter="dateTimeFormatter">修改时间</th>
			<th field="signature">签名</th>
          	<th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_apiDocService_action',value,row)}">动作</th>
        </tr>
      </thead>
    </table>

    <!-- 每行的Action动作模板 -->
    <div id="manage_apiDocService_action" style="display: none;">
      <a onclick="$.acooly.framework.edit({url:'/manage/apidoc/apiDocService/edit.html',id:'{0}',entity:'apiDocService',width:500,height:400});" href="#" title="编辑"><i class="fa fa-pencil fa-lg fa-fw fa-col"></i></a>
      <a onclick="showServiceInfo('{0}')" href="javascript:void(0);" title="查看"><i class="fa fa-file-o fa-lg fa-fw fa-col"></i></a>
      <%--<a onclick="$.acooly.framework.remove('/manage/apidoc/apiDocService/deleteJson.html','{0}','manage_apiDocService_datagrid');" href="#" title="删除"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i></a>--%>
    </div>

    <!-- 表格的工具栏 -->
    <div id="manage_apiDocService_toolbar">
      <%--<a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.create({url:'/manage/apidoc/apiDocService/create.html',entity:'apiDocService',width:500,height:400})"><i class="fa fa-plus-circle fa-lg fa-fw fa-col"></i>添加</a>--%>
      <%--<a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.removes('/manage/apidoc/apiDocService/deleteJson.html','manage_apiDocService_datagrid')"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i>批量删除</a>--%>
      <%--<a href="#" class="easyui-menubutton" data-options="menu:'#manage_apiDocService_exports_menu'"><i class="fa fa-arrow-circle-o-down fa-lg fa-fw fa-col"></i>批量导出</a>--%>
      <%--<div id="manage_apiDocService_exports_menu" style="width:150px;">--%>
        <%--<div onclick="$.acooly.framework.exports('/manage/apidoc/apiDocService/exportXls.html','manage_apiDocService_searchform','服务信息')"><i class="fa fa-file-excel-o fa-lg fa-fw fa-col"></i>Excel</div>--%>
        <%--<div onclick="$.acooly.framework.exports('/manage/apidoc/apiDocService/exportCsv.html','manage_apiDocService_searchform','服务信息')"><i class="fa fa-file-text-o fa-lg fa-fw fa-col"></i>CSV</div>--%>
      <%--</div>--%>
      <%--<a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.imports({url:'/manage/apidoc/apiDocService/importView.html',uploader:'manage_apiDocService_import_uploader_file'});"><i class="fa fa-arrow-circle-o-up fa-lg fa-fw fa-col"></i>批量导入</a>--%>
    </div>
  </div>

</div>
