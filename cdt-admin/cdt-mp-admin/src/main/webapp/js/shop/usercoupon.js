$(function () {
    let url = '../usercoupon/list';
    $("#jqGrid").Grid({
        url: url,
        colModel: [
            {label: '优惠券id', name: 'couponId', index: 'coupon_id',width:80},
            {label: '会员', name: 'nickName', index: 'nickname', width: 80},
            {label: '优惠券', name: 'couponName', index: 'coupon_name', width: 80},
            {label: '数量', name: 'number', index: 'number', width: 80},
            {
                label: '类型', name: 'couponType', index: 'coupon_type', width: 40, formatter: function (value) {
                    if (value == 0) {
                        return '满减卷';
                    } else if (value == 1) {
                        return '折扣卷';
                    } else if (value == 2) {
                        return '现金抵用卷';
                    }
                    return '';
                }
            },
            {
                label: '领取时间', name: 'userTime', index: 'userTime', width: 80, formatter: function (value) {
                    return transDate(value);
                }
            },
            {
                label: '状态', name: 'status', index: 'status', width: 40, formatter: function (value) {
                    if (value == 0) {
                        return '正常';
                    } else if (value == 1) {
                        return '已用';
                    } else if (value == 3) {
                        return '过期';
                    }
                    return '';
                }
            }
           ]
    });
});

var vm = new Vue({
    el: '#rrapp',
    data: {
        showList: true,
        title: null,
        userCoupon: {},
        q: {
            userName: '',
            couponName: ''
        }
    },
    methods: {
        query: function () {
            vm.reload();
        },
        add: function () {
            vm.showList = false;
            vm.title = "新增";
            vm.userCoupon = {};
        },
        update: function (event) {
            var id = getSelectedRow("#jqGrid");
            if (id == null) {
                return;
            }
            vm.showList = false;
            vm.title = "修改";

            vm.getInfo(id)
        },
        saveOrUpdate: function (event) {
            var url = vm.userCoupon.id == null ? "../usercoupon/save" : "../usercoupon/update";

            Ajax.request({
                type: "POST",
                url: url,
                contentType: "application/json",
                params: JSON.stringify(vm.userCoupon),
                successCallback: function (r) {
                    alert('操作成功', function (index) {
                        vm.reload();
                    });
                }
            });
        },
        del: function (event) {
            var ids = getSelectedRows("#jqGrid");
            if (ids == null) {
                return;
            }

            confirm('确定要删除选中的记录？', function () {

                Ajax.request({
                    type: "POST",
                    url: "../usercoupon/delete",
                    contentType: "application/json",
                    params: JSON.stringify(ids),
                    successCallback: function (r) {
                        alert('操作成功', function (index) {
                            vm.reload();
                        });
                    }
                });
            });
        },
        getInfo: function (id) {
            Ajax.request({
                url: "../usercoupon/info/" + id,
                async: true,
                successCallback: function (r) {
                    vm.userCoupon = r.userCoupon;
                }
            });
        },
        reload: function (event) {
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {'userName': vm.q.userName, 'couponName': vm.q.couponName},
                page: page
            }).trigger("reloadGrid");
        }
    }
});