package com.stitch.service.cms.feign;

import com.stitch.common.base.result.R;
import com.stitch.service.cms.feign.fallback.OSSFileServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 远程调用服务名为service-oss的微服务的接口,一定要加url
 */
@Service
@FeignClient(value = "service-oss",fallback = OSSFileServiceFallBack.class)//声明调用的服务
public interface OSSFileService {
    @GetMapping("/admin/oss/file/test")//指定具体方法
    R test();

    @DeleteMapping("/admin/oss/file/removeFile")
    R removeFile(@RequestBody String url);
}
