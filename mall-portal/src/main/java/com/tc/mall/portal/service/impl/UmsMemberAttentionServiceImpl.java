package com.tc.mall.portal.service.impl;

import com.github.pagehelper.PageHelper;
import com.tc.mall.mapper.PmsBrandMapper;
import com.tc.mall.mapper.UmsMemberBrandAttentionMapper;
import com.tc.mall.model.*;
import com.tc.mall.portal.service.MemberAttentionService;
import com.tc.mall.portal.service.UmsMemberService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 会员关注品牌
 *
 * @author honggang.liu
 */
@Service
public class UmsMemberAttentionServiceImpl implements MemberAttentionService {
    /**
     * 商品服务
     */
    @Resource
    private PmsBrandMapper brandMapper;

    /**
     * 会员品牌收藏接口
     */
    @Resource
    private UmsMemberBrandAttentionMapper umsMemberBrandAttentionMapper;

    /**
     * 会员服务
     */
    @Resource
    private UmsMemberService memberService;

    /**
     * 添加关注
     *
     * @param umsMemberBrandAttention 关注model
     * @return 影响行数
     */
    @Override
    public int add(UmsMemberBrandAttention umsMemberBrandAttention) {
        int count = 0;
        if (umsMemberBrandAttention.getBrandId() == null) {
            return 0;
        }
        UmsMember member = memberService.getCurrentMember();
        umsMemberBrandAttention.setMemberId(member.getId());
        umsMemberBrandAttention.setMemberNickname(member.getNickname());
        umsMemberBrandAttention.setMemberIcon(member.getIcon());
        umsMemberBrandAttention.setCreateTime(new Date());
        UmsMemberBrandAttentionExample example = new UmsMemberBrandAttentionExample();
        example.createCriteria()
                .andMemberIdEqualTo(member.getId())
                .andBrandIdEqualTo(umsMemberBrandAttention.getBrandId());
        List<UmsMemberBrandAttention> findAttention = umsMemberBrandAttentionMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(findAttention)) {
            PmsBrand brand = brandMapper.selectByPrimaryKey(umsMemberBrandAttention.getBrandId());
            if (brand == null) {
                return 0;
            }
            umsMemberBrandAttention.setBrandCity(null);
            umsMemberBrandAttention.setBrandName(brand.getName());
            umsMemberBrandAttention.setBrandLogo(brand.getLogo());
            umsMemberBrandAttentionMapper.insert(umsMemberBrandAttention);
            count = 1;
        }
        return count;
    }

    /**
     * 取消关注
     *
     * @param brandId 品牌ID
     * @return 影响行数
     */
    @Override
    public int delete(Long brandId) {
        UmsMember member = memberService.getCurrentMember();
        UmsMemberBrandAttentionExample example = new UmsMemberBrandAttentionExample();
        example.createCriteria()
                .andMemberIdEqualTo(member.getId())
                .andBrandIdEqualTo(brandId);
        return umsMemberBrandAttentionMapper.deleteByExample(example);
    }

    /**
     * 获取用户关注列表
     *
     * @param pageNum  页码
     * @param pageSize 页大小
     * @return 分页列表
     */
    @Override
    public List<UmsMemberBrandAttention> list(Integer pageNum, Integer pageSize) {
        UmsMember member = memberService.getCurrentMember();
        PageHelper.startPage(pageNum, pageSize);
        UmsMemberBrandAttentionExample example = new UmsMemberBrandAttentionExample();
        example.createCriteria()
                .andMemberIdEqualTo(member.getId());
        return umsMemberBrandAttentionMapper.selectByExample(example);
    }

    /**
     * 获取用户关注详情
     *
     * @param brandId 品牌ID
     * @return 关注详细
     */
    @Override
    public UmsMemberBrandAttention detail(Long brandId) {
        UmsMember member = memberService.getCurrentMember();
        UmsMemberBrandAttentionExample example = new UmsMemberBrandAttentionExample();
        example.createCriteria()
                .andMemberIdEqualTo(member.getId())
                .andBrandIdEqualTo(brandId);
        List<UmsMemberBrandAttention> memberBrandAttentions = umsMemberBrandAttentionMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(memberBrandAttentions)) {
            return null;
        }
        return memberBrandAttentions.get(0);
    }

    /**
     * 清空关注列表
     */
    @Override
    public void clear() {
        UmsMember member = memberService.getCurrentMember();
        UmsMemberBrandAttentionExample example = new UmsMemberBrandAttentionExample();
        example.createCriteria()
                .andMemberIdEqualTo(member.getId());
        umsMemberBrandAttentionMapper.deleteByExample(example);
    }

}
