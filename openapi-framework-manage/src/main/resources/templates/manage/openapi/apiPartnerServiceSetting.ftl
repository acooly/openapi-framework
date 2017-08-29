<style>
    .servicelist {
    }

    .servicelist ul {
        list-style: none;
        padding: 0 5px;
        margin: 5px 0;
    }

    .servicelist ul li {
        display: inline;
        float: left;
        margin-right: 5px;
        border: 1px solid #ddd;
        border-radius: 3px;
        padding: 3px 3px;
        margin-bottom:5px;
    }

    .servicelist h2,h3 {
        margin: 5px;
    }

    .servicelist h2{
        border-bottom: 1px solid #dddddd; margin-top: 5px;
    }

    .servicelist ul li span {cursor: pointer;}

    .servicelist ul li div {float:left;height: 36px;line-height: 36px;margin-right: 5px;}

    .servicelist ul li a {float:left;color:#000;text-decoration:none; }
    .servicelist ul li a:hover {color:#393D49;}
    .servicelist ul li a:visited {color:#000;}

    .partnerinfo {margin: 5px;}
    .partnerinfo span {margin-right: 10px; font-size: 14px;}
    .partnerinfo label {font-weight: bold;}

</style>
<!-- 布局 -->
<div id="manage_apiPartner_setting_layout" class="easyui-layout" data-options="fit:true,border:true">
    <!-- 可选服务视图 -->
    <div data-options="region:'west',split:false,border:true,collapsible:false" style="width:605px;padding:5px">
        <div class="servicelist">
            <!-- 查询框 -->
            <div class="tableForm">
                <input id="manage_apiService_search_key" type="text" class="text" style="width: 300px;"/>
                <a href="javascript:void(0);" onclick="manage_apiService_search()" class="easyui-linkbutton" data-options="plain:false" onclick=""><i
                        class="fa fa-search fa-lg fa-fw fa-col"></i>搜索</a>

            </div>
            <!-- 可选服务列表,按一级分类显示 -->
            <div id="availableApis">
                <h3>通用服务</h3>
                <ul></ul>
                <div style="clear: both;"></div>

            </div>
        </div>
    </div>

    <!-- 操作按钮 -->
    <div data-options="region:'center',split:false,border:true,collapsible:false" >
        <div id="manage_apiPartnerService_titlebar" style="padding:60px 11px;">
            <a href="javascript:void(0)" class="easyui-linkbutton" style="margin-bottom: 5px;" onclick="manage_apiPartner_service_add()">
                <i class="fa fa-chevron-right fa-lg fa-fw fa-col" style="font-size: 20px;margin-top: 6px;"></i><div style="margin-top: -2px;">添加</div>
            </a>
            <a href="javascript:void(0)" class="easyui-linkbutton" style="margin-bottom: 5px;" onclick="manage_apiPartner_service_remove()">
                <i class="fa fa-chevron-left fa-lg fa-fw fa-col" style="font-size: 20px;margin-top: 6px;"></i><div style="margin-top: -2px;">移除</div>
            </a>
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="manage_apiPartner_service_save()">
                <i class="fa fa-floppy-o fa-lg fa-fw fa-col" style="font-size: 20px;margin-top: 6px;"></i><div style="margin-top: -2px;">保存</div>
            </a>
        </div>
    </div>


    <!-- 已选服务视图 -->
    <div data-options="region:'east',split:false,border:true,collapsible:false" style="width: 100px;">
        <form id="manage_apipartnerservice_form">
        <input type="hidden" id="manage_partnerservice_apipartnerid" name="apiPartnerId" value="${apiPartner.id}"/>
        <div class="servicelist" id="manage_apipartnerservice">
            <h3>选择的服务:</h3>
            <ul id="manage_apipartnerservice_selected"></ul>
            <div style="clear: both;margin-top: 5px;"><hr></div>
            <div id="manage_apipartner_authedApis"></div>
        </div>
        </form>
    </div>

</div>
<script>
    function manage_apiPartner_layout_resize() {
        var fullWidth = $('#manage_apiPartner_setting_layout').css('width');
        fullWidth = fullWidth.substring(0, fullWidth.length - 2);
        var wk_width = (parseInt(fullWidth) - 60) / 2;
        $('#manage_apiPartner_setting_layout').layout('panel', 'west').panel('resize', {width: wk_width});
        $('#manage_apiPartner_setting_layout').layout('panel', 'east').panel('resize', {width: wk_width});
        $('#manage_apiPartner_setting_layout').layout('resize');
    }

    /**
     * 查询可选服务列表
     */
    function manage_apiService_search() {
        var key = $('#manage_apiService_search_key').val();
        $.ajax({
            url: '/manage/openapi/apiService/search.html',
            data: {key: key},
            success: function (result) {
                if(result.success){
                    var apiList = '';
                    $.each(result.data.apis,function(key,value){
                        if(value && value.length > 0){
                            var section = '<h3>'+key+'</h3><ul>';
                            $.each(value,function(index,e){
                                section += '<li><div><input id="manage_apiService_api_'+e.id+'" name="manage_apiService_api" value="'+e.id+','+e.name+','+e.title+','+e.version+'" type="checkbox"></div>' +
                                        '<a href="javascript:;" onclick="$(\'#manage_apiService_api_'+e.id+'\').click()" title="'+e.title+':'+e.name+':'+e.version+'">'+e.title+'<br>'+e.name+'</a></li>';
                            });
                            apiList += section + '</ul><div style="clear: both;"></div>';
                        }
                    });
                    $('#availableApis').html(apiList);
                }
                if (result.message) $.messager.show({title : '提示', msg : result.message});
            },
            error: function (r,s,e) {
                $.messager.show({title : '失败', msg : e});
            }
        });
    }

    /**
     * 查询接入商服务权限列表
     */
    function manage_partner_service_search() {
        var apiPartnerId = $('#manage_partnerservice_apipartnerid').val();
        $.ajax({
            url: '/manage/openapi/apiPartnerService/search.html',
            data: {apiPartnerId: apiPartnerId},
            success: function (result) {
                if(result.success){
                    var apiList = '';
                    $.each(result.data.apis,function(key,value){
                        if(value && value.length > 0){
                            var section = '<h3>'+key+'</h3><ul>';
                            $.each(value,function(index,e){
                                section += '<li><div><input name="manage_partner_api" value="'+e.id+'" type="checkbox"></div>' +
                                        '<a href="javascript:;" title="'+e.title+':'+e.name+':v'+e.version+'" onclick="$(this).siblings().children().click();">'+e.title+'<br>'+e.name+'</a></li>';
                            });
                            apiList += section + '</ul><div style="clear: both;"></div>';
                        }
                    });
                    $('#manage_apipartner_authedApis').html(apiList);
                }
                if (result.message) $.messager.show({title : '提示', msg : result.message});
            },
            error: function (r,s,e) {
                $.messager.show({title : '失败', msg : e});
            }
        });
    }

    /**
     * 添加服务
     */
    function manage_apiPartner_service_add() {
        var apiHtml;
        $('input[name="manage_apiService_api"]:checked').each(function(){
            var value=$(this).val();
            var apiVals = value.split(',');
            var id = apiVals[0],name=apiVals[1],title=apiVals[2],version=apiVals[3];
            var exist = existCheck(id);
            if(!exist){
                apiHtml = '<li><div><input name="manage_partner_api" value="'+id+'" type="checkbox"></div>' +
                        '<a href="javascript:;" title="'+title+':'+name+':v'+version+'" onclick="$(this).siblings().children().click();">'+title+'<br>'+name+'</a></li>';
                $('#manage_apipartnerservice_selected').append(apiHtml);
            }

        });
    }

    /**
     * 删除服务
     */
    function manage_apiPartner_service_remove(){
        $('input[name="manage_partner_api"]:checked').each(function(){
            $(this).parent().parent().remove();
        });
    }


    /**
     * 提交保存设置
     */
    function manage_apiPartner_service_save(){
        $("#manage_apipartnerservice input[type='checkbox']").attr("checked",'checked');
        var formData = $('#manage_apipartnerservice_form').serialize();
        $.ajax({
            url:'/manage/openapi/apiPartnerService/settingSave.html',
            data:formData,
            success:function(result){
                if(result.success){
                    clearSelectedContainer();
                    // 保持成功后,retrivie用户权限列表
                    manage_partner_service_search();
                }
                if (result.message) $.messager.show({title : '提示', msg : result.message});
            },
            error: function (r,s,e) {
                $.messager.show({title : '失败', msg : e});
            }
        });

    }


    function clearSelectedContainer(){
        $('#manage_apipartnerservice_selected').empty();
    }

    function existCheck(apiId){
        var result = false;
        $("#manage_apipartnerservice input[type='checkbox']").each(function(){
            if($(this).val() == apiId){
                result = true;
                return false;
            }
        });
        return result;
    }


    $(function () {
        $('#manage_apiPartner_setting_layout').layout();
        manage_apiPartner_layout_resize();
        manage_apiService_search();
        manage_partner_service_search();
        $(window).resize(function () { //浏览器窗口变化
            manage_apiPartner_layout_resize();
        });
        $('#manage_apiService_search_key').keydown(function(event) {
            if (event.keyCode == 13) {
                manage_apiService_search();
            }
        });
    });


</script>