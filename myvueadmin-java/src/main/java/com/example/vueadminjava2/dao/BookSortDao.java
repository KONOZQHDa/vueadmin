package com.example.vueadminjava2.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.vueadminjava2.entity.BookSort;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (BookSort)表数据库访问层
 *
 * @author KONO 曾淇杭哒！
 * @since 2021-11-06 18:25:35
 */
public interface BookSortDao extends BaseMapper<BookSort>{

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<BookSort> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<BookSort> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<BookSort> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<BookSort> entities);

}

