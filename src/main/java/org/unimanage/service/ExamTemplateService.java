package org.unimanage.service;

import org.unimanage.domain.exam.ExamTemplate;

import java.util.List;

public interface ExamTemplateService extends BaseService<ExamTemplate,Long> {

    List<ExamTemplate> getExamsByOfferedCourseId(Long offeredCourseId);

    void addQuestionToExam(Long questionId,Long examTemplateId);

}
