package com.chundengtai.base.controller;

import com.chundengtai.base.dao.CustomerDao;
import com.chundengtai.base.dao.UserDao;
import com.chundengtai.base.entity.AddressEntity;
import com.chundengtai.base.entity.CustomerEntity;
import com.chundengtai.base.service.admin.CustomerService;
import com.chundengtai.base.utils.PageUtils;
import com.chundengtai.base.utils.Query;
import com.chundengtai.base.utils.R;
import com.chundengtai.base.utils.excel.ExcelExport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;


/**
 * 用户管理
 */
@RestController
@RequestMapping("customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private CustomerDao customerDao;

    /**
     * 查看列表
     */

    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据

        Query query = new Query(params);

        List<CustomerEntity> customerList = customerService.queryList(query);
        int total = customerService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(customerList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    @RequestMapping("/wclist")
    public R wclist(@RequestParam Map<String, Object> params) {
        //查询列表数据
        params.put("customerState", 1);
        params.put("ctime", 1);
        Query query = new Query(params);
        List<CustomerEntity> customerList = customerDao.query2List(query);
        int total = customerDao.query2Total(query);
        PageUtils pageUtil = new PageUtils(customerList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }


    /**
     * 查看信息
     */

    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id) {
        CustomerEntity customer1 = new CustomerEntity();
        customer1.setId(id);
        CustomerEntity customer = customerService.queryObject(customer1);

        return R.ok().put("user", customer);
    }

    /**
     * 保存
     */

    @RequestMapping("/save")
    public R save(@RequestBody CustomerEntity customerEntity) {
        customerService.save(customerEntity);

        return R.ok();
    }

    /**
     * 修改
     */

    @RequestMapping("/update")
    public R update(@RequestBody CustomerEntity customerEntity) {
        customerService.update(customerEntity);

        return R.ok();
    }

    /**
     * 删除
     */

    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids) {
        customerService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */

    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<CustomerEntity> customerEntityListList = customerService.queryList(params);

        return R.ok().put("list", customerEntityListList);
    }

    /**
     * 总计
     */

    @RequestMapping("/queryTotal")
    public R queryTotal(@RequestParam Map<String, Object> params) {
        int sum = customerService.queryTotal(params);

        return R.ok().put("userSum", sum);
    }

    /**
     * 导出会员
     */
    @RequestMapping("/export")
    public R export(HttpServletResponse response) {
        Map<String, Object> params = new HashMap<>();
        //查询列表数据
        params.put("customerState", 1);
        params.put("ctime", 1);
        List<CustomerEntity> customerList = customerDao.query2List(params);
        ExcelExport ee = new ExcelExport("代维客户列表");
        String[] header = new String[]{"用户姓名", "用户手机号", "客户姓名", "性别", "出生日期", "手机号码", "职务", "运维结束时间", "地址", "备注"};
        List<Map<String, Object>> list = new ArrayList<>();

        if (customerList != null && customerList.size() != 0) {
            for (CustomerEntity customerEntity : customerList) {
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put("realName", customerEntity.getRealName());
                map.put("mobile2", customerEntity.getMobile2());
                map.put("uname", customerEntity.getUname());
                map.put("gender", "1".equals(customerEntity.getGender()) ? "男" : "女");
                map.put("birthday", customerEntity.getBirthday());
                map.put("mobile", customerEntity.getMobile());
                map.put("job", customerEntity.getJob());
                map.put("end_date", customerEntity.getEnd_date());
                AddressEntity value = customerEntity.getAddressEntity();
                map.put("addressEntity", value.getProvinceName() + value.getCityName() + value.getCountyName() + value.getDetailInfo());
                map.put("remarks", customerEntity.getRemarks());
                list.add(map);
            }
        }

        ee.addSheetByMap("会员", list, header);
        ee.export(response);
        return R.ok();
    }
}
