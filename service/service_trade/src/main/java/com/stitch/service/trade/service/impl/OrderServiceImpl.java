package com.stitch.service.trade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.stitch.common.base.result.ResultCodeEnum;
import com.stitch.service.base.dto.CourseDto;
import com.stitch.service.base.dto.MemberDto;
import com.stitch.service.base.exception.StitchException;
import com.stitch.service.trade.entity.Order;
import com.stitch.service.trade.entity.PayLog;
import com.stitch.service.trade.feign.EduCourseService;
import com.stitch.service.trade.feign.UcenterMemberService;
import com.stitch.service.trade.mapper.OrderMapper;
import com.stitch.service.trade.mapper.PayLogMapper;
import com.stitch.service.trade.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stitch.service.trade.service.IPayLogService;
import com.stitch.service.trade.util.OrderNoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author ysf
 * @since 2021-03-18
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {
    @Autowired
    private EduCourseService eduCourseService;

    @Autowired
    private UcenterMemberService ucenterMemberService;

    @Autowired
    private PayLogMapper payLogMapper;

    @Override
    public String saveOrder(String courseId, String memberId) {
        //查询当前用户是否已有当前课程的订单
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        queryWrapper.eq("member_id", memberId);
        Order orderExist = baseMapper.selectOne(queryWrapper);
        if (orderExist != null) {
            return orderExist.getId(); //订单存在，直接返回订单id
        }
        //创建订单
        //查询课程信息
        System.out.println(courseId);
        CourseDto courseDto = eduCourseService.getCourseDtoByCourseId(courseId);
        if (courseDto == null) {
            throw new StitchException(ResultCodeEnum.PARAM_ERROR);
        }
        //查询用户信息
        MemberDto memberDto = ucenterMemberService.getMemberDtoByMemberId(memberId);
        if (memberDto == null) {
            throw new StitchException(ResultCodeEnum.PARAM_ERROR);
        }
        Order order = new Order();
        order.setOrderNo(OrderNoUtils.getOrderNo());//订单号
        order.setCourseId(courseDto.getId());
        order.setCourseTitle(courseDto.getTitle());
        order.setCourseCover(courseDto.getCover());
        order.setTeacherName(courseDto.getTeacherName());
        order.setTotalFee(courseDto.getPrice().multiply(new
                BigDecimal(100)));

        order.setMemberId(memberDto.getId());
        order.setMobile(memberDto.getMobile());
        order.setNickname(memberDto.getNickname());

        order.setStatus(0);//订单状态
        order.setPayType(1);//微信支付

        int insert = baseMapper.insert(order);

        return order.getId();
    }

    @Override
    public Order getByOrderId(String orderId, String memberId) {
        //查询当前用户是否已有当前课程的订单
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", orderId);
        queryWrapper.eq("member_id", memberId);
        Order order = baseMapper.selectOne(queryWrapper);
        if (order != null) {
            return order; //订单存在，直接返回
        }
        return null;
    }

    @Override
    public Boolean isBuyByCourseId(String courseId, String memberId) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("course_id", courseId)
                .eq("member_id", memberId)
                .eq("status", 1);

        Integer count = baseMapper.selectCount(queryWrapper);

        return count.intValue() > 0;
    }

    @Override
    public List<Order> selectByMemberId(String memberId) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("gmt_create")
                .eq("member_id", memberId);
        List<Order> orderList = baseMapper.selectList(queryWrapper);
        return orderList;
    }

    @Override
    public boolean removeById(String orderId, String memberId) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", orderId)
                .eq("member_id", memberId);
        return this.remove(queryWrapper);
    }

    @Override
    public Order getOrderByOrderNo(String orderNo) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);
        Order order = baseMapper.selectOne(queryWrapper);
        return order;
    }

    @Override
    public void updateOrderStatus(Map<String, String> xmlToMap) {
        //更新订单状态
        String outTradeNo = xmlToMap.get("out_trade_no");
        Order order = this.getOrderByOrderNo(outTradeNo);
        order.setStatus(1);
        baseMapper.updateById(order);

        //记录支付日志
        PayLog payLog = new PayLog();
        payLog.setOrderNo(outTradeNo);
        payLog.setPayTime(new Date());
        payLog.setPayType(1);
        payLog.setTotalFee(Long.parseLong(xmlToMap.get("total_fee")));
        payLog.setTradeState(xmlToMap.get("result_code"));
        payLog.setTransactionId(xmlToMap.get("transaction_id"));
        payLog.setAttr(new Gson().toJson(xmlToMap));
        payLogMapper.insert(payLog);

    }

    @Override
    public boolean queryPayStatus(String orderNo) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);
        Order order = baseMapper.selectOne(queryWrapper);

        return order.getStatus() == 1;
    }
}
