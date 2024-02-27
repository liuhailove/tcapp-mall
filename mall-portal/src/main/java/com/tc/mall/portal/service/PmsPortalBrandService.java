package com.tc.mall.portal.service;

import com.tc.mall.common.api.CommonPage;
import com.tc.mall.model.PmsBrand;
import com.tc.mall.model.PmsProduct;

import java.util.List;

/**
 * 前台品牌管理Service
 *
 * @author honggang.liu
 */
public interface PmsPortalBrandService {
    /**
     * 分页获取推荐品牌
     *
     * @param pageNum  页码
     * @param pageSize 分页大小
     * @return 推荐品牌
     */
    List<PmsBrand> recommendList(Integer pageNum, Integer pageSize);

    /**
     * 获取品牌详情
     *
     * @param brandId 品牌ID
     * @return 品牌详情
     */
    PmsBrand detail(Long brandId);

    /**
     * 分页获取品牌关联商品
     *
     * @param brandId  品牌ID
     * @param pageNum  页码
     * @param pageSize 分页大小
     * @return 分页数据
     */
    CommonPage<PmsProduct> productList(Long brandId, Integer pageNum, Integer pageSize);
}
