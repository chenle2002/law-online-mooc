package com.chenle.commonutils.ordervo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author 陈乐
 * @Date 2023/5/23 16:13
 * @Version 1.0
 */
@Data
public class MsmVo implements Serializable {
    private String phone;
    private String code;
}
