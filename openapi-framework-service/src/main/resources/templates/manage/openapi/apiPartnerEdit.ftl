<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_apiPartner_editform" class="form-horizontal" action="/manage/openapi/apiPartner/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post">
        <@jodd.form bean="apiPartner" scope="request">
            <input name="id" type="hidden"/>
            <div class="card-body">
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">接入方编码</label>
                    <div class="col-sm-9">
                        <input type="text" name="partnerId" placeholder="建议默认与商户号一致" class="easyui-validatebox form-control" data-options="validType:['length[1,32]']" required="true"/>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">接入方名称</label>
                    <div class="col-sm-9">
                        <input type="text" name="partnerName" placeholder="请输入接入方名称..." class="easyui-validatebox form-control" data-options="validType:['length[4,32]']" required="true"/>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">所属租户 <a href="javascript:;" data-toggle="tooltip" data-placement="right" title="(可选)，绑定对应的租户，可以在OpenApi服务内，通过tenantId()方法获取并传入下层，以判断当前请求对应的租户身份"><i class="fa fa-info-circle" aria-hidden="true"></i></a></label>
                    <div class="col-sm-9">
                        <select name="tenantId" id="manage_apiPartner_editform_tenantId" class="form-control input-sm select2bs4" data-placeholder="[可选] 绑定所属租户编码">
                            <#list tenants as t>
                                <option value="${t.tenantId}">${t.tenantName}</option></#list>
                        </select>
                        <input name="tenantName" id="manage_apiPartner_editform_tenantName" type="hidden"/>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">商户编码</label>
                    <div class="col-sm-9">
                        <input type="text" name="merchantNo" placeholder="所属的商户编码..." class="easyui-validatebox form-control" data-options="validType:['length[1,32]']" required="true"/>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">备注</label>
                    <div class="col-sm-9">
                        <input type="text" name="comments" placeholder="请输入备注..." class="easyui-validatebox form-control" data-options="validType:['length[1,128]']"/>
                    </div>
                </div>
            </div>
        </@jodd.form>
    </form>
    <script>
        $(function (){
            // 注册onchange事件
            $('#manage_apiPartner_editform_tenantId').on("select2:select", function (e) {
                $('#manage_apiPartner_editform_tenantName').val(e.params.data.text);
            });
        });
    </script>
</div>
