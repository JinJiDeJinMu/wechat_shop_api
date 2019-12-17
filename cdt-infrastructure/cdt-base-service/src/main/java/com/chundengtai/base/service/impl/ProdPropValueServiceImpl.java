package com.chundengtai.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chundengtai.base.bean.ProdPropValue;
import com.chundengtai.base.dao.ProdPropValueMapper;
import com.chundengtai.base.service.ProdPropValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by lgh on 2018/07/06.
 */
@Service
public class ProdPropValueServiceImpl extends ServiceImpl<ProdPropValueMapper, ProdPropValue> implements ProdPropValueService {

    @Autowired
    private ProdPropValueMapper prodPropValueMapper;

}
