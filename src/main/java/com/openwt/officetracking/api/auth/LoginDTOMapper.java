package com.openwt.officetracking.api.auth;

import lombok.experimental.UtilityClass;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@UtilityClass
public class LoginDTOMapper {

    public static Authentication toAuthentication(final LoginDTO loginDTO) {
        return new UsernamePasswordAuthenticationToken(
                loginDTO.getEmail(),
                loginDTO.getPassword()
        );
    }
}
