package com.openwt.officetracking.domain.course;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CourseDetailResponseMapper {

    public static CourseDetailResponse toCourseDetailResponse(final Course course) {
        return CourseDetailResponse.builder()
                .id(course.getId())
                .name(course.getName())
                .description(course.getDescription())
                .createdAt(course.getCreatedAt())
                .startAt(course.getStartAt())
                .endAt(course.getEndAt())
                .updatedAt(course.getUpdatedAt())
                .build();
    }
}
