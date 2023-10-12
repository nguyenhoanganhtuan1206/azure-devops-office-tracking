package com.openwt.officetracking.fake;

import com.openwt.officetracking.domain.auth.JwtUserDetails;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static java.util.Collections.singleton;
import static java.util.UUID.randomUUID;

@UtilityClass
public class JwtUserDetailsFakes {

    public static JwtUserDetails buildUserDetails() {
        return new JwtUserDetails(
                randomUUID(),
                "test@gmail.com",
                "password",
                singleton(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
    }
}
