package com.example.vueadminjava2.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.vueadminjava2.entity.Diction;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @Description :
 * @Author :kono淇杭哒、
 * @Create 2021-12-05 15:20
 */
@SpringBootTest
public class DictionDaoTest{
    @Resource
    private DictionDao dictionDao;

    @Test
    public void queryDictionTest() {
        QueryWrapper<Diction> dictionQueryWrapper = new QueryWrapper<>();
        dictionQueryWrapper.eq("id", "1460883270307753986");
        Diction diction = dictionDao.selectOne(dictionQueryWrapper);
        System.out.println(diction);
    }
}
