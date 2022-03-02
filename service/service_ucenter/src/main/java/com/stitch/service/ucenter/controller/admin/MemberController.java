package com.stitch.service.ucenter.controller.admin;

import com.stitch.common.base.result.R;
import com.stitch.service.ucenter.service.IMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "会员管理")
@RestController
@RequestMapping("/admin/ucenter/member")
@Slf4j
public class MemberController {
    @Autowired
    private IMemberService memberService;

    @ApiOperation("根据统计日期统计注册人数")
    @GetMapping("count-register-num/{day}")
    public R countRegisterNum(
            @ApiParam(value = "统计日期", required = true)
            @PathVariable String day) {
        Integer num = memberService.selectRegisterNumByDay(day);
        return R.ok().data("registerNum", num);
    }
}
