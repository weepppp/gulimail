package com.atguigu.gulimall.ware.vo;

import lombok.Data;
import lombok.NonNull;

import java.util.List;

/**
 * @author weepppp 2022/7/2 14:39
 * @version V1.0
 **/
@Data
public class PurchaseFinishVo {

    @NonNull
    private Long id;

    private List<ItemVo> items;

    public PurchaseFinishVo() {
    }
}
