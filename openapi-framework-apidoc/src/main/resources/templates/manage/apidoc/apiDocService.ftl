<#if ssoEnable><#include "/manage/common/ssoInclude.ftl"></#if>
<div id="dialog_div_id"></div>
<div class="easyui-layout" data-options="fit : true,border : false">
    <!-- 查询条件 -->
    <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;" align="left">
        <form id="manage_apiDocService_searchform" class="form-inline ac-form-search" onsubmit="return false">
            <div class="form-group">
                <label class="col-form-label">服务名称：</label>
                <input type="text" style="width: 190px;" class="form-control form-control-sm" name="search_LIKE_name"/>
            </div>
            <div class="form-group">
                <label class="col-form-label">服务标题：</label>
                <input type="text" class="form-control form-control-sm" name="search_LIKE_title"/>
            </div>
            <div class="form-group">
                <label class="col-form-label">服务类型：</label>
                <select name="search_EQ_serviceType" class="form-control-sm select2bs4">
                    <option value="">所有</option>
                    <#list allServiceTypes as k,v>
                        <option value="${k}">${v}</option></#list>
                </select>
            </div>
            <div class="form-group">
                <label class="col-form-label">业务类型：</label>
                <select name="search_EQ_busiType" class="form-control-sm select2bs4">
                    <option value="">所有</option>
                    <#list allBusiTypes as k,v>
                        <option value="${k}">${v}</option></#list>
                </select>
            </div>
            <div class="form-group">
                <button class="btn btn-sm btn-primary" type="button" onclick="$.acooly.framework.search('manage_apiDocService_searchform','manage_apiDocService_datagrid');"><i class="fa fa-search fa-fw fa-col"></i> 查询</button>
            </div>
        </form>
    </div>


    <!-- 列表和工具栏 -->
    <div data-options="region:'center',border:false">
        <table id="manage_apiDocService_datagrid" class="easyui-datagrid" url="${pageContext.request.contextPath}/manage/apidoc/apiDocService/listJson.html" toolbar="#manage_apiDocService_toolbar" fit="true" border="false" fitColumns="false"
               pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
            <thead>
            <tr>
                <th field="showCheckboxWithId" checkbox="true" data-options="formatter:function(value, row, index){ return row.id }">编号</th>
                <th field="id" sum="true">id</th>
                <th field="name">名称</th>
                <th field="version">版本</th>
                <th field="title">标题</th>
                <th field="owner">所属</th>
                <th field="note" data-options="formatter:function(value, row, index){ return $.acooly.format.content(value,30);  }">服务说明</th>
                <th field="serviceType" formatter="mappingFormatter">服务类型</th>
                <th field="busiType" formatter="mappingFormatter">业务类型</th>
                <th field="updateTime" formatter="dateTimeFormatter">更新时间</th>
                <th field="rowActions" data-options="formatter:function(value, row, index){return  formatString($('#manage_apiDocService_action').html(), row.id,row.serviceNo);}">动作</th>
            </tr>
            </thead>
        </table>

        <!-- 每行的Action动作模板 -->
        <div id="manage_apiDocService_action" style="display: none;">
            <a onclick="$.acooly.framework.edit({url:'/manage/apidoc/apiDocService/edit.html',id:'{0}',entity:'apiDocService',width:500,height:400});" href="#" title="编辑"><i class="fa fa-pencil fa-lg fa-fw fa-col"></i></a>
            <a onclick="showServiceInfo('{1}')" href="javascript:void(0);" title="查看"><i class="fa fa-file-o fa-lg fa-fw fa-col"></i></a>
        </div>

        <!-- 表格的工具栏 -->
        <div id="manage_apiDocService_toolbar"></div>
    </div>

    <script type="text/javascript">
        $(function () {
            $.acooly.framework.initPage('manage_apiDocService_searchform', 'manage_apiDocService_datagrid');
        });

        /**
         * 查询服务详细信息
         */
        function showServiceInfo(serviceId) {
            window.open("/docs/apidoc/scheme/1/" + serviceId + ".html");
        }
    </script>
</div>
