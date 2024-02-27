package com.tc.mall.portal.domain;

import com.tc.mall.model.OmsOrder;
import com.tc.mall.model.OmsOrderItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 包含商品信息的订单详情
 *
 * @author honggang.liu
 */
@Getter
@Setter
public class OmsOrderDetail extends OmsOrder {
    /**
     * 订单商品列表
     */
    @ApiModelProperty("订单商品列表")
    private List<OmsOrderItem> orderItemList;
}
