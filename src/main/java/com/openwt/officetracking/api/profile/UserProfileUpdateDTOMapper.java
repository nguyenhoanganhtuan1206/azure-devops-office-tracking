package com.openwt.officetracking.api.profile;

import com.openwt.officetracking.domain.user.UserProfileUpdate;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserProfileUpdateDTOMapper {

    public static UserProfileUpdateDTO toUserProfileUpdateDTO(final UserProfileUpdate userProfileUpdate) {
        return UserProfileUpdateDTO.builder()
                .id(userProfileUpdate.getId())
                .companyEmail(userProfileUpdate.getCompanyEmail())
                .personalEmail(userProfileUpdate.getPersonalEmail())
                .identifier(userProfileUpdate.getIdentifier())
                .firstName(userProfileUpdate.getFirstName())
                .lastName(userProfileUpdate.getLastName())
                .dateOfBirth(userProfileUpdate.getDateOfBirth())
                .gender(userProfileUpdate.getGender())
                .photo(userProfileUpdate.getPhoto())
                .address(userProfileUpdate.getAddress())
                .contractType(userProfileUpdate.getContractType())
                .phoneNumber(userProfileUpdate.getPhoneNumber())
                .positionName(userProfileUpdate.getPositionName())
                .startDate(userProfileUpdate.getStartDate())
                .endDate(userProfileUpdate.getEndDate())
                .build();
    }
}
