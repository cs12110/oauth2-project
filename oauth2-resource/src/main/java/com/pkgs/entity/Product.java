package com.pkgs.entity;

import com.alibaba.fastjson.JSON;
import lombok.Data;

/**
 * @author huanghuapeng create at 2019/4/11 9:09
 * @version 1.0.0
 */
@Data
public class Product {
    private Integer id;
    private String code;
    private String name;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
