package com.stitch.service.edu.feign.failback;

import com.stitch.common.base.result.R;
import com.stitch.service.edu.feign.VodVideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class VodVideoServiceFallBack implements VodVideoService {
    @Override
    public R removeVideo(String videoId) {
        log.info("熔断保护");
        return R.error();
    }

    @Override
    public R removeVideoByIdList(List<String> videoList) {
        log.info("熔断保护");
        return R.error();
    }
}
