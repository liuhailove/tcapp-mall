package com.tc.mall.portal.dao;

import com.tc.mall.model.CmsSubject;
import com.tc.mall.model.PmsBrand;
import com.tc.mall.model.PmsProduct;
import com.tc.mall.portal.domain.FlashPromotionProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 首页内容管理自定义Dao
 *
 * @author honggang.liu
 */
@Mapper
public interface HomeDao {

    /**
     * 获取推荐品牌
     *
     * @param offset 偏移
     * @param limit  分页大小
     * @return 推荐品牌
     */
    List<PmsBrand> getRecommendBrandList(@Param("offset") Integer offset, @Param("limit") Integer limit);

    /**
     * 获取秒杀商品
     *
     * @param flashPromotionId 秒杀ID
     * @param sessionId        会话ID
     */
    List<FlashPromotionProduct> getFlashProductList(@Param("flashPromotionId") Long flashPromotionId, @Param("sessionId") Long sessionId);

    /**
     * 获取新品推荐
     *
     * @param offset 偏移
     * @param limit  分页大小
     * @return 新品推荐
     */
    List<PmsProduct> getNewProductList(@Param("offset") Integer offset, @Param("limit") Integer limit);

    /**
     * 获取人气推荐
     *
     * @param offset 偏移
     * @param limit  分页大小
     * @return 人气推荐
     */
    List<PmsProduct> getHotProductList(@Param("offset") Integer offset, @Param("limit") Integer limit);

    /**
     * 获取推荐专题
     *
     * @param offset 偏移
     * @param limit  分页大小
     * @return 推荐专题
     */
    List<CmsSubject> getRecommendSubjectList(@Param("offset") Integer offset, @Param("limit") Integer limit);
}
