var schemeInfoSource = $("#schemeInfo-template").html();
var schemeInfoTemplate = Handlebars.compile(schemeInfoSource);
var schemeInfoContext;
function schemeInfo(id) {
    $.ajax({
        type: "POST",
        data: {id: id},
        url: "/manage/apidoc/apiDocScheme/getSignSchemeInfo.html",
        error: function (data) {
            alert("Connection error");
            return false;
        },
        success: function (data) {
            schemeInfoContext = data;
            var schemeInfoHtml = schemeInfoTemplate(schemeInfoContext);
            $('#schemeBaseInfo').html(schemeInfoHtml);
        },
        dataType: "json"
    });
};

function addAndRemoveEditInit(id) {
    var modal;
    $.ajax({
        type: "POST",
        data: {id: id},
        url: '/manage/apidoc/apiDocScheme/getSelectSchemeList.html',
        dataType: "json",
        error: function (res) {
            alert("Connection error");
            return false;
        },
        success: function (res) {
            !modal ? modal = corossBosService(res.data, res.schemeNo) : null;
            modal.open();
            $(document).on('closed', '.remodal', function (e) {
                modal && modal.destroy();
                modal = null;
                schemeInfo(id);
            });
            $(document).on('click', '#closeModal', function(e){
                modal && modal.close();
            })
        }
    })
}

function corossBosService(options, schemeNo) {
    var data = options || {};
    var tpl = $('#modal-template').html();
    var modal = $(Handlebars.compile(tpl)(data));
    var remodal = modal.remodal();


    modal.find('#search').multiselect({
        search: {
            left: '<input type="text" name="q" class="form-control" placeholder="Search..." />',
            right: '<input type="text" name="q" class="form-control" placeholder="Search..." />',
        },
        startUp: false,
        sort: false,
        afterMoveToRight: function ($left, $right, $options) {
            // *** 未解决方案添加服务 ***

            var serviceNos = '',
                len = $options.length;

            $.each($options, function (index, ele) {

                if (index == (len - 1)) {
                    serviceNos += $(ele).data('service-no')
                } else {
                    serviceNos += $(ele).data('service-no') + ',';
                }

            })

            $.ajax({
                url: '/manage/apidoc/apiDocScheme/addServicesToScheme.json',
                data: {serviceNos: serviceNos, schemeNo: schemeNo},
                success: function (res) {
                    if (res && res.success) {
                        // Y.alert('Tip', res.message || 'success...')
                    } else {
                        // Y.alert('Tip', res.message || 'failed...')
                    }
                }
            })

        },
        afterMoveToLeft: function ($left, $right, $options) {
            // *** 删除解决方案下的服务 ***
            var serviceNos = '',
                len = $options.length;

            $.each($options, function (index, ele) {

                if (index == (len - 1)) {
                    serviceNos += $(ele).data('service-no')
                } else {
                    serviceNos += $(ele).data('service-no') + ',';
                }
            })

            $.ajax({
                url: '/manage/apidoc/apiDocScheme/deleteServicesToScheme.json',
                data: {serviceNos: serviceNos, schemeNo: schemeNo},
                success: function (res) {
                }
            })
        }
    });

    return remodal;
}