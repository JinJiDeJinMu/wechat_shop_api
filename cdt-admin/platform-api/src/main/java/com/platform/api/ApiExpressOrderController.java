package com.platform.api;

import com.platform.annotation.IgnoreAuth;
import com.platform.entity.ExpressOrderVo;
import com.platform.service.ApiExpressOrderService;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * Controller
 *
 * @author lipengjun
 * @date 2019-11-29 10:44:41
 */
@Controller
@RequestMapping("/api/expressOrder")
public class ApiExpressOrderController {
    @Autowired
    private ApiExpressOrderService apiExpressOrderService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("nideshopexpressorder:list")
    @ResponseBody
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<ExpressOrderVo> nideshopExpressOrderList = apiExpressOrderService.queryList(query);
        int total = apiExpressOrderService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(nideshopExpressOrderList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("nideshopexpressorder:info")
    @ResponseBody
    public R info(@PathVariable("id") Integer id) {
        ExpressOrderVo nideshopExpressOrder = apiExpressOrderService.queryObject(id);

        return R.ok().put("nideshopExpressOrder", nideshopExpressOrder);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value = "添加商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "string", paramType = "query", example = "2223333"),
            @ApiImplicitParam(name = "country", value = "国家", dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "province", value = "省", dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "city", value = "市", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "district", value = "区", dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "address", value = "详细地址", dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "mobile", value = "手机号码", dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "姓名", dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "realName", value = "真实姓名", dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "remarks", value = "备注", dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "pickNumber", value = "取件号", dataType = "long", paramType = "query")
    }
            )
    @IgnoreAuth
//    @RequiresPermissions("nideshopexpressorder:save")
    @ResponseBody
    public R save(Integer userId,String country,String province,String city,String district,
                  String address,String mobile,String name,String realName,String remarks,String pickNumber
                  ) {
        //添加快递代取商品的详细信息
        ExpressOrderVo expressOrderVo = new ExpressOrderVo();
        expressOrderVo.setName(name);
        expressOrderVo.setMobile(mobile);
        expressOrderVo.setUserId(userId);
        expressOrderVo.setCountry(country);
        expressOrderVo.setProvince(province);
        expressOrderVo.setCity(city);
        expressOrderVo.setDistrict(district);
        expressOrderVo.setAddress(address);
        expressOrderVo.setRemarks(remarks);
        expressOrderVo.setPickNumber(pickNumber);
        expressOrderVo.setRealName(realName);
        apiExpressOrderService.save(expressOrderVo);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("nideshopexpressorder:update")
    @ResponseBody
    public R update(@RequestBody ExpressOrderVo nideshopExpressOrder) {
        apiExpressOrderService.update(nideshopExpressOrder);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("nideshopexpressorder:delete")
    @ResponseBody
    public R delete(@RequestBody Integer[] ids) {
        apiExpressOrderService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    @ResponseBody
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<ExpressOrderVo> list = apiExpressOrderService.queryList(params);

        return R.ok().put("list", list);
    }
}
