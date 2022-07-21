package com.atguigu.gulimall.search.vo;

import lombok.Data;

import java.util.List;

/**
 * @author weepppp 2022/7/14 19:29
 **/
@Data
public class SearchParam {

    private String keyword;
    private Long catalog3Id;
    private String sort;
    private Integer hasStork;
    private String skuPrice;
    private List<Long> brandId;
    private List<String> attrs;
    private Integer pageNum;
}
