package com.openwt.officetracking.api.course;

import com.openwt.officetracking.domain.course.CourseAssignmentResponse;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.openwt.officetracking.api.course.CoachAssignmentResponseDTOMapper.toMenteeCoachResponseDTOs;
import static com.openwt.officetracking.api.user.UserDTOMapper.toUserDTO;

@UtilityClass
public class CourseAssignmentResponseDTOMapper {

    public static CourseAssignmentResponseDTO toCourseAssignmentResponseDTO(final CourseAssignmentResponse courseAssignmentResponse) {
        return CourseAssignmentResponseDTO.builder()
                .mentor(toUserDTO(courseAssignmentResponse.getMentor()))
                .coachAssignments(toMenteeCoachResponseDTOs(courseAssignmentResponse.getCoachAssignments()))
                .build();
    }

    public static List<CourseAssignmentResponseDTO> toCourseAssignmentResponseDTOs(final List<CourseAssignmentResponse> courseAssignmentResponses) {
        return courseAssignmentResponses.stream()
                .map(CourseAssignmentResponseDTOMapper::toCourseAssignmentResponseDTO)
                .toList();
    }
}
