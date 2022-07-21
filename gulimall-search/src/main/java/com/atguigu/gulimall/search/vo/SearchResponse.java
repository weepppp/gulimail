package com.atguigu.gulimall.search.vo;

import com.atguigu.common.to.es.SkuEsModel;
import lombok.Data;

import java.util.List;

/**
 * @author weepppp 2022/7/14 19:48
 **/
@Data
public class SearchResponse {

    private List<SkuEsModel> products;
    private Integer pageNum;
    private Long total;
    private Integer totalPages;
    private List<BrandVo> brands;
    private List<CatalogVo> catalogs;
    private List<AttrVo> attrs;

    @Data
    private static class BrandVo {
        private Long brandId;
        private String brandName;
        private String brandImg;
    }

    @Data
    private static class CatalogVo {
        private Long catalogId;
        private String catalogName;

    }

    @Data
    private static class AttrVo {
        private Long AttrId;
        private String attrName;
        private List<String> attrValue;
    }
}
