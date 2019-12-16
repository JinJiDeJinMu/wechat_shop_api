package com.platform.api;

import com.platform.entity.CdtMerchantEntity;
import com.platform.entity.CdtMt;
import com.platform.service.CdtMerchantService;
import com.platform.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v2/cdtMerchant")
@Slf4j
public class ApiCdtMerchantController {

    @Autowired
    private CdtMerchantService cdtMerchantService;

    @PostMapping("/save")
    @ResponseBody
    public R save(@RequestBody CdtMt cdtMerchant) {

        CdtMerchantEntity cdtMerchantEntity = new CdtMerchantEntity();
        cdtMerchantEntity.setShopType(cdtMerchant.getShoptype());
        cdtMerchantEntity.setMobile(cdtMerchant.getPhone());
        cdtMerchantEntity.setShopAddress(cdtMerchant.getAddress());
        cdtMerchantEntity.setShopName(cdtMerchant.getShopname());
        cdtMerchantEntity.setShopOwner(cdtMerchant.getShopOwner());
        log.info("CdtMerchantEntity="+cdtMerchantEntity);
        cdtMerchantService.save(cdtMerchantEntity);
        return R.ok();
    }
}
