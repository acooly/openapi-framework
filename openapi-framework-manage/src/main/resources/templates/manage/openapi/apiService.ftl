<#if ssoEnable>
    <#include "*/include.ftl">
</#if>
<script type="text/javascript">

    function manage_apiServiceType_loadTree(defaultNode, clear) {
        $.ajax({
            url : '/manage/openapi/apiServiceType/loadTree.html',
            success : function(data, status) {
                if (typeof (data) == 'string') {
                    data = eval('(' + data + ')');
                }
                if (!data.success) {
                    return;
                }
                $.fn.zTree.init($("#manage_apiServiceType_tree"), {
                    view : {
                        showLine : true,
                        showIcon : true,
                        showTitle : true,
                        addHoverDom : manage_apiServiceType_tree_addHoverDom,
                        removeHoverDom : manage_apiServiceType_tree_removeHoverDom,
                    },
                    edit : {
                        enable : true,
                        showRemoveBtn : false,
                        showRenameBtn : false
                    },
                    callback : {
                        onClick : function(event, treeId, treeNode, clickFlag) {
                            manage_apiService_search(treeNode)
                        },
                        onDrop : function(event, treeId, treeNodes, targetNode, moveType) {
                            manage_apiServiceType_form_moveup(treeNodes[0], targetNode, moveType);
                        }
                    }
                }, data.rows);
                var zTree = $.fn.zTree.getZTreeObj("manage_apiServiceType_tree");
                zTree.expandAll(true);
                if (defaultNode)
                    zTree.selectNode(defaultNode, true);
                if (clear)
                    manage_apiServiceType_form_add();
            }
        });
    }

    function manage_apiService_search(apiServiceType) {
        $('#manage_apiService_typePath').val(apiServiceType.path);
        $('#manage_apiService_typeId').val(apiServiceType.id);
        $('#manage_apiService_typeName').val(apiServiceType.name);
        $.acooly.framework.search('manage_apiService_searchform', 'manage_apiService_datagrid');
    }

    /**
     * 鼠标移动到节点动态添加添加和删除链接。
     */
    function manage_apiServiceType_tree_addHoverDom(treeId, treeNode) {
        var aObj = $("#" + treeNode.tId + "_a");
        // 添加子
        if ($("#manage_apiServiceType_add_Btn_" + treeNode.id).length == 0) {
            var html = "<a id='manage_apiServiceType_add_Btn_" + treeNode.id + "' href='javascript:;' onclick='manage_apiServiceType_tree_add(" + treeNode.id + ");return false;' style='margin:0 0 0 5px;'>添加</a>";
            aObj.append(html)
        }
        // 编辑
        if ($("#manage_apiServiceType_edit_Btn_" + treeNode.id).length == 0) {
            var html = "<a id='manage_apiServiceType_edit_Btn_" + treeNode.id + "' href='javascript:;' onclick='manage_apiServiceType_tree_edit(" + treeNode.id + ");return false;' style='margin:0 0 0 5px;'>修改</a>";
            aObj.append(html)
        }
        // 删除
        if (treeNode.children && treeNode.children.length == 0) {
            if ($("#manage_apiServiceType_delete_Btn_" + treeNode.id).length > 0)
                return;
            var html = "<a id='manage_apiServiceType_delete_Btn_" + treeNode.id + "' href='javascript:;' onclick='manage_apiServiceType_tree_delete(" + treeNode.id + ");return false;' style='margin:0 0 0 5px;'>删除</a>";
            aObj.append(html)
        }
    }
    /**
     * 鼠标移出节点处理
     */
    function manage_apiServiceType_tree_removeHoverDom(treeId, treeNode) {
        if ($("#manage_apiServiceType_add_Btn_" + treeNode.id).length > 0)
            $("#manage_apiServiceType_add_Btn_" + treeNode.id).unbind().remove();
        if ($("#manage_apiServiceType_edit_Btn_" + treeNode.id).length > 0)
            $("#manage_apiServiceType_edit_Btn_" + treeNode.id).unbind().remove();
        if (treeNode.children.length == 0)
            $("#manage_apiServiceType_delete_Btn_" + treeNode.id).unbind().remove();
    }

    // 添加子节点
    function manage_apiServiceType_tree_add(parentId) {
        var url = '/manage/openapi/apiServiceType/create.html';
        if (parentId) {
            url += '?parentId=' + parentId;
        }
        $.acooly.framework.create({
            url : url,
            entity : 'apiServiceType',
            width : 500,
            height : 200,
            onSuccess : function(result) {
                var zTree = $.fn.zTree.getZTreeObj("manage_apiServiceType_tree");
                if (parentId) {
                    var parentNode = zTree.getNodeByParam("id", parentId, null);
                    zTree.addNodes(parentNode, result.entity, false);
                } else {
                    zTree.addNodes(null, result.entity, false);
                }
            }
        })
    }

    // 修改节点
    function manage_apiServiceType_tree_edit(id) {
        $.acooly.framework.edit({
            url : '/manage/openapi/apiServiceType/edit.html',
            id : id,
            entity : 'apiServiceType',
            width : 500,
            height : 200,
            reload : false,
            onSuccess : function(d, result) {
                var zTree = $.fn.zTree.getZTreeObj("manage_apiServiceType_tree");
                var node = zTree.getNodeByParam("id", id, null);
                node.name = result.entity.name;
                zTree.updateNode(node, true);
            }
        })
    }

    //删除子节点
    function manage_apiServiceType_tree_delete(id) {
        $.messager.confirm("确认", "你确认删除该类型？	", function(r) {
            if (!r)
                return;
            $.ajax({
                url : '/manage/openapi/apiServiceType/deleteJson.html?id=' + id,
                success : function(data, status) {
                    if (data.success) {
                        var zTree = $.fn.zTree.getZTreeObj("manage_apiServiceType_tree");
                        var node = zTree.getNodeByParam("id", id, null);
                        zTree.removeNode(node);
                    }
                    if (data.message)
                        $.messager.show({
                            title : '提示',
                            msg : data.message
                        });
                }
            });
        })
    }

    //移动
    function manage_apiServiceType_form_moveup(sourceNode, targetNode, moveType) {
        if (!moveType || moveType == null)
            return;
        console.info("sourceNode:" + sourceNode.name + ", targetNode:" + targetNode.name + ",moveType:" + moveType);
        $.ajax({
            url : '/manage/openapi/apiServiceType/move.html',
            data : {
                sourceId : sourceNode.id,
                targetId : targetNode.id,
                moveType : moveType
            },
            success : function(data, status) {
                if (data.success) {
                    console.info("save move success!");
                }
            }
        });
    }

    //展开/收缩
    var manage_apiServiceType_tree_expand_state = true;
    function manage_resource_tree_toggle() {
        var zTree = $.fn.zTree.getZTreeObj("manage_apiServiceType_tree");
        zTree.expandAll(!manage_apiServiceType_tree_expand_state);
        manage_apiServiceType_tree_expand_state = !manage_apiServiceType_tree_expand_state;
    }

    function manage_apiService_create() {
        var typeId = $('#manage_apiService_typeId').val();
        if (!typeId) {
            alert("请先选择分类");
            return;
        }
        $.acooly.framework.create({
            url : '/manage/openapi/apiService/create.html?typeId=' + typeId,
            entity : 'apiService',
            width : 500,
            height : 400,
            maximizable : false
        });
    }



$(function() {
    manage_apiServiceType_loadTree();
	$.acooly.framework.registerKeydown('manage_apiService_searchform','manage_apiService_datagrid');
});
</script>
<div class="easyui-layout" data-options="fit : true,border : false">

    <!-- 菜单树 -->
    <div data-options="region:'west',border:true,split:true" style="width: 200px;" align="left">
        <div class="easyui-panel" style="padding: 5px;">
            <a href="#" class="easyui-linkbutton" data-options="plain:true" onclick="manage_resource_tree_toggle()">[+/-]</a> <a href="#" class="easyui-linkbutton" data-options="plain:true" onclick="manage_apiServiceType_tree_add()">添加根</a>
        </div>
        <div id="manage_apiServiceType_tree" class="ztree"></div>
    </div>

    <!-- 列表和工具栏 -->
    <div data-options="region:'center',border:true,split:true">

        <div class="easyui-layout" data-options="fit : true,border : false">

            <div data-options="region:'north',border:false" style="padding:2px 5px; overflow: hidden;">
                <!-- 查询条件 -->
                <form id="manage_apiService_searchform" onsubmit="return false">
                    <table class="tableForm" width="100%">
                        <tr>
                            <td style="border-bottom: 0px;">
                                <div>
                                    分类: <input id="manage_apiService_typeName" disabled type="text" class="text" size="15" name="manage_apiService_typeName" value="根分类"/>
                                    服务名: <input type="text" class="text" size="15" name="search_LIKE_name"/>
                                    中文名: <input type="text" class="text" size="15" name="search_LIKE_title"/>
                                    <input id="manage_apiService_typePath" name="search_RLIKE_apiServiceType.path" type="hidden">
                                    <input id="manage_apiService_typeId" name="apiServiceTypeId" type="hidden">
                                    <a href="javascript:void(0);" class="easyui-linkbutton" style="margin-top: -2px;" data-options="plain:false" onclick="$.acooly.framework.search('manage_apiService_searchform','manage_apiService_datagrid');"><i class="fa fa-search fa-lg fa-fw fa-col"></i>查询</a>
                                </div>
                            </td>
                        </tr>
                    </table>
                </form>
            </div>

            <div data-options="region:'center',border:false">
                <table id="manage_apiService_datagrid" class="easyui-datagrid" url="/manage/openapi/apiService/listJson.html" toolbar="#manage_apiService_toolbar" fit="true" border="false" fitColumns="false"
                       pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true"
                       selectOnCheck="true" singleSelect="true" nowrap="false">
                    <thead>
                    <tr>
                        <th field="showCheckboxWithId" checkbox="true" data-options="formatter:function(value, row, index){ return row.id }">编号</th>
                        <th field="id">id</th>
                        <th field="code">服务编码</th>
                        <th field="title">中文描述</th>
                        <th field="name">服务名</th>
                        <th field="version">服务版本</th>
                        <th field="createTime" formatter="dateTimeFormatter">创建时间</th>
                        <th field="updateTime" formatter="dateTimeFormatter">更新时间</th>
                        <th field="comments" formatter="contentFormatter">备注</th>
                        <th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_apiService_action',value,row)}">动作</th>
                    </tr>
                    </thead>
                </table>
                <!-- 每行的Action动作模板 -->
                <div id="manage_apiService_action" style="display: none;">
                    <a onclick="$.acooly.framework.edit({url:'/manage/openapi/apiService/edit.html',id:'{0}',entity:'apiService',width:500,height:400});" href="#" title="编辑"><i class="fa fa-pencil fa-lg fa-fw fa-col"></i></a>
                    <a onclick="$.acooly.framework.remove('/manage/openapi/apiService/deleteJson.html','{0}','manage_apiService_datagrid');" href="#" title="删除"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i></a>
                </div>
                <!-- 表格的工具栏 -->
                <div id="manage_apiService_toolbar">
                    <a href="#" title="添加新的服务" class="easyui-linkbutton easyui-tooltip" plain="true" onclick="manage_apiService_create()"><i class="fa fa-plus-circle fa-lg fa-fw fa-col"></i>添加</a>
                </div>
            </div>
        </div>

    </div>

</div>
