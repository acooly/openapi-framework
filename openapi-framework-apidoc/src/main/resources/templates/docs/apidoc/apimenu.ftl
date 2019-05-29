<div class="doc-menu">
    <div class="doc-menu-header">
    ${apiScheme.title}<s>（${apidocs?size}）</s>
        <i class="icon_menu_switch"></i>
    </div>
    <ul>
    <#list apidocs as e>
        <li id="apidoc_api_${e.id}">
            <a href="javascript:;" title="${e.title}" onclick="loadData('${e.serviceNo}')"><div>${e.name}</div></a>
        </li>
    </#list>
    </ul>
</div>

<script>

    $('.doc-menu ul li').click(function(){
        $('.doc-menu ul li').removeClass("item-this");
        $(this).addClass("item-this");
    });

    $('.doc-menu .doc-menu-header').click(function () {
        if($(this).next('ul').is(':hidden')){
            $(this).next('ul').show();
        }else{
            $(this).next('ul').hide();
        }
    });



</script>