package com.OrderMate.controller.admin;

import com.OrderMate.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * ClassName: ShopController
 * Package: com.OrderMate.controller.admin
 * Description:
 *
 * @Author Gush
 * @Create 2024-02-26 10:29
 */
@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Slf4j
@Api(tags = "店铺相关接口")
public class ShopController {
    @Autowired
    private RedisTemplate redisTemplate;



    @PutMapping("/{status}")
    @ApiOperation("设置店铺状态")
    public Result setStatus(@PathVariable Integer status) {
        redisTemplate.opsForValue().set("SHOP_STATUS",status);
        return Result.success();
    }

    @GetMapping("/status")
    @ApiOperation("查询店铺状态")
    public Result<Integer> getStatus() {
        Integer shop_status = (Integer) redisTemplate.opsForValue().get("SHOP_STATUS");
        log.info("获取到店铺的状态为:{}",shop_status==1?"营业中":"打烊中");
        return Result.success(shop_status);
    }
}
