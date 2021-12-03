package com.example.vueadminjava2.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.vueadminjava2.commen.result.Result;
import com.example.vueadminjava2.dao.DictypeDao;
import com.example.vueadminjava2.entity.Dictype;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * (Dictype)表控制层
 *
 * @author KONO 曾淇杭哒！
 * @since 2021-11-10 17:05:50
 */
@RestController
@RequestMapping("dictype")
public class DictypeController{
    @Resource
    private DictypeDao dictypeDao;

    @GetMapping("listDicType")
    @SaCheckPermission("sys:dic:list")
    public Result listDicType() {
        List<Dictype> dictypes = dictypeDao.selectList(null);
        return Result.success(dictypes);
    }


    @PostMapping("addDicType")
    @SaCheckPermission("sys:dic:save")
    public Result addDicType(@RequestBody Dictype dictype) {
        try {
            dictypeDao.insert(dictype);
            return Result.success(200, "添加字典类型成功！", null);
        } catch (org.springframework.dao.DuplicateKeyException e) {
            return Result.fail(400, "字典名已存在！", null);
        }

    }

    @PutMapping("updateDicType")
    @SaCheckPermission("sys:dic:update")
    public Result updateDicType(@RequestBody Dictype dictype) {
        dictypeDao.updateById(dictype);
        return Result.success(200, "更新字典类型成功！", null);
    }

    @DeleteMapping("deleteDicType")
    @SaCheckPermission("sys:dic:delete")
    public Result deleteDicTypes(String id) {
        dictypeDao.deleteById(id);
        return Result.success(200, "删除字典类型成功！", null);
    }

    @GetMapping("search")
    @SaCheckPermission("sys:dic:list")
    public Result search(String word) {
        QueryWrapper<Dictype> dictypeQueryWrapper = new QueryWrapper<>();
        dictypeQueryWrapper.like(word != null, "dictype_key", word);
        List<Dictype> dictypes = dictypeDao.selectList(dictypeQueryWrapper);
        return Result.success(dictypes);
    }
}

