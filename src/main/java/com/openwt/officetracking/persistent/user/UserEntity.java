package com.openwt.officetracking.persistent.user;

import com.openwt.officetracking.domain.contract_type.ContractType;
import com.openwt.officetracking.domain.gender.Gender;
import com.openwt.officetracking.domain.level.Level;
import com.openwt.officetracking.domain.mentorship_status.CoachMentorMenteeStatus;
import com.openwt.officetracking.domain.role.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Table(name = "users")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String personalEmail;

    private String companyEmail;

    private String password;

    private String firstName;

    private String lastName;

    private String identifier;

    private String photo;

    @Enumerated(EnumType.STRING)
    private Level level;

    private Instant dateOfBirth;

    private String address;

    private String university;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private ContractType contractType;

    private String phoneNumber;

    private UUID positionId;

    private String qrCode;

    private Instant lastPasswordFailedAt;

    private int passwordFailedCount;

    private boolean isActive;

    @Enumerated(EnumType.STRING)
    private CoachMentorMenteeStatus coachStatus;

    @Enumerated(EnumType.STRING)
    private CoachMentorMenteeStatus mentorStatus;

    @Enumerated(EnumType.STRING)
    private CoachMentorMenteeStatus menteeStatus;

    private Instant startDate;

    private Instant endDate;

    private Instant createdAt;

    private Instant lastSendResetPasswordAt;

    private String resetPasswordCode;

    private Instant assignedCoachAt;

    private Instant assignedMentorAt;

    private Instant assignedMenteeAt;
}