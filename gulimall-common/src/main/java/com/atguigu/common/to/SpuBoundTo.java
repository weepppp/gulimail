package com.atguigu.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author weepppp 2022/7/1 20:55
 * @version V1.0
 **/
@Data
public class SpuBoundTo {
    private Long spuId;
    private BigDecimal buyBounds;
    private BigDecimal growBounds;

}
