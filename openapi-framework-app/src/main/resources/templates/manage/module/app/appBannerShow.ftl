<div style="padding: 5px;font-family:微软雅黑;">
<table class="tableForm" width="100%">
	<tr>
		<th width="20%">标题:</th>
		<td>${appBanner.title}</td>
	</tr>					
	<tr>
		<th>广告图片:</th>
		<td><img src="${serverRoot}${appBanner.mediaUrl}" width="288"/></td>
	</tr>					
	<tr>
		<th>内容链接:</th>
		<td>${appBanner.link}</td>
	</tr>					
	<tr>
		<th>创建时间:</th>
		<td>${appBanner.createTime}</td>
	</tr>					
	<tr>
		<th>最后修改时间:</th>
		<td>${appBanner.updateTime?string('yyyy-MM-dd HH:mm:ss')}</td>
	</tr>					
	<tr>
		<th>备注:</th>
		<td>${appBanner.comments}</td>
	</tr>					
</table>
</div>
