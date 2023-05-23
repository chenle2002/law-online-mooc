### 法途——法学在线教学管理平台（https://github.com/chenle2002/law-online-mooc)
> 说明: 这是本项目的后端部分,后台管理系统见(https://github.com/chenle2002/law-online-mooc-admin),
用户前端见(https://github.com/chenle2002/law-online-mooc-web)
#### 项目介绍
该项目为云南大学大创校级立项项目，采用微服务架构，包含课程信息、视频点播、实时弹幕、人员管理、用户评论等模块。可供用户进行课程信息查看、课程选择、课程章节视频播放、发送弹幕、简单评论等功能，并支持管理员对课程信息、视频信息进行管理

#### 主要技术

Vue,SpringBoot,Spring Cloud,Mysql,Redis,RabbitMQ,MybatisPlus,Shiro,Docker等

#### 项目亮点

* 项目采用微服务架构开发，各模块耦合程度低，涉及地微服务理念包括服务发现、配置中心、微服务网关、负载均衡等
* 使用RabbitMQ实现弹幕异步存储，并解决分布式环境下WebSocket的实时弹幕推送问题
* 使用Redisson分布式锁解决秒杀环境下的课程超卖问题以及订单重复创建的幂等性问题
* 使用Redis完成短信登陆、注册功能，并使用Redis对查询的数据做缓存
* 使用RabbitMQ将用户秒杀成功的消息异步保存到数据库中，并且使用RabbitMQ完成异步发送短信验证码的功能
* 使用Docker完成项目前后端的快速部署
