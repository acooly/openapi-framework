<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_apiService_editform" action="/manage/openapi/apiService/<#if action == 'create'>save<#else>update</#if>Json.html" method="post">
      <@jodd.form bean="apiService" scope="request">
        <input name="id" type="hidden" />
        <table class="tableForm" width="100%">
			<tr>
				<th width="25%">分类：</th>
				<td>${typeName}<input type="hidden" name="apiServiceType.id" value="${typeId}" /></td>
			</tr>
			<tr>
				<th>中文名称：</th>
				<td><input type="text" name="title" size="48" class="easyui-validatebox text" data-options="required:true" validType="byteLength[1,32]"/></td>
			</tr>
			<tr>
				<th>服务名：</th>
				<td><input type="text" name="name" size="48" class="easyui-validatebox text" data-options="required:true" validType="byteLength[1,32]"/></td>
			</tr>					
			<tr>
				<th>服务版本：</th>
				<td><input type="text" name="version" size="48" class="easyui-validatebox text" data-options="required:true" validType="byteLength[1,16]"/></td>
			</tr>
			<tr>
				<th>备注：</th>
				<td><input type="text" name="comments" size="48" class="easyui-validatebox text"  validType="length[1,64]"/></td>
			</tr>					
        </table>
      </@jodd.form>
    </form>
</div>
