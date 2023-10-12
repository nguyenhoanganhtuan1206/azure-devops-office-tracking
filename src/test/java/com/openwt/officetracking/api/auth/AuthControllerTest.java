package com.openwt.officetracking.api.auth;

import com.openwt.officetracking.api.AbstractControllerTest;
import com.openwt.officetracking.domain.auth.*;
import com.openwt.officetracking.api.WithMockAdmin;
import com.openwt.officetracking.domain.auth.AuthService;
import com.openwt.officetracking.domain.auth.JwtTokenService;
import com.openwt.officetracking.domain.auth.JwtUserDetails;
import com.openwt.officetracking.domain.user.UserService;
import com.openwt.officetracking.error.BadRequestException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import static com.openwt.officetracking.api.auth.AuthUserResponseDTOMapper.toUserLoginDTO;
import static com.openwt.officetracking.fake.AuthFakes.buildAuth;
import static com.openwt.officetracking.fake.JwtUserDetailsFakes.buildUserDetails;
import static com.openwt.officetracking.fake.RegisterMobileFakes.buildRegisterMobileDTO;
import static com.openwt.officetracking.fake.UserFakes.buildUser;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest extends AbstractControllerTest {

    private static final String BASE_URL = "/api/v1/auths";

    @MockBean
    private JwtTokenService jwtTokenService;

    @MockBean
    private AuthService authService;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Test
    void shouldGetCurrentUser_OK() throws Exception {
        final var user = buildUser();

        when(userService.findByCurrentUserId())
                .thenReturn(user);

        final var actual = toUserLoginDTO(user);

        get(BASE_URL)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(actual.getId().toString()))
                .andExpect(jsonPath("$.personalEmail").value(actual.getPersonalEmail()))
                .andExpect(jsonPath("$.companyEmail").value(actual.getCompanyEmail()))
                .andExpect(jsonPath("$.identifier").value(actual.getIdentifier()))
                .andExpect(jsonPath("$.firstName").value(actual.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(actual.getLastName()))
                .andExpect(jsonPath("$.role").value(actual.getRole().toString()))
                .andExpect(jsonPath("$.photo").value(actual.getPhoto()))
                .andExpect(jsonPath("$.gender").value(actual.getGender().toString()))
                .andExpect(jsonPath("$.positionId").value(actual.getPositionId().toString()))
                .andExpect(jsonPath("$.dateOfBirth").value(actual.getDateOfBirth().toString()))
                .andExpect(jsonPath("$.address").value(actual.getAddress()))
                .andExpect(jsonPath("$.university").value(actual.getUniversity()))
                .andExpect(jsonPath("$.contractType").value(actual.getContractType().toString()))
                .andExpect(jsonPath("$.phoneNumber").value(actual.getPhoneNumber()))
                .andExpect(jsonPath("$.qrCode").value(actual.getQrCode()))
                .andExpect(jsonPath("$.active").value(actual.isActive()))
                .andExpect(jsonPath("$.startDate").value(actual.getStartDate().toString()))
                .andExpect(jsonPath("$.endDate").value(actual.getEndDate().toString()))
                .andExpect(jsonPath("$.photo").value(actual.getPhoto()))
                .andExpect(jsonPath("$.contractType").value(actual.getContractType().toString()));

        verify(userService).findByCurrentUserId();
    }

    @Test
    void shouldLoginWithEmailAndPassword_OK() throws Exception {
        final var auth = buildAuth();
        final var userDetails = buildUserDetails();
        final var token = randomAlphabetic(3, 10);

        doNothing().when(userService).safeVerifyPasswordForLogin(any(String.class), any(String.class));
        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenReturn(auth);
        when(jwtTokenService.generateAccessToken(any(JwtUserDetails.class)))
                .thenReturn(token);
        when(jwtTokenService.generateRefreshToken(any(JwtUserDetails.class)))
                .thenReturn(token);
        when(authService.login(any(LoginDTO.class)))
                .thenReturn(userDetails);

        post(BASE_URL + "/login", auth)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(token))
                .andExpect(jsonPath("$.refreshToken").value(token));

        verify(authService).login(any(LoginDTO.class));
        verify(authenticationManager, times(0)).authenticate(any(Authentication.class));
        verify(jwtTokenService).generateAccessToken(any(JwtUserDetails.class));
        verify(jwtTokenService).generateRefreshToken(any(JwtUserDetails.class));
    }

    @Test
    void shouldLoginWithEmailAndPassword_ThroughBadRequestException() throws Exception {
        final var auth = buildAuth();

        when(authenticationManager.authenticate(auth))
                .thenThrow(BadRequestException.class);
        doNothing().when(userService).safeVerifyPasswordForLogin(any(String.class), any(String.class));

        post(BASE_URL + "/login", null)
                .andExpect(status().isBadRequest());

        verify(authenticationManager, times(0)).authenticate(any(Authentication.class));
    }

    @Test
    void shouldLoginWithGoogle_Ok() throws Exception {
        final var tokenRequest = new TokenRequestDTO(randomAlphabetic(3, 10));
        final var token = randomAlphabetic(3, 10);
        final var userDetails = buildUserDetails();

        when(authService.login(tokenRequest.getIdToken())).thenReturn(userDetails);
        when(jwtTokenService.generateAccessToken(userDetails))
                .thenReturn(token);
        when(jwtTokenService.generateRefreshToken(userDetails))
                .thenReturn(token);

        post(BASE_URL + "/google", tokenRequest)
                .andExpect(jsonPath("$.accessToken").value(token));

        verify(authService).login(tokenRequest.getIdToken());
        verify(jwtTokenService).generateAccessToken(userDetails);
        verify(jwtTokenService).generateRefreshToken(userDetails);
    }

    @Test
    @WithMockAdmin
    void shouldRefreshToken_OK() throws Exception {
        final var userDetails = buildUserDetails();
        final var token = randomAlphabetic(3, 10);

        when(jwtTokenService.generateAccessToken(any(JwtUserDetails.class)))
                .thenReturn(token);
        when(jwtTokenService.generateRefreshToken(any(JwtUserDetails.class)))
                .thenReturn(token);
        when(authService.refreshToken())
                .thenReturn(userDetails);

        post(BASE_URL + "/refresh-token", null)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(token));

        verify(authService).refreshToken();
        verify(jwtTokenService).generateAccessToken(any(JwtUserDetails.class));
        verify(jwtTokenService).generateRefreshToken(any(JwtUserDetails.class));
    }

    @Test
    void shouldRegisterMobile_OK() throws Exception {
        final var requestMobile = buildRegisterMobileDTO();
        final var token = randomAlphabetic(3, 10);
        final var userDetails = buildUserDetails();

        when(authService.registerMobile(any(RegisterMobile.class))).thenReturn(userDetails);
        when(jwtTokenService.generateAccessToken(any(JwtUserDetails.class)))
                .thenReturn(token);
        when(jwtTokenService.generateRefreshToken(any(JwtUserDetails.class)))
                .thenReturn(token);

        post(BASE_URL + "/register-mobile", requestMobile)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(token))
                .andExpect(jsonPath("$.refreshToken").value(token));

        verify(authService).registerMobile(any(RegisterMobile.class));
        verify(jwtTokenService).generateAccessToken(any(JwtUserDetails.class));
        verify(jwtTokenService).generateRefreshToken(any(JwtUserDetails.class));
    }
}
