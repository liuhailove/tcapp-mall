package com.tc.mall.portal.controller;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 会员优惠券管理Controller
 *
 * @author honggang.liu
 */
@Controller
@Api(tags = "UmsMemberCouponController")
@Tag(name="UmsMemberCouponController",description ="用户优惠券管理")
@RequestMapping("/member/coupon")
public class UmsMemberCouponController {
}
