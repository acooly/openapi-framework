<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"]/>
<div>
    <form id="manage_apiScheme_editform" action="/manage/openapi/apiScheme/<#if action == 'create'>save<#else>update</#if>Json.html" method="post">
      <@jodd.form bean="apiScheme" scope="request">
        <input name="id" type="hidden" />
        <table class="tableForm" width="100%">
			<tr>
				<th width="25%">名称：</th>
				<td><input type="text" name="name" size="48" class="easyui-validatebox text" data-options="required:true" validType="byteLength[1,32]"/></td>
			</tr>					
			<tr>
				<th>备注：</th>
				<td><input type="text" name="comments" size="48" class="easyui-validatebox text"  validType="byteLength[1,128]"/></td>
			</tr>					
        </table>
      </@jodd.form>
    </form>
</div>
