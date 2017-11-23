<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_apiPartner_editform" action="/manage/openapi/apiPartner/<#if action == 'create'>save<#else>update</#if>Json.html" method="post">
        <@jodd.form bean="apiPartner" scope="request">
            <input name="id" type="hidden"/>
            <table class="tableForm" width="100%">
                <tr>
                    <th width="25%">接入方编码：</th>
                    <td><input type="text" id="manage_apiPartner_editform_partnerId" name="partnerId" size="48" class="easyui-validatebox text" data-options="required:true"
                               validType="byteLength[1,32]"/>
                        <a onclick="generatePartnerId()" href="#" title="生成新接入方编码"><i class="fa fa-pencil fa-lg fa-fw fa-col"></i></a>
                    </td>
                </tr>
                <tr>
                    <th>接入方名称：</th>
                    <td><input type="text" name="partnerName" size="48" class="easyui-validatebox text" data-options="required:true"
                               validType="byteLength[1,32]"/></td>
                </tr>
                <tr>
                    <th>安全方案：</th>
                    <td><select name="secretType" editable="false" style="height:27px;" panelHeight="auto" class="easyui-combobox"
                                data-options="required:true">
                        <#list allSecretTypes as k,v><option value="${k}">${v}</option></#list>
                    </select>
                    </td>
                </tr>
                <tr>
                    <th>签名类型：</th>
                    <td><select name="signType" editable="false" style="height:27px;" panelHeight="auto" class="easyui-combobox"
                                data-options="required:true">
                        <#list allSignTypes as k,v><option value="${k}">${v}</option></#list>

                    </select>
                    </td>
                </tr>
                <tr>
                    <th>秘钥：</th>
                    <td><input type="text" id="manage_apiPartner_editform_secretKey" name="secretKey" size="48" class="easyui-validatebox text" data-options="required:true"
                               validType="byteLength[1,45]" readonly/>
                        <a onclick="generateSecretKey()" href="#" title="生成新接入方秘钥"><i class="fa fa-pencil fa-lg fa-fw fa-col"></i></a>
                    </td>
                </tr>
                <tr>
                    <th>备注：</th>
                    <td><input type="text" name="comments" size="48" class="easyui-validatebox text" validType="byteLength[1,128]"/></td>
                </tr>
            </table>
        </@jodd.form>
    </form>
</div>
<script>

        /**
         * 生成新的商户号
         */
        function generatePartnerId() {

            $.ajax({
                url: '/manage/openapi/apiPartner/generatePartnerId.html',
                method: 'POST',
                success: function (result) {
                    $('#manage_apiPartner_editform_partnerId').val(result.data.partnerId);
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
                url: '/manage/openapi/apiPartner/generateSecretKey.html',
                method: 'POST',
                success: function (result) {
                    $('#manage_apiPartner_editform_secretKey').val(result.data.secretKey);
                },
                error: function (r, s, e) {
                    $.messager.alert('提示', e);
                }
            });
        }

</script>