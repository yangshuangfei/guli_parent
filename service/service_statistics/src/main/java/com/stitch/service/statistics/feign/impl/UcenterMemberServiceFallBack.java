package com.stitch.service.statistics.feign.impl;

import com.stitch.common.base.result.R;
import com.stitch.service.statistics.feign.UcenterMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UcenterMemberServiceFallBack implements UcenterMemberService {
    @Override
    public R countRegisterNum(String day) {
        log.error("UcenterMemberService熔断");
        return R.ok().data("registerNum",0);
    }
}
