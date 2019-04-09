<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp"%>
<div style="padding: 5px;font-family:微软雅黑;">
<table class="tableForm" width="100%">
	<tr>
		<th>ID:</th>
		<td>${apiDocScheme.id}</td>
	</tr>					
	<tr>
		<th width="25%">方案编码:</th>
		<td>${apiDocScheme.schemeNo}</td>
	</tr>					
	<tr>
		<th>标题:</th>
		<td>${apiDocScheme.title}</td>
	</tr>					
	<tr>
		<th>作者:</th>
		<td>${apiDocScheme.author}</td>
	</tr>					
	<tr>
		<th>说明:</th>
		<td>${apiDocScheme.note}</td>
	</tr>					
	<tr>
		<th>方案类型:</th>
		<td>${apiDocScheme.schemeType.message}</td>
	</tr>					
	<tr>
		<th>排序值:</th>
		<td>${apiDocScheme.sortTime}</td>
	</tr>					
	<tr>
		<th>创建时间:</th>
		<td><fmt:formatDate value="${apiDocScheme.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
	<tr>
		<th>修改时间:</th>
		<td><fmt:formatDate value="${apiDocScheme.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
	<tr>
		<th>备注:</th>
		<td>${apiDocScheme.comments}</td>
	</tr>					
</table>
</div>
