package com.openwt.officetracking.api.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.openwt.officetracking.converter.NullIfEmptyConverter;
import com.openwt.officetracking.domain.contract_type.ContractType;
import com.openwt.officetracking.domain.gender.Gender;
import com.openwt.officetracking.domain.level.Level;
import com.openwt.officetracking.domain.role.Role;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {

    @JsonDeserialize(using = NullIfEmptyConverter.class)
    private String personalEmail;

    private String companyEmail;

    @JsonDeserialize(using = NullIfEmptyConverter.class)
    private String identifier;

    private String firstName;

    private String lastName;

    private Level level;

    private Instant dateOfBirth;

    @JsonDeserialize(using = NullIfEmptyConverter.class)
    private String address;

    @JsonDeserialize(using = NullIfEmptyConverter.class)
    private String university;

    private Gender gender;

    private Role role;

    private ContractType contractType;

    private String phoneNumber;

    private UUID positionId;

    private boolean isActive;

    private Instant startDate;

    private Instant endDate;

    @JsonDeserialize(using = NullIfEmptyConverter.class)
    private String photo;

    private boolean changeCode;
}
