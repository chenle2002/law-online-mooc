package com.chenle.msmservice.consumer;


import com.chenle.commonutils.ordervo.MsmVo;
import com.chenle.msmservice.config.RabbitmqConfig;
import com.chenle.msmservice.service.MsmService;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.impl.AMQImpl;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.stereotype.Component;


@Component
public class ReceiveHandler {
    @Autowired
    MsmService msmService;
   //监听sms队列
    @RabbitListener(queues = {RabbitmqConfig.QUEUE_INFORM_SMS})
    public void receive_sms(MsmVo msmVo, Message message, Channel channel) throws Exception {
        System.out.println(msmVo);
        System.out.println(msmVo.getCode());
        msmService.send(msmVo.getCode(),msmVo.getPhone());
    }
}
