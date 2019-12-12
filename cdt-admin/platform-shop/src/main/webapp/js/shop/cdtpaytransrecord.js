$(function () {
	var name = getQueryString("name");
    var orderNo = getQueryString("orderNo");
    var payState = getQueryString("payState");
    var channelId = getQueryString("channelId");
    var createDate = getQueryString("create_date");
    var updateDate = getQueryString("update_date");
    var url = '../cdtpaytransrecord/list';
    if (name) {
        url += '?name=' + name;
    }
    if (orderNo) {
        url += '?orderNo=' + orderNo;
    }
    if (payState) {
        url += '?payState=' + payState;
    }
    if (channelId) {
        url += '?channelId=' + channelId;
    }
    if (createDate) {
        url += '?createDate=' + createDate;
    }
    if (updateDate) {
        url += '?updateDate=' + updateDate;
    }
    $("#jqGrid").Grid({
        url: '../cdtpaytransrecord/list',
        colModel: [
			{label: 'id', name: 'id', index: 'id', key: true, hidden: true},
			{label: '', name: 'rid', index: 'rid', width: 60},
			{label: '名称', name: 'name', index: 'name', width: 80},
			{label: '商品备注', name: 'memo', index: 'memo', width: 80},
			{label: '创建者id', name: 'creator', index: 'creator', width: 80,hidden:true},
			{label: '创建者名称', name: 'creatorName', index: 'creator_name', width: 80},
			{label: '创建时间', name: 'createDate', index: 'create_date', width: 80,
                formatter: function (value) {
                    return transDate(value);
                }
			},
			{label: '数据等级', name: 'dataLevel', index: 'data_level', width: 80},
			{label: '订单号', name: 'orderNo', index: 'order_no', width: 100},
			{label: '订单id', name: 'payOrderId', index: 'pay_order_id', width: 80,hidden:true},
			{label: '支付商户id', name: 'mchId', index: 'mch_id', width: 80},
			{label: '支付订单号', name: 'mchOrderNo', index: 'mch_order_no', width: 80},
			{label: '渠道id', name: 'channelId', index: 'channel_id', width: 80},
			{label: '总金额', name: 'amount', index: 'amount', width: 80},
			{label: '交易金额', name: 'tradePrice', index: 'trade_price', width: 80},
            {label: '付款状态', name: 'payState',align: 'center', index: 'pay_state', width: 80,
                formatter: function (value) {
                    if (value == '0') {
                        return '未付款';
                    } else if (value == '1') {
                        return '付款中';
                    } else if (value == '2') {
                        return '已付款';
                    } else if (value == '4') {
                        return '退款';
                    }
                    return value;
                }
            },
			{label: '支付类型', name: 'payType', index: 'pay_type', width: 80},
			{label: 'app_id', name: 'appId', index: 'app_id', width: 80},
			{label: '更新时间', name: 'updateDate', index: 'update_date', width: 80,
                formatter: function (value) {
                    return transDate(value);
                }
			},
			{label: '请求文本', name: 'reqText', index: 'req_text', width: 80,hidden:true},
			{label: '返回文本', name: 'resText', index: 'res_text', width: 80,hidden:true}]
    });
});

var vm = new Vue({
	el: '#rrapp',
	data: {
        showList: true,
        title: null,
		cdtPaytransRecord: {},
		ruleValidate: {
			name: [
				{required: true, message: '名称不能为空', trigger: 'blur'}
			]
		},
		q: {
		    name: '',
            orderNo: '',
			payState: '',
			channelId: '',
            createDate:'',
            updateDate:''
		}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function () {
			vm.showList = false;
			vm.title = "新增";
			vm.cdtPaytransRecord = {};
		},
		update: function (event) {
            var id = getSelectedRow("#jqGrid");
			if (id == null) {
				return;
			}
			vm.showList = false;
            vm.title = "修改";

            vm.getInfo(id);
		},
		saveOrUpdate: function (event) {
            var url = vm.cdtPaytransRecord.id == null ? "../cdtpaytransrecord/save" : "../cdtpaytransrecord/update";
            Ajax.request({
			    url: url,
                params: JSON.stringify(vm.cdtPaytransRecord),
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
            var ids = getSelectedRows("#jqGrid");
			if (ids == null){
				return;
			}

			confirm('确定要删除选中的记录？', function () {
                Ajax.request({
				    url: "../cdtpaytransrecord/delete",
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
		getInfo: function(id){
            Ajax.request({
                url: "../cdtpaytransrecord/info/"+id,
                async: true,
                successCallback: function (r) {
                    vm.cdtPaytransRecord = r.cdtPaytransRecord;
                }
            });
		},
		reload: function (event) {
			vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
			$("#jqGrid").jqGrid('setGridParam', {
                postData: {
                	'name': vm.q.name,
                    'orderNo': vm.q.orderNo,
                    'payState': vm.q.payState,
                    'channelId': vm.q.channelId,
                    'createDate': vm.q.createDate,
                    'updateDate': vm.q.updateDate
				},
                page: page
            }).trigger("reloadGrid");
            vm.handleReset('formValidate');
		},
        reloadSearch: function() {
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