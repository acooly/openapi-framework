<#if ssoEnable><#include "/manage/common/ssoInclude.ftl"></#if>
<div class="easyui-layout" data-options="fit : true,border : false">
    <!-- 查询条件 -->
    <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;">
        <form id="manage_apiAuthDynamic_searchform" class="form-inline ac-form-search" onsubmit="return false">
            <div class="form-group">
                <label class="col-form-label">接入方：</label>
                <select name="search_EQ_partnerId" style="width:200px;" class="form-control input-sm select2bs4">
                    <option value="">所有</option><#list allPartners as k,v>
                    <option value="${k}">${k}:${v}</option></#list></select>
            </div>
            <div class="form-group">
                <label class="col-form-label">父AccessKey：</label>
                <input type="text" class="form-control form-control-sm" placeholder="AccessKey" style="width: 190px;" name="search_RLIKE_accessKey"/>
            </div>
            <div class="form-group">
                <label class="col-form-label">客户标志：</label>
                <input type="text" class="form-control form-control-sm" placeholder="动态秘钥客户标志" style="width: 190px;" name="search_LLIKE_accessKey"/>
            </div>
            <div class="form-group">
                <button class="btn btn-sm btn-primary" type="button" onclick="$.acooly.framework.search('manage_apiAuthDynamic_searchform','manage_apiAuthDynamic_datagrid');"><i class="fa fa-search fa-fw fa-col"></i> 查询</button>
            </div>
        </form>
    </div>

    <!-- 列表和工具栏 -->
    <div data-options="region:'center',border:false">
        <table id="manage_apiAuthDynamic_datagrid" class="easyui-datagrid" url="/manage/openapi/apiAuth/listDynamic.html" toolbar="#manage_apiAuthDynamic_toolbar" fit="true" fitColumns="false"
               pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
            <thead>
            <tr>
                <th field="showCheckboxWithId" checkbox="true" data-options="formatter:function(value, row, index){ return row.id }">编号</th>
                <th field="id" sum="true">ID</th>
                <th field="partnerId">接入方编码</th>
                <th field="partnerName" data-options="formatter:function(value,row){return formatRefrence('manage_apiAuthDynamic_datagrid','allPartners',row.partnerId);} ">接入方名称</th>
                <th field="accessKey">访问帐号(AccessKey)</th>
                <th field="secretKey" formatter="$.acooly.openapi.manage.formatter.password">访问秘钥(SecretKey)</th>
                <th field="status" formatter="mappingFormatter">状态</th>
                <th field="createTime" formatter="dateTimeFormatter">创建时间</th>
                <th field="updateTime" formatter="dateTimeFormatter">修改时间</th>
            </tr>
            </thead>
            <thead frozen="true">
            <th field="rowActions" data-options="formatter:function(value, row, index){return manage_apiAuthDynamic_action_show(row)}">动作</th>
            </thead>
        </table>

        <!-- 表格的工具栏 -->
        <div id="manage_apiAuthDynamic_toolbar">
            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.removes('/manage/openapi/apiAuth/deleteJson.html','manage_apiAuthDynamic_datagrid','','确认删除吗？删除认证秘钥的同时会清除缓存。如果开启的一级缓存，则需要等待所有节点过期。')"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i>批量删除</a>
            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.openapi.manage.clipAuth('manage_apiAuthDynamic_datagrid','#manage_apiAuthDynamic_datagrid_cliAuth_btn')" id="manage_apiAuthDynamic_datagrid_cliAuth_btn"><i class="fa fa-clipboard fa-fw fa-col"></i>复制秘钥信息</a>
        </div>
    </div>

    <script type="text/javascript">

        $(function () {
            $.acooly.framework.initPage('manage_apiAuthDynamic_searchform', 'manage_apiAuthDynamic_datagrid');
        });


        function manage_apiAuthDynamic_action_show(row) {
            var html = "<div class=\"btn-group btn-group-xs\">";
            if (!row.parentId) {
                html += "<a onclick=\"$.acooly.framework.edit({url:'/manage/openapi/apiAuth/edit.html',id:'" + row.id + "',entity:'apiAuth',width:600,height:600});\" class=\"btn btn-outline-primary btn-xs\" href=\"#\" title=\"编辑\"><i class=\"fa fa-pencil\"></i> 编辑</a>"
                // " <a onclick=\"$.acooly.framework.show('/manage/openapi/apiAuth/show.html?id="+row.id+"',500,400);\" href=\"#\" title=\"查看\"><i class=\"fa fa-file-o fa-lg fa-fw fa-col\"></i></a>";
            }
            html += "<a onclick=\"$.acooly.framework.remove('/manage/openapi/apiAuth/deleteJson.html','" + row.id + "','manage_apiAuthDynamic_datagrid','','确认删除吗？删除认证秘钥的同时会清除缓存。如果开启的一级缓存，则需要等待所有节点过期。');\" class=\"btn btn-outline-primary btn-xs\" href=\"#\" title=\"删除\"><i class=\"fa fa-trash-o\"></i> 删除</a>";
            html += "</div>";
            return html;
        }

    </script>

</div>

