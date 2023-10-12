package com.openwt.officetracking.api.course;

import com.openwt.officetracking.domain.course.CourseRequest;
import lombok.experimental.UtilityClass;

import static com.openwt.officetracking.api.course.CourseAssignmentRequestDTOMapper.toCourseAssignmentRequests;

@UtilityClass
public class CourseRequestDTOMapper {

    public static CourseRequest toCourseRequest(final CourseRequestDTO courseRequestDTO) {
        return CourseRequest.builder()
                .name(courseRequestDTO.getName())
                .description(courseRequestDTO.getDescription())
                .startAt(courseRequestDTO.getStartAt())
                .endAt(courseRequestDTO.getEndAt())
                .courseAssignments(toCourseAssignmentRequests(courseRequestDTO.getCourseAssignments()))
                .build();
    }
}
