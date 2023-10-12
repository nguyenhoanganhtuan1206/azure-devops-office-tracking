package com.openwt.officetracking.domain.device;

import com.openwt.officetracking.domain.auth.AuthsProvider;
import com.openwt.officetracking.domain.device_assignment.DeviceAssignment;
import com.openwt.officetracking.domain.device_assignment.DeviceAssignmentService;
import com.openwt.officetracking.domain.device_configuration.DeviceConfiguration;
import com.openwt.officetracking.domain.device_configuration.DeviceConfigurationRequest;
import com.openwt.officetracking.domain.device_configuration.DeviceConfigurationService;
import com.openwt.officetracking.domain.device_configuration_value.DeviceConfigurationValue;
import com.openwt.officetracking.domain.device_configuration_value.DeviceConfigurationValueService;
import com.openwt.officetracking.domain.device_device_configuration.DeviceDeviceConfiguration;
import com.openwt.officetracking.domain.device_device_configuration.DeviceDeviceConfigurationService;
import com.openwt.officetracking.domain.device_history.DeviceHistory;
import com.openwt.officetracking.domain.device_history.DeviceHistoryService;
import com.openwt.officetracking.domain.device_model.DeviceModel;
import com.openwt.officetracking.domain.device_model.DeviceModelResponse;
import com.openwt.officetracking.domain.device_model.DeviceModelService;
import com.openwt.officetracking.domain.device_model_configuration_value.DeviceModelConfigurationValue;
import com.openwt.officetracking.domain.device_model_configuration_value.DeviceModelConfigurationValueService;
import com.openwt.officetracking.domain.device_status.DeviceStatus;
import com.openwt.officetracking.domain.device_type.DeviceType;
import com.openwt.officetracking.domain.device_type.DeviceTypeRequest;
import com.openwt.officetracking.domain.device_type.DeviceTypeService;
import com.openwt.officetracking.domain.email.EmailService;
import com.openwt.officetracking.domain.request_status.RequestStatus;
import com.openwt.officetracking.domain.user.User;
import com.openwt.officetracking.domain.user.UserService;
import com.openwt.officetracking.persistent.device.DeviceStore;
import com.openwt.officetracking.persistent.device_history.DeviceHistoryStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.openwt.officetracking.domain.device.DeviceError.*;
import static com.openwt.officetracking.domain.device.DeviceMapper.toDeviceDetail;
import static com.openwt.officetracking.domain.device.DeviceRequestMapper.toDevice;
import static com.openwt.officetracking.domain.device.DeviceRequestMapper.toDeviceWhenRequest;
import static com.openwt.officetracking.domain.device.DeviceValidation.*;
import static com.openwt.officetracking.domain.device_configuration.DeviceConfigurationError.supplyDeviceConfigurationInvalid;
import static com.openwt.officetracking.domain.device_configuration_value.DeviceConfigurationValueError.supplyDeviceConfigurationValueInvalid;
import static com.openwt.officetracking.error.CommonError.supplyBadRequestError;
import static com.openwt.officetracking.error.CommonError.supplyValidationError;
import static java.time.Instant.now;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceStore deviceStore;

    private final EmailService emailService;

    private final UserService userService;

    private final DeviceDeviceConfigurationService deviceDeviceConfigurationService;

    private final DeviceModelConfigurationValueService deviceModelConfigurationValueService;

    private final DeviceHistoryService deviceHistoryService;

    private final DeviceHistoryStore deviceHistoryStore;

    private final DeviceTypeService deviceTypeService;

    private final DeviceConfigurationValueService deviceConfigurationValueService;

    private final DeviceConfigurationService deviceConfigurationService;

    private final DeviceAssignmentService deviceAssignmentService;

    private final AuthsProvider authsProvider;

    private final DeviceModelService deviceModelService;

    private static final String ROLE_AMIN = "ROLE_ADMIN";

    public List<DeviceDetail> findMyDevices() {
        final List<Device> devices = deviceStore.findMyDevices(authsProvider.getCurrentUserId());

        return devices.stream()
                .map(this::buildDeviceDetail)
                .toList();
    }

    private DeviceDetail buildDeviceDetail(final Device device) {
        final DeviceAssignment deviceAssignment = deviceAssignmentService.findByUserIdAndDeviceId(authsProvider.getCurrentUserId(), device.getId());

        return toDeviceDetail(device)
                .withDeviceStatus(deviceAssignment.getDeviceStatus())
                .withFromTimeAt(deviceAssignment.getFromTimeAt())
                .withToTimeAt(deviceAssignment.getToTimeAt());
    }

    public Device findById(final UUID deviceId) {
        return deviceStore.findById(deviceId)
                .orElseThrow(supplyDeviceNotFound("id", deviceId));
    }

    public DeviceDetail findDetailById(final UUID deviceId) {
        final Device device = findById(deviceId);
        final List<DeviceDeviceConfiguration> deviceDeviceConfigurations = deviceDeviceConfigurationService.findByDeviceId(deviceId);

        final DeviceTypeRequest deviceTypeRequest = DeviceTypeRequest.builder()
                .modelId(device.getModelId())
                .configurations(buildDeviceConfigurations(deviceDeviceConfigurations))
                .build();

        return buildDeviceDetailProperties(device)
                .withDeviceTypeConfig(deviceTypeRequest);
    }

    public List<Device> findAllDevices() {
        return deviceStore.findAllDevices();
    }

    public List<Device> findDeviceRequests() {
        if (authsProvider.getCurrentUserRole().equals(ROLE_AMIN)) {
            return deviceStore.findAllRequests();
        }

        final UUID currentUserId = authsProvider.getCurrentUserId();
        return deviceStore.findAllRequests(currentUserId);
    }

    @Transactional
    public Device acceptDeviceRequest(final DeviceRequestAccept deviceRequestAccept) {
        final Device deviceRequest = updateRequestOnAccept(deviceRequestAccept.getRequestId());

        final Device assignDevice = findById(deviceRequestAccept.getDeviceId());

        updateDeviceOnAccept(assignDevice, deviceRequest);

        deviceAssignmentService.createByAssignUserIdAndDeviceId(
                assignDevice.getAssignUserId(),
                deviceRequest.getRequestUserId(),
                assignDevice.getId(),
                assignDevice.getDeviceStatus());

        deviceHistoryService.createWithDeviceOnAssignment(assignDevice);
        deviceStore.save(assignDevice);

        final Device acceptRequest = deviceStore.save(deviceRequest);
        final DeviceModel deviceModel = deviceModelService.findById(acceptRequest.getModelId());
        final DeviceType deviceType = deviceTypeService.findById(deviceModel.getTypeId());
        final User requestedUser = userService.findById(acceptRequest.getRequestUserId());

        emailService.sendRequestAcceptEmail(acceptRequest, acceptRequest.getDetail(), deviceType, deviceModel, requestedUser);

        return acceptRequest;
    }

    public Device rejectDeviceRequest(final DeviceRequestReject deviceRequestReject) {
        validateNote(deviceRequestReject.getRejectNote());

        final Device deviceRequest = updateRequestOnReject(deviceRequestReject);

        final Device rejectRequest = deviceStore.save(deviceRequest);

        final DeviceModel deviceModel = deviceModelService.findById(rejectRequest.getModelId());
        final DeviceType deviceType = deviceTypeService.findById(deviceModel.getTypeId());
        final User requestedUser = userService.findById(rejectRequest.getRequestUserId());

        emailService.sendRequestRejectEmail(rejectRequest, rejectRequest.getDetail(), deviceType, deviceModel, requestedUser);

        return rejectRequest;
    }

    private Device updateRequestOnReject(final DeviceRequestReject deviceRequestReject) {
        final Device deviceRequest = findById(deviceRequestReject.getRequestId());

        deviceRequest.setRequestNote(deviceRequestReject.getRejectNote());
        deviceRequest.setRejectedAt(now());
        deviceRequest.setLastModifiedAt(now());
        deviceRequest.setRequestStatus(RequestStatus.REJECTED);

        return deviceRequest;
    }

    @Transactional
    public Device create(final DeviceRequest deviceRequest) {
        validateDeviceRequest(deviceRequest);
        verifyIfDeviceExisted(deviceRequest.getSerialNumber());
        verifyIfDeviceIfNotExisted(deviceRequest.getDeviceTypeConfig().getModelId());
        validateDeviceRequestConfiguration(deviceRequest);
        final Device newDevice = deviceStore.save(toDevice(deviceRequest)
                .withModelId(deviceRequest.getDeviceTypeConfig().getModelId()));

        createNewDeviceDeviceConfiguration(newDevice, deviceRequest);
        deviceHistoryService.createWithNewDevice(newDevice, deviceRequest);
        newDevice.setDetail(findDeviceDetail(newDevice.getId()));

        updateDeviceRequest(newDevice, deviceRequest);

        if (deviceRequest.getDeviceRequestId() != null) {
            newDevice.setDeviceRequestId(deviceRequest.getDeviceRequestId());
            deviceStore.save(updateRequestOnAccept(deviceRequest.getDeviceRequestId()));
        }

        sendEmailOnAssignment(newDevice, deviceRequest.getDeviceRequestId());

        return deviceStore.save(newDevice);
    }

    @Transactional
    public Device update(final UUID deviceId, final DeviceRequest deviceRequest) {
        deviceRequest.setPurchaseAt(convertInstant(deviceRequest.getPurchaseAt()));
        validateDeviceRequest(deviceRequest);
        verifyIfDeviceIfNotExisted(deviceRequest.getDeviceTypeConfig().getModelId());
        validateDeviceRequestConfiguration(deviceRequest);

        final Device currentDevice = findById(deviceId);

        validateDeviceStatus(currentDevice, deviceRequest);

        if (!deviceRequest.getSerialNumber().equals(currentDevice.getSerialNumber())) {
            verifyIfDeviceExisted(deviceRequest.getSerialNumber());
            currentDevice.setSerialNumber(deviceRequest.getSerialNumber());
        }

        handleDeviceDeviceConfigurationAlignWithDeviceModel(currentDevice, deviceRequest);

        final UUID assignedUserId = determineAssignedUserId(currentDevice, deviceRequest);

        updateDeviceStatusAndSendEmail(currentDevice, deviceRequest);

        createDeviceAssignment(currentDevice.getAssignUserId(), deviceRequest.getAssignUserId(), deviceId, deviceRequest.getDeviceStatus());

        updateDeviceAssignmentAndProperties(currentDevice, deviceRequest, assignedUserId);

        updateDevicePropertiesAndHistory(currentDevice, deviceRequest);

        if (deviceRequest.getDeviceRequestId() != null) {
            deviceStore.save(updateRequestOnAccept(deviceRequest.getDeviceRequestId()));
        }

        return updateDeviceProperties(currentDevice, deviceRequest);
    }

    private void createDeviceAssignment(final UUID currentAssignUserId,
                                        final UUID assignUserIdRequest,
                                        final UUID deviceId,
                                        final DeviceStatus requestStatus) {
        if (currentAssignUserId != assignUserIdRequest) {
            deviceAssignmentService.createByAssignUserIdAndDeviceId(currentAssignUserId, assignUserIdRequest, deviceId, requestStatus);
        }
    }

    private void updateDeviceRequest(final Device newDevice, final DeviceRequest deviceRequest) {
        if (deviceRequest.getDeviceStatus() == DeviceStatus.ASSIGNED && deviceRequest.getAssignUserId() != null) {
            newDevice.setAssignUserId(deviceRequest.getAssignUserId());
            saveDeviceAssignment(deviceRequest, newDevice);
        }
    }

    private void sendEmailOnAssignment(final Device deviceAssign, final UUID deviceRequestId) {
        Device deviceRequested = null;
        if (deviceRequestId != null) {
            deviceRequested = findById(deviceRequestId);
        }

        sendEmailForUserWhenAssigned(deviceAssign, deviceRequested);
    }

    private void sendEmailForUserWhenAssigned(final Device deviceAssign, final Device deviceRequested) {
        if (deviceAssign.getDeviceStatus() == DeviceStatus.ASSIGNED && deviceAssign.getAssignUserId() != null) {
            final DeviceModel deviceModel = deviceModelService.findById(deviceAssign.getModelId());
            final DeviceType deviceType = deviceTypeService.findById(deviceModel.getTypeId());
            final User user = userService.findById(deviceAssign.getAssignUserId());

            emailService.sendAssignedDeviceEmail(deviceAssign, deviceRequested, deviceType, deviceModel, user);
        }
    }

    private void sendEmailForUserWhenUpdateStatusDevice(final Device device) {
        final Optional<DeviceHistory> devicePreviousConfirmHistory = deviceHistoryStore.findDevicePreviousConfirmHistory(device.getId());
        final Optional<DeviceHistory> deviceUtilizedHistory = deviceHistoryStore.findDevicePreviousHistory(device.getId(), DeviceStatus.UTILIZED);
        final Optional<DeviceHistory> deviceAssignedHistory = deviceHistoryStore.findDevicePreviousHistory(device.getId(), DeviceStatus.ASSIGNED);

        if ((devicePreviousConfirmHistory.isPresent() || deviceUtilizedHistory.isPresent() || deviceAssignedHistory.isPresent()) && device.getAssignUserId() != null) {
            final DeviceModel deviceModel = deviceModelService.findById(device.getModelId());
            final DeviceType deviceType = deviceTypeService.findById(deviceModel.getTypeId());
            final User user = userService.findById(device.getAssignUserId());

            emailService.sendUpdateStatusDeviceEmail(device, deviceType, deviceModel, user);
        }
    }


    private void validateDeviceStatus(final Device currentDevice, final DeviceRequest deviceRequest) {
        if (currentDevice.getDeviceStatus() == DeviceStatus.UTILIZED && deviceRequest.getDeviceStatus() == DeviceStatus.ASSIGNED) {
            throw supplyDeviceValidation("Can't assign device to user when status in UTILIZED").get();
        }

        if (currentDevice.getDeviceStatus() != DeviceStatus.REPAIRING && currentDevice.getDeviceStatus() != DeviceStatus.AVAILABLE && deviceRequest.getDeviceStatus() == DeviceStatus.SCRAPPED) {
            throw supplyValidationError("You cannot update the status to SCRAPPED when the current device status is not REPAIRING or AVAILABLE").get();
        }

        if (currentDevice.getDeviceStatus() == DeviceStatus.SCRAPPED) {
            throw supplyValidationError("You cannot update another status when the current device status is SCRAPPED").get();
        }

        if (currentDevice.getDeviceStatus() == DeviceStatus.LOST && deviceRequest.getDeviceStatus() != DeviceStatus.AVAILABLE) {
            throw supplyValidationError("You can only update the status to AVAILABLE when the current device status is LOST").get();
        }

        if (currentDevice.getDeviceStatus() == DeviceStatus.REPAIRING) {
            if (deviceRequest.getDeviceStatus() != DeviceStatus.SCRAPPED &&
                    deviceRequest.getDeviceStatus() != DeviceStatus.ASSIGNED &&
                    deviceRequest.getDeviceStatus() != DeviceStatus.AVAILABLE) {
                throw supplyValidationError("You cannot update this status when the current device status is REPAIRING").get();
            }
        }
    }

    private Device updateRequestOnAccept(final UUID requestId) {
        final Device deviceRequest = findById(requestId);

        deviceRequest.setAcceptedAt(now());
        deviceRequest.setLastModifiedAt(now());
        deviceRequest.setRequestStatus(RequestStatus.ACCEPTED);

        return deviceRequest;
    }

    private void updateRequestOnCompleted(final UUID requestId) {
        final Device deviceRequest = findById(requestId);

        deviceRequest.setCompletedAt(now());
        deviceRequest.setLastModifiedAt(now());
        deviceRequest.setRequestStatus(RequestStatus.COMPLETED);

        deviceStore.save(deviceRequest);
    }

    private void updateDeviceOnAccept(final Device assignDevice, final Device deviceRequest) {

        assignDevice.setAssignUserId(deviceRequest.getRequestUserId());
        assignDevice.setRequestedAt(deviceRequest.getRequestedAt());
        assignDevice.setAcceptedAt(deviceRequest.getAcceptedAt());
        assignDevice.setRequestReason(deviceRequest.getRequestReason());
        assignDevice.setDeviceStatus(DeviceStatus.ASSIGNED);
        assignDevice.setDeviceRequestId(deviceRequest.getId());
        assignDevice.setLastModifiedAt(now());
    }

    private void verifyIfDeviceIfNotExisted(final UUID deviceModelId) {
        deviceModelService.findById(deviceModelId);
    }

    private String findDeviceDetail(final UUID deviceId) {
        final List<DeviceDeviceConfiguration> deviceDeviceConfigurationList = deviceDeviceConfigurationService.findByDeviceId(deviceId);

        final List<String> deviceConfigurationValues = deviceDeviceConfigurationList.stream()
                .map(deviceDeviceConfiguration -> {
                    final DeviceModelConfigurationValue deviceModelConfigurationValue = deviceModelConfigurationValueService.findById(deviceDeviceConfiguration.getDeviceModelConfigurationValueId());

                    if (deviceModelConfigurationValue.getDeviceConfigurationValueId() != null) {
                        final DeviceConfigurationValue deviceConfigurationValue = deviceConfigurationValueService.findById(deviceModelConfigurationValue.getDeviceConfigurationValueId());
                        return deviceConfigurationValue.getValue();
                    }

                    return "";
                })
                .toList();

        return String.join(" ", deviceConfigurationValues);
    }

    private void handleDeviceDeviceConfigurationAlignWithDeviceModel(final Device currentDevice, final DeviceRequest deviceRequest) {
        validateDeviceConfigRequestsExisting(deviceRequest.getDeviceTypeConfig().getConfigurations());

        if (currentDevice.getModelId().equals(deviceRequest.getDeviceTypeConfig().getModelId())) {
            updateDeviceDeviceConfiguration(currentDevice, deviceRequest);
            return;
        }

        deviceDeviceConfigurationService.deleteByDeviceId(currentDevice.getId());
        createNewDeviceDeviceConfiguration(currentDevice, deviceRequest);
    }

    private void updateDeviceDeviceConfiguration(final Device device, final DeviceRequest deviceRequest) {
        deviceRequest.getDeviceTypeConfig().getConfigurations()
                .forEach(deviceConfigurationRequest -> updateDeviceConfigurationIfDifferent(device, deviceConfigurationRequest));
    }


    private void updateDeviceConfigurationIfDifferent(final Device device, final DeviceConfigurationRequest deviceConfigurationRequest) {
        final List<DeviceDeviceConfiguration> deviceDeviceConfigurationExists = deviceDeviceConfigurationService.findByDeviceId(device.getId());
        final UUID configurationIdRequest = deviceConfigurationRequest.getId();
        final UUID valueIdRequest = deviceConfigurationRequest.getValueId();

        deviceDeviceConfigurationExists
                .forEach(deviceDeviceConfiguration -> {
                    final DeviceModelConfigurationValue deviceModelConfigurationValueExit = deviceModelConfigurationValueService.findById(deviceDeviceConfiguration.getDeviceModelConfigurationValueId());
                    final UUID configurationIdExit = deviceModelConfigurationValueExit.getDeviceConfigurationId();
                    final UUID valueIdExit = deviceModelConfigurationValueExit.getDeviceConfigurationValueId();

                    if (configurationIdRequest.equals(configurationIdExit) && !valueIdExit.equals(valueIdRequest)) {
                        final DeviceModelConfigurationValue deviceModelConfigurationValue = deviceModelConfigurationValueService.findByModelAndConfigurationAndValue(device.getModelId(), configurationIdExit, valueIdRequest);

                        deviceDeviceConfiguration.setDeviceModelConfigurationValueId(deviceModelConfigurationValue.getId());
                        deviceDeviceConfigurationService.save(deviceDeviceConfiguration);
                    }
                });
    }

    private void validateDeviceConfigRequestsExisting(final List<DeviceConfigurationRequest> deviceConfigurationRequests) {
        deviceConfigurationRequests
                .forEach(deviceConfigurationRequest -> {
                    deviceConfigurationService.findById(deviceConfigurationRequest.getId());
                    deviceConfigurationValueService.findById(deviceConfigurationRequest.getValueId());
                });
    }

    private Device updateDeviceProperties(final Device currentDevice, final DeviceRequest deviceRequest) {
        return currentDevice
                .withModelId(deviceRequest.getDeviceTypeConfig().getModelId())
                .withPurchaseAt(deviceRequest.getPurchaseAt())
                .withLastModifiedAt(now())
                .withDeviceStatus(deviceRequest.getDeviceStatus())
                .withDetail(findDeviceDetail(currentDevice.getId()))
                .withWarrantyEndAt(deviceRequest.getWarrantyEndAt())
                .withDeviceRequestId(deviceRequest.getDeviceRequestId());
    }

    private void verifyIfDeviceExisted(final String serialNumber) {
        deviceStore.findBySerialNumber(serialNumber)
                .ifPresent(_device -> {
                    throw supplyDeviceExisted(serialNumber, "serial number").get();
                });
    }

    public void confirmDevice(final UUID confirmDeviceId) {
        final Device confirmDevice = findById(confirmDeviceId);

        verifyDeviceAssignedUser(confirmDevice);
        verifyDeviceAssignedStatus(confirmDevice);
        confirmDevice.setDeviceStatus(DeviceStatus.UTILIZED);
        confirmDevice.setLastModifiedAt(now());

        deviceHistoryService.createWithDeviceOnAssignment(confirmDevice);

        final Device updateDevice = deviceStore.save(confirmDevice);
        final DeviceAssignment deviceAssignment = deviceAssignmentService.findByUserIdAndDeviceId(authsProvider.getCurrentUserId(), updateDevice.getId());

        deviceAssignmentService.save(deviceAssignment.withDeviceStatus(DeviceStatus.UTILIZED));

        if (confirmDevice.getDeviceRequestId() != null) {
            updateRequestOnCompleted(confirmDevice.getDeviceRequestId());
        }

        final DeviceModel deviceModel = deviceModelService.findById(updateDevice.getModelId());
        final DeviceType deviceType = deviceTypeService.findById(deviceModel.getTypeId());
        final User confirmUser = userService.findById(updateDevice.getAssignUserId());

        emailService.sendConfirmDeviceEmail(updateDevice, updateDevice.getDetail(), deviceType, deviceModel, confirmUser);
    }

    private void verifyDeviceAssignedStatus(final Device device) {
        if (device.getDeviceStatus() != DeviceStatus.ASSIGNED) {
            throw supplyBadRequestError("Device status must be ASSIGNED to confirm").get();
        }
    }

    private void verifyDeviceAssignedUser(final Device device) {
        if (!device.getAssignUserId().equals(authsProvider.getCurrentUserId())) {
            throw supplyBadRequestError("Only the assigned user can confirm the device").get();
        }
    }

    public Device createDeviceRequest(final DeviceRequest deviceRequest) {
        validateDeviceRequestRequest(deviceRequest);

        final Device newDeviceRequest = deviceStore.save((toDeviceWhenRequest(deviceRequest)).withRequestUserId(authsProvider.getCurrentUserId()));

        createNewDeviceDeviceConfiguration(newDeviceRequest, deviceRequest);
        final String deviceDetail = findDeviceDetail(newDeviceRequest.getId());

        newDeviceRequest.setDetail(deviceDetail);
        deviceStore.save(newDeviceRequest);

        final DeviceModel deviceModel = deviceModelService.findById(newDeviceRequest.getModelId());
        final DeviceType deviceType = deviceTypeService.findById(deviceModel.getTypeId());
        final User requestedUser = userService.findById(newDeviceRequest.getRequestUserId());

        emailService.sendRequestDeviceEmail(newDeviceRequest, deviceDetail, deviceType, deviceModel, requestedUser);

        return newDeviceRequest;
    }

    private List<DeviceConfigurationRequest> buildDeviceConfigurations(final List<DeviceDeviceConfiguration> deviceDeviceConfigurations) {
        return deviceDeviceConfigurations.stream()
                .map(deviceDeviceConfiguration -> {
                    final DeviceModelConfigurationValue deviceModelConfigurationValue = deviceModelConfigurationValueService.findById(deviceDeviceConfiguration.getDeviceModelConfigurationValueId());

                    return DeviceConfigurationRequest.builder()
                            .id(deviceModelConfigurationValue.getDeviceConfigurationId())
                            .valueId(deviceModelConfigurationValue.getDeviceConfigurationValueId())
                            .build();
                })
                .toList();
    }

    private DeviceDetail buildDeviceDetailProperties(final Device device) {
        return DeviceDetail.builder()
                .id(device.getId())
                .modelId(device.getModelId())
                .detail(device.getDetail())
                .serialNumber(device.getSerialNumber())
                .purchaseAt(device.getPurchaseAt())
                .warrantyEndAt(device.getWarrantyEndAt())
                .deviceStatus(device.getDeviceStatus())
                .build();
    }

    public void createNewDeviceDeviceConfiguration(final Device device, final DeviceRequest deviceRequest) {
        final DeviceTypeRequest deviceTypeRequest = deviceRequest.getDeviceTypeConfig();

        if (deviceTypeRequest.getConfigurations().isEmpty()) {
            final List<DeviceModelConfigurationValue> deviceModelConfigurationValues = deviceModelConfigurationValueService.findByModel(deviceTypeRequest.getModelId());

            if (deviceModelConfigurationValues.size() == 1) {
                final DeviceDeviceConfiguration deviceDeviceConfiguration = DeviceDeviceConfiguration.builder()
                        .deviceId(device.getId())
                        .deviceModelConfigurationValueId(deviceModelConfigurationValues.get(0).getId())
                        .build();

                deviceDeviceConfigurationService.create(deviceDeviceConfiguration);
            }
        }

        for (final DeviceConfigurationRequest deviceConfigurationRequest : deviceRequest.getDeviceTypeConfig().getConfigurations()) {
            final DeviceModelConfigurationValue deviceModelConfigurationValue =  deviceModelConfigurationValueService.findByModelAndConfigurationAndValue(deviceTypeRequest.getModelId(), deviceConfigurationRequest.getId(), deviceConfigurationRequest.getValueId());

            final DeviceDeviceConfiguration deviceDeviceConfiguration = DeviceDeviceConfiguration.builder()
                    .deviceId(device.getId())
                    .deviceModelConfigurationValueId(deviceModelConfigurationValue.getId())
                    .build();

            deviceDeviceConfigurationService.create(deviceDeviceConfiguration);
        }
    }

    private void validateDeviceRequestConfiguration(final DeviceRequest deviceRequest) {
        final UUID deviceModelId = deviceRequest.getDeviceTypeConfig().getModelId();
        final DeviceModelResponse deviceModelResponse = deviceModelService.findByModelId(deviceModelId);
        final List<DeviceConfigurationRequest> deviceConfigRequests = deviceRequest.getDeviceTypeConfig().getConfigurations();
        final List<DeviceConfiguration> deviceConfigurations = deviceModelResponse.getConfigurations();

        deviceConfigRequests.forEach(configRequest -> validateConfiguration(configRequest, deviceConfigurations));
    }

    private void validateConfiguration(final DeviceConfigurationRequest deviceConfiguration, final List<DeviceConfiguration> deviceConfigurations) {
        final UUID configId = deviceConfiguration.getId();
        final UUID configValueId = deviceConfiguration.getValueId();

        if (configValueId == null) {
            throw supplyValidationError("Configuration value cannot be null").get();
        }

        final boolean isMatchingConfiguration = findMatchingConfiguration(deviceConfigurations, configId).getValues().stream()
                .anyMatch(value -> value.getId().equals(configValueId));

        if (!isMatchingConfiguration) {
            throw supplyDeviceConfigurationValueInvalid().get();
        }
    }

    private DeviceConfiguration findMatchingConfiguration(final List<DeviceConfiguration> deviceConfigurations, final UUID configId) {
        return deviceConfigurations.stream()
                .filter(typeConfig -> typeConfig.getId().equals(configId))
                .findFirst()
                .orElseThrow(() -> supplyDeviceConfigurationInvalid().get());
    }

    private void saveDeviceAssignment(final DeviceRequest deviceRequest, final Device device) {
        final DeviceAssignment deviceAssignment = DeviceAssignment.builder()
                .userId(deviceRequest.getAssignUserId())
                .deviceId(device.getId())
                .fromTimeAt(now())
                .toTimeAt(null)
                .build();

        if (deviceRequest.getDeviceStatus() == DeviceStatus.ASSIGNED && deviceRequest.getAssignUserId() != null) {
            deviceAssignment.setDeviceStatus(DeviceStatus.ASSIGNED);
        }

        deviceAssignmentService.save(deviceAssignment);
    }

    private boolean isDeviceConfirmAssignment(final UUID userId, final UUID deviceId) {
        if (userId == null) return false;

        final DeviceAssignment deviceAssignment = deviceAssignmentService.findByUserIdAndDeviceId(userId, deviceId);

        return deviceAssignment != null && (deviceAssignment.getDeviceStatus() == DeviceStatus.UTILIZED);
    }

    private UUID determineAssignedUserId(final Device currentDevice, final DeviceRequest deviceRequest) {
        if (deviceRequest.getAssignUserId() == null) {
            if (deviceRequest.getDeviceStatus() != DeviceStatus.AVAILABLE && isDeviceConfirmAssignment(currentDevice.getAssignUserId(), currentDevice.getId())) {
                return currentDevice.getAssignUserId();
            }
        }

        if (deviceRequest.getDeviceStatus() == DeviceStatus.ASSIGNED) {
            return deviceRequest.getAssignUserId();
        }

        return null;
    }

    private void updateDeviceStatusAndSendEmail(final Device currentDevice, final DeviceRequest deviceRequest) {
        if (deviceRequest.getDeviceStatus() != DeviceStatus.ASSIGNED) {
            currentDevice.setDeviceStatus(deviceRequest.getDeviceStatus());
            sendEmailForUserWhenUpdateStatusDevice(currentDevice);
        }
    }

    private void updateDeviceAssignmentAndProperties(final Device currentDevice, final DeviceRequest deviceRequest, final UUID assignedUserId) {
        if ((!Objects.equals(currentDevice.getAssignUserId(), assignedUserId) || currentDevice.getDeviceStatus() == DeviceStatus.REPAIRING) && assignedUserId != null) {
            final DeviceModel model = deviceModelService.findById(deviceRequest.getDeviceTypeConfig().getModelId());

            currentDevice.setModelId(model.getId());
            currentDevice.setAssignUserId(assignedUserId);
            currentDevice.setDeviceStatus(deviceRequest.getDeviceStatus());
            sendEmailOnAssignment(currentDevice, deviceRequest.getDeviceRequestId());
        }

        currentDevice.setAssignUserId(assignedUserId);
    }

    private void updateDevicePropertiesAndHistory(final Device currentDevice, final DeviceRequest deviceRequest) {
        final DeviceHistory newHistory = deviceHistoryService.createWithNewDevice(currentDevice, deviceRequest);

        deviceStore.save(updateDeviceProperties(currentDevice, deviceRequest)
                .withRequestReason(newHistory.getCondition()));
    }

    private Instant convertInstant(final Instant instantInput) {
        return instantInput.atZone(ZoneOffset.UTC)
                .toLocalDate()
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant();
    }
}