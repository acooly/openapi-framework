<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_apiPartner_editform" action="/manage/openapi/apiPartner/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post">
	<@jodd.form bean="apiPartner" scope="request">
        <input name="id" type="hidden"/>
        <table class="tableForm" width="100%">
            <tr>
                <th width="20%">接入方编码：</th>
                <td>
                    <input type="text" name="partnerId" size="48" placeholder="建议默认与商户号一致" class="easyui-validatebox text" data-options="validType:['length[1,32]'],required:true"/>
                </td>
            </tr>
            <tr>
                <th>接入方名称：</th>
                <td><input type="text" name="partnerName" size="48" placeholder="请输入接入方名称..." class="easyui-validatebox text" data-options="validType:['length[1,32]'],required:true"/></td>
            </tr>
            <tr>
                <th>商户编码：</th>
                <td><input type="text" name="merchantNo" size="48" placeholder="所属的商户编码..." class="easyui-validatebox text" data-options="validType:['length[1,64]']"/></td>
            </tr>
            <tr>
                <th>租户编码：</th>
                <td><input type="text" name="tenantNo" size="48" placeholder="所属的租户编码" class="easyui-validatebox text" data-options="validType:['length[1,64]']"/></td>
            </tr>
            <tr>
                <th>备注：</th>
                <td><input type="text" name="comments" size="48" placeholder="请输入备注..." class="easyui-validatebox text" data-options="validType:['length[1,128]']"/></td>
            </tr>
        </table>
    </@jodd.form>
    </form>
</div>
