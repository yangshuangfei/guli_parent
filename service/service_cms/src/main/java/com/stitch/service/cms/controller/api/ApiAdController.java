package com.stitch.service.cms.controller.api;

import com.stitch.common.base.result.R;
import com.stitch.service.cms.entity.Ad;
import com.stitch.service.cms.service.IAdService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * redisTemplate.opsForValue(); //操作字符串
 * redisTemplate.opsForHash(); //操作hash
 * redisTemplate.opsForList(); //操作list
 * redisTemplate.opsForSet(); //操作set
 * redisTemplate.opsForZSet(); //操作有序set
 */
@CrossOrigin
@Api
@RestController
@RequestMapping("/api/cms/ad")
@Slf4j
public class ApiAdController {

    @Autowired
    private IAdService adService;

    @Autowired
    private RedisTemplate redisTemplate;


    @ApiOperation("根据推荐位id显示广告推荐")
    @GetMapping("list/{adTypeId}")
    public R listByAdTypeId(
            @ApiParam(value = "推荐位id", required = true)
            @PathVariable String adTypeId) {
        List<Ad> list = adService.selectByAdTypeID(adTypeId);
        return R.ok().data("items", list);
    }

    @ApiOperation("测试redis存数据")
    @PostMapping("save-test")
    public R saveAd(@RequestBody Ad ad){
        redisTemplate.opsForValue().set("ad",ad);
        return R.ok().message("存储成功");
    }

    @ApiOperation("测试获取redis数据")
    @GetMapping("get-test")
    public R getAd(String key){
        Ad ad = (Ad) redisTemplate.opsForValue().get(key);
        return R.ok().data("ad",ad).message("获取成功");
    }

    @ApiOperation("测试获取redis数据")
    @DeleteMapping("delete-test")
    public R deleteAd(String key){
        Boolean delete = redisTemplate.delete(key);//判断是否删除成功
        Boolean aBoolean = redisTemplate.hasKey(key);//判断这个key是否存在
        log.info(String.valueOf(delete));
        log.info(String.valueOf(aBoolean));
        return R.ok().message("成功");
    }
}
