package com.chundengtai.base.api;


import com.chundengtai.base.annotation.IgnoreAuth;
import com.chundengtai.base.entity.PurchasePeopleEntity;
import com.chundengtai.base.result.Result;
import com.chundengtai.base.service.ApiPurchasePeopleService;
import com.chundengtai.base.utils.R;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller
 *
 * @date 2019-11-25 09:39:01
 */
@Api(value = "未名严选详情购买用户", tags = "购买人群列表")
@RestController
@RequestMapping("/api/v2/purchasePeople")
public class ApiPurchasePeopleController {
    @Autowired
    private ApiPurchasePeopleService PurchasePeopleService;

    /**
     * 查看列表
     */
    @ApiOperation(value = "获取商品购买用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "goodsId", value = "产品id", dataType = "int", paramType = "query")
    })
    @ApiResponses({@ApiResponse(code = 200, message = "If successful, this method return JwtAccessTokenVO."),
            @ApiResponse(code = 400, message = "If bad request."),
            @ApiResponse(code = 500, message = "If internal server error."),
            @ApiResponse(code = 503, message = "If service unavailable.")
    })
    @GetMapping("/list")
    @IgnoreAuth
    public Result<Map<String, Object>> list(@RequestParam Integer goodsId) {
        //查询列表数据
        Map<String, Object> params = new HashMap<>();
        params.put("goodsId", goodsId);
        params.put("sidx", "id");
        params.put("order", "desc");
        List<PurchasePeopleEntity> nideshopPurchasePeopleList = PurchasePeopleService.queryList(params);
        Map<String, Object> PurchasePeopleList = new HashMap<String, Object>();
        PurchasePeopleList.put("purchasePeopleList", nideshopPurchasePeopleList);
        return Result.success(PurchasePeopleList);
    }

    /**
     * 查看信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id) {
        PurchasePeopleEntity nideshopPurchasePeople = PurchasePeopleService.queryObject(id);

        return R.ok().put("nideshopPurchasePeople", nideshopPurchasePeople);
    }

    /**
     * 保存
     */
    //@RequestMapping("/save")
    @IgnoreAuth
    public R save(PurchasePeopleEntity PurchasePeople) {
        PurchasePeopleService.save(PurchasePeople);

        return R.ok();
    }

    /**
     * 修改
     */
    //@RequestMapping("/update")
    public R update(@RequestBody PurchasePeopleEntity nideshopPurchasePeople) {
        PurchasePeopleService.update(nideshopPurchasePeople);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids) {
        PurchasePeopleService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<PurchasePeopleEntity> list = PurchasePeopleService.queryList(params);

        return R.ok().put("list", list);
    }
}
