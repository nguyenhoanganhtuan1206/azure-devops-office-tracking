package com.openwt.officetracking.domain.course;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CourseRequestMapper {

    public static Course toCourse(final CourseRequest courseRequest) {
        return Course.builder()
                .name(courseRequest.getName())
                .startAt(courseRequest.getStartAt())
                .endAt(courseRequest.getEndAt())
                .description(courseRequest.getDescription())
                .build();
    }
}
