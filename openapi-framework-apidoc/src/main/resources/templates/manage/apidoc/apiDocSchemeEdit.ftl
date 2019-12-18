<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_apiDocScheme_editform${category}" action="/manage/apidoc/apiDocScheme/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post">
        <@jodd.form bean="apiDocScheme" scope="request">
            <input name="id" type="hidden" />
            <input name="category" type="hidden" value="${category}"/>
            <input name="schemeType" type="hidden" value="custom" />
            <table class="tableForm" width="100%">
                <tr>
                    <th>父节点：</th>
                    <td>
                        <#if action == 'create'>
                            <#if parent??>${parent.title}<#else>顶级节点</#if>
                            <input type="hidden" name="parentId" value="<#if parent??>${parent.id}</#if>"/>
                        <#else>
                            <#if parent??>${parent.title}<#else>顶级节点</#if><input type="hidden" name="parentId"/>
                        </#if>
                    </td>
                </tr>
                <tr>
                    <th>编码：</th>
                    <td>
                        <#if action == 'create'>
                            <input type="text" name="schemeNo" size="40" class="easyui-validatebox text" validType="byteLength[1,32]"/>
                        <#else>
                            ${apiDocScheme.schemeNo}
                        </#if>
                    </td>
                </tr>
                <tr>
                    <th>标题：</th>
                    <td><input type="text" name="title" size="40" class="easyui-validatebox text" data-options="required:true" validType="byteLength[1,32]"/></td>
                </tr>
<#--                <tr>-->
<#--                    <th>链接地址：</th>-->
<#--                    <td>-->
<#--                        <textarea name="link" style="width: 300px; height: 60px;"></textarea>-->
<#--                    </td>-->
<#--                </tr>-->
            </table>
        </@jodd.form>
    </form>
</div>

