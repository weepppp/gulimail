package com.atguigu.gulimall.member.feign;

import com.atguigu.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * @author weepppp 2022/6/25 21:33
 * @version V1.0
 * @since
 **/


/**
 * @功能 此feign接口用于转发属于gulimall-coupon模块的请求
 * @author weepppp
 */
@FeignClient("gulimall-coupon")
public interface CouponFeignService {

    @RequestMapping("/coupon/coupon/member/list")
    public R membercoupons();
}
