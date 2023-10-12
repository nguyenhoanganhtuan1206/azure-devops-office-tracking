package com.openwt.officetracking.domain.user;

import com.openwt.officetracking.domain.contract_type.ContractType;
import com.openwt.officetracking.domain.gender.Gender;
import com.openwt.officetracking.domain.role.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@With
public class UserDetail {

    private UUID id;

    private String personalEmail;

    private String companyEmail;

    private String identifier;

    private String firstName;

    private String lastName;

    private Instant dateOfBirth;

    private Gender gender;

    private ContractType contractType;

    private String phoneNumber;

    private String positionName;

    private Instant startDate;

    private Instant endDate;
}
