<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_apiAuth_editform" action="/manage/openapi/apiAuth/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post">
    <@jodd.form bean="apiAuth" scope="request">
        <input name="id" type="hidden"/>
        <table class="tableForm" width="100%">
            <tr>
                <th>接入方ID：</th>
                <td>
                 <#if partnerId??>
                     ${partnerId} <input type="hidden" name="partnerId" value="${partnerId}">
                 <#else>
                     <input type="text" placeholder="接入方编码(PartnerId)..." style="width:300px;" name="partnerId" class="easyui-validatebox" data-options="validType:['length[1,32]'],required:true"/>
                 </#if>
                </td>
            </tr>
            <#if action=='edit'>
            <tr>
                <th>认证编码：</th>
                <td>${apiAuth.authNo}</td>
            </tr>
            </#if>
            <tr>
                <th>安全类型：</th>
                <td>
                    <select name="secretType" editable="false" panelHeight="auto" class="easyui-combobox" data-options="required:true">
						<#list allSecretTypes as k,v>
                            <option value="${k}">${v}</option></#list>
                    </select>
                </td>
            </tr>
            <tr>
                <th>签名类型：</th>
                <td>
                    <select name="signType" editable="false" panelHeight="auto" class="easyui-combobox" data-options="required:true">
						<#list allSignTypes as k,v>
                            <option value="${k}">${v}</option></#list>
                    </select>
                </td>
            </tr>
            <tr>
                <th width="25%">访问帐号：</th>
                <td><input type="text" id="manage_editform_accessKey" style="width:300px;" onclick="generatePartnerId()" name="accessKey" size="48" placeholder="点击生成访问帐号..."
                           class="easyui-validatebox text"
                           data-options="validType:['length[1,45]'],required:true"/>
                    <a onclick="generateAccessKey()" href="#" title="生成新接入方秘钥"><i class="fa fa-pencil fa-lg fa-fw fa-col"></i></a>
                </td>
            </tr>
            <tr>
                <th>访问秘钥：</th>
                <td><input type="text" id="manage_editform_secretKey" style="width:300px;" name="secretKey" size="48" placeholder="点击生成访问秘钥..." class="easyui-validatebox text"
                           data-options="validType:['length[1,45]'],required:true" readonly/>
                    <a onclick="generateSecretKey()" href="#" title="生成新接入方秘钥"><i class="fa fa-pencil fa-lg fa-fw fa-col"></i></a>
                </td>
            </tr>
            <tr>
                <th>访问权限：</th>
                <td><textarea rows="3" cols="40" placeholder="请输入访问权限..." style="width:300px;" name="permissions" class="easyui-validatebox" data-options="validType:['length[1,512]']"></textarea></td>
            </tr>
            <tr>
                <th>备注：</th>
                <td><input type="text" name="comments" size="48" placeholder="请输入备注..." class="easyui-validatebox text" data-options="validType:['length[1,128]']"/></td>
            </tr>
        </table>
    </@jodd.form>
    </form>
</div>
<script>

    /**
     * 生成新的商户号
     */
    function generateAccessKey() {

        $.ajax({
            url: '/manage/openapi/apiAuth/generateAccessKey.json',
            method: 'POST',
            success: function (result) {
                $('#manage_editform_accessKey').val(result.data);
            },
            error: function (r, s, e) {
                $.messager.alert('提示', e);
            }
        });
    }

    /**
     * 生成新秘钥
     */
    function generateSecretKey() {

        $.ajax({
            url: '/manage/openapi/apiAuth/generateSecretKey.json',
            method: 'POST',
            success: function (result) {
                $('#manage_editform_secretKey').val(result.data);
            },
            error: function (r, s, e) {
                $.messager.alert('提示', e);
            }
        });
    }

</script>
