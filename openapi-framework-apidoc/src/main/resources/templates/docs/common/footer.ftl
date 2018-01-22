<!--底部-->
<div class="foot">
    <div class="container transparent">
        <div class="d1">${copyright}</div>
    </div>
</div>
<!--end-->
<script>

    layui.use('element', function(){
        var util = layui.util
        var element = layui.element; //导航的hover效果、二级菜单等功能，需要依赖element模块
        element.init();

        //固定块
        util.fixbar({
            bar1: false,
            bar2: false,
            css: {right: 15, bottom: 61},
            bgcolor: '#393D49'
        });
    });

</script>
