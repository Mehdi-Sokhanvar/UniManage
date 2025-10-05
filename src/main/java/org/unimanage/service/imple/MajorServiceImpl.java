package org.unimanage.service.imple;


import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.unimanage.domain.course.Course;
import org.unimanage.domain.course.Major;
import org.unimanage.repository.CourseRepository;
import org.unimanage.repository.MajorRepository;
import org.unimanage.service.MajorService;
import org.unimanage.util.exception.AccessDeniedException;
import org.unimanage.util.exception.DuplicateEntityException;
import java.util.List;
import java.util.UUID;


@Service

public class MajorServiceImpl extends BaseServiceImpl<Major, Long> implements MajorService {

    private final MajorRepository majorRepository;
    private final CourseRepository courseRepository;
    private final MessageSource messageSource;

    public MajorServiceImpl(MajorRepository majorRepository, CourseRepository courseRepository, MessageSource messageSource) {
        super(majorRepository);
        this.majorRepository = majorRepository;
        this.courseRepository = courseRepository;
        this.messageSource = messageSource;
    }

    @Override
    protected void prePersist(Major entity) {
        if (majorRepository.existsByName(entity.getName())) {
            throw new DuplicateEntityException(messageSource.getMessage("error.major.duplicate.name",new Object[]{entity.getName()}, LocaleContextHolder.getLocale()));
        }
        entity.setCode(UUID.randomUUID());
        entity.setActive(true);
    }

    @Override
    public void deleteById(Long id) {
        Major major = majorRepository.findById(id)
                .orElseThrow(() -> new org.unimanage.util.exception.EntityNotFoundException(messageSource.getMessage("error.major.not.found.by.id",new Object[]{id}, LocaleContextHolder.getLocale())));
       if (!major.getActive()){
           String errorMessage = messageSource.getMessage(
                   "error.major.already.inactive",
                   new Object[]{major.getName()},
                   LocaleContextHolder.getLocale()
           );
           throw new AccessDeniedException(errorMessage);
       }
        major.setActive(false);
        majorRepository.save(major);
    }

    @Override
    public Major findById(Long majorId) {
        Major major = majorRepository.findById(majorId)
                .orElseThrow(() ->
                        new org.unimanage.util.exception.EntityNotFoundException(messageSource.getMessage("",new Object[]{""}, LocaleContextHolder.getLocale())));

        if (major.getActive()){
            return major;
        }

        throw new AccessDeniedException(messageSource.getMessage("",new Object[]{},LocaleContextHolder.getLocale()));
    }

    @Override
    public List<Course> getCoursesByMajor(Long majorId) {
        return courseRepository.findCourseByMajor_Id(majorId);
    }

    @Override
    public List<Major> findAll() {
        return majorRepository.findMajorByActiveIsTrue();
    }

}

