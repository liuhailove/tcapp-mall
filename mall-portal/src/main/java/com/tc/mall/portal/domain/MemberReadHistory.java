package com.tc.mall.portal.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * 会员商品浏览历史记录
 *
 * @author honggang.liu
 */
@Getter
@Setter
public class MemberReadHistory {
    /**
     * 主键ID
     */
    @Id
    private String id;
    /**
     * 会员ID
     */
    private Long memberId;
    /**
     * 会员昵称
     */
    private String memberNickname;
    /**
     * 会员Icon
     */
    private String memberIcon;
    /**
     * 商品ID
     */
    private Long productId;
    /**
     * 商品名称
     */
    private String productName;
    /**
     * 商品图片
     */
    private String productPic;
    /**
     * 商品子标题
     */
    private String productSubTitle;
    /**
     * 商品价格
     */
    private String productPrice;
    /**
     * 创建时间
     */
    private Date createTime;
}
