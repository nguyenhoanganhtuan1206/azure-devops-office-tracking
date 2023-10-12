package com.openwt.officetracking.persistent.user;

import com.openwt.officetracking.domain.user.User;
import lombok.experimental.UtilityClass;

import java.util.List;

import static org.apache.commons.collections4.ListUtils.emptyIfNull;

@UtilityClass
public class UserEntityMapper {

    public static User toUser(final UserEntity userEntity) {
        return User.builder()
                .id(userEntity.getId())
                .personalEmail(userEntity.getPersonalEmail())
                .companyEmail(userEntity.getCompanyEmail())
                .password(userEntity.getPassword())
                .identifier(userEntity.getIdentifier())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .level(userEntity.getLevel())
                .role(userEntity.getRole())
                .gender(userEntity.getGender())
                .positionId(userEntity.getPositionId())
                .contractType(userEntity.getContractType())
                .qrCode(userEntity.getQrCode())
                .photo(userEntity.getPhoto())
                .lastPasswordFailedAt(userEntity.getLastPasswordFailedAt())
                .passwordFailedCount(userEntity.getPasswordFailedCount())
                .dateOfBirth(userEntity.getDateOfBirth())
                .address(userEntity.getAddress())
                .university(userEntity.getUniversity())
                .contractType(userEntity.getContractType())
                .phoneNumber(userEntity.getPhoneNumber())
                .isActive(userEntity.isActive())
                .mentorStatus(userEntity.getMentorStatus())
                .menteeStatus(userEntity.getMenteeStatus())
                .startDate(userEntity.getStartDate())
                .endDate(userEntity.getEndDate())
                .createdAt(userEntity.getCreatedAt())
                .lastSendResetPasswordAt(userEntity.getLastSendResetPasswordAt())
                .resetPasswordCode(userEntity.getResetPasswordCode())
                .assignedCoachAt(userEntity.getAssignedCoachAt())
                .assignedMentorAt(userEntity.getAssignedMentorAt())
                .assignedMenteeAt(userEntity.getAssignedMenteeAt())
                .coachStatus(userEntity.getCoachStatus())
                .menteeStatus(userEntity.getMenteeStatus())
                .mentorStatus(userEntity.getMentorStatus())
                .build();
    }

    public static List<User> toUsers(final List<UserEntity> userEntities) {
        return emptyIfNull(userEntities)
                .stream()
                .map(UserEntityMapper::toUser)
                .toList();
    }

    public static UserEntity toUserEntity(final User user) {
        return UserEntity.builder()
                .id(user.getId())
                .personalEmail(user.getPersonalEmail())
                .companyEmail(user.getCompanyEmail())
                .password(user.getPassword())
                .identifier(user.getIdentifier())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .level(user.getLevel())
                .role(user.getRole())
                .gender(user.getGender())
                .positionId(user.getPositionId())
                .contractType(user.getContractType())
                .qrCode(user.getQrCode())
                .photo(user.getPhoto())
                .lastPasswordFailedAt(user.getLastPasswordFailedAt())
                .passwordFailedCount(user.getPasswordFailedCount())
                .dateOfBirth(user.getDateOfBirth())
                .address(user.getAddress())
                .university(user.getUniversity())
                .contractType(user.getContractType())
                .phoneNumber(user.getPhoneNumber())
                .isActive(user.isActive())
                .coachStatus(user.getCoachStatus())
                .mentorStatus(user.getMentorStatus())
                .menteeStatus(user.getMenteeStatus())
                .startDate(user.getStartDate())
                .endDate(user.getEndDate())
                .createdAt(user.getCreatedAt())
                .lastSendResetPasswordAt(user.getLastSendResetPasswordAt())
                .resetPasswordCode(user.getResetPasswordCode())
                .assignedMentorAt(user.getAssignedMentorAt())
                .assignedMenteeAt(user.getAssignedMenteeAt())
                .assignedCoachAt(user.getAssignedCoachAt())
                .build();
    }
}
