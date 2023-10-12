package com.openwt.officetracking.fake;

import com.openwt.officetracking.api.auth.LoginDTO;
import lombok.experimental.UtilityClass;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@UtilityClass
public class LoginFakes {

    public static LoginDTO buildLoginDTO() {
        return LoginDTO.builder()
                .email("example@gmail.com")
                .password(randomAlphabetic(9))
                .build();
    }
}
