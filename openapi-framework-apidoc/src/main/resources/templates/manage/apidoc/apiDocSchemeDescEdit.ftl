<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<link rel="stylesheet" href="/plugin/editormd/css/editormd.css" />
<style>
    li{ list-style: disc;}
</style>
<div class="easyui-layout" data-options="fit : true,border : false">
<#--    <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;" align="left">-->
<#--        <form id="manage_apiDocSchemeDesc_searchform" onsubmit="return false">-->
<#--            <table class="tableForm" width="100%">-->
<#--                <tr>-->
<#--                    <td align="left">-->
<#--                        <div>-->
<#--                            <a href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:false" onclick="insertApi()"><i class="fa fa-flag fa-lg fa-fw fa-col"></i>插入Api</a>-->
<#--                        </div>-->
<#--                    </td>-->
<#--                </tr>-->
<#--            </table>-->
<#--        </form>-->
<#--    </div>-->
    <!-- 列表和工具栏 -->
    <div data-options="region:'center',border:true,split:true">
        <div class="easyui-layout" data-options="fit : true,border : false">
            <div data-options="region:'center',border:false">
                <form id="manage_apiDocSchemeDesc_editform" action="/manage/apidoc/apiDocSchemeDesc/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post">
                    <@jodd.form bean="apiDocSchemeDesc" scope="request">
                        <input name="id" type="hidden" />
                        <input name="schemeNo" type="hidden"/>
                        <input id="schemeDesc" name="schemeDesc" type="hidden"/>
                    </@jodd.form>
                </form>
                <div id="editorContent">
                    <textarea style="display: none">${apiDocSchemeDesc.schemeDesc}</textarea>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="/plugin/editormd/editormd.js"></script>
<script type="text/javascript">
    var editor;
    $(function() {
        editor = editormd("editorContent", {
            // width  : "100%",
            // height : "95%",
            path   : "/plugin/editormd/lib/",
            toolbarIcons : function() {
                // Or return editormd.toolbarModes[name]; // full, simple, mini
                // Using "||" set icons align right.
                return ["undo", "redo", "|",
                    "bold", "del", "italic", "quote", "ucwords", "uppercase", "lowercase", "|",
                    "h1", "h2", "h3", "h4", "h5", "h6", "|",
                    "list-ul", "list-ol", "hr", "|",
                    "link", "reference-link", "image", "code", "preformatted-text", "code-block", "table", "datetime", "emoji", "html-entities", "pagebreak", "|",
                    "goto-line", "watch", "preview", "fullscreen", "clear", "search", "|",
                    "help", "info","save"]
            },
            toolbarIconsClass : {
                save : "fa-fw fa-col fa-floppy-o"  // 指定一个FontAawsome的图标类
            },
            // toolbarIconTexts : {
            // 	save : "保存"  // 如果没有图标，则可以这样直接插入内容，可以是字符串或HTML标签
            // },
            lang : {
                toolbar : {
                    save: "保存",
                }
            },
            toolbarAutoFixed:true,
            // autoHeight : true,
            // theme : "dark",
            // previewTheme : "dark",
            // editorTheme : "pastel-on-dark",
            codeFold : true,
            //syncScrolling : false,
            saveHTMLToTextarea : true,    // 保存 HTML 到 Textarea
            searchReplace : true,
            //watch : false,                // 关闭实时预览
            htmlDecode : "style,script,iframe|on*",            // 开启 HTML 标签解析，为了安全性，默认不开启
            //toolbar  : false,             //关闭工具栏
            //previewCodeHighlight : false, // 关闭预览 HTML 的代码块高亮，默认开启
            emoji : true,
            taskList : true,
            tocm : true,         // Using [TOCM]
            // tex : true,                   // 开启科学公式TeX语言支持，默认关闭
            // flowChart : true,             // 开启流程图支持，默认关闭
            // sequenceDiagram : true,       // 开启时序/序列图支持，默认关闭,
            //dialogLockScreen : false,   // 设置弹出层对话框不锁屏，全局通用，默认为true
            //dialogShowMask : false,     // 设置弹出层对话框显示透明遮罩层，全局通用，默认为true
            //dialogDraggable : false,    // 设置弹出层对话框不可拖动，全局通用，默认为true
            //dialogMaskOpacity : 0.4,    // 设置透明遮罩层的透明度，全局通用，默认值为0.1
            //dialogMaskBgColor : "#000", // 设置透明遮罩层的背景颜色，全局通用，默认为#fff
            // 自定义工具栏按钮的事件处理
            toolbarHandlers : {
                /**
                 * @param {Object}      cm         CodeMirror对象
                 * @param {Object}      icon       图标按钮jQuery元素对象
                 * @param {Object}      cursor     CodeMirror的光标对象，可获取光标所在行和位置
                 * @param {String}      selection  编辑器选中的文本
                 */
                save : function(cm, icon, cursor, selection) {
                    //var cursor    = cm.getCursor();     //获取当前光标对象，同cursor参数
                    //var selection = cm.getSelection();  //获取当前选中的文本，同selection参数
                    alert("test");
                },
            },
            imageUpload : true,
            imageFormats : ["jpg", "jpeg", "gif", "png", "bmp", "webp"],
            imageUploadURL : "/ofile/upload.html",
            onload : function() {
                // console.log('onload', this);
                // this.fullscreen();
                // this.unwatch();
                // this.watch().fullscreen();
                //
                <#--this.setMarkdown(${apiDocSchemeDesc.content});-->
                // this.width("100%");
                // this.height(480);
                // this.resize("100%", 640);
            }
        });
    });

    function manage_apiDocSchemeDesc_editform_beforeSubmit() {
        $("#schemeDesc").val(editor.getMarkdown());
    }
</script>
