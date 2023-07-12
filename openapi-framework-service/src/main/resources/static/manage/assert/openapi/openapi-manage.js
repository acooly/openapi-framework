/**
 * OpenApi OrderInfo 管理界面 JS
 * @type {{}}
 */
let acooly_openapi_manage_order = {

    init: function () {
        $.acooly.framework.initPage('manage_orderInfo_searchform', 'manage_orderInfo_datagrid');
        this._init_detailview();
    },

    /**
     * 初始化列表的详细试图
     * @private
     */
    _init_detailview: function () {
        $('#manage_orderInfo_datagrid').datagrid({
            view: detailview,
            detailFormatter: function (index, row) {
                return '<div class="ddv" style="padding:10px 5px"></div>';
            },
            onExpandRow: function (index, row) {
                let html = $.acooly.template.render("manage_openapi_orderInfo_list_detail_template", {row: row},);
                let ddv = $(this).datagrid('getRowDetail', index).find('div.ddv');
                ddv.panel({border: false, cache: false, content: html});
                $('#manage_orderInfo_datagrid').datagrid('fixDetailRowHeight', index);
            }
        });
    }

};

let acooly_openapi_manage_auth = {
    clearCache: function (datagridId, id) {
        $.acooly.framework.confirmRequest("/manage/openapi/auth/clearCache.html",
            {id: id}, datagridId, null, "确定要清除该认证对象的缓存吗？"
        );
    }
};

let acooly_openapi_manage = {
    order: acooly_openapi_manage_order,

    auth: acooly_openapi_manage_auth,

    clip: function (btn, target, msg) {
        if (!ClipboardJS.isSupported()) {
            layer.tips("当前浏览器不支持复制", btn, {tips: 3});
            return;
        }
        let clipboard = new ClipboardJS(btn, {
            text: function (trigger) {
                if (!target.startsWith("#") && !target.startsWith(".")) {
                    return target;
                }
                let content = $(target).text();
                if (!content && content == '') {
                    content = $(target).val();
                }
                return content;
            }
        });
        if (!msg) {
            msg = "<B>copied!</B>";
        }
        clipboard.on('success', function (e) {
            e.clearSelection();
            layer.tips(msg, btn, {tips: 3});
        });
    },

    clipAuth: function (datagridId, element) {
        $.acooly.framework.fireSelectRow(datagridId, function (row) {
            var parentName = formatRefrence(datagridId, 'allPartners', row.partnerId);
            let content = "接入方编码：" + row.partnerId
                + "\n接入方名称：" + parentName
                + "\naccessKey：" + row.accessKey
                + "\nsecretKey：" + row.secretKey;
            acooly_openapi_manage.clip(element, content, "复制秘钥信息成功！");
        });
    },

    formatter: {

        password: function (value, row, index) {
            if (!value) {
                return "";
            }
            let idPartRandom = Math.ceil(Math.random() * 9) + 1;
            let id = "manage_clipboard_" + idPartRandom + "_" + row.id;
            let html = "********(" + value.length + ")"
                + " <a href='javascript:;' style='margin-left: 3px;' id='" + id + "' onclick='$.acooly.openapi.manage.clip(\"#" + id + "\",\"" + value + "\",\"复制成功\")'><i class=\"fa fa-clipboard\" aria-hidden=\"true\"></i></a>";
            return html;
        },

    }

};

/***
 * JQuery静态类前缀处理
 */
(function ($) {
    if (!$.acooly) {
        $.acooly = {};
    }
    if (!$.acooly.openapi) {
        $.acooly.openapi = {};
    }
    if (!$.acooly.openapi.manage) {
        $.extend($.acooly.openapi, {manage: acooly_openapi_manage});
    }
})(jQuery);
