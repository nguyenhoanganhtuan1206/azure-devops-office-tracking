package com.openwt.officetracking.domain.user;

import com.google.zxing.WriterException;
import com.openwt.officetracking.domain.auth.AuthsProvider;
import com.openwt.officetracking.domain.email.EmailService;
import com.openwt.officetracking.domain.position.Position;
import com.openwt.officetracking.domain.position.PositionService;
import com.openwt.officetracking.domain.role.Role;
import com.openwt.officetracking.domain.tracking_history.TrackingHistory;
import com.openwt.officetracking.domain.user_mobile.UserMobile;
import com.openwt.officetracking.domain.user_mobile.UserMobileService;
import com.openwt.officetracking.persistent.tracking_history.TrackingHistoryStore;
import com.openwt.officetracking.persistent.user.UserStore;
import com.openwt.officetracking.properties.AppProperties;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static com.openwt.officetracking.domain.auth.AuthError.supplyUserLoginFailed;
import static com.openwt.officetracking.domain.user.UserError.*;
import static com.openwt.officetracking.domain.user.UserMapper.toUserProfileUpdate;
import static com.openwt.officetracking.domain.user.UserValidation.*;
import static com.openwt.officetracking.domain.utils.Base64Utils.isBase64;
import static com.openwt.officetracking.domain.utils.DateUtil.isDateEqual;
import static com.openwt.officetracking.domain.utils.PasswordGenerator.generatePassword;
import static com.openwt.officetracking.domain.utils.QRCodeGenerator.generateImageQRCode;
import static com.openwt.officetracking.error.CommonError.supplyAccessDeniedError;
import static java.time.Duration.ofMinutes;
import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStore userStore;

    private final EmailService emailService;

    private final CloudinaryService cloudinaryService;

    private final AuthsProvider authsProvider;

    private final AppProperties appProperties;

    private final PasswordEncoder passwordEncoder;

    private final UserMobileService userMobileService;

    private final TrackingHistoryStore trackingHistoryStore;

    private final PositionService positionService;

    final static int MAXIMUM_ATTEMPT_LOGIN = 5;

    private static final long RESET_PASSWORD_EXPIRATION_MINUTES = 10;

    public List<User> findAll() {
        return userStore.findAll();
    }

    public User findById(final UUID id) {
        return userStore.findById(id)
                .orElseThrow(supplyUserNotFound("id", id));
    }

    public User findByCompanyEmail(final String email) {
        return userStore.findByCompanyEmail(email)
                .orElseThrow(supplyUserNotFound("company email", email));
    }

    public User findByCurrentUserId() {
        return userStore.findById(authsProvider.getCurrentUserId())
                .orElseThrow(supplyUserNotFound("id", authsProvider.getCurrentUserId()));
    }

    public User findByResetPasswordCode(final String code) {
        return userStore.findByResetPasswordCode(code)
                .orElseThrow(supplyInvalidResetPasswordCode("Reset password code is invalid"));

    }

    public List<User> findByName(final String name) {
        return userStore.findByName(name);
    }

    public User create(final User user) {
        validateUser(user);
        verifyIfCompanyEmailAvailable(user.getCompanyEmail());

        if (isNotBlank(user.getPersonalEmail())) {
            verifyIfPersonalEmailAvailable(user.getPersonalEmail());
        }

        if (isNotBlank(user.getIdentifier())) {
            verifyIfIdentifierAvailable(user.getIdentifier());
        }

        final var password = generatePassword();
        final var encryptedPassword = passwordEncoder.encode(password);

        user.setQrCode(generateQRCode());
        user.setRole(Role.USER);
        user.setActive(true);
        user.setCreatedAt(Instant.now());

        if (isNotBlank(user.getPhoto()) && isBase64(user.getPhoto())) {
            final var url = cloudinaryService.uploadBase64(user.getPhoto());

            user.setPhoto(url);
        }

        final User newUser = userStore.save(user.withPassword(encryptedPassword));
        final UserMobile newMobile = UserMobile.builder()
                .userId(newUser.getId())
                .isActive(false)
                .build();

        final TrackingHistory newHistory = TrackingHistory.builder()
                .userId(newUser.getId())
                .trackedDate(LocalDateTime.of(LocalDate.now(), LocalTime.of(1, 0)).toInstant(ZoneOffset.UTC))
                .build();

        trackingHistoryStore.save(newHistory);
        emailService.sendActivationEmail(newUser.withPassword(password));
        userMobileService.create(newMobile);

        return newUser;
    }

    public User update(final UUID userId, final User userUpdate) {
        final User currentUser = findById(userId);

        if (findByCurrentUserId().getRole() == Role.ADMIN) {
            validateUser(userUpdate);
        }
        validateWithRoleUser(userUpdate);
        validateAdminUpdatePermission(currentUser, userUpdate);

        return userStore.save(updateCurrentUserProperties(currentUser, userUpdate));
    }

    public UserProfileUpdate updateProfileMobile(final UUID currentUserId, final User updateUserRequest) {
        validateProfileRequestMobile(updateUserRequest);

        final User currentUser = findById(currentUserId);
        final Position userPosition = positionService.findById(currentUser.getPositionId());

        return toUserProfileUpdate(userStore.save(updateUserMobileProperties(currentUser, updateUserRequest)))
                .withPositionName(userPosition.getName());
    }

    public void updatePassword(final PasswordUpdate passwordUpdate) {
        final User currentUser = findById(authsProvider.getCurrentUserId());
        final String newPassword = passwordEncoder.encode(passwordUpdate.getNewPassword());

        verifyPasswordFormat(passwordUpdate.getNewPassword());

        safeVerifyPasswordForCurrentUser(currentUser, passwordUpdate.getCurrentPassword());

        currentUser.setPassword(newPassword);

        userStore.save(currentUser);
    }

    public void sendForgotPasswordEmail(final String email) {
        final User user = findByCompanyEmail(email);
        final String code = randomUUID().toString();

        user.setResetPasswordCode(code);
        user.setLastSendResetPasswordAt(Instant.now());

        userStore.save(user);

        emailService.sendForgotPasswordEmail(user);
    }

    public void verifyCodeExpiration(final String code) {
        final User user = findByResetPasswordCode(code);

        checkResetPasswordCodeExpiration(user.getLastSendResetPasswordAt());
    }

    public void resetPassword(final PasswordReset passwordReset) {
        final User user = findByResetPasswordCode(passwordReset.getCode());

        checkResetPasswordCodeExpiration(user.getLastSendResetPasswordAt());
        verifyPasswordFormat(passwordReset.getNewPassword());

        final String newPassword = passwordEncoder.encode(passwordReset.getNewPassword());

        user.setPassword(newPassword);
        user.setLastSendResetPasswordAt(null);
        user.setResetPasswordCode(null);

        userStore.save(user);
    }

    public void safeVerifyPasswordForLogin(final String email, final String password) {
        final User currentUser = safeVerifyUserByEmail(email);
        safeVerifyActiveForLogin(currentUser);

        final boolean passwordMatched = verifyPasswordAndUpdateUser(currentUser, password);

        if (passwordMatched) {
            throw supplyUserLoginFailed("You have " + (MAXIMUM_ATTEMPT_LOGIN - currentUser.getPasswordFailedCount()) +
                    " attempts remaining before your account gets locked").get();
        }
    }

    public void safeVerifyPasswordForCurrentUser(final User user, final String password) {
        if (verifyPasswordAndUpdateUser(user, password)) {
            throw supplierPasswordNotMatches().get();
        }
    }

    private User safeVerifyUserByEmail(final String email) {
        final User currentUser = userStore.findByCompanyEmail(email)
                .or(() -> userStore.findByPersonalEmail(email))
                .orElseThrow(() -> supplyUserLoginFailed("Invalid username or password. Please try again").get());

        if (StringUtils.equals(currentUser.getPersonalEmail(), email)) {
            throw supplyUserLoginFailed("You cannot log in with personal email. Please try using your company email.").get();
        }

        return currentUser;
    }

    private void safeVerifyActiveForLogin(final User user) {
        if (!user.isActive()) {
            throw supplyUserLoginFailed("Account is deactivated").get();
        }
    }

    private boolean verifyPasswordAndUpdateUser(final User user, final String password) {
        final var lockoutExpirationTime = user.getLastPasswordFailedAt();
        final var isPasswordCorrect = passwordEncoder.matches(password, user.getPassword());

        if (lockoutExpirationTime != null && !lockoutExpirationTime.isBefore(Instant.now())) {
            final var unlockRemainingTime = Instant.now().until(lockoutExpirationTime, ChronoUnit.MINUTES);
            throw supplierLockUserAccount("Your account is locked. Please try again after " + unlockRemainingTime + " minutes").get();
        }
        user.setLastPasswordFailedAt(null);

        if (isPasswordCorrect) {
            user.setPasswordFailedCount(0);
            userStore.save(user);

            return false;
        }

        user.setPasswordFailedCount(user.getPasswordFailedCount() + 1);
        userStore.save(user);

        if (user.getPasswordFailedCount() >= MAXIMUM_ATTEMPT_LOGIN) {
            lockUserAccountIfLoginFailedExcessive(user);
        }

        return true;
    }

    private void lockUserAccountIfLoginFailedExcessive(final User user) {
        final Instant lockoutExpiration = Instant.now()
                .plus(ofMinutes(30));

        user.setLastPasswordFailedAt(lockoutExpiration);
        user.setPasswordFailedCount(0);
        userStore.save(user);

        throw supplyUserLoginFailed("Your account has been temporarily locked. Please try again after 30 minutes").get();
    }

    public void activateById(final UUID id) {
        userStore.save(findById(id).withActive(true));
    }

    public void deactivateById(final UUID id) {
        final User user = findById(id);

        checkPermissionUserUpdate(user);
        userStore.save(user.withActive(false));
    }

    public byte[] generateQRCode(final UUID id) throws IOException, WriterException {
        final User user = findById(id);

        return generateImageQRCode(user.getQrCode(), appProperties.getQrCodeWidth(), appProperties.getQrCodeHeight());
    }

    public User getUserByQRCode(final String qrCode) {

        return userStore.findByQRCode(qrCode)
                .orElseThrow(supplyUserNotFound("qrcode", qrCode));
    }

    private void checkResetPasswordCodeExpiration(final Instant lastSendResetPasswordAt) {
        final Instant currentTime = Instant.now();
        final Instant expirationTime = lastSendResetPasswordAt.plus(RESET_PASSWORD_EXPIRATION_MINUTES, ChronoUnit.MINUTES);

        if (currentTime.isAfter(expirationTime)) {
            throw supplierCodeExpiration("Reset password code has expired.").get();
        }
    }

    private void validateAdminUpdatePermission(final User currentUser, final User userUpdate) {
        if (isRoleCompareNeeded(currentUser, userUpdate)) {
            checkAccessForAdminRole();
        }
    }

    private boolean isRoleCompareNeeded(final User currentUser, final User userUpdate) {
        if (isDateEqual(currentUser.getStartDate(), userUpdate.getStartDate())) {
            return true;
        }

        if (!currentUser.getPositionId().equals(userUpdate.getPositionId())) {
            return true;
        }

        if (isDateEqual(currentUser.getEndDate(), userUpdate.getEndDate())) {
            return true;
        }

        if (!currentUser.getContractType().equals(userUpdate.getContractType())) {
            return true;
        }

        return currentUser.isChangeCode() != userUpdate.isChangeCode();
    }

    private void checkAccessForAdminRole() {
        final User currentUser = findByCurrentUserId();

        if (currentUser.getRole() != Role.ADMIN) {
            throw supplyAccessDeniedError().get();
        }
    }

    private void verifyIfPersonalEmailAvailable(final String email) {
        userStore.findByPersonalEmail(email)
                .ifPresent(existingUser -> {
                    throw supplyUserExisted("personal email", email).get();
                });
    }

    private void verifyIfCompanyEmailAvailable(final String email) {
        userStore.findByCompanyEmail(email)
                .ifPresent(existingUser -> {
                    throw supplyUserExisted("company email", email).get();
                });
    }

    private void verifyIfIdentifierAvailable(final String identifier) {
        userStore.findByIdentifier(identifier)
                .ifPresent(_existingUser -> {
                    throw supplyUserExisted("identifier", identifier).get();
                });
    }

    private String generateQRCode() {
        String qrCode;
        final int qrCodeLength = appProperties.getQrCodeLength();

        do {
            qrCode = random(qrCodeLength, true, true);
        } while (userStore.findByQRCode(qrCode).isPresent());

        return qrCode;
    }

    private User updateCurrentUserProperties(final User currentUser, final User userUpdate) {
        if (!StringUtils.equals(currentUser.getPersonalEmail(), userUpdate.getPersonalEmail())) {
            verifyIfPersonalEmailAvailable(userUpdate.getPersonalEmail());

            currentUser.setPersonalEmail(userUpdate.getPersonalEmail());
        }

        if (!StringUtils.equals(currentUser.getCompanyEmail(), userUpdate.getCompanyEmail())) {
            verifyIfCompanyEmailAvailable(userUpdate.getCompanyEmail());

            currentUser.setCompanyEmail(userUpdate.getCompanyEmail());
        }

        if (!StringUtils.equals(currentUser.getIdentifier(), userUpdate.getIdentifier())) {
            verifyIfIdentifierAvailable(userUpdate.getIdentifier());

            currentUser.setIdentifier(userUpdate.getIdentifier());
        }

        if (isBlank(currentUser.getQrCode()) || userUpdate.isChangeCode()) {
            currentUser.setQrCode(generateQRCode());

            currentUser.setChangeCode(false);
        }

        if (isNotBlank(userUpdate.getPhoto()) && isBase64(userUpdate.getPhoto())) {
            final var url = cloudinaryService.uploadBase64(userUpdate.getPhoto());

            userUpdate.setPhoto(url);
        }

        return currentUser
                .withGender(userUpdate.getGender())
                .withFirstName(userUpdate.getFirstName())
                .withLastName(userUpdate.getLastName())
                .withLevel(userUpdate.getLevel())
                .withPositionId(userUpdate.getPositionId())
                .withDateOfBirth(userUpdate.getDateOfBirth())
                .withAddress(userUpdate.getAddress())
                .withUniversity(userUpdate.getUniversity())
                .withPhoto(userUpdate.getPhoto())
                .withContractType(userUpdate.getContractType())
                .withPhoneNumber(userUpdate.getPhoneNumber())
                .withStartDate(userUpdate.getStartDate())
                .withEndDate(userUpdate.getEndDate());
    }

    private User updateUserMobileProperties(final User user, final User userRequest) {
        if (isNotBlank(userRequest.getPersonalEmail()) &&
                !StringUtils.equals(user.getPersonalEmail(), userRequest.getPersonalEmail())) {
            verifyIfPersonalEmailAvailable(userRequest.getPersonalEmail());

            user.setPersonalEmail(userRequest.getPersonalEmail());
        }

        if (isNotBlank(userRequest.getCompanyEmail()) &&
                !StringUtils.equals(user.getCompanyEmail(), userRequest.getCompanyEmail())) {
            verifyIfCompanyEmailAvailable(userRequest.getCompanyEmail());

            user.setCompanyEmail(userRequest.getCompanyEmail());
        }

        if (isNotBlank(userRequest.getPhoto()) && isBase64(userRequest.getPhoto())) {
            final var url = cloudinaryService.uploadBase64(userRequest.getPhoto());

            userRequest.setPhoto(url);
        }

        return user
                .withCompanyEmail(updateUserInfoIfNotNull(userRequest.getCompanyEmail(), user.getCompanyEmail()))
                .withPersonalEmail(updateUserInfoIfNotNull(userRequest.getPersonalEmail(), user.getPersonalEmail()))
                .withFirstName(updateUserInfoIfNotNull(userRequest.getFirstName(), user.getFirstName()))
                .withLastName(updateUserInfoIfNotNull(userRequest.getLastName(), user.getLastName()))
                .withPhoto(updateUserInfoIfNotNull(userRequest.getPhoto(), user.getPhoto()))
                .withAddress(updateUserInfoIfNotNull(userRequest.getAddress(), user.getAddress()))
                .withPhoneNumber(updateUserInfoIfNotNull(userRequest.getPhoneNumber(), user.getPhoneNumber()));
    }

    private static String updateUserInfoIfNotNull(final String newUserInfo, final String oldUserInfo) {
        return newUserInfo != null ? newUserInfo : oldUserInfo;
    }

    private void checkPermissionUserUpdate(final User userUpdate) {
        if (userUpdate.getRole() == Role.ADMIN) {
            throw supplyAccessDeniedError().get();
        }
    }
}
