package com.tc.mall.portal.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 订单删除回调参数
 *
 * @author honggang.liu
 */
@Data
@EqualsAndHashCode
public class DeleteOrderCallbackParam {
    private Long orderId;
}
