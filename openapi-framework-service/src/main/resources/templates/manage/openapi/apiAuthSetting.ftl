<#if ssoEnable>
    <#include "*/include.ftl">
</#if>
<style>
    .openapi-apilist {
        margin-top: 10px;
        font-size: 0;
        height: 485px;
        overflow: scroll;
        -webkit-user-select: none;
    }

    .openapi-apilist li.selected {
        background-color: #01AAED;
        color: #fff;
        font-weight: bold;
    }

    .openapi-apilist li {
        display: inline-block;
        vertical-align: middle;
        *display: inline;
        *zoom: 1;
    }

    .openapi-apilist li {
        width: 23.8%;
        margin: 0 10px 10px 0;
        padding: 0 8px 0 8px;
        border: 1px solid #dfdfdf;
        border-radius: 5px;
        cursor: pointer;
    }

    .openapi-apilist li:hover {
        box-shadow: 1px 1px 5px rgba(0, 0, 0, .1);
    }

    .openapi-api-item {
        position: relative;
        padding: 8px 0px 0 18px;
    }

    .openapi-api-item .ckbox {
        position: absolute;
        left: 0;
        top: 15px;
    }

    .openapi-api-item .text-elip {
        margin: 0 0 5px;
        font-size: 12px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
    }
    .servicelist{
        padding-left: 10px;
    }
</style>
<div id="manage_apiAuth_setting_layout" class="easyui-layout" data-options="fit:true,border:true">
    <div style="padding-left: 10px;padding-right:20px;margin-top:5px;text-align: left;" class="tableForm">
        <div>
            关键字 : <input type="text" class="text" id="keywords" />
            <a href="javascript:void(0);" class="easyui-linkbutton" onclick="searchService()"><i class="fa fa-search fa-lg fa-fw fa-col"></i>查询</a>
            <a href="javascript:void(0);" class="easyui-linkbutton" onclick="showAllService()">显示全部服务</a>
        </div>
    </div>
    <div id="manage_openapi_apilist_container" class="servicelist" style="border-bottom: cornflowerblue;">
    </div>
</div>

<script id="manage_openapi_apilist_template" type="text/html">
    <ul class="openapi-apilist">
        <%
        for(i in data) {
        var e = data[i];
        %>
        <li>
            <div class="openapi-api-item">
                <p class="text-elip" title="<%=e.serviceNo%>"><%=e.busiType%> | <%=e.serviceNo%></p>
                <p class="text-elip" title="<%=e.serviceDesc%>"><%=e.serviceDesc%></p>
                <input type="checkbox" class="ckbox" name="serviceNo" value="<%=e.serviceNo%>" onclick="bindCheckBox(event)"/>
            </div>
        </li>
        <%}%>
    </ul>
</script>

<script>

    var totalService = 0;
    var authService = 0;

    /**
     * 加载全部服务API
     */
    function loadServices() {
        var permissions = "${apiAuth.permissions}";
        $.acooly.loading();
        $.ajax({
            url: '/manage/openapi/apiAuth/getAllService.json',
            success: function (result) {
                if (!result.success) {
                    $.acooly.messager('失败', result.detail, 'danger');
                }
                $('#manage_openapi_apilist_container').html($.acooly.template.render("manage_openapi_apilist_template", result));
                // 注册事件：点击行联动checkbox
                $('.openapi-api-item').click(function () {
                    var checkbox = $(this).children(".ckbox");
                    checkbox.click();
                });
                loadACLs();
            },
            error: function (r, s, e) {
                $.acooly.loaded();
                $.acooly.messager('失败', e, 'danger');
            }
        });
    }

    function bindCheckBox(event) {
        var checkbox = $(event.target);
        var linode = checkbox.parent().parent();
        if (checkbox.is(':checked')) {
            $(linode).addClass("selected");
        } else {
            $(linode).removeClass("selected");
        }
        event.stopPropagation();
    }

    /**
     * 加载权限
     */
    function loadACLs() {
        var authNo = "${apiAuth.authNo}";
        //$.acooly.loading();
        $.ajax({
            url: '/manage/openapi/apiAuth/loadAcls.json',
            data: {authNo: authNo},
            success: function (result) {
                if (!result.success) {
                    $.acooly.messager('失败', result.message, 'danger');
                    return;
                }
                var acls = result.rows;
                $(".openapi-api-item input[name='serviceNo']").each(function () {
                    var that = $(this);
                    var serviceNo = $(that).val();
                    for (var i in acls) {
                        totalService++;
                        if (acls[i].serviceNo == serviceNo) {
                            $(that).click();
                            authService++;
                        }
                    }
                });
            },
            error: function (r, s, e) {
                $.acooly.messager('失败', e, 'danger');

            },
            complete : function () {
                $.acooly.loaded();
            }
        });
    }

    /**
     * 保存ACLs
     */
    function saveAcls(dial) {
        var serviceNoValues = getServiceNoValues();
        if(!serviceNoValues || serviceNoValues ===''){
            $.acooly.messager("提示信息","请至少选择一个服务",'danger');
            return;
        }
        $.acooly.loading("保存中...");
        $.ajax({
            url: '/manage/openapi/apiAuth/settingSave.json',
            data: {
                "authNo": "${apiAuth.authNo}",
                "serviceNo": getServiceNoValues()
            },
            success: function (result) {
                if (result.success) {
                    $.acooly.messager("设置ACL", result.message, result.success?'success':'danger');
                }
            },
            error: function (r, s, e) {
                $.acooly.messager('失败', e, 'danger');

            },
            complete : function () {
                dial.dialog('close');
                $.acooly.loaded();
            }
        });
    }


    function getServiceNoValues() {
        var vals = [];
        $('.openapi-api-item input[name="serviceNo"]:checked').each(function (index, item) {
            vals.push($(this).val());
        });
        return vals.join(",");
    }

    function searchService() {
        var keyval = $("#keywords").val();
        if(keyval === '' ){
            $(".openapi-apilist > li").show();
        }
        $(".openapi-apilist > li").each(function(){
            var tobj = $(this);
            tobj.hide();
            if(tobj.text().indexOf(keyval) > -1){
                tobj.show();
            }
        })
    }

    function showAllService() {
        $("#keywords").val('');
        $(".openapi-apilist > li").show();
    }

    $(function () {
        loadServices();
    });
</script>