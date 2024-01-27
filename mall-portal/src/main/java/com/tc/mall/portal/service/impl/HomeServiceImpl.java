package com.tc.mall.portal.service.impl;

import com.github.pagehelper.PageHelper;
import com.tc.mall.mapper.PmsProductMapper;
import com.tc.mall.model.CmsSubject;
import com.tc.mall.model.PmsProduct;
import com.tc.mall.model.PmsProductCategory;
import com.tc.mall.model.PmsProductExample;
import com.tc.mall.portal.domain.HomeContentResult;
import com.tc.mall.portal.service.HomeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 首页内容管理Service实现类
 *
 * @author honggang.liu
 */
@Service
public class HomeServiceImpl implements HomeService {

    /**
     * 产品Mapper
     */
    @Resource
    private PmsProductMapper productMapper;

    /**
     * 获取首页内容
     */
    @Override
    public HomeContentResult content() {
        return null;
    }

    /**
     * 首页商品推荐
     *
     * @param pageSize 分页大小
     * @param pageNum  第几页
     */
    @Override
    public List<PmsProduct> recommendProductList(Integer pageSize, Integer pageNum) {
        // TODO 暂时默认推荐所有商品，等待推荐系统介入后修改
        PageHelper.startPage(pageNum, pageSize);
        PmsProductExample example = new PmsProductExample();
        example.createCriteria()
                .andDeleteStatusEqualTo(0)
                .andPublishStatusEqualTo(1);
        return productMapper.selectByExample(example);
    }

    /**
     * 获取商品分类
     *
     * @param parentId 0:获取一级分类；其他：获取指定二级分类
     */
    @Override
    public List<PmsProductCategory> getProductCateList(Long parentId) {
        return null;
    }

    /**
     * 根据专题分类分页获取专题
     *
     * @param cateId   专题分类id
     * @param pageSize
     * @param pageNum
     */
    @Override
    public List<CmsSubject> getSubjectList(Long cateId, Integer pageSize, Integer pageNum) {
        return null;
    }

    /**
     * 分页获取人气推荐商品
     *
     * @param pageNum
     * @param pageSize
     */
    @Override
    public List<PmsProduct> hotProductList(Integer pageNum, Integer pageSize) {
        return null;
    }

    /**
     * 分页获取新品推荐商品
     *
     * @param pageNum
     * @param pageSize
     */
    @Override
    public List<PmsProduct> newProductList(Integer pageNum, Integer pageSize) {
        return null;
    }
}
