package com.chenle.bulletchat.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.chenle.bulletchat.entity.BulletChat;

import java.util.List;

/**
 * <p>
 * 弹幕 服务类
 * </p>
 *
 * @author chenle
 * @since 2023-05-22
 */
public interface BulletChatService extends IService<BulletChat> {
    /**
     * 新增弹幕
     */
    void saveBullet(BulletChat bulletChat);

    /**
     * 推送弹幕
     */
    void pushBullet(BulletChat bulletChat);

    /**
     * 获取弹幕
     *
     * @return
     */
    List<BulletChat> getBulletsByVideoId(String videoId);
}
