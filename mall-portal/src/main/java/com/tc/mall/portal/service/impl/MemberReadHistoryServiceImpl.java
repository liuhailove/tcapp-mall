package com.tc.mall.portal.service.impl;

import com.github.pagehelper.Page;
import com.tc.mall.mapper.PmsProductMapper;
import com.tc.mall.portal.domain.MemberReadHistory;
import com.tc.mall.portal.service.MemberReadHistoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 会员浏览记录管理Service实现类
 *
 * @author honggang.liu
 */
@Service
public class MemberReadHistoryServiceImpl implements MemberReadHistoryService {
    /**
     * 商品Mapper
     */
    @Resource
    private PmsProductMapper productMapper;


    /**
     * 生成浏览记录
     *
     * @param memberReadHistory 会员浏览历史
     * @return 操作影响行数
     */
    @Override
    public int create(MemberReadHistory memberReadHistory) {
        return 0;
    }

    /**
     * 批量删除浏览记录
     *
     * @param ids 主键ID集合
     * @return 影响行数
     */
    @Override
    public int delete(List<String> ids) {
        return 0;
    }

    /**
     * 分页获取用户浏览历史记录
     *
     * @param pageNum  页码
     * @param pageSize 分页大小
     * @return 分页数
     */
    @Override
    public Page<MemberReadHistory> list(Integer pageNum, Integer pageSize) {
        return null;
    }

    /**
     * 清空浏览记录
     */
    @Override
    public void clear() {

    }
}
