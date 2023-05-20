package com.chenle.eduservice.client;

import com.chenle.commonutils.R;
import org.springframework.stereotype.Component;

/**
 * @Author 陈乐
 * @Date 2023/5/20 15:05
 * @Version 1.0
 */
@Component
public class OrdersClientFeignClient implements OrdersClient{
    @Override
    public boolean isBuyCourse(String courseId, String memberId) {
        return false;
    }
}
