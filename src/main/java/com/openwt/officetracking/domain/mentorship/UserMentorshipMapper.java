package com.openwt.officetracking.domain.mentorship;

import com.openwt.officetracking.domain.user.User;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class UserMentorshipMapper {

    public static User toUser(final UserMentorShip userMentorShip) {
        return User.builder()
                .id(userMentorShip.getId())
                .firstName(userMentorShip.getFirstName())
                .lastName(userMentorShip.getLastName())
                .positionId(userMentorShip.getPositionId())
                .personalEmail(userMentorShip.getPersonalEmail())
                .companyEmail(userMentorShip.getCompanyEmail())
                .photo(userMentorShip.getPhoto())
                .dateOfBirth(userMentorShip.getDateOfBirth())
                .contractType(userMentorShip.getContractType())
                .phoneNumber(userMentorShip.getPhoneNumber())
                .qrCode(userMentorShip.getQrCode())
                .coachStatus(userMentorShip.getCoachStatus())
                .mentorStatus(userMentorShip.getMentorStatus())
                .menteeStatus(userMentorShip.getMenteeStatus())
                .build();
    }

    public static List<User> toUsers(final List<UserMentorShip> userMentorShip) {
        return userMentorShip.stream()
                .map(UserMentorshipMapper::toUser)
                .toList();
    }

    public static UserMentorShip toUserMentorship(final User user) {
        return UserMentorShip.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .positionId(user.getPositionId())
                .personalEmail(user.getPersonalEmail())
                .companyEmail(user.getCompanyEmail())
                .photo(user.getPhoto())
                .dateOfBirth(user.getDateOfBirth())
                .contractType(user.getContractType())
                .phoneNumber(user.getPhoneNumber())
                .qrCode(user.getQrCode())
                .coachStatus(user.getCoachStatus())
                .mentorStatus(user.getMentorStatus())
                .menteeStatus(user.getMenteeStatus())
                .build();
    }

    public static List<UserMentorShip> toUserMentorshipDTOs(final List<User> users) {
        return users.stream()
                .map(UserMentorshipMapper::toUserMentorship)
                .toList();
    }
}
