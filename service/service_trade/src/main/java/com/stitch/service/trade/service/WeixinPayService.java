package com.stitch.service.trade.service;

import org.springframework.stereotype.Service;

import java.util.Map;

public interface WeixinPayService {
    Map<String, Object> createNative(String orderNo, String remoteAddr);
}
