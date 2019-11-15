$(function () {
    $("#jqGrid").Grid({
        url: '../cdtmerchant/list',
        colModel: [
            {label: 'id', name: 'id', index: 'id', key: true, hidden: true},
            {label: '名称(数字、中文，英文(可混合，不可有特殊字符)，可修改)、不唯一', name: 'shopName', index: 'shop_name', width: 80},
            {label: '用户id', name: 'userId', index: 'user_id', width: 80},
            {label: '类型', name: 'shopType', index: 'shop_type', width: 80},
            {label: '简介(可修改)', name: 'intro', index: 'intro', width: 80},
            {label: '公告(可修改)', name: 'shopNotice', index: 'shop_notice', width: 80},
            {label: '店铺行业(餐饮、生鲜果蔬、鲜花等)', name: 'shopIndustry', index: 'shop_industry', width: 80},
            {label: '店长', name: 'shopOwner', index: 'shop_owner', width: 80},
            {label: '店铺绑定的手机(登录账号：唯一)', name: 'mobile', index: 'mobile', width: 80},
            {label: '店铺联系电话', name: 'tel', index: 'tel', width: 80},
            {label: '店铺所在纬度(可修改)', name: 'shopLat', index: 'shop_lat', width: 80},
            {label: '店铺所在经度(可修改)', name: 'shopLng', index: 'shop_lng', width: 80},
            {label: '店铺详细地址', name: 'shopAddress', index: 'shop_address', width: 80},
            {label: '店铺所在省份（描述）', name: 'province', index: 'province', width: 80},
            {label: '店铺所在城市（描述）', name: 'city', index: 'city', width: 80},
            {label: '店铺所在区域（描述）', name: 'area', index: 'area', width: 80},
            {label: '店铺省市区代码，用于回显', name: 'pcaCode', index: 'pca_code', width: 80},
            {label: '店铺logo(可修改)', name: 'shopLogo', index: 'shop_logo', width: 80},
            {label: '店铺相册', name: 'shopPhotos', index: 'shop_photos', width: 80},
            {label: '每天营业时间段(可修改)', name: 'openTime', index: 'open_time', width: 80},
            {label: '店铺状态(-1:未开通 0: 停业中 1:营业中)，可修改', name: 'shopStatus', index: 'shop_status', width: 80},
            {label: '0:商家承担运费; 1:买家承担运费', name: 'transportType', index: 'transport_type', width: 80},
            {label: '固定运费', name: 'fixedFreight', index: 'fixed_freight', width: 80},
            {label: '满X包邮', name: 'fullFreeShipping', index: 'full_free_shipping', width: 80},
            {label: '创建时间', name: 'createTime', index: 'create_time', width: 80},
            {label: '更新时间', name: 'updateTime', index: 'update_time', width: 80},
            {label: '分销开关(0:开启 1:关闭)', name: 'isDistribution', index: 'is_distribution', width: 80}]
    });
});

let vm = new Vue({
    el: '#rrapp',
    data: {
        showList: true,
        title: null,
        cdtMerchant: {},
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
        update: function (event) {
            let id = getSelectedRow("#jqGrid");
            if (id == null) {
                return;
            }
            vm.showList = false;
            vm.title = "修改";

            vm.getInfo(id)
        },
        saveOrUpdate: function (event) {
            let url = vm.cdtMerchant.id == null ? "../cdtmerchant/save" : "../cdtmerchant/update";
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
        }
    }
});