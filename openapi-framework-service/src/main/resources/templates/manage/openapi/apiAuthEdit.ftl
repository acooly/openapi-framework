<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_apiAuth_editform" class="form-horizontal" action="/manage/openapi/apiAuth/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post">
        <@jodd.form bean="apiAuth" scope="request">
            <input name="id" type="hidden"/>
            <div class="card-body">
                <div class="form-group row">
                    <label class="col-sm-2 col-form-label">接入方</label>
                    <div class="col-sm-10">
                        <#if partnerId??>
                            ${partnerId} <input type="hidden" name="partnerId" value="${partnerId}">
                        <#else>
                            <select name="partnerId" class="form-control select2bs4">
                                <#list allPartners as k,v>
                                    <option value="${k}">${k}:${v}</option></#list>
                            </select>
                        </#if>
                    </div>
                </div>

                <div class="form-group row">
                    <label class="col-sm-2 col-form-label">安全类型</label>
                    <div class="col-sm-4">
                        <select name="secretType" class="form-control select2bs4">
                            <#list allSecretTypes as k,v>
                                <option value="${k}">${v}</option></#list>
                        </select>
                    </div>
                    <label class="col-sm-2 col-form-label">签名类型</label>
                    <div class="col-sm-4">
                        <select name="signType" class="form-control select2bs4">
                            <#list allSignTypes as k,v>
                                <option value="${k}">${v}</option></#list>
                        </select>
                    </div>
                </div>

                <div class="form-group row">
                    <label class="col-sm-2 col-form-label">访问帐号</label>
                    <#if action='create'>
                        <div class="col-sm-10">
                            <div class="input-group">
                                <input type="text" id="manage_editform_accessKey" name="accessKey" placeholder="点击生成访问帐号..." class="easyui-validatebox form-control" data-options="validType:['length[1,45]']" required="true"/>
                                <div class="input-group-append">
                                    <a href="javascript:;" title="生成新的访问账号AccessKey" onclick="generateAccessKey()" class="input-group-text easyui-tooltip"><i class="fa fa-refresh"></i></a>
                                </div>
                            </div>
                        </div>
                    <#else>
                        <div class="col-sm-10 col-form-content">${apiAuth.accessKey}</div>
                    </#if>
                </div>
                <div class="form-group row">
                    <label class="col-sm-2 col-form-label">访问秘钥</label>
                    <div class="col-sm-10">
                        <div class="input-group">
                            <input type="text" id="manage_editform_secretKey" name="secretKey" placeholder="点击生成访问秘钥..." class="easyui-validatebox form-control" data-options="validType:['length[1,45]']" required="true"/>
                            <div class="input-group-append">
                                <a href="javascript:;" title="点击生成新的安全码SecretKey" onclick="generateSecretKey()" class="input-group-text easyui-tooltip"><i class="fa fa-refresh"></i></a>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-2 col-form-label"><a title="注意: 通过acooly.openapi.permi.script-enable参数控制开启，默认false，特别强调下个版本(2021-6-30前)，online环境强制关闭" class="easyui-tooltip">访问权限 <i class="fa fa-info-circle" aria-hidden="true"></i></a></label>
                    <div class="col-sm-10">
                        <textarea rows="2" cols="40" placeholder="格式：accessKey:service,多个逗号分隔，支持通配符:'*'.例如：test:order*,test:queryInfo,app*:login" name="permissions" class="easyui-validatebox  form-control" data-options="validType:['perms','length[1,512]']"></textarea>
                    </div>
                </div>
                <div class="form-group row">
                    <label title="是否开启IP白名单，IP白名单" class="col-sm-2 col-form-label easyui-tooltip">启用白名单</label>
                    <div class="col-sm-10">
                        <select name="whitelistCheck" id="manage_apiAuth_whitelistCheck" class="form-control select2bs4">
                            <#list allWhitelistChecks as k,v>
                                <option value="${k}">${v}</option></#list>
                        </select>
                    </div>
                </div>
                <div class="form-group row" id="manage_auth_whitelist_row">
                    <label class="col-sm-2 col-form-label">IP白名单</label>
                    <div class="col-sm-10">
                        <textarea rows="2" cols="40" placeholder="请输入访问权限,格式：xxx.xxx.xxx.xxx, yyy.yyy.yyy.yyy 多个逗号分隔." name="whitelist" class="easyui-validatebox  form-control" data-options="validType:['ips','length[1,127]']"></textarea>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-2 col-form-label">
                        <a data-toggle="tooltip" data-placement="right" title="状态修改为非'正常'时，该认证对象不可用">状态
                            <i class="fa fa-info-circle" aria-hidden="true"></i></a>
                    </label>
                    <div class="col-sm-10">
                        <select name="status" class="form-control select2bs4">
                            <#list allStatuss as k,v>
                                <option value="${k}">${v}</option></#list>
                        </select>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-2 col-form-label">备注</label>
                    <div class="col-sm-10">
                        <input type="text" name="comments" placeholder="请输入备注..." class="easyui-validatebox form-control" data-options="validType:['length[1,128]']"/>
                    </div>
                </div>
            </div>
        </@jodd.form>
    </form>
</div>
<script>

    $.extend($.fn.validatebox.defaults.rules, {
        ips: {
            validator: function (value, param) {
                var regex = /^(((2(5[0-5]|[0-4]\d))|[0-1]?\d{1,2})(\.((2(5[0-5]|[0-4]\d))|[0-1]?\d{1,2})){3})+(,\s*(((2(5[0-5]|[0-4]\d))|[0-1]?\d{1,2})(\.((2(5[0-5]|[0-4]\d))|[0-1]?\d{1,2})){3})+)*$/;
                return regex.test(value);
            },
            message: '单个或多个IP（逗号分隔）'
        },
        perms: {
            validator: function (value, param) {
                var regex = /^([0-9a-zA-Z_\*]+:[0-9a-zA-Z_\*]+)+(,([0-9a-zA-Z_\*]*:[0-9a-zA-Z_\*]*)+)*$/;
                return regex.test(value);
            },
            message: '权限定义格式错误'
        }
    });

    /**
     * 生成新的商户号
     */
    function generateAccessKey() {

        $.ajax({
            url: '/manage/openapi/apiAuth/generateAccessKey.json',
            method: 'POST',
            success: function (result) {
                $('#manage_editform_accessKey').val(result.data);
                $('#manage_editform_accessKey').validatebox("validate")
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
                $('#manage_editform_secretKey').validatebox("validate")
            },
            error: function (r, s, e) {
                $.messager.alert('提示', e);
            }
        });
    }

    function toggle_manage_apiAuth_whitelist(val) {
        if (val == 'yes') {
            $('#manage_auth_whitelist_row').show();
        } else {
            $('#manage_auth_whitelist_row').hide();
        }
    }

    $(function () {
        $('#manage_apiAuth_whitelistCheck').on("select2:select", function (e) {
            toggle_manage_apiAuth_whitelist(e.params.data.id);
        });

        toggle_manage_apiAuth_whitelist($('#manage_apiAuth_whitelistCheck').val());
    });
</script>
