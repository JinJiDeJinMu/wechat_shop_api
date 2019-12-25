$(function () {
});

const defaultListQuery = {
	limit: 10,
	page: 1,
	sidx: '',
	order: 'asc',
	name: '',
	goodSn: ''
};

var vm = new Vue({
	el: '#app',
	data: {
		showList: true,
		title: null,

		CdtDistridetail: {
			id: '',
			userId: '',
			goldUserId: '',
			orderSn: '',
			money: '',
			status: '',
			token: '',
			createdTime: '',
			updateTime: '',
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
		getList: function () {
			this.listLoading = false;
			Ajax.request({
				url: "../cdtDistridetail/list?limit=" + this.listQuery.limit + "&page=" + this.listQuery.page + "&sidx=" +
					this.listQuery
						.sidx + "&name=" + this.listQuery.name + "&order=" + this.listQuery.order,
				async: true,
				successCallback: function (res) {
					vm.listLoading = false;
					console.log(res);
					vm.list = res.page.list;
					vm.total = res.page.totalCount;
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
					message: '请选择要操作的商品',
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
		del: function (ids) {
			if (ids == null) {
				return;
			}
			Ajax.request({
				type: "POST",
				url: "../cdtDistridetail/delete",
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
				url: "../cdtDistridetail/info/" + id,
				async: true,
				successCallback: function (res) {
					vm.CdtDistridetail = res.CdtDistridetail;
				}
			});
		},
		reload: function (event) {
			vm.showList = true;
			this.getList()
			vm.handleReset('formValidate');
		},
		add: function () {
			vm.showList = false;
			vm.title = "新增";

		},

		updateFun: function (id) {
			if (id == null) {
				return;
			}
			vm.showList = false;
			vm.title = "修改";
		},

		saveOrUpdate: function (event) {
			var url = vm.CdtDistridetail.id == null ? "../cdtDistridetail/save" : "../cdtDistridetail/update";
			Ajax.request({
				type: "POST",
				url: url,
				contentType: "application/json",
				params: JSON.stringify(vm.CdtDistridetail),
				successCallback: function (r) {
					alert('操作成功', function (index) {
						vm.reload();
					});
				}
			});
		},

		dataFormSubmit: function (name) {
			handleSubmitValidate(this, name, function () {
				vm.saveOrUpdate()
			});
		},
	},
	mounted() {
	}
});
