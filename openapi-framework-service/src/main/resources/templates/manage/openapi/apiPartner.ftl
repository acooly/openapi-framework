<#if ssoEnable>
    <#include "/manage/common/ssoInclude.ftl">
</#if>
<script type="text/javascript">
$(function() {
	$.acooly.framework.registerKeydown('manage_apiTenant_searchform','manage_apiTenant_datagrid');
});

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
                商户号: <input type="text" class="text" size="15" name="search_LIKE_merchantNo"/>
                创建时间: <input size="15" type="text" class="text" id="search_GTE_createTime" name="search_GTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
					至 <input size="15" type="text" class="text" id="search_LTE_createTime" name="search_LTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
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
      pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
      <thead>
        <tr>
        	<th field="showCheckboxWithId" checkbox="true" data-options="formatter:function(value, row, index){ return row.id }">编号</th>
			<th field="id" sum="true">ID</th>
			<th field="partnerId">接入方编码</th>
			<th field="partnerName">接入方名称</th>
			<th field="merchantNo">商户编码</th>
            <th field="tenantNo">租户编码</th>
		    <th field="createTime" formatter="dateTimeFormatter">创建时间</th>
		    <th field="updateTime" formatter="dateTimeFormatter">修改时间</th>
			<th field="comments">备注</th>
          	<th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_apiPartner_action',value,row)}">动作</th>
        </tr>
      </thead>
    </table>

    <!-- 每行的Action动作模板 -->
    <div id="manage_apiPartner_action" style="display: none;">
      <a onclick="$.acooly.framework.edit({url:'/manage/openapi/apiPartner/edit.html',id:'{0}',entity:'apiPartner',width:500,height:400});" href="#" title="编辑"><i class="fa fa-pencil fa-lg fa-fw fa-col"></i></a>
      <a onclick="$.acooly.framework.remove('/manage/openapi/apiPartner/deleteJson.html','{0}','manage_apiPartner_datagrid');" href="#" title="删除"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i></a>
    </div>

    <!-- 表格的工具栏 -->
    <div id="manage_apiPartner_toolbar">
      <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.create({url:'/manage/openapi/apiPartner/create.html',entity:'apiPartner',width:500,height:400})"><i class="fa fa-plus-circle fa-lg fa-fw fa-col"></i>添加</a>
      <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.removes('/manage/openapi/apiPartner/deleteJson.html','manage_apiPartner_datagrid')"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i>批量删除</a>
    </div>
  </div>

</div>
