package com.itmk.web.house_unit.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @Classname HouseUnitParm
 * @Description
 * @Date 2025/2/27 11:01
 * @Created by Oliver
 */
@Data
public class HouseUnitParm implements Serializable {
    private String buildName;
    private String unitName;
    private Long currentPage;
    private Long pageSize;
}
