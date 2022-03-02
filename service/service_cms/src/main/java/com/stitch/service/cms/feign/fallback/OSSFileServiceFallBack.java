package com.stitch.service.cms.feign.fallback;

import com.stitch.common.base.result.R;
import com.stitch.service.cms.feign.OSSFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("oSSFileServiceFallBack")
@Slf4j
public class OSSFileServiceFallBack implements OSSFileService {
    @Override
    public R test() {
        return null;
    }

    @Override
    public R removeFile(String url) {
        log.info("熔断保护");
        return R.error();
    }
}
