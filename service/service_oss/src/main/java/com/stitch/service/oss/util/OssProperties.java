package com.stitch.service.oss.util;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "aliyun.oss")
public class OssProperties {
//    @Value("${endpoint}")
    private String endpoint;
    private String keyid;
    private String keysecret;
    private String bucketname;
}
