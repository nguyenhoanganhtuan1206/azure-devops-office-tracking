package com.openwt.officetracking.api.user;

import com.openwt.officetracking.api.AbstractControllerTest;
import com.openwt.officetracking.api.WithMockAdmin;
import com.openwt.officetracking.domain.auth.AuthsProvider;
import com.openwt.officetracking.domain.role.Role;
import com.openwt.officetracking.domain.user.User;
import com.openwt.officetracking.domain.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.openwt.officetracking.fake.PasswordUpdateFakes.buildPasswordUpdateDTO;
import static com.openwt.officetracking.fake.UserFakes.buildUser;
import static com.openwt.officetracking.fake.UserFakes.buildUsers;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@ExtendWith(SpringExtension.class)
class UserControllerTest extends AbstractControllerTest {

    private static final String BASE_URL = "/api/v1/users";

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
    void shouldGenerateUserBarcode_OK() throws Exception {
        final var user = buildUser();
        final var image = "abc".getBytes();

        when(userService.generateQRCode(user.getId())).thenReturn(image);

        get(BASE_URL + "/" + user.getId() + "/qrcode")
                .andExpect(status().isOk())
                .andExpect(content().bytes(image));

        verify(userService).generateQRCode(user.getId());
    }

    @Test
    @WithMockAdmin
    void shouldFindById_OK() throws Exception {
        final var user = buildUser();

        when(userService.findById(user.getId())).thenReturn(user);

        get(BASE_URL + "/" + user.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId().toString()))
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.identifier").value(user.getIdentifier()))
                .andExpect(jsonPath("$.personalEmail").value(user.getPersonalEmail()))
                .andExpect(jsonPath("$.companyEmail").value(user.getCompanyEmail()))
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
                .andExpect(jsonPath("$.endDate").value(user.getEndDate().toString()));

        verify(userService).findById(user.getId());
    }

    @Test
    @WithMockAdmin
    void shouldFindAll_OK() throws Exception {
        final var users = buildUsers();

        when(userService.findAll()).thenReturn(users);

        get(BASE_URL)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(users.size()))
                .andExpect(jsonPath("$[0].id").value(users.get(0).getId().toString()))
                .andExpect(jsonPath("$[0].firstName").value(users.get(0).getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(users.get(0).getLastName()))
                .andExpect(jsonPath("$[0].identifier").value(users.get(0).getIdentifier()))
                .andExpect(jsonPath("$[0].personalEmail").value(users.get(0).getPersonalEmail()))
                .andExpect(jsonPath("$[0].companyEmail").value(users.get(0).getCompanyEmail()))
                .andExpect(jsonPath("$[0].role").value(users.get(0).getRole().toString()))
                .andExpect(jsonPath("$[0].photo").value(users.get(0).getPhoto()))
                .andExpect(jsonPath("$[0].gender").value(users.get(0).getGender().toString()))
                .andExpect(jsonPath("$[0].contractType").value(users.get(0).getContractType().toString()))
                .andExpect(jsonPath("$[0].positionId").value(users.get(0).getPositionId().toString()))
                .andExpect(jsonPath("$[0].dateOfBirth").value(users.get(0).getDateOfBirth().toString()))
                .andExpect(jsonPath("$[0].address").value(users.get(0).getAddress()))
                .andExpect(jsonPath("$[0].university").value(users.get(0).getUniversity()))
                .andExpect(jsonPath("$[0].contractType").value(users.get(0).getContractType().toString()))
                .andExpect(jsonPath("$[0].phoneNumber").value(users.get(0).getPhoneNumber()))
                .andExpect(jsonPath("$[0].qrCode").value(users.get(0).getQrCode()))
                .andExpect(jsonPath("$[0].active").value(users.get(0).isActive()))
                .andExpect(jsonPath("$[0].startDate").value(users.get(0).getStartDate().toString()))
                .andExpect(jsonPath("$[0].endDate").value(users.get(0).getEndDate().toString()));

        verify(userService).findAll();
    }

    @Test
    @WithMockAdmin
    void shouldFindByName_OK() throws Exception {
        final var user = buildUser();
        final var users = buildUsers();

        when(userService.findByName(any(String.class))).thenReturn(users);

        get(BASE_URL + "/search?searchTerm=" + user.getFirstName())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(users.size()))
                .andExpect(jsonPath("$[0].id").value(users.get(0).getId().toString()))
                .andExpect(jsonPath("$[0].firstName").value(users.get(0).getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(users.get(0).getLastName()))
                .andExpect(jsonPath("$[0].identifier").value(users.get(0).getIdentifier()))
                .andExpect(jsonPath("$[0].personalEmail").value(users.get(0).getPersonalEmail()))
                .andExpect(jsonPath("$[0].companyEmail").value(users.get(0).getCompanyEmail()))
                .andExpect(jsonPath("$[0].role").value(users.get(0).getRole().toString()))
                .andExpect(jsonPath("$[0].photo").value(users.get(0).getPhoto()))
                .andExpect(jsonPath("$[0].gender").value(users.get(0).getGender().toString()))
                .andExpect(jsonPath("$[0].contractType").value(users.get(0).getContractType().toString()))
                .andExpect(jsonPath("$[0].positionId").value(users.get(0).getPositionId().toString()))
                .andExpect(jsonPath("$[0].dateOfBirth").value(users.get(0).getDateOfBirth().toString()))
                .andExpect(jsonPath("$[0].address").value(users.get(0).getAddress()))
                .andExpect(jsonPath("$[0].university").value(users.get(0).getUniversity()))
                .andExpect(jsonPath("$[0].contractType").value(users.get(0).getContractType().toString()))
                .andExpect(jsonPath("$[0].phoneNumber").value(users.get(0).getPhoneNumber()))
                .andExpect(jsonPath("$[0].qrCode").value(users.get(0).getQrCode()))
                .andExpect(jsonPath("$[0].active").value(users.get(0).isActive()))
                .andExpect(jsonPath("$[0].startDate").value(users.get(0).getStartDate().toString()))
                .andExpect(jsonPath("$[0].endDate").value(users.get(0).getEndDate().toString()));

        verify(userService).findByName(any(String.class));
    }

    @Test
    @WithMockAdmin
    void shouldCreateWithRoleAdmin_WithUserRequestRoleUser_OK() throws Exception {
        final var user = buildUser().withRole(Role.USER);

        when(userService.create(argThat(userArg -> StringUtils.equals(userArg.getPersonalEmail(), user.getPersonalEmail()))))
                .thenReturn(user);

        post(BASE_URL, user)
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
                .andExpect(jsonPath("$.contractType").value(user.getContractType().toString()))
                .andExpect(jsonPath("$.positionId").value(user.getPositionId().toString()))
                .andExpect(jsonPath("$.dateOfBirth").value(user.getDateOfBirth().toString()))
                .andExpect(jsonPath("$.address").value(user.getAddress()))
                .andExpect(jsonPath("$.university").value(user.getUniversity()))
                .andExpect(jsonPath("$.contractType").value(user.getContractType().toString()))
                .andExpect(jsonPath("$.phoneNumber").value(user.getPhoneNumber()))
                .andExpect(jsonPath("$.qrCode").value(user.getQrCode()))
                .andExpect(jsonPath("$.active").value(user.isActive()))
                .andExpect(jsonPath("$.startDate").value(user.getStartDate().toString()))
                .andExpect(jsonPath("$.endDate").value(user.getEndDate().toString()));

        verify(userService).create(argThat(b -> StringUtils.equals(b.getPersonalEmail(), user.getPersonalEmail())));
    }

    @Test
    @WithMockAdmin
    void shouldUpdateWithRoleAdmin_UserRequestRoleUser_BecomeAdmin_OK() throws Exception {
        final var userToUpdate = buildUser().withRole(Role.USER);
        final var userUpdate = buildUser()
                .withId(userToUpdate.getId())
                .withRole(Role.ADMIN);

        when(userService.update(eq(userToUpdate.getId()), any(User.class))).thenReturn(userUpdate);

        put(BASE_URL + "/" + userToUpdate.getId(), userUpdate)
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

        verify(userService).update(eq(userToUpdate.getId()), any(User.class));
    }

    @Test
    @WithMockAdmin
    void shouldUpdatePassword_OK() throws Exception {
        final var passwordUpdate = buildPasswordUpdateDTO();

        put(BASE_URL + "/password", passwordUpdate)
                .andExpect(status().isOk());

        verify(userService).updatePassword(argThat(passwordArg ->
                StringUtils.equals(passwordArg.getCurrentPassword(), passwordUpdate.getCurrentPassword()) &&
                        StringUtils.equals(passwordArg.getNewPassword(), passwordUpdate.getNewPassword())
        ));
    }

    @Test
    @WithMockAdmin
    void shouldDeleteWithRoleAdmin_UserRequestRoleUser_OK() throws Exception {
        final var userDelete = buildUser().withRole(Role.USER);

        put(BASE_URL + "/" + userDelete.getId() + "/deactivate", null)
                .andExpect(status().isOk());

        verify(userService).deactivateById(userDelete.getId());
    }

    @Test
    @WithMockAdmin
    void shouldActiveWithRoleAdmin_UserRequestRoleUser_OK() throws Exception {
        final var userDelete = buildUser().withRole(Role.USER);

        put(BASE_URL + "/" + userDelete.getId() + "/activate", null)
                .andExpect(status().isOk());

        verify(userService).activateById(userDelete.getId());
    }
}
