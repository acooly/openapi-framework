var x,y;
function enableDnd(t,callback) {
	var nodes = t.treegrid('getPanel').find('tr[node-id]');
	nodes.find('span.tree-hit').bind('mousedown.treegrid', function() {
		return false;
	});
	nodes.draggable({
		disabled: false,
		revert: true,
		cursor: 'pointer',
		proxy: function(source) {
			var p = $('<div class="tree-node-proxy tree-dnd-no"></div>').appendTo('body');
			p.html($(source).find('.tree-title').html());
			p.hide();
			return p;
		},
		deltaX: 15,
		deltaY: 15,
		onBeforeDrag: function() {
			$(this).next('tr.treegrid-tr-tree').find('tr[node-id]').droppable({ accept: 'no-accept' });
		},
		onStartDrag: function() {
			$(this).draggable('proxy').css({
				left: -10000,
				top: -10000
			});
		},
		onDrag: function(e) {
			var prox =$(this).draggable('proxy');
			$(document).mousemove(function(event){//鼠标释放
				prox.show();
			});
		},
		onStopDrag: function() {
			$(this).next('tr.treegrid-tr-tree').find('tr[node-id]').droppable({ accept: 'tr[node-id]' });
		}
	}).droppable({
		accept: 'tr[node-id]',
		onDragOver: function(e, source) {
			var pageY = source.pageY;
			var top = $(this).offset().top;
			var bottom = top + $(this).outerHeight();
			$(source).draggable('proxy').removeClass('tree-dnd-no').addClass('tree-dnd-yes');
			$(this).removeClass('row-append row-top row-bottom');
			if (pageY > top + (bottom - top) / 2) {
				if (bottom - pageY < 5) {
					$(this).addClass('row-bottom');
				} else {
					$(this).addClass('row-append');
				}
			} else {
				if (pageY - top < 5) {
					$(this).addClass('row-top');
				} else {
					$(this).addClass('row-append');
				}
			}
		},
		onDragLeave: function(e, source) {
			$(source).draggable('proxy').removeClass('tree-dnd-yes').addClass('tree-dnd-no');
			$(this).removeClass('row-append row-top row-bottom');
		},
		onDrop: function(e, source) {
			var action, point;
			if ($(this).hasClass('row-append')) {
				action = 'append';
			} else {
				action = 'insert';
				point = $(this).hasClass('row-top') ? 'top' : 'bottom';
			}
			$(this).removeClass('row-append row-top row-bottom');
			// alert(action + ":" + point);
			// your logic code here
			// do append or insert action and reload the treegrid data
			//==================================
			//做自己的逻辑处理
			var src  = t.treegrid('find', $(source).attr('node-id'));
			var dest = t.treegrid('find', $(this).attr('node-id'));
			callback(src,dest);
		}
	});
}