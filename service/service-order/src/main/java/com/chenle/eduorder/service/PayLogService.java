package com.chenle.eduorder.service;



import com.baomidou.mybatisplus.extension.service.IService;
import com.chenle.eduorder.entity.PayLog;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author chenle
 * @since 2023-5-18
 */
public interface PayLogService extends IService<PayLog> {
    Map createNatvie(String orderNo);

    Map<String, String> queryPayStatus(String orderNo);

    void updateOrdersStatus(Map<String, String> map);
}
