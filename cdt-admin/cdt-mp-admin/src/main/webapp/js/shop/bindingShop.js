$(function () {
    // var  promoterId = getQueryString("promoterId");

});


var vm = new Vue({
    el: '#rrapp',
    data: {
        showList: true,
        q: {
            uname: ''
            //  telNumber: ''
        },
        user: {
            merchantId: '',
            id: 0
        },
        merchants: [],
        title: null,
        ruleValidate: {
            mobile: [
                {required: true, message: '请选择店铺', trigger: 'blur'}
            ]
        }
    },
    created: function () {
        console.log('---------eeee--');
        var Id = getQueryString("userId");
        console.log('--------' + Id);
        this.user.id = Id;
        console.log('--------' + this.user.id);
        // alert("111222");
        this.getMerchant();
    },
    methods: {
        query: function () {
            vm.reload();
        },
        reload: function (event) {
            var Id = getQueryString("userId");
            vm.showList = true;

        },
        handleSubmit: function (name) {
            handleSubmitValidate(this, name, function () {
                vm.updatePromoter()
            });
        },
        handleReset: function (name) {
            handleResetForm(this, name);
        },
        updatePromoter: function (event) {
            confirm('确定要更换店铺？', function () {
                Ajax.request({
                    type: "POST",
                    url: "../user/binding",
                    contentType: "application/json",
                    params: JSON.stringify(vm.user),
                    successCallback: function () {
                        alert('操作成功', function (index) {
                            vm.reload();
                        });
                    }
                });
            });
        },
        getMerchant: function () {
            // alert("111");
            Ajax.request({
                url: "../cdtmerchant/queryAll",
                async: true,
                successCallback: function (r) {
                    vm.merchants = r.list;
                }
            });
        }
    }
});
