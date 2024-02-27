package com.tc.mall.portal.service;

import com.tc.mall.model.UmsMemberReceiveAddress;

import java.util.List;

/**
 * 用户地址管理Service
 *
 * @author honggang.liu
 */
public interface UmsMemberReceiveAddressService {
    /**
     * 添加收货地址
     *
     * @param address 收货地址
     * @return 操作结果
     */
    int add(UmsMemberReceiveAddress address);

    /**
     * 删除收货地址
     *
     * @param id 地址表ID
     * @return 操作结果
     */
    int delete(Long id);

    /**
     * 修改收货地址
     *
     * @param id      地址表的id
     * @param address 修改的收货地址信息
     * @return 修改结果
     */
    int update(Long id, UmsMemberReceiveAddress address);

    /**
     * 返回当前用户的收货地址
     *
     * @return 收货地址
     */
    List<UmsMemberReceiveAddress> list();

    /**
     * 获取地址详情
     *
     * @param id 地址id
     * @return 地址详情
     */
    UmsMemberReceiveAddress getItem(Long id);
}
