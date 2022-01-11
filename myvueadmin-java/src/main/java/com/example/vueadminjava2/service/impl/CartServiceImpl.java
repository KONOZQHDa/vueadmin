package com.example.vueadminjava2.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.vueadminjava2.dao.CartDao;
import com.example.vueadminjava2.entity.Cart;
import com.example.vueadminjava2.service.CartService;
import org.springframework.stereotype.Service;

/**
 * (Cart)表服务实现类
 *
 * @author KONO 曾淇杭哒！
 * @since 2021-12-05 20:57:53
 */
@Service("cartService")
public class CartServiceImpl extends ServiceImpl<CartDao, Cart> implements CartService{

}

