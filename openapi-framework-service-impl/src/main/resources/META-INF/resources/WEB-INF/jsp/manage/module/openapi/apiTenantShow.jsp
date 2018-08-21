<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp"%>
<div style="padding: 5px;font-family:微软雅黑;">
<table class="tableForm" width="100%">
	<tr>
		<th>ID:</th>
		<td>${apiTenant.id}</td>
	</tr>					
	<tr>
		<th width="25%">合作方编码:</th>
		<td>${apiTenant.partnerId}</td>
	</tr>					
	<tr>
		<th>合作方名称:</th>
		<td>${apiTenant.partnerName}</td>
	</tr>					
	<tr>
		<th>安全方案:</th>
		<td>${apiTenant.secretType.message}</td>
	</tr>					
	<tr>
		<th>签名类型:</th>
		<td>${apiTenant.signType.message}</td>
	</tr>					
	<tr>
		<th>创建时间:</th>
		<td><fmt:formatDate value="${apiTenant.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
	<tr>
		<th>修改时间:</th>
		<td><fmt:formatDate value="${apiTenant.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
	<tr>
		<th>备注:</th>
		<td>${apiTenant.comments}</td>
	</tr>					
</table>
</div>
