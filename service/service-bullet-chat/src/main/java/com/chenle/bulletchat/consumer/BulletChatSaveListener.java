package com.chenle.bulletchat.consumer;


import com.chenle.bulletchat.config.RabbitBeanConfig;
import com.chenle.bulletchat.entity.BulletChat;
import com.chenle.bulletchat.service.BulletChatService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * com.example.bulletchat.consumer
 *
 * @author chenle
 * 2023/3/13
 * 17:48
 */
@RabbitListener(queues = RabbitBeanConfig.BULLET_SAVE_QUEUE)
@Component
@Slf4j
public class BulletChatSaveListener {
    @Autowired
    BulletChatService bulletChatService;

    @RabbitHandler(isDefault = true)
    public void saveBulletChat(BulletChat bulletChat, Channel channel, Message message) {
        log.info("收到弹幕新增消息,弹幕内容:{}", bulletChat.getContent());
        bulletChatService.saveBullet(bulletChat);

        //手动应答
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
