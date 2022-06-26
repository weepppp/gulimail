package com.atguigu.gulimall.coupon.controller;

import java.util.Arrays;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.atguigu.gulimall.coupon.entity.CouponEntity;
import com.atguigu.gulimall.coupon.service.CouponService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.R;



/**
 * 优惠券信息
 *
 * @author weepppp
 * @email zoeaki@163.com
 * @date 2022-06-25 15:00:44
 */
@RefreshScope
@RestController
@RequestMapping("coupon/coupon")
public class CouponController {
    
    /**
     * @功能 [演示Nacos作为配置中心的使用]
     * 传统方法：从系统配置文件中获取配置
     * 缺点：无法适配需要在服务在线同时动态修改变量数据的请求
     * 解决：在Nacos配置中心中设置配置文件内容，只要修改配置中心，其他数据都会发生相应改变
     * 操作：
     * 1、导入配置中心依赖，设置bootstrap.properties配置中心地址
     * 2、添加gulimall-coupon.properties的Data Id数据集到配置中心，添加 user1.name=zhangsan，user1.age=18作为配置的内容
     * 3、在本类增加刷新配置的注解@RefreshScope
     * 4、注意：要配置不同的使用环境，可以新建配置空间新建配置，在bootstrap.properties配置切换使用环境，设置配置组也同理
     * 5、在当前项目中，我们给不同微服务创建相应的配置空间，该模块的所有配置拆分放置在该空间下；通过配置组区分dev和prod的环境
     */
    @Value("${user1.name}")
    private String name;

    @Value("${user1.age}")
    private String age;

    @RequestMapping("/test")
    public R test(){
        return R.ok().put("name",name).put("age",age);
    }

    @Autowired
    private CouponService couponService;

    @RequestMapping("/member/list")
    public R membercoupons(){
        CouponEntity couponEntity = new CouponEntity();
        couponEntity.setCouponName("满100减10");
        return  R.ok().put("coupons",Arrays.asList(couponEntity));
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = couponService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		CouponEntity coupon = couponService.getById(id);

        return R.ok().put("coupon", coupon);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CouponEntity coupon){
		couponService.save(coupon);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CouponEntity coupon){
		couponService.updateById(coupon);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		couponService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
