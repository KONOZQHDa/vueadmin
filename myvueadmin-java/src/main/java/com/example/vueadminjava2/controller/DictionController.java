package com.example.vueadminjava2.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.example.vueadminjava2.commen.result.Result;
import com.example.vueadminjava2.dao.DictionDao;
import com.example.vueadminjava2.dao.DictypeDao;
import com.example.vueadminjava2.entity.Diction;
import com.example.vueadminjava2.entity.Dictype;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * (Diction)表控制层
 *
 * @author KONO 曾淇杭哒！
 * @since 2021-11-10 17:05:00
 */
@RestController
@RequestMapping("diction")
public class DictionController extends ApiController{
    @Resource
    private DictypeDao dictypeDao;

    @Resource
    private DictionDao dictionDao;

    @GetMapping("getDic")
    public Result getDic(String dictypeId) {
        QueryWrapper<Dictype> dictypeQueryWrapper = new QueryWrapper<>();
        dictypeQueryWrapper.eq(dictypeId != null, "id", dictypeId);
        List<Dictype> dictypes = dictypeDao.selectList(dictypeQueryWrapper);
        HashMap<String, List<Diction>> dictionMap = new HashMap<>();
        for (Dictype dictype : dictypes) {
            QueryWrapper<Diction> dictionQueryWrapper = new QueryWrapper<>();
            dictionQueryWrapper.eq("dictype_id", dictype.getId());
            List<Diction> dictions = dictionDao.selectList(dictionQueryWrapper);
            dictionMap.put(dictype.getDictypeKey(), dictions);
        }
        return Result.success(dictionMap);
    }

    @PostMapping("addDic")
    public Result addDic(@RequestBody Diction diction) {
        try {
            dictionDao.insert(diction);
            return Result.success(200, "添加字典数据成功！", null);
        } catch (org.springframework.dao.DuplicateKeyException e) {
            return Result.fail(400, "字典数据key已存在！", null);
        }
    }

    @PutMapping("updateDic")
    public Result updateDic(@RequestBody Diction diction) {
        dictionDao.updateById(diction);
        return Result.success(200, "更新字典数据成功！", null);
    }

    @DeleteMapping("deleteDic")
    public Result deleteDic(String id) {
        dictionDao.deleteById(id);
        return Result.success(200, "删除字典数据成功！", null);
    }

    @GetMapping("search")
    public Result search(String word) {
        QueryWrapper<Diction> dictionQueryWrapper = new QueryWrapper<>();
        dictionQueryWrapper.like(word != null, "dic_key", word);
        List<Diction> dictions = dictionDao.selectList(dictionQueryWrapper);
        return Result.success(dictions);
    }
}

