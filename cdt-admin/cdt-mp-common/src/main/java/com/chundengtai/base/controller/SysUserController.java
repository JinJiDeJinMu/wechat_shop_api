package com.chundengtai.base.controller;

import com.chundengtai.base.annotation.SysLog;
import com.chundengtai.base.dao.SysUserDao;
import com.chundengtai.base.entity.MlsUserEntity2;
import com.chundengtai.base.entity.SysUserEntity;
import com.chundengtai.base.service.SysUserRoleService;
import com.chundengtai.base.service.SysUserService;
import com.chundengtai.base.utils.*;
import com.chundengtai.base.validator.Assert;
import com.chundengtai.base.validator.ValidatorUtils;
import com.chundengtai.base.validator.group.AddGroup;
import com.chundengtai.base.validator.group.UpdateGroup;
import org.apache.commons.lang.ArrayUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 系统用户
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends AbstractController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserDao sysUserDao;
    
    @Autowired
    private SysUserRoleService sysUserRoleService;

    /**
     * 所有用户列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:user:list")
    public R list(@RequestParam Map<String, Object> params) {
        //只有超级管理员，才能查看所有管理员列表
        if (getUserId() != Constant.SUPER_ADMIN) {
            params.put("createUserId", getUserId());
        }

        //查询列表数据
        Query query = new Query(params);
        List<SysUserEntity> userList = sysUserService.queryList(query);
        int total = sysUserService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(userList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 获取登录的用户信息
     */
    @RequestMapping("/info")
    public R info() {
        return R.ok().put("user", getUser());
    }

    /**
     * 修改登录用户密码
     */
    @SysLog("修改密码")
    @RequestMapping("/password")
    public R password(String password, String newPassword) {
        if(ResourceUtil.getConfigByName("sys.demo").equals("1")){
            throw new RRException("演示环境无法修改密码！");
        }
        Assert.isBlank(newPassword, "新密码不为能空");

        //sha256加密
        password = new Sha256Hash(password).toHex();
        //sha256加密
        newPassword = new Sha256Hash(newPassword).toHex();

        //更新密码
        int count = sysUserService.updatePassword(getUserId(), password, newPassword);
        if (count == 0) {
            return R.error("原密码不正确");
        }

        //退出
        ShiroUtils.logout();

        return R.ok();
    }

    /**
     * 用户信息
     */
    @RequestMapping("/info/{userId}")
    @RequiresPermissions("sys:user:info")
    public R info(@PathVariable("userId") Long userId) {
        SysUserEntity user = sysUserService.queryObject(userId);
        //获取用户所属的角色列表
        List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
        user.setRoleIdList(roleIdList);
        return R.ok().put("user", user);
    }

    /**
     * 保存用户
     */
    @SysLog("保存用户")
    @RequestMapping("/save")
    @RequiresPermissions("sys:user:save")
    public R save(@RequestBody SysUserEntity user) {
        ValidatorUtils.validateEntity(user, AddGroup.class);
        int count=sysUserDao.mlsUseCount(user.getMobile());
        if(count>0) {
        	return R.error("手机号已被注册");
        }
        user.setCreateUserId(getUserId());
        if (!user.getRoleIdList().contains(Long.valueOf(5)) && user.getMerchantId() == null) {
            return R.error("非超级管理员必须选择绑定的店铺");
        }
        sysUserService.save(user);
        //sysUserDao.updateMerchantId(user);
        
        MlsUserEntity2 mlsUserVo=new MlsUserEntity2();
        mlsUserVo.setUserTel(user.getMobile());
        mlsUserVo.setFx(user.getFx());
        mlsUserVo.setFx1(user.getFx1());
        mlsUserVo.setFx2(user.getFx2());
        mlsUserVo.setPfx(user.getPfx());
        mlsUserVo.setFid(-1L);
        mlsUserVo.setRootId(user.getUserId());
        mlsUserVo.setMerchantId(user.getMerchantId());
        mlsUserVo.setAllShow(user.getAllShow());
        sysUserDao.insertMlsUse(mlsUserVo);
        return R.ok();
    }

    /**
     * 修改用户
     */
    @SysLog("修改用户")
    @RequestMapping("/update")
    @RequiresPermissions("sys:user:update")
    public R update(@RequestBody SysUserEntity user) {
        ValidatorUtils.validateEntity(user, UpdateGroup.class);

        user.setCreateUserId(getUserId());

        sysUserService.update(user);
        MlsUserEntity2 mlsUserVo=new MlsUserEntity2();
        mlsUserVo.setFx(user.getFx());
        mlsUserVo.setFx1(user.getFx1());
        mlsUserVo.setFx2(user.getFx2());
        mlsUserVo.setPfx(user.getPfx());
        mlsUserVo.setRootId(user.getUserId());
        mlsUserVo.setAllShow(user.getAllShow());
        sysUserDao.updateMlsUse(mlsUserVo);
        
        return R.ok();
    }

    /**
     * 删除用户
     */
    @SysLog("删除用户")
    @RequestMapping("/delete")
    @RequiresPermissions("sys:user:delete")
    public R delete(@RequestBody Long[] userIds) {
        if (ArrayUtils.contains(userIds, 1L)) {
            return R.error("系统管理员不能删除");
        }

        if (ArrayUtils.contains(userIds, getUserId())) {
            return R.error("当前用户不能删除");
        }

        sysUserService.deleteBatch(userIds);

        return R.ok();
    }
}