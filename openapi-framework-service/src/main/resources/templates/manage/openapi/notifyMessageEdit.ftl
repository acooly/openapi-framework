<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_apiNotifyMessage_editform" action="/manage/openapi/notifyMessage/updateJson.html" method="post">
      <@jodd.form bean="notifyMessage" scope="request">
        <input name="id" type="hidden" />
        <table class="tableForm" width="100%">
			<tr>
				<th width="25%">gid：</th>
				<td>${notifyMessage.gid}</td>
			</tr>					
			<tr>
				<th>接入方ID：</th>
				<td>${notifyMessage.partnerId}</td>
			</tr>					
			<tr>
				<th>请求序号：</th>
				<td>${notifyMessage.requestNo}</td>
			</tr>					
			<tr>
				<th>商户订单号：</th>
				<td>${notifyMessage.merchOrderNo}</td>
			</tr>
			<tr>
				<th>服务：</th>
				<td>${notifyMessage.service}_${notifyMessage.version}</td>
			</tr>
			<tr>
				<th>通知响应：</th>
				<td>${notifyMessage.respInfo}</td>
			</tr>					
			<tr>
				<th>已通知次数：</th>
				<td><input type="text" name="sendCount" size="25" class="easyui-validatebox text" data-options="required:true"></td>
			</tr>
			<tr>
				<th>通知地址：</th>
				<td><textarea rows="3" cols="40" style="width:300px;" name="url" class="easyui-validatebox" data-options="required:true" validType="byteLength[1,255]"></textarea></td>
			</tr>
			<tr>
				<th>下次通知时间：</th>
				<td><input type="text" name="nextSendTime" size="25" class="easyui-validatebox text" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" /></td>
			</tr>					
			<tr>
				<th>通知状态：</th>
				<td>
                    <select name="status" editable="false" style="height:27px;" panelHeight="auto" class="easyui-combobox" data-options="required:true">
						<#list allStatuss as k,v><option <#if notifyMessage.status.code()==k>selected</#if> value="${k}">${v}</option></#list>
					</select>
                </td>
			</tr>
            <tr>
                <th>注意：</th>
                <td><span style="color: red;">非特殊情况,请不要轻易进行修改操作~!</span></td>
            </tr>
        </table>
      </@jodd.form>
    </form>
</div>
