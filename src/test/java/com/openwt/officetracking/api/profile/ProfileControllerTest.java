package com.openwt.officetracking.api.profile;

import com.openwt.officetracking.api.AbstractControllerTest;
import com.openwt.officetracking.api.WithMockUser;
import com.openwt.officetracking.domain.auth.AuthsProvider;
import com.openwt.officetracking.domain.role.Role;
import com.openwt.officetracking.domain.user.User;
import com.openwt.officetracking.domain.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.openwt.officetracking.fake.UserFakes.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProfileController.class)
@ExtendWith(SpringExtension.class)
class ProfileControllerTest extends AbstractControllerTest {

    private static final String BASE_URL = "/api/v1/profile";

    @MockBean
    private UserService userService;

    @MockBean
    private AuthsProvider authsProvider;

    @Test
    @WithMockUser
    void shouldUpdateWithRoleUser_OK() throws Exception {
        final var userToUpdate = buildUser().withRole(Role.USER);
        final var userUpdate = buildUser()
                .withId(userToUpdate.getId());

        when(userService.update(eq(authsProvider.getCurrentUserId()), any(User.class))).thenReturn(userUpdate);

        put(BASE_URL, userUpdate)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userUpdate.getId().toString()))
                .andExpect(jsonPath("$.personalEmail").value(userUpdate.getPersonalEmail()))
                .andExpect(jsonPath("$.companyEmail").value(userUpdate.getCompanyEmail()))
                .andExpect(jsonPath("$.identifier").value(userUpdate.getIdentifier()))
                .andExpect(jsonPath("$.firstName").value(userUpdate.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(userUpdate.getLastName()))
                .andExpect(jsonPath("$.role").value(userUpdate.getRole().toString()))
                .andExpect(jsonPath("$.gender").value(userUpdate.getGender().toString()))
                .andExpect(jsonPath("$.photo").value(userUpdate.getPhoto()))
                .andExpect(jsonPath("$.contractType").value(userUpdate.getContractType().toString()))
                .andExpect(jsonPath("$.positionId").value(userUpdate.getPositionId().toString()))
                .andExpect(jsonPath("$.dateOfBirth").value(userUpdate.getDateOfBirth().toString()))
                .andExpect(jsonPath("$.address").value(userUpdate.getAddress()))
                .andExpect(jsonPath("$.university").value(userUpdate.getUniversity()))
                .andExpect(jsonPath("$.contractType").value(userUpdate.getContractType().toString()))
                .andExpect(jsonPath("$.phoneNumber").value(userUpdate.getPhoneNumber()))
                .andExpect(jsonPath("$.qrCode").value(userUpdate.getQrCode()))
                .andExpect(jsonPath("$.active").value(userUpdate.isActive()))
                .andExpect(jsonPath("$.startDate").value(userUpdate.getStartDate().toString()))
                .andExpect(jsonPath("$.endDate").value(userUpdate.getEndDate().toString()));

        verify(userService).update(eq(authsProvider.getCurrentUserId()), any(User.class));
    }

    @Test
    @WithMockUser
    void shouldUpdateProfileMobile_OK() throws Exception {
        final var userToUpdate = buildUserProfileUpdate();
        final var userUpdate = buildUserProfileUpdate().withCompanyEmail(userToUpdate.getCompanyEmail());

        when(userService.updateProfileMobile(eq(authsProvider.getCurrentUserId()),
                argThat(userArg -> userArg.getCompanyEmail().equals(userToUpdate.getCompanyEmail())))).thenReturn(userUpdate);

        put(BASE_URL + "/mobile", userUpdate)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personalEmail").value(userUpdate.getPersonalEmail()))
                .andExpect(jsonPath("$.companyEmail").value(userUpdate.getCompanyEmail()))
                .andExpect(jsonPath("$.identifier").value(userUpdate.getIdentifier()))
                .andExpect(jsonPath("$.firstName").value(userUpdate.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(userUpdate.getLastName()));

        verify(userService).updateProfileMobile(eq(authsProvider.getCurrentUserId()),
                argThat(userArg -> userArg.getCompanyEmail().equals(userToUpdate.getCompanyEmail())));
    }
}
