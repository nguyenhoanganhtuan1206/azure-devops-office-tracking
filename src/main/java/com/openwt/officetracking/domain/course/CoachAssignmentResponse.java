package com.openwt.officetracking.domain.course;

import com.openwt.officetracking.domain.user.User;
import lombok.*;

@Getter
@Setter
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoachAssignmentResponse {

    private User mentee;

    private User coach;
}
