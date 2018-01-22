<!DOCTYPE html>
<html>
	<head>
        <@includePage path="/docs/common/meta.html"/>
        <@includePage path="/docs/common/include.html"/>
		<link rel="stylesheet" type="text/css" href="/portal/style/css/index.css"/>
        <link rel="stylesheet" href="/portal/js/fullpage/jquery.fullPage.css" />
		<script src="/portal/js/fullpage/jquery.fullPage.min.js"></script>
	</head>
	<body>
		<!--顶部-->
		<@includePage path="/docs/common/header.html"/>
		<!--正文区域-->
		
			<!--绑定菜单-->
				<ul id="menu">
					<li data-menuanchor="page1" class="active"><a href="#page1"></a></li>
					<li data-menuanchor="page2"><a href="#page2"></a></li>
					<li data-menuanchor="page3"><a href="#page3"></a></li>
				</ul>	
			<!--end-->
		
			<!--主体-->		
			<div id="dowebok">
				<div class="section">
					<div class="img w1190">
						<img src="/portal/images/index1.png"/>
					</div>					
				</div>
				<div class="section">
					<div class="img w1190">
						<img src="/portal/images/index2.png"/>
					</div>
				</div>
				<div class="section">
					<div class="img w1190">
						<img src="/portal/images/index3.png"/>
					</div>
				</div>				
			</div>
			<div class="mouse"></div>
				
			<!--end-->
		
		<!--end-->

        <@includePage path="/docs/common/footer.html"/>

		<script>
		$(function(){
			$('#dowebok').fullpage({
				anchors: ['page1', 'page2', 'page3'],
				menu: '#menu',
				afterLoad:function(anchorLink,index){
					if(index==3){
						$(".foot").delay(500).fadeIn(1000);
					}
				},
				onLeave:function(index, direction){
					if(index==3){
						$(".foot").fadeOut(500);
					}
				}
			});
		});
		</script>		
		
	</body>
</html>
