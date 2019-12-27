var util = require('../../utils/util.js');
var api = require('../../config/api.js');

Page({
    data: {
        baseForm: {
            pageIndex: 1,
            pageSize: 10,
            sortField: 'id',
            order: 'desc',
            data: {
#foreach($field in ${table.fields})
${field.propertyName}
:
null,
    #end
}
},
flag:1,
    id
:
0,
    list
:
[]
},
onLoad: function (options) {
    if (options.id) {
        this.setData({
            id: options.id
        });
    }
    this.getCatalog();
}
,
onReady: function () {
    // 页面渲染完成
}
,
onShow: function () {
    // 页面显示
}
,
onHide: function () {
    // 页面隐藏
}
,
onUnload: function () {
    // 页面关闭
}
,
getList: function () {
    var that = this;
    util.request(api.ApiRootUrl + 'api/catalog/' + that.data.id
        .then(function (res) {
            that.setData({
                categoryList: res.data,
            });
        });
}
})