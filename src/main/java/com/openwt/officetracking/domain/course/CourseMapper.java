package com.openwt.officetracking.domain.course;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CourseMapper {

    public static MenteeCourse toMenteeCourse(final Course course) {
        return MenteeCourse.builder()
                .id(course.getId())
                .name(course.getName())
                .startAt(course.getStartAt())
                .endAt(course.getEndAt())
                .status(course.getStatus())
                .build();
    }

    public static MentorCourse toMentorCourse(final Course course) {
        return MentorCourse.builder()
                .id(course.getId())
                .name(course.getName())
                .startAt(course.getStartAt())
                .endAt(course.getEndAt())
                .status(course.getStatus())
                .build();
    }

    public static CoachCourse toCoachCourse(final Course course) {
        return CoachCourse.builder()
                .id(course.getId())
                .name(course.getName())
                .startAt(course.getStartAt())
                .endAt(course.getEndAt())
                .status(course.getStatus())
                .build();
    }
}
