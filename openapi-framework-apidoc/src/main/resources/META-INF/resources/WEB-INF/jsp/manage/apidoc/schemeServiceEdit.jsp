<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp" %>
<script type="text/javascript">
    $(function () {
        $.acooly.framework.registerKeydown('manage_apiScheme_searchform', 'manage_apiScheme_datagrid');
    });

    /**
     * 打开权限设置界面
     */
    function manage_apiPartner_showSetting(id) {
        $('#layout_center_tabs').tabs('close', "编辑解决方案服务");
        $('#edit_scheme_id').dialog({
            title: '编辑解决方案服务',
            href: '/manage/apidoc/apiDocScheme/settingService.html?id='+id,
            closable: true,
            modal: true,
            width: 800,
            height: 600,
        });
    }
</script>
<link rel="stylesheet" type="text/css" href="/plugin/bossedit/css/wangEditor.min.css">
<link rel="stylesheet" type="text/css" href="/plugin/bossedit/css/common.css">
<link rel="stylesheet" type="text/css" href="/plugin/bossedit/css/remodal.css">
<link rel="stylesheet" type="text/css" href="/plugin/bossedit/css/remodal-default-theme.css">
<div id="edit_scheme_id"></div>
<div class="easyui-layout" data-options="fit : true,border : false">
    <div class="module fn-clear">
        <div class="module-main" id="schemeBaseInfo">

        </div>
    </div>
</div>
</div>

<script id="schemeInfo-template" type="text/html">
    <div class="user-main mt10">
        <div class="plan-title fn-clear">
            <div class="fn-left title-left">
                <div class="fn-clear">
                    <label>解决方案名称：</label>
                    <div class="plan-text">{{data.name}}</div>
                </div>
                <div class="fn-clear">
                    <label>说明：</label>
                    <div class="plan-text">{{data.note}}</div>
                </div>
            </div>
            <div class="fn-right title-right">
                <div class="fn-clear">

                </div>
                <div class="fn-clear">
                    <div class="plan-text">
                        <a href="javascript:;" id="edit-solution-detail" class="type-btn blue-btn btn"
                           onclick="addAndRemoveEditInit(${id})"><i class="fa fa-plus-circle fa-lg">编辑服务</i></a>
                    </div>
                </div>
            </div>
            <br><br>
            <div class="fn-clear">
                <div class="plan-text"></div>
            </div>
        </div>
        <div class="plan-list fn-clear">
            <ul>
                {{#each data.services}}
                <li><a href="javascript:;">
                    <dd>{{serviceTitle}}</dd>
                    <dd>{{serviceNo}}</dd>
                </a></li>
                {{/each}}
            </ul>
        </div>
    </div>
</script>

<script id="modal-template" type="text/html">
    <div class="layer multi-layer cross_box" style="">
        <form id="xForm" style="width: 600px;">
            <div class="row fn-clear">
                <div class="left-box fn-left">
                    <select name="from[]" id="search" class="select-box form-control" size="8" multiple="multiple">
                        {{#each otherAllServices}}
                        <option value="" data-service-no="{{serviceNo}}" title="{{serviceTitle}}（{{serviceNo}}）">
                            {{serviceTitle}}
                            <br/>
                            {{serviceNo}}
                        </option>
                        {{/each}}
                    </select>

                </div>

                <div class="middle-box fn-left">
                    <button type="button" id="search_rightSelected" class="btn btn-block">&gt;&gt;</button>
                    <button type="button" id="search_leftSelected" class="btn btn-block">&lt;&lt;</button>
                    <button type="button" id="closeModal" class="close-cross-box btn btn-block">返回</button>
                </div>

                <div class="right-box fn-right">
                    <select name="to[]" id="search_to" class="select-box form-control" size="8" multiple="multiple">
                        {{#each schemeServices}}
                        <option value="" data-service-no="{{serviceNo}}" title="{{serviceTitle}}（{{serviceNo}}）">
                            {{serviceTitle}} <br/>{{serviceNo}}
                        </option>
                        {{/each}}
                    </select>
                    </select>
                </div>
            </div>
        </form>
    </div>

</script>
<!--handlebars -->
<script src="/plugin/handlebars-v4.0.5.js"></script>
<script src="/plugin/bossedit/js/crossSelect.js"></script>
<script src="/plugin/bossedit/js/remodal.min.js"></script>
<script src="/plugin/bossedit/js/schemeServiceEdit.js"></script>
<%--<script src="../plugs/bossedit/js/common.js"></script>--%>
<%--<script src="../plugs/bossedit/js/solution_detail.js"></script>--%>
<script>
    schemeInfo(${id});
</script>