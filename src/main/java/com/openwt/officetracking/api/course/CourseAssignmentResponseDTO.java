package com.openwt.officetracking.api.course;

import com.openwt.officetracking.api.user.UserResponseDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseAssignmentResponseDTO {

    private UserResponseDTO mentor;

    private List<CoachAssignmentResponseDTO> coachAssignments ;
}
