package com.itmk.web.role.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @Classname RoleAssignParm
 * @Description
 * @Date 2025/2/24 13:07
 * @Created by Oliver
 */
@Data
public class RoleAssignParm implements Serializable {
    //用户id
    private Long userId;
    //角色id
    private Long roleId;
}
