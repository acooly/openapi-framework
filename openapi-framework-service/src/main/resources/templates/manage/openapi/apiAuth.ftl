<#if ssoEnable>
    <#include "*/include.ftl">
</#if>
<script type="text/javascript">

    $(function () {
        $.acooly.framework.registerKeydown('manage_apiAuth_searchform', 'manage_apiAuth_datagrid');
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
                            saveAcls();
                        }
                    }, {
                        text: '<i class="fa fa-times-circle-o fa-lg fa-fw fa-col"></i> 关闭',
                        handler: function () {
                            d.dialog('close');
                        }
                    }],
                onClose: function () {
                    $(this).dialog('destroy');
                }
            });
        });
    }

    function manage_apiAuth_action_show(row){
        var html = "";
        if(!row.parentId){
            html = "<a onclick=\"$.acooly.framework.edit({url:'/manage/openapi/apiAuth/edit.html',id:'"+row.id+"',entity:'apiAuth',width:600,height:500});\" href=\"#\" title=\"编辑\"><i class=\"fa fa-pencil fa-lg fa-fw fa-col\"></i></a>";
                    // " <a onclick=\"$.acooly.framework.show('/manage/openapi/apiAuth/show.html?id="+row.id+"',500,400);\" href=\"#\" title=\"查看\"><i class=\"fa fa-file-o fa-lg fa-fw fa-col\"></i></a>";
        }
        html += "<a onclick=\"$.acooly.framework.remove('/manage/openapi/apiAuth/deleteJson.html','"+row.id+"','manage_apiAuth_datagrid');\" href=\"#\" title=\"删除\"><i class=\"fa fa-trash-o fa-lg fa-fw fa-col\"></i></a>"
        return html;
    }

</script>
<div class="easyui-layout" data-options="fit : true,border : false">
    <!-- 查询条件 -->
    <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;" align="left">
        <form id="manage_apiAuth_searchform" onsubmit="return false">
            <table class="tableForm" width="100%">
                <tr>
                    <td align="left">
                        <div>
                            PartnerId: <input type="text" class="text" size="15" name="search_LIKE_partnerId"/>
                            AccessKey: <input type="text" class="text" size="15" name="search_LIKE_accessKey"/>
                            修改时间: <input size="15" type="text" class="text" id="search_GTE_updateTime" name="search_GTE_updateTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
                            至 <input size="15" type="text" class="text" id="search_LTE_updateTime" name="search_LTE_updateTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
                            <a href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:false" onclick="$.acooly.framework.search('manage_apiAuth_searchform','manage_apiAuth_datagrid');"><i class="fa fa-search fa-lg fa-fw fa-col"></i>查询</a>
                        </div>
                    </td>
                </tr>
            </table>
        </form>
    </div>

    <!-- 列表和工具栏 -->
    <div data-options="region:'center',border:false">
        <table id="manage_apiAuth_datagrid" class="easyui-datagrid" url="/manage/openapi/apiAuth/listJson.html" toolbar="#manage_apiAuth_toolbar" fit="true" border="false" fitColumns="false"
               pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
            <thead>
            <tr>
                <th field="showCheckboxWithId" checkbox="true" data-options="formatter:function(value, row, index){ return row.id }">编号</th>
                <th field="id" sum="true">ID</th>
                <th field="partnerId">PartnerId</th>
                <th field="secretType" formatter="mappingFormatter">安全类型</th>
                <th field="signType" formatter="mappingFormatter">签名类型</th>
                <th field="accessKey">AccessKey</th>
                <th field="secretKey">安全码</th>
                <th field="permissions" formatter="contentFormatter">通用权限</th>
                <th field="comments">备注</th>
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
            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.create({url:'/manage/openapi/apiAuth/create.html',entity:'apiAuth',width:600,height:500})"><i class="fa fa-plus-circle fa-lg fa-fw fa-col"></i>添加</a>
            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.removes('/manage/openapi/apiAuth/deleteJson.html','manage_apiAuth_datagrid')"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i>批量删除</a>
            <a href="#" class="easyui-linkbutton" plain="true" onclick="manage_apiAuth_showSetting();"><i class="fa fa-cog fa-lg fa-fw fa-col"></i>设置权限</a>
        </div>
    </div>
</div>
