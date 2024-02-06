package com.tc.mall.portal.controller;

import com.tc.mall.portal.service.OmsPortalOrderService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * 订单管理Controller
 *
 * @author honggang.liu
 */
@Controller
@Api(tags = "OmsPortalOrderController")
@Tag(name = "OmsPortalOrderController", description = "订单管理")
@RequestMapping("/order")
public class OmsPortalOrderController {

    /**
     * 订单管理服务
     */
    @Resource
    private OmsPortalOrderService portalOrderService;
}
