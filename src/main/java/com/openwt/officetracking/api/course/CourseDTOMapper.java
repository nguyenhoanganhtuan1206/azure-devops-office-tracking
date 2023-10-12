package com.openwt.officetracking.api.course;

import com.openwt.officetracking.domain.course.Course;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class CourseDTOMapper {

    public static CourseResponseDTO toCourseResponseDTO(final Course course) {
        return CourseResponseDTO.builder()
                .id(course.getId())
                .name(course.getName())
                .startAt(course.getStartAt())
                .endAt(course.getEndAt())
                .status(course.getStatus())
                .assign(course.getAssign())
                .build();
    }

    public static List<CourseResponseDTO> toCourseDTOs(final List<Course> courses) {
        return courses.stream()
                .map(CourseDTOMapper::toCourseResponseDTO)
                .toList();
    }
}
