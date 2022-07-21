package com.atguigu.gulimall.search.service;

import com.atguigu.gulimall.search.vo.SearchParam;

import javax.naming.directory.SearchResult;

/**
 * @author weepppp 2022/7/14 19:31
 **/
public interface MallSearchService {
    SearchResult search(SearchParam param);
}
