package org.unimanage.service;

import org.unimanage.domain.course.Course;
import org.unimanage.domain.course.OfferedCourse;
import org.unimanage.util.exception.EntityNotFoundException;

import java.security.Principal;
import java.util.List;

public interface CourseOfferingService extends BaseService<OfferedCourse, Long> {

    boolean chooseCourseOffering(Principal principal, OfferedCourse offeredCourse);

    List<OfferedCourse> getOfferedCourse(Principal principal);

    List<OfferedCourse> getOfferedCourseByTermId(Long termId);


}
