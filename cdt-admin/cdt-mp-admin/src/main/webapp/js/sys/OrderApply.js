const defaultModel = {
    id:null,
    orderSn:null,
    merchantId:null,
    merchantName:null,
    money:null,
    userId:null,
    wxopenId:null,
    userName:null,
    applyTime:null,
    endTime:null,
    status:null,
    remarks:null,
};
let vue = new Vue({
    el: '#app',
    data: {
        showList: true,
        title: null,
        baseForm: {
            pageIndex: 1,
            pageSize: 10,
            sortField: 'id',
            order: 'desc',
            data: Object.assign({}, defaultModel)
        },
        model: Object.assign({}, defaultModel),
        ruleValidate: {},
        operates: [{
            label: "上架",
            value: "publishOn"
        },
            {
                label: "下架",
                value: "publishOff"
            },
            {
                label: "移入回收站",
                value: "recycle"
            }
        ],
        operateType: null,
        list: null,
        total: null,
        listLoading: false,
        multipleSelection: [],
        publishStatusOptions: [{
            value: 1,
            label: '上架'
        }, {
            value: 0,
            label: '下架'
        }],
        verifyStatusOptions: [{
            value: 1,
            label: '审核通过'
        }, {
            value: 0,
            label: '未审核'
        }]
    },
    created: function () {
        this.getList();
    },
    filters: {
        dateFormat: function (time) {
            var date = new Date(time);
            var year = date.getFullYear();
            var month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
            var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
            var hours = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
            var minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
            var seconds = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
            return year + "-" + month + "-" + day + " " + hours + ":" + minutes + ":" + seconds;
        },
        formatPayType(value) {
            if (value === 1) {
                return '支付宝';
            } else if (value === 2) {
                return '微信';
            } else {
                return '未支付';
            }
        },
        type(value) {
            if (value === 0) {
                return '未审核';
            } else if (value === 1) {
                return '审核通过';
            } else if(value === 2){
                return '审核失败';
            }
        }
    },
    methods: {

        saveOrUpdate: function (event) {
            var url = this.baseForm.data.id == null ? "../orderApply/saveModel.json" : "../orderApply/updateModel.json";
            var params = JSON.stringify(this.baseForm.data);
            let that = this;
            Ajax.request({
                type: "POST",
                url: url,
                contentType: "application/json",
                params: params,
                successCallback: function (res) {
                    if (res.data) {
                        alert('操作成功', function (index) {
                            that.reload();
                        });
                    }
                }
            });
        },
        deleteModel: function (ids) {
            if (ids == null) {
                return;
            }
            let that = this;
            Ajax.request({
                type: "POST",
                url: "../orderApply/deleteModel.json",
                contentType: "application/json",
                params: JSON.stringify(ids),
                successCallback: function (res) {
                    alert('操作成功', function (index) {
                        that.reload();
                    });
                }
            });
        },
        getModel: function (id) {
            let that = this;
            Ajax.request({
                url: "../orderApply/getModel/" + id + ".json",
                async: true,
                successCallback: function (res) {
                    that.baseForm.data = res.data;
                }
            });
        },
        reviewModel: function (ids) {
            if (ids == null) {
                return;
            }
            let that = this;
            Ajax.request({
                type: "POST",
                url: "../orderApply/reviewModel.json",
                contentType: "application/json",
                params: JSON.stringify(ids),
                successCallback: function (res) {
                    alert('操作成功', function (index) {
                        this.handleResetSearch();
                        that.reload();
                    });
                }
            });
        },
        getList: function () {
            this.listLoading = false;
            var params = this.baseForm;
            let that = this;
            Ajax.request({
                type: "POST",
                url: "../orderApply/list.json",
                contentType: "application/json",
                params: JSON.stringify(params),
                async: true,
                successCallback: function (res) {
                    that.listLoading = false;
                    console.log(res);
                    that.list = res.data.list;
                    that.total = res.data.total;
                }
            });
        },
        getSelectRowIds() {
            let ids = [];
            for (let i = 0; i < this.multipleSelection.length; i++) {
                ids.push(this.multipleSelection[i].id);
            }
            return ids;
        },
        checkSelected: function () {
            if (this.multipleSelection == null || this.multipleSelection.length < 1) {
                this.$message({
                    message: '请选择要操作的数据',
                    type: 'warning',
                    duration: 1000
                });
                return false;
            }
            return true;
        },
        handleBatchOperate() {
            if (!this.checkSelected()) {
                return;
            }
            this.$confirm('是否要进行审核操作?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                var idlist = this.getSelectRowIds();
                this.reviewModel(idlist);

                this.getList();
            })
            ;
        },
        handleResetSearch: function () {
            this.baseForm.data = Object.assign({}, defaultModel);
            this.getList();
        },
        handleSizeChange(val) {
            this.baseForm.pageIndex = 1;
            this.baseForm.pageSize = val;
            this.getList();
        },
        handleCurrentChange(val) {
            this.baseForm.pageIndex = val;
            this.getList();
        },
        handleSelectionChange(val) {
            this.multipleSelection = val;
        },
        handleDelete(index, row) {
            let ids = [];
            ids.push(row.id);
            this.deleteModel(ids);
        },
        handlePublishStatusChange(index, row) {
            let ids = [];
            ids.push(row.id);
            console.log(JSON.stringify(row));
            //this.updatePublishStatus(row.status, ids);
        },
        handleUpdateProduct(index, row) {
            this.updateFun(row.id);
        },
        updatePublishStatus: function (status, ids) {
            console.log(status);
        },
        updateDeleteStatus(deleteStatus, ids) {
            let params = new URLSearchParams();
            params.append('ids', ids);
            params.append('deleteStatus', deleteStatus);
            this.deleteModel(ids);
        },
        reload: function (event) {
            this.showList = true;
            this.getList();
            this.handleReset('formValidate');
        },
        add: function () {
            this.showList = false;
            this.title = "新增";
        },
        updateFun: function (id) {
            if (id == null) {
                return;
            }
            this.showList = false;
            this.title = "修改";
            this.getModel(id);
        },
        dataFormSubmit: function (name) {
            handleSubmitValidate(this, name, function () {
                vue.saveOrUpdate()
            });
        },
    },
    mounted() {
    }
});
