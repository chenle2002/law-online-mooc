package com.chenle.eduorder.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.chenle.eduorder.entity.Order;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author chenle
 * @since 2023-5-18
 */
public interface OrderService extends IService<Order> {
    String createOrders(String courseId, String memberIdByJwtToken);

}
