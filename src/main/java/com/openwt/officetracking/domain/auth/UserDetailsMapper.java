package com.openwt.officetracking.domain.auth;

import com.openwt.officetracking.domain.user.User;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import static java.util.Collections.singleton;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@UtilityClass
public class UserDetailsMapper {

    public static UserDetails toUserDetails(final User user) {
        if (user == null) {
            return null;
        }

        final String email = isNotBlank(user.getCompanyEmail()) ? user.getCompanyEmail() : user.getPersonalEmail();

        return new JwtUserDetails(
                user.getId(),
                email,
                user.getPassword(),
                singleton(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }
}
