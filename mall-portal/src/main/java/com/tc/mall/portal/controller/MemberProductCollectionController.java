package com.tc.mall.portal.controller;

import com.tc.mall.common.api.CommonPage;
import com.tc.mall.common.api.CommonResult;
import com.tc.mall.model.UmsMemberProductCollection;
import com.tc.mall.portal.service.MemberCollectionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 会员商品收藏管理Controller
 *
 * @author honggang.liu
 */
@Controller
@Api(tags = "MemberCollectionController")
@Tag(name = "MemberCollectionController", description = "会员收藏管理")
@RequestMapping("/member/productCollection")
public class MemberProductCollectionController {

    /**
     * 会员商品收藏服务
     */
    @Resource
    private MemberCollectionService memberCollectionService;

    @ApiOperation("添加商品收藏")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult add(@RequestBody UmsMemberProductCollection productCollection) {
        int count = memberCollectionService.add(productCollection);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("删除商品收藏")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(Long productId) {
        int count = memberCollectionService.delete(productId);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("显示当前用户商品收藏列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<UmsMemberProductCollection>> list(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                                  @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        List<UmsMemberProductCollection> page = memberCollectionService.list(pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(page));
    }

    @ApiOperation("显示商品收藏详情")
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<UmsMemberProductCollection> detail(@RequestParam Long productId) {
        UmsMemberProductCollection memberProductCollection = memberCollectionService.detail(productId);
        return CommonResult.success(memberProductCollection);
    }

    @ApiOperation("清空当前用户商品收藏列表")
    @RequestMapping(value = "/clear", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult clear() {
        memberCollectionService.clear();
        return CommonResult.success(null);
    }
}
