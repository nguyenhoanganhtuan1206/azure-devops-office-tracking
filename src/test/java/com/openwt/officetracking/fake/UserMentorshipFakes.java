package com.openwt.officetracking.fake;

import com.openwt.officetracking.domain.contract_type.ContractType;
import com.openwt.officetracking.domain.mentorship.UserMentorShip;
import com.openwt.officetracking.domain.mentorship_status.CoachMentorMenteeStatus;
import com.openwt.officetracking.domain.mentorship_status.MentorshipStatus;
import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.util.List;

import static com.openwt.officetracking.fake.CommonFakes.buildList;
import static com.openwt.officetracking.fake.CommonFakes.randomInteger;
import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@UtilityClass
public class UserMentorshipFakes {

    public static UserMentorShip buildUserMentorship() {
        return UserMentorShip.builder()
                .id(randomUUID())
                .firstName(randomAlphabetic(3, 10))
                .lastName(randomAlphabetic(3, 10))
                .personalEmail("example@gmail.com")
                .companyEmail("example@openwt.com")
                .positionId(randomUUID())
                .contractType(ContractType.EMPLOYEE)
                .qrCode(randomAlphabetic(6))
                .photo("data:image/png;base64,qetT7ef6DBfb")
                .phoneNumber("0987654321")
                .dateOfBirth(Instant.now())
                .positionId(randomUUID())
                .mentorshipStatus(MentorshipStatus.IN_COURSE)
                .coachStatus(CoachMentorMenteeStatus.ACTIVE)
                .mentorStatus(CoachMentorMenteeStatus.ACTIVE)
                .menteeStatus(CoachMentorMenteeStatus.ACTIVE)
                .numberOfCourses(randomInteger(50))
                .build();
    }

    public static List<UserMentorShip> buildUserMentorShips() {
        return buildList(UserMentorshipFakes::buildUserMentorship);
    }
}
