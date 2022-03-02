package com.stitch.service.trade.feign;

import com.stitch.service.base.dto.MemberDto;
import com.stitch.service.trade.feign.failback.UcenterMemberServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@FeignClient(value = "service-ucenter",fallback = UcenterMemberServiceFallBack.class)
public interface UcenterMemberService {
    @GetMapping("/api/ucenter/member/inner/get-member-dto/{memberId}")
    public MemberDto getMemberDtoByMemberId(
            @PathVariable(value = "memberId") String memberId);
}
