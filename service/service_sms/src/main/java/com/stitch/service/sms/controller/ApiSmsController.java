package com.stitch.service.sms.controller;

import com.stitch.common.base.result.R;
import com.stitch.common.base.result.ResultCodeEnum;
import com.stitch.common.base.util.FormUtils;
import com.stitch.common.base.util.RandomUtils;
import com.stitch.service.base.exception.StitchException;
import com.stitch.service.sms.service.SmsService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RequestMapping("api/sms/")
@RestController
@Slf4j
@CrossOrigin
@Api(description = "短信管理")
@RefreshScope //启动动态刷新配置
public class ApiSmsController {
    @Autowired
    private SmsService smsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("send/{mobile}")
    public R getCode(@PathVariable String mobile) {
        //校验手机号是否合法
        if (StringUtils.isEmpty(mobile) || !FormUtils.isMobile(mobile)) {
            log.error("手机号码格式不正确");
            throw new StitchException(ResultCodeEnum.LOGIN_MOBILE_ERROR);
        }
        //生成验证码
        String checkCode = RandomUtils.getFourBitRandom();
        //发送验证码
//        smsService.send(mobile, checkCode);

        //存储验证码到redis
        redisTemplate.opsForValue().set(mobile, checkCode, 5, TimeUnit.MINUTES);//过期时间五分钟

        return R.ok().message("短信发送成功");

    }
}
