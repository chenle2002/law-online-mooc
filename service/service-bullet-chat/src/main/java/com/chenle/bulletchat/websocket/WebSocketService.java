package com.chenle.bulletchat.websocket;


import com.chenle.bulletchat.config.RabbitBeanConfig;
import com.chenle.bulletchat.entity.BulletChat;
import com.chenle.bulletchat.entity.BulletChatRequest;
import com.chenle.bulletchat.util.OnLineCountUtil;
import com.chenle.commonutils.JsonUtil;
import com.chenle.commonutils.JwtUtils;
import com.chenle.commonutils.ResultCode;
import com.chenle.servicebase.exceptionhandler.GuliException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;

/**
 * com.example.bulletchat.service
 *
 * @author chenle
 * 2023/3/12
 * 15:53
 * 说明:websocketService实际上为多例.
 * 由于加了@Component注解,容器启动时创建一个Websocket对象
 * 之后每次请求,都会由Spring new一个Websocket对象,但新new的Websocket对象不受容器管理,因此无法使用自动注入
 * 解决:借助ApplicationContextAware接口,利用容器时启动时创建的第一个WebSocket,设置静态的redisTemplate
 */
@Component
@ServerEndpoint("/edubc/bulletchat/{videoId}/{token}")
@Slf4j
public class WebSocketService implements ApplicationContextAware {
    private Session session;

    private String sessionId;
    private String videoId;
    private String userId;

    private static RabbitTemplate rabbitTemplate;

    @OnOpen
    public void openConnection(Session session, @PathParam("token") String token, @PathParam("videoId") String videoId) {
        JwtUtils userInfo = null;
        try {
            this.userId = token;
            //游客也可以观看视频,建立连接
        } catch (Exception e) {
            this.userId = "temp::" + session.getId();
        }
        this.videoId = videoId;
        this.sessionId = session.getId();
        this.session = session;
        WebSocketMap.put(this);
        OnLineCountUtil.add(videoId, userId);
        log.info("用户Id:{} 连接成功。当前视频在线人数：{}", userId, OnLineCountUtil.getVideoOnLineCount(videoId, userId));
        sendMessage("连接建立成功!");
    }

    @OnClose
    public void closeConnection() {
        WebSocketMap.remove(this);
        OnLineCountUtil.remove(videoId, userId);
        log.info("用户sessionId:{} 退出。当前在线人数：{}", sessionId, OnLineCountUtil.getVideoOnLineCount(videoId, userId));
    }

    /**
     * 收到前端发来的弹幕时
     *
     * @param message
     */
    @OnMessage
    public void onMessage(String message) {
        BulletChatRequest request = JsonUtil.parse(message, BulletChatRequest.class);
        if (!StringUtils.hasLength(request.getContent()) || request.getTimestamp() == null) {
            return;
        }
        if (userId.startsWith("temp")) {
            sendMessage("游客不能发送弹幕!");
            return;
        }

        BulletChat bulletChat = new BulletChat(null, videoId, userId, request.getContent(),
                request.getTimestamp(), 0, new Date());
        //发布弹幕到消息队列,让所有弹幕服务向已连接的websocket发送消息
        rabbitTemplate.convertAndSend(RabbitBeanConfig.BULLET_PUSH_EXCHANGE, "", bulletChat);
        //发送保存弹幕的消息到消息队列,异步保存
        rabbitTemplate.convertAndSend(RabbitBeanConfig.BULLET_SAVE_EXCHANGE, RabbitBeanConfig.BULLET_SAVE_BINDING, bulletChat);
        sendMessage("弹幕发送成功!");

    }

    @OnError
    public void onError(Throwable error) {
        error.printStackTrace();
    }

    public void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.error("WebSocket::videoId{},userId:{},content:{}||发送失败!原因:{}", videoId, userId, message, e.getMessage());
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        try {
            rabbitTemplate = applicationContext.getBean("rabbitTemplate", RabbitTemplate.class);
        } catch (BeansException e) {
            throw new GuliException(ResultCode.ERROR, "WebSocketService初始化失败!");
        }
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public String getVideoId() {
        return videoId;
    }
}
