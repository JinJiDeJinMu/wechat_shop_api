$(function () {
    let goodsId = getQueryString("goodsId");
    let url = '../goodsspecification/list';
    if (goodsId) {
        url += '?goodsId=' + goodsId;
    }
    $("#jqGrid").Grid({
        url: url,
        colModel: [{
            label: 'id',
            name: 'id',
            index: 'id',
            key: true,
            hidden: true
        },
            {
                label: '商品',
                name: 'goodsName',
                index: 'goods_id',
                width: 80
            },
            {
                label: '规格',
                name: 'specificationName',
                index: 'specification_id',
                width: 80
            },
            {
                label: '规格说明',
                name: 'value',
                index: 'value',
                width: 80
            },
            {
                label: '规格图片',
                name: 'picUrl',
                index: 'pic_url',
                width: 80,
                formatter: function (value) {
                    return transImg(value);
                }
            }
        ]
    });
});

Vue.use(window.AVUE);
Vue.use(httpVueLoader);
const tableOption = {
    border: true,
    index: true,
    indexLabel: '序号',
    stripe: true,
    menuAlign: 'center',
    menuWidth: 350,
    align: 'center',
    refreshBtn: true,
    searchSize: 'mini',
    addBtn: false,
    editBtn: false,
    viewBtn: false,
    delBtn: false,
    props: {
        label: 'label',
        value: 'value'
    },
    column: [{
        label: '属性ID',
        prop: 'propId'
    }, {
        label: '属性名称',
        prop: 'propName',
        search: true
    }, {
        label: '属性值',
        prop: 'prodPropValues',
        slot: true
    }]
};
var vm = new Vue({
    el: '#app',
    data: {
        showList: true,
        title: null,
        goodsSpecification: {},
        ruleValidate: {
            name: [{
                required: true,
                message: '名称不能为空',
                trigger: 'blur'
            }]
        },
        q: {
            name: ''
        },
        goodss: [],
        specifications: [],

        dataForm: {
            prodProp: ''
        },
        dataList: [],
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        dataListLoading: false,
        dataListSelections: [],
        addOrUpdateVisible: false,

        page: {
            total: 0, // 总页数
            currentPage: 1, // 当前页数
            pageSize: 10 // 每页显示多少条
        },

        border: true,
        index: true,
        indexLabel: '序号',
        stripe: true,
        menuAlign: 'center',
        menuWidth: 350,
        align: 'center',
        refreshBtn: true,
        searchSize: 'mini',
        addBtn: false,
        editBtn: false,
        viewBtn: false,
        delBtn: false,
        props: {
            label: 'label',
            value: 'value'
        },
        column: [{
            label: '属性ID',
            prop: 'propId'
        }, {
            label: '属性名称',
            prop: 'propName',
            search: true
        }, {
            label: '属性值',
            prop: 'prodPropValues',
            slot: true
        }],
        tableOption: tableOption
    },
    components: {
        'AddOrUpdate': 'url:../js/vue/spec-add-or-update.vue',
        // 将组建加入组建库
        'my-component': 'url:../js/vue/ao.vue'
    },
    methods: {
        // 获取数据列表
        getDataList(page, params) {
            this.dataListLoading = true
            this.$http({
                url: this.$http.adornUrl('/prod/spec/page'),
                method: 'get',
                params: this.$http.adornParams(
                    Object.assign({
                            current: page == null ? this.page.currentPage : page.currentPage,
                            size: page == null ? this.page.pageSize : page.pageSize
                        },
                        params
                    )
                )
            }).then(({
                         data
                     }) => {
                this.dataList = data.records
                this.page.total = data.total
                this.dataListLoading = false
            })
        },
        // 新增 / 修改
        addOrUpdateHandle(val) {
            this.addOrUpdateVisible = true
            this.$nextTick(() => {
                this.$refs.addOrUpdate.init(val)
            })
        },
        // 删除
        deleteHandle(id) {
            var ids = id ?
                [id] :
                this.dataListSelections.map(item => {
                    return item.propId
                })
            this.$confirm(`确定进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            })
                .then(() => {
                    this.$http({
                        url: this.$http.adornUrl(`/prod/spec/${ids}`),
                        method: 'delete',
                        data: this.$http.adornData(ids, false)
                    }).then(({
                                 data
                             }) => {
                        this.$message({
                            message: '操作成功',
                            type: 'success',
                            duration: 1500,
                            onClose: () => {
                                this.getDataList(this.page)
                            }
                        })
                    })
                })
                .catch(() => {
                })
        },
        searchChange(params) {
            this.getDataList(this.page, params)
        },
        getSpecification: function () {
            Ajax.request({
                url: "../specification/queryAll",
                async: true,
                successCallback: function (r) {
                    vm.specifications = r.list;
                }
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
        query: function () {
            vm.reload();
        },
        add: function () {
            vm.showList = false;
            vm.title = "新增";
            vm.goodsSpecification = {};
            vm.getSpecification();
            vm.getGoodss();
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
            var url = vm.goodsSpecification.id == null ? "../goodsspecification/save" : "../goodsspecification/update";

            Ajax.request({
                type: "POST",
                url: url,
                contentType: "application/json",
                params: JSON.stringify(vm.goodsSpecification),
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
                    url: "../goodsspecification/delete",
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
                url: "../goodsspecification/info/" + id,
                async: true,
                successCallback: function (r) {
                    vm.goodsSpecification = r.goodsSpecification;
                    vm.getSpecification();
                    vm.getGoodss();
                }
            });
        },
        reload: function (event) {
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {
                    'name': vm.q.name
                },
                page: page
            }).trigger("reloadGrid");
            vm.handleReset('formValidate');
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
        handleSuccess: function (res, file) {
            this.$set(vm.goodsSpecification, 'picUrl', file.response.url)
        },
        eyeImage: function () {
            var url = vm.goodsSpecification.picUrl;
            eyeImage(url);
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
