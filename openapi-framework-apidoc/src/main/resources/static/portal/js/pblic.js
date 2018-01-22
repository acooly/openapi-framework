
//tab切换
function tab(a,b,c,d){
	$(a).each(function(){
		var index = $(this).find(b+".on").index();
		var text = $(this).find(b+".on").text();
		$(this).find(c).find(d).text(text);
		$(this).find(c).eq(index).show().siblings().hide();
		$(this).find(b).on("click",function(){
			var i = $(this).index();
			var t = $(this).text();
			$(this).parents(a).find(c).find(d).text(t);
			$(this).addClass("on").siblings().removeClass("on");
			$(this).parents(a).find(c).eq(i).show().siblings().hide();
		})
	})
}


//展开收缩

function change(a,b,c,d){
	$(a).each(function(){
		$(this).find(b).on("click",function(){
			$(this).parents(a).toggleClass("on");
			$(this).parents(a).find(c).slideToggle();				
		})
		$(this).find(d).on("click",function(){
			$(this).parents(a).toggleClass("on");
			$(this).parent(c).slideToggle();			
		})		
	})
}


//分页点击
function page_tj(a,b,c){
	$(a).click(function(){
      	/* get given page */
      	var page = parseInt( $(c).val() );
      	/* jump to that page */
      	$(b).jPages( page );
		$(c).val("");			    		
	})
}

//显示/隐藏正在加载
function loading(n){
	if(n==0){
		$(".loading").show();
	}else{
		$(".loading").hide();
	}
}


//手风琴
function accordion(a,b,c,d){
	$(a).each(function(){
		$(this).find(b).on("click",function(){
			$(this).parent(a).toggleClass("active");
			$(this).siblings(c).slideToggle();
			$(this).parent(a).siblings().removeClass("active").find(c).slideUp();
		});
		$(this).find(c).children().on("click",function(){
			$(this).addClass("active01").siblings().removeClass("active01");
			$(this).parents(a).siblings().find(c).children().removeClass("active01");
		})
	})
}


//返回顶部
$(document).ready(function() {
	//首先将#back-to-top隐藏
//	$(".back-top").hide();
	//当滚动条的位置处于距顶部100像素以下时，跳转链接出现，否则消失
	$(function() {
		$(window).scroll(function() {
//			var h1 = $("#cs").offset().top;
//			if($(window).scrollTop() > 100) {
//				$(".back-top").fadeIn(1500);
//			} else {
//				$(".back-top").fadeOut(1500);
//			}
		});
		//当点击跳转链接后，回到页面顶部位置
		$(".back-top").click(function() {
			$('body,html').animate({
					scrollTop: 0
				},
				500);
			return false;
		});
	});
});


