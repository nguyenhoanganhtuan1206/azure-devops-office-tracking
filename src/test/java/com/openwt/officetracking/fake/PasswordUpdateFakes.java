package com.openwt.officetracking.fake;

import com.openwt.officetracking.api.user.PasswordUpdateDTO;
import com.openwt.officetracking.domain.user.PasswordUpdate;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PasswordUpdateFakes {

    public static PasswordUpdate buildPasswordUpdate() {
        return PasswordUpdate.builder()
                .currentPassword("currentPassword1!")
                .newPassword("newPassword1!")
                .build();
    }

    public static PasswordUpdateDTO buildPasswordUpdateDTO() {
        return PasswordUpdateDTO.builder()
                .currentPassword("currentPassword1!")
                .newPassword("newPassword1!")
                .build();
    }
}
