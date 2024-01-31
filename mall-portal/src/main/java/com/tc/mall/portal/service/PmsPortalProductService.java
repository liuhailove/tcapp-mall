package com.tc.mall.portal.service;

import com.tc.mall.model.PmsProduct;
import com.tc.mall.portal.domain.PmsPortalProductDetail;
import com.tc.mall.portal.domain.PmsProductCategoryNode;

import java.util.List;

/**
 * 前台商品管理Service
 *
 * @author honggang.liu
 */
public interface PmsPortalProductService {
    /**
     * 综合搜索商品
     *
     * @param keyword           搜索关键字
     * @param brandId           品牌ID
     * @param productCategoryId 产品品类
     * @param pageNum           分页页码
     * @param pageSize          分页大小
     * @param sort              排序
     * @return 符合条件的商品
     */
    List<PmsProduct> search(String keyword, Long brandId, Long productCategoryId, Integer pageNum, Integer pageSize, Integer sort);

    /**
     * 以树形结构获取所有商品分类
     * @return 商品分类
     */
    List<PmsProductCategoryNode> categoryTreeList();

    /**
     * 获取前台商品详情
     * @param id 商品ID
     * @return 商品详情
     */
    PmsPortalProductDetail detail(Long id);
}
