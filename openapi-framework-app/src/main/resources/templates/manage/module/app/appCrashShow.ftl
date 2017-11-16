<div style="padding: 5px; font-family: 微软雅黑;">
	<table class="tableForm" width="100%">
		<tr>
			<th width="25%">应用名称:</th>
			<td>${appCrash.appName}</td>
			<th width="25%">用户名:</th>
			<td>${appCrash.userName}</td>
		</tr>
		<tr>
			<th>平台ID:</th>
			<td>${appCrash.platformId}</td>
			<th>Android版本号:</th>
			<td>${appCrash.androidVersion}</td>
		</tr>
		<tr>
			<th>应用版本代码:</th>
			<td>${appCrash.appVersionCode}</td>
			<th>应用版本名称:</th>
			<td>${appCrash.appVersionName}</td>
		</tr>

		<tr>
			<th>设备ID:</th>
			<td>${appCrash.deviceId}</td>
			<th>手机/平板的模型:</th>
			<td>${appCrash.model}</td>
		</tr>
		<tr>
			<th>品牌:</th>
			<td>${appCrash.brand}</td>
			<th>Android产品信息:</th>
			<td>${appCrash.product}</td>
		</tr>
		<tr>
			<th>崩溃的时间点:</th>
			<td>${appCrash.crashDate}</td>
			<th>应用的包名:</th>
			<td>${appCrash.packageName}</td>
		</tr>
	</table>
	<h3>堆栈信息</h3>
	<div>
		<pre>${appCrash.stackTrace}</pre>
	</div>
</div>
