package com.openwt.officetracking.api.user;

import com.openwt.officetracking.domain.user.PasswordReset;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PasswordResetDTOMapper {

    public static PasswordReset toPasswordReset(final PasswordResetDTO passwordResetDTO) {
        return PasswordReset.builder()
                .code(passwordResetDTO.getCode())
                .newPassword(passwordResetDTO.getNewPassword())
                .build();
    }
}
