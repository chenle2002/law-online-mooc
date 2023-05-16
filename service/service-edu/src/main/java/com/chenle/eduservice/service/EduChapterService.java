package com.chenle.eduservice.service;

import com.chenle.eduservice.entity.EduChapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chenle.eduservice.entity.chapter.ChapterVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author chenle
 * @since 2023-05-15
 */
public interface EduChapterService extends IService<EduChapter> {

    List<ChapterVo> getChapterVideoByCourseId(String courseId);

    boolean deleteChapter(String chapterId);

    void removeChapterByCourseId(String courseId);
}
