$(function () {
    $("#jqGrid").Grid({
        url: '../coupon/list',
        colModel: [
            {label: 'id', name: 'id', index: 'id', key: true, hidden: true},
            {label: '优惠券名称', name: 'name', index: 'name', width: 60},
            {
                label: '类型', name: 'type', index: 'type', width: 40, formatter: function (value) {
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
                label: '适用范围', name: 'useType', index: 'use_type', width: 40, formatter: function (value) {
                    if (value == 0) {
                        return '全场通用';
                    } else if (value == 1) {
                        return '指定分类';
                    } else if (value == 2) {
                        return '指定商品';
                    }
                    return '';
                }
            },
            {label: '限领个数', name: 'perLimit', index: 'per_limit', width: 40},
            {label: '领取类型', name: 'memberLevel', index: 'member_level', width: 40,
                formatter: function (value) {
                    if (value == 0) {
                        return '无限制';
                    } else if (value == 1) {
                        return '会员';
                    } else{
                        return '其他';
                    }
                    return '';
                }
            },
            {label: '满多少', name: 'fullMoney', index: 'full_money', width: 40},
            {label: '减多少', name: 'reduceMoney', index: 'reduce_money', width: 40},
            {label: '折扣', name: 'discount', index: 'discount', width: 40},
            {label: '抵现金', name: 'offsetMoney', index: 'offset_money', width: 40},
            {label: '时间类型', name: 'timeType', index: 'time_type', width: 40,
                formatter: function (value) {
                    if (value == 0) {
                        return '起始时间';
                    } else if (value == 1) {
                        return '天数';
                    }
                    return '';
                }
            },
            {
                label: '开始时间', name: 'startDate', index: 'start_date', width: 70, formatter: function (value) {return transDate(value);}
            },
            {
                label: '结束时间', name: 'endDate', index: 'end_date', width: 70, formatter: function (value) {return transDate(value);
                }
            },
            {label: '几天有效', name: 'days', index: 'days', width: 40},
            {label: '发放数量', name: 'totalCount', index: 'total_count', width: 40}

          ]
    });
});
var ztree;
var setting = {
    data: {
        simpleData: {
            enable: true,
            idKey: "id",
            pIdKey: "parentId",
            rootPId: -1
        },
        key: {
            url: "nourl"
        }
    }
};
var vm = new Vue({
    el: '#rrapp',
    data: {
        showList: true,
        showCard: false,
        showGoods: false,
        title: null,
        coupon: {sendType: 0,imgUrl:""},
        ruleValidate: {
            name: [
                {required: true, message: '优惠券名称不能为空', trigger: 'blur'}
            ]
        },
        q: {
            name: ''
        },
        goods: '',
        user: [],
        users: [],
        selectData: {},
        sendSms: '',//是否发送短信
        goodcaes:[],
        categoryId:''
    },
    methods: {
        query: function () {
            vm.reload();
        },
        add: function () {
            vm.showList = false;
            vm.showCard = true;
            vm.showGoods = false;
            vm.title = "新增";
            vm.getCategory();

        },
        update: function (event) {
            var id = getSelectedRow("#jqGrid");
            if (id == null) {
                return;
            }
            vm.showList = false;
            vm.showCard = true;
            vm.showGoods = false;
            vm.title = "修改";

            vm.getInfo(id)
            vm.getCategory();
        },
        saveOrUpdate: function (event) {
            var url = vm.coupon.id == null ? "../coupon/save" : "../coupon/update";
            if(vm.coupon.timeType == 0){
                if (vm.coupon.startDate >= vm.coupon.endDate) {
                    alert("发放开始时间不能大于等于发放结束时间")
                    return false;
                }
                if (vm.coupon.startDate >= vm.coupon.endDate) {
                    alert("使用开始时间不能大于等于使用结束时间")
                    return false;
                }
            }
            vm.coupon.goods=vm.goods;
            vm.coupon.categoryId = vm.categoryId;
            console.log(vm.coupon);
            Ajax.request({
                type: "POST",
                url: url,
                contentType: "application/json",
                params: JSON.stringify(vm.coupon),
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
                    url: "../coupon/delete",
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
                url: "../coupon/info/" + id,
                async: true,
                successCallback: function (r) {
                    vm.coupon = r.coupon;
                }
            });
        },
        reload: function (event) {
            vm.showList = true;
            vm.showCard = false;
            vm.showGoods = false;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {'name': vm.q.name},
                page: page
            }).trigger("reloadGrid");
            vm.handleReset('formValidate');
        },
        getCategory: function () {
            //加载分类树
            Ajax.request({
                url: "../category/getCategorySelect",
                async: true,
                successCallback: function (r) {
                    vm.goodcaes = r.list;
                }
            });
        },
        categoryTree: function () {
            openWindow({
                title: "选择类型",
                area: ['300px', '450px'],
                content: jQuery("#categoryLayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    var node = ztree.getSelectedNodes();
                    //选择上级菜单
                    vm.goodcaes.categoryId = node[0].id;
                    vm.goodcaes.categoryName = node[0].name;

                    layer.close(index);
                }
            });
        },
        handleSubmit: function (name) {
            handleSubmitValidate(this, name, function () {
                vm.saveOrUpdate()
            });
        },
        handleReset: function (name) {
            handleResetForm(this, name);
        },
        publish: function (id, sendType) {
            vm.showGoods = true;
            vm.goods = [];
            vm.user = [];
            vm.getGoodss();
            vm.getUsers();
            vm.selectData = {id: id, sendType: sendType};
            vm.sendSms = false;
            openWindow({
                title: "发放",
                area: ['600px', '350px'],
                content: jQuery("#sendDiv")
            })
        },
        getUsers: function () {
            Ajax.request({
                url: "../user/queryAll",
                async: true,
                successCallback: function (r) {
                    vm.users = r.list;
                }
            });
        },
        publishSubmit: function () {

            var sendType = vm.selectData.sendType;
            if (sendType == 1 && vm.user.length == 0) {
                vm.$Message.error('请选择下发会员');
                return;
            }
            if (sendType == 3 && vm.goods.length == 0) {
                vm.$Message.error('请选择下发商品');
                return;
            }
            confirm('确定下发优惠券？', function () {
                Ajax.request({
                    type: "POST",
                    dataType: 'json',
                    url: "../coupon/publish",
                    contentType: "application/json",
                    params: JSON.stringify({
                        sendType: vm.selectData.sendType,
                        couponId: vm.selectData.id,
                        goodsIds: vm.goods.toString(),
                        userIds: vm.user.toString(),
                        sendSms: vm.sendSms
                    }),
                    successCallback: function (r) {
                        alert('操作成功', function (index) {
                            vm.reload();
                            vm.showGoods = false;
                            vm.showList = true;
                        });
                    }
                });
            });
        },
        getGoodss: function () {
            Ajax.request({
                url: "../goods/queryAll/",
                async: true,
                successCallback: function (r) {
                    vm.goodss = r.list;
                }
            });
        },
        handleSuccess(res, file) {
            // 因为上传过程为实例，这里模拟添加 url
            file.imgUrl = res.url;
            file.name = res.url;
            vm.uploadList.add(file);
        },
        handleBeforeUpload() {
            const check = this.uploadList.length < 5;
            if (!check) {
                this.$Notice.warning({
                    title: '最多只能上传 5 张图片。'
                });
            }
            return check;
        },
        handleSubmit: function (name) {
            handleSubmitValidate(this, name, function () {
                vm.saveOrUpdate()
            });
        },
        handleFormatError: function (file) {
            this.$Notice.warning({
                title: '文件格式不正确',
                desc: '文件 ' + file.name + ' 格式不正确，请上传 jpg 或 png 格式的图片。'
            });
        },
        handleMaxSize: function (file) {
            this.$Notice.warning({
                title: '超出文件大小限制',
                desc: '文件 ' + file.name + ' 太大，不能超过 2M。'
            });
        },
        handleReset: function (name) {
            handleResetForm(this, name);
        },
        handleSuccessPicUrl: function (res, file) {
            vm.coupon.imgUrl = file.response.url;
        },
        eyeImagePicUrl: function () {
            var url = vm.coupon.imgUrl;
            eyeImage(url);
        },
        eyeImage: function (e) {
            eyeImage($(e.target).attr('src'));
        }
    }
});