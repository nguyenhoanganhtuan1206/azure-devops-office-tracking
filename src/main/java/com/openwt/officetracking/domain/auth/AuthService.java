package com.openwt.officetracking.domain.auth;

import com.openwt.officetracking.api.auth.LoginDTO;
import com.openwt.officetracking.domain.user.User;
import com.openwt.officetracking.domain.user.UserService;
import com.openwt.officetracking.domain.user_mobile.UserMobile;
import com.openwt.officetracking.domain.user_mobile.UserMobileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.openwt.officetracking.api.auth.LoginDTOMapper.toAuthentication;
import static com.openwt.officetracking.domain.auth.RegisterMobileValidation.validateUserRegisterMobileEmail;
import static com.openwt.officetracking.error.CommonError.supplyBadRequestError;
import static java.time.Instant.now;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final GoogleTokenVerifierService googleTokenVerifierService;

    private final JwtUserDetailsService jwtUserDetailsService;

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final AuthsProvider authsProvider;

    private final UserMobileService userMobileService;

    public UserDetails login(final String decodedToken) {
        final User userAccount = googleTokenVerifierService.verifyGoogleIdToken(decodedToken);

        return jwtUserDetailsService.loadUserByUsername(userAccount.getPersonalEmail());
    }

    public UserDetails login(final LoginDTO loginDTO) {
        userService.safeVerifyPasswordForLogin(loginDTO.getEmail(), loginDTO.getPassword());

        return (UserDetails) getAuthenticate(loginDTO).getPrincipal();
    }

    public UserDetails refreshToken() {
        final UUID userId = authsProvider.getCurrentUserId();
        final User loginUser = userService.findById(userId);

        return jwtUserDetailsService.loadUserByUsername(loginUser.getCompanyEmail());
    }

    public UserDetails registerMobile(final RegisterMobile registerMobile) {
        validateUserRegisterMobileEmail(registerMobile.getEmail());

        final User registerUser = userService.findByCompanyEmail(registerMobile.getEmail());

        if (!registerUser.isActive()) {
            throw supplyBadRequestError("This account cannot be registered because it is in a deactivated status").get();
        }

        updateUserMobileProperties(registerMobile, registerUser);

        return jwtUserDetailsService.loadUserByUsername(registerUser.getCompanyEmail());
    }

    private void updateUserMobileProperties(final RegisterMobile registerMobile, final User user) {
        final UserMobile updateMobile = UserMobile.builder()
                .userId(user.getId())
                .biometryToken(registerMobile.getBiometryToken())
                .deviceType(registerMobile.getDeviceType())
                .model(registerMobile.getModel())
                .osVersion(registerMobile.getOsVersion())
                .fcmToken(registerMobile.getFcmToken())
                .isActive(true)
                .registeredAt(now())
                .build();

        userMobileService.registerUserMobile(updateMobile);
    }

    private Authentication getAuthenticate(final LoginDTO loginDTO) {
        return authenticationManager.authenticate(toAuthentication(loginDTO));
    }
}
