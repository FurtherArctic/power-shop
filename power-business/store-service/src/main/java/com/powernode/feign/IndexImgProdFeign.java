package com.powernode.feign;

import com.powernode.domain.Prod;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author wangjunchen
 */
@FeignClient(value = "product-service")
public interface IndexImgProdFeign {

    /**
     * 根据轮播图跨服务获取商品信息
     *
     * @param id 商品id
     * @return 商品信息
     */
    @GetMapping("prod/prod/getProdById")
    Prod getProdById(@RequestParam Long id);
}
