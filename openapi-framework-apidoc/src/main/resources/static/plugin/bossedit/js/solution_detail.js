
/**
 * @export none
 * @author zhengze@yiji.com
 * @created 20160613
 * @update 
 */


require('./lib/crossSelect/lib/crossSelect.js');

var TplApply = require('./modules/tplapply.js'),

    solutionDetailTpl = require('./modules/page/solution_detail.handlebars'),

    site = require('@yj/site'),

    solutionId = site.getParam().id,

    crossBox = require('./modules/cross_box'),

    solutionDetail = new TplApply({

        url: '/apischeme/getSignSchemeInfo.json?id=' + solutionId,

        template: solutionDetailTpl,

        afterRender: function() {

            $('#edit-solution-detail').click(function(){

                // *** get data && show crossBox　***
                $.ajax({
                    url: '/apischeme/getSelectSchemeList.json?id=' + solutionId,
                    success: function(res) {

                        if(res && res.success) {
                            crossBox({data: res.data, id: solutionId}).show();
                        } else {
                            Y.alert('Tip', res.message || '获取数据失败！！！')
                        }
                    },
                    error: function(){
                        Y.alert('Tip', '获取数据失败！！！')
                    }
                })

                // crossBo x({id: solutionId}).show();
            });
        }

    }).renderTo('.module-main');

    // solutionDetail = new Paging({

    //     url: '/solution_detail.json?solutionName=' + window.hash,
    //     tpl: solutionDetailTpl,
    //     container: '.module-main',

    //     afterQuery: function(res){
    //         if(!res.data.result || !res.data.result.length) {
    //             $('#no-result').show().siblings().hide();
    //         }
    //     },

    //     afterRender: function (data) {

    //     }
    // });

// *** search ***
$('.js-search').click(function(){

    // var searchText = {};

    // $('#solutionSearchForm').serializeArray().forEach(function(item){
    //     searchText[item.name] = item.value;
    // });

    // pageSearchResultObj.setParam(searchText);
    // pageSearchResultObj.toPage(1);
    // *** search result page ***
    // pageSearchResultObj = new Paging({

    //     url: '/solution.json',
    //     tpl: searchResultTpl,
    //     param: searchText,
    //     container: '.user-main',

    //     afterQuery: function(res){
    //         if(!res.data.result || !res.data.result.length) {
    //             $('#no-result').show().siblings().hide();
    //         }
    //     },

    //     afterRender: function (data) {

    //     }
    // });

});


// *** check api detail & edit ***
$(document).on('click', '.edit-solution', function(){

    solutionEdit({
        data: {},
        url: '/user/changePwd.json',
        cb: function(){
            pageObj.toPage(1);
        }
    }).show();   

});

