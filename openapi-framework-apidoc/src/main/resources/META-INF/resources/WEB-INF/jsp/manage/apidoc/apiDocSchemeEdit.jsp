<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp"%>
<script language="JavaScript">

    $(function () {
        var token = $("meta[name='X-CSRF-TOKEN']").attr("content");// 从meta中获取token
        $.acooly.framework.kingEditor({
            uploadUrl: '/ofile/kindEditor.html?_csrf=' + token,
            minHeight: '310',
            textareaId: 'contentId'
        });
    });
</script>
<div>
    <form id="manage_apiDocScheme_editform" action="${pageContext.request.contextPath}/manage/apidoc/apiDocScheme/${action=='create'?'saveJson':'updateJson'}.html" method="post">
      <jodd:form bean="apiDocScheme" scope="request">
        <input name="id" type="hidden" />
        <table class="tableForm" width="100%">
			<tr>
				<th>标题：</th>
				<td><input type="text" name="title" size="48" placeholder="请输入标题..." class="easyui-validatebox text" data-options="validType:['length[1,64]'],required:true"/></td>
			</tr>
			<tr>
				<th>作者：</th>
				<td><input type="text" name="author" size="48" placeholder="请输入作者..." class="easyui-validatebox text" data-options="validType:['length[1,128]']"/></td>
			</tr>
			<tr>
				<th>方案类型：</th>
				<td><select name="schemeType" editable="false" panelHeight="auto" class="easyui-combobox" >
					<c:forEach items="${allSchemeTypes}" var="e">
						<option value="${e.key}">${e.value}</option>
					</c:forEach>
				</select></td>
			</tr>
			<tr>
				<th style="width: 80px">描述：</th>
				<td colspan="2">
                    <textarea id="contentId" name="schemeDesc" data-options="required:true"
							  style="width:100%;height:430px;">${apiDocScheme.apiDocschemeDesc.schemeDesc}</textarea>
				</td>
			</tr>
        </table>
      </jodd:form>
    </form>
</div>
