package com.chenle.eduservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chenle.eduservice.entity.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chenle.eduservice.entity.frontvo.CourseFrontVo;
import com.chenle.eduservice.entity.frontvo.CourseWebVo;
import com.chenle.eduservice.entity.vo.CourseInfoVo;
import com.chenle.eduservice.entity.vo.CoursePublishVo;

import java.util.Map;

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

    Map<String, Object> getCourseFrontList(Page<EduCourse> pageCourse, CourseFrontVo courseFrontVo);

    CourseWebVo getBaseCourseInfo(String courseId);

//    void removeCourse(String courseId);
}
