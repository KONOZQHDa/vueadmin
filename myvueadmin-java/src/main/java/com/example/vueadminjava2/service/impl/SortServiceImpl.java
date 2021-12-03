package com.example.vueadminjava2.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.vueadminjava2.dao.SortDao;
import com.example.vueadminjava2.entity.Sort;
import com.example.vueadminjava2.service.SortService;
import org.springframework.stereotype.Service;

/**
 * (Sort)表服务实现类
 *
 * @author KONO 曾淇杭哒！
 * @since 2021-11-06 18:20:27
 */
@Service("sortService")
public class SortServiceImpl extends ServiceImpl<SortDao, Sort> implements SortService{

}

