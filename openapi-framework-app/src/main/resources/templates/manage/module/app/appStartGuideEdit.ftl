<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] /> 
<div>
    <form id="manage_appStartGuide_editform" action="/manage/module/app/appStartGuide/<#if action == 'create'>save<#else>update</#if>Json.html" method="post" enctype="multipart/form-data">
      <@jodd.form bean="appStartGuide" scope="request">
        <input name="id" type="hidden" />
        <table class="tableForm" width="100%">
			<tr>
				<th>默认图片：</th>
				<td><input type="file" name="fileDefault" class="easyui-validatebox" data-options="required:true"></td>
			</tr>					
			<tr>
				<th>IPHONE4：</th>
				<td><input type="file" name="fileIphone4" class="easyui-validatebox" > 960*640px</td>
			</tr>					
			<tr>
				<th>iphone5/6：</th>
				<td><input type="file" name="fileIphone6" class="easyui-validatebox" > 1242*2208px</td>
			</tr>					
			<tr>
				<th>android：</th>
				<td><input type="file" name="fileAndroid" class="easyui-validatebox" > 1080 * 1920px</td>
			</tr>					
			<tr>
				<th>状态：</th>
				<td>
				<select name="status" editable="false" panelHeight="auto" class="easyui-combobox">
                    <#list allStatuss as k,v><option value="${k}">${v}</option></#list>

                </select>
				</td>
			</tr>					
			<tr>
				<th>备注：</th>
				<td><textarea rows="3" cols="40" name="comments" class="easyui-validatebox"  validType="byteLength[1,255]"></textarea></td>
			</tr>					
        </table>
      </@jodd.form>
    </form>
</div>
