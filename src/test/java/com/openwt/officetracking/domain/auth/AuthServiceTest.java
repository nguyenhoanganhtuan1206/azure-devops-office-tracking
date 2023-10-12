package com.openwt.officetracking.domain.auth;

import com.openwt.officetracking.domain.user.UserService;
import com.openwt.officetracking.domain.user_mobile.UserMobileService;
import com.openwt.officetracking.error.AccessDeniedException;
import com.openwt.officetracking.error.BadRequestException;
import com.openwt.officetracking.error.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;

import java.time.Instant;

import static com.openwt.officetracking.api.auth.LoginDTOMapper.toAuthentication;
import static com.openwt.officetracking.fake.AuthFakes.buildAuth;
import static com.openwt.officetracking.fake.JwtUserDetailsFakes.buildUserDetails;
import static com.openwt.officetracking.fake.LoginFakes.buildLoginDTO;
import static com.openwt.officetracking.fake.RegisterMobileFakes.buildRegisterMobile;
import static com.openwt.officetracking.fake.UserFakes.buildUser;
import static com.openwt.officetracking.fake.UserMobileFakes.buildUserMobile;
import static java.time.Duration.ofMinutes;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private GoogleTokenVerifierService googleTokenVerifierService;

    @Mock
    private JwtUserDetailsService jwtUserDetailsService;

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private AuthsProvider authsProvider;

    @Mock
    private UserMobileService userMobileService;

    @Test
    public void shouldLogin_OK() {
        final var token = "test-token";
        final var user = buildUser();
        final var expected = buildUserDetails();

        when(googleTokenVerifierService.verifyGoogleIdToken(token)).thenReturn(user);
        when(jwtUserDetailsService.loadUserByUsername(user.getPersonalEmail())).thenReturn(expected);

        final var actual = authService.login(token);

        assertEquals(expected, actual);

        verify(googleTokenVerifierService).verifyGoogleIdToken(token);
        verify(jwtUserDetailsService).loadUserByUsername(user.getPersonalEmail());
    }

    @Test
    public void shouldLogin_WithInvalidEmail() {
        final var token = "test-token";
        final var user = buildUser();

        when(googleTokenVerifierService.verifyGoogleIdToken(token)).thenReturn(user);
        when(jwtUserDetailsService.loadUserByUsername(user.getPersonalEmail())).thenThrow(new NotFoundException(user.getPersonalEmail()));

        assertThrows(NotFoundException.class, () -> authService.login(token));

        verify(googleTokenVerifierService).verifyGoogleIdToken(token);
    }

    @Test
    public void shouldLogin_WithRoleUser() {
        final var token = "test-token";
        final var user = buildUser();

        when(googleTokenVerifierService.verifyGoogleIdToken(token)).thenReturn(user);
        when(jwtUserDetailsService.loadUserByUsername(user.getPersonalEmail()))
                .thenThrow(new AccessDeniedException("User has invalid role"));

        assertThrows(AccessDeniedException.class, () -> authService.login(token));

        verify(googleTokenVerifierService).verifyGoogleIdToken(token);
    }

    @Test
    void shouldLoginWithEmailAndPassword_OK() {
        final var auth = buildAuth();
        final var user = buildUser()
                .withActive(true)
                .withLastPasswordFailedAt(Instant.now().minus(ofMinutes(10)));
        final var loginDTO = buildLoginDTO()
                .withEmail(user.getCompanyEmail())
                .withPassword(user.getPassword());

        doNothing().when(userService)
                .safeVerifyPasswordForLogin(loginDTO.getEmail(), loginDTO.getPassword());
        when(authenticationManager.authenticate(toAuthentication(loginDTO)))
                .thenReturn(auth);

        authService.login(loginDTO);

        verify(userService).safeVerifyPasswordForLogin(user.getCompanyEmail(), user.getPassword());
        verify(authenticationManager).authenticate(toAuthentication(loginDTO));
    }

    @Test
    void shouldRefreshToken_OK() {
        final var userDetails = buildUserDetails();
        final var user = buildUser()
                .withCompanyEmail(userDetails.getUsername())
                .withActive(true)
                .withLastPasswordFailedAt(Instant.now().minus(ofMinutes(10)));

        when(authsProvider.getCurrentUserId()).thenReturn(user.getId());
        when(userService.findById(user.getId())).thenReturn(user);
        when(jwtUserDetailsService.loadUserByUsername(user.getCompanyEmail())).thenReturn(userDetails);

        authService.refreshToken();

        verify(authsProvider).getCurrentUserId();
        verify(userService).findById(user.getId());
        verify(jwtUserDetailsService).loadUserByUsername(user.getCompanyEmail());
    }

    @Test
    void shouldRegisterMobile_OK() {
        final var userDetails = buildUserDetails();
        final var user = buildUser()
                .withCompanyEmail(userDetails.getUsername())
                .withActive(true)
                .withLastPasswordFailedAt(Instant.now().minus(ofMinutes(10)));
        final var registerMobile = buildRegisterMobile();
        final var userMobile = buildUserMobile()
                .withUserId(user.getId());

        when(userService.findByCompanyEmail(registerMobile.getEmail())).thenReturn(user);
        when(userMobileService.registerUserMobile(argThat(mobileArg -> mobileArg.getUserId() == userMobile.getUserId()))).thenReturn(userMobile);
        when(jwtUserDetailsService.loadUserByUsername(user.getCompanyEmail())).thenReturn(userDetails);

        authService.registerMobile(registerMobile);

        verify(userService).findByCompanyEmail(registerMobile.getEmail());
        verify(userMobileService).registerUserMobile(argThat(mobileArg -> mobileArg.getUserId() == userMobile.getUserId()));
        verify(jwtUserDetailsService).loadUserByUsername(user.getCompanyEmail());
    }

    @Test
    void shouldRegisterMobile_UserDeactivate() {
        final var userDetails = buildUserDetails();
        final var user = buildUser()
                .withCompanyEmail(userDetails.getUsername())
                .withActive(false)
                .withLastPasswordFailedAt(Instant.now().minus(ofMinutes(10)));
        final var registerMobile = buildRegisterMobile();

        when(userService.findByCompanyEmail(registerMobile.getEmail())).thenReturn(user);

        assertThrows(BadRequestException.class, () -> authService.registerMobile(registerMobile));

        verify(userService).findByCompanyEmail(registerMobile.getEmail());
    }
}


