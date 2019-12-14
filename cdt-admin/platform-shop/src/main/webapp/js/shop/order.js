$(function () {
    var shippingStatus = getQueryString("shippingStatus");
    var payStatus = getQueryString("payStatus");
    var orderStatus = getQueryString("orderStatus");
    var goodsType = getQueryString("goodsType");
    var addTime = (getQueryString("addTime"));
    var endTime= getQueryString("endTime");
    var url = '../order/list';
    if (shippingStatus) {
        url += '?shippingStatus=' + shippingStatus;
    }
    if (payStatus) {
        url += '?payStatus=' + payStatus;
    }
    if (orderStatus) {
        url += '?orderStatus=' + orderStatus;
    }
    if (goodsType) {
        url += '?goodsType=' + goodsType;
    }
    if (addTime) {
        url += '?addTime=' + addTime;
    }
    if (endTime) {
        url += '?endTime=' + endTime;
    }
    $("#jqGrid").Grid({
        url: url,
        datatype: "json",
        colModel: [
            {label: 'id', name: 'id', index: 'id', key: true,hidden:true},
            {label: '订单号', name: 'orderSn', index: 'order_sn',align: 'center', width: 100},
            {label: '会员', name: 'userName', index: 'user_name',align: 'center',width: 80},
            {
                label: '订单类型', name: 'goodsType', index: 'goods_type',align: 'center',width: 80, formatter: function (value) {
                    if (value == '1') {
                        return '普通订单';
                    } else if (value == '2') {
                        return '秒杀';
                    } else if (value == '3') {
                        return '团购';
                    } else if (value == '4') {
                        return '砍价';
                    } else if (value == '5') {
                        return '快递代取';
                    } else if (value == '6') {
                        return '核销订单';
                    }
                    return '-';
                }
            },
            {
                label: '订单状态', name: 'orderStatus',align: 'center', index: 'order_status', width: 80, formatter: function (value) {
                    if (value == '0') {
                        return '待付款';
                    } else if (value == '101') {
                        return '订单已取消';
                    } else if (value == '102') {
                        return '订单已删除';
                    } else if (value == '201') {
                        return '订单已付款';
                    } else if (value == '206') {
                        return '待使用';
                    } else if (value == '207') {
                        return '待XX';
                    } else if (value == '300') {
                        return '订单已发货';
                    } else if (value == '301') {
                        return '用户确认收货';
                    } else if (value == '401') {
                        return '退款';
                    } else if (value == '402') {
                        return '完成';
                    } else if (value == '501') {
                        return '买家申请退货';
                    } else if (value == '502') {
                        return '退货寄回中';
                    } else if (value == '503') {
                        return '仓库已收退货';
                    } else if (value == '504') {
                        return '仓库拒绝退货';
                    }
                    return value;
                }
            },
            {
                label: '发货状态',
                name: 'shippingStatus',
                index: 'shipping_status',
                width: 60,
                align: 'center',
                formatter: function (value) {
                    if (value == '0') {
                        return '未发货';
                    } else if (value == '1') {
                        return '已发货';
                    } else if (value == '2') {
                        return '已收货';
                    } else if (value == '4') {
                        return '退货';
                    }
                    return value;
                }
            },
            {
                label: '付款状态', name: 'payStatus',align: 'center', index: 'pay_status', width: 80,
                formatter: function (value) {
                    if (value == '0') {
                        return '未付款';
                    } else if (value == '1') {
                        return '付款中';
                    } else if (value == '2') {
                        return '已付款';
                    } else if (value == '4') {
                        return '退款';
                    }
                    return value;
                }
            },
            // {
            //     label: '评价状态', name: 'evaluateStatus', index: 'evaluateStatus', width: 80,
            //     formatter: function (value) {
            //         if (value == 0) {
            //             return '未评价';
            //         } else if (value == 1) {
            //             return '已评价';
            //         }
            //         return value;
            //     }
            // },
            // {label: '实际支付金额', name: 'actualPrice', index: 'actual_price', width: 80},
            {label: '订单总价', name: 'orderPrice', index: 'order_price',align: 'center', width: 60},
            {label: '商品总价', name: 'goodsPrice', index: 'goods_price',align: 'center', width: 60},
            {
                label: '下单时间', name: 'addTime', index: 'add_time',align: 'center', width: 100,
                formatter: function (value) {
                    return transDate(value);
                }
            },
            {
                label: '操作', width: 120, align: 'center', sortable: false, formatter: function (value, col, row) {
                    return '<button class="btn btn-outline btn-info" onclick="vm.lookDetail(' + row.id + ')"><i class="fa fa-info-circle"></i>&nbsp;详情</button>' +
                        '<button class="btn btn-outline btn-primary" style="margin-top: 0px;" onclick="vm.printDetail(' + row.id + ')"><i class="fa fa-print"></i>&nbsp;打印</button>';
                }
            }
        ],
        footerrow: true,
        gridComplete: function() {
            var sum_order=$("#jqGrid").getCol('orderPrice',false,'sum').toFixed(4);
            var sum_goods=$("#jqGrid").getCol('goodsPrice',false,'sum').toFixed(4);
            $("#jqGrid").footerData('set', {orderSn:"合计",orderPrice:sum_order,goodsPrice:sum_goods})
        }
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
            addTime:'',
            endTime:''
        }
    },
    methods: {
        query: function () {
            vm.reload();
    },
        review: function(){
            var id = getSelectedRow("#jqGrid");
            if (id == null) {
                return;
            }
            Ajax.request({
                url: "../ordercashapply/save/" + id,
                async: true,
                successCallback: function (r) {
                }
            });
        },

        sendOrderComplete: function (event) {
            var id = getSelectedRow("#jqGrid");
            if (id == null) {
                return;
            }
            vm.showList = false;
            vm.title = "使用";
            Ajax.request({
                url: "../order/used/" + id,
                async: true,
                successCallback: function (r) {
                    vm.order = r.order;
                }
            });
        },
        sendGoods: function (event) {
            var id = getSelectedRow("#jqGrid");
            if (id == null) {
                return;
            }
            vm.showList = false;
            vm.title = "发货";
            Ajax.request({
                url: "../order/info/" + id,
                async: true,
                successCallback: function (r) {
                    vm.order = r.order;
                }
            });
        },
        confirm: function (event) {
            var id = getSelectedRow("#jqGrid");
            if (id == null) {
                return;
            }
            confirm('确定收货？', function () {
                Ajax.request({
                    type: "POST",
                    url: "../order/confirm",
                    contentType: "application/json",
                    params: JSON.stringify(id),
                    successCallback: function (r) {
                        if (r.code == 0) {
                            alert('操作成功', function (index) {
                                vm.reload();
                            });
                        } else {
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        saveOrUpdate: function (event) {
            Ajax.request({
                type: "POST",
                url: "../order/sendGoods",
                contentType: "application/json",
                params: JSON.stringify(vm.order),
                successCallback: function (r) {
                    vm.reload();
                }
            });
        },
        reload: function (event) {
            vm.showList = true;
            vm.detail = false;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {
                    'orderSn': vm.q.orderSn,
                    'orderStatus': vm.q.orderStatus,
                    'goodsType': vm.q.goodsType,
                    'addTime': vm.q.addTime,
                    'endTime': vm.q.endTime
                },
                page: page
            }).trigger("reloadGrid");
        },
        lookDetail: function (rowId) { //第三步：定义编辑操作
            vm.detail = true;
            vm.title = "详情";
            Ajax.request({
                url: "../order/info/" + rowId,
                async: true,
                successCallback: function (r) {
                    vm.order = r.order;
                }
            });
        },
        printDetail: function (rowId) {
            openWindow({
                type: 2,
                title: '<i class="fa fa-print"></i>打印票据',
                content: '../shop/orderPrint.html?orderId=' + rowId
            })
        }
    },
    created: function () {
        var vue = this;
        Ajax.request({
            url: "../shipping/queryAll",
            async: true,
            successCallback: function (r) {
                vue.shippings = r.list;
            }
        });
    }
});