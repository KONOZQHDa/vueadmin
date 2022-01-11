package com.example.vueadminjava2.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.vueadminjava2.entity.Dictype;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @Description :
 * @Author :kono淇杭哒、
 * @Create 2021-12-05 15:25
 */
@SpringBootTest
public class DictypeTest{
    @Resource
    private DictypeDao dictypeDao;

    @Test
    public void queryDictypeTest() {
        QueryWrapper<Dictype> dictionQueryWrapper = new QueryWrapper<>();
        dictionQueryWrapper.eq("id", "1460883175562620930");
        Dictype dictype = dictypeDao.selectOne(dictionQueryWrapper);
        System.out.println(dictype);
    }
}
