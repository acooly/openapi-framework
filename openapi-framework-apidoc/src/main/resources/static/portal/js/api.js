$(document).ready(function(){
	//定义input选择美化函数
	function change(){
		//美化input[checkbox]
		$(".input").each(function(){		
			$(this).on("click",function(){			
				var c = $(this).prop("checked");
				if(c){
					$(this).parents(".checkbox").addClass("checkbox-selected");
				}else{
					$(this).parents(".checkbox").removeClass("checkbox-selected");
				}
			})
		})		
		//全选及反选
		$("table th .input").on("click",function(){
			$(this).parents("table").find("td .input").prop("checked",this.checked);
			var c =this.checked;
			if(c){
				$(this).parents("table").find(".checkbox").addClass("checkbox-selected");
			}else{
				$(this).parents("table").find(".checkbox").removeClass("checkbox-selected");
			}			
		})
		//判断每个是否被选中勾选全选或者反选
		$("table td .input").on("click",function(){
			var flag = true;
			$("table td .input").each(function(){
				if(this.checked==false){
					flag = false;
				}
				$("table th .input").prop("checked",flag);
				if(flag){
					$(this).parents("table").find("th .checkbox").addClass("checkbox-selected");
				}else{
					$(this).parents("table").find("th .checkbox").removeClass("checkbox-selected");
				}
			})			
		})		
	}	
	

	//tab切换点击加载数据
	$(".tab").each(function(){
		var f = $(this).find(".tab-menu li").children().length;
		//tab是否还有二级
		if(f==0){
			var i = $(this).find(".tab-menu ul li.on").index();
			var t = $(this).find(".tab-menu ul li.on").text();
			$(this).find(".tab-content .list").find(".list-title").text(t);
			//load(i);
			$(this).find(".tab-menu ul li").click(function(){
				var val = $(this).text();
				var index = $(this).index();
				$(this).addClass("on").siblings().removeClass("on");
				$(this).parents(".tab").find(".tab-content .list").find(".list-title").text(val);
			})
		}
	})

})

	$("#myDiv").fadeTo("slow", 0.01, function() { //fade
		$(this).slideUp("slow", function() { //slide up
			$(this).remove(); //then remove from the DOM
		});
	});