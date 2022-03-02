package com.stitch.service.sms.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "aliyun.sms")
public class SmsProperties {
    private String  regionId;
    private String keyid;
    private String keysecret;
    private String templateCode;
    private String signName;
}
