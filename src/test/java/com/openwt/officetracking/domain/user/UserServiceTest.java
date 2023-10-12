package com.openwt.officetracking.domain.user;

import com.google.zxing.WriterException;
import com.openwt.officetracking.domain.auth.AuthsProvider;
import com.openwt.officetracking.domain.email.EmailService;
import com.openwt.officetracking.domain.position.PositionService;
import com.openwt.officetracking.domain.role.Role;
import com.openwt.officetracking.domain.tracking_history.TrackingHistory;
import com.openwt.officetracking.domain.user_mobile.UserMobileService;
import com.openwt.officetracking.error.AccessDeniedException;
import com.openwt.officetracking.error.BadRequestException;
import com.openwt.officetracking.error.NotFoundException;
import com.openwt.officetracking.persistent.tracking_history.TrackingHistoryStore;
import com.openwt.officetracking.persistent.user.UserStore;
import com.openwt.officetracking.properties.AppProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;

import static com.openwt.officetracking.fake.ImageFakes.buildImage;
import static com.openwt.officetracking.fake.PasswordResetFakes.buildPasswordReset;
import static com.openwt.officetracking.fake.PasswordUpdateFakes.buildPasswordUpdate;
import static com.openwt.officetracking.fake.PositionFakes.buildPosition;
import static com.openwt.officetracking.fake.TrackingHistoryFakes.buildTrackingHistory;
import static com.openwt.officetracking.fake.UserAuthenticationTokenFakes.buildAdmin;
import static com.openwt.officetracking.fake.UserFakes.*;
import static com.openwt.officetracking.fake.UserMobileFakes.buildUserMobile;
import static java.time.Duration.ofMinutes;
import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserStore userStore;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserService userService;

    @Mock
    private AuthsProvider authsProvider;

    @Mock
    private AppProperties appProperties;

    @Spy
    private PasswordEncoder passwordEncoder;

    @Mock
    private CloudinaryService cloudinaryService;

    @Mock
    private UserMobileService userMobileService;

    @Mock
    private TrackingHistoryStore trackingHistoryStore;

    @Mock
    private PositionService positionService;

    @Test
    void shouldFindAll_OK() {
        final var users = buildUsers();

        when(userStore.findAll()).thenReturn(users);

        final var actual = userService.findAll();

        assertEquals(users.size(), actual.size());
        assertEquals(users.get(0).getId(), actual.get(0).getId());
        assertEquals(users.get(0).getPersonalEmail(), actual.get(0).getPersonalEmail());
        assertEquals(passwordEncoder.encode(users.get(0).getPassword()), passwordEncoder.encode(actual.get(0).getPassword()));
        assertEquals(users.get(0).getFirstName(), actual.get(0).getFirstName());
        assertEquals(users.get(0).getLastName(), actual.get(0).getLastName());
        assertEquals(users.get(0).getIdentifier(), actual.get(0).getIdentifier());
        assertEquals(users.get(0).getPositionId(), actual.get(0).getPositionId());
        assertEquals(users.get(0).getContractType(), actual.get(0).getContractType());
        assertEquals(users.get(0).getRole(), actual.get(0).getRole());
        assertEquals(users.get(0).getPhoto(), actual.get(0).getPhoto());

        verify(userStore).findAll();
    }

    @Test
    void shouldFindById_OK() {
        final var user = buildUser();

        when(userStore.findById(user.getId()))
                .thenReturn(Optional.of(user));

        final var actual = userService.findById(user.getId());

        assertEquals(user, actual);

        verify(userStore).findById(user.getId());
    }

    @Test
    void shouldFindById_ThrownNotFoundException() {
        final var userId = randomUUID();

        when(userStore.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.findById(userId));

        verify(userStore).findById(userId);
    }

    @Test
    void shouldFindByCompanyEmail_OK() {
        final var user = buildUser();

        when(userStore.findByCompanyEmail(user.getCompanyEmail()))
                .thenReturn(Optional.of(user));

        final var actual = userService.findByCompanyEmail(user.getCompanyEmail());

        assertEquals(user, actual);

        verify(userStore).findByCompanyEmail(user.getCompanyEmail());
    }

    @Test
    void shouldFindByCompanyEmail_ThrownNotFoundException() {
        final var email = randomAlphabetic(3, 10);

        when(userStore.findByCompanyEmail(email))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.findByCompanyEmail(email));

        verify(userStore).findByCompanyEmail(email);
    }

    @Test
    void shouldCreateUser_WithCurrentUserRoleAdmin_OK() {
        final var userToCreate = buildUser()
                .withRole(Role.USER);
        final var encodedPassword = randomAlphabetic(6, 10);
        final var userDeviceInformation = buildUserMobile().withUserId(userToCreate.getId());
        final var trackingHistory = buildTrackingHistory();

        when(passwordEncoder.encode(anyString()))
                .thenReturn(encodedPassword);
        when(userStore.save(userToCreate))
                .thenReturn(userToCreate);
        when(trackingHistoryStore.save(any(TrackingHistory.class))).thenReturn(trackingHistory);
        when(userMobileService.create(argThat(userDeviceArg -> userDeviceArg.getUserId() == userDeviceInformation.getUserId())))
                .thenReturn(userDeviceInformation);
        doNothing().when(emailService).sendActivationEmail(argThat(userArg -> userArg.getId() == userToCreate.getId()));

        userToCreate.setPassword(encodedPassword);
        final var actual = userService.create(userToCreate);

        assertEquals(userToCreate, actual);

        verify(passwordEncoder).encode(anyString());
        verify(userStore).save(userToCreate);
        verify(trackingHistoryStore).save(any(TrackingHistory.class));
        verify(userMobileService).create(argThat(userDeviceArg -> userDeviceArg.getUserId() == userDeviceInformation.getUserId()));
        verify(emailService).sendActivationEmail(argThat(userArg -> userArg.getId() == userToCreate.getId()));
    }

    @Test
    void shouldCreateUser_WithInvalidEmail_ThroughBadRequestException() {
        final var userToCreate = buildUser()
                .withPersonalEmail("abc")
                .withRole(Role.USER);

        assertThrows(BadRequestException.class, () -> userService.create(userToCreate));
    }

    @Test
    void shouldUpdateUser_BecomeRoleUser_WithCurrentUserRoleAdmin_OK() {
        final var currentUser = buildUser().withRole(Role.ADMIN);
        final var userToUpdate = buildUser().withRole(Role.USER);
        final var userRequest = buildUser()
                .withRole(Role.USER);

        when(userStore.findById(userToUpdate.getId()))
                .thenReturn(Optional.of(userToUpdate));
        when(userStore.findById(authsProvider.getCurrentUserId()))
                .thenReturn(Optional.of(currentUser));
        when(userStore.save(argThat(user -> user.getRole() == Role.USER)))
                .thenReturn(userRequest);

        final var actual = userService.update(userToUpdate.getId(), userRequest);

        assertEquals(userRequest, actual);

        verify(userStore).findById(userToUpdate.getId());
        verify(userStore).save(argThat(user -> user.getRole() == Role.USER));
    }

    @Test
    void shouldUpdateUser_WithCurrentUserRoleAdmin_ChooseChangeCode_OK() {
        final var currentUser = buildUser()
                .withRole(Role.ADMIN);
        final var userToUpdate = buildUser()
                .withRole(Role.USER);
        final var userRequest = buildUser()
                .withRole(Role.USER)
                .withChangeCode(true);

        when(userStore.findById(userToUpdate.getId()))
                .thenReturn(Optional.of(userToUpdate));
        when(userStore.findById(authsProvider.getCurrentUserId()))
                .thenReturn(Optional.of(currentUser));
        when(userStore.save(argThat(user -> user.getRole() == Role.USER)))
                .thenReturn(userRequest);

        final var actual = userService.update(userToUpdate.getId(), userRequest);

        assertFalse(userToUpdate.isChangeCode());
        assertNotEquals(userToUpdate.getQrCode(), actual.getQrCode());

        verify(userStore).findById(userToUpdate.getId());
        verify(userStore).save(argThat(user -> user.getRole() == Role.USER));
    }

    @Test
    void shouldUpdateUser_HaveDifferentEmail_OK() {
        final var currentUser = buildUser().withRole(Role.ADMIN);
        final var userToUpdate = buildUser().withRole(Role.USER);
        final var userRequest = buildUser()
                .withPersonalEmail("email1@gmail.com")
                .withRole(Role.USER);

        when(userStore.findById(userToUpdate.getId()))
                .thenReturn(Optional.of(userToUpdate));
        when(userStore.findById(authsProvider.getCurrentUserId()))
                .thenReturn(Optional.of(currentUser));
        when(userStore.save(argThat(user -> user.getRole() == Role.USER)))
                .thenReturn(userRequest);

        final var actual = userService.update(userToUpdate.getId(), userRequest);

        assertEquals(userRequest, actual);

        verify(userStore).findById(userToUpdate.getId());
        verify(userStore).save(argThat(user -> user.getRole() == Role.USER));
    }

    @Test
    void shouldUpdateUser_HaveDifferentPersonalEmail_CompanyEmail_OK() {
        final var currentUser = buildUser().withRole(Role.ADMIN);
        final var userToUpdate = buildUser().withRole(Role.USER);
        final var userRequest = buildUser()
                .withPersonalEmail("email1@gmail.com")
                .withCompanyEmail("email01@openwt.com")
                .withRole(Role.USER);

        when(userStore.findById(userToUpdate.getId()))
                .thenReturn(Optional.of(userToUpdate));
        when(authsProvider.getCurrentUserId())
                .thenReturn(buildAdmin().getUserId());
        when(userStore.findById(authsProvider.getCurrentUserId()))
                .thenReturn(Optional.of(currentUser));
        when(userStore.save(argThat(user -> user.getRole() == Role.USER)))
                .thenReturn(userRequest);

        final var actual = userService.update(userToUpdate.getId(), userRequest);

        assertEquals(userRequest, actual);

        verify(userStore).findById(userToUpdate.getId());
        verify(userStore).save(argThat(user -> user.getRole() == Role.USER));
    }

    @Test
    void shouldUpdateUser_CompanyEmailUserRequestExisted_ThroughBadRequestException() {
        final var currentUser = buildUser()
                .withCompanyEmail(randomAlphabetic(9))
                .withRole(Role.USER);
        final var userExisted = buildUser();
        final var userToUpdate = buildUser()
                .withRole(Role.USER)
                .withCompanyEmail(userExisted.getCompanyEmail());
        final var userRequest = buildUser()
                .withCompanyEmail(userExisted.getCompanyEmail())
                .withRole(Role.ADMIN);

        when(userStore.findById(authsProvider.getCurrentUserId()))
                .thenReturn(Optional.of(userRequest));
        when(userStore.findById(currentUser.getId()))
                .thenReturn(Optional.of(currentUser));
        when(userStore.findByCompanyEmail(userToUpdate.getCompanyEmail()))
                .thenReturn(Optional.of(userToUpdate));

        assertThrows(BadRequestException.class, () -> userService.update(currentUser.getId(), userToUpdate));

        verify(userStore).findById(currentUser.getId());
        verify(userStore).findByCompanyEmail(userToUpdate.getCompanyEmail());
    }

    @Test
    void shouldUpdateUser_CurrentUserNotExisting_ThroughNotFoundException() {
        final var currentUserId = randomUUID();
        final var userToCreate = buildUser().withRole(Role.USER);
        final var userRequest = buildUser()
                .withRole(Role.USER);

        when(userStore.findById(userToCreate.getId()))
                .thenReturn(Optional.of(userToCreate));
        when(authsProvider.getCurrentUserId())
                .thenReturn(currentUserId);
        when(userStore.findById(currentUserId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.update(userToCreate.getId(), userRequest));

        verify(userStore).findById(currentUserId);
        verify(userStore).findById(userToCreate.getId());
        verify(authsProvider, times(2)).getCurrentUserId();
    }

    @Test
    void shouldUpdateUser_UserRequestNotExisting_ThroughNotFoundException() {
        final var userIdToCreate = randomUUID();
        final var userRequest = buildUser()
                .withRole(Role.USER);

        when(userStore.findById(userIdToCreate))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.update(userIdToCreate, userRequest));

        verify(userStore).findById(userIdToCreate);
    }

    @Test
    void shouldUpdateUser_EmailUserRequestExisted_ThroughBadRequestException() {
        final var currentUser = buildUser()
                .withPersonalEmail(randomAlphabetic(9))
                .withRole(Role.USER);
        final var userExisted = buildUser();
        final var userToUpdate = buildUser()
                .withRole(Role.USER)
                .withPersonalEmail(userExisted.getPersonalEmail());
        final var userRequest = buildUser()
                .withPersonalEmail(userExisted.getPersonalEmail())
                .withRole(Role.ADMIN);

        when(userStore.findById(authsProvider.getCurrentUserId()))
                .thenReturn(Optional.of(userRequest));
        when(userStore.findById(currentUser.getId()))
                .thenReturn(Optional.of(currentUser));
        when(userStore.findByPersonalEmail(userToUpdate.getPersonalEmail()))
                .thenReturn(Optional.of(userToUpdate));

        assertThrows(BadRequestException.class, () -> userService.update(currentUser.getId(), userToUpdate));

        verify(userStore).findById(currentUser.getId());
        verify(userStore).findByPersonalEmail(userToUpdate.getPersonalEmail());
    }

    @Test
    void shouldUpdateUser_IdentifierUserRequestExisted_ThroughBadRequestException() {
        final var currentUser = buildUser()
                .withRole(Role.ADMIN);
        final var userExisted = buildUser()
                .withRole(Role.USER);
        final var userToUpdate = buildUser()
                .withRole(Role.USER);
        final var userRequest = buildUser()
                .withIdentifier(userExisted.getIdentifier())
                .withRole(Role.ADMIN);

        when(userStore.findByIdentifier(userExisted.getIdentifier()))
                .thenReturn(Optional.of(userExisted));
        when(userStore.findById(userToUpdate.getId()))
                .thenReturn(Optional.of(userToUpdate));
        when(authsProvider.getCurrentUserId())
                .thenReturn(buildAdmin().getUserId());
        when(userStore.findById(authsProvider.getCurrentUserId()))
                .thenReturn(Optional.of(currentUser));

        assertThrows(BadRequestException.class, () -> userService.update(userToUpdate.getId(), userRequest));

        verify(userStore).findById(userToUpdate.getId());
        verify(userStore).findByIdentifier(userExisted.getIdentifier());
    }

    @Test
    void shouldUpdateProfileMobile_OK() {
        final var updateUser = buildUser()
                .withCompanyEmail("test@openwt.com")
                .withPersonalEmail("test@gmail.com");
        final var updateToUser = buildUserProfileUpdate()
                .withCompanyEmail(updateUser.getCompanyEmail())
                .withFirstName(updateUser.getFirstName());
        final var currentUser = buildUser()
                .withFirstName(updateUser.getFirstName());
        final var position = buildPosition().withId(currentUser.getPositionId());

        when(userStore.findById(currentUser.getId())).thenReturn(Optional.of(currentUser));
        when(positionService.findById(currentUser.getPositionId())).thenReturn(position);
        when(userStore.findByPersonalEmail(updateUser.getPersonalEmail())).thenReturn(Optional.empty());
        when(userStore.findByCompanyEmail(updateUser.getCompanyEmail())).thenReturn(Optional.empty());
        when(userStore.save(argThat(userArg -> userArg.getCompanyEmail().equals(currentUser.getCompanyEmail())))).thenReturn(currentUser);

        final var actual = userService.updateProfileMobile(currentUser.getId(), updateUser);

        assertEquals(updateToUser.getCompanyEmail(), actual.getCompanyEmail());
        assertEquals(updateToUser.getFirstName(), actual.getFirstName());

        verify(userStore).findById(currentUser.getId());
        verify(positionService).findById(currentUser.getPositionId());
        verify(userStore).findByPersonalEmail(updateUser.getPersonalEmail());
        verify(userStore).findByCompanyEmail(updateUser.getCompanyEmail());
        verify(userStore).save(argThat(userArg -> userArg.getCompanyEmail().equals(currentUser.getCompanyEmail())));
    }

    @Test
    void shouldUpdateProfileMobile_ThrowValidate() {
        final var updateUser = buildUser().withCompanyEmail("123");

        assertThrows(BadRequestException.class, () -> userService.updateProfileMobile(randomUUID(), updateUser));
    }

    @Test
    void shouldUpdateProfileMobile_ThrowUserNotFound() {
        final var updateUser = buildUser();
        final var currentUser = buildUser();

        when(userStore.findById(currentUser.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.updateProfileMobile(currentUser.getId(), updateUser));

        verify(userStore).findById(currentUser.getId());
    }

    @Test
    void shouldUpdatePassword_OK() {
        final PasswordUpdate passwordRequest = buildPasswordUpdate();
        final User currentUser = buildUser()
                .withPasswordFailedCount(1)
                .withRole(Role.USER)
                .withPassword(passwordRequest.getCurrentPassword());

        final var currentPassword = currentUser.getPassword();

        when(authsProvider.getCurrentUserId())
                .thenReturn(currentUser.getId());
        when(userStore.findById(currentUser.getId()))
                .thenReturn(Optional.of(currentUser));
        when(passwordEncoder.encode(passwordRequest.getNewPassword()))
                .thenReturn(passwordRequest.getNewPassword());
        when(passwordEncoder.matches(passwordRequest.getCurrentPassword(), currentUser.getPassword()))
                .thenReturn(true);

        userService.updatePassword(passwordRequest);

        final ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        verify(userStore, times(2)).save(userCaptor.capture());

        final User savedUser = userCaptor.getValue();
        assertEquals(passwordRequest.getNewPassword(), savedUser.getPassword());
        assertEquals(currentUser.getId(), savedUser.getId());

        verify(authsProvider, times(1)).getCurrentUserId();
        verify(userStore).findById(currentUser.getId());
        verify(passwordEncoder).encode(passwordRequest.getNewPassword());
        verify(passwordEncoder).matches(passwordRequest.getCurrentPassword(), currentPassword);
    }

    static Stream<Arguments> shouldUpdatePasswordProvider() {
        return Stream.of(
                Arguments.of(null, "newPassword1!"),
                Arguments.of("currentPassword", null),
                Arguments.of("invalidPassword", "newPassword"),
                Arguments.of("currentPassword", "1234567"),
                Arguments.of("currentPassword", "12345678"),
                Arguments.of("currentPassword", "1234567A"),
                Arguments.of("currentPassword", "123456Aa"),
                Arguments.of("currentPassword", "newPassword!")
        );
    }

    @ParameterizedTest
    @MethodSource("shouldUpdatePasswordProvider")
    void shouldUpdatePassword(final String currentPassword, final String newPassword) {
        final PasswordUpdate passwordRequest = buildPasswordUpdate()
                .withCurrentPassword(currentPassword)
                .withNewPassword(newPassword);
        final User currentUser = buildUser();

        when(authsProvider.getCurrentUserId()).thenReturn(currentUser.getId());
        when(userStore.findById(currentUser.getId())).thenReturn(Optional.of(currentUser));

        assertThrows(BadRequestException.class, () -> userService.updatePassword(passwordRequest));

        verify(userStore).findById(currentUser.getId());
    }

    @Test
    void updatePasswordSuccess_5FailedAttempts_HaveLockoutTime_BadRequestException() {
        final PasswordUpdate passwordRequest = buildPasswordUpdate();
        final User currentUser = buildUser()
                .withRole(Role.USER)
                .withPassword(passwordRequest.getCurrentPassword())
                .withPasswordFailedCount(5)
                .withLastPasswordFailedAt(Instant.now().plus(ofMinutes(10)));

        when(authsProvider.getCurrentUserId())
                .thenReturn(currentUser.getId());
        when(userStore.findById(currentUser.getId()))
                .thenReturn(Optional.of(currentUser));
        when(passwordEncoder.matches(passwordRequest.getCurrentPassword(), currentUser.getPassword()))
                .thenReturn(true);

        assertThrows(BadRequestException.class, () -> userService.updatePassword(passwordRequest));

        verify(authsProvider, times(1)).getCurrentUserId();
        verify(userStore).findById(currentUser.getId());
    }

    @Test
    void updatePasswordSuccess_4FailedAttempts_DontHaveLockoutTime_OK() {
        final PasswordUpdate passwordRequest = buildPasswordUpdate();
        final User currentUser = buildUser()
                .withRole(Role.USER)
                .withPassword(passwordRequest.getCurrentPassword())
                .withPasswordFailedCount(4)
                .withLastPasswordFailedAt(null);

        when(authsProvider.getCurrentUserId())
                .thenReturn(currentUser.getId());
        when(userStore.findById(currentUser.getId()))
                .thenReturn(Optional.of(currentUser));
        when(passwordEncoder.matches(passwordRequest.getCurrentPassword(), currentUser.getPassword()))
                .thenReturn(true);

        userService.updatePassword(passwordRequest);

        assertEquals(currentUser.getPasswordFailedCount(), 0);

        verify(authsProvider, times(1)).getCurrentUserId();
        verify(userStore, times(1)).findById(currentUser.getId());
    }

    @Test
    void updatePasswordFailure_WithFailedLoginAttemptLessThan5Times_OK() {
        final User currentUser = buildUser()
                .withRole(Role.USER)
                .withPassword(randomAlphanumeric(9))
                .withPasswordFailedCount(0)
                .withLastPasswordFailedAt(null);

        when(userStore.save(currentUser)).thenReturn(currentUser);

        assertThrows(BadRequestException.class, () -> userService.safeVerifyPasswordForCurrentUser(currentUser, currentUser.getPassword()));
        assertEquals(1, currentUser.getPasswordFailedCount());

        verify(userStore).save(currentUser);
    }

    @Test
    void updatePasswordFailure_WithFailedLoginAttempts4Times_AndSetLockoutTime_OK() {
        final User currentUser = buildUser()
                .withRole(Role.USER)
                .withPassword(randomAlphanumeric(9))
                .withPasswordFailedCount(4)
                .withLastPasswordFailedAt(null);

        when(userStore.save(currentUser)).thenReturn(currentUser);

        assertThrows(AccessDeniedException.class, () -> userService.safeVerifyPasswordForCurrentUser(currentUser, currentUser.getPassword()));

        verify(userStore, times(2)).save(currentUser);
    }

    @Test
    void shouldDeactivateById_UserRequestRoleUser_CurrentUserRoleAdmin_OK() {
        final var userToDeactivate = buildUser().withRole(Role.USER);

        when(userStore.findById(userToDeactivate.getId()))
                .thenReturn(Optional.of(userToDeactivate));
        userToDeactivate.setActive(false);
        when(userStore.save(userToDeactivate))
                .thenReturn(userToDeactivate);

        userService.deactivateById(userToDeactivate.getId());
        assertFalse(userToDeactivate.isActive());

        verify(userStore).findById(userToDeactivate.getId());
    }

    @Test
    void shouldDeactivateById_UserRequestRoleAdmin_CurrentUserRoleAdmin_ThroughAccessDeniedException() {
        final var userToDeactivate = buildUser().withRole(Role.ADMIN);

        when(userStore.findById(userToDeactivate.getId()))
                .thenReturn(Optional.of(userToDeactivate));

        assertThrows(AccessDeniedException.class, () -> userService.deactivateById(userToDeactivate.getId()));

        verify(userStore).findById(userToDeactivate.getId());
    }

    @Test
    void shouldFindByName_OK() {
        final var user = buildUser();
        final var expected = buildUsers();

        when(userStore.findByName(user.getFirstName()))
                .thenReturn(expected);

        final var actual = userService.findByName(user.getFirstName());

        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(0).getId(), actual.get(0).getId());
        assertEquals(expected.get(0).getRole().toString(), actual.get(0).getRole().toString());
        assertEquals(expected.get(0).getFirstName(), actual.get(0).getFirstName());
        assertEquals(expected.get(0).getLastName(), actual.get(0).getLastName());
        assertEquals(expected.get(0).getPersonalEmail(), actual.get(0).getPersonalEmail());
        assertEquals(passwordEncoder.encode(expected.get(0).getPassword()), passwordEncoder.encode(actual.get(0).getPassword()));
        assertEquals(expected.get(0).getIdentifier(), actual.get(0).getIdentifier());
        assertEquals(expected.get(0).getPositionId().toString(), actual.get(0).getPositionId().toString());
        assertEquals(expected.get(0).getContractType().toString(), actual.get(0).getContractType().toString());

        verify(userStore).findByName(user.getFirstName());
    }

    @Test
    void shouldFindByName_IsEmpty() {
        final var firstName = randomAlphabetic(9, 10);

        when(userStore.findByName(firstName))
                .thenReturn(Collections.emptyList());

        final var actual = userService.findByName(firstName);

        assertTrue(actual.isEmpty());

        verify(userStore).findByName(firstName);
    }

    @Test
    public void shouldGenerateQRCode_OK() throws IOException, WriterException {
        final var user = buildUser();
        final var width = 200;
        final var height = 200;

        when(userStore.findById(user.getId())).thenReturn(Optional.of(user));
        when(appProperties.getQrCodeWidth()).thenReturn(width);
        when(appProperties.getQrCodeHeight()).thenReturn(height);

        final var actual = userService.generateQRCode(user.getId());

        assertNotNull(actual);
        assertTrue(actual.length > 0);

        verify(userStore).findById(user.getId());
        verify(appProperties).getQrCodeWidth();
        verify(appProperties).getQrCodeHeight();
    }

    @Test
    public void shouldGenerateQRCode_ThrowUserNotFound() {
        final var user = buildUser();

        when(userStore.findById(user.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.generateQRCode(user.getId()));

        verify(userStore).findById(user.getId());
    }

    @Test
    public void shouldGetUserByQRCode_OK() {
        final var qrCode = randomAlphabetic(6);
        final var expected = buildUser()
                .withQrCode(qrCode);

        when(userStore.findByQRCode(qrCode)).thenReturn(Optional.of(expected));

        final var actual = userService.getUserByQRCode(qrCode);

        assertEquals(expected, actual);

        verify(userStore).findByQRCode(qrCode);
    }

    @Test
    public void shouldGetUserByQRCode_ThrowUserNotFound() {
        final var qrCode = randomAlphabetic(6);

        when(userStore.findByQRCode(qrCode)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getUserByQRCode(qrCode));

        verify(userStore).findByQRCode(qrCode);
    }

    @Test
    void shouldFindByCurrentUserId_OK() {
        final var user = buildUser();

        when(authsProvider.getCurrentUserId())
                .thenReturn(user.getId());
        when(userStore.findById(user.getId()))
                .thenReturn(Optional.of(user));

        final var actual = userService.findByCurrentUserId();
        assertEquals(user, actual);

        verify(userStore).findById(user.getId());
        verify(authsProvider, times(2)).getCurrentUserId();
    }

    @Test
    void shouldFindByCurrentUserId_ThroughNotFoundException() {
        final var userId = randomUUID();

        when(authsProvider.getCurrentUserId())
                .thenReturn(userId);
        when(userStore.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.findByCurrentUserId());

        verify(userStore).findById(userId);
        verify(authsProvider, times(2)).getCurrentUserId();
    }

    @Test
    void shouldLoginSuccessfully_CountPasswordFailedWillBeReset_Have2Email_OK() {
        final var user = buildUser()
                .withLastPasswordFailedAt(Instant.now().minus(1, ChronoUnit.HOURS))
                .withPasswordFailedCount(4);

        when(userStore.findByCompanyEmail(user.getCompanyEmail()))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches(user.getPassword(), user.getPassword()))
                .thenReturn(true);

        userService.safeVerifyPasswordForLogin(user.getCompanyEmail(), user.getPassword());
        assertEquals(0, user.getPasswordFailedCount());

        verify(userStore).findByCompanyEmail(user.getCompanyEmail());
        verify(passwordEncoder).matches(user.getPassword(), user.getPassword());
    }

    @Test
    void shouldLoginSuccessfully_CountPasswordFailedWillBeReset_WithCompanyEmail_OK() {
        final var user = buildUser()
                .withLastPasswordFailedAt(Instant.now().minus(1, ChronoUnit.HOURS))
                .withPasswordFailedCount(4);

        when(userStore.findByCompanyEmail(user.getCompanyEmail()))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches(user.getPassword(), user.getPassword()))
                .thenReturn(true);

        userService.safeVerifyPasswordForLogin(user.getCompanyEmail(), user.getPassword());
        assertEquals(0, user.getPasswordFailedCount());

        verify(userStore).findByCompanyEmail(user.getCompanyEmail());
        verify(passwordEncoder).matches(user.getPassword(), user.getPassword());
    }

    @Test
    void shouldLoginFailed_WithUserDeactivate_ThroughAccessException() {
        final var user = buildUser()
                .withCompanyEmail(null)
                .withActive(false);

        when(userStore.findByCompanyEmail(user.getCompanyEmail()))
                .thenReturn(Optional.of(user));

        assertThrows(AccessDeniedException.class, () -> userService.safeVerifyPasswordForLogin(user.getCompanyEmail(), user.getPassword()));

        verify(userStore).findByCompanyEmail(user.getCompanyEmail());
    }

    @Test
    void shouldLoginSuccessfully_CountPasswordFailedWillBeReset_ThrowInvalidEmail() {
        final var email = "abc@openwt.com";
        final var user = buildUser()
                .withPasswordFailedCount(4);

        when(userStore.findByCompanyEmail(email))
                .thenReturn(Optional.empty());

        assertThrows(AccessDeniedException.class, () -> userService.safeVerifyPasswordForLogin(email, user.getPassword()));

        verify(userStore).findByCompanyEmail(email);
    }

    @Test
    void shouldLoginSuccessfully_StillHaveLockTime_ThroughAccessDenied() {
        final var user = buildUser()
                .withPasswordFailedCount(3)
                .withLastPasswordFailedAt(Instant.now().plus(ofMinutes(10)));

        when(userStore.findByCompanyEmail(user.getCompanyEmail()))
                .thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> userService.safeVerifyPasswordForLogin(user.getCompanyEmail(), user.getPassword()));
        assertEquals(3, user.getPasswordFailedCount());

        verify(userStore).findByCompanyEmail(user.getCompanyEmail());
    }

    @Test
    void shouldLoginFailed_IncreasePasswordFailedCount_OK() {
        final var user = buildUser()
                .withPasswordFailedCount(1);

        when(userStore.findByCompanyEmail(user.getCompanyEmail()))
                .thenReturn(Optional.of(user));

        assertThrows(AccessDeniedException.class, () -> userService.safeVerifyPasswordForLogin(user.getCompanyEmail(), user.getPassword()));
        assertEquals(2, user.getPasswordFailedCount());

        verify(userStore).findByCompanyEmail(user.getCompanyEmail());
    }

    @Test
    void shouldLoginFailed_IncreasePasswordFailedCountExceedAttempts_OK() {
        final var user = buildUser()
                .withPasswordFailedCount(4);

        when(userStore.findByCompanyEmail(user.getCompanyEmail()))
                .thenReturn(Optional.of(user));

        assertThrows(AccessDeniedException.class, () -> userService.safeVerifyPasswordForLogin(user.getCompanyEmail(), user.getPassword()));
        assertEquals(0, user.getPasswordFailedCount());

        verify(userStore).findByCompanyEmail(user.getCompanyEmail());
    }

    @Test
    void shouldLoginWithPersonalEmail_ThroughAccessDeniedException() {
        final User user = buildUser()
                .withPasswordFailedCount(4);

        when(userStore.findByCompanyEmail(user.getPersonalEmail())).thenReturn(Optional.empty());
        when(userStore.findByPersonalEmail(user.getPersonalEmail())).thenReturn(Optional.of(user));

        assertThrows(AccessDeniedException.class, () ->
                userService.safeVerifyPasswordForLogin(user.getPersonalEmail(), user.getPassword()));
        assertEquals(4, user.getPasswordFailedCount());

        verify(userStore).findByCompanyEmail(user.getPersonalEmail());
        verify(userStore).findByPersonalEmail(user.getPersonalEmail());
    }


    @Test
    void shouldActiveUserById_OK() {
        final var userToActivate = buildUser()
                .withActive(false)
                .withRole(Role.USER);

        when(userStore.findById(userToActivate.getId()))
                .thenReturn(Optional.of(userToActivate));
        userToActivate.setActive(true);
        when(userStore.save(userToActivate))
                .thenReturn(userToActivate);

        userService.activateById(userToActivate.getId());
        assertTrue(userToActivate.isActive());

        verify(userStore).findById(userToActivate.getId());
        verify(userStore).save(userToActivate);
    }

    @Test
    void shouldCreateUserAndUploadPhoto_OK() {
        final var input = Base64.getEncoder().encode("Test".getBytes());
        final var photo = String.format("%s,%s", "data:image/png;base64", new String(input));
        final var encodedPassword = randomAlphabetic(6, 10);
        final var userToCreate = buildUser()
                .withRole(Role.USER)
                .withPhoto(photo);
        final var image = buildImage()
                .withUserId(userToCreate.getId());

        when(passwordEncoder.encode(anyString()))
                .thenReturn(encodedPassword);
        when(userStore.save(userToCreate))
                .thenReturn(userToCreate);
        when(cloudinaryService.uploadBase64(photo)).thenReturn(image.getUrl());
        doNothing().when(emailService).sendActivationEmail(argThat(userArg -> userArg.getId() == userToCreate.getId()));

        userToCreate.setPassword(encodedPassword);
        final var actual = userService.create(userToCreate);

        assertEquals(userToCreate, actual);

        verify(passwordEncoder).encode(anyString());
        verify(emailService).sendActivationEmail(argThat(userArg -> userArg.getId() == userToCreate.getId()));
        verify(cloudinaryService).uploadBase64(photo);
        verify(userStore).save(userToCreate);
    }

    @Test
    void shouldUpdateAndUploadPhoto_OK() {
        final var input = Base64.getEncoder().encode("Test".getBytes());
        final var photo = String.format("%s,%s", "data:image/png;base64", new String(input));
        final var currentUser = buildUser()
                .withRole(Role.ADMIN);
        final var userToUpdate = buildUser()
                .withRole(Role.USER);
        final var userRequest = buildUser()
                .withRole(Role.USER)
                .withPhoto(photo);
        final var image = buildImage()
                .withUserId(userToUpdate.getId());

        when(userStore.findById(userToUpdate.getId()))
                .thenReturn(Optional.of(userToUpdate));
        when(userStore.findById(authsProvider.getCurrentUserId()))
                .thenReturn(Optional.of(currentUser));
        when(cloudinaryService.uploadBase64(photo)).thenReturn(image.getUrl());
        when(userStore.save(argThat(user -> user.getRole() == Role.USER)))
                .thenReturn(userRequest);

        final var actual = userService.update(userToUpdate.getId(), userRequest);

        assertEquals(userRequest, actual);

        verify(userStore).findById(userToUpdate.getId());
        verify(cloudinaryService).uploadBase64(photo);
        verify(userStore).save(argThat(user -> user.getRole() == Role.USER));
    }

    @Test
    public void shouldSendForgotPasswordEmail_OK() {
        final var email = "test@gmail.com";
        final var user = buildUser()
                .withCompanyEmail(email);

        when(userStore.findByCompanyEmail(email)).thenReturn(Optional.of(user));
        when(userStore.save(user)).thenReturn(user);

        userService.sendForgotPasswordEmail(email);

        verify(userStore).findByCompanyEmail(email);
        verify(userStore).save(user);
    }

    @Test
    public void shouldSendForgotPasswordEmail_ThrowUserNotFound() {
        final var email = randomAlphabetic(3, 10);

        when(userStore.findByCompanyEmail(email)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.sendForgotPasswordEmail(email));

        verify(userStore).findByCompanyEmail(email);
    }

    @Test
    public void shouldVerifyCodeExpiration_OK() {
        final var code = randomAlphabetic(3, 10);
        final var user = buildUser()
                .withResetPasswordCode(code);

        when(userStore.findByResetPasswordCode(code)).thenReturn(Optional.of(user));

        userService.verifyCodeExpiration(code);

        verify(userStore).findByResetPasswordCode(code);
    }

    @Test
    public void shouldVerifyCodeExpiration_ThrowUserNotFound() {
        final var code = randomAlphabetic(3, 10);

        when(userStore.findByResetPasswordCode(code)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> userService.verifyCodeExpiration(code));

        verify(userStore).findByResetPasswordCode(code);
    }

    @Test
    public void shouldVerifyCodeExpiration_ThrowCodeExpiration() {
        final var code = randomAlphabetic(3, 10);
        final var user = buildUser()
                .withResetPasswordCode(code)
                .withLastSendResetPasswordAt(Instant.now().minus(15, ChronoUnit.MINUTES));

        when(userStore.findByResetPasswordCode(code)).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> userService.verifyCodeExpiration(code));

        verify(userStore).findByResetPasswordCode(code);
    }

    @Test
    public void shouldResetPassword_OK() {
        final var passwordRequest = buildPasswordReset();
        final var user = buildUser()
                .withResetPasswordCode(passwordRequest.getCode());

        when(userStore.findByResetPasswordCode(passwordRequest.getCode())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(passwordRequest.getNewPassword()))
                .thenReturn(passwordRequest.getNewPassword());
        when(userStore.save(user)).thenReturn(user);

        userService.resetPassword(passwordRequest);

        verify(userStore).findByResetPasswordCode(passwordRequest.getCode());
        verify(passwordEncoder).encode(passwordRequest.getNewPassword());
        verify(userStore).save(user);
    }

    @Test
    public void shouldResetPassword_ThrowUserNotFound() {
        final var passwordRequest = buildPasswordReset();

        when(userStore.findByResetPasswordCode(passwordRequest.getCode())).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> userService.resetPassword(passwordRequest));

        verify(userStore).findByResetPasswordCode(passwordRequest.getCode());
    }

    @Test
    public void shouldResetPassword_ThrowCodeExpiration() {
        final var passwordRequest = buildPasswordReset();
        final var user = buildUser()
                .withResetPasswordCode(passwordRequest.getCode())
                .withLastSendResetPasswordAt(Instant.now().minus(15, ChronoUnit.MINUTES));

        when(userStore.findByResetPasswordCode(passwordRequest.getCode())).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> userService.resetPassword(passwordRequest));

        verify(userStore).findByResetPasswordCode(passwordRequest.getCode());
    }

    @Test
    public void shouldResetPassword_ThrowInvalidPassword() {
        final var passwordRequest = buildPasswordReset()
                .withNewPassword("123");
        final var user = buildUser()
                .withResetPasswordCode(passwordRequest.getCode());

        when(userStore.findByResetPasswordCode(passwordRequest.getCode())).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> userService.resetPassword(passwordRequest));

        verify(userStore).findByResetPasswordCode(passwordRequest.getCode());
    }
}
