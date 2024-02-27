package com.tc.mall.portal.service.impl;

import com.tc.mall.mapper.UmsMemberReceiveAddressMapper;
import com.tc.mall.model.UmsMember;
import com.tc.mall.model.UmsMemberReceiveAddress;
import com.tc.mall.model.UmsMemberReceiveAddressExample;
import com.tc.mall.portal.service.UmsMemberReceiveAddressService;
import com.tc.mall.portal.service.UmsMemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户地址管理Service实现类
 *
 * @author honggang.liu
 */
@Service
public class UmsMemberReceiveAddressServiceImpl implements UmsMemberReceiveAddressService {

    /**
     * 会员服务
     */
    @Resource
    private UmsMemberService memberService;

    /**
     * 收货地址Mapper
     */
    @Resource
    private UmsMemberReceiveAddressMapper addressMapper;

    /**
     * 添加收货地址
     *
     * @param address 收货地址
     * @return 操作结果
     */
    @Override
    public int add(UmsMemberReceiveAddress address) {
        UmsMember currentMember = memberService.getCurrentMember();
        address.setMemberId(currentMember.getId());
        return addressMapper.insert(address);
    }

    /**
     * 删除收货地址
     *
     * @param id 地址表ID
     * @return 操作结果
     */
    @Override
    public int delete(Long id) {
        UmsMember currentMember = memberService.getCurrentMember();
        UmsMemberReceiveAddressExample example = new UmsMemberReceiveAddressExample();
        example.createCriteria().andMemberIdEqualTo(currentMember.getId()).andIdEqualTo(id);
        return addressMapper.deleteByExample(example);
    }

    /**
     * 修改收货地址
     *
     * @param id      地址表的id
     * @param address 修改的收货地址信息
     * @return 修改结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int update(Long id, UmsMemberReceiveAddress address) {
        address.setId(null);
        UmsMember currentMember = memberService.getCurrentMember();
        UmsMemberReceiveAddressExample example = new UmsMemberReceiveAddressExample();
        example.createCriteria().andMemberIdEqualTo(currentMember.getId()).andIdEqualTo(id);
        if (address.getDefaultStatus() == null) {
            address.setDefaultStatus(0);
        }
        if (address.getDefaultStatus() == 1) {
            // 先将原来的默认地址去除
            UmsMemberReceiveAddress record = new UmsMemberReceiveAddress();
            record.setDefaultStatus(0);
            UmsMemberReceiveAddressExample updateExample = new UmsMemberReceiveAddressExample();
            updateExample.createCriteria()
                    .andMemberIdEqualTo(currentMember.getId())
                    .andDefaultStatusEqualTo(1);
            addressMapper.updateByExampleSelective(record, updateExample);
        }
        return addressMapper.updateByExampleSelective(address, example);
    }

    /**
     * 返回当前用户的收货地址
     *
     * @return 收货地址
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<UmsMemberReceiveAddress> list() {
        UmsMember currentMember = memberService.getCurrentMember();
        UmsMemberReceiveAddressExample example = new UmsMemberReceiveAddressExample();
        example.createCriteria().andMemberIdEqualTo(currentMember.getId());
        return addressMapper.selectByExample(example);
    }

    /**
     * 获取地址详情
     *
     * @param id 地址id
     * @return 地址详情
     */
    @Override
    public UmsMemberReceiveAddress getItem(Long id) {
        UmsMember currentMember = memberService.getCurrentMember();
        UmsMemberReceiveAddressExample example = new UmsMemberReceiveAddressExample();
        example.createCriteria().andMemberIdEqualTo(currentMember.getId()).andIdEqualTo(id);
        List<UmsMemberReceiveAddress> addressList = addressMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(addressList)) {
            return addressList.get(0);
        }
        return null;
    }
}
