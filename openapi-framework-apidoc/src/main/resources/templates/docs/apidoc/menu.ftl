<div class="doc-menu">
    <div class="doc-menu-header">
        <i class="layui-icon">&#xe705;</i> 文档说明<i class="icon_menu_switch"></i>
    </div>
    <ul class="doc-menu-content">
        <li <#if page?? && page = 'index'>class="item-this"</#if> ><a href="/docs/apischeme/index.html">使用说明</a></li>
        <li <#if page?? && page = 'spec'>class="item-this"</#if> ><a href="/docs/apischeme/spec.html">开发规范</a></li>
<#if commonSchemes??>
    <#list commonSchemes as e>
        <li <#if e.id = schemeId >class="item-this"</#if>><a href="/docs/apischeme/scheme.html?schemeId=${e.id}">${e.name}</a></li>
    </#list>
</#if>
    </ul>
</div>


<div class="doc-menu">
    <#list customSchemes as e>
        <div class="doc-menu-header"><i class="layui-icon">&#xe637;</i> ${e.name}<i class="icon_menu_switch"></i></div>
        <ul>
        <#list e.contents?keys as key>
            <li <#if page?? && page = key>class="item-this"</#if>><a href="/docs/apischeme/content.html?id=${key}&schemeId=${e.id}">${e.contents[key]}</a></li>
        </#list>
        <li <#if e.id = schemeId >class="item-this"</#if>><a href="/docs/apischeme/scheme.html?shemeId=${e.id}">Api列表</a></li>
        </ul>
    </#list>
</div>
<script>


    $('.doc-menu .doc-menu-header').click(function () {
        if($(this).next('ul').is(':hidden')){
            $(this).next('ul').show();
        }else{
            $(this).next('ul').hide();
        }
    });


</script>