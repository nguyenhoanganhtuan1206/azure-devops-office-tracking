package com.openwt.officetracking.api.user;

import com.openwt.officetracking.domain.user.User;
import com.openwt.officetracking.domain.user.UserDetail;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class UserDTOMapper {

    public static UserResponseDTO toUserDTO(final User user) {
        if (user == null) {
            return null;
        }

        return UserResponseDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .identifier(user.getIdentifier())
                .personalEmail(user.getPersonalEmail())
                .companyEmail(user.getCompanyEmail())
                .positionId(user.getPositionId())
                .gender(user.getGender())
                .level(user.getLevel())
                .role(user.getRole())
                .contractType(user.getContractType())
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
                .createdAt(user.getCreatedAt())
                .build();
    }

    public static List<UserResponseDTO> toUserDTOs(final List<User> users) {
        return users.stream()
                .map(UserDTOMapper::toUserDTO)
                .toList();
    }

    public static User toUser(final UserRequestDTO userRequestDTO) {
        return User.builder()
                .firstName(userRequestDTO.getFirstName())
                .lastName(userRequestDTO.getLastName())
                .identifier(userRequestDTO.getIdentifier())
                .personalEmail(userRequestDTO.getPersonalEmail())
                .companyEmail(userRequestDTO.getCompanyEmail())
                .level(userRequestDTO.getLevel())
                .positionId(userRequestDTO.getPositionId())
                .gender(userRequestDTO.getGender())
                .photo(userRequestDTO.getPhoto())
                .role(userRequestDTO.getRole())
                .dateOfBirth(userRequestDTO.getDateOfBirth())
                .address(userRequestDTO.getAddress())
                .university(userRequestDTO.getUniversity())
                .contractType(userRequestDTO.getContractType())
                .phoneNumber(userRequestDTO.getPhoneNumber())
                .isActive(userRequestDTO.isActive())
                .startDate(userRequestDTO.getStartDate())
                .endDate(userRequestDTO.getEndDate())
                .contractType(userRequestDTO.getContractType())
                .changeCode(userRequestDTO.isChangeCode())
                .build();
    }

    public static UserDetailDTO toUserDetailDTO(final UserDetail userDetail) {
        return UserDetailDTO.builder()
                .id(userDetail.getId())
                .firstName(userDetail.getFirstName())
                .lastName(userDetail.getLastName())
                .identifier(userDetail.getIdentifier())
                .personalEmail(userDetail.getPersonalEmail())
                .companyEmail(userDetail.getCompanyEmail())
                .positionName(userDetail.getPositionName())
                .gender(userDetail.getGender())
                .contractType(userDetail.getContractType())
                .dateOfBirth(userDetail.getDateOfBirth())
                .phoneNumber(userDetail.getPhoneNumber())
                .startDate(userDetail.getStartDate())
                .endDate(userDetail.getEndDate())
                .build();
    }
}
