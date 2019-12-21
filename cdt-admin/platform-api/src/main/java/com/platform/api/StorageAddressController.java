package com.platform.api;

import com.platform.annotation.IgnoreAuth;
import com.platform.entity.StorageAddressEntity;
import com.platform.service.StorageAddressService;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * 存放地址表Controller
 *
 * @date 2019-11-22 14:40:12
 */
@RestController
@Slf4j
@RequestMapping(value = "/storageaddress")
public class StorageAddressController {
    @Autowired
    private StorageAddressService storageAddressService;

    /**
     * 查看列表
     */


//    @RequiresPermissions("storageaddress:list")
    @RequestMapping(value = "/list")
    @IgnoreAuth
    @ResponseBody
    public R list(Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<StorageAddressEntity> storageAddressList = storageAddressService.queryList(query);
        int total = storageAddressService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(storageAddressList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("storageaddress:info")
    @IgnoreAuth
    @ResponseBody
    public R info(@PathVariable("id") Integer id) {
        StorageAddressEntity storageAddress = storageAddressService.queryObject(id);
        return R.ok().put("storageAddress", storageAddress);
    }

    /**
     *
     * 保存
     *
     */
    @RequestMapping(value = "/save")
//    @RequiresPermissions("storageaddress:save")
    @ResponseBody
    @IgnoreAuth
    public R save(StorageAddressEntity storageAddress) {
        System.out.println(storageAddress);
        storageAddressService.save(storageAddress);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("storageaddress:update")
    @IgnoreAuth
    @ResponseBody
    public R update(@RequestBody StorageAddressEntity storageAddress) {
        storageAddressService.update(storageAddress);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("storageaddress:delete")
    @ResponseBody
    public R delete(@RequestBody Integer[] ids) {
        storageAddressService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    @ResponseBody
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<StorageAddressEntity> list = storageAddressService.queryList(params);

        return R.ok().put("list", list);
    }
}
