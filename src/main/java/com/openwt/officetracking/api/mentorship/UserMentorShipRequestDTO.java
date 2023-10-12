package com.openwt.officetracking.api.mentorship;

import lombok.*;

import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserMentorShipRequestDTO {

    private UUID userId;
}
