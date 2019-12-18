package com.platform.api;

import com.chundengtai.base.result.Result;
import com.platform.annotation.IgnoreAuth;
import com.platform.entity.CdtMerchantEntity;
import com.platform.entity.CdtMt;
import com.platform.service.CdtMerchantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@RestController
@RequestMapping("/api/v2/cdtMerchant")
@Slf4j
public class ApiCdtMerchantController {

    @Autowired
    private CdtMerchantService cdtMerchantService;

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
