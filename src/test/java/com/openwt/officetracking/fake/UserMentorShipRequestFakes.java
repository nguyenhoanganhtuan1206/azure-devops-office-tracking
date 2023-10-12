package com.openwt.officetracking.fake;

import com.openwt.officetracking.api.mentorship.UserMentorShipRequestDTO;
import lombok.experimental.UtilityClass;

import static java.util.UUID.randomUUID;

@UtilityClass
public class UserMentorShipRequestFakes {

    public static UserMentorShipRequestDTO buildUserMentorShipRequestDTO() {
        return UserMentorShipRequestDTO.builder()
                .userId(randomUUID())
                .build();
    }
}