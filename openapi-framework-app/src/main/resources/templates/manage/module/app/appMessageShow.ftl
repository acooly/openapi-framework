<div style="padding: 5px;font-family:微软雅黑;">
<table class="tableForm" width="100%">
	<tr>
		<th width="100">标题:</th>
		<td>${appMessage.title}</td>
	</tr>	
	<tr>
		<th>内容:</th>
		<td>${appMessage.content}</td>
	</tr>	
	<tr>
		<th>会话参数:</th>
		<td><pre style="width:350px;"><code class="json">${appMessage.context}</code></pre></td>
	</tr>				
	<tr>
		<th>推送时间:</th>
		<td>${appMessage.sendTime}</td>
	</tr>					
	<tr>
		<th>推送类型:</th>
		<td>${appMessage.type.message}</td>
	</tr>					
	<tr>
		<th>发送人:</th>
		<td>${appMessage.sender}</td>
	</tr>					
	<tr>
		<th>接收人:</th>
		<td>${appMessage.receivers}</td>
	</tr>					
	<tr>
		<th>状态.:</th>
		<td>${appMessage.status.message}</td>
	</tr>					
	<tr>
		<th>备注:</th>
		<td>${appMessage.comments}</td>
	</tr>					
</table>
</div>