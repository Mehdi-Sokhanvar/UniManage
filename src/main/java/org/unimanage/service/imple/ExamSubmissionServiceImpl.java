package org.unimanage.service.imple;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unimanage.domain.exam.ExamInstance;
import org.unimanage.domain.exam.ExamQuestion;
import org.unimanage.service.ExamSubmissionService;

public class ExamSubmissionServiceImpl extends BaseServiceImpl<ExamInstance,Long> implements ExamSubmissionService {
    protected ExamSubmissionServiceImpl(JpaRepository<ExamInstance, Long> repository) {
        super(repository);
    }
}
