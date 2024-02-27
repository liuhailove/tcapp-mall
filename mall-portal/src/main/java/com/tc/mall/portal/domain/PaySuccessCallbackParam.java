package com.tc.mall.portal.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 支付成功回调参数
 *
 * @author honggang.liu
 */
@Data
@EqualsAndHashCode
public class PaySuccessCallbackParam {
    private Long orderId;
    private Integer payType;
}
