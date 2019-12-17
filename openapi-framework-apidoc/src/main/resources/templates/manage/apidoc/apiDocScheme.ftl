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
                console.info("targetRow:" + targetRow.title);
                console.info("sourceRow:" + sourceRow.title);
                console.info("point:" + JSON.stringify(point));
                if (!point || point == null){
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
                            console.info("save move success!");
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
<script src="/plugin/easyui/treegrid-dnd.js"></script>
<div class="easyui-layout" data-options="fit : true,border : false">
    <!-- 列表和工具栏 -->
    <div data-options="region:'center',border:false">
        <table id="manage_apiDocScheme_datagrid${category}" class="easyui-treegrid"
               toolbar="#manage_apiDocScheme_toolbar${category}" fit="true"
               border="false" fitColumns="true" onBeforeDrop="dropCallBack"
               rownumbers="true" idField="id" treeField="title" collapsible="true" checkOnSelect="true" selectOnCheck="true">
            <thead>
            <tr>
                <th field="showCheckboxWithId" checkbox="true" data-options="formatter:function(value, row, index){ return row.id }">编号</th>
                <th field="id" sortable="true" sum="true">id</th>
                <#--                <th field="parentId" sortable="true" sum="true">父级id</th>-->
                <th field="schemeNo">编码</th>
                <th field="title" formatter="contentFormatter">标题</th>
                <th field="category">分类</th>
                <th field="subCount">子节点数量</th>
                <#--                <th field="sortTime" sortable="true" sum="true">排序值</th>-->
                <#--                <th field="path" formatter="contentFormatter">层级路径</th>-->
<#--                <th field="link">链接地址</th>-->
                <th field="statusText">状态</th>
                <#--                <th field="operUsernameCreate">创建人员</th>-->
                <#--                <th field="operUsernameModify">修改人员</th>-->
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
            <a onclick="$.acooly.framework.edit({url:'/manage/apidoc/apiDocScheme/edit.html',id:'{0}',form: 'manage_apiDocScheme_editform${category}', datagrid: 'manage_apiDocScheme_datagrid${category}',width:400,height:280,reload:true});"
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
        <#--            <a onclick="$.acooly.framework.show('/manage/module/security/apiDocScheme/show.html?id={0}',600,500);" href="#" title="查看"><i-->
        <#--                        class="fa fa-file-o fa-lg fa-fw fa-col"></i></a>-->


        <!-- 表格的工具栏 -->
        <div id="manage_apiDocScheme_toolbar${category}">
            <a href="#" class="easyui-linkbutton" plain="true"
               onclick="$.acooly.framework.create({url:'/manage/apidoc/apiDocScheme/create.html?category=${category}',form: 'manage_apiDocScheme_editform${category}', datagrid: 'manage_apiDocScheme_datagrid${category}',width:400,height:280})"><i
                        class="fa fa-plus-circle fa-lg fa-fw fa-col"></i>添加</a>
        </div>
    </div>

</div>
<script type="text/javascript">
    function manage_apiDocScheme_action_formatter${category}(value, row, index){
        let actionHtml = "";
        actionHtml += formatString($('#manage_apiDocScheme_action_create${category}').html(), row.id);
        actionHtml += formatString($('#manage_apiDocScheme_action_edit${category}').html(), row.id);
        if (row.subCount == 0) {
            actionHtml += formatString($('#manage_apiDocScheme_action_doc${category}').html(), row.id, row.schemeNo);
        }
        if (row.status == 'draft' || row.status == 'offShelf') {
            actionHtml += formatString($('#manage_apiDocScheme_action_onShelf${category}').html(), row.id);
        } else if(row.status == 'onShelf'){
            actionHtml += formatString($('#manage_apiDocScheme_action_offShelf${category}').html(), row.id);
        }
        actionHtml += formatString($('#manage_apiDocScheme_action_remove${category}').html(), row.id);
        return actionHtml;
    }
</script>

