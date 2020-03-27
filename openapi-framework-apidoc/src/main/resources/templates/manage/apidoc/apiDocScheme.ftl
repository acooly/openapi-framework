<script type="text/javascript">
    $(function () {
        $("#manage_apiDocScheme_datagrid${category}").treegrid({
            idField :'id',
            treeField: 'title',
            url: "${pageContext.request.contextPath}/manage/apidoc/apiDocScheme/loadTree.html?category=${category}",
            onLoadSuccess: function(row){
                $(this).treegrid('enableDnd', row?row.id:null);
            },
            onBeforeDrop :function(targetRow, sourceRow, point){
                if (!point || point == null|| !targetRow || targetRow == null || !sourceRow || sourceRow == null){
                    return false;
                }
                $.ajax({
                    url : '/manage/apidoc/apiDocScheme/move.html',
                    data : {
                        sourceId : sourceRow.id,
                        targetId : targetRow.id,
                        point : point
                    },
                    success : function(data, status) {
                        if (data.success) {
                            $("#manage_apiDocScheme_datagrid${category}").treegrid('reload');
                            // console.info("save move success!");
                        }else {
                            return false;
                        }
                    }
                });
            }
        });
        $.acooly.framework.registerKeydown('manage_apiDocScheme_searchform${category}', 'manage_apiDocScheme_datagrid${category}');
    });
</script>
<script src="/plugin/easyui/treegrid-dnd.js?${category}"></script>
<div class="easyui-layout" data-options="fit : true,border : false">
    <form id="manage_apiDocScheme_searchform${category}" onsubmit="return false">
        <table class="tableForm" width="100%">
            <tr>
                <td align="left">
                    <div>
                        编码: <input type="text" class="text" size="15" name="search_LIKE_schemeNo"/>
                        标题: <input type="text" class="text" size="15" name="search_LIKE_title"/>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:false"
                           onclick="$.acooly.framework.search('manage_apiDocScheme_searchform${category}','manage_apiDocScheme_datagrid${category}');"><i
                                class="fa fa-search fa-lg fa-fw fa-col"></i>查询</a>
                    </div>
                </td>
            </tr>
        </table>
    </form>
    <!-- 列表和工具栏 -->
    <div data-options="region:'center',border:false" style="height: 55%">
        <table id="manage_apiDocScheme_datagrid${category}" class="easyui-treegrid"
               toolbar="#manage_apiDocScheme_toolbar${category}" fit="true" animate="true"
               border="false" fitColumns="true" onBeforeDrop="dropCallBack"
               rownumbers="true" idField="id" treeField="title" collapsible="true" checkOnSelect="true" selectOnCheck="true"
               data-options="onClickRow:manage_apiDocScheme_onClickRow${category}">
            <thead>
            <tr>
                <th field="showCheckboxWithId" checkbox="true" data-options="formatter:function(value, row, index){ return row.id }">编号</th>
                <th field="id" sortable="true" sum="true">id</th>
                <th field="schemeNo">编码</th>
                <th field="title" formatter="contentFormatter">标题</th>
                <th field="category">分类</th>
                <th field="subCount">子节点数量</th>
                <th field="statusText">状态</th>
                <th field="createTime" formatter="dateTimeFormatter">创建时间</th>
                <th field="updateTime" formatter="dateTimeFormatter">更新时间</th>
                <th field="rowActions" data-options="formatter:manage_apiDocScheme_action_formatter${category}">动作</th>
            </tr>
            </thead>
        </table>

        <!-- 每行的Action动作模板 -->
        <div id="manage_apiDocScheme_action_create${category}" style="display: none;">
            <a onclick="$.acooly.framework.create({url:'/manage/apidoc/apiDocScheme/create.html?parentId={0}&category=${category}',form: 'manage_apiDocScheme_editform${category}', datagrid: 'manage_apiDocScheme_datagrid${category}',width:400,height:280,reload:true});"
               href="#" title="添加"><i class="fa fa-plus-circle fa-lg fa-fw fa-col"></i></a>
        </div>

        <div id="manage_apiDocScheme_action_edit${category}" style="display: none;">
            <a onclick="$.acooly.framework.edit({url:'/manage/apidoc/apiDocScheme/edit.html?category=${category}',id:'{0}',form: 'manage_apiDocScheme_editform${category}', datagrid: 'manage_apiDocScheme_datagrid${category}',width:400,height:280,reload:true});"
               href="#" title="编辑"><i class="fa fa-pencil fa-lg fa-fw fa-col"></i></a>
        </div>
        <div id="manage_apiDocScheme_action_remove${category}" style="display: none;">
            <a onclick="$.acooly.framework.remove('/manage/apidoc/apiDocScheme/deleteJson.html','{0}','manage_apiDocScheme_datagrid${category}');" href="#"
               title="删除"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i></a>
        </div>
        <div id="manage_apiDocScheme_action_onShelf${category}" style="display: none;">
            <a href="#" onclick="$.acooly.framework.confirmSubmit('/manage/apidoc/apiDocScheme/changeStatus.html?status=onShelf','{0}','manage_apiDocScheme_datagrid${category}','确定','您是否要提交该操作?')"
               title="上架">上架</a>
        </div>
        <div id="manage_apiDocScheme_action_offShelf${category}" style="display: none;">
            <a href="#" onclick="$.acooly.framework.confirmSubmit('/manage/apidoc/apiDocScheme/changeStatus.html?status=offShelf','{0}','manage_apiDocScheme_datagrid${category}','确定','您是否要提交该操作?')"
               title="下架">下架</a>
        </div>
        <div id="manage_apiDocScheme_action_doc${category}" style="display: none;">
            <a onclick="$.acooly.framework.edit({url:'/manage/apidoc/apiDocSchemeDesc/createOrEdit.html?schemeNo={1}',id:'{0}',form: 'manage_apiDocSchemeDesc_editform', datagrid: 'manage_apiDocScheme_datagrid${category}',width:1200,height:800,reload:false});"
               href="#" title="文章内容"><i class="fa fa-file-text fa-lg fa-fw fa-col" aria-hidden="true"></i></a>
        </div>
        <div id="manage_apiDocScheme_action_api${category}" style="display: none;">
            <a href="#" onclick="showApis${category}('{0}','{1}')" title="关联api">关联api</a>
        </div>
        <#--            <a onclick="$.acooly.framework.show('/manage/module/security/apiDocScheme/show.html?id={0}',600,500);" href="#" title="查看"><i-->
        <#--                        class="fa fa-file-o fa-lg fa-fw fa-col"></i></a>-->


        <!-- 表格的工具栏 -->
        <div id="manage_apiDocScheme_toolbar${category}">
            <a href="#" class="easyui-linkbutton" plain="true"
               onclick="$.acooly.framework.create({url:'/manage/apidoc/apiDocScheme/create.html?category=${category}',form: 'manage_apiDocScheme_editform${category}', datagrid: 'manage_apiDocScheme_datagrid${category}',width:400,height:280})"><i
                    class="fa fa-plus-circle fa-lg fa-fw fa-col"></i>添加</a>
        </div>
    </div>

    <div data-options="region:'south',border:false" style="height:45%;">
        <div class="easyui-tabs" fit="true">
            <div title="关联api列表">
                <table id="manage_apiDocSchemeService_datagrid${category}" class="easyui-datagrid" toolbar="#manage_apiDocSchemeService_toolbar${category}" fit="true" border="false" fitColumns="false"
                       pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
                    <thead>
                    <tr>
                        <th field="showCheckboxWithId" checkbox="true" data-options="formatter:function(value, row, index){ return row.id }">编号</th>
                        <th field="id" sum="true">id</th>
                        <th field="serviceNo">服务编号</th>
                        <th field="name">服务名称</th>
                        <th field="version">服务版本</th>
                        <th field="title">服务标题</th>
                        <th field="owner">所属系统</th>
                        <th field="note">服务说明</th>
                        <th field="manualNote" formatter="contentFormatter">手工说明</th>
                        <th field="serviceType" formatter="mappingFormatter">服务类型</th>
                        <th field="busiType" formatter="mappingFormatter">业务类型</th>
                        <th field="comments">备注</th>
                        <th field="createTime" formatter="dateTimeFormatter">创建时间</th>
                        <th field="updateTime" formatter="dateTimeFormatter">修改时间</th>
                        <th field="signature">签名</th>
                        <th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_apiDocSchemeService_action',value,row)}">动作</th>
                    </tr>
                    </thead>
                </table>

                <!-- 每行的Action动作模板 -->
                <div id="manage_apiDocSchemeService_action" style="display: none;">
                    <a onclick="$.acooly.framework.confirmSubmit('/manage/apidoc/apiDocScheme/changeSchemaServiceOrder.html?direction=up','{0}','manage_apiDocSchemeService_datagrid${category}','确定','您是否要进行该操作?');" href="#" title="上移"><i class="fa fa-long-arrow-up fa-fw fa-col"></i></a>
                    <a onclick="$.acooly.framework.confirmSubmit('/manage/apidoc/apiDocScheme/changeSchemaServiceOrder.html?direction=down','{0}','manage_apiDocSchemeService_datagrid${category}','确定','您是否要进行该操作?');" href="#" title="下移"><i class="fa fa-long-arrow-down fa-fw fa-col"></i></a>
                </div>

            </div>
        </div>
    </div>
</div>
<#--<div id="edit_scheme_id${category}"></div>-->
<script type="text/javascript">
    function manage_apiDocScheme_action_formatter${category}(value, row, index){
        let actionHtml = "";
        actionHtml += formatString($('#manage_apiDocScheme_action_create${category}').html(), row.id);
        actionHtml += formatString($('#manage_apiDocScheme_action_edit${category}').html(), row.id);
        if (row.subCount == 0) {
            actionHtml += formatString($('#manage_apiDocScheme_action_doc${category}').html(), row.id, row.schemeNo);
            actionHtml += formatString($('#manage_apiDocScheme_action_api${category}').html(), row.id, row.schemeNo);
        }
        if (row.status == 'draft' || row.status == 'offShelf') {
            actionHtml += formatString($('#manage_apiDocScheme_action_onShelf${category}').html(), row.id);
        } else if(row.status == 'onShelf'){
            actionHtml += formatString($('#manage_apiDocScheme_action_offShelf${category}').html(), row.id);
        }
        actionHtml += formatString($('#manage_apiDocScheme_action_remove${category}').html(), row.id);
        return actionHtml;
    }

    function manage_apiDocScheme_onClickRow${category}(rowData) {
        $.acooly.framework.loadGrid({
            gridId: "manage_apiDocSchemeService_datagrid${category}",
            url: '${pageContext.request.contextPath}/manage/apidoc/apiDocScheme/schemeServiceList.html',
            ajaxData: {"schemeNo": rowData.schemeNo}
        });
    }

    /**
     * 打开编辑解决方案服务界面
     */
    function showApis${category}(id,schemeNo){
        $('<div/>').dialog({
            title:'<i class="fa fa-cog fa-lg fa-fw fa-col"></i> 配置关联api列表',
            href:'${pageContext.request.contextPath}/manage/apidoc/apiDocScheme/settingService.html?id='+id,
            closable : true,
            modal: true,
            width: 1000,
            height: 450,
            onClose:function(){
                $(this).dialog('destroy');
                $.acooly.framework.loadGrid({
                    gridId: "manage_apiDocSchemeService_datagrid${category}",
                    url: '${pageContext.request.contextPath}/manage/apidoc/apiDocScheme/schemeServiceList.html',
                    ajaxData: {"schemeNo": schemeNo}
                });
            }
        });
    }
</script>