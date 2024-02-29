package com.tc.mall.portal.service.impl;

import com.github.pagehelper.PageHelper;
import com.tc.mall.mapper.PmsProductMapper;
import com.tc.mall.mapper.UmsMemberReadHistoryMapper;
import com.tc.mall.model.PmsProduct;
import com.tc.mall.model.UmsMember;
import com.tc.mall.model.UmsMemberReadHistory;
import com.tc.mall.model.UmsMemberReadHistoryExample;
import com.tc.mall.portal.service.MemberReadHistoryService;
import com.tc.mall.portal.service.UmsMemberService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
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
     * 浏览历史
     */
    @Resource
    private UmsMemberReadHistoryMapper umsMemberReadHistoryMapper;

    /**
     * 会员服务
     */
    @Resource
    private UmsMemberService memberService;

    /**
     * 生成浏览记录
     *
     * @param memberReadHistory 会员浏览历史
     * @return 操作影响行数
     */
    @Override
    public int create(UmsMemberReadHistory memberReadHistory) {
        if (memberReadHistory.getProductId() == null) {
            return 0;
        }
        UmsMember member = memberService.getCurrentMember();
        memberReadHistory.setMemberId(member.getId());
        memberReadHistory.setMemberNickname(member.getNickname());
        memberReadHistory.setMemberIcon(member.getIcon());
        memberReadHistory.setId(null);
        memberReadHistory.setCreateTime(new Date());
        PmsProduct product = productMapper.selectByPrimaryKey(memberReadHistory.getProductId());
        if (product == null || product.getDeleteStatus() == 1) {
            return 0;
        }
        memberReadHistory.setProductName(product.getName());
        memberReadHistory.setProductSubTitle(product.getSubTitle());
        memberReadHistory.setProductPrice(product.getPrice());
        memberReadHistory.setProductPic(product.getPic());
        umsMemberReadHistoryMapper.insert(memberReadHistory);
        return 1;
    }

    /**
     * 批量删除浏览记录
     *
     * @param ids 主键ID集合
     * @return 影响行数
     */
    @Override
    public int delete(List<String> ids) {
        for (String id : ids) {
            umsMemberReadHistoryMapper.deleteByPrimaryKey(Long.parseLong(id));
        }
        return ids.size();
    }

    /**
     * 分页获取用户浏览历史记录
     *
     * @param pageNum  页码
     * @param pageSize 分页大小
     * @return 分页数
     */
    @Override
    public List<UmsMemberReadHistory> list(Integer pageNum, Integer pageSize) {
        UmsMember member = memberService.getCurrentMember();
        PageHelper.startPage(pageNum, pageSize);
        UmsMemberReadHistoryExample example = new UmsMemberReadHistoryExample();
        example.createCriteria()
                .andMemberIdEqualTo(member.getId());
        return umsMemberReadHistoryMapper.selectByExample(example);
    }

    /**
     * 清空浏览记录
     */
    @Override
    public void clear() {
        UmsMember member = memberService.getCurrentMember();
        UmsMemberReadHistoryExample example = new UmsMemberReadHistoryExample();
        example.createCriteria()
                .andMemberIdEqualTo(member.getId());
        umsMemberReadHistoryMapper.deleteByExample(example);
    }
}
