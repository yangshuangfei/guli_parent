package com.stitch.service.edu.feign;

import com.stitch.common.base.result.R;
import com.stitch.service.edu.feign.failback.VodVideoServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@FeignClient(value = "service-vod", fallback = VodVideoServiceFallBack.class)
public interface VodVideoService {
    @DeleteMapping("/admin/vod/video/remove/{videoId}")
    R removeVideo(@PathVariable("videoId") String videoId);

    @DeleteMapping("/admin/vod/video/removeByIdList")
    R removeVideoByIdList(@RequestBody List<String> videoList);
}
