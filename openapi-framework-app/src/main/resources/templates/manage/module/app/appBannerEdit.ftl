<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] /> 
<div>
	<form id="manage_appBanner_editform" action="/manage/module/app/appBanner/<#if action == 'create'>save<#else>update</#if>Json.html" method="post" enctype="multipart/form-data">
		<@jodd.form bean="appBanner" scope="request">
			<input name="id" type="hidden" />
			<table class="tableForm" width="100%">
				<tr>
					<th width="25%">标题：</th>
					<td><input type="text" name="title" class="easyui-validatebox" size="40"size="40" data-options="required:true" validType="byteLength[1,64]" /></td>
				</tr>
				<tr>
					<th>广告图片：</th>
					<td>
					<input type="file" name="bannerFile" data-options="required:false" /> 
					<div style="margin-top:5px;">规格:720px*257px </div>
					<#if action == 'edit'>
					<div style="margin:10px 0px;"><img src="${serverRoot}${appBanner.mediaUrl}" width="288"/></div>
					</#if>
					</td>
				</tr>
				<tr>
					<th>内容链接：</th>
					<td><input type="text" name="link" class="easyui-validatebox" size="40" data-options="required:false" validType="byteLength[1,128]" /></td>
				</tr>
				<tr>
					<th>备注：</th>
					<td><textarea rows="3" cols="40" name="comments" class="easyui-validatebox" validType="byteLength[1,255]"></textarea></td>
				</tr>
			</table>
		</@jodd.form>
	</form>
</div>
