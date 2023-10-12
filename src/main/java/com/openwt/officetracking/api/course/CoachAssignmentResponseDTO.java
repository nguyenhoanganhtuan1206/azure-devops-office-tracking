package com.openwt.officetracking.api.course;

import com.openwt.officetracking.api.user.UserResponseDTO;
import lombok.*;

@Getter
@Setter
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoachAssignmentResponseDTO {

    private UserResponseDTO mentee;

    private UserResponseDTO coach;
}
