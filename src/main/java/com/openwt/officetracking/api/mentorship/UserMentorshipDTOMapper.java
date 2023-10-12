package com.openwt.officetracking.api.mentorship;

import com.openwt.officetracking.domain.mentorship.UserMentorShip;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class UserMentorshipDTOMapper {

    public static UserMentorshipResponseDTO toUserMentorshipDTO(final UserMentorShip userMentorShip) {
        return UserMentorshipResponseDTO.builder()
                .id(userMentorShip.getId())
                .firstName(userMentorShip.getFirstName())
                .lastName(userMentorShip.getLastName())
                .personalEmail(userMentorShip.getPersonalEmail())
                .companyEmail(userMentorShip.getCompanyEmail())
                .photo(userMentorShip.getPhoto())
                .dateOfBirth(userMentorShip.getDateOfBirth())
                .contractType(userMentorShip.getContractType())
                .phoneNumber(userMentorShip.getPhoneNumber())
                .qrCode(userMentorShip.getQrCode())
                .positionId(userMentorShip.getPositionId())
                .positionName(userMentorShip.getPositionName())
                .numberOfCourses(userMentorShip.getNumberOfCourses())
                .mentorshipStatus(userMentorShip.getMentorshipStatus())
                .coachStatus(userMentorShip.getCoachStatus())
                .mentorStatus(userMentorShip.getMentorStatus())
                .menteeStatus(userMentorShip.getMenteeStatus())
                .build();
    }

    public static List<UserMentorshipResponseDTO> toUserMentorshipDTOs(final List<UserMentorShip> userMentorShips) {
        return userMentorShips.stream()
                .map(UserMentorshipDTOMapper::toUserMentorshipDTO)
                .toList();
    }
}
