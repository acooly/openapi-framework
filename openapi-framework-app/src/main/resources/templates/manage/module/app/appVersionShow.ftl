<div style="padding: 5px; font-family: 微软雅黑;">
	<table class="tableForm" width="100%">
		<tr>
			<th width="25%">设备类型:</th>
			<td>${appVersion.deviceType}</td>
		</tr>
		<tr>
			<th>版本编码:</th>
			<td>${appVersion.versionCode}</td>
		</tr>
		<tr>
			<th>版本号:</th>
			<td>${appVersion.versionName}</td>
		</tr>
		<tr>
			<th>存储地址:</th>
			<td>${appVersion.path}</td>
		</tr>
		<tr>
			<th>下载地址:</th>
			<td>
				文件地址：<a href="${appVersion.url}" target="_blank">${appVersion.url}</a><br>
				固定地址：<a href="${platformHost}/app/${appVersion.appCode}/${appVersion.deviceType}.html" target="_blank">${platformHost}/app/${appVersion.appCode}/${appVersion.deviceType}.html</a>
			</td>
		</tr>
		<tr>
			<th>Iphone安装地址:</th>
			<td><a href="${appVersion.appleUrl}" target="_blank">${appVersion.appleUrl}</a></td>
		</tr>		
		<tr>
			<th>版本说明:</th>
			<td>${appVersion.subject}</td>
		</tr>
		<tr>
			<th>发布时间:</th>
			<td>${appVersion.pubTime}</td>
		</tr>
		<tr>
			<th>强制更新:</th>
			<td>${allForceUpdates.get(appVersion.forceUpdate?string)}</td>
		</tr>
		<tr>
			<th>备注:</th>
			<td>${appVersion.comments}</td>
		</tr>
	</table>
</div>
