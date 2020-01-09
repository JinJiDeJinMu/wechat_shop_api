
package com.platform.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chundengtai.base.bean.ProdProp;
import com.chundengtai.base.bean.ProdPropValue;
import com.chundengtai.base.exception.BizException;
import com.chundengtai.base.service.ProdPropService;
import com.chundengtai.base.service.ProdPropValueService;
import com.chundengtai.base.utils.ShiroUtils;
import com.chundengtai.base.weixinapi.ProdPropRule;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * 规格管理
 *
 * @author lgh
 */
@RestController
@RequestMapping("/prod/spec")
public class SpecController {

    @Autowired
    private ProdPropService prodPropService;
    @Autowired
    private ProdPropValueService prodPropValueService;

    /**
     * 分页获取
     */
    @GetMapping("/page")
    @RequiresPermissions("prod:spec:page")
    public ResponseEntity<IPage<ProdProp>> page(ProdProp prodProp) {
        prodProp.setRule(ProdPropRule.SPEC.value());
        prodProp.setShopId(ShiroUtils.getUserEntity().getMerchantId());
        IPage<ProdProp> list = prodPropService.pagePropAndValue(prodProp);
        return ResponseEntity.ok(list);
    }

    /**
     * 获取所有的规格
     */
    @GetMapping("/list")
    public ResponseEntity<List<ProdProp>> list() {
        List<ProdProp> list = prodPropService.list(new LambdaQueryWrapper<ProdProp>().eq(ProdProp::getRule, ProdPropRule.SPEC.value()).eq(ProdProp::getShopId, ShiroUtils.getUserEntity().getMerchantId()));
        return ResponseEntity.ok(list);
    }

    /**
     * 根据规格id获取规格值
     */
    @GetMapping("/listSpecValue/{specId}")
    public ResponseEntity<List<ProdPropValue>> listSpecValue(@PathVariable("specId") Long specId) {
        List<ProdPropValue> list = prodPropValueService.list(new LambdaQueryWrapper<ProdPropValue>().eq(ProdPropValue::getPropId, specId));
        return ResponseEntity.ok(list);
    }

    /**
     * 保存
     */
    @PostMapping
    @RequiresPermissions("prod:spec:save")
    public ResponseEntity<Void> save(@Valid @RequestBody ProdProp prodProp) {
        prodProp.setRule(ProdPropRule.SPEC.value());
        prodProp.setShopId(ShiroUtils.getUserEntity().getMerchantId());
        prodPropService.saveProdPropAndValues(prodProp);
        return ResponseEntity.ok().build();
    }

    /**
     * 修改
     */
    @PutMapping
    @RequiresPermissions("prod:spec:update")
    public ResponseEntity<Void> update(@Valid @RequestBody ProdProp prodProp) {
        ProdProp dbProdProp = prodPropService.getById(prodProp.getPropId());
        if (!Objects.equals(dbProdProp.getShopId(), ShiroUtils.getUserEntity().getMerchantId())) {
            throw new BizException("没有权限获取该商品规格信息");
        }
        prodProp.setRule(ProdPropRule.SPEC.value());
        prodProp.setShopId(ShiroUtils.getUserEntity().getMerchantId());
        prodPropService.updateProdPropAndValues(prodProp);
        return ResponseEntity.ok().build();
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    @RequiresPermissions("prod:spec:delete")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        prodPropService.deleteProdPropAndValues(id, ProdPropRule.SPEC.value(), ShiroUtils.getUserEntity().getMerchantId());
        return ResponseEntity.ok().build();
    }
}
