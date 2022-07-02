package com.atguigu.gulimall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @author weepppp 2022/7/2 11:09
 * @version V1.0
 **/
@Data
public class MergeVo {
    private Long purchaseId;
    private List<Long> items;
}
