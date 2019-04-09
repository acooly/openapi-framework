$(document).ready(function(){
	//复制内容到剪切板
	function copy(){
		var clipboard = new Clipboard('.copy-btn');
		clipboard.on('success', function(e) {
			setTimeout(function(){
				$(".copy-btn").addClass("copy-btn-selected").text("已复制");
			},1)
		});				
	}
	copy();	
	
	//代码高亮
	function gl(){
		$("pre code").each(function(i, block) {
			hljs.highlightBlock(block);
		});					
	}


	$(".api-details .tab-title ul li").on("click",function(){
		var index = $(this).attr("data-id");
		var k = $(this).index();
		$(this).addClass("on").siblings().removeClass("on");
	})
	
	
	//函数调用
	tab(".tab1",".tab-title1 ul li",".tab-content1 .list1");
	change(".switch",".switch-title a",".switch-content")
	
	
	//经过显示详情及离开关闭详情	
	function hover(){
		$(".hover-details").on("mouseenter",function(){
			$(this).find(".details").fadeIn();
		})
		$(".hover-details").on("mouseout",function(){
			var t = $(this);
			setTimeout(function(){
				t.find(".details").fadeOut();	
			},1000)			
		})			
	}


	
})





