package com.example.vueadminjava2.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.vueadminjava2.commen.result.Result;
import com.example.vueadminjava2.dao.SortDao;
import com.example.vueadminjava2.entity.Sort;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * (Sort)表控制层
 *
 * @author KONO 曾淇杭哒！
 * @since 2021-11-06 18:20:24
 */
@RestController
@RequestMapping("sort")
public class SortController{
    /**
     * 服务对象
     */
    @Resource
    private SortDao sortDao;

    @PostMapping("addSort")
    public Result addSort(@RequestBody Sort sort) {
        sortDao.insert(sort);
        return Result.success(200, "新增书籍类型成功！", null);
    }

    @DeleteMapping("deleteSort")
    public Result deleteSort(@RequestParam String id) {
        sortDao.deleteById(id);
        return Result.success(200, "删除成功！", null);
    }

    @PutMapping("updateSort")
    public Result updateSort(@RequestBody Sort sort) {
        sortDao.updateById(sort);
        return Result.success(200, "更新书籍类型成功！", null);
    }

    @GetMapping("listSort")
    public Result listSort(@RequestParam("usful") Integer usful, String word) {
        QueryWrapper<Sort> sortQueryWrapper = new QueryWrapper<>();
        //是否只查询可用状态的书籍类型
        if (usful == 1) {
            sortQueryWrapper.eq("usful", 1);
        }

        if (!"".equals(word) && word != null) {
            sortQueryWrapper.like("name", word);
        }
        List<Sort> sorts = sortDao.selectList(sortQueryWrapper);
        return Result.success(sorts);
    }


}

