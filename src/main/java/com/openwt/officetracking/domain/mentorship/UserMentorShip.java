package com.openwt.officetracking.domain.mentorship;

import com.openwt.officetracking.domain.contract_type.ContractType;
import com.openwt.officetracking.domain.mentorship_status.CoachMentorMenteeStatus;
import com.openwt.officetracking.domain.mentorship_status.MentorshipStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

import java.time.Instant;
import java.util.UUID;

@Builder
@Getter
@Setter
@With
public class UserMentorShip {

    private UUID id;

    private String firstName;

    private String lastName;

    private UUID positionId;

    private String positionName;

    private String personalEmail;

    private String companyEmail;

    private String photo;

    private Instant dateOfBirth;

    private ContractType contractType;

    private String phoneNumber;

    private String qrCode;

    private MentorshipStatus mentorshipStatus;

    private CoachMentorMenteeStatus coachStatus;

    private CoachMentorMenteeStatus mentorStatus;

    private CoachMentorMenteeStatus menteeStatus;

    private Integer numberOfCourses;
}
