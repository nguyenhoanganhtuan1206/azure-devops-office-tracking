package com.openwt.officetracking.domain.auth;

import com.openwt.officetracking.persistent.user.UserStore;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.openwt.officetracking.domain.user.UserError.supplyUserNotFound;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserStore userStore;

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        return userStore.findByCompanyEmail(email)
                .map(UserDetailsMapper::toUserDetails)
                .orElseThrow(supplyUserNotFound("email", email));
    }
}
