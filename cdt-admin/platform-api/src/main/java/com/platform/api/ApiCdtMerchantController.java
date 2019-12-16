package com.platform.api;

import com.platform.entity.CdtMerchantEntity;
import com.platform.service.CdtMerchantService;
import com.platform.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v2/cdtMerchant")
@Slf4j
public class ApiCdtMerchantController {

    @Autowired
    private CdtMerchantService cdtMerchantService;

    @RequestMapping("/save")
    @ResponseBody
    public R save(@RequestBody CdtMerchantEntity cdtMerchant) {
        cdtMerchantService.save(cdtMerchant);
        return R.ok();
    }
}
