<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp"%>
<div style="padding: 5px;font-family:微软雅黑;">
<table class="tableForm" width="100%">
	<tr>
		<th>ID:</th>
		<td>${apiAuth.id}</td>
	</tr>					
	<tr>
		<th width="25%">访问帐号:</th>
		<td>${apiAuth.accessKey}</td>
	</tr>					
	<tr>
		<th>访问秘钥:</th>
		<td>${apiAuth.secretKey}</td>
	</tr>					
	<tr>
		<th>访问权限:</th>
		<td>${apiAuth.permissions}</td>
	</tr>					
	<tr>
		<th>创建时间:</th>
		<td><fmt:formatDate value="${apiAuth.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
	<tr>
		<th>修改时间:</th>
		<td><fmt:formatDate value="${apiAuth.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
	<tr>
		<th>备注:</th>
		<td>${apiAuth.comments}</td>
	</tr>					
</table>
</div>
