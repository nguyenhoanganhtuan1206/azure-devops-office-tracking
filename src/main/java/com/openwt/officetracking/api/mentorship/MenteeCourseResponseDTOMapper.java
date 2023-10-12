package com.openwt.officetracking.api.mentorship;

import com.openwt.officetracking.domain.course.MenteeCourse;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class MenteeCourseResponseDTOMapper {

    public static MenteeCourseResponseDTO toMenteeCourseResponseDTO(final MenteeCourse menteeCourse) {
        return MenteeCourseResponseDTO.builder()
                .id(menteeCourse.getId())
                .name(menteeCourse.getName())
                .startAt(menteeCourse.getStartAt())
                .endAt(menteeCourse.getEndAt())
                .status(menteeCourse.getStatus())
                .mentors(menteeCourse.getMentors())
                .build();
    }

    public static List<MenteeCourseResponseDTO> toMenteeCourseResponseDTOs(final List<MenteeCourse> menteeCourses) {
        return menteeCourses.stream()
                .map(MenteeCourseResponseDTOMapper::toMenteeCourseResponseDTO)
                .toList();
    }
}