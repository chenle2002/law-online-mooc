package com.chenle.eduservice.service;

import com.chenle.eduservice.entity.EduSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chenle.eduservice.entity.subject.OneSubject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author chenle
 * @since 2023-05-15
 */
public interface EduSubjectService extends IService<EduSubject> {

    void saveSubject(MultipartFile file, EduSubjectService subjectService);

    List<OneSubject> getAllOneTwoSubject();
}
