<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] /> 
<div>
    <form id="manage_appVersion_editform" action="${rc.contextPath}/manage/module/app/appVersion/<#if action == 'create'>save<#else>update</#if>Json.html" method="post" enctype="multipart/form-data">
      <@jodd.form bean="appVersion" scope="request">
        <input name="id" type="hidden" />
        <table class="tableForm" width="100%">
			<tr>
				<th>应用编码：</th>
				<td><input type="text" name="appCode" <#if action == 'create'>value="loan"</#if> class="easyui-validatebox" data-options="required:true" validType="byteLength[1,32]"/></td>
			</tr>
			<tr>
				<th>应用名称：</th>
				<td><input type="text" name="appName" class="easyui-validatebox" data-options="required:true" validType="byteLength[1,32]"/></td>
			</tr>			        
			<tr>
				<th width="25%">设备类型：</th>
				<td>
				<select style="width:80px;" name="deviceType" editable="false" panelHeight="auto" class="easyui-combobox">
                    <#list allDeviceTypes as k,v><option value="${k}">${v}</option></#list>

                </select>
				</td>
			</tr>	
			<tr>
				<th>版本编码：</th>
				<td><input type="text" name="versionCode" class="easyui-numberbox" data-options="required:true" validType="byteLength[1,10]"/> 通过这个的最大值判断最新版本</td>
			</tr>							
			<tr>
				<th>版本号：</th>
				<td><input type="text" name="versionName" class="easyui-validatebox" data-options="required:true" validType="byteLength[1,16]"/> 如：1.0.0, 用于显示</td>
			</tr>	
			<tr>
				<th>程序文件：</th>
				<td><input type="file" name="appFile" data-options="required:false"/> </td>
			</tr>
			<tr>
				<th>iphone安装地址：</th>
				<td><input type="text" name="appleUrl" class="easyui-validatebox" size="40"/> 只用于iphone安装</td>
			</tr>				
			<tr>
				<th>版本说明：</th>
				<td><textarea rows="4" cols="40" name="subject" class="easyui-validatebox" data-options="required:true" validType="byteLength[1,255]"></textarea></td>
			</tr>					
			<tr>
				<th>是否强制更新：</th>
				<td><select name="forceUpdate" editable="false" panelHeight="auto" class="easyui-combobox" >
					<#list allForceUpdates as k,v><option value="${k}">${v}</option></#list>
				</select></td>
			</tr>										
			<tr>
				<th>备注：</th>
				<td><textarea rows="2" cols="40" name="comments" class="easyui-validatebox"  validType="byteLength[1,255]"></textarea></td>
			</tr>					
        </table>
      </@jodd.form>
    </form>
</div>
