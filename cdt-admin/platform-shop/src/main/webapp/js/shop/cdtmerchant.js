$(function () {
    $("#jqGrid").Grid({
        url: '../cdtmerchant/list',
        colModel: [
            {label: 'id', name: 'id', index: 'id', key: true, hidden: true},
            {label: '名称', name: 'shopName', index: 'shop_name', width: 80},
            // {label: '用户id', name: 'userId', index: 'user_id', width: 80},
            {label: '类型', name: 'shopType', index: 'shop_type', width: 50},
            // {label: '简介(可修改)', name: 'intro', index: 'intro', width: 80},
            // {label: '公告(可修改)', name: 'shopNotice', index: 'shop_notice', width: 80},
            // {label: '店铺行业(餐饮、生鲜果蔬、鲜花等)', name: 'shopIndustry', index: 'shop_industry', width: 80},
            {label: '店长', name: 'shopOwner', index: 'shop_owner', width: 80},
            {label: '绑定的手机', name: 'mobile', index: 'mobile', width: 80},
            {label: '联系电话', name: 'tel', index: 'tel', width: 80},
            // {label: '店铺所在纬度(可修改)', name: 'shopLat', index: 'shop_lat', width: 80},
            // {label: '店铺所在经度(可修改)', name: 'shopLng', index: 'shop_lng', width: 80},
            {label: '详细地址', name: 'shopAddress', index: 'shop_address', width: 80},
            // {label: '店铺所在省份（描述）', name: 'province', index: 'province', width: 80},
            // {label: '店铺所在城市（描述）', name: 'city', index: 'city', width: 80},
            // {label: '店铺所在区域（描述）', name: 'area', index: 'area', width: 80},
            // {label: '店铺省市区代码，用于回显', name: 'pcaCode', index: 'pca_code', width: 80},
            {
                label: '店铺logo', name: 'shopLogo', index: 'shop_logo', width: 80, formatter: function (value) {
                    return transImg(value);
                }
            },
            {
                label: '店铺相册', name: 'shopPhotos', index: 'shop_photos', width: 80, formatter: function (value) {
                    return transImg(value);
                }
            },
            {label: '营业时间段', name: 'openTime', index: 'open_time', width: 80},
            {
                label: '店铺状态',
                name: 'shopStatus',
                index: 'shop_status',
                width: 80,
                formatter: function (value) {
                    if (value === -1) {
                        return "未开通";
                    } else if (value === 0) {
                        return "停业中";
                    } else {
                        return "营业中";
                    }
                }
            },
            {
                label: '运费', name: 'transportType', index: 'transport_type', width: 50,
                formatter: function (value) {
                    if (value === 0) {
                        return "商家承担运费";
                    } else {
                        return "买家承担运费";
                    }
                }
            },
            {label: '固定运费', name: 'fixedFreight', index: 'fixed_freight', width: 80},
            {label: '满X包邮', name: 'fullFreeShipping', index: 'full_free_shipping', width: 80},
            // {label: '创建时间', name: 'createTime', index: 'create_time', width: 80},
            // {label: '更新时间', name: 'updateTime', index: 'update_time', width: 80},
            {
                label: '分销开关', name: 'isDistribution', index: 'is_distribution', width: 80,
                formatter: function (value) {
                    if (value === 0) {
                        return "开启";
                    } else {
                        return "关闭";
                    }
                }
            },
            {
                label: '提现功能', name: 'cashStatus', index: 'cash_status', width: 80,
                formatter: function (value) {
                    if (value === 0) {
                        return "未开通";
                    } else if (value === 1) {
                        return "已开通";
                    } else if (value === 2) {
                        return "已冻结";
                    } else {
                        return "其他";
                    }
                }
            }
        ]
    });
});

let vm = new Vue({
    el: '#rrapp',
    data: {
        showList: true,
        title: null,
        cdtMerchant: {
            shopLogo: '',
            shopPhotos: ''
        },
        ruleValidate: {
            name: [
                {required: true, message: '名称不能为空', trigger: 'blur'}
            ]
        },
        q: {
            name: ''
        }
    },
    methods: {
        query: function () {
            vm.reload();
        },
        add: function () {
            vm.showList = false;
            vm.title = "新增";
            vm.cdtMerchant = {};
        },
        open: function () {
            var id = getSelectedRow("#jqGrid");
            if (id == null) {
                return;
            }
            Ajax.request({
                url: "../cdtmerchant/open/" + id +"/1",
                async: true,
                successCallback: function (r) {
                    if(r.code ==0){
                        alert("开通成功");
                    }
                    vm.reload();
                }
            });
        },
        close: function () {
            var id = getSelectedRow("#jqGrid");
            if (id == null) {
                return;
            }
            Ajax.request({
                url: "../cdtmerchant/open/" + id +"/2",
                async: true,
                successCallback: function (r) {
                    if(r.code ==0){
                        alert("冻结成功");
                    }
                    vm.reload();
                }
            });
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
            var url = vm.cdtMerchant.id == null ? "../cdtmerchant/save" : "../cdtmerchant/update";
            Ajax.request({
                url: url,
                params: JSON.stringify(vm.cdtMerchant),
                type: "POST",
                contentType: "application/json",
                successCallback: function (r) {
                    alert('操作成功', function (index) {
                        vm.reload();
                    });
                }
            });
        },
        del: function (event) {
            let ids = getSelectedRows("#jqGrid");
            if (ids == null) {
                return;
            }

            confirm('确定要删除选中的记录？', function () {
                Ajax.request({
                    url: "../cdtmerchant/delete",
                    params: JSON.stringify(ids),
                    type: "POST",
                    contentType: "application/json",
                    successCallback: function () {
                        alert('操作成功', function (index) {
                            vm.reload();
                        });
                    }
                });
            });
        },
        getInfo: function (id) {
            Ajax.request({
                url: "../cdtmerchant/info/" + id,
                async: true,
                successCallback: function (r) {
                    vm.cdtMerchant = r.cdtMerchant;
                }
            });
        },
        reload: function (event) {
            vm.showList = true;
            let page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {'name': vm.q.name},
                page: page
            }).trigger("reloadGrid");
            vm.handleReset('formValidate');
        },
        reloadSearch: function () {
            vm.q = {
                name: ''
            }
            vm.reload();
        },
        handleSubmit: function (name) {
            handleSubmitValidate(this, name, function () {
                vm.saveOrUpdate()
            });
        },
        handleReset: function (name) {
            handleResetForm(this, name);
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
        handleSuccessPicUrl: function (res, file) {
            vm.cdtMerchant.shopLogo = file.response.url;
        },
        handleSuccessListPicUrl: function (res, file) {
            vm.cdtMerchant.shopPhotos = file.response.url;
        },
        eyeImagePicUrl: function () {
            var url = vm.cdtMerchant.shopLogo;
            eyeImage(url);
        },
        eyeImageListPicUrl: function () {
            var url = vm.cdtMerchant.shopPhotos;
            eyeImage(url);
        },
        eyeImage: function (e) {
            eyeImage($(e.target).attr('src'));
        }
    }
});