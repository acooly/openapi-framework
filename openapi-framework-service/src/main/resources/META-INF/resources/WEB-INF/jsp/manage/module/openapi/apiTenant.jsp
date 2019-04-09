<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp"%>
<c:if test="${initParam['ssoEnable']=='true'}">
    <%@ include file="/WEB-INF/jsp/manage/common/ssoInclude.jsp" %>
</c:if>
<script type="text/javascript">
$(function() {
	$.acooly.framework.registerKeydown('manage_apiTenant_searchform','manage_apiTenant_datagrid');
});

</script>
<div class="easyui-layout" data-options="fit : true,border : false">
  <!-- 查询条件 -->
  <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;" align="left">
    <form id="manage_apiTenant_searchform" onsubmit="return false">
      <table class="tableForm" width="100%">
        <tr>
          <td align="left">
          	<div>
                合作方编码: <input type="text" class="text" size="15" name="search_LIKE_partnerId"/>
                合作方名称: <input type="text" class="text" size="15" name="search_LIKE_partnerName"/>
				安全方案: <select style="width:80px;height:27px;" name="search_EQ_secretType" editable="false" panelHeight="auto" class="easyui-combobox"><option value="">所有</option><c:forEach var="e" items="${allSecretTypes}"><option value="${e.key}" ${param.search_EQ_secretType == e.key?'selected':''}>${e.value}</option></c:forEach></select>
				签名类型: <select style="width:80px;height:27px;" name="search_EQ_signType" editable="false" panelHeight="auto" class="easyui-combobox"><option value="">所有</option><c:forEach var="e" items="${allSignTypes}"><option value="${e.key}" ${param.search_EQ_signType == e.key?'selected':''}>${e.value}</option></c:forEach></select>
                创建时间: <input size="15" type="text" class="text" id="search_GTE_createTime" name="search_GTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
					至 <input size="15" type="text" class="text" id="search_LTE_createTime" name="search_LTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
          	<a href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:false" onclick="$.acooly.framework.search('manage_apiTenant_searchform','manage_apiTenant_datagrid');"><i class="fa fa-search fa-lg fa-fw fa-col"></i>查询</a>
          	</div>
          </td>
        </tr>
      </table>
    </form>
  </div>

  <!-- 列表和工具栏 -->
  <div data-options="region:'center',border:false">
    <table id="manage_apiTenant_datagrid" class="easyui-datagrid" url="${pageContext.request.contextPath}/manage/module/openapi/apiTenant/listJson.html" toolbar="#manage_apiTenant_toolbar" fit="true" border="false" fitColumns="false"
      pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
      <thead>
        <tr>
        	<th field="showCheckboxWithId" checkbox="true" data-options="formatter:function(value, row, index){ return row.id }">编号</th>
			<th field="id" sum="true">ID</th>
			<th field="partnerId">合作方编码</th>
			<th field="partnerName">合作方名称</th>
			<th field="secretType" formatter="mappingFormatter">安全方案</th>
			<th field="signType" formatter="mappingFormatter">签名类型</th>
		    <th field="createTime" formatter="dateTimeFormatter">创建时间</th>
		    <th field="updateTime" formatter="dateTimeFormatter">修改时间</th>
			<th field="comments">备注</th>
          	<th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_apiTenant_action',value,row)}">动作</th>
        </tr>
      </thead>
    </table>

    <!-- 每行的Action动作模板 -->
    <div id="manage_apiTenant_action" style="display: none;">
      <a onclick="$.acooly.framework.edit({url:'/manage/module/openapi/apiTenant/edit.html',id:'{0}',entity:'apiTenant',width:500,height:400});" href="#" title="编辑"><i class="fa fa-pencil fa-lg fa-fw fa-col"></i></a>
      <a onclick="$.acooly.framework.show('/manage/module/openapi/apiTenant/show.html?id={0}',500,400);" href="#" title="查看"><i class="fa fa-file-o fa-lg fa-fw fa-col"></i></a>
      <a onclick="$.acooly.framework.remove('/manage/module/openapi/apiTenant/deleteJson.html','{0}','manage_apiTenant_datagrid');" href="#" title="删除"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i></a>
    </div>

    <!-- 表格的工具栏 -->
    <div id="manage_apiTenant_toolbar">
      <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.create({url:'/manage/module/openapi/apiTenant/create.html',entity:'apiTenant',width:500,height:400})"><i class="fa fa-plus-circle fa-lg fa-fw fa-col"></i>添加</a>
      <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.removes('/manage/module/openapi/apiTenant/deleteJson.html','manage_apiTenant_datagrid')"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i>批量删除</a>
      <%--<a href="#" class="easyui-menubutton" data-options="menu:'#manage_apiTenant_exports_menu'"><i class="fa fa-arrow-circle-o-down fa-lg fa-fw fa-col"></i>批量导出</a>--%>
      <%--<div id="manage_apiTenant_exports_menu" style="width:150px;">--%>
        <%--<div onclick="$.acooly.framework.exports('/manage/module/openapi/apiTenant/exportXls.html','manage_apiTenant_searchform','租户管理')"><i class="fa fa-file-excel-o fa-lg fa-fw fa-col"></i>Excel</div>--%>
        <%--<div onclick="$.acooly.framework.exports('/manage/module/openapi/apiTenant/exportCsv.html','manage_apiTenant_searchform','租户管理')"><i class="fa fa-file-text-o fa-lg fa-fw fa-col"></i>CSV</div>--%>
      <%--</div>--%>
      <%--<a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.imports({url:'/manage/module/openapi/apiTenant/importView.html',uploader:'manage_apiTenant_import_uploader_file'});"><i class="fa fa-arrow-circle-o-up fa-lg fa-fw fa-col"></i>批量导入</a>--%>
    </div>
  </div>

</div>
