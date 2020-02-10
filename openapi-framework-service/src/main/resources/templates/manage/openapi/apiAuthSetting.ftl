<#if ssoEnable>
    <#include "*/include.ftl">
</#if>
<style>
    .openapi-apilist {
        display: flex;
        display: -webkit-flex;
        justify-content: space-between;
        flex-direction: row;
        flex-wrap: wrap;
        margin-top: 10px;
    }

    .openapi-apilist .openapi-api {
        width: 25%;
    }

    .openapi-apilist .openapi-api .openapi-api-item {
        cursor: pointer;
        display: flex;
        border: 1px solid #dfdfdf;
        border-radius: 3px;
        padding: 5px;
        margin: 0 0 10px 10px;
    }

    .openapi-apilist .openapi-api .openapi-api-item .left {
        width: 25px;
        line-height: 40px;
    }

    .openapi-apilist .openapi-api .openapi-api-item .right {
        display: flex;
        flex: 1;
        height: 100%;
    }

    .openapi-apilist .openapi-api-empty {
        width: 25%;
        height: 0px;
    }

    .openapi-apilist .openapi-api .selected { background-color: #01AAED; color:#fff; border-color: #1E9FFF; }
</style>
<div id="manage_apiAuth_setting_layout" class="easyui-layout" data-options="fit:true,border:true">
    <div id="manage_openapi_apilist_container" class="servicelist" style="border-bottom: cornflowerblue"></div>
</div>

<script id="manage_openapi_apilist_template" type="text/html">
    <div class="openapi-apilist">
        <%
        for(i in data) {
        var e = data[i];
        %>
        <div class="openapi-api">
            <div class="openapi-api-item">
                <div class="left"><input type="checkbox" name="serviceNo" value="<%=e.serviceNo%>" onclick="alert('111')"></div>
                <div class="right"><%=e.serviceNo%> <br> <%=e.serviceDesc%></div>
            </div>
        </div>
        <%}%>
        <div class="openapi-api-empty"></div>
        <div class="openapi-api-empty"></div>
        <div class="openapi-api-empty"></div>
    </div>
</script>

<script>


    /**
     * 加载全部服务API
     */
    function loadServices() {
        var permissions = "${apiAuth.permissions}";
        $.ajax({
            url: '/manage/openapi/apiAuth/getAllService.json',
            success: function (result) {
                if (!result.success) {
                    $.messager.show({title: '失败', msg: result.detail});
                }
                $('#manage_openapi_apilist_container').html($.acooly.template.render("manage_openapi_apilist_template", result));
                // 注册事件：点击行联动checkbox
                $('.openapi-api-item').click(function () {
                    var checkbox = $(this).children().first().children();
                    if ($(checkbox).attr("checked")) {
                        $(checkbox).prop("checked", false);
                        $(this).removeClass("selected")
                    } else {
                        $(checkbox).prop("checked", true);
                        $(this).addClass("selected")
                    }
                });
                loadACLs();

            },
            error: function (r, s, e) {
                $.messager.show({title: '失败', msg: e});
            }
        });
    }

    /**
     * 加载权限
     */
    function loadACLs() {
        var authNo = "${apiAuth.authNo}";
        $.ajax({
            url: '/manage/openapi/apiAuth/loadAcls.json',
            data: {authNo: authNo},
            success: function (result) {
                if (!result.success) {
                    $.messager.show({title: '失败', msg: result.message});
                    return;
                }
                var acls = result.rows;
                $(".openapi-api-item input[name='serviceNo']").each(function () {
                    var that = $(this);
                    var serviceNo = $(that).val();
                    for (var i in acls) {
                        if (acls[i].serviceNo == serviceNo) {
                            $(that).prop("checked", true)
                            $(that).parent().parent().addClass("selected")
                        }
                    }
                });
            },
            error: function (r, s, e) {
                $.messager.show({title: '失败', msg: e});
            }
        });
    }

    /**
     * 保存ACLs
     */
    function saveAcls() {
        $.ajax({
            url: '/manage/openapi/apiAuth/settingSave.json',
            data: {
                "authNo": "${apiAuth.authNo}",
                "serviceNo": getServiceNoValues()
            },
            success: function (result) {
                if (result.success) {
                    $.messager.show({title: '设置成功', msg: '设置成功'});
                }
            },
            error: function (r, s, e) {
                $.messager.show({title: '失败', msg: e});
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


    $(function () {
        loadServices();

    });


</script>