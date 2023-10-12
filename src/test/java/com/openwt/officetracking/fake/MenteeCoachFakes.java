package com.openwt.officetracking.fake;

import com.openwt.officetracking.domain.course.CoachAssignmentRequest;
import com.openwt.officetracking.domain.course.CoachAssignmentResponse;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.openwt.officetracking.fake.CommonFakes.buildList;
import static com.openwt.officetracking.fake.UserFakes.buildUser;
import static java.util.UUID.randomUUID;

@UtilityClass
public class MenteeCoachFakes {

    public static CoachAssignmentResponse buildMenteeCoachResponse() {
        return CoachAssignmentResponse.builder()
                .mentee(buildUser())
                .coach(buildUser())
                .build();
    }

    public static List<CoachAssignmentResponse> buildMenteeCoachResponses() {
        return buildList(MenteeCoachFakes::buildMenteeCoachResponse);
    }

    public static CoachAssignmentRequest buildMenteeCoachRequest() {
        return CoachAssignmentRequest.builder()
                .menteeId(randomUUID())
                .coachId(randomUUID())
                .build();
    }

    public static List<CoachAssignmentRequest> buildMenteeCoachRequests() {
        return buildList(MenteeCoachFakes::buildMenteeCoachRequest);
    }
}
