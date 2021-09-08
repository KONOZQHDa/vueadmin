package com.example.vueadminjava2.commen.result;

import com.example.vueadminjava2.entity.SysRole;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @Description :
 * @Author :kono淇杭哒、
 * @Create 2021-09-01 21:27
 */
@Data
@AllArgsConstructor
public class RolesResultData{
    private List<SysRole> roles;
    private Integer totalCount;

    public static RolesResultData create(List<SysRole> roles,Integer totalCount){
        return new RolesResultData(roles,totalCount);
    }
}
