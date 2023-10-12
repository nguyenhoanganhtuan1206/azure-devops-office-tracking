package com.openwt.officetracking.api.mentorship;

import com.openwt.officetracking.domain.course.MentorCourse;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class MentorCourseResponseDTOMapper {

    public static MentorCourseResponseDTO toMentorCourseResponseDTO(final MentorCourse mentorCourse) {
        return MentorCourseResponseDTO.builder()
                .id(mentorCourse.getId())
                .name(mentorCourse.getName())
                .startAt(mentorCourse.getStartAt())
                .endAt(mentorCourse.getEndAt())
                .status(mentorCourse.getStatus())
                .mentees(mentorCourse.getCourseMentees())
                .build();
    }

    public static List<MentorCourseResponseDTO> toMentorCourseResponseDTOs(final List<MentorCourse> mentorCourses) {
        return mentorCourses.stream()
                .map(MentorCourseResponseDTOMapper::toMentorCourseResponseDTO)
                .toList();
    }
}
