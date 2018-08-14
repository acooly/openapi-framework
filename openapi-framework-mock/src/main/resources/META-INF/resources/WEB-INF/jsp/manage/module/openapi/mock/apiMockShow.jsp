<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp"%>
<div style="padding: 5px;font-family:微软雅黑;">
<table class="tableForm" width="100%">
	<tr>
		<th>id:</th>
		<td>${apiMock.id}</td>
	</tr>					
	<tr>
		<th width="25%">服务名称:</th>
		<td>${apiMock.serviceName}</td>
	</tr>					
	<tr>
		<th>版本号:</th>
		<td>${apiMock.version}</td>
	</tr>					
	<tr>
		<th>期望参数:</th>
		<td>${apiMock.expect}</td>
	</tr>					
	<tr>
		<th>同步响应:</th>
		<td>${apiMock.response}</td>
	</tr>					
	<tr>
		<th>异步响应:</th>
		<td>${apiMock.notify}</td>
	</tr>					
	<tr>
		<th>创建时间:</th>
		<td><fmt:formatDate value="${apiMock.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
	<tr>
		<th>修改时间:</th>
		<td><fmt:formatDate value="${apiMock.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
</table>
</div>
