package com.openwt.officetracking.api.course;

import com.openwt.officetracking.domain.course.CourseDetailResponse;
import lombok.experimental.UtilityClass;

import static com.openwt.officetracking.api.course.CourseAssignmentResponseDTOMapper.toCourseAssignmentResponseDTOs;

@UtilityClass
public class CourseDetailResponseDTOMapper {

    public static CourseDetailResponseDTO toCourseDetailResponseDTOMapper(final CourseDetailResponse courseDetailResponse) {
        return CourseDetailResponseDTO.builder()
                .id(courseDetailResponse.getId())
                .name(courseDetailResponse.getName())
                .description(courseDetailResponse.getDescription())
                .startAt(courseDetailResponse.getStartAt())
                .endAt(courseDetailResponse.getEndAt())
                .createdAt(courseDetailResponse.getCreatedAt())
                .updatedAt(courseDetailResponse.getUpdatedAt())
                .courseAssignments(toCourseAssignmentResponseDTOs(courseDetailResponse.getCourseAssignments()))
                .build();
    }
}
