package com.tc.mall.admin.service.impl;

import com.tc.mall.admin.dao.*;
import com.tc.mall.admin.dto.PmsProductParam;
import com.tc.mall.admin.dto.PmsProductQueryParam;
import com.tc.mall.admin.service.PmsProductService;
import com.tc.mall.mapper.*;
import com.tc.mall.model.PmsProduct;
import com.tc.mall.model.PmsSkuStock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 商品管理Service实现
 *
 * @author honggang.liu
 */
@Service
public class PmsProductServiceImpl implements PmsProductService {
    /**
     * LOGGER
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PmsProductServiceImpl.class);

    /**
     * 产品DAO
     */
    @Resource
    private PmsProductMapper productDaoMapper;

    /**
     * 会员价dao
     */
    @Resource
    private PmsMemberPriceDao memberPriceDao;

    /**
     * 会员价mapper
     */
    @Resource
    private PmsMemberPriceMapper pmsMemberPriceMapper;

    /**
     * 阶梯价dao
     */
    @Resource
    private PmsProductLadderDao productLadderDao;

    /**
     * 阶梯价Mapper
     */
    @Resource
    private PmsProductLadderMapper pmsProductLadderMapper;

    /**
     * 满减Dao
     */
    @Resource
    private PmsProductFullReductionDao productFullReductionDao;

    /**
     * 满减Mapper
     */
    @Resource
    private PmsProductFullReductionMapper productFullReductionMapper;

    /**
     * sku库存Dao
     */
    @Resource
    private PmsSkuStockDao skuStockDao;

    /**
     * 库存Mapper
     */
    @Resource
    private PmsSkuStockMapper skuStockMapper;

    /**
     * 商品属性值Dao
     */
    @Resource
    private PmsProductAttributeValueDao productAttributeValueDao;

    /**
     * 商品属性值Mapper
     */
    @Resource
    private PmsProductAttributeValueMapper productAttributeValueMapper;

    /**
     * 商品主题关系Dao
     */
    @Resource
    private CmsSubjectProductRelationDao subjectProductRelationDao;

    /**
     * 优选商品关系Dao
     */
    @Resource
    private CmsPrefrenceAreaProductRelationDao prefrenceAreaProductRelationDao;

    /**
     * 创建商品
     *
     * @param productParam 创建商品参数
     * @return 增加结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int create(PmsProductParam productParam) {
        int count;
        // 创建商品
        productParam.setId(null);
        productDaoMapper.insertSelective(productParam);
        // 根据促销类型设置价格：会员价格、阶梯价格、满减价格
        Long productId = productParam.getId();
        // 会员价格
        relateAndInsertList(memberPriceDao, productParam.getMemberPriceList(), productId);
        // 阶梯价格
        relateAndInsertList(productLadderDao, productParam.getProductLadderList(), productId);
        // 满减价格
        relateAndInsertList(productFullReductionDao, productParam.getProductFullReductionList(), productId);
        // 处理SKU的编码
        handleSkuStockCode(productParam.getSkuStockList(), productId);
        // 添加SKU库存信息
        relateAndInsertList(skuStockDao, productParam.getSkuStockList(), productId);
        // 添加商品参数，添加自定义商品规则
        relateAndInsertList(productAttributeValueDao, productParam.getProductAttributeValueList(), productId);
        // 关键专题
        relateAndInsertList(subjectProductRelationDao, productParam.getSubjectProductRelationList(), productId);
        // 关联优选
        relateAndInsertList(prefrenceAreaProductRelationDao, productParam.getPrefrenceAreaProductRelationList(), productId);
        count = 1;
        return count;
    }

    /**
     * 更新商品
     *
     * @param id           商品ID
     * @param productParam 更新参数
     * @return 更新结果
     */
    @Override
    public int update(Long id, PmsProductParam productParam) {
        return 0;
    }

    /**
     * 分页查找商品
     *
     * @param productQueryParam 查询参数
     * @param pageSize          分页大小
     * @param pageNum           页码
     * @return 产品列表
     */
    @Override
    public List<PmsProduct> list(PmsProductQueryParam productQueryParam, Integer pageSize, Integer pageNum) {
        return null;
    }

    /**
     * 更新Verify状态
     *
     * @param ids          商品ID集合
     * @param verifyStatus 审核状态
     * @param detail       明细
     * @return 审核更新结果
     */
    @Override
    public int updateVerifyStatus(List<Long> ids, Integer verifyStatus, String detail) {
        return 0;
    }

    /**
     * 批量修改商品上架状态
     *
     * @param ids           商品ID集合
     * @param publishStatus 上架状态
     * @return 更新结果
     */
    @Override
    public int updatePublishStatus(List<Long> ids, Integer publishStatus) {
        return 0;
    }

    /**
     * 批量修改商品推荐状态
     *
     * @param ids             商品ID集合
     * @param recommendStatus 推荐状态
     * @return 更新结果
     */
    @Override
    public int updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        return 0;
    }

    /**
     * 批量修改新品状态
     *
     * @param ids       商品集合
     * @param newStatus 新品状态
     * @return 更新结果
     */
    @Override
    public int updateNewStatus(List<Long> ids, Integer newStatus) {
        return 0;
    }

    /**
     * 批量更新商品删除状态
     *
     * @param ids          商品ID集合
     * @param deleteStatus 删除状态
     * @return 更新结果
     */
    @Override
    public int updateDeleteStatus(List<Long> ids, Integer deleteStatus) {
        return 0;
    }

    /**
     * 根据商品名称或者货号模糊查询
     *
     * @param keyword 商品名称模糊关键字
     * @return 返回商品
     */
    @Override
    public List<PmsProduct> list(String keyword) {
        return null;
    }

    /**
     * 建立和插入关系表操作
     *
     * @param dao       可以操作的dao
     * @param dataList  要插入的数据
     * @param productId 建立关系的id
     */
    private void relateAndInsertList(Object dao, List dataList, Long productId) {
        try {
            if (CollectionUtils.isEmpty(dataList)) {
                return;
            }
            for (Object item : dataList) {
                Method setId = item.getClass().getMethod("setId", Long.class);
                setId.invoke(item, (Long) null);
                Method setProductId = item.getClass().getMethod("setProductId", Long.class);
                setProductId.invoke(item, productId);
            }
            Method insertList = dao.getClass().getMethod("insertList", List.class);
            insertList.invoke(dao, dataList);
        } catch (Exception e) {
            LOGGER.warn("创建产品出错:{}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    private void handleSkuStockCode(List<PmsSkuStock> skuStockList, Long productId) {
        if (CollectionUtils.isEmpty(skuStockList)) {
            return;
        }
        for (int i = 0; i < skuStockList.size(); i++) {
            PmsSkuStock skuStock = skuStockList.get(i);
            if (StringUtils.isEmpty(skuStock.getSkuCode())) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                //日期
                String sb = sdf.format(new Date()) +
                        //四位商品id
                        String.format("%04d", productId) +
                        //三位索引id
                        String.format("%03d", i + 1);
                skuStock.setSkuCode(sb);
            }
        }
    }
}
