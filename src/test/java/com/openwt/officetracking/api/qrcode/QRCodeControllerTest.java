package com.openwt.officetracking.api.qrcode;

import com.openwt.officetracking.api.AbstractControllerTest;
import com.openwt.officetracking.api.WithMockAdmin;
import com.openwt.officetracking.domain.auth.AuthsProvider;
import com.openwt.officetracking.domain.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.openwt.officetracking.fake.UserFakes.buildUser;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(QRCodeController.class)
@ExtendWith(SpringExtension.class)
class QRCodeControllerTest extends AbstractControllerTest {

    private static final String BASE_URL = "/api/v1/qrcode";

    @MockBean
    private AuthsProvider authsProvider;

    @MockBean
    private UserService userService;

    @BeforeEach
    void init() {
        when(authsProvider.getCurrentAuthentication()).thenCallRealMethod();
    }

    @Test
    @WithMockAdmin
    public void shouldGetUser_OK() throws Exception {
        final var qrCode = randomAlphabetic(6);
        final var user = buildUser()
                .withQrCode(qrCode);

        when(userService.getUserByQRCode(qrCode)).thenReturn(user);

        get(BASE_URL + "?q=" + qrCode)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId().toString()))
                .andExpect(jsonPath("$.personalEmail").value(user.getPersonalEmail()))
                .andExpect(jsonPath("$.companyEmail").value(user.getCompanyEmail()))
                .andExpect(jsonPath("$.identifier").value(user.getIdentifier()))
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.role").value(user.getRole().toString()))
                .andExpect(jsonPath("$.photo").value(user.getPhoto()))
                .andExpect(jsonPath("$.gender").value(user.getGender().toString()))
                .andExpect(jsonPath("$.positionId").value(user.getPositionId().toString()))
                .andExpect(jsonPath("$.dateOfBirth").value(user.getDateOfBirth().toString()))
                .andExpect(jsonPath("$.address").value(user.getAddress()))
                .andExpect(jsonPath("$.university").value(user.getUniversity()))
                .andExpect(jsonPath("$.contractType").value(user.getContractType().toString()))
                .andExpect(jsonPath("$.phoneNumber").value(user.getPhoneNumber()))
                .andExpect(jsonPath("$.qrCode").value(user.getQrCode()))
                .andExpect(jsonPath("$.active").value(user.isActive()))
                .andExpect(jsonPath("$.startDate").value(user.getStartDate().toString()))
                .andExpect(jsonPath("$.endDate").value(user.getEndDate().toString()))
                .andExpect(jsonPath("$.photo").value(user.getPhoto()))
                .andExpect(jsonPath("$.contractType").value(user.getContractType().toString()));

        verify(userService).getUserByQRCode(qrCode);
    }
}
