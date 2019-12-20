$(function () {
    $("#jqGrid").Grid({
        url: '../cdtdistridetail/list',
        colModel: [
            {label: 'id', name: 'id', index: 'id', key: true, hidden: true},
            {label: '用户的id', name: 'userId', index: 'user_id', width: 80},
            {label: '得钱的用户id', name: 'goldUserId', index: 'gold_user_id', width: 80},
            {label: '订单号', name: 'orderSn', index: 'order_sn', width: 80},
            {label: '得到的钱', name: 'money', index: 'money', width: 80},
            {label: '校验token', name: 'token', index: 'token', width: 80},
            {label: '创建时间', name: 'createdTime', index: 'created_time', width: 80}]
    });
});

let vm = new Vue({
    el: '#rrapp',
    data: {
        showList: true,
        title: null,
        cdtDistridetail: {},
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
            vm.cdtDistridetail = {};
        },
        update: function (event) {
            let id = getSelectedRow("#jqGrid");
            if (id == null) {
                return;
            }
            vm.showList = false;
            vm.title = "修改";

            vm.getInfo(id);
        },
        saveOrUpdate: function (event) {
            let url = vm.cdtDistridetail.id == null ? "../cdtdistridetail/save" : "../cdtdistridetail/update";
            Ajax.request({
                url: url,
                params: JSON.stringify(vm.cdtDistridetail),
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
                    url: "../cdtdistridetail/delete",
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
                url: "../cdtdistridetail/info/" + id,
                async: true,
                successCallback: function (r) {
                    vm.cdtDistridetail = r.cdtDistridetail;
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
            };
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