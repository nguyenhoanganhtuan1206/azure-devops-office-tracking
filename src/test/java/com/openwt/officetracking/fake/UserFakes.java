package com.openwt.officetracking.fake;

import com.openwt.officetracking.api.user.UserDetailDTO;
import com.openwt.officetracking.domain.contract_type.ContractType;
import com.openwt.officetracking.domain.gender.Gender;
import com.openwt.officetracking.domain.mentorship_status.CoachMentorMenteeStatus;
import com.openwt.officetracking.domain.role.Role;
import com.openwt.officetracking.domain.user.User;
import com.openwt.officetracking.domain.user.UserDetail;
import com.openwt.officetracking.domain.user.UserProfileUpdate;
import com.openwt.officetracking.persistent.user.UserEntity;
import lombok.experimental.UtilityClass;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static com.openwt.officetracking.fake.CommonFakes.buildList;
import static com.openwt.officetracking.fake.CommonFakes.randomInstant;
import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

@UtilityClass
public class UserFakes {

    public static User buildUser() {
        final var startDate = randomInstant();

        return User.builder()
                .id(randomUUID())
                .personalEmail("example@gmail.com")
                .companyEmail("example@openwt.com")
                .password(randomAlphabetic(9))
                .identifier(randomNumeric(9))
                .firstName(randomAlphabetic(3, 10))
                .lastName(randomAlphabetic(3, 10))
                .role(Role.ADMIN)
                .gender(Gender.MALE)
                .positionId(randomUUID())
                .contractType(ContractType.EMPLOYEE)
                .qrCode(randomAlphabetic(6))
                .photo("data:image/png;base64,qetT7ef6DBfb")
                .passwordFailedCount(3)
                .lastPasswordFailedAt(Instant.now())
                .dateOfBirth(Instant.now())
                .address(randomAlphabetic(3, 10))
                .university(randomAlphabetic(3, 10))
                .contractType(ContractType.EMPLOYEE)
                .phoneNumber("0987654321")
                .isActive(true)
                .menteeStatus(CoachMentorMenteeStatus.ACTIVE)
                .mentorStatus(CoachMentorMenteeStatus.ACTIVE)
                .startDate(startDate)
                .endDate(startDate.plus(Duration.ofDays(1)))
                .changeCode(false)
                .lastSendResetPasswordAt(Instant.now())
                .resetPasswordCode("resetCode")
                .build();
    }

    public static List<User> buildUsers() {
        return buildList(UserFakes::buildUser);
    }

    public static UserEntity buildUserEntity() {
        final var startDate = randomInstant();

        return UserEntity.builder()
                .id(randomUUID())
                .personalEmail("example@gmail.com")
                .companyEmail("example@openwt.com")
                .password(randomAlphabetic(9))
                .identifier(randomNumeric(9))
                .firstName(randomAlphabetic(3, 10))
                .lastName(randomAlphabetic(3, 10))
                .positionId(randomUUID())
                .role(Role.ADMIN)
                .qrCode(randomAlphabetic(6))
                .photo(randomAlphabetic(3, 10))
                .gender(Gender.MALE)
                .passwordFailedCount(3)
                .lastPasswordFailedAt(Instant.now())
                .dateOfBirth(Instant.now())
                .address(randomAlphabetic(3, 10))
                .university(randomAlphabetic(3, 10))
                .contractType(ContractType.EMPLOYEE)
                .phoneNumber("0987654321")
                .isActive(true)
                .startDate(startDate)
                .endDate(startDate.plus(Duration.ofDays(1)))
                .lastSendResetPasswordAt(Instant.now())
                .resetPasswordCode("resetCode")
                .build();
    }

    public static List<UserEntity> buildUserEntities() {
        return buildList(UserFakes::buildUserEntity);
    }

    public static UserDetail buildUserDetail() {
        final var startDate = randomInstant();

        return UserDetail.builder()
                .id(randomUUID())
                .personalEmail("example@gmail.com")
                .companyEmail("example@openwt.com")
                .identifier(randomNumeric(9))
                .firstName(randomAlphabetic(3, 10))
                .lastName(randomAlphabetic(3, 10))
                .positionName(randomAlphabetic(3, 10))
                .contractType(ContractType.EMPLOYEE)
                .gender(Gender.MALE)
                .dateOfBirth(Instant.now())
                .phoneNumber("0987654321")
                .startDate(startDate)
                .endDate(startDate.plus(Duration.ofDays(1)))
                .build();
    }

    public static UserDetailDTO buildUserDetailDTO() {
        final var startDate = randomInstant();

        return UserDetailDTO.builder()
                .id(randomUUID())
                .personalEmail("example@gmail.com")
                .companyEmail("example@openwt.com")
                .identifier(randomNumeric(9))
                .firstName(randomAlphabetic(3, 10))
                .lastName(randomAlphabetic(3, 10))
                .positionName(randomAlphabetic(3, 10))
                .contractType(ContractType.EMPLOYEE)
                .gender(Gender.MALE)
                .dateOfBirth(Instant.now())
                .phoneNumber("0987654321")
                .startDate(startDate)
                .endDate(startDate.plus(Duration.ofDays(1)))
                .build();
    }

    public static UserProfileUpdate buildUserProfileUpdate() {
        final var startDate = randomInstant();

        return UserProfileUpdate.builder()
                .id(randomUUID())
                .personalEmail("example@gmail.com")
                .companyEmail("example@openwt.com")
                .identifier(randomNumeric(9))
                .firstName(randomAlphabetic(3, 10))
                .lastName(randomAlphabetic(3, 10))
                .photo(randomAlphabetic(3, 10))
                .address(randomAlphabetic(3, 10))
                .positionName(randomAlphabetic(3, 10))
                .contractType(ContractType.EMPLOYEE)
                .gender(Gender.MALE)
                .dateOfBirth(Instant.now())
                .phoneNumber("0987654321")
                .startDate(startDate)
                .endDate(startDate.plus(Duration.ofDays(1)))
                .build();
    }
}
