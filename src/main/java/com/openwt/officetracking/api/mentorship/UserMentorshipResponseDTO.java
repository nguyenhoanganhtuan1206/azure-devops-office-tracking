package com.openwt.officetracking.api.mentorship;

import com.openwt.officetracking.domain.contract_type.ContractType;
import com.openwt.officetracking.domain.mentorship_status.CoachMentorMenteeStatus;
import com.openwt.officetracking.domain.mentorship_status.MentorshipStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Builder
@Setter
@Getter
public class UserMentorshipResponseDTO {

    private UUID id;

    private String firstName;

    private String lastName;

    private String personalEmail;

    private String companyEmail;

    private String photo;

    private Instant dateOfBirth;

    private ContractType contractType;

    private String phoneNumber;

    private String qrCode;

    private UUID positionId;

    private String positionName;

    private MentorshipStatus mentorshipStatus;

    private Integer numberOfCourses;

    private CoachMentorMenteeStatus coachStatus;

    private CoachMentorMenteeStatus mentorStatus;

    private CoachMentorMenteeStatus menteeStatus;
}
