package com.openwt.officetracking.persistent.course;

import com.openwt.officetracking.domain.course.Course;
import lombok.experimental.UtilityClass;

import java.util.List;

import static org.apache.commons.collections4.ListUtils.emptyIfNull;

@UtilityClass
public class CourseEntityMapper {

    public static Course toCourse(final CourseEntity courseEntity) {
        return Course.builder()
                .id(courseEntity.getId())
                .name(courseEntity.getName())
                .startAt(courseEntity.getStartAt())
                .endAt(courseEntity.getEndAt())
                .description(courseEntity.getDescription())
                .createdAt(courseEntity.getCreatedAt())
                .updatedAt(courseEntity.getUpdatedAt())
                .build();
    }

    public static List<Course> toCourses(final List<CourseEntity> courseEntities) {
        return emptyIfNull(courseEntities)
                .stream()
                .map(CourseEntityMapper::toCourse)
                .toList();
    }

    public static CourseEntity toCourseEntity(final Course course) {
        return CourseEntity.builder()
                .id(course.getId())
                .name(course.getName())
                .startAt(course.getStartAt())
                .endAt(course.getEndAt())
                .createdAt(course.getCreatedAt())
                .description(course.getDescription())
                .updatedAt(course.getUpdatedAt())
                .description(course.getDescription())
                .build();
    }
}
