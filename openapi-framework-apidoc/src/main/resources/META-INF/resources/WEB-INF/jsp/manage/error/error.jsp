<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp"%>
<html>
<head>
<title>系统提示</title>
<link id="easyuiTheme" rel="stylesheet" href="${pageContext.request.contextPath}/manage/plugin/jquery-easyui-1.3.1/themes/<c:out value="${cookie.easyuiThemeName.value}" default="default"/>/easyui.css" type="text/css"></link>
<link rel="stylesheet" href="${pageContext.request.contextPath}/manage/plugin/jquery-easyui-1.3.1/themes/icon.css" type="text/css"></link>

</head>
<body>
<div class="panel" style="display: block; width: 500px;margin:auto;padding:100px;">
  <div class="panel-header" style="width: 488px;">
    <div class="panel-title panel-with-icon">系统提示</div>
    <div class="panel-icon icon-tip"></div>
  </div>
  <div class="easyui-panel panel-body" style="width: 498px; text-align: center;padding-top: 20px;padding-bottom: 20px;">
    <table align="center">
      <tr>
        <td align="right"><img src="${pageContext.request.contextPath}/manage/style/icons/action/attention.png"/></td>
        <td align="left" style="font-size: 16px; font-weight: bold; vertical-align:middle;padding-left: 5px;">
        <%
        Exception e = (Exception)request.getAttribute("exception");
        if(e != null){
        	out.print(e.getMessage()); 
        }
        %>
        </td>
      </tr>
    </table>
  </div>
</div>
</body>
</html>
