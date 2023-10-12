package com.openwt.officetracking.fake;

import com.openwt.officetracking.domain.auth.UserAuthenticationToken;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static java.util.UUID.randomUUID;

@UtilityClass
public class UserAuthenticationTokenFakes {

    public static UserAuthenticationToken buildAdmin() {
        return buildUser("admin", "ADMIN");
    }

    public static UserAuthenticationToken buildUser() {
        return buildUser("user", "USER");
    }

    public static UserAuthenticationToken buildUser(final String name, final String roleName) {
        return new UserAuthenticationToken(
                randomUUID(),
                name,
                List.of(new SimpleGrantedAuthority(roleName)));
    }
}
