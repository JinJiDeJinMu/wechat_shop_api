package com.platform.api;

import cn.hutool.http.HttpUtil;
import com.chundengtai.base.bean.Delivery;
import com.chundengtai.base.bean.dto.DeliveryDto;
import com.chundengtai.base.common.Json;
import com.chundengtai.base.service.DeliveryService;
import com.platform.entity.OrderVo;
import com.platform.service.ApiOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/delivery")
@Api(tags = "查看物流接口")
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;

    @Autowired
    private ApiOrderService orderService;

    /**
     * 查看物流接口
     */
    @GetMapping("/check")
    @ApiOperation(value = "查看物流", notes = "根据订单号查看物流")
    @ApiImplicitParam(name = "orderNumber", value = "订单号", required = true, dataType = "String")
    public ResponseEntity<DeliveryDto> checkDelivery(String orderNumber) {

        OrderVo order = orderService.queryOrderNo(orderNumber);
        Delivery delivery = deliveryService.getById(orderNumber);
        //String url = delivery.getQueryUrl().replace("{dvyFlowId}", order.getDvyFlowId());
        String url = delivery.getQueryUrl().replace("{dvyFlowId}", order.getShipping_id().toString());
        String deliveryJson = HttpUtil.get(url);

        DeliveryDto deliveryDto = Json.parseObject(deliveryJson, DeliveryDto.class);
        //deliveryDto.setDvyFlowId(order.getDvyFlowId());
        deliveryDto.setCompanyHomeUrl(delivery.getCompanyHomeUrl());
        deliveryDto.setCompanyName(delivery.getDvyName());
        return ResponseEntity.ok(deliveryDto);
    }
}
