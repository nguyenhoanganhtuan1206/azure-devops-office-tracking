package com.openwt.officetracking.fake;

import com.openwt.officetracking.api.reset_password.ForgotPasswordRequestDTO;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ForgotPasswordRequestFakes {

    public static ForgotPasswordRequestDTO buildForgotPasswordRequestDTO() {
        return ForgotPasswordRequestDTO.builder()
                .email("test@gmail.com")
                .build();
    }
}
