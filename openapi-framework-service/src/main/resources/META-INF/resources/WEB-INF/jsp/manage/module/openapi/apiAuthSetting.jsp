<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp" %>
<c:if test="${initParam['ssoEnable']=='true'}">
    <%@ include file="/WEB-INF/jsp/manage/common/ssoInclude.jsp" %>
</c:if>
<style>


</style>
<!-- 布局 -->
<div id="manage_apiAuth_setting_layout" class="easyui-layout" data-options="fit:true,border:true">
    <!-- 可选服务视图 -->
    <div data-options="region:'west',split:false,border:true,collapsible:false" style="width:405px;padding:5px">
        <div>
            <h3>租户列表</h3>
            <table>
                <thead>
                <tr>
                    <th field="partnerId">租户名</th>
                    <th field="partnerName">租户描述</th>
                    <th field="se">选择</th>
                </tr>
                </thead>
                <tbody id="manage_apiAuth_setting_layout_apiTenant">
                <tr>
                    <td>*</td>
                    <td>所有租户</td>
                    <td><input type="checkbox" name="partnerId" value="*"/></td>
                </tr>
                </tbody>
            </table>
            </ul>
        </div>
        <div class="servicelist" style="border-bottom: cornflowerblue">
            <h3>服务列表:</h3>
            <table>
                <thead>
                <tr>
                    <th field="serviceName">服务名</th>
                    <th field="serviceDesc">描述</th>
                    <th field="se">选择</th>
                </tr>
                </thead>
                <tbody id="manage_apiAuth_setting_layout_services">
                <tr>
                    <td>*</td>
                    <td>所有服务</td>
                    <td><input type="checkbox" name="service" value="*"/></td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <div data-options="region:'center',split:false,border:true,collapsible:false">
        <div>
            <a href="javascript:void(0)" class="easyui-linkbutton"
               onclick="genPerm()">
                <i class="">生成权限字符串</i>
            </a>

        </div>
        <textarea id="permStr" rows="20" cols="80"></textarea>
        <div>
            <a href="javascript:void(0)" class="easyui-linkbutton"
               onclick="setPerm()">
                <i class="">设置此权限字符串</i>
            </a>

        </div>
    </div>

</div>
<script>

    function setPerm() {
        $.ajax({
            url: '/manage/module/openapi/apiAuth/setPerm.json',
            data: {
                "accessKey":"${apiAuth.accessKey}",
                "perm": $('#permStr').val()
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

    $.ajax({
        url: '/manage/module/openapi/apiTenant/listJson.json',
        success: function (result) {
            if (result.success) {
                var html = '';
                $.each(result.rows, function (key, value) {
                    html += ' <tr>' +
                        '                    <td>' + value.partnerId + '</td>' +
                        '                    <td>' + value.partnerName + '</td>' +
                        '                    <td><input type="checkbox" name="partnerId" value="' + value.partnerId + '"/> </td>' +
                        '                </tr>';
                });
                $('#manage_apiAuth_setting_layout_apiTenant').append(html);
            }
        },
        error: function (r, s, e) {
            $.messager.show({title: '失败', msg: e});
        }
    });
    $.ajax({
        url: '/manage/module/openapi/apiAuth/getAllService.json',
        success: function (result) {
            if (result.success) {
                var html = '';
                $.each(result.data, function (key, value) {
                    html += ' <tr>' +
                        '                    <td>' + value.serviceName + '</td>' +
                        '                    <td>' + value.serviceDesc + '</td>' +
                        '                    <td><input type="checkbox" name="service" value="' + value.serviceName + '"/> </td>' +
                        '                </tr>';
                });
                $('#manage_apiAuth_setting_layout_services').append(html);
            }
        },
        error: function (r, s, e) {
            $.messager.show({title: '失败', msg: e});
        }
    });

    function genPerm() {
        var partnerIds = getCheckBoxValue("partnerId");
        var services = getCheckBoxValue("service");

        var permString = '';
        for (var i = 0; i < partnerIds.length; i++) {
            var p = partnerIds[i];
            for (var j = 0; j < services.length; j++) {
                var service = services[j];
                permString += p + ":" + service + ',';
            }
        }
        permString = permString.substr(0, permString.length - 1);
        $('#permStr').val(permString);

    }

    function getCheckBoxValue(name) {
        var vals = [];
        var conc = false;
        $('input[name="' + name + '"]:checked').each(function (index, item) {
            var value = $(this).val();
            if (value === '*') {
                conc = true
            }
            vals.push($(this).val());
        });
        if (conc) {
            vals = [];
            vals.push('*');
        }
        return vals;
    }

    $(function () {


    });


</script>