package com.openwt.officetracking.domain.email;

import com.openwt.officetracking.domain.device.Device;
import com.openwt.officetracking.domain.device_model.DeviceModel;
import com.openwt.officetracking.domain.device_type.DeviceType;
import com.openwt.officetracking.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.time.ZoneId;
import java.util.Map;

import static com.openwt.officetracking.domain.utils.ResourceUtils.readResource;
import static com.openwt.officetracking.domain.utils.StringUtils.findCaptureGroup;
import static com.openwt.officetracking.domain.utils.StringUtils.replaceText;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Map.entry;

@Service
@RequiredArgsConstructor
public class EmailService {

    private static final String ACTIVATION_EMAIL_TEMPLATE_PATH = "/templates/ActivationEmail.html";

    private static final String RESET_PASSWORD_EMAIL_TEMPLATE_PATH = "/templates/ResetPasswordEmail.html";

    private static final String REQUEST_ACCEPT_EMAIL_TEMPLATE_PATH = "/templates/RequestAcceptEmail.html";

    private static final String REQUEST_REJECT_EMAIL_TEMPLATE_PATH = "/templates/RequestRejectEmail.html";

    private static final String CONFIRM_DEVICE_EMAIL_TEMPLATE_PATH = "/templates/ConfirmDeviceEmail.html";

    private static final String REQUEST_DEVICE_EMAIL_TEMPLATE_PATH = "/templates/RequestDeviceEmail.html";

    private static final String ASSIGNED_DEVICE_EMAIL_TEMPLATE_PATH = "/templates/AssignedDeviceEmail.html";

    private static final String UPDATE_STATUS_DEVICE_EMAIL_TEMPLATE_PATH = "/templates/UpdateStatusDeviceEmail.html";

    private static final String EMAIL_SUBJECT_REGEX = "<title>(.*?)</title>";

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String contactMail;

    @Value("${app.system.name}")
    private String systemName;

    @Value("${frontend.domain}")
    private String frontendDomain;

    public void sendActivationEmail(final User user) {
        final String email = user.getCompanyEmail();
        final String mailContent = readResource(ACTIVATION_EMAIL_TEMPLATE_PATH);
        final var replacements = createReplacementsMapActivation(user);
        final String formattedMailContent = replacePlaceholders(mailContent, replacements);

        mailSender.send(buildMessage(email, formattedMailContent));
    }

    public void  sendForgotPasswordEmail(final User user) {
        final String email = user.getCompanyEmail();
        final String mailContent = readResource(RESET_PASSWORD_EMAIL_TEMPLATE_PATH);
        final var replacements = createReplacementsMapForgotPassword(user);
        final String formattedMailContent = replacePlaceholders(mailContent, replacements);

        mailSender.send(buildMessage(email, formattedMailContent));
    }

    public void sendRequestAcceptEmail(final Device device, final String deviceDetail, final DeviceType deviceType, final DeviceModel deviceModel, final User user) {
        final String email = user.getCompanyEmail();
        final String mailContent = readResource(REQUEST_ACCEPT_EMAIL_TEMPLATE_PATH);
        final var replacements = createReplacementsMapRequestAccept(device, deviceDetail, deviceType, deviceModel, user);
        final String formattedMailContent = replacePlaceholders(mailContent, replacements);

        mailSender.send(buildMessage(email, formattedMailContent));
    }

    public void sendRequestRejectEmail(final Device device, final String deviceDetail, final DeviceType deviceType, final DeviceModel deviceModel, final User user) {
        final String email = user.getCompanyEmail();
        final String mailContent = readResource(REQUEST_REJECT_EMAIL_TEMPLATE_PATH);
        final var replacements = createReplacementsMapRequestReject(device, deviceDetail, deviceType, deviceModel, user);
        final String formattedMailContent = replacePlaceholders(mailContent, replacements);

        mailSender.send(buildMessage(email, formattedMailContent));
    }

    public void sendConfirmDeviceEmail(final Device device, final String deviceDetail, final DeviceType deviceType, final DeviceModel deviceModel, final User user) {
        final String mailContent = readResource(CONFIRM_DEVICE_EMAIL_TEMPLATE_PATH);
        final var replacements = createReplacementsMapConfirmDevice(device, deviceDetail, deviceType, deviceModel, user);
        final String formattedMailContent = replacePlaceholders(mailContent, replacements);

        mailSender.send(buildMessage(contactMail, formattedMailContent));
    }

    public void sendRequestDeviceEmail(final Device device, final String deviceDetail, final DeviceType deviceType, final DeviceModel deviceModel, final User user) {
        final String mailContent = readResource(REQUEST_DEVICE_EMAIL_TEMPLATE_PATH);
        final var replacements = createReplacementsMapRequestDevice(device, deviceDetail, deviceType, deviceModel, user);
        final String formattedMailContent = replacePlaceholders(mailContent, replacements);

        mailSender.send(buildMessage(contactMail, formattedMailContent));
    }

    public void sendAssignedDeviceEmail(final Device deviceAssign, final Device deviceRequested, final DeviceType deviceType, final DeviceModel deviceModel, final User user) {
        final String email = user.getCompanyEmail();
        final String mailContent = readResource(ASSIGNED_DEVICE_EMAIL_TEMPLATE_PATH);
        final var replacements = createReplacementsMapAssignedDevice(deviceAssign, deviceRequested, deviceType, deviceModel, user);
        final String formattedMailContent = replacePlaceholders(mailContent, replacements);

        mailSender.send(buildMessage(email, formattedMailContent));
    }

    public void sendUpdateStatusDeviceEmail(final Device device, final DeviceType deviceType, final DeviceModel deviceModel, final User user) {
        final String email = user.getCompanyEmail();
        final String mailContent = readResource(UPDATE_STATUS_DEVICE_EMAIL_TEMPLATE_PATH);
        final var replacements = createReplacementsMapUpdateStatusDevice(device, deviceType, deviceModel, user);
        final String formattedMailContent = replacePlaceholders(mailContent, replacements);

        mailSender.send(buildMessage(email, formattedMailContent));
    }

    private String replacePlaceholders(final String mailContent, final Map<String, String> replacements) {
        return replaceText(mailContent, replacements);
    }

    private Map<String, String> createReplacementsMapActivation(final User user) {
        return Map.of(
                "firstName", user.getFirstName(),
                "lastName", user.getLastName(),
                "username", user.getCompanyEmail(),
                "password", user.getPassword()
        );
    }

    private Map<String, String> createReplacementsMapForgotPassword(final User user) {
        return Map.of(
                "firstName", user.getFirstName(),
                "lastName", user.getLastName(),
                "domain", frontendDomain,
                "code", user.getResetPasswordCode()
        );
    }

    private Map<String, String> createReplacementsMapConfirmDevice(final Device device, final String deviceDetail, final DeviceType deviceType, final DeviceModel deviceModel, final User user) {
        final String requestedAtValue = device.getRequestedAt() != null ? ofPattern("dd-MM-yyyy").format(device.getRequestedAt().atZone(ZoneId.systemDefault())) : "N/A";
        final String acceptedAtValue = device.getAcceptedAt() != null ? ofPattern("dd-MM-yyyy").format(device.getAcceptedAt().atZone(ZoneId.systemDefault())) : "N/A";

        return Map.of(
                "firstName", user.getFirstName(),
                "lastName", user.getLastName(),
                "email", user.getCompanyEmail(),
                "type", deviceType.getName(),
                "model", deviceModel.getName(),
                "detail", deviceDetail,
                "reason", device.getRequestReason(),
                "requestedAt", requestedAtValue,
                "acceptedAt", acceptedAtValue,
                "lastModifiedAt", ofPattern("dd-MM-yyyy").format(device.getLastModifiedAt().atZone(ZoneId.systemDefault()))
        );
    }

    private Map<String, String> createReplacementsMapRequestDevice(final Device device, final String deviceDetail, final DeviceType deviceType, final DeviceModel deviceModel, final User user) {
        final String requestedModelValue = deviceModel.getName() != null ? deviceModel.getName() : "N/A";

        return Map.of(
                "firstName", user.getFirstName(),
                "lastName", user.getLastName(),
                "email", user.getCompanyEmail(),
                "type", deviceType.getName(),
                "model", requestedModelValue,
                "detail", deviceDetail,
                "reason", device.getRequestReason(),
                "requestedAt", ofPattern("dd-MM-yyyy").format(device.getRequestedAt().atZone(ZoneId.systemDefault())),
                "status", device.getRequestStatus().toString()
        );
    }

    private Map<String, String> createReplacementsMapRequestAccept(final Device device, final String deviceDetail, final DeviceType deviceType, final DeviceModel deviceModel, final User user) {
        return Map.of(
                "firstName", user.getFirstName(),
                "lastName", user.getLastName(),
                "email", user.getCompanyEmail(),
                "type", deviceType.getName(),
                "model", deviceModel.getName(),
                "detail", deviceDetail,
                "reason", device.getRequestReason(),
                "requestedAt", ofPattern("dd-MM-yyyy").format(device.getRequestedAt().atZone(ZoneId.systemDefault())),
                "acceptedAt", ofPattern("dd-MM-yyyy").format(device.getAcceptedAt().atZone(ZoneId.systemDefault()))
        );
    }

    private Map<String, String> createReplacementsMapRequestReject(final Device device, final String deviceDetail, final DeviceType deviceType, final DeviceModel deviceModel, final User user) {
        return Map.of(
                "firstName", user.getFirstName(),
                "lastName", user.getLastName(),
                "email", user.getCompanyEmail(),
                "type", deviceType.getName(),
                "model", deviceModel.getName(),
                "detail", deviceDetail,
                "reason", device.getRequestReason(),
                "note", device.getRequestNote(),
                "requestedAt", ofPattern("dd-MM-yyyy").format(device.getRequestedAt().atZone(ZoneId.systemDefault())),
                "rejectedAt", ofPattern("dd-MM-yyyy").format(device.getRejectedAt().atZone(ZoneId.systemDefault()))
        );
    }

    private Map<String, String> createReplacementsMapUpdateStatusDevice(final Device device, final DeviceType deviceType, final DeviceModel deviceModel, final User user) {
        return Map.of(
                "firstName", user.getFirstName(),
                "lastName", user.getLastName(),
                "email", user.getCompanyEmail(),
                "type", deviceType.getName(),
                "model", deviceModel.getName(),
                "detail", device.getDetail(),
                "reason", device.getRequestReason(),
                "status", device.getDeviceStatus().toString(),
                "lastModifiedOn", ofPattern("dd-MM-yyyy").format(device.getLastModifiedAt().atZone(ZoneId.systemDefault()))
        );
    }

    private Map<String, String> createReplacementsMapAssignedDevice(final Device deviceAssign, final Device deviceRequested, final DeviceType deviceType, final DeviceModel deviceModel, final User user) {
        final String requestedDetailValue = deviceRequested != null ? deviceRequested.getDetail() : "N/A";
        final String requestedAtValue = deviceRequested != null
                ? ofPattern("dd-MM-yyyy").format(deviceRequested.getRequestedAt().atZone(ZoneId.systemDefault())) : "N/A";

        return Map.ofEntries(
                entry("firstName", user.getFirstName()),
                entry("lastName", user.getLastName()),
                entry("email", user.getCompanyEmail()),
                entry("type", deviceType.getName()),
                entry("model", deviceModel.getName()),
                entry("assignedDetail", deviceAssign.getDetail()),
                entry("reason", deviceAssign.getRequestReason()),
                entry("status", deviceAssign.getDeviceStatus().toString()),
                entry("assignedAt", ofPattern("dd-MM-yyyy").format(deviceAssign.getLastModifiedAt().atZone(ZoneId.systemDefault()))),
                entry("requestedDetail", requestedDetailValue),
                entry("requestedAt", requestedAtValue)
        );
    }

    private MimeMessage buildMessage(final String to, final String body) {
        try {
            final MimeMessage message = mailSender.createMimeMessage();
            final MimeMessageHelper helper = new MimeMessageHelper(message, true);
            final String subject = findCaptureGroup(body, EMAIL_SUBJECT_REGEX);

            helper.setFrom(contactMail, systemName);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            return message;
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while processing the email. Please try again later", e);
        }
    }
}
