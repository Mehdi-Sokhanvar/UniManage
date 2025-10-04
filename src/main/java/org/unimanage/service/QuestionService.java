package org.unimanage.service;

import org.unimanage.domain.question.Option;
import org.unimanage.domain.question.Question;

import java.util.List;

public interface QuestionService extends BaseService<Question,Long> {

    List<Question> getQuestionByCourseId(Long courseId);

    List<Question>   getQuestionByCourseId(Long courseId,String filter); //difficulty, category, type

}
