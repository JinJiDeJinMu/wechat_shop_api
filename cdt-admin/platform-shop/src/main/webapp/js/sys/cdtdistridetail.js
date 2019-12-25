const defaultModel = {
	id: null,
	userId: null,
	goldUserId: null,
	orderSn: null,
	money: null,
	status: null,
	token: null,
	createdTime: null,
	updateTime: null,
};
var vue = new Vue({
	el: '#app',
	data: {
		showList: true,
		title: null,
		baseForm: {
			pageIndex: 1,
			pageSize: 10,
			sortField: 'id',
			order: 'desc',
			data: Object.assign({}, defaultModel),
		},
		ruleValidate: {},
		operates: [{
			label: "商品上架",
			value: "publishOn"
		},
			{
				label: "商品下架",
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
		}
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
			return year + "-" + month + "-" + day + " " + hours + ":" + minutes + ":" + seconds;
		},
		saveOrUpdate: function (event) {
			var url = this.baseForm.data.id == null ? "../cdtDistridetail/save.json" : "../cdtDistridetail/update.json";
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
		del: function (ids) {
			if (ids == null) {
				return;
			}
			let that = this;
			Ajax.request({
				type: "POST",
				url: "../cdtDistridetail/delete.json",
				contentType: "application/json",
				params: JSON.stringify(ids),
				successCallback: function (res) {
					alert('操作成功', function (index) {
						that.reload();
					});
				}
			});
		},
		getInfo: function (id) {
			let that = this;
			Ajax.request({
				url: "../cdtDistridetail/info/" + id + ".json",
				async: true,
				successCallback: function (res) {
					that.baseForm.data = res.data;
				}
			});
		},
		getList: function () {
			this.listLoading = false;
			var params = this.baseForm;
			let that = this;
			Ajax.request({
				type: "POST",
				url: "../cdtDistridetail/list.json",
				contentType: "application/json",
				params: JSON.stringify(params),
				async: true,
				successCallback: function (res) {
					that.listLoading = false;
					console.log(res);
					that.list = res.data.list;
					that.total = res.data.totalCount;
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

		},
		handleSizeChange(val) {
			this.baseForm.pageIndex = 1;
			this.listQuery.pageSize = val;
			this.getList();
		},
		handleCurrentChange(val) {
			this.listQuery.pageSize = val;
			this.getList();
		},
		handleSelectionChange(val) {
			this.multipleSelection = val;
		},
		handleDelete(index, row) {

		},
		handlePublishStatusChange(index, row) {
			let ids = [];
			ids.push(row.id);
			console.log(JSON.stringify(row));
			this.updatePublishStatus(row.isOnSale, ids);
		},

		updatePublishStatus: function (status, ids) {
			console.log(status);
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
		reload: function (event) {
			this.showList = true;
			this.getList()
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
