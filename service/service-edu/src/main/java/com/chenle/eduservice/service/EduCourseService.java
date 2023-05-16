package com.chenle.eduservice.service;

import com.chenle.eduservice.entity.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chenle.eduservice.entity.vo.CourseInfoVo;
import com.chenle.eduservice.entity.vo.CoursePublishVo;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author chenle
 * @since 2023-05-15
 */
public interface EduCourseService extends IService<EduCourse> {

    String saveCourseInfo(CourseInfoVo courseInfoVo);

    CourseInfoVo getCourseInfo(String courseId);

    void updateCourseInfo(CourseInfoVo courseInfoVo);

    CoursePublishVo publishCourseInfo(String id);

    void removeCourse(String courseId);

//    void removeCourse(String courseId);
}
