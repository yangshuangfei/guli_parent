package com.stitch.service.sms.service.impl;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;
import com.stitch.common.base.result.ResultCodeEnum;
import com.stitch.common.base.util.ExceptionUtils;
import com.stitch.service.base.exception.StitchException;
import com.stitch.service.sms.service.SmsService;
import com.stitch.service.sms.util.SmsProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class SmsServiceImpl implements SmsService {
    @Autowired
    private SmsProperties properties;

    @Override
    public void send(String mobile, String checkCode) {
        //创建配置对象
        DefaultProfile profile = DefaultProfile.getProfile(
                properties.getRegionId(), properties.getKeyid(), properties.getKeysecret());
        //创建client对象
        IAcsClient client = new DefaultAcsClient(profile);

        //创建参数对象
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", properties.getRegionId());
        request.putQueryParameter("PhoneNumbers", mobile);
        request.putQueryParameter("SignName", properties.getSignName());
        request.putQueryParameter("TemplateCode", properties.getTemplateCode());
        Map<String, String> param = new HashMap<>();
        param.put("code", checkCode);
        Gson gson = new Gson();
        request.putQueryParameter("TemplateParam", gson.toJson(param));
        String code = null;
        String message = null;
        try {
            CommonResponse response = client.getCommonResponse(request);
            String data = response.getData();
            //解析响应结果
            HashMap<String, String> hashMap = gson.fromJson(data, HashMap.class);
            code = hashMap.get("Code");
            message = hashMap.get("Message");
        } catch (ServerException e) {
            log.error(ExceptionUtils.getMessage(e));
        } catch (ClientException e) {
            log.error(ExceptionUtils.getMessage(e));
        }
        if (!"OK".equals(code)) {
            log.error("短信发送失败：" + "code:" + code + ",message:" + message);
            throw new StitchException(ResultCodeEnum.SMS_SEND_ERROR);
        }
    }
}
