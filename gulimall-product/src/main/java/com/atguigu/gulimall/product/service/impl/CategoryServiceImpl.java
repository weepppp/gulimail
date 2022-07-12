package com.atguigu.gulimall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.atguigu.common.utils.Query;
import com.atguigu.gulimall.product.service.CategoryBrandRelationService;
import com.atguigu.gulimall.product.vo.Catelog2Vo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import jdk.nashorn.internal.scripts.JS;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.common.utils.PageUtils;

import com.atguigu.gulimall.product.dao.CategoryDao;
import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    //    @Autowired
//    private CategoryDao categoryDao;
    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {

        /**
         * @功能 [编写树形结构组装方法]
         */

        // 1、查出所有分类
        List<CategoryEntity> entities = baseMapper.selectList(null);
        // 2、组装树形结构
        // 找到所有的一级分类
        List<CategoryEntity> level1Menus = entities.stream().filter(categoryEntity ->
                categoryEntity.getParentCid() == 0
        ).map((menu) -> {
            menu.setChildren(getChildrens(menu, entities));
            return menu;
        }).sorted((menu1, menu2) -> {
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());

        return level1Menus;
    }

    @Override
    public Long[] findCatelogPath(Long cateId) {
        List<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(cateId, paths);
        Collections.reverse(parentPath);
        return parentPath.toArray(new Long[parentPath.size()]);
    }

    @Transactional
    @Override
    public void updateCascade(CategoryEntity category) {
        /**
         * @功能 [级联更新所有关联的数据]
         */
        this.updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());
    }

    @Override
    public List<CategoryEntity> getLevel1Categorys() {
        Long l = System.currentTimeMillis();
        List<CategoryEntity> entities = this.baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
        System.out.println("消耗时间" + (System.currentTimeMillis() - l));
        return entities;
    }

    // redis分布式缓存存取数据：
    // 解决缓存击穿（锁）+缓存雪崩（设置过期时间）
    @Override
    public Map<String, List<Catelog2Vo>> getCatalogJson() {
        String catalogJson = redisTemplate.opsForValue().get("catalogJson");
        if (StringUtils.isEmpty(catalogJson)) {
            Map<String, List<Catelog2Vo>> catalogJsonFromDb = getCatalogJsonFromDb();
            return catalogJsonFromDb;
        }
        Map<String, List<Catelog2Vo>> stringListMap = JSON.parseObject(catalogJson, new TypeReference<Map<String, List<Catelog2Vo>>>() {
        });
        return stringListMap;
    }

    // 从数据库查询并封装分类数据
    public Map<String, List<Catelog2Vo>> getCatalogJsonFromDb() {
        /**
         * @功能 一级分类的id+二三级分类资源的查找展示
         */
//        synchronized (this) {  // 这里的this是本地锁，多个微服务就会有多个锁，也就是有多个线程进入项目，并不满足只有单线程的要求，所有我们需要分布式锁
        String s = UUID.randomUUID().toString();
        Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", s, 300, TimeUnit.SECONDS); // 升级成分布式锁+设置锁的过期时间
        Map<String, List<Catelog2Vo>> stringListMap;
        if (lock) {
            try {
                stringListMap = getStringListMap();
            } finally {
                //获取值对比+对比成功删除=原子操作  lua脚本解锁
                String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                Long lock1 = redisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class), Arrays.asList("lock"), s);
            }
            return stringListMap;
        } else {
            // 加锁失败，重试
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return getCatalogJsonFromDb(); // 自旋的方式
        }

    }

    private Map<String, List<Catelog2Vo>> getStringListMap() {
        String catalogJson = redisTemplate.opsForValue().get("catalogJson");
        if (!StringUtils.isEmpty(catalogJson)) {
            Map<String, List<Catelog2Vo>> stringListMap = JSON.parseObject(catalogJson, new TypeReference<Map<String, List<Catelog2Vo>>>() {
            });
            return stringListMap;
        }
        List<CategoryEntity> level1Categorys = getLevel1Categorys();
        System.out.println(level1Categorys.toString());
        Map<String, List<Catelog2Vo>> map = level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            List<CategoryEntity> entities = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", v.getCatId()));
            List<Catelog2Vo> catelog2VoList = null;
            if (entities != null) {
                catelog2VoList = entities.stream().map(item -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, item.getCatId().toString(), item.getName());
                    List<CategoryEntity> entities1 = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", item.getCatId()));
                    if (entities1 != null) {
                        List<Catelog2Vo.Catelog3Vo> catelog3VoList = entities1.stream().map(l3 -> {
                            Catelog2Vo.Catelog3Vo catelog3Vo = new Catelog2Vo.Catelog3Vo(item.getCatId().toString(), l3.getCatId().toString(), l3.getName());
                            return catelog3Vo;
                        }).collect(Collectors.toList());
                        catelog2Vo.setCatalog3List(catelog3VoList);
                    }
                    return catelog2Vo;
                }).collect(Collectors.toList());
            }
            return catelog2VoList;
        }));
        String s = JSON.toJSONString(map);
        redisTemplate.opsForValue().set("catalogJson", s, 1, TimeUnit.DAYS);
        return map;
    }

    private List<Long> findParentPath(Long cateId, List<Long> paths) {
        paths.add(cateId);
        CategoryEntity id = this.getById(cateId);
        if (id.getParentCid() != 0) {
            findParentPath(id.getParentCid(), paths);
        }
        return paths;
    }

    // 递归查找所有菜单的子菜单
    private List<CategoryEntity> getChildrens(CategoryEntity root, List<CategoryEntity> all) {
        List<CategoryEntity> children = all.stream().filter(categoryEntity -> {
            return categoryEntity.getParentCid() == root.getCatId();
        }).map(categoryEntity -> {
            categoryEntity.setChildren(getChildrens(categoryEntity, all));
            return categoryEntity;
        }).sorted((menu1, menu2) -> {
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());

        return children;
    }

}