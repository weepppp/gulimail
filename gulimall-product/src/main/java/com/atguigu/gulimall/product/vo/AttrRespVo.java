package com.atguigu.gulimall.product.vo;

import lombok.Data;

/**
 * @author weepppp 2022/6/30 19:49
 * @version V1.0
 **/
@Data
public class AttrRespVo extends AttrVo {
    private String catelogName;
    private String groupName;
    private Long[] catelogPath;

}
