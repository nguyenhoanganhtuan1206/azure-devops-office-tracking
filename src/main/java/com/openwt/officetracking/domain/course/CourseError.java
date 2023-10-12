package com.openwt.officetracking.domain.course;

import com.openwt.officetracking.error.BadRequestException;
import com.openwt.officetracking.error.NotFoundException;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class CourseError {

    public static Supplier<NotFoundException> supplyCourseNotFound(final String fieldName, final Object fieldValue) {
        return () -> new NotFoundException("Course with %s %s not found", fieldName, fieldValue);
    }

    public static Supplier<BadRequestException> supplyCourseExisted(final String fieldName, final Object fieldValue) {
        return () -> new BadRequestException("Course with %s %s has been taken", fieldName, fieldValue);
    }

    public static Supplier<BadRequestException> supplyCourseValidation(final String message) {
        return () -> new BadRequestException(message);
    }
}
