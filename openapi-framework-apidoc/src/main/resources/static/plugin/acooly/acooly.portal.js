/**
 *
 * acooly portal js for gfintech
 *
 * @author zhangpu
 * @date 2017-07-09
 * @type {{trimToEmpty: acooly_portal.trimToEmpty, ajax: acooly_portal.ajax, ajaxRender: acooly_portal.ajaxRender, renderData: acooly_portal.renderData}}
 */

var acooly_portal = {
    trimToEmpty: function (text) {
        if (text == null) return '';
        return text;
    },

    ajax: function (url, jsonData, onSuccess, onError) {
        $.ajax({
            url: url,
            method: 'post',
            data: jsonData,
            beforeSend: function () {
                $.acooly.layui.loading();
            },
            complete: function () {
                $.acooly.layui.loaded();
            },
            success: function (result) {
                if (onSuccess) {
                    onSuccess.call(this, result);
                }
            },
            error: function (e) {
                if (onError) {
                    onError.call(this, e);
                } else {
                    console.info(e);
                    $.acooly.layui.msg("通讯异常:" + e, 2);
                }

            }
        })
    },

    ajaxRender: function (url, jsonData, container, template,beforeRender,afterReader) {
        var This = this;
        $.ajax({
            url: url,
            method: 'post',
            data: jsonData,
            beforeSend: function () {
                $.acooly.layui.loading();
            },
            complete: function () {
                $.acooly.layui.loaded();
            },
            success: function (result) {
                if (result.success) {
                    if(beforeRender){
                        beforeRender.call(this, result);
                    }
                    This.renderData(container, template, result);
                    if(afterReader){
                        afterReader.call(this, result);
                    }
                } else {
                    layer.msg(result.message);
                }
            },
            error: function (e) {
                layer.msg('数据加载失败:' + e);
            }
        })
    },

    renderData: function (container, template, result) {
        baidu.template.LEFT_DELIMITER = '<%';
        baidu.template.RIGHT_DELIMITER = '%>';
        var html = baidu.template(template, result);
        $('#' + container).html(html);
        return result.data.totalPage;
    }

};


/**
 * 密码相关
 * @type {{}}
 */
var acooly_password = {

    options: {
        higRegex: "^(?=.{8,})(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*\\W).*$",
        midRegex: "^(?=.{8,})(((?=.*[A-Z])(?=.*[a-z]))|((?=.*[A-Z])(?=.*[0-9]))|((?=.*[a-z])(?=.*[0-9]))).*$",
        lowRegex: "(?=.{8,}).*"
    },

    /**
     * 验证码密码等级
     * @param password 密码
     * @returns {number} 等级（0：不合格,1:低，2:中，3:高）
     */
    verify: function (password) {
        if (new RegExp(this.options.higRegex, "g").test(password)) {
            return 3;
        } else if (new RegExp(this.options.midRegex, "g").test(password)) {
            return 2;
        } else if (new RegExp(this.options.lowRegex, "g").test(password)) {
            return 1;
        } else {
            return 0;
        }
    }
};


var acooly_countdown = {
    // 默认倒计时60s
    count: 60,

    disabledClass: '',

    /**
     * 启动
     * @param count
     * @param buttonId
     */
    start: function (buttonId, count, disabledClass) {
        if (count) {
            this.count = count;
        }
        if (disabledClass) {
            this.disabledClass = disabledClass;
        }
        this.doCount(buttonId);
    },

    /**
     * 停止
     */
    stop: function () {
        this.count = 0;
    },

    doCount: function (buttonId) {
        var i = --this.count;
        if (i < 0) {
            $("#" + buttonId).prop('disabled', false).removeClass(this.disabledClass);
            $("#" + buttonId).text("重新下发短信");
        } else {
            $("#" + buttonId).prop('disabled', true).addClass(this.disabledClass);
            $("#" + buttonId).text("倒计时" + i + "秒");
            setTimeout("$.acooly.countdown.doCount('" + buttonId + "')", 1000)
        }
    },
};


/**
 * acooly 对 layui的扩展和封装使用
 * @type {{loading: acooly_layui.loading, loaded: acooly_layui.loaded, msg: acooly_layui.msg}}
 */
var acooly_layui = {

    loadingId: '',
    /**
     * 打开加载load效果
     * @param msg
     * @returns {string} loadingId
     */
    loading: function (msg) {
        if (!msg)
            msg = '加载中…';
        this.loadingId = layer.msg(msg, {icon: 16, shade: 0.01});
        return this.loadingId;
    },

    /**
     * 关闭加载load效果
     * @param id
     */
    loaded: function (id) {
        if (id) {
            layer.close(id);
            return;
        }
        if (this.loadingId) {
            layer.close(this.loadingId);
            return;
        }
        layer.closeAll();
    },

    msg: function (message, icon) {
        if (!icon) {
            icon = 0;
        }
        layer.msg(message, {icon: icon, closeBtn: 1});
    },

    tips: function (content, objectId, position) {
        if (!position) {
            position = 2;
        }
        // if (objectId.indexOf('#') != 0) {
        //     objectId = '#' + objectId;
        // }
        layer.tips(content, objectId, {
            tips: position,
            style: ['background-color:#81af43; color:#fff', '#81af43'],
            maxWidth: 200,
            closeBtn: [1, true] //显示关闭按钮
        });
    },
};


(function ($) {
    if (!$.acooly) {
        $.extend({acooly: {}});
    }

    $.extend($.acooly, {
        portal: acooly_portal
    });

    $.extend($.acooly, {
        password: acooly_password
    });

    $.extend($.acooly, {
        countdown: acooly_countdown
    });

    $.extend($.acooly, {
        layui: acooly_layui
    });


    $.fn.serializeJson = function () {
        var serializeObj = {};
        var array = this.serializeArray();
        var str = this.serialize();
        $(array).each(function () {
            if (serializeObj[this.name]) {
                if ($.isArray(serializeObj[this.name])) {
                    serializeObj[this.name].push(this.value);
                } else {
                    serializeObj[this.name] = [serializeObj[this.name], this.value];
                }
            } else {
                serializeObj[this.name] = this.value;
            }
        });
        return serializeObj;
    };

})(jQuery);


