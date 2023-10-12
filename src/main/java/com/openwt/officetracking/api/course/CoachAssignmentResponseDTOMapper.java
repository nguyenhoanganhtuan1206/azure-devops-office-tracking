package com.openwt.officetracking.api.course;

import com.openwt.officetracking.domain.course.CoachAssignmentResponse;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.openwt.officetracking.api.user.UserDTOMapper.toUserDTO;

@UtilityClass
public class CoachAssignmentResponseDTOMapper {

    public static CoachAssignmentResponseDTO toMenteeCoachResponseDTO(final CoachAssignmentResponse coachAssignmentResponse) {
        return CoachAssignmentResponseDTO.builder()
                .mentee(toUserDTO(coachAssignmentResponse.getMentee()))
                .coach(toUserDTO(coachAssignmentResponse.getCoach()))
                .build();
    }

    public static List<CoachAssignmentResponseDTO> toMenteeCoachResponseDTOs(final List<CoachAssignmentResponse> coachAssignmentResponses) {
        return coachAssignmentResponses.stream()
                .map(CoachAssignmentResponseDTOMapper::toMenteeCoachResponseDTO)
                .toList();
    }
}
