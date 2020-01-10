package com.chundengtai.base.controller;

import com.chundengtai.base.annotation.IgnoreAuth;
import com.chundengtai.base.entity.CdtMerchantEntity;
import com.chundengtai.base.entity.CdtMt;
import com.chundengtai.base.result.Result;
import com.chundengtai.base.service.CdtMerchantWxService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@RestController
@RequestMapping("/api/v2/cdtMerchant")
@Slf4j
public class ApiCdtMerchantController {

    @Autowired
    private CdtMerchantWxService cdtMerchantService;

    @PostMapping("/save")
    @ResponseBody
    @IgnoreAuth
    public Result save(@RequestBody CdtMt cdtMerchant) {

        CdtMerchantEntity cdtMerchantEntity = new CdtMerchantEntity();
        cdtMerchantEntity.setShopType(null);
        cdtMerchantEntity.setMobile(cdtMerchant.getPhone());
        cdtMerchantEntity.setShopAddress(cdtMerchant.getAddress());
        cdtMerchantEntity.setShopName(cdtMerchant.getShopname());
        cdtMerchantEntity.setShopOwner(cdtMerchant.getShopOwner());
        cdtMerchantEntity.setCreateTime(new Date());
        cdtMerchantEntity.setIsDistribution(1);
        cdtMerchantEntity.setShopStatus(-1);
        cdtMerchantService.save(cdtMerchantEntity);
        return Result.success();
    }
}
