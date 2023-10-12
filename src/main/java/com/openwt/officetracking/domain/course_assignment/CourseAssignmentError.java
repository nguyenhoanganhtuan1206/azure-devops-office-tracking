package com.openwt.officetracking.domain.course_assignment;

import com.openwt.officetracking.error.BadRequestException;
import com.openwt.officetracking.error.NotFoundException;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class CourseAssignmentError {

    public static Supplier<NotFoundException> supplyCourseAssignmentNotFound(final String fieldName, final Object fieldValue) {
        return () -> new NotFoundException("Course assignment with %s %s not found", fieldName, fieldValue);
    }

    public static Supplier<BadRequestException> supplyMentorAndMenteeDuplicate(final String message) {
        return () -> new BadRequestException(message);
    }
}
