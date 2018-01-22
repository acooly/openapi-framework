<link rel="stylesheet" href="/plugin/layui/css/layui.css">
<link rel="stylesheet" href="/portal/style/css/global.css" />
<link rel="stylesheet" href="/portal/style/css/main.css" />
<script type="text/javascript" src="/portal/js/jquery-1.11.3.min.js" ></script>
<script type="text/javascript" src="/plugin/layui/layui.all.js"></script>
<script type="text/javascript" src="/plugin/acooly/acooly.portal.js"></script>
<script type="text/javascript" src="/manage/assert/script/acooly.format.js"></script>
<script type="text/javascript">
    /**
     * CSRF-Token 客户端支持脚本。
     */
    var token = $("meta[name='X-CSRF-TOKEN']").attr("content");// 从meta中获取token
    $(document).ajaxSend(function(e, xhr, options) {
        if (!options.crossDomain) {
            xhr.setRequestHeader("X-CSRF-TOKEN", token);// 每次ajax提交请求都会加入此token
        }
    });
    $(function() {
        // CSRF自动为当前页码的所有表单添加hidden(csrfToken)
        $("form").each(function() {
            if ($(this).attr('enctype') == 'multipart/form-data') {
                var action = $(this).attr('action');
                action += (action.indexOf('?') != -1 ? '&' : '?');
                action += "_csrf=${Request['org.springframework.security.web.csrf.CsrfToken'].token}";
                $(this).attr('action', action);
            } else {
                $(this).append("<input type=\"hidden\" name=\"_csrf\" value=\"${Request['org.springframework.security.web.csrf.CsrfToken'].token}\"/>");
            }
        });
    });
</script>