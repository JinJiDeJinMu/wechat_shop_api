$(function () {
    var url = '../ordercashapply/list';
    $("#jqGrid").Grid({
        url: url,
        datatype: "json",
        colModel: [
            {label: 'id', name: 'orderId', index: 'order_id', key: true, align: 'center', width: 60, hidden: true},
            {label: '订单号', name: 'orderSn', index: 'order_sn', align: 'center', width: 120},
            {label: '商户店铺', name: 'merchantName', index: 'merchant_name', align: 'center', width: 100},
            {label: '商户号', name: 'merchantId', index: 'merchant_id', align: 'center', width: 80, hidden: true},
            {label: '订单金额', name: 'actualPrice', index: 'actual_price', align: 'center', width: 80},
            {
                label: '订单申请类型',
                name: 'status',
                index: 'status',
                align: 'center',
                width: 60,
                formatter: function (value) {
                    if (value == '0') {
                        return '审核中';
                    } else if (value == '1') {
                        return '已通过';
                    } else if (value == '2') {
                        return '已驳回';
                    } else if (value == '3') {
                        return '其他';
                    }
                    return '-';
                }
            },
            {
                label: '订单支付时间', name: 'payTime', index: 'pay_time', align: 'center', width: 100,
                formatter: function (value) {
                    return transDate(value);
                }
            },
            {label: '申请人id', name: 'applyId', index: 'apply_id', align: 'center', width: 80, hidden: true},
            {label: '申请人', name: 'applyName', index: 'apply_name', align: 'center', width: 80},
            {
                label: '提现申请时间', name: 'applyTime', index: 'apply_time', align: 'center', width: 100,
                formatter: function (value) {
                    return transDate(value);
                }
            },
            {label: '审核人id', name: 'operator', index: 'operator', align: 'center', width: 80, hidden: true},
            {label: '审核人', name: 'operatorName', index: 'operator_name', align: 'center', width: 80},
            {label: '备注', name: 'remarks', index: 'remarks', align: 'center', width: 80},

           /* {
                label: '操作', width: 120, align: 'center', sortable: false, formatter: function (value, col, row) {
                    return '<button class="btn btn-outline btn-info" onclick="vm.review(' + row.orderId + ',' + row.status + ')"><i class="fa fa-info-circle"></i>&nbsp;审核</button>' +
                        '<button class="btn btn-outline btn-primary" style="margin-top: 0px;" onclick="vm.reject(' + row.orderId + ',' + row.status + ')")"><i class="fa fa-print"></i>&nbsp;驳回</button>';

                }
            }*/
        ]
    });
});

var vm = new Vue({
    el: '#rrapp',
    data: {
        showList: true,
        detail: false,
        title: null,
        order: {},
        shippings: [],
        q: {
            orderSn: '',
            orderStatus: '',
            goodsType: '',
            addTime: '',
            endTime: ''
        }
    },
    methods: {
        query: function () {
            vm.reload();
        },
        reload: function (event) {
            vm.showList = true;
            vm.detail = false;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {},
                page: page
            }).trigger("reloadGrid");
        },
        review: function () {
                Ajax.request({
                    url: "../ordercashapply/review",
                    async: true,
                    successCallback: function (r) {
                        vm.reload();
                    }
                });
        },
        reject: function (id, status) {
            if (status == 0) {
                Ajax.request({
                    url: "../ordercashapply/reject/" + id,
                    async: true,
                    successCallback: function (r) {
                        vm.reload();
                    }
                });
            } else {
                alert("订单提现已通过或者驳回！");
                return;
            }
        }
    }
});