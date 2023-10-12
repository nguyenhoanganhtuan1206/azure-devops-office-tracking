package com.openwt.officetracking.api.course;

import com.openwt.officetracking.domain.course.CourseAssignmentRequest;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.openwt.officetracking.api.course.CoachAssignmentRequestDTOMapper.toMenteeCoachRequests;

@UtilityClass
public class CourseAssignmentRequestDTOMapper {

    public static CourseAssignmentRequest toCourseAssignmentRequest(final CourseAssignmentRequestDTO courseAssignmentRequestDTO) {
        return CourseAssignmentRequest.builder()
                .mentorId(courseAssignmentRequestDTO.getMentorId())
                .coachAssignments(toMenteeCoachRequests(courseAssignmentRequestDTO.getCoachAssignments()))
                .build();
    }

    public static List<CourseAssignmentRequest> toCourseAssignmentRequests(final List<CourseAssignmentRequestDTO> courseAssignmentRequestDTOs) {
        return courseAssignmentRequestDTOs.stream()
                .map(CourseAssignmentRequestDTOMapper::toCourseAssignmentRequest)
                .toList();
    }
}
