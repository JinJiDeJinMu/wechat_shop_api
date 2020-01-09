$(function () {
    $("#jqGrid").Grid({
        url: '../commentv2/list',
        colModel: [
            {label: 'id', name: 'id', index: 'id', key: true, hidden: true},
            {label: '订单id', name: 'orderNo', index: 'order_no', width: 80},
            {label: '商品id', name: 'goodId', index: 'good_id', width: 80},
            {label: '评价内容', name: 'content', index: 'content', width: 80},
            {
                label: '图片',
                name: 'commentPictureEntities',
                index: 'commentPictureEntities',
                width: 200,
                formatter: function (value) {
                    let pic = "";
                    for (var i = 0; i < value.length; i++) {
                        pic = pic + transImg(value[i].picUrl) + " ";
                    }
                    return pic;
                }
            },
            {
                label: '添加时间', name: 'createTime', index: 'create_time',
                width: 80, formatter: function (value) {
                    return transDate(value);
                }
            },
            {label: '等级', name: 'starLevel', index: 'star_level', width: 80},
            {label: '评论人', name: 'userId', index: 'user_id', width: 80},
            {
                label: '状态', name: 'status', index: 'status', width: 80, formatter: function (value) {
                    if (value == 0) {
                        return '<span class="label label-success">显示</span>';
                    }
                    return '<span class="label label-danger">隐藏</span>';
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
        nideshopCommentV2: {id: ''},
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
            vm.nideshopCommentV2 = {};
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
            let url = vm.nideshopCommentV2.id == null ? "../commentv2/save" : "../commentv2/update";
            Ajax.request({
                url: url,
                params: JSON.stringify(vm.nideshopCommentV2),
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
                    url: "../commentv2/delete",
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
                url: "../commentv2/info/" + id,
                async: true,
                successCallback: function (r) {
                    vm.nideshopCommentV2 = r.nideshopCommentV2;
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
        },
        toggleStatus: function (event) {
            var id = getSelectedRow("#jqGrid");
            if (id == null) {
                return;
            }
            vm.nideshopCommentV2.id = id;

            confirm('确定要切换状态？', function () {
                Ajax.request({
                    type: "POST",
                    url: "../commentv2/toggleStatus",
                    contentType: "application/json",
                    params: JSON.stringify(vm.nideshopCommentV2),
                    successCallback: function (r) {
                        alert('操作成功', function (index) {
                            vm.reload();
                        });
                    }
                });
            });
        }
    }
});