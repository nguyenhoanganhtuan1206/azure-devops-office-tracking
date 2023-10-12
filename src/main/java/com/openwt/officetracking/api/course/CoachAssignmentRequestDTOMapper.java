package com.openwt.officetracking.api.course;

import com.openwt.officetracking.domain.course.CoachAssignmentRequest;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class CoachAssignmentRequestDTOMapper {

    public static CoachAssignmentRequest toMenteeCoachRequest(final CoachAssignmentRequestDTO coachAssignmentRequestDTO) {
        return CoachAssignmentRequest.builder()
                .menteeId(coachAssignmentRequestDTO.getMenteeId())
                .coachId(coachAssignmentRequestDTO.getCoachId())
                .build();
    }

    public static List<CoachAssignmentRequest> toMenteeCoachRequests(final List<CoachAssignmentRequestDTO> coachAssignmentRequestDTOS) {
        return coachAssignmentRequestDTOS.stream()
                .map(CoachAssignmentRequestDTOMapper::toMenteeCoachRequest)
                .toList();
    }
}
