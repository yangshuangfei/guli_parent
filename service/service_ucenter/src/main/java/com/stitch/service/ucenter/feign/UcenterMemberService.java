package com.stitch.service.ucenter.feign;

import com.stitch.common.base.result.R;
import com.stitch.service.ucenter.feign.impl.UcenterMemberServiceFallBack;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-ucenter", fallback = UcenterMemberServiceFallBack.class)
public interface UcenterMemberService {
    @GetMapping("/admin/ucenter/member/count-register-num/{day}")
    R countRegisterNum(@PathVariable("day") String day);
}
