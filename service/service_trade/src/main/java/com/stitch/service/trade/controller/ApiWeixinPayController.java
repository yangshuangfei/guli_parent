package com.stitch.service.trade.controller;

import com.github.wxpay.sdk.WXPayUtil;
import com.stitch.common.base.result.R;
import com.stitch.service.trade.entity.Order;
import com.stitch.service.trade.service.IOrderService;
import com.stitch.service.trade.service.WeixinPayService;
import com.stitch.service.trade.util.StreamUtils;
import com.stitch.service.trade.util.WeixinPayProperties;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Api(description = "网站微信支付")
@Slf4j
@RequestMapping("api/trade/weixin-pay")
@RestController
public class ApiWeixinPayController {
    @Autowired
    private WeixinPayService weixinPayService;

    @Autowired
    private WeixinPayProperties weixinPayProperties;

    @Autowired
    private IOrderService orderService;

    @GetMapping("create-native/{orderNo}")
    public R createNative(@PathVariable String orderNo, HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();
        Map<String, Object> map = weixinPayService.createNative(orderNo, remoteAddr);
        return R.ok().data(map);
    }

    @PostMapping("callback/notify")
    public String wxNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("callback被调用");
        ServletInputStream inputStream = request.getInputStream();
        String notifyXml = StreamUtils.inputStream2String(inputStream, "utf-8");
        log.info(notifyXml);

        //验签：验证签名是否正确
        if (WXPayUtil.isSignatureValid(notifyXml, weixinPayProperties.getPartnerKey())) {
            //解析返回结果
            Map<String, String> xmlToMap = WXPayUtil.xmlToMap(notifyXml);
            if ("SUCCESS".equals(xmlToMap.get("result_code"))) {
                //金额校验
                String totalFee = xmlToMap.get("total_fee");
                String tradeNo = xmlToMap.get("out_trade_no");
                Order order = orderService.getOrderByOrderNo(tradeNo);
                if (order != null && order.getTotalFee().intValue() == Integer.parseInt(totalFee)) {

                    //接口调用幂等性：无论接口被调用多少次，最后所影响的结果都是一致的
                    if (order.getStatus() == 0) {
                        //更新订单状态
                        orderService.updateOrderStatus(xmlToMap);
                    }
                    //创建响应对象
                    Map<String, String> returnMap = new HashMap<>();
                    returnMap.put("return_code", "SUCCESS");
                    returnMap.put("return_msg", "OK");
                    String resultXml = WXPayUtil.mapToXml(returnMap);
                    response.setContentType("text/xml");
                    log.info("支付成功,通知已处理");
                    return resultXml;
                }
            }
        }
       //创建失败响应对象
        Map<String, String> returnMap = new HashMap<>();
        returnMap.put("return_code", "FAIL");
        returnMap.put("return_msg", "");
        String resultXml = WXPayUtil.mapToXml(returnMap);
        response.setContentType("text/xml");
        log.info("校验失败");
        return resultXml;

    }
}
