package com.tc.mall.common.api;

/**
 * API返回码接口
 * @author honggang.liu
 */
public interface IErrorCode {
    /**
     * 返回码
     */
    long getCode();

    /**
     * 返回信息
     */
    String getMessage();
}
