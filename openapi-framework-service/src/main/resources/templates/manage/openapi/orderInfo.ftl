<#if ssoEnable>
    <#include "*/include.ftl">
</#if>
<div class="easyui-layout" data-options="fit : true,border : false">
    <!-- 查询条件 -->
    <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;" align="left">
        <form id="manage_orderInfo_searchform" class="form-inline ac-form-search" onsubmit="return false">
            <div class="form-group">
                <label class="col-form-label">GID：</label>
                <input type="text" class="form-control form-control-sm" style="width: 150px;" name="search_EQ_gid"/>
            </div>
            <div class="form-group">
                <label class="col-form-label">requestNo：</label>
                <input type="text" class="form-control form-control-sm" style="width: 150px;" name="search_EQ_requestNo"/>
            </div>
            <div class="form-group">
                <label class="col-form-label">商户ID：</label>
                <input type="text" class="form-control form-control-sm" name="search_EQ_partnerId"/>
            </div>
            <div class="form-group">
                <label class="col-form-label">服务码：</label>
                <input type="text" class="form-control form-control-sm" name="search_EQ_service"/>
            </div>
            <div class="form-group">
                <label class="col-form-label">时间：</label>
                <input type="text" class="form-control form-control-sm" id="search_GTE_rawAddTime" name="search_GTE_rawAddTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
                <span class="mr-1 ml-1">至</span> <input type="text" class="form-control form-control-sm" id="search_LTE_rawAddTime" name="search_LTE_rawAddTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
            </div>
            <div class="form-group">
                <button class="btn btn-sm btn-primary" type="button" onclick="$.acooly.framework.search('manage_orderInfo_searchform','manage_orderInfo_datagrid');"><i class="fa fa-search fa-lg fa-fw fa-col"></i> 查询</button>
            </div>
        </form>
    </div>

    <!-- 列表和工具栏 -->
    <div data-options="region:'center',border:false">
        <table id="manage_orderInfo_datagrid" class="easyui-datagrid" url="/manage/openapi/orderInfo/listJson.html" toolbar="" fit="true" border="false" fitColumns="false"
               pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc"
               checkOnSelect="true" selectOnCheck="true" singleSelect="true">
            <thead>
            <tr>
                <th field="showCheckboxWithId" checkbox="true" data-options="formatter:function(value, row, index){ return row.id }">编号</th>
                <th field="id">ID</th>
                <th field="gid">GID</th>
                <th field="requestNo">requestNo</th>
                <th field="rawAddTime">请求时间</th>
                <th field="partnerId">商户ID</th>
                <th field="accessKey">访问码</th>
                <th field="service">服务码</th>
                <th field="notifyUrl">通知地址</th>
                <th field="returnUrl">返回地址</th>
                <th field="context" formatter="contentFormatter">会话信息</th>
                <th field="businessInfo" formatter="jsonFormatter">扩展信息</th>
            </tr>
            </thead>
        </table>

        <!-- 每行的Action动作模板 -->
        <div id="manage_orderInfo_action" style="display: none;">
            <a class="line-action icon-edit" onclick="$.acooly.framework.edit({url:'/manage/openapi/orderInfo/edit.html',id:'{0}',entity:'orderInfo',width:500,height:400});" href="#" title="编辑"></a>
            <a class="line-action icon-show" onclick="$.acooly.framework.show('/manage/openapi/orderInfo/show.html?id={0}',500,400);" href="#" title="查看"></a>
            <a class="line-action icon-delete" onclick="$.acooly.framework.remove('/manage/openapi/orderInfo/deleteJson.html','{0}','manage_orderInfo_datagrid');" href="#" title="删除"></a>
        </div>

        <!-- 表格的工具栏 -->
        <div id="manage_orderInfo_toolbar">
            <a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="$.acooly.framework.create({url:'/manage/openapi/orderInfo/create.html',entity:'orderInfo',width:500,height:400})">添加</a>
            <a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="$.acooly.framework.removes('/manage/openapi/orderInfo/deleteJson.html','manage_orderInfo_datagrid')">批量删除</a>
            <a href="#" class="easyui-menubutton" data-options="menu:'#manage_orderInfo_exports_menu',iconCls:'icon-export'">批量导出</a>
            <div id="manage_orderInfo_exports_menu" style="width:150px;">
                <div data-options="iconCls:'icon-excel'" onclick="$.acooly.framework.exports('/manage/openapi/orderInfo/exportXls.html','manage_orderInfo_searchform','请求信息表')">Excel</div>
                <div data-options="iconCls:'icon-csv'" onclick="$.acooly.framework.exports('/manage/openapi/orderInfo/exportCsv.html','manage_orderInfo_searchform','请求信息表')">CSV</div>
            </div>
            <a href="#" class="easyui-linkbutton" iconCls="icon-import" plain="true" onclick="$.acooly.framework.imports({url:'/manage/openapi/orderInfo/importView.html',uploader:'manage_orderInfo_import_uploader_file'});">批量导入</a>
        </div>
    </div>
    <script type="text/javascript">
        $(function () {
            $.acooly.framework.initPage('manage_orderInfo_searchform', 'manage_orderInfo_datagrid');
        });
    </script>
</div>
