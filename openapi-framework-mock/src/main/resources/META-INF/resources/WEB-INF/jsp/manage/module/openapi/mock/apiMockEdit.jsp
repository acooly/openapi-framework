<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp"%>
<div>
    <form id="manage_apiMock_editform" action="${pageContext.request.contextPath}/manage/module/openapi/mock/apiMock/${action=='create'?'saveJson':'updateJson'}.html" method="post">
      <jodd:form bean="apiMock" scope="request">
        <input name="id" type="hidden" />
        <table class="tableForm" width="100%">
			<tr>
				<th width="25%">服务名称：</th>
				<td><input type="text" name="serviceName" size="48" placeholder="请输入服务名称..." class="easyui-validatebox text" data-options="validType:['length[1,128]'],required:true"/></td>
			</tr>					
			<tr>
				<th>版本号：</th>
				<td><input type="text" name="version" size="48" placeholder="请输入版本号..." class="easyui-validatebox text" data-options="validType:['length[1,32]']"/></td>
			</tr>					
			<tr>
				<th>期望参数：</th>
				<td><textarea rows="3" cols="40" placeholder="请输入期望参数，json代码" style="width:300px;" name="expect" class="easyui-validatebox" data-options="validType:['length[1,256]']"></textarea></td>
			</tr>					
			<tr>
				<th>同步响应：</th>
				<td><textarea rows="3" cols="40" placeholder="请输入同步响应，json代码" style="width:300px;" name="response" class="easyui-validatebox" data-options="validType:['length[1,256]']"></textarea></td>
			</tr>					
			<tr>
				<th>异步响应：</th>
				<td><textarea rows="3" cols="40" placeholder="请输入异步响应，json代码" style="width:300px;" name="notify" class="easyui-validatebox" data-options="validType:['length[1,256]']"></textarea></td>
			</tr>					
        </table>
      </jodd:form>
    </form>
</div>
