package com.tc.mall.portal.service.impl;

import com.tc.mall.mapper.*;
import com.tc.mall.model.*;
import com.tc.mall.portal.dao.PortalProductDao;
import com.tc.mall.portal.domain.PmsPortalProductDetail;
import com.tc.mall.portal.domain.PmsProductCategoryNode;
import com.tc.mall.portal.service.PmsPortalProductService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 前台订单管理Service
 *
 * @author honggang.liu
 */
@Service
public class PmsPortalProductServiceImpl implements PmsPortalProductService {

    /**
     * 商品Mapper
     */
    @Resource
    private PmsProductMapper productMapper;

    /**
     * 品牌Mapper
     */
    @Resource
    private PmsBrandMapper brandMapper;

    /**
     * 商品属性Mapper
     */
    @Resource
    private PmsProductAttributeMapper productAttributeMapper;

    /**
     * 商品属性值
     */
    @Resource
    private PmsProductAttributeValueMapper productAttributeValueMapper;

    /**
     * sku库存Mapper
     */
    @Resource
    private PmsSkuStockMapper skuStockMapper;

    /**
     * 阶梯价格
     */
    @Resource
    private PmsProductLadderMapper productLadderMapper;

    /**
     * 满减Mapper
     */
    @Resource
    private PmsProductFullReductionMapper productFullReductionMapper;

    /**
     * 商品Dao
     */
    @Resource
    private PortalProductDao portalProductDao;

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
    @Override
    public List<PmsProduct> search(String keyword, Long brandId, Long productCategoryId, Integer pageNum, Integer pageSize, Integer sort) {
        return null;
    }

    /**
     * 以树形结构获取所有商品分类
     *
     * @return 商品分类
     */
    @Override
    public List<PmsProductCategoryNode> categoryTreeList() {
        return null;
    }

    /**
     * 获取前台商品详情
     *
     * @param id 商品ID
     * @return 商品详情
     */
    @Override
    public PmsPortalProductDetail detail(Long id) {
        PmsPortalProductDetail result = new PmsPortalProductDetail();
        // 获取商品信息
        PmsProduct product = productMapper.selectByPrimaryKey(id);
        result.setProduct(product);
        // 获取品牌信息
        PmsBrand brand = brandMapper.selectByPrimaryKey(product.getBrandId());
        result.setBrand(brand);
        // 获取商品属性信息
        PmsProductAttributeExample attributeExample = new PmsProductAttributeExample();
        attributeExample.createCriteria().andProductAttributeCategoryIdEqualTo(product.getProductAttributeCategoryId());
        List<PmsProductAttribute> productAttributeList = productAttributeMapper.selectByExample(attributeExample);
        result.setProductAttributeList(productAttributeList);
        // 获取商品属性值信息
        if (!CollectionUtils.isEmpty(productAttributeList)) {
            List<Long> attributeIds = productAttributeList.stream().map(PmsProductAttribute::getId).collect(Collectors.toList());
            PmsProductAttributeValueExample attributeValueExample = new PmsProductAttributeValueExample();
            attributeValueExample.createCriteria().andProductIdEqualTo(product.getId())
                    .andProductAttributeIdIn(attributeIds);
            List<PmsProductAttributeValue> productAttributeValueList = productAttributeValueMapper.selectByExample(attributeValueExample);
            result.setProductAttributeValueList(productAttributeValueList);
        }
        // 获取商品SKU库存信息
        PmsSkuStockExample skuStockExample = new PmsSkuStockExample();
        skuStockExample.createCriteria().andProductIdEqualTo(product.getId());
        List<PmsSkuStock> skuStockList = skuStockMapper.selectByExample(skuStockExample);
        result.setSkuStockList(skuStockList);
        // 商品阶梯价格设置
        if (product.getPromotionType() == 3) {
            PmsProductLadderExample ladderExample = new PmsProductLadderExample();
            ladderExample.createCriteria().andProductIdEqualTo(product.getId());
            List<PmsProductLadder> productLadderList = productLadderMapper.selectByExample(ladderExample);
            result.setProductLadderList(productLadderList);
        }
        // 商品满减价格设置
        if (product.getPromotionType() == 4) {
            PmsProductFullReductionExample fullReductionExample = new PmsProductFullReductionExample();
            fullReductionExample.createCriteria().andProductIdEqualTo(product.getId());
            List<PmsProductFullReduction> productFullReductionList = productFullReductionMapper.selectByExample(fullReductionExample);
            result.setProductFullReductionList(productFullReductionList);
        }
        // 商品可用优惠券
        result.setCouponList(portalProductDao.getAvailableCouponList(product.getId(), product.getProductCategoryId()));
        return result;
    }
}
