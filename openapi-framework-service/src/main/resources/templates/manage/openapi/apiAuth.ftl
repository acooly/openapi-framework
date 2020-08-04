<#if ssoEnable>
    <#include "*/include.ftl">
</#if>
<script type="text/javascript">

    $(function () {
        $.acooly.framework.initPage('manage_apiAuth_searchform', 'manage_apiAuth_datagrid');
    });

    /**
     * 打开权限设置界面
     */
    function manage_apiAuth_showSetting() {
        $.acooly.framework.fireSelectRow('manage_apiAuth_datagrid', function (row) {
            var d = $('<div/>').dialog({
                href: '/manage/openapi/apiAuth/setting.json?id=' + row.id,
                width: 1000, height: 600, modal: true,
                title: '<i class="fa fa-cog fa-lg fa-fw fa-col"></i> 设置访问权限: ' + row.accessKey,
                buttons: [
                    {
                        text: '<i class="fa fa-floppy-o fa-lg fa-fw fa-col"></i> 保存',
                        handler: function () {
                            saveAcls(d);
                        }
                    }, {
                        text: '<i class="fa fa-times-circle-o fa-lg fa-fw fa-col"></i> 关闭',
                        handler: function () {
                            d.dialog('close');
                        }
                    }],
                onClose: function () {
                    $(this).dialog('destroy');
                    reloadSubList(row.authNo);
                }
            });
        });
    }

    function manage_apiAuth_action_show(row) {
        var html = "";
        if (!row.parentId) {
            html = "<a onclick=\"$.acooly.framework.edit({url:'/manage/openapi/apiAuth/edit.html',id:'" + row.id + "',entity:'apiAuth',width:600,height:600});\" href=\"#\" title=\"编辑\"><i class=\"fa fa-pencil fa-lg fa-fw fa-col\"></i></a>";
            // " <a onclick=\"$.acooly.framework.show('/manage/openapi/apiAuth/show.html?id="+row.id+"',500,400);\" href=\"#\" title=\"查看\"><i class=\"fa fa-file-o fa-lg fa-fw fa-col\"></i></a>";
        }
        html += "<a onclick=\"$.acooly.framework.remove('/manage/openapi/apiAuth/deleteJson.html','" + row.id + "','manage_apiAuth_datagrid');\" href=\"#\" title=\"删除\"><i class=\"fa fa-trash-o fa-lg fa-fw fa-col\"></i></a>"
        return html;
    }

    function manage_apiAuth_datagrid_loadFilter(data) {
        return data.rows;
    }

</script>
<div class="easyui-layout" data-options="fit : true,border : false">
    <!-- 查询条件 -->
    <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;" align="left">
        <form id="manage_apiAuth_searchform" class="form-inline ac-form-search" onsubmit="return false">
            <div class="form-group">
                <label class="col-form-label">接入方：</label>
                <select name="search_EQ_partnerId" class="form-control input-sm select2bs4"><option value="">所有</option><#list allPartners as k,v><option value="${k}">${k}:${v}</option></#list></select>
            </div>
            <div class="form-group">
                <label class="col-form-label">访问帐号：</label>
                <input type="text" class="form-control form-control-sm" style="width: 200px;" name="search_LIKE_accessKey"/>
            </div>
            <div class="form-group">
                <label class="col-form-label">服务码：</label>
                <input type="text" class="form-control form-control-sm" style="width: 200px;" name="serviceCode" id="serviceCode" />
            </div>
            <div class="form-group">
                <label class="col-form-label">时间：</label>
                <input type="text" class="form-control form-control-sm" id="search_GTE_updateTime" name="search_GTE_updateTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
                <span class="mr-1 ml-1">至</span> <input type="text" class="form-control form-control-sm" id="search_LTE_updateTime" name="search_LTE_updateTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
            </div>
            <div class="form-group">
                <button class="btn btn-sm btn-primary" type="button" onclick="$.acooly.framework.search('manage_apiAuth_searchform','manage_apiAuth_datagrid');"><i class="fa fa-search fa-lg fa-fw fa-col"></i> 查询</button>
            </div>
        </form>
    </div>

    <!-- 列表和工具栏 -->
    <div data-options="region:'center',border:false">
        <table id="manage_apiAuth_datagrid" class="easyui-treegrid" url="/manage/openapi/apiAuth/loadLevel.html" toolbar="#manage_apiAuth_toolbar" fit="true" border="false" fitColumns="false"
               pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true"
               treeField="accessKey" data-options="loadFilter:manage_apiAuth_datagrid_loadFilter, onSelect:manage_apiAuth_onClickRow">
            <thead>
            <tr>
                <th field="showCheckboxWithId" checkbox="true" data-options="formatter:function(value, row, index){ return row.id }">编号</th>
                <th field="id" sum="true">ID</th>
                <th field="partnerId">接入方编码</th>
                <th field="partnerName" data-options="formatter:function(value,row){return formatRefrence('manage_apiAuth_datagrid','allPartners',row.partnerId);} ">接入方名称</th>
                <th field="accessKey">访问帐号</th>
                <th field="secretKey">访问秘钥</th>
                <th field="permissions" width="150px">配置权限</th>
                <th field="whitelistCheck" formatter="mappingFormatter">启用白名单</th>
                <th field="whitelist">白名单</th>
                <th field="rowActions" data-options="formatter:function(value, row, index){return manage_apiAuth_action_show(row)}">动作</th>
            </tr>
            </thead>
        </table>

        <!-- 每行的Action动作模板 -->
        <#--<div id="manage_apiAuth_action" style="display: none;">-->
        <#--<a onclick="$.acooly.framework.edit({url:'/manage/openapi/apiAuth/edit.html',id:'{0}',entity:'apiAuth',width:500,height:400});" href="#" title="编辑"><i class="fa fa-pencil fa-lg fa-fw fa-col"></i></a>-->
        <#--<a onclick="$.acooly.framework.show('/manage/openapi/apiAuth/show.html?id={0}',500,400);" href="#" title="查看"><i class="fa fa-file-o fa-lg fa-fw fa-col"></i></a>-->
        <#--<a onclick="$.acooly.framework.remove('/manage/openapi/apiAuth/deleteJson.html','{0}','manage_apiAuth_datagrid');" href="#" title="删除"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i></a>-->
        <#--</div>-->

        <!-- 表格的工具栏 -->
        <div id="manage_apiAuth_toolbar">
            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.create({url:'/manage/openapi/apiAuth/create.html',entity:'apiAuth',width:600,height:600})"><i class="fa fa-plus-circle fa-lg fa-fw fa-col"></i>添加</a>
            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.removes('/manage/openapi/apiAuth/deleteJson.html','manage_apiAuth_datagrid')"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i>批量删除</a>
            <a href="#" class="easyui-linkbutton" plain="true" onclick="manage_apiAuth_showSetting();"><i class="fa fa-cog fa-lg fa-fw fa-col"></i>设置权限</a>
        </div>
    </div>

    <div data-options="region:'south',border:false" style="height:45%;">
        <div class="easyui-tabs" fit="true">
            <div title="已配置权限">
                <table id="manage_apiAuthMetaService_datagrid" class="easyui-datagrid" toolbar="#manage_apiAuthMetaService_toolbarZZ" fit="true" border="false" fitColumns="false" idField="id" sortName="id" sortOrder="desc">
                    <thead>
                    <tr>
                        <th field="id" sum="false">id</th>
                        <th field="serviceName">服务编号</th>
                        <th field="serviceDesc">服务名称</th>
                        <th field="note" formatter="contentFormatter">服务说明</th>
                        <th field="busiType" formatter="mappingFormatter">业务类型</th>
                        <th field="createTime" formatter="dateTimeFormatter">创建时间</th>
                        <th field="updateTime" formatter="dateTimeFormatter">修改时间</th>
                    </tr>
                    </thead>
                </table>
            </div>
        </div>
    </div>

</div>
<script type="text/javascript">

    function manage_apiAuth_onClickRow(rowData) {
        reloadSubList(rowData.authNo);
    }

    function reloadSubList(authNo) {
        $.acooly.framework.loadGrid({
            gridId: "manage_apiAuthMetaService_datagrid",
            url: '${pageContext.request.contextPath}/manage/openapi/apiAuth/loadMetaServices.html',
            ajaxData: {"authNo": authNo}
        });
    }

    //服务码选择
    $('#serviceCode').combobox({
        url:'/manage/openapi/apiAuth/getAllService.json',
        valueField:'serviceName',
        textField:'serviceDesc',
        groupField:"busiType",
        formatter: function(row){
            var opts = $(this).combobox('options');
            return row[opts.valueField]+"-"+row[opts.textField];
        },
        loadFilter:function(data){
            return data.data;
        },
        filter: function(q, row){
            var opts = $(this).combobox('options');
            return row[opts.textField].indexOf(q) == 0 || row[opts.valueField].indexOf(q) == 0;
        }
    });

</script>