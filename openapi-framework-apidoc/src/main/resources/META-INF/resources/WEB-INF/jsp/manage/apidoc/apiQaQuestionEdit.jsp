<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp"%>
<script>
    $(function () {
        var token = $("meta[name='X-CSRF-TOKEN']").attr("content");// 从meta中获取token
        $.acooly.framework.kingEditor({
            uploadUrl : '/ofile/kindEditor.html?_csrf=' + token,
            minHeight : '310',
            textareaId : 'contentId'
        });
    });
</script>
<div>
	<form id="manage_apiQaQuestion_editform" action="${pageContext.request.contextPath}/manage/apidoc/apiQaQuestion/${action=='create'?'saveJson':'updateJson'}.html" method="post">
		<jodd:form bean="apiQaQuestion" scope="request">
			<input name="id" type="hidden" />
			<table class="tableForm" width="100%" align="center">
				<tr>
					<th style="width: 80px">分类：</th>
					<td>${typeName}<input type="hidden" name="classifyId" value="${typeId}" />
				</tr>
				<tr>
					<th style="width: 80px">问题标题：</th>
					<td><input type="text" name="problem" size="64" class="easyui-validatebox text" data-options="required:true" validType="byteLength[1,64]"/></td>
				</tr>

				<tr>
					<th style="width: 80px">问题详情</th>
					<td colspan="2">
                    <textarea id="contentId" name="solution" data-options="required:true"
							  style="width:100%;height:430px;"></textarea>
					</td>
				</tr>
			</table>
		</jodd:form>
	</form>
</div>
