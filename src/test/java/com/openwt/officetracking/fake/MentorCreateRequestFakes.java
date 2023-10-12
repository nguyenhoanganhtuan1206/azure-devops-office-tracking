package com.openwt.officetracking.fake;

import com.openwt.officetracking.api.mentorship.UserMentorShipRequestDTO;
import lombok.experimental.UtilityClass;

import static java.util.UUID.randomUUID;

@UtilityClass
public class MentorCreateRequestFakes {

    public static UserMentorShipRequestDTO buildMentorShipRequestDTO() {
        return UserMentorShipRequestDTO.builder()
                .userId(randomUUID())
                .build();
    }
}
