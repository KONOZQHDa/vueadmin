package com.example.vueadminjava2.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.vueadminjava2.dao.FavoritesDao;
import com.example.vueadminjava2.entity.Favorites;
import com.example.vueadminjava2.service.CollectionService;
import org.springframework.stereotype.Service;

/**
 * (Collection)表服务实现类
 *
 * @author KONO 曾淇杭哒！
 * @since 2021-12-05 20:58:14
 */
@Service("collectionService")
public class CollectionServiceImpl extends ServiceImpl<FavoritesDao, Favorites> implements CollectionService{

}

