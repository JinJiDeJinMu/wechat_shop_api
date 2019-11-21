$(function () {
    $("#jqGrid").Grid({
        url: '../attributecategory/list',
        colModel: [
            {label: 'id', name: 'id', index: 'id', key: true, hidden: true},
            {label: '名称', name: 'name', index: 'name', width: 80},
            {label: '呈现类型', name: 'showStyle', index: 'showStyle', width: 80},
            {
                label: '展示位置', name: 'showPosition', index: 'showPosition', width: 80, formatter: function (value) {
                     if(value===0){
                        return "头部";
                    }else if(value===1){
                         return "中部";
                     }else {
                         return "不展示";
                     }
                }
            },
            {
                label: '图片',
                name: 'bannerUrl',
                index: 'bannerUrl',
                width: 80,
                formatter: function (value) {
                    return transImg(value);
                }
            },
            {
                label: '是否可用', name: 'enabled', index: 'enabled', width: 80, formatter: function (value, options, row) {
                    return value === 0 ?
                        '<span class="label label-danger">禁用</span>' :
                        '<span class="label label-success">启用</span>';
                }
            }]
    });
});

var vm = new Vue({
    el: '#rrapp',
    data: {
        showList: true,
        title: null,
        attributeCategory: {
            enabled: '1',
            bannerUrl: '',
            name: '',
            showStyle: '',
            showPosition: ''
        },
        ruleValidate: {
            name: [
                {required: true, message: '名称不能为空', trigger: 'blur'}
            ]
        },
        q: {
            name: ''
        },
        status: ''
    },
    methods: {
        query: function () {
            vm.reload();
        },
        add: function () {
            vm.showList = false;
            vm.title = "新增";
            vm.attributeCategory = {enabled: '1'};
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
            var url = vm.attributeCategory.id == null ? "../attributecategory/save" : "../attributecategory/update";
            if (vm.status) {
                vm.attributeCategory.enabled = '1';
            } else {
                vm.attributeCategory.enabled = '0';
            }
            Ajax.request({
                type: "POST",
                url: url,
                contentType: "application/json",
                params: JSON.stringify(vm.attributeCategory),
                successCallback: function () {
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
                    url: "../attributecategory/delete",
                    contentType: "application/json",
                    params: JSON.stringify(ids),
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
                url: "../attributecategory/info/" + id,
                async: true,
                successCallback: function (r) {
                    vm.attributeCategory = r.attributeCategory;
                    if (vm.attributeCategory.enabled == 1) {
                        vm.status = true;
                    } else {
                        vm.status = false;
                    }
                }
            });
        },
        reload: function (event) {
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {'name': vm.q.name},
                page: page
            }).trigger("reloadGrid");
            vm.handleReset('formValidate');
        },
        changeEnable: function () {
            if (vm.status) {
                vm.attributeCategory.enabled = 1;
            } else {
                vm.attributeCategory.enabled = 0;
            }
        },
        handleSuccess: function (res, file) {
            vm.attributeCategory.bannerUrl = file.response.url;
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
        handleSubmit: function (name) {
            handleSubmitValidate(this, name, function () {
                vm.saveOrUpdate()
            });
        },
        eyeImage: function () {
            var url = vm.attributeCategory.bannerUrl;
            eyeImage(url);
        },
        handleReset: function (name) {
            handleResetForm(this, name);
        }
    }
});