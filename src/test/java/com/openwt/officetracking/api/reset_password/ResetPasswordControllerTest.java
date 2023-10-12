package com.openwt.officetracking.api.reset_password;

import com.openwt.officetracking.api.AbstractControllerTest;
import com.openwt.officetracking.api.WithMockAdmin;
import com.openwt.officetracking.api.auth.AuthController;
import com.openwt.officetracking.domain.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.openwt.officetracking.fake.ForgotPasswordRequestFakes.buildForgotPasswordRequestDTO;
import static com.openwt.officetracking.fake.PasswordResetFakes.buildPasswordResetDTO;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ResetPasswordController.class)
@AutoConfigureMockMvc(addFilters = false)
class ResetPasswordControllerTest extends AbstractControllerTest {

    private static final String BASE_URL = "/api/v1/reset-password";

    @MockBean
    private UserService userService;

    @Test
    void shouldSendForgotPasswordEmail_OK() throws Exception {
        final var requestEmail = buildForgotPasswordRequestDTO();

        doNothing().when(userService).sendForgotPasswordEmail(requestEmail.getEmail());

        post(BASE_URL , requestEmail)
                .andExpect(status().isOk());

        verify(userService).sendForgotPasswordEmail(requestEmail.getEmail());
    }

    @Test
    void shouldVerifyResetPasswordCode_OK() throws Exception {
        final var code = randomAlphabetic(3, 10);

        doNothing().when(userService).verifyCodeExpiration(code);

        get(BASE_URL + "/?code=" + code)
                .andExpect(status().isOk());

        verify(userService).verifyCodeExpiration(code);
    }

    @Test
    @WithMockAdmin
    void shouldResetPassword_OK() throws Exception {
        final var passwordRequest = buildPasswordResetDTO();

        put(BASE_URL, passwordRequest)
                .andExpect(status().isOk());

        verify(userService).resetPassword(argThat(passwordArg ->
                StringUtils.equals(passwordArg.getNewPassword(), passwordRequest.getNewPassword())));
    }
}