package com.stitch.service.trade.controller;


import com.stitch.common.base.result.R;
import com.stitch.common.base.result.ResultCodeEnum;
import com.stitch.common.base.util.JwtInfo;
import com.stitch.common.base.util.JwtUtils;
import com.stitch.service.trade.entity.Order;
import com.stitch.service.trade.service.IOrderService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author ysf
 * @since 2021-03-18
 */
@Api(description = "网站订单管理")
@RestController
@RequestMapping("/api/trade/order")
@Slf4j
public class ApiOrderController {
    @Autowired
    private IOrderService orderService;

    @ApiOperation("新增订单")
    @PostMapping("auth/save/{courseId}")
    public R save(@PathVariable String courseId, HttpServletRequest request) {

        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        String orderId = orderService.saveOrder(courseId, jwtInfo.getId());
        return R.ok().data("orderId", orderId);
    }

    @ApiOperation("获取订单")
    @GetMapping("auth/get/{orderId}")
    public R get(@PathVariable String orderId, HttpServletRequest request) {
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        Order order = orderService.getByOrderId(orderId, jwtInfo.getId());
        return R.ok().data("item", order);
    }

    @ApiOperation("判断课程是否购买")
    @GetMapping("auth/is-buy/{courseId}")
    public R isBuyByCourseId(@PathVariable String courseId, HttpServletRequest request) {
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        Boolean isBuy = orderService.isBuyByCourseId(courseId, jwtInfo.getId());
        return R.ok().data("isBuy", isBuy);
    }

    @ApiOperation("根据用户id查询所以订单")
    @GetMapping("auth/list")
    public R list(HttpServletRequest request) {
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        List<Order> orderList = orderService.selectByMemberId(jwtInfo.getId());
        return R.ok().data("items", orderList);
    }

    @ApiOperation("删除订单")
    @DeleteMapping("auth/remove/{orderId}")
    public R remove(@PathVariable(value = "orderId") String orderId, HttpServletRequest request) {
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        boolean result = orderService.removeById(orderId, jwtInfo.getId());
        if (result) {
            return R.ok().message("删除成功");
        } else {
            return R.ok().message("数据不存在");

        }
    }

    @GetMapping("query-pay-status/{orderNo}")
    public R queryPayStatus(String orderNo) {
        boolean result = orderService.queryPayStatus(orderNo);
        if (result) {
            return R.ok().message("支付成功");
        }
        return R.setResult(ResultCodeEnum.PAY_RUN);
    }
}

