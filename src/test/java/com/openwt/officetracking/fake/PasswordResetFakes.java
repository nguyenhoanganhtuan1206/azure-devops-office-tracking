package com.openwt.officetracking.fake;

import com.openwt.officetracking.api.user.PasswordResetDTO;
import com.openwt.officetracking.domain.user.PasswordReset;
import lombok.experimental.UtilityClass;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@UtilityClass
public class PasswordResetFakes {

    public static PasswordResetDTO buildPasswordResetDTO() {
        return PasswordResetDTO.builder()
                .code(randomAlphabetic(3, 10))
                .newPassword("newPassword1!")
                .build();
    }

    public static PasswordReset buildPasswordReset() {
        return PasswordReset.builder()
                .code(randomAlphabetic(3, 10))
                .newPassword("newPassword1!")
                .build();
    }
}
