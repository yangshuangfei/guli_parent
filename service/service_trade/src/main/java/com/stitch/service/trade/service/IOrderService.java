package com.stitch.service.trade.service;

import com.stitch.service.trade.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author ysf
 * @since 2021-03-18
 */
public interface IOrderService extends IService<Order> {

    String saveOrder(String courseId, String id);

    Order getByOrderId(String orderId, String id);

    Boolean isBuyByCourseId(String courseId, String id);

    List<Order> selectByMemberId(String id);

    boolean removeById(String orderId, String id);

    Order getOrderByOrderNo(String orderNo);

    void updateOrderStatus(Map<String, String> xmlToMap);

    boolean queryPayStatus(String orderNo);
}
