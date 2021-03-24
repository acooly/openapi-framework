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


let acooly_openapi_manage = {

    order: acooly_openapi_manage_order
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