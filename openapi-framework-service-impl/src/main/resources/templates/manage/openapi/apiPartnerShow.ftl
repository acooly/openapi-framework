<div style="padding: 5px;font-family:微软雅黑;">
<table class="tableForm" width="100%">
	<tr>
		<th>ID:</th>
		<td>${apiPartner.id}</td>
	</tr>					
	<tr>
		<th width="25%">合作方编码:</th>
		<td>${apiPartner.partnerId}</td>
	</tr>					
	<tr>
		<th>合作方名称:</th>
		<td>${apiPartner.partnerName}</td>
	</tr>					
	<tr>
		<th>安全方案:</th>
		<td>${apiPartner.secretType.message()}</td>
	</tr>					
	<tr>
		<th>签名类型:</th>
		<td>${apiPartner.signType.message()}</td>
	</tr>					
	<tr>
		<th>秘钥:</th>
		<td>${apiPartner.secretKey}</td>
	</tr>					
	<tr>
		<th>创建时间:</th>
		<td>${(apiPartner.createTime?string("yyyy-MM-dd HH:dd:ss"))!}</td>
	</tr>
	<tr>
		<th>更新时间:</th>
		<td>${(apiPartner.updateTime?string("yyyy-MM-dd HH:dd:ss"))!}</td>
	</tr>
	<tr>
		<th>备注:</th>
		<td>${apiPartner.comments}</td>
	</tr>
</table>
</div>
