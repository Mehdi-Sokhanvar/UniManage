package org.unimanage.util.customAnnotation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.unimanage.util.dto.OfferedCourseDTO;


public class StartBeforeEndTimeValidator implements ConstraintValidator<StartBeforeEndTime, OfferedCourseDTO> {


    @Override
    public boolean isValid(OfferedCourseDTO dto, ConstraintValidatorContext context) {
        if (dto.getStartTime() == null || dto.getEndTime() == null) {
            return true;
        }

        boolean isValid = dto.getStartTime().isBefore(dto.getEndTime());

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Start time must be before end time")
                    .addPropertyNode("startTime")
                    .addConstraintViolation();
        }
        return isValid;
    }
}
