package com.tc.mall.admin.service;

import com.tc.mall.admin.dto.PmsProductParam;
import com.tc.mall.admin.dto.PmsProductQueryParam;
import com.tc.mall.model.PmsProduct;

import java.util.List;

/**
 * 商品管理
 *
 * @author honggang.liu
 */
public interface PmsProductService {

    /**
     * 创建商品
     *
     * @param productParam 创建商品参数
     * @return 增加结果
     */
    int create(PmsProductParam productParam);

    /**
     * 更新商品
     *
     * @param id           商品ID
     * @param productParam 更新参数
     * @return 更新结果
     */
    int update(Long id, PmsProductParam productParam);

    /**
     * 分页查找商品
     *
     * @param productQueryParam 查询参数
     * @param pageSize          分页大小
     * @param pageNum           页码
     * @return 产品列表
     */
    List<PmsProduct> list(PmsProductQueryParam productQueryParam, Integer pageSize, Integer pageNum);

    /**
     * 更新Verify状态
     *
     * @param ids          商品ID集合
     * @param verifyStatus 审核状态
     * @param detail       明细
     * @return 审核更新结果
     */
    int updateVerifyStatus(List<Long> ids, Integer verifyStatus, String detail);

    /**
     * 批量修改商品上架状态
     *
     * @param ids           商品ID集合
     * @param publishStatus 上架状态
     * @return 更新结果
     */
    int updatePublishStatus(List<Long> ids, Integer publishStatus);

    /**
     * 批量修改商品推荐状态
     *
     * @param ids             商品ID集合
     * @param recommendStatus 推荐状态
     * @return 更新结果
     */
    int updateRecommendStatus(List<Long> ids, Integer recommendStatus);

    /**
     * 批量修改新品状态
     *
     * @param ids       商品集合
     * @param newStatus 新品状态
     * @return 更新结果
     */
    int updateNewStatus(List<Long> ids, Integer newStatus);

    /**
     * 批量更新商品删除状态
     *
     * @param ids          商品ID集合
     * @param deleteStatus 删除状态
     * @return 更新结果
     */
    int updateDeleteStatus(List<Long> ids, Integer deleteStatus);

    /**
     * 根据商品名称或者货号模糊查询
     *
     * @param keyword 商品名称模糊关键字
     * @return 返回商品
     */
    List<PmsProduct> list(String keyword);
}
