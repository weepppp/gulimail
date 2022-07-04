package com.atguigu.common.to;

import lombok.Data;

/**
 * @author weepppp 2022/7/4 20:24
 * @version V1.0
 **/
@Data
public class SkuHasStockVo {
    private Long skuId;
    private Boolean hasStock;
}
