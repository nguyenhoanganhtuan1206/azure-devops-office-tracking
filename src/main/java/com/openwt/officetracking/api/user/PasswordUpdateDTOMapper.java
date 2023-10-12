package com.openwt.officetracking.api.user;

import com.openwt.officetracking.domain.user.PasswordUpdate;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PasswordUpdateDTOMapper {

    public static PasswordUpdate toPasswordUpdate(final PasswordUpdateDTO passwordUpdateDTO) {
        return PasswordUpdate.builder()
                .currentPassword(passwordUpdateDTO.getCurrentPassword())
                .newPassword(passwordUpdateDTO.getNewPassword())
                .build();
    }
}
