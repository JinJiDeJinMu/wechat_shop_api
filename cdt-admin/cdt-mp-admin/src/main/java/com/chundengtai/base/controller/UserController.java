package com.chundengtai.base.controller;

import com.chundengtai.base.constance.ShopShow;
import com.chundengtai.base.entity.SysUserEntity;
import com.chundengtai.base.entity.UserEntity;
import com.chundengtai.base.service.admin.UserService;
import com.chundengtai.base.utils.Base64;
import com.chundengtai.base.utils.*;
import com.chundengtai.base.utils.excel.ExcelExport;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("user:list")
    public R list(@RequestParam Map<String, Object> params) {
        SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
        System.out.println("merchantId" + sysUserEntity.getMerchantId());
        //查询列表数据
        Query query = new Query(params);
        if (sysUserEntity.getMerchantId() != ShopShow.ADMINISTRATOR.getCode()) {
            query.put("merchantId", sysUserEntity.getMerchantId());
        }
        if (query.containsKey("username")) {
            query.put("username", Base64.encode(query.get("username").toString()));
        }
        List<UserEntity> userList = userService.queryList(query);
        int total = userService.queryTotal(query);
        for (UserEntity user : userList) {
            user.setUsername(Base64.decode(user.getUsername()));
            user.setNickname(Base64.decode(user.getNickname()));
            user.setPromoterName(Base64.decode(user.getPromoterName()));
        }
        PageUtils pageUtil = new PageUtils(userList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看推广列表
     */
    @RequestMapping("/promoterList")
    public R promoterList(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<UserEntity> userList = userService.queryList(query);
        int total = userService.queryTotal(query);
        PageUtils pageUtil = new PageUtils(userList, total, query.getLimit(), query.getPage());
        return R.ok().put("page", pageUtil);
    }

    /**
     * 推广关系变更
     *
     * @param user
     * @return
     */
    @RequestMapping("/updatePromoter")
    public R updatePromoter(@RequestBody UserEntity user) {
        Map map = new HashMap();
        map.put("mobile", user.getMobile());
        List<UserEntity> userList = userService.queryList(map);
        if (userList.size() > 0) {
            user.setPromoterId(userList.get(0).getId());
            user.setPromoterName(userList.get(0).getUsername());
        } else {
            return R.error().put("error", "该用户不存在");
        }
        userService.updatePromoter(user);
        return R.ok().put("user", user);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("user:info")
    public R info(@PathVariable("id") Integer id) {
        UserEntity user = userService.queryObject(id);
        user.setUsername(Base64.decode(user.getUsername()));
        return R.ok().put("user", user);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("user:save")
    public R save(@RequestBody UserEntity user) {
        userService.save(user);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("user:update")
    public R update(@RequestBody UserEntity user) {
        user.setUsername(Base64.encode(user.getUsername()));
        userService.update(user);

        return R.ok();
    }

    /**
     * 绑定店铺
     */
    @RequestMapping("/binding")
    @RequiresPermissions("user:update")
    public R binding(@RequestBody UserEntity user) {
        System.out.println(user);
        userService.update(user);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("user:delete")
    public R delete(@RequestBody Integer[] ids) {
        userService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<UserEntity> userList = userService.queryList(params);
        for (UserEntity user : userList) {
            user.setUsername(Base64.decode(user.getUsername()));
        }
        return R.ok().put("list", userList);
    }

    /**
     * 总计
     */
    @RequestMapping("/queryTotal")
    public R queryTotal(@RequestParam Map<String, Object> params) {
        int sum = userService.queryTotal(params);

        return R.ok().put("userSum", sum);
    }

    /**
     * 导出会员
     */
    @RequestMapping("/export")
//    @RequiresPermissions("user:export")
    public R export(@RequestParam Map<String, Object> params, HttpServletResponse response) {

        List<UserEntity> userList = userService.queryList(params);

        ExcelExport ee = new ExcelExport("会员列表");

        String[] header = new String[]{"会员名称", "昵称", "性别", "是否实名", "手机号码", "真实姓名", "身份证号"};

        List<Map<String, Object>> list = new ArrayList<>();

        if (userList != null && userList.size() != 0) {
            for (UserEntity userEntity : userList) {
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put("USERNAME", Base64.decode(userEntity.getUsername()));
                map.put("Nickname", Base64.decode(userEntity.getNickname()));
                map.put("GENDER", userEntity.getGender() == 1 ? "男" : (userEntity.getGender() == 2 ? "女" : "未知"));
                map.put("isReal", (userEntity.getIsReal().equals("1") ? "未实名" : "实名"));
                map.put("MOBILE", userEntity.getMobile());
                map.put("realName", userEntity.getRealName());
                map.put("idCard", userEntity.getIdCard());
                list.add(map);
            }
        }

        ee.addSheetByMap("会员", list, header);
        ee.export(response);
        return R.ok();
    }
}
