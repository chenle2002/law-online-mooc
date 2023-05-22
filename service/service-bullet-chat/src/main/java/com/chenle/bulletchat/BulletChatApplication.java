package com.chenle.bulletchat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author 陈乐
 * @Date 2023/5/22 16:14
 * @Version 1.0
 */
@SpringBootApplication
@ComponentScan({"com.chenle"}) //指定扫描位置
@EnableDiscoveryClient
@MapperScan("com.chenle.bulletchat.dao")
public class BulletChatApplication {
    public static void main(String[] args) {
        SpringApplication.run(BulletChatApplication.class, args);
    }
}

