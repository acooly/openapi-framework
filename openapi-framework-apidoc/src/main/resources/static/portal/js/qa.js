$(document).ready(function(){

	//点击加载数据

	$(".tab-title ul li dl dd").click(function(){
		var val = $(this).text();
		var k = $(this).attr("data-id");
		$(".wt-list").html("");
		$(".tab-content .list-title").text(val);
		$(this).addClass("active01").siblings().removeClass("active01");
		$(this).parents("li").siblings("li").find("dl dd").removeClass("active01");
		var html="";
		$.ajax({
			type:"get",
			url:"json/qa.json",
			dataType:"json",
			async:true,
			beforeSend:function(data){
				$(".loading").show();
			},
			success:function(data){						
				var d = data;
				$(".loading").hide();	
				$(".wt-list").html("");
				$.each(d, function(i) {	
					if(k==data[i].id){								
		                if(typeof d[i].children !="undefined" && d[i].children.length >0) {			                				                    
		                    $.each(d[i].children, function(j) {
		                    	html+='<li class="clearfix switch">';
								html+=	'<div class="fl">'+d[i].children[j].bh+'</div>';
								html+=	'<div class="fr">';
								html+=		'<div class="switch-title clearfix"><a href="javascript:void(0)">'+d[i].children[j].wt+'</a></div>';
								html+=		'<div class="switch-content">';
								html+=			'<div class="text"><p>'+d[i].children[j].hd+'</p></div>';
								html+=			'<div class="btn"></div>';
								html+=		'</div>'
								html+=	'</div>'
								html+='</li>';
		                    });	
		                }
		                $(".paging").show();
		                return false;
					}else{
						$(".paging").hide();
					}
				});
				$(".wt-list").append(html);
				page();		
				change(".switch",".switch-title a",".switch-content",".btn");
			},
			error:function(data){
				
			}
		});
	})
		
	
	
	//进入加载所有数据
	function ajax(data){
		var html="";
		$.ajax({
			type:"get",
			url:"json/qa.json",
			dataType:"json",
			async:true,
			beforeSend:function(data){
				$(".loading").show();
			},
			success:function(data){						
				var d = data;
				$(".loading").hide();	
				$(".wt-list").html("");
				$.each(d, function(i) {	
	                if(typeof d[i].children !="undefined" && d[i].children.length >0) {			                				                    
	                    $.each(d[i].children, function(j) {
	                    	html+='<li class="clearfix switch">';
							html+=	'<div class="fl">'+d[i].children[j].bh+'</div>';
							html+=	'<div class="fr">';
							html+=		'<div class="switch-title clearfix"><a href="javascript:void(0)">'+d[i].children[j].wt+'</a></div>';
							html+=		'<div class="switch-content">';
							html+=			'<div class="text"><p>'+d[i].children[j].hd+'</p></div>';
							html+=			'<div class="btn"></div>';
							html+=		'</div>'
							html+=	'</div>'
							html+='</li>';
	                    });			                    
	                }											
				});
				$(".wt-list").append(html);
				page();
				change(".switch",".switch-title a",".switch-content",".btn");
			},
			error:function(data){
				
			}
		});		
	}
	
	
	//分页函数
	function page(){
	    $("div.holder").jPages({
	    	containerID  : "itemContainer",
	        first       : "首页",
	        previous    : "上一页",
	        next        : "下一页",
	        last        : "末页",							    	
		    perPage      : 20,
		    startPage    : 1,
		    startRange   : 1,
		    midRange     : 5,
		    endRange     : 1,
		    minHeight    :false,
	        callback    : function( pages, items ){
		        $("#legend1").html(items.range.start + " - " + items.range.end + "条");
		        $("#legend2").html(" 共 " + items.count + "条数据");
	        }
	    });		
	}
	
	ajax();
	
	
})

