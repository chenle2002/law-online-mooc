package com.chenle.eduservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chenle.eduservice.entity.EduTeacher;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author chenle
 * @since 2023-05-14
 */
public interface EduTeacherService extends IService<EduTeacher> {

    Map<String, Object> getTeacherFrontList(Page<EduTeacher> pageTeacher);
}
