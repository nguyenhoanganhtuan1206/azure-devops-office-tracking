package com.openwt.officetracking.domain.course;

import lombok.experimental.UtilityClass;

import static com.openwt.officetracking.error.CommonError.supplyValidationError;
import static org.apache.commons.lang3.StringUtils.isBlank;

@UtilityClass
public class CourseValidation {

    public static void validateCourse(final Course course) {
        validateCourseName(course.getName());
    }

    private void validateCourseName(final String courseName) {
        if (isBlank(courseName)) {
            throw supplyValidationError("Course name cannot be blank").get();
        }
    }
}
