package com.stitch.service.sms.service;


public interface SmsService {
    void send(String mobile, String checkCode);
}
