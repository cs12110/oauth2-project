package com.pkgs.ctrl;

import com.alibaba.fastjson.JSON;
import com.pkgs.entity.Product;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * product controller
 *
 * @author huanghuapeng create at 2019/4/11 9:12
 * @version 1.0.0
 */
@Controller
@RequestMapping("/oauth/resource/product/")
public class ProductController {

    @RequestMapping("/list")
    @ResponseBody
    public String list(Integer times) {
        if (null == times) {
            times = 5;
        }
        List<Product> list = new ArrayList<>(times);
        for (int index = 0; index < times; index++) {
            list.add(build(index, "p" + index, "c" + index));
        }
        return JSON.toJSONString(list);
    }

    private Product build(int id, String name, String code) {
        Product product = new Product();
        product.setId(id);
        product.setCode(code);
        product.setName(name);
        return product;
    }
}
