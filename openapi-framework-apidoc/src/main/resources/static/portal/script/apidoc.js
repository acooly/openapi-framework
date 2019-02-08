function loadData(id) {
    var url = '/docs/apidoc/apidoc.html';
    var template = 'apidoc_template';
    var renderTo = 'dataCont';
    var jsonData = {id: id};
    baidu.template.ESCAPE = false;
    $.acooly.portal.ajaxRender(url, jsonData, renderTo, template, null, function (result) {
        // 选中菜单
        selectMenu(id);
        $('#apidoc_nav_name').html(result.entity.serviceName);
        // 示例报文
        loadMessageDemos(id);
        // 浮动目录渲染
        initDir(result)
        //设置提示效果
        initTooltip();
    });
}

/**
 * 导航目录
 */
function initDir(result) {
    $.acooly.portal.renderData("apidoc_content_menu", "apidoc_content_menu_template", result);
    $(".doc-content-dir ul li").on("click", function () {
        $(this).addClass("on").siblings().removeClass("on");
    })
}

/**
 * 可选值选项
 */
function initTooltip() {
    $('.fn-tooltip').tooltipster({
        plugins: ['sideTip', 'scrollableTip'],
        theme: 'tooltipster-shadow',
        trigger: 'click',
        interactive: true,
        contentCloning: true,
        contentAsHTML: true,
        zIndex: 99999999999
    });
}

/**
 * 显示子报文
 * @param name
 * @param id
 */
function openSubMessage(name, id) {
    layer.open({
        type: 1,
        title: name,
        area: '979px',
        maxHeight: 600,
        shadeClose: true,
        scrollbar: false,
        content: $('#' + id).html(),
        success: function (layero, index) {
            initTooltip();
        }
    });
}


/**
 * 加载示例报文
 */
function loadMessageDemos(id) {
    var url = '/docs/apidoc/demo/message.html';
    var template = 'apidoc_demo_template';
    var renderTo = 'apidoc_demo_container';
    var jsonData = {id: id};
    baidu.template.ESCAPE = false;
    $.acooly.portal.ajaxRender(url, jsonData, renderTo, template, null, function (result) {
        // layui.use('code', function () {
        //     layui.code({encode: false, about: false});
        // });
        $(result.rows).each(function(i,e){
            $("#apidoc_demo_"+e.messageType).JSONView(e.body);
        });
    });
}


/**
 * 字段格式化：示例
 * @param item
 * @returns {*}
 */
function demoFormatter(item) {
    var demo = item.demo;
    if(!demo || demo == ''){
        return demo;
    }
    if(!isJSON(demo)){
        return demo;
    }
    try {
        var itemData = {};
        itemData.value = $.acooly.format.json(demo);
        itemData.id = item.name + "_" + item.id + "_demo";
        var content = baidu.template("apidoc_demo_json_template", itemData);
        return content;
    } catch (e) {
        return demo;
    }
}

function isJSON(str) {
    if (typeof str == 'string') {
        try {
            var obj=JSON.parse(str);
            if(str.indexOf('{') == 0 || str.indexOf('[') == 0){
                return true;
            }else{
                return false;
            }
        } catch(e) {
            return false;
        }
    }
    return false;
}

/**
 * 字段格式化：数据类型
 * @param item
 */
function dataTypeFormatter(item) {
    var dataType = item.dataType;
    var min = item.min;
    var max = item.max;
    if((min == null || min == '') && (min == null || min == '')){
        return dataType;
    }

    if(min == max){
        return dataType + "(" + min + ")";
    }

    if((min == null || min == '') && max != null && max != ''){
        return dataType + "(" + max + ")";
    }

    if((max == null || max == '') && min != null && min != ''){
        return dataType + "(" + min + "-?)";
    }


    return dataType + "(" + min + '-' + max + ")";
}

/**
 * 字段格式化：是否加密
 * @param item
 * @returns {string}
 */
function securityFormatter(item) {
    return item.encryptstatus == 'no' ? '否' : '是';
}

/**
 * 字段格式化：备注
 * @param item
 * @returns {*}
 */
function descnFormatter(item) {
    var descn = item.descn;
    try {
        var itemData = JSON.parse(descn);
        if(itemData.context == null || itemData.context.length == 0){
            return itemData.content;
        }
        itemData.id = item.name + item.id;
        var content = baidu.template("apidoc_options_template", itemData);
        return content;
    } catch (e) {
        // 非json
        return descn;
    }
}
