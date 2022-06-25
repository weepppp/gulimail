package com.atguigu.gulimall.coupon.dao;

import com.atguigu.gulimall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author weepppp
 * @email zoeaki@163.com
 * @date 2022-06-25 15:00:44
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
