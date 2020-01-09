package com.platform.controller;

import com.chundengtai.base.bean.CdtProdcutTrace;
import com.chundengtai.base.service.CdtProdcutTraceService;
import com.platform.annotation.IgnoreAuth;
import com.platform.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/h5")
public class WxH5ProdcutTraceController {
    @Autowired
    public CdtProdcutTraceService cdtProdcutTraceService;

    /**
     * 信息
     */
    @GetMapping("/getModel/{id}.json")
    @IgnoreAuth
    public R info(@PathVariable("id") Integer id) {
        CdtProdcutTrace model = cdtProdcutTraceService.getById(id);
        return R.ok(model);
    }

}
