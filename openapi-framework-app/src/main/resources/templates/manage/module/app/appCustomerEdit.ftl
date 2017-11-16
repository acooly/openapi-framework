<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] /> 
<div>
    <form id="manage_appCustomer_editform" action="/manage/module/app/appCustomer/<#if action == 'create'>save<#else>update</#if>Json.html" method="post">
      <@jodd.form bean="appCustomer" scope="request">
        <input name="id" type="hidden" />
        <table class="tableForm" width="100%">
			<tr>
				<th width="25%">用户名：</th>
				<td><input type="text" name="userName" class="easyui-validatebox" data-options="required:true" validType="byteLength[1,32]"/></td>
			</tr>					
			<tr>
				<th>访问码：</th>
				<td><input type="text" name="accessKey" class="easyui-validatebox" data-options="required:true" validType="byteLength[1,64]"/></td>
			</tr>					
			<tr>
				<th>安全码：</th>
				<td><input type="text" name="secretKey" class="easyui-validatebox" data-options="required:true" validType="byteLength[1,64]"/></td>
			</tr>					
			<tr>
				<th>设备类型：</th>
				<td><input type="text" name="deviceType" class="easyui-validatebox"  validType="byteLength[1,16]"/></td>
			</tr>					
			<tr>
				<th>设备型号：</th>
				<td><input type="text" name="deviceModel" class="easyui-validatebox"  validType="byteLength[1,64]"/></td>
			</tr>					
			<tr>
				<th>设备标识：</th>
				<td><input type="text" name="deviceId" class="easyui-validatebox" data-options="required:true" validType="byteLength[1,64]"/></td>
			</tr>					
			<tr>
				<th>状态：</th>
				<td><input type="text" name="status" class="easyui-validatebox"  validType="byteLength[1,64]"/></td>
			</tr>					
        </table>
      </@jodd.form>
    </form>
</div>
