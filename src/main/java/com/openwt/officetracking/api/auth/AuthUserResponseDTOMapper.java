package com.openwt.officetracking.api.auth;

import com.openwt.officetracking.domain.user.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AuthUserResponseDTOMapper {

    public static AuthUserResponseDTO toUserLoginDTO(final User user) {
        return AuthUserResponseDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .identifier(user.getIdentifier())
                .personalEmail(user.getPersonalEmail())
                .companyEmail(user.getCompanyEmail())
                .level(user.getLevel())
                .positionId(user.getPositionId())
                .gender(user.getGender())
                .role(user.getRole())
                .qrCode(user.getQrCode())
                .photo(user.getPhoto())
                .dateOfBirth(user.getDateOfBirth())
                .address(user.getAddress())
                .university(user.getUniversity())
                .contractType(user.getContractType())
                .phoneNumber(user.getPhoneNumber())
                .isActive(user.isActive())
                .startDate(user.getStartDate())
                .endDate(user.getEndDate())
                .build();
    }
}
