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
var ztree;
const defaultListQuery = {
    limit: 10,
    page: 1,
    sidx: '',
    order: 'asc',
    name: '',
    goodSn: ''
};
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
        title: null,

        goods: {
            primaryPicUrl: '',
            listPicUrl: '',
            categoryId: '',
            isOnSale: 1,
            isNew: 0,
            isAppExclusive: 0,
            brokerage_percent: 0,
            isLimited: 0,
            isHot: 0,
            retailPrice: 0,
            marketPrice: 0,
            goodsNumber: 0,
            isSecKill: "1",
            successPeople: 0,
            successTime: 0,
            groupPrice: 1,
            categoryName: '',
            purchaseType: 1,
            merchantId: ''
        },
        ruleValidate: {
            name: [{
                required: true,
                message: '名称不能为空',
                trigger: 'blur'
            }],
            goodsSn: [{
                required: true,
                message: '序列号不能为空',
                trigger: 'blur'
            }],
            listPicUrl: [{
                required: true,
                message: '首页列表页图片不能为空',
                trigger: 'blur'
            }],
            primaryPicUrl: [{
                required: true,
                message: '产品主图不能为空',
                trigger: 'blur'
            }]
        },


        editSkuInfo: {
            dialogVisible: false,
            productId: null,
            productSn: '',
            productAttributeCategoryId: null,
            stockList: [],
            productAttr: [],
            keyword: null
        },
        operates: [{
            label: "商品上架",
            value: "publishOn"
        },
            {
                label: "商品下架",
                value: "publishOff"
            },
            {
                label: "设为新品",
                value: "newOn"
            },
            {
                label: "取消新品",
                value: "newOff"
            },
            {
                label: "移入回收站",
                value: "recycle"
            }
        ],
        operateType: null,
        listQuery: Object.assign({}, defaultListQuery),
        list: null,
        total: null,
        listLoading: false,
        selectProductCateValue: null,
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
        formatPayType(value) {
            if (value === 1) {
                return '支付宝';
            } else if (value === 2) {
                return '微信';
            } else {
                return '未支付';
            }
        },
        formatSourceType(value) {
            if (value === 1) {
                return 'APP订单';
            } else {
                return 'PC订单';
            }
        },
        formatGoodsType(value) {
            if (value === 1) {
                return '普通商品';
            } else if (value === 2) {
                return '秒杀';
            } else if (value === 3) {
                return '团购';
            } else if (value === 4) {
                return '砍价';
            } else if (value === 5) {
                return '快递代取';
            } else if (value === 6) {
                return '核销商品';
            } else {
                return '普通';
            }
        },
    },
    methods: {
        dateFormat: function (time) {
            var date = new Date(time.addTime);
            var year = date.getFullYear();
            var month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
            var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
            var hours = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
            var minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
            var seconds = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
            // 拼接
            return year + "-" + month + "-" + day + " " + hours + ":" + minutes + ":" + seconds;
        },
        getList: function () {
            this.listLoading = false;
            Ajax.request({
                url: "../goods/list?limit=" + this.listQuery.limit + "&page=" + this.listQuery.page + "&sidx=" + this.listQuery
                    .sidx + "&name=" + this.listQuery.name + "&order=" + this.listQuery.order,
                async: true,
                successCallback: function (r) {
                    console.log(r)
                    vm.listLoading = false;
                    vm.list = r.page.list;
                    vm.total = r.page.totalCount;
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
            }).then(() => {
                let ids = this.getSelectRowIds();
                switch (this.operateType) {
                    case this.operates[0].value:
                        this.updatePublishStatus(1, ids);
                        break;
                    case this.operates[1].value:
                        this.updatePublishStatus(0, ids);
                        break;
                    case this.operates[2].value:
                        this.updateNewStatus(1, ids);
                        break;
                    case this.operates[3].value:
                        this.updateNewStatus(0, ids);
                        break;
                    case this.operates[4].value:
                        this.updateDeleteStatus(1, ids);
                        break;
                    default:
                        break;
                }
                this.getList();
            });
        },
        handleSizeChange(val) {
            this.listQuery.page = 1;
            this.listQuery.limit = val;
            this.getList();
        },
        handleCurrentChange(val) {
            this.listQuery.page = val;
            this.getList();
        },
        handleSelectionChange(val) {
            this.multipleSelection = val;
        },
        handleDelete(index, row) {
            this.$confirm('是否要进行删除操作?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                let ids = [];
                ids.push(row.id);
                this.del(ids);
            });
        },
        handlePublishStatusChange(index, row) {
            let ids = [];
            ids.push(row.id);
            console.log(JSON.stringify(row))

            this.updatePublishStatus(row.isOnSale, ids);

        },
        handleNewStatusChange(index, row) {
            let ids = [];
            ids.push(row.id);
            this.updateNewStatus(row.newStatus, ids);
        },
        handleSelectionChange: function (val) {
            vm.multipleSelection = val;
        },
        updatePublishStatus: function (status, ids) {
            console.log(status);
            console.log(ids);
            if (status == 1) {
                this.$confirm('确定要上架选中的商品?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    ids.forEach(this.enSale)
                });
            } else {
                this.$confirm('确定要下架选中的商品?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    ids.forEach(this.unSale)
                });
            }
        },
        updateNewStatus: function (status, ids) {
            console.log(status)
            console.log(ids)
            if (status == 1) {
                ids.forEach(this.enSale)
            } else {
                ids.forEach(this.unSale)
            }
        },
        handleUpdateProduct(index, row) {
            this.updateFun(row.id);
        },
        updateDeleteStatus(deleteStatus, ids) {
            let params = new URLSearchParams();
            params.append('ids', ids);
            params.append('deleteStatus', deleteStatus);
            this.del(ids);
        },
        enSale: function (item, index) {
            if (item == null) {
                item = getSelectedRow("#jqGrid");
                return;
            }

            Ajax.request({
                type: "POST",
                url: "../goods/enSale",
                params: JSON.stringify(item),
                contentType: "application/json",
                type: 'POST',
                successCallback: function () {
                    alert('提交成功', function (index) {
                        vm.reload();
                    });
                }
            });
        },
        openSpe: function () {
            var id = getSelectedRow("#jqGrid");
            if (id == null) {
                return;
            }
            openWindow({
                type: 2,
                title: '商品规格',
                content: '../shop/goodsspecification.html?goodsId=' + id
            })
        },
        openPro: function () {
            var id = getSelectedRow("#jqGrid");
            if (id == null) {
                return;
            }
            openWindow({
                type: 2,
                title: '产品设置',
                content: '../shop/product.html?goodsId=' + id
            });
        },
        unSale: function (item, index) {
            if (item == null) {
                item = getSelectedRow("#jqGrid");
                return;
            }
            this.$confirm('确定要下架选中的商品?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                Ajax.request({
                    type: "POST",
                    url: "../goods/unSale",
                    contentType: "application/json",
                    params: JSON.stringify(item),
                    successCallback: function (r) {
                        alert('操作成功', function (index) {
                            vm.reload();
                            ;
                        });
                    }
                });
            });
        },
        batDel: function () {
            if (!this.checkSelected()) {
                return;
            }
            let ids = [];
            for (let i = 0; i < this.multipleSelection.length; i++) {
                ids.push(this.multipleSelection[i].id);
            }
            console.log(JSON.stringify(ids));
            this.del(ids);
        },

        del: function (ids) {
            if (ids == null) {
                return;
            }
            Ajax.request({
                type: "POST",
                url: "../goods/delete",
                contentType: "application/json",
                params: JSON.stringify(ids),
                successCallback: function (r) {
                    alert('操作成功', function (index) {
                        vm.reload();
                    });
                }
            });
        },
        getInfo: function (id) {
            Ajax.request({
                url: "../goods/info/" + id,
                async: true,
                successCallback: function (r) {
                    vm.goods = r.goods;
                    vm.goods.isSecKill = r.goods.isSecKill + "";
                    $('#goodsDesc').editable('setHTML', vm.goods.goodsDesc);
                    vm.getCategory();
                }
            });
        },
        reload: function (event) {
            vm.showList = true;
            this.getList()
            vm.handleReset('formValidate');
        },
        query: function () {
            vm.reload();
        },
        add: function () {
            vm.showList = false;
            vm.title = "新增";
            vm.uploadList = [];
            vm.goods = {
                primaryPicUrl: '',
                listPicUrl: '',
                categoryId: '',
                isOnSale: 1,
                isNew: 1,
                isAppExclusive: 0,
                brokerage_percent: 0,
                isLimited: 0,
                isHot: 0,
                retailPrice: 0,
                marketPrice: 0,
                goodsNumber: 0,
                sellVolume: 0,
                isSecKill: 1,
                successPeople: 0,
                successTime: 0,
                groupPrice: 1,
                categoryName: '',
                purchaseType: 1,
                extraPrice: 0
            };
            $('#goodsDesc').editable('setHTML', '');
            vm.getCategory();
            vm.brands = [];
            vm.macros = [];
            vm.attributeCategories = [];
            vm.getBrands();
            vm.getMacro();
            vm.getAttributeCategories();
            vm.getCategories();
        },
        checkSelected: function () {
            if (this.multipleSelection == null || this.multipleSelection.length < 1) {
                this.$message({
                    message: '请选择要操作的商品',
                    type: 'warning',
                    duration: 1000
                });
                return false;
            }
            return true;
        },
        batUpdate: function (event) {
            if (!this.checkSelected()) {
                return;
            }
            id = this.multipleSelection[0].id;
            this.updateFun(id);
        },
        updateFun: function (id) {
            if (id == null) {
                return;
            }
            vm.showList = false;
            vm.title = "修改";
            vm.uploadList = [];
            vm.getInfo(id);
            vm.getBrands();
            vm.getMacro();
            vm.getAttributeCategories();
            vm.getCategories();
            vm.getGoodsGallery(id);
        },
        /**
         * 获取品牌
         */
        getBrands: function () {
            Ajax.request({
                url: "../brand/queryAll",
                async: true,
                successCallback: function (r) {
                    vm.brands = r.list;
                }
            });
        },
        /**
         * 获取单位
         */
        getMacro: function () {
            Ajax.request({
                url: "../sys/macro/queryMacrosByValue?value=goodsUnit",
                async: true,
                successCallback: function (r) {
                    vm.macros = r.list;
                }
            });
        },
        getGoodsGallery: function (id) { //获取商品顶部轮播图
            Ajax.request({
                url: "../goodsgallery/queryAll?goodsId=" + id,
                async: true,
                successCallback: function (r) {
                    vm.uploadList = r.list;
                }
            });
        },
        getAttributeCategories: function () {
            Ajax.request({
                url: "../attributecategory/queryAll",
                async: true,
                successCallback: function (r) {
                    vm.attributeCategories = r.list;
                }
            });
        },
        getCategories: function () {
            Ajax.request({
                url: "../category/getCategorySelect",
                async: true,
                successCallback: function (r) {
                    vm.categories = r.list;
                }
            });
        },
        saveOrUpdate: function (event) {
            if (vm.goods.goodsSn == '') {
                alert('序列号不能为空');
                return false;
            }
            if (vm.goods.name == '') {
                alert('产品名称不能为空');
                return false;
            }
            if (vm.goods.listPicUrl == '') {
                alert('商品列表图必须上传');
                return false;
            }
            if (vm.goods.primaryPicUrl == '') {
                alert('商品主图必须上传');
                return false;
            }
            var url = vm.goods.id == null ? "../goods/save" : "../goods/update";
            vm.goods.goodsDesc = $('#goodsDesc').editable('getHTML');
            vm.goods.goodsImgList = vm.uploadList;
            Ajax.request({
                type: "POST",
                url: url,
                contentType: "application/json",
                params: JSON.stringify(vm.goods),
                successCallback: function (r) {
                    alert('操作成功', function (index) {
                        vm.reload();
                    });
                }
            });
        },

        getCategory: function () {
            //加载分类树
            Ajax.request({
                url: "../category/queryAll",
                async: true,
                successCallback: function (r) {
                    ztree = $.fn.zTree.init($("#categoryTree"), setting, r.list);
                    var node = ztree.getNodeByParam("id", vm.goods.categoryId);
                    if (node) {
                        ztree.selectNode(node);
                        vm.goods.categoryName = node.name;
                    } else {
                        node = ztree.getNodeByParam("id", 0);
                        ztree.selectNode(node);
                        vm.goods.categoryName = node.name;
                    }
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
                    vm.goods.categoryId = node[0].id;
                    vm.goods.categoryName = node[0].name;

                    layer.close(index);
                }
            });
        },
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
            vm.goods.primaryPicUrl = file.response.url;
        },
        handleSuccessListPicUrl: function (res, file) {
            vm.goods.listPicUrl = file.response.url;
        },
        eyeImagePicUrl: function () {
            var url = vm.goods.primaryPicUrl;
            eyeImage(url);
        },
        eyeImageListPicUrl: function () {
            var url = vm.goods.listPicUrl;
            eyeImage(url);
        },
        eyeImage: function (e) {
            eyeImage($(e.target).attr('src'));
        },
        getMerchant: function () {
            Ajax.request({
                url: "../cdtmerchant/queryAll",
                async: true,
                successCallback: function (r) {
                    vm.merchants = r.list;
                }
            });
        }
    },
    mounted() {
        this.uploadList = this.$refs.upload.fileList;
    }
});