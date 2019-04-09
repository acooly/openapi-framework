<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp"%>
<div>
    <form id="manage_apiDocService_editform" action="${pageContext.request.contextPath}/manage/apidoc/apiDocService/${action=='create'?'saveJson':'updateJson'}.html" method="post">
      <jodd:form bean="apiDocService" scope="request">
        <input name="id" type="hidden" />
        <table class="tableForm" width="100%">
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
				<th>服务标题：</th>
				<td><textarea rows="3" cols="40" placeholder="请输入服务标题..." style="width:300px;" name="title" class="easyui-validatebox" data-options="validType:['length[1,256]'],required:true"></textarea></td>
			</tr>
			<tr>
				<th>所属系统:</th>
				<td>${apiDocService.owner}</td>
			</tr>
			<tr>
				<th>服务说明：</th>
				<td><textarea rows="3" cols="40" placeholder="请输入服务说明..." style="width:300px;" name="note" class="easyui-validatebox" data-options="validType:['length[1,512]']"></textarea></td>
			</tr>					
			<tr>
				<th>手工说明：</th>
				<td><textarea rows="3" cols="40" placeholder="请输入手工说明..." style="width:300px;" name="manualNote" class="easyui-validatebox" data-options="validType:['length[1,512]']"></textarea></td>
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
				<th>备注：</th>
				<td><textarea rows="3" cols="40" placeholder="请输入备注..." style="width:300px;" name="comments" class="easyui-validatebox" data-options="validType:['length[1,255]']"></textarea></td>
			</tr>					
			<tr>
				<th>签名：</th>
				<td><input type="text" name="signature" size="48" placeholder="请输入签名..." class="easyui-validatebox text" data-options="validType:['length[1,128]']"/></td>
			</tr>					
        </table>
      </jodd:form>
    </form>
</div>
