package com.chenle.eduorder.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenle.commonutils.ResultCode;
import com.chenle.commonutils.ordervo.CourseWebVoOrder;
import com.chenle.commonutils.ordervo.UcenterMemberOrder;
import com.chenle.eduorder.client.EduClient;
import com.chenle.eduorder.client.UcenterClient;
import com.chenle.eduorder.entity.Order;
import com.chenle.eduorder.mapper.OrderMapper;
import com.chenle.eduorder.service.OrderService;
import com.chenle.eduorder.utils.OrderNoUtil;
import com.chenle.servicebase.exceptionhandler.GuliException;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author chenle
 * @since 2023-5-18
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Autowired
    private EduClient eduClient;

    @Autowired
    private UcenterClient ucenterClient;
    @Autowired
    RedissonClient redissonClient;
    @Autowired
    RedisTemplate redisTemplate;
    //1 生成订单的方法并使用redisson分布式锁实现创建订单的幂等性
    @Override
    public String createOrders(String courseId, String memberId) {
        Object o = redisTemplate.opsForValue().get("createOrders:" + memberId + "coursre:" + courseId);
        //key存在则说明相同的请求正在继续处理，直接拒绝，不存在则进行处理
        if(o==null){
            //使用redisson对用户id和课程id进行上锁，保证在第一个订单生成时，该用户的所有请求都会排队进行等待，数据存到数据库后再解锁
            RLock orderlock=redissonClient.getLock("createOrders:"+memberId+"coursre:"+courseId);
            //进行时间限制，防止死锁
            orderlock.lock(10, TimeUnit.SECONDS);
            //进行判断，判断该用户的该课程是否存在已经创建的订单
            Order order1 = this.baseMapper.selectOne(new QueryWrapper<Order>().eq("course_id", courseId).eq("member_id", memberId));
            //不存在说明可以继续生成订单，存在则拒绝
            if(order1!=null){
                //通过远程调用根据用户id获取用户信息
                UcenterMemberOrder userInfoOrder = ucenterClient.getUserInfoOrder(memberId);

                //通过远程调用根据课程id获取课信息
                CourseWebVoOrder courseInfoOrder = eduClient.getCourseInfoOrder(courseId);

                //创建Order对象，向order对象里面设置需要数据
                Order order = new Order();
                order.setOrderNo(OrderNoUtil.getOrderNo());//订单号
                order.setCourseId(courseId); //课程id
                order.setCourseTitle(courseInfoOrder.getTitle());
                order.setCourseCover(courseInfoOrder.getCover());
                order.setTeacherName(courseInfoOrder.getTeacherName());
                order.setTotalFee(courseInfoOrder.getPrice());
                order.setMemberId(memberId);
                order.setMobile(userInfoOrder.getMobile());
                order.setNickname(userInfoOrder.getNickname());
                order.setStatus(0);  //订单状态（0：未支付 1：已支付）
                order.setPayType(1);  //支付类型 ，微信1
                baseMapper.insert(order);
                orderlock.unlock();
                //返回订单号
                return order.getOrderNo();
            }else {
                throw new GuliException(ResultCode.ERROR,"该用户已生成过该商品的订单！");
            }
        }else {
            throw new GuliException(ResultCode.ERROR,"该用户的相同请求正在处理！");
        }
    }
}
