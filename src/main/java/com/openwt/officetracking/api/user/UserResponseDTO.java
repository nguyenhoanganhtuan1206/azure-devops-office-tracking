package com.openwt.officetracking.api.user;

import com.openwt.officetracking.domain.contract_type.ContractType;
import com.openwt.officetracking.domain.gender.Gender;
import com.openwt.officetracking.domain.level.Level;
import com.openwt.officetracking.domain.role.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
public class UserResponseDTO {

    private UUID id;

    private String personalEmail;

    private String companyEmail;

    private String identifier;

    private String firstName;

    private String lastName;

    private String photo;

    private Level level;

    private Instant dateOfBirth;

    private String address;

    private String university;

    private Gender gender;

    private Role role;

    private ContractType contractType;

    private String phoneNumber;

    private UUID positionId;

    private String qrCode;

    private boolean isActive;

    private Instant startDate;

    private Instant endDate;

    private Instant createdAt;
}
