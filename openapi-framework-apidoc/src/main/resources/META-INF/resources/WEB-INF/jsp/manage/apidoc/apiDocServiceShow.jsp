<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp"%>
<div style="padding: 5px;font-family:微软雅黑;">
<table class="tableForm" width="100%">
	<tr>
		<th>id:</th>
		<td>${apiDocService.id}</td>
	</tr>					
	<tr>
		<th width="25%">服务编号:</th>
		<td>${apiDocService.serviceNo}</td>
	</tr>					
	<tr>
		<th>服务名称:</th>
		<td>${apiDocService.name}</td>
	</tr>					
	<tr>
		<th>服务版本:</th>
		<td>${apiDocService.version}</td>
	</tr>					
	<tr>
		<th>服务标题:</th>
		<td>${apiDocService.title}</td>
	</tr>					
	<tr>
		<th>所属系统:</th>
		<td>${apiDocService.owner}</td>
	</tr>					
	<tr>
		<th>服务说明:</th>
		<td>${apiDocService.note}</td>
	</tr>					
	<tr>
		<th>手工说明:</th>
		<td>${apiDocService.manualNote}</td>
	</tr>					
	<tr>
		<th>服务类型:</th>
		<td>${apiDocService.serviceType.msg}</td>
	</tr>					
	<tr>
		<th>业务类型:</th>
		<td>${apiDocService.busiType}</td>
	</tr>					
	<tr>
		<th>排序值:</th>
		<td>${apiDocService.sortTime}</td>
	</tr>					
	<tr>
		<th>备注:</th>
		<td>${apiDocService.comments}</td>
	</tr>					
	<tr>
		<th>创建时间:</th>
		<td><fmt:formatDate value="${apiDocService.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
	<tr>
		<th>修改时间:</th>
		<td><fmt:formatDate value="${apiDocService.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
	<tr>
		<th>签名:</th>
		<td>${apiDocService.signature}</td>
	</tr>					
</table>
</div>
