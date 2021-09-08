package com.example.vueadminjava2.commen.result;

import com.example.vueadminjava2.entity.SysUser;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @Description :作为/userList接口的data
 * @Author :kono淇杭哒、
 * @Create 2021-08-30 23:41
 */
@Data
@AllArgsConstructor
public class UsersResultData{
    private List<SysUser> users;
    private Integer totalCount;

    public static UsersResultData create(List<SysUser> users,Integer totalCount){
        return new UsersResultData(users,totalCount);
    }
}
