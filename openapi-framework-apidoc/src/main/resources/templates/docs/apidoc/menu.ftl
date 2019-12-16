<div class="doc-menu">
    <div class="doc-menu-header">
        <a href="/docs/scheme/index.html"><i class="layui-icon">&#xe705;</i> 解决方案<i class="icon_menu_switch"></i></a>
    </div>
    <ul class="doc-menu-content">
    <#if showSchemes??>
    <#list showSchemes as e>
        <li <#if e.id = schemeId >class="item-this"</#if>><a href="/docs/scheme/scheme.html?schemeId=${e.id}">${e.title}</a></li>
    </#list>
    </#if>
    </ul>
</div>


<#--<div class="doc-menu">-->
    <#--<#list showSchemes as e>-->
        <#--<div class="doc-menu-header"><i class="layui-icon">&#xe637;</i> ${e.name}<i class="icon_menu_switch"></i></div>-->
        <#--<ul>-->
        <#--<#list e.contents?keys as key>-->
            <#--<li <#if page?? && page = key>class="item-this"</#if>><a href="/docs/scheme/content.html?id=${key}&schemeId=${e.id}">${e.contents[key]}</a></li>-->
        <#--</#list>-->
        <#--<li <#if e.id = schemeId >class="item-this"</#if>><a href="/docs/scheme/scheme.html?shemeId=${e.id}">Api列表</a></li>-->
        <#--</ul>-->
    <#--</#list>-->
<#--</div>-->
<script>


    // $('.doc-menu .doc-menu-header').click(function () {
    //     if($(this).next('ul').is(':hidden')){
    //         $(this).next('ul').show();
    //     }else{
    //         $(this).next('ul').hide();
    //     }
    // });


</script>