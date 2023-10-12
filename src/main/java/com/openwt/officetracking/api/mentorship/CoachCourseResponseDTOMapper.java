package com.openwt.officetracking.api.mentorship;

import com.openwt.officetracking.domain.course.CoachCourse;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class CoachCourseResponseDTOMapper {

    public static CoachCourseResponseDTO toCoachCourseResponseDTO(final CoachCourse coachCourse) {
        return CoachCourseResponseDTO.builder()
                .id(coachCourse.getId())
                .name(coachCourse.getName())
                .startAt(coachCourse.getStartAt())
                .endAt(coachCourse.getEndAt())
                .status(coachCourse.getStatus())
                .mentees(coachCourse.getCoachMentees())
                .build();
    }

    public static List<CoachCourseResponseDTO> toCoachCourseResponseDTOs(final List<CoachCourse> coachCourses) {
        return coachCourses.stream()
                .map(CoachCourseResponseDTOMapper::toCoachCourseResponseDTO)
                .toList();
    }
}
