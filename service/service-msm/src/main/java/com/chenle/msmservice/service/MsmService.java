package com.chenle.msmservice.service;

import java.util.Map;

public interface MsmService {
    boolean send(String code, String phone) throws Exception;
    //发送短信的方法

}
