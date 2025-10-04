package org.unimanage.service.imple;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unimanage.domain.question.Question;
import org.unimanage.service.QuestionService;

import java.util.List;

public class QuestionServiceImpl extends  BaseServiceImpl<Question,Long> implements QuestionService  {
    public QuestionServiceImpl(JpaRepository<Question, Long> repository) {
        super(repository);
    }

    @Override
    public List<Question> getQuestionByCourseId(Long courseId) {
        return List.of();
    }

    @Override
    public List<Question> getQuestionByCourseId(Long courseId, String filter) {
        return List.of();
    }


}
