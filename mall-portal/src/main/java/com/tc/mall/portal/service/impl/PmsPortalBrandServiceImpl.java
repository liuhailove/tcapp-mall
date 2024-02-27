package com.tc.mall.portal.service.impl;

import com.github.pagehelper.PageHelper;
import com.tc.mall.common.api.CommonPage;
import com.tc.mall.mapper.PmsBrandMapper;
import com.tc.mall.mapper.PmsProductMapper;
import com.tc.mall.model.PmsBrand;
import com.tc.mall.model.PmsProduct;
import com.tc.mall.model.PmsProductExample;
import com.tc.mall.portal.dao.HomeDao;
import com.tc.mall.portal.service.PmsPortalBrandService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 前台品牌管理Service实现类
 *
 * @author honggang.liu
 */
@Service
public class PmsPortalBrandServiceImpl implements PmsPortalBrandService {

    /**
     * home dao
     */
    @Resource
    private HomeDao homeDao;

    /**
     * 品牌mapper
     */
    @Resource
    private PmsBrandMapper brandMapper;

    /**
     * 产品Mapper
     */
    @Resource
    private PmsProductMapper productMapper;

    /**
     * 分页获取推荐品牌
     *
     * @param pageNum  页码
     * @param pageSize 分页大小
     * @return 推荐品牌
     */
    @Override
    public List<PmsBrand> recommendList(Integer pageNum, Integer pageSize) {
        int offset=(pageNum-1)*pageSize;
        return homeDao.getRecommendBrandList(offset,pageSize);
    }

    /**
     * 获取品牌详情
     *
     * @param brandId 品牌ID
     * @return 品牌详情
     */
    @Override
    public PmsBrand detail(Long brandId) {
        return brandMapper.selectByPrimaryKey(brandId);
    }

    /**
     * 分页获取品牌关联商品
     *
     * @param brandId  品牌ID
     * @param pageNum  页码
     * @param pageSize 分页大小
     * @return 分页数据
     */
    @Override
    public CommonPage<PmsProduct> productList(Long brandId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        PmsProductExample example=new PmsProductExample();
        example.createCriteria().andDeleteStatusEqualTo(0)
                .andPublishStatusEqualTo(1)
                .andBrandIdEqualTo(brandId);
        List<PmsProduct> productList=productMapper.selectByExample(example);
        return CommonPage.restPage(productList);
    }
}
