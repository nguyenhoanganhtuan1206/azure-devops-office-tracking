package com.openwt.officetracking.api.profile;

import com.openwt.officetracking.domain.contract_type.ContractType;
import com.openwt.officetracking.domain.gender.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
public class UserProfileUpdateDTO {

    private UUID id;

    private String personalEmail;

    private String companyEmail;

    private String identifier;

    private String firstName;

    private String lastName;

    private Instant dateOfBirth;

    private Gender gender;

    private String photo;

    private String address;

    private ContractType contractType;

    private String phoneNumber;

    private String positionName;

    private Instant startDate;

    private Instant endDate;
}
