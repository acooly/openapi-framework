<div>
    <form id="manage_appMessage_editform" action="${rc.contextPath}/manage/module/app/appMessage/push.html" method="post">
        <table class="tableForm" width="100%">
			<tr>
				<th width="25%">标题：</th>
				<td><input type="text" name="title" class="easyui-validatebox" data-options="required:true" validType="byteLength[1,32]"/></td>
			</tr>	
				<th>内容分类：</th>
				<td>
				<select name="contentType" editable="false" panelHeight="auto" class="easyui-combobox">
                    <#list allContentTypes as k,v><option value="${k}">${v}</option></#list>

                </select>
				</td>
			</tr>							
			<tr>
				<th>发送内容：</th>
				<td><textarea rows="3" cols="40" name="content" class="easyui-validatebox" data-options="required:true" validType="byteLength[1,1024]"></textarea></td>
			</tr>
			<tr>
				<th>接收人：</th>
				<td><textarea rows="3" cols="40" name="receivers" class="easyui-validatebox"  validType="byteLength[1,256]"></textarea>
				<div>多个逗号分隔。为空则表示广播</div>
				</td>
			</tr>
			<tr>
				<th>扩展参数：</th>
				<td><textarea rows="3" cols="40" name="context" class="easyui-validatebox"  validType="byteLength[1,256]"></textarea>
				<div>标准JSON格式</div>
				</td>
			</tr>			
			<tr>
				<th>JPush-APNs：</th>
				<td><input type="text" name="online" class="easyui-validatebox" data-options="required:false" validType="byteLength[1,32]"/>
				<div>只用于JPush推送。对应options.apns_production, 可选true/false</div>
				</td>
			</tr>												
        </table>
    </form>
</div>
