package com.openwt.officetracking.domain.user;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {

    public static User toUser(final GoogleIdToken.Payload payload) {
        return User.builder()
                .personalEmail(payload.getEmail())
                .firstName((String) payload.get("family_name"))
                .lastName((String) payload.get("given_name"))
                .build();
    }

    public static UserProfileUpdate toUserProfileUpdate(final User user) {
        return UserProfileUpdate.builder()
                .id(user.getId())
                .companyEmail(user.getCompanyEmail())
                .personalEmail(user.getPersonalEmail())
                .identifier(user.getIdentifier())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dateOfBirth(user.getDateOfBirth())
                .gender(user.getGender())
                .photo(user.getPhoto())
                .address(user.getAddress())
                .contractType(user.getContractType())
                .phoneNumber(user.getPhoneNumber())
                .startDate(user.getStartDate())
                .endDate(user.getEndDate())
                .build();
    }
}
