package com.tc.mall.portal.service.impl;

import com.github.pagehelper.PageHelper;
import com.tc.mall.mapper.PmsProductMapper;
import com.tc.mall.mapper.UmsMemberProductCollectionMapper;
import com.tc.mall.model.*;
import com.tc.mall.portal.service.MemberCollectionService;
import com.tc.mall.portal.service.UmsMemberService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 会员收藏Service实现类
 *
 * @author honggang.liu
 */
@Service
public class MemberCollectionServiceImpl implements MemberCollectionService {

    /**
     * 商品服务
     */
    @Resource
    private PmsProductMapper productMapper;

    /**
     * 会员收藏接口
     */
    @Resource
    private UmsMemberProductCollectionMapper umsMemberProductCollectionMapper;

    /**
     * 会员服务
     */
    @Resource
    private UmsMemberService memberService;

    /**
     * 添加收藏
     *
     * @param productCollection
     */
    @Override
    public int add(UmsMemberProductCollection productCollection) {
        int count = 0;
        if (productCollection.getProductId() == null) {
            return 0;
        }
        UmsMember member = memberService.getCurrentMember();
        productCollection.setMemberId(member.getId());
        productCollection.setMemberNickname(member.getNickname());
        productCollection.setMemberIcon(member.getIcon());
        productCollection.setCreateTime(new Date());
        UmsMemberProductCollectionExample example = new UmsMemberProductCollectionExample();
        example.createCriteria()
                .andMemberIdEqualTo(member.getId())
                .andProductIdEqualTo(productCollection.getProductId());
        List<UmsMemberProductCollection> findCollection = umsMemberProductCollectionMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(findCollection)) {
            PmsProduct product = productMapper.selectByPrimaryKey(productCollection.getProductId());
            if (product == null || product.getDeleteStatus() == 1) {
                return 0;
            }
            productCollection.setProductName(product.getName());
            productCollection.setProductSubTitle(product.getSubTitle());
            productCollection.setProductPrice(product.getPrice());
            productCollection.setProductPic(product.getPic());
            umsMemberProductCollectionMapper.insert(productCollection);
            count = 1;
        }
        return count;
    }

    /**
     * 删除收藏
     *
     * @param productId
     */
    @Override
    public int delete(Long productId) {
        UmsMember member = memberService.getCurrentMember();
        UmsMemberProductCollectionExample example = new UmsMemberProductCollectionExample();
        example.createCriteria()
                .andMemberIdEqualTo(member.getId())
                .andProductIdEqualTo(productId);
        return umsMemberProductCollectionMapper.deleteByExample(example);
    }

    /**
     * 分页查询收藏
     *
     * @param pageNum
     * @param pageSize
     */
    @Override
    public List<UmsMemberProductCollection> list(Integer pageNum, Integer pageSize) {
        UmsMember member = memberService.getCurrentMember();
        PageHelper.startPage(pageNum, pageSize);
        UmsMemberProductCollectionExample example = new UmsMemberProductCollectionExample();
        example.createCriteria()
                .andMemberIdEqualTo(member.getId());
        return umsMemberProductCollectionMapper.selectByExample(example);
    }

    /**
     * 查看收藏详情
     *
     * @param productId
     */
    @Override
    public UmsMemberProductCollection detail(Long productId) {
        UmsMember member = memberService.getCurrentMember();
        UmsMemberProductCollectionExample example = new UmsMemberProductCollectionExample();
        example.createCriteria()
                .andMemberIdEqualTo(member.getId())
                .andProductIdEqualTo(productId);
        List<UmsMemberProductCollection> memberProductCollections = umsMemberProductCollectionMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(memberProductCollections)) {
            return null;
        }
        return memberProductCollections.get(0);
    }

    /**
     * 清空收藏
     */
    @Override
    public void clear() {
        UmsMember member = memberService.getCurrentMember();
        UmsMemberProductCollectionExample example = new UmsMemberProductCollectionExample();
        example.createCriteria()
                .andMemberIdEqualTo(member.getId());
        umsMemberProductCollectionMapper.deleteByExample(example);
    }
}
