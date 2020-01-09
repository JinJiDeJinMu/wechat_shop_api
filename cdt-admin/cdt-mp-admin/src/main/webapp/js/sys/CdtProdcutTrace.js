$(function () {
    $('#goodsDesc').editable({
        inlineMode: false,
        alwaysBlank: true,
        height: '500px', //高度
        minHeight: '200px',
        language: "zh_cn",
        spellcheck: false,
        plainPaste: true,
        enableScript: false,
        imageButtons: ["floatImageLeft", "floatImageNone", "floatImageRight", "linkImage", "replaceImage", "removeImage"],
        allowedImageTypes: ["jpeg", "jpg", "png", "gif"],
        imageUploadURL: '../sys/oss/upload',
        imageUploadParams: {
            id: "edit"
        },
        imagesLoadURL: '../sys/oss/queryAll'
    })
});
const defaultModel = {
    id: null,
    name: null,
    mainImage: null,
    figureImage: null,
    qrcode: null,
    mpQrcode: null,
    originGoods: null,
    height: null,
    produceDateCode: null,
    expirationDate: null,
    storageCondition: null,
    platformBatchNumber: null,
    productBrief: null,
    accessCount: null,
    lastAccessTime: null,
    productionEnterpriseNo: null,
    responsibilityName: null,
    responsibilityAddress: null,
    manufacturerName: null,
    manufacturerAddress: null,
    distributorName: null,
    distributorAddress: null,
    distributorContact: null,
    materialInfo: null,
    materialCountryOrigin: null,
    revision: null,
    createdBy: null,
    createdTime: null,
    updatedBy: null,
    updatedTime: null,
    productDesc: null,
};
let vue = new Vue({
    el: '#app',
    data: {
        showList: true,
        title: null,
        uploadList: [],
        baseForm: {
            pageIndex: 0,
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
        }
    },
    methods: {
        handleView(name) {
            this.imgName = name;
            this.visible = true;
        },
        handleRemove(file) {
            // 从 upload 实例删除数据
            const fileList = this.uploadList;
            this.uploadList.splice(fileList.indexOf(file), 1);
        },
        handleSuccess(res, file) {
            // 因为上传过程为实例，这里模拟添加 url
            file.imgUrl = res.url;
            file.name = res.url;
            this.uploadList.add(file);
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
        handleSuccessMainImage: function (res, file) {
            this.baseForm.data.mainImage = file.response.url;
        },
        handleSuccessFigureImage: function (res, file) {
            this.baseForm.data.figureImage = file.response.url;
        },
        eyeImagePicUrl: function () {
            var url = this.baseForm.data.mainImage;
            eyeImage(url);
        },
        eyeImageListPicUrl: function () {
            var url = this.baseForm.data.figureImage;
            eyeImage(url);
        },
        eyeImage: function (e) {
            eyeImage($(e.target).attr('src'));
        },
        toUtf8: function (str) {
            var out, i, len, c;
            out = "";
            len = str.length;
            for (i = 0; i < len; i++) {
                c = str.charCodeAt(i);
                if ((c >= 0x0001) && (c <= 0x007F)) {
                    out += str.charAt(i);
                } else if (c > 0x07FF) {
                    out += String.fromCharCode(0xE0 | ((c >> 12) & 0x0F));
                    out += String.fromCharCode(0x80 | ((c >> 6) & 0x3F));
                    out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F));
                } else {
                    out += String.fromCharCode(0xC0 | ((c >> 6) & 0x1F));
                    out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F));
                }
            }
            return out;
        },
        useqrcode: function (id) {
            $("#code").empty();
            var content = this.toUtf8('https://www.chundengtai.com/product.html?id=' + id);
            $("#code" + id).qrcode({
                render: "canvas",
                width: 200,
                height: 200,
                text: content
            });
        },
        saveOrUpdate: function () {
            var url = this.baseForm.data.id == null ? "../cdtProdcutTrace/saveModel.json" : "../cdtProdcutTrace/updateModel.json";
            //var html=$('#goodsDesc').froalaEditor('html.get', true);
            this.baseForm.data.productDesc = $('#goodsDesc').editable('getHTML');
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
                            that.baseForm.data = Object.assign({}, defaultModel);
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
                url: "../cdtProdcutTrace/deleteModel.json",
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
                url: "../cdtProdcutTrace/getModel/" + id + ".json",
                async: true,
                successCallback: function (res) {
                    that.baseForm.data = res.data;
                    $('#goodsDesc').editable('setHTML', that.baseForm.data.productDesc);
                }
            });
        },
        getList: function () {
            this.listLoading = false;
            var params = this.baseForm;
            let that = this;
            Ajax.request({
                type: "POST",
                url: "../cdtProdcutTrace/list.json",
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
            if (this.operateType == null) {
                this.$message({
                    message: '请选择操作类型',
                    type: 'warning',
                    duration: 1000
                });
                return;
            }
            if (!this.checkSelected()) {
                return;
            }
            this.$confirm('是否要进行该批量操作?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
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
            this.title = "新增 ";
        },
        updateFun: function (id) {
            if (id == null) {
                return;
            }
            this.showList = false;
            this.title = "修改 ";
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
