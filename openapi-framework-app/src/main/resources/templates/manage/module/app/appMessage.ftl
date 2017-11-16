<#if ssoEnable>
    <#include "*/include.ftl">
</#if>
<script type="text/javascript">
$(function() {
	$.acooly.framework.registerKeydown('manage_appMessage_searchform','manage_appMessage_datagrid');
});
</script>
<div class="easyui-layout" data-options="fit : true,border : false">
  <!-- 查询条件 -->
  <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;" align="left">
    <form id="manage_appMessage_searchform" onsubmit="return false">
      <table class="tableForm" width="100%">
        <tr>
          <td align="left">
					标题:<input type="text" size="15" name="search_LIKE_title" />
					发送时间:<input id="search_GTE_sendTime" name="search_GTE_sendTime" size="10" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
					至<input id="search_LTE_sendTime" name="search_LTE_sendTime" size="10" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" /> 			
					类型:<select style="width:80px;" name="search_EQ_type" editable="false" panelHeight="auto" class="easyui-combobox"><option value="">所有</option>
            <#list allTypes as k,v><option value="${k}">${v}</option></#list>

          </select>
					内容分类:<select style="width:80px;" name="search_EQ_contentType" editable="false" panelHeight="auto" class="easyui-combobox"><option value="">所有</option>
          <#list allContentTypes as k,v><option value="${k}">${v}</option></#list>

          </select>
					<!--接受人:<input type="text" size="10" name="search_LIKE_receivers"/>-->
          	<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:false" onclick="$.acooly.framework.search('manage_appMessage_searchform','manage_appMessage_datagrid');">查询</a> 
          </td>
        </tr>
      </table>
    </form>
  </div>

  <!-- 列表和工具栏 -->
  <div data-options="region:'center',border:false">
    <table id="manage_appMessage_datagrid" class="easyui-datagrid" url="/manage/module/app/appMessage/listJson.html" toolbar="#manage_appMessage_toolbar" fit="true" border="false" fitColumns="false"
      pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
      <thead>
        <tr>
        	<th field="showCheckboxWithId" checkbox="true" data-options="formatter:function(value, row, index){ return row.id }">编号</th>
			<th field="id">ID</th>
			<th field="sendTime">发送时间</th>
			<th field="title">标题</th>
			<th field="content" formatter="formatContent">发送内容</th>
			<th field="context" formatter="formatContent">会话参数</th>
			<th field="type" data-options="formatter:function(value){ return formatRefrence('manage_appMessage_datagrid','allTypes',value);} ">类型</th>
			<th field="contentType" data-options="formatter:function(value){ return formatRefrence('manage_appMessage_datagrid','allContentTypes',value);} ">内容分类</th>
			<th field="sender">发送人</th>
			<th field="receivers" formatter="formatContent">接收人</th>
			<th field="status" data-options="formatter:function(value){ return formatRefrence('manage_appMessage_datagrid','allStatuss',value);} ">状态</th>
          	<th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_appMessage_action',value,row);}">动作</th>
        </tr>
      </thead>
    </table>
    
    <!-- 每行的Action动作模板 -->
    <div id="manage_appMessage_action" style="display: none;">
      <a class="line-action icon-show" onclick="$.acooly.framework.show('/manage/module/app/appMessage/show.html?id={0}',500,400);" href="#" title="查看"></a>
    </div>
    
    <!-- 表格的工具栏 -->
    <div id="manage_appMessage_toolbar">
      <a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="$.acooly.framework.create({url:'/manage/module/app/appMessage/create.html',entity:'appMessage',width:500,height:400,reload:true,title:'系统推送',addButton:'确定推送'})">发送Push</a>
    </div>
  </div>

</div>
