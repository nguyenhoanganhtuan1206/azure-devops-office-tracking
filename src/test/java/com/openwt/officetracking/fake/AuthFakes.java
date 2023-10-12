package com.openwt.officetracking.fake;

import com.openwt.officetracking.domain.auth.JwtUserDetails;
import lombok.experimental.UtilityClass;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Collections;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@UtilityClass
public class AuthFakes {

    public static Authentication buildAuth() {
        final var userDetails = new JwtUserDetails(
                randomUUID(),
                randomAlphabetic(3, 10),
                randomAlphabetic(3, 10),
                Collections.emptyList()
        );
        return new UsernamePasswordAuthenticationToken(userDetails,
                userDetails.getPassword(),
                userDetails.getAuthorities());
    }
}
