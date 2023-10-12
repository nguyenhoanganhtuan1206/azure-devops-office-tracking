package com.openwt.officetracking.domain.device;

import com.openwt.officetracking.domain.auth.AuthsProvider;
import com.openwt.officetracking.domain.device_assignment.DeviceAssignmentService;
import com.openwt.officetracking.domain.device_configuration.DeviceConfigurationService;
import com.openwt.officetracking.domain.device_configuration_value.DeviceConfigurationValueService;
import com.openwt.officetracking.domain.device_device_configuration.DeviceDeviceConfiguration;
import com.openwt.officetracking.domain.device_device_configuration.DeviceDeviceConfigurationService;
import com.openwt.officetracking.domain.device_history.DeviceHistoryService;
import com.openwt.officetracking.domain.device_model.DeviceModelService;
import com.openwt.officetracking.domain.device_model_configuration_value.DeviceModelConfigurationValueService;
import com.openwt.officetracking.domain.device_status.DeviceStatus;
import com.openwt.officetracking.domain.device_type.DeviceTypeService;
import com.openwt.officetracking.domain.email.EmailService;
import com.openwt.officetracking.domain.user.UserService;
import com.openwt.officetracking.error.BadRequestException;
import com.openwt.officetracking.error.NotFoundException;
import com.openwt.officetracking.persistent.device.DeviceStore;
import com.openwt.officetracking.persistent.device_history.DeviceHistoryStore;
import com.openwt.officetracking.persistent.device_type.DeviceTypeStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import static com.openwt.officetracking.fake.DeviceAssignmentFakes.buildDeviceAssignment;
import static com.openwt.officetracking.fake.DeviceConfigurationFakes.buildDeviceConfiguration;
import static com.openwt.officetracking.fake.DeviceConfigurationFakes.buildDeviceConfigurationRequest;
import static com.openwt.officetracking.fake.DeviceConfigurationValueFakes.buildDeviceConfigurationValue;
import static com.openwt.officetracking.fake.DeviceDeviceConfigurationFakes.buildDeviceDeviceConfigurations;
import static com.openwt.officetracking.fake.DeviceFakes.*;
import static com.openwt.officetracking.fake.DeviceHistoryFakes.buildDeviceHistory;
import static com.openwt.officetracking.fake.DeviceModelConfigurationValueFakes.buildDeviceModelConfigurationValue;
import static com.openwt.officetracking.fake.DeviceModelFakes.buildDeviceModel;
import static com.openwt.officetracking.fake.DeviceModelFakes.buildDeviceModelResponse;
import static com.openwt.officetracking.fake.DeviceTypeFakes.buildDeviceType;
import static com.openwt.officetracking.fake.DeviceTypeFakes.buildDeviceTypeRequest;
import static com.openwt.officetracking.fake.UserFakes.buildUser;
import static java.time.Instant.now;
import static java.util.Collections.singletonList;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeviceServiceTest {

    @InjectMocks
    private DeviceService deviceService;

    @Mock
    private DeviceStore deviceStore;

    @Mock
    private DeviceHistoryService deviceHistoryService;

    @Mock
    private AuthsProvider authsProvider;

    @Mock
    private DeviceDeviceConfigurationService deviceDeviceConfigurationService;

    @Mock
    private DeviceTypeService deviceTypeService;

    @Mock
    private DeviceConfigurationService deviceConfigurationService;

    @Mock
    private EmailService emailService;

    @Mock
    private UserService userService;

    @Mock
    private DeviceTypeStore deviceTypeStore;

    @Mock
    private DeviceConfigurationValueService deviceConfigurationValueService;

    @Mock
    private DeviceHistoryStore deviceHistoryStore;


    @Mock
    private DeviceAssignmentService deviceAssignmentService;

    @Mock
    private DeviceModelService deviceModelService;

    @Mock
    private DeviceModelConfigurationValueService deviceModelConfigurationValueService;


    @Test
    void shouldFindById_OK() {
        final var device = buildDevice();

        when(deviceStore.findById(device.getId()))
                .thenReturn(Optional.of(device));

        final var actual = deviceService.findById(device.getId());

        assertEquals(device, actual);
        assertEquals(device.getId(), actual.getId());
        assertEquals(device.getModelId(), actual.getModelId());
        assertEquals(device.getDetail(), actual.getDetail());
        assertEquals(device.getSerialNumber(), actual.getSerialNumber());
        assertEquals(device.getPurchaseAt(), actual.getPurchaseAt());
        assertEquals(device.getWarrantyEndAt(), actual.getWarrantyEndAt());
        assertEquals(device.getDeviceStatus(), actual.getDeviceStatus());

        verify(deviceStore).findById(device.getId());
    }

    @Test
    void shouldUpdateDevice_OK() {
        final var deviceType = buildDeviceType();
        final var modelId = randomUUID();
        final var configurationId = randomUUID();
        final var configurationValueId = randomUUID();
        final var deviceConfigurationValue = buildDeviceConfigurationValue()
                .withId(configurationValueId);
        final var deviceConfiguration = buildDeviceConfiguration()
                .withId(configurationId)
                .withValues(singletonList(deviceConfigurationValue));
        final var modelResponse = buildDeviceModelResponse()
                .withId(modelId)
                .withConfigurations(singletonList(deviceConfiguration));
        final var model = buildDeviceModel()
                .withId(modelId)
                .withTypeId(deviceType.getId());
        final var device = buildDevice()
                .withModelId(model.getId());
        final var deviceRequest = buildDeviceRequest()
                .withPurchaseAt(now().minus(10, ChronoUnit.MINUTES))
                .withWarrantyEndAt(now().minus(10, ChronoUnit.MINUTES))
                .withDeviceTypeConfig(buildDeviceTypeRequest()
                        .withModelId(model.getId())
                        .withConfigurations(singletonList(buildDeviceConfigurationRequest()
                                .withId(deviceConfiguration.getId())
                                .withValueId(deviceConfigurationValue.getId()))));
        final var deviceHistory = buildDeviceHistory()
                .withAssignUserId(deviceRequest.getAssignUserId());
        final var deviceModelConfigurationValue = buildDeviceModelConfigurationValue();

        when(deviceStore.findById(device.getId()))
                .thenReturn(Optional.of(device));
        when(deviceTypeService.findById(any(UUID.class)))
                .thenReturn(deviceType);
        when(deviceConfigurationService.findById(any(UUID.class)))
                .thenReturn(deviceConfiguration);
        when(deviceConfigurationValueService.findById(any(UUID.class)))
                .thenReturn(deviceConfigurationValue);
        when(deviceDeviceConfigurationService.findByDeviceId(any(UUID.class)))
                .thenReturn(buildDeviceDeviceConfigurations());
        when(deviceModelConfigurationValueService.findById(any())).thenReturn(deviceModelConfigurationValue);
        when(deviceModelService.findById(deviceRequest.getDeviceTypeConfig().getModelId())).thenReturn(model);
        when(deviceModelService.findByModelId(any())).thenReturn(modelResponse);
        when(deviceHistoryService.createWithNewDevice(any(Device.class), any(DeviceRequest.class)))
                .thenReturn(deviceHistory);
        when(deviceStore.save(any(Device.class)))
                .thenReturn(device);

        final Device actual = deviceService.update(device.getId(), deviceRequest);

        assertEquals(device.getId(), actual.getId());
        assertEquals(device.getModelId(), actual.getModelId());
        assertEquals(device.getDeviceStatus(), actual.getDeviceStatus());
        assertEquals(deviceRequest.getPurchaseAt(), actual.getPurchaseAt());
        assertEquals(deviceRequest.getWarrantyEndAt(), actual.getWarrantyEndAt());

        verify(deviceStore, times(1)).findById(device.getId());
        verify(deviceTypeService, times(1)).findById(any(UUID.class));
        verify(deviceModelService, times(3)).findById(any());
        verify(deviceHistoryService).createWithNewDevice(any(Device.class), any(DeviceRequest.class));
        verify(deviceConfigurationService, times(1)).findById(any(UUID.class));
        verify(deviceConfigurationValueService, times(9)).findById(any(UUID.class));
        verify(deviceStore).save(any(Device.class));
        verify(deviceDeviceConfigurationService, times(3)).findByDeviceId(any(UUID.class));
    }

    @Test
    void shouldFindDetailById_OK() {
        final var device = buildDevice();
        final var deviceModelConfigurationValue = buildDeviceModelConfigurationValue();
        final var deviceDeviceConfigurations = buildDeviceDeviceConfigurations();

        when(deviceStore.findById(device.getId()))
                .thenReturn(Optional.of(device));
        when(deviceDeviceConfigurationService.findByDeviceId(device.getId()))
                .thenReturn(deviceDeviceConfigurations);
        when(deviceModelConfigurationValueService.findById(any()))
                .thenReturn(deviceModelConfigurationValue);

        final var actual = deviceService.findDetailById(device.getId());

        assertEquals(device.getId(), actual.getId());
        assertEquals(device.getModelId(), actual.getModelId());
        assertEquals(device.getDetail(), actual.getDetail());
        assertEquals(device.getSerialNumber(), actual.getSerialNumber());
        assertEquals(device.getPurchaseAt(), actual.getPurchaseAt());
        assertEquals(device.getWarrantyEndAt(), actual.getWarrantyEndAt());
        assertEquals(device.getDeviceStatus(), actual.getDeviceStatus());

        verify(deviceStore).findById(device.getId());
        verify(deviceDeviceConfigurationService).findByDeviceId(device.getId());
    }

    @Test
    public void shouldFindAllDevices_OK() {
        final var devices = buildDevices();

        when(deviceStore.findAllDevices()).thenReturn(devices);

        final var actual = deviceService.findAllDevices();

        assertEquals(devices.size(), actual.size());
        assertEquals(devices.get(0).getId(), actual.get(0).getId());
        assertEquals(devices.get(0).getAssignUserId(), actual.get(0).getAssignUserId());
        assertEquals(devices.get(0).getModelId(), actual.get(0).getModelId());
        assertEquals(devices.get(0).getRequestUserId(), actual.get(0).getRequestUserId());
        assertEquals(devices.get(0).getDetail(), actual.get(0).getDetail());
        assertEquals(devices.get(0).getSerialNumber(), actual.get(0).getSerialNumber());
        assertEquals(devices.get(0).getPurchaseAt(), actual.get(0).getPurchaseAt());
        assertEquals(devices.get(0).getWarrantyEndAt(), actual.get(0).getWarrantyEndAt());
        assertEquals(devices.get(0).getRequestNote(), actual.get(0).getRequestNote());
        assertEquals(devices.get(0).getRequestReason(), actual.get(0).getRequestReason());
        assertEquals(devices.get(0).getRequestedAt(), actual.get(0).getRequestedAt());
        assertEquals(devices.get(0).getAcceptedAt(), actual.get(0).getAcceptedAt());
        assertEquals(devices.get(0).getRejectedAt(), actual.get(0).getRejectedAt());
        assertEquals(devices.get(0).getCreatedAt(), actual.get(0).getCreatedAt());
        assertEquals(devices.get(0).getLastModifiedAt(), actual.get(0).getLastModifiedAt());

        verify(deviceStore).findAllDevices();
    }

    @Test
    public void shouldFindRequestsByAdmin_OK() {
        final var devices = buildDevices();

        devices.forEach(device -> device.setRequested(true));

        when(authsProvider.getCurrentUserRole()).thenReturn("ROLE_ADMIN");
        when(deviceStore.findAllRequests()).thenReturn(devices);

        final var actual = deviceService.findDeviceRequests();

        assertEquals(devices.size(), actual.size());
        assertEquals(devices.get(0).getId(), actual.get(0).getId());
        assertEquals(devices.get(0).getRequestUserId(), actual.get(0).getRequestUserId());
        assertEquals(devices.get(0).getModelId(), actual.get(0).getModelId());
        assertEquals(devices.get(0).getDetail(), actual.get(0).getDetail());
        assertEquals(devices.get(0).getRequestStatus(), actual.get(0).getRequestStatus());
        assertEquals(devices.get(0).getRequestNote(), actual.get(0).getRequestNote());
        assertEquals(devices.get(0).getRequestReason(), actual.get(0).getRequestReason());
        assertEquals(devices.get(0).getRequestedAt(), actual.get(0).getRequestedAt());
        assertEquals(devices.get(0).getAcceptedAt(), actual.get(0).getAcceptedAt());
        assertEquals(devices.get(0).getRejectedAt(), actual.get(0).getRejectedAt());
        assertEquals(devices.get(0).getCreatedAt(), actual.get(0).getCreatedAt());
        assertEquals(devices.get(0).getLastModifiedAt(), actual.get(0).getLastModifiedAt());

        verify(authsProvider).getCurrentUserRole();
        verify(deviceStore).findAllRequests();
    }

    @Test
    public void shouldFindRequestsByUser_OK() {
        final var currentUserId = randomUUID();
        final var devices = buildDevices();

        devices.forEach(device -> device.setRequested(true));

        when(authsProvider.getCurrentUserRole()).thenReturn("ROLE_USER");
        when(authsProvider.getCurrentUserId()).thenReturn(currentUserId);
        when(deviceStore.findAllRequests(currentUserId)).thenReturn(devices);

        final var actual = deviceService.findDeviceRequests();

        assertEquals(devices.size(), actual.size());
        assertEquals(devices.get(0).getId(), actual.get(0).getId());
        assertEquals(devices.get(0).getRequestUserId(), actual.get(0).getRequestUserId());
        assertEquals(devices.get(0).getModelId(), actual.get(0).getModelId());
        assertEquals(devices.get(0).getDetail(), actual.get(0).getDetail());
        assertEquals(devices.get(0).getRequestStatus(), actual.get(0).getRequestStatus());
        assertEquals(devices.get(0).getRequestNote(), actual.get(0).getRequestNote());
        assertEquals(devices.get(0).getRequestReason(), actual.get(0).getRequestReason());
        assertEquals(devices.get(0).getRequestedAt(), actual.get(0).getRequestedAt());
        assertEquals(devices.get(0).getAcceptedAt(), actual.get(0).getAcceptedAt());
        assertEquals(devices.get(0).getRejectedAt(), actual.get(0).getRejectedAt());
        assertEquals(devices.get(0).getCreatedAt(), actual.get(0).getCreatedAt());
        assertEquals(devices.get(0).getLastModifiedAt(), actual.get(0).getLastModifiedAt());

        verify(authsProvider).getCurrentUserRole();
        verify(authsProvider).getCurrentUserId();
        verify(deviceStore).findAllRequests(currentUserId);
    }

    @Test
    public void shouldFindById_ThrowDeviceNotFound() {
        final var device = buildDevice();

        when(deviceStore.findById(device.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> deviceService.findById(device.getId()));

        verify(deviceStore).findById(device.getId());
    }

    @Test
    public void shouldAdminAcceptRequestFromUser_OK() {
        final var deviceRequestAccept = buildDeviceRequestAccept();
        final var userId = randomUUID();
        final var deviceType = buildDeviceType();
        final var deviceModel = buildDeviceModel()
                .withTypeId(deviceType.getId());
        final var deviceRequest = buildDevice()
                .withId(deviceRequestAccept.getRequestId())
                .withModelId(deviceModel.getId())
                .withRequestUserId(userId);
        final var assignDevice = buildDevice()
                .withId(deviceRequestAccept.getDeviceId())
                .withModelId(deviceModel.getId())
                .withAssignUserId(userId);
        final var userRequest = buildUser();

        when(deviceStore.findById(deviceRequest.getId())).thenReturn(Optional.of(deviceRequest));
        when(deviceStore.findById(assignDevice.getId())).thenReturn(Optional.of(assignDevice));
        doNothing().when(deviceAssignmentService).createByAssignUserIdAndDeviceId(
                userId,
                userId,
                assignDevice.getId(),
                assignDevice.getDeviceStatus());
        doNothing().when(deviceHistoryService).createWithDeviceOnAssignment(assignDevice);
        when(deviceStore.save(assignDevice)).thenReturn(assignDevice);
        when(deviceStore.save(deviceRequest)).thenReturn(deviceRequest);
        when(deviceTypeService.findById(deviceModel.getTypeId())).thenReturn(deviceType);
        when(deviceModelService.findById(deviceRequest.getModelId())).thenReturn(deviceModel);
        when(userService.findById(deviceRequest.getRequestUserId())).thenReturn(userRequest);
        doNothing().when(emailService).sendRequestAcceptEmail(eq(deviceRequest), any(String.class), eq(deviceType), eq(deviceModel), eq(userRequest));

        final var actual = deviceService.acceptDeviceRequest(deviceRequestAccept);

        assertEquals(deviceRequest.getId(), actual.getId());
        assertEquals(deviceRequest.getAssignUserId(), actual.getAssignUserId());
        assertEquals(deviceRequest.getModelId(), actual.getModelId());
        assertEquals(deviceRequest.getRequestUserId(), actual.getRequestUserId());
        assertEquals(deviceRequest.getDetail(), actual.getDetail());
        assertEquals(deviceRequest.getRequestReason(), actual.getRequestReason());
        assertEquals(deviceRequest.getRequestedAt(), actual.getRequestedAt());
        assertEquals(deviceRequest.getAcceptedAt(), actual.getAcceptedAt());
        assertEquals(deviceRequest.getLastModifiedAt(), actual.getLastModifiedAt());
        assertEquals(deviceRequest.getRequestStatus(), actual.getRequestStatus());

        verify(deviceStore).findById(deviceRequest.getId());
        verify(deviceStore).findById(assignDevice.getId());
        verify(deviceAssignmentService).createByAssignUserIdAndDeviceId(
                userId,
                userId,
                assignDevice.getId(),
                assignDevice.getDeviceStatus());
        verify(deviceHistoryService).createWithDeviceOnAssignment(assignDevice);
        verify(deviceStore).save(assignDevice);
        verify(deviceStore).save(deviceRequest);
        verify(deviceTypeService).findById(deviceModel.getTypeId());
        verify(deviceModelService).findById(deviceRequest.getModelId());
        verify(userService).findById(deviceRequest.getRequestUserId());
        verify(emailService).sendRequestAcceptEmail(eq(deviceRequest), any(String.class), eq(deviceType), eq(deviceModel), eq(userRequest));
    }

    @Test
    public void shouldAdminRejectRequestFrom_OK() {
        final var deviceRequestReject = buildDeviceRequestReject();
        final var deviceType = buildDeviceType();
        final var deviceModel = buildDeviceModel()
                .withTypeId(deviceType.getId());
        final var deviceRequest = buildDevice()
                .withId(deviceRequestReject.getRequestId())
                .withModelId(deviceModel.getId());
        final var userRequest = buildUser();

        when(deviceStore.findById(deviceRequest.getId())).thenReturn(Optional.of(deviceRequest));
        when(deviceStore.save(deviceRequest)).thenReturn(deviceRequest);
        when(deviceModelService.findById(deviceRequest.getModelId())).thenReturn(deviceModel);
        when(deviceTypeService.findById(deviceModel.getTypeId())).thenReturn(deviceType);
        when(userService.findById(deviceRequest.getRequestUserId())).thenReturn(userRequest);
        doNothing().when(emailService).sendRequestRejectEmail(eq(deviceRequest), any(String.class), eq(deviceType), eq(deviceModel), eq(userRequest));

        final var actual = deviceService.rejectDeviceRequest(deviceRequestReject);

        assertEquals(deviceRequest.getId(), actual.getId());
        assertEquals(deviceRequest.getAssignUserId(), actual.getAssignUserId());
        assertEquals(deviceRequest.getModelId(), actual.getModelId());
        assertEquals(deviceRequest.getRequestUserId(), actual.getRequestUserId());
        assertEquals(deviceRequest.getDetail(), actual.getDetail());
        assertEquals(deviceRequest.getRequestNote(), actual.getRequestNote());
        assertEquals(deviceRequest.getRequestReason(), actual.getRequestReason());
        assertEquals(deviceRequest.getRequestedAt(), actual.getRequestedAt());
        assertEquals(deviceRequest.getRejectedAt(), actual.getRejectedAt());
        assertEquals(deviceRequest.getLastModifiedAt(), actual.getLastModifiedAt());
        assertEquals(deviceRequest.getRequestStatus(), actual.getRequestStatus());

        verify(deviceStore).findById(deviceRequest.getId());
        verify(deviceStore).save(deviceRequest);
        verify(deviceModelService).findById(deviceRequest.getModelId());
        verify(deviceTypeService).findById(deviceModel.getTypeId());
        verify(userService).findById(deviceRequest.getRequestUserId());
        verify(emailService).sendRequestRejectEmail(eq(deviceRequest), any(String.class), eq(deviceType), eq(deviceModel), eq(userRequest));
    }

    @Test
    public void shouldUserConfirmDeviceAssigned_OK() {
        final var confirmDeviceId = randomUUID();
        final var deviceType = buildDeviceType();
        final var deviceModel = buildDeviceModel();
        final var confirmUser = buildUser();
        final var device = buildDevice()
                .withId(confirmDeviceId)
                .withModelId(deviceModel.getId())
                .withAssignUserId(confirmUser.getId());
        final var deviceAssignment = buildDeviceAssignment()
                .withDeviceId(device.getId())
                .withUserId(device.getAssignUserId());

        when(deviceStore.findById(device.getId())).thenReturn(Optional.of(device));
        when(authsProvider.getCurrentUserId()).thenReturn(device.getAssignUserId());
        doNothing().when(deviceHistoryService).createWithDeviceOnAssignment(device);
        when(deviceStore.save(device)).thenReturn(device);
        when(deviceAssignmentService.findByUserIdAndDeviceId(device.getAssignUserId(), device.getId())).thenReturn(deviceAssignment);
        when(deviceAssignmentService.save(argThat(deviceAssignmentArg ->
                deviceAssignmentArg.getId().equals(deviceAssignment.getId())))).thenReturn(deviceAssignment);
        when(deviceModelService.findById(device.getModelId())).thenReturn(deviceModel);
        when(deviceTypeService.findById(any())).thenReturn(deviceType);
        when(userService.findById(device.getAssignUserId())).thenReturn(confirmUser);
        doNothing().when(emailService).sendConfirmDeviceEmail(eq(device), any(String.class), eq(deviceType), eq(deviceModel), eq(confirmUser));

        deviceService.confirmDevice(confirmDeviceId);

        verify(deviceStore).findById(device.getId());
        verify(authsProvider, times(2)).getCurrentUserId();
        verify(deviceHistoryService).createWithDeviceOnAssignment(device);
        verify(deviceStore).save(device);
        verify(deviceAssignmentService).findByUserIdAndDeviceId(device.getAssignUserId(), device.getId());
        verify(deviceAssignmentService).save(argThat(deviceAssignmentArg ->
                deviceAssignmentArg.getId().equals(deviceAssignment.getId())));
        verify(deviceTypeService).findById(any());
        verify(userService).findById(device.getAssignUserId());
        verify(emailService).sendConfirmDeviceEmail(eq(device), any(String.class), eq(deviceType), eq(deviceModel), eq(confirmUser));
    }

    @Test
    public void shouldUserConfirmDeviceAssigned_ThrowDeviceNotFound() {
        final var confirmDeviceId = randomUUID();

        when(deviceStore.findById(confirmDeviceId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> deviceService.confirmDevice(confirmDeviceId));

        verify(deviceStore).findById(confirmDeviceId);
    }

    @Test
    public void shouldUserConfirmDeviceAssigned_ThrowDeviceAssignedUserNotMatch() {
        final var confirmDeviceId = randomUUID();
        final var device = buildDevice()
                .withId(confirmDeviceId)
                .withDeviceStatus(DeviceStatus.AVAILABLE);

        when(deviceStore.findById(device.getId())).thenReturn(Optional.of(device));
        when(authsProvider.getCurrentUserId()).thenReturn(randomUUID());

        assertThrows(BadRequestException.class, () -> deviceService.confirmDevice(confirmDeviceId));

        verify(deviceStore).findById(device.getId());
        verify(authsProvider).getCurrentUserId();
    }

    @Test
    public void shouldUserConfirmDeviceAssigned_ThrowDeviceStatusMustBeAssign() {
        final var confirmDeviceId = randomUUID();
        final var device = buildDevice()
                .withId(confirmDeviceId)
                .withDeviceStatus(DeviceStatus.AVAILABLE);

        when(deviceStore.findById(device.getId())).thenReturn(Optional.of(device));
        when(authsProvider.getCurrentUserId()).thenReturn(device.getAssignUserId());

        assertThrows(BadRequestException.class, () -> deviceService.confirmDevice(confirmDeviceId));

        verify(deviceStore).findById(device.getId());
        verify(authsProvider).getCurrentUserId();
    }

    @Test
    public void shouldCreateDeviceRequest_OK() {
        final var deviceType = buildDeviceType();
        final var model = buildDeviceModel()
                .withTypeId(deviceType.getId());
        final var configurationId = randomUUID();
        final var configurationValueId = randomUUID();
        final var deviceRequest = buildDeviceRequest()
                .withDeviceTypeConfig(buildDeviceTypeRequest()
                        .withModelId(model.getId())
                        .withConfigurations(singletonList(buildDeviceConfigurationRequest()
                                .withId(configurationId)
                                .withValueId(configurationValueId))));
        final var deviceModelConfigurationValue = buildDeviceModelConfigurationValue();

        final var user = buildUser().withId(authsProvider.getCurrentUserId());
        final Device newDeviceRequest = buildDevice().withRequestUserId(user.getId());

        when(deviceTypeService.findById(any())).thenReturn(deviceType);
        when(deviceModelService.findById(any())).thenReturn(model);
        when(deviceModelConfigurationValueService.findByModelAndConfigurationAndValue(any(), any(), any())).thenReturn(deviceModelConfigurationValue);
        when(authsProvider.getCurrentUserId()).thenReturn(user.getId());
        when(deviceStore.save(any(Device.class))).thenReturn(newDeviceRequest);
        when(userService.findById(user.getId())).thenReturn(user);
        doNothing().when(emailService).sendRequestDeviceEmail(eq(newDeviceRequest), any(String.class), eq(deviceType), eq(model), eq(user));

        final Device actual = deviceService.createDeviceRequest(deviceRequest);

        assertEquals(newDeviceRequest.getId(), actual.getId());
        assertEquals(newDeviceRequest.getAssignUserId(), actual.getAssignUserId());

        verify(deviceTypeService).findById(any());
        verify(deviceModelService).findById(any());
        verify(authsProvider, times(2)).getCurrentUserId();
        verify(deviceStore, times(2)).save(any(Device.class));
        verify(deviceDeviceConfigurationService, times(1)).create(any(DeviceDeviceConfiguration.class)); // Customize this as needed
        verify(userService).findById(user.getId());
        verify(emailService).sendRequestDeviceEmail(eq(newDeviceRequest), any(String.class), eq(deviceType), eq(model), eq(user));
    }

    @Test
    public void shouldCreateDeviceRequest_ThrowDeviceTypeIdNotNull() {
        final var deviceTypeConfig = buildDeviceTypeRequest()
                .withModelId(null);
        final var deviceRequest = buildDeviceRequest()
                .withDeviceTypeConfig(deviceTypeConfig);

        assertThrows(BadRequestException.class, () -> deviceService.createDeviceRequest(deviceRequest));
    }

    @Test
    void shouldCreate_Ok() {
        final var deviceTypeId = randomUUID();
        final var deviceType = buildDeviceType()
                .withId(deviceTypeId);
        final var modelId = randomUUID();
        final var configurationId = randomUUID();
        final var configurationValueId = randomUUID();
        final var deviceConfigurationValue = buildDeviceConfigurationValue()
                .withId(configurationValueId);
        final var deviceConfiguration = buildDeviceConfiguration()
                .withId(configurationId)
                .withValues(singletonList(deviceConfigurationValue));
        final var deviceModelResponse = buildDeviceModelResponse()
                .withId(modelId)
                .withConfigurations(singletonList(deviceConfiguration));
        final var deviceRequest = buildDeviceRequest()
                .withDeviceTypeConfig(buildDeviceTypeRequest()
                        .withModelId(modelId)
                        .withConfigurations(singletonList(buildDeviceConfigurationRequest()
                                .withId(configurationId)
                                .withValueId(configurationValueId))));
        final var model = buildDeviceModel()
                .withId(modelId)
                .withTypeId(deviceTypeId);
        final var newDevice = buildDevice()
                .withModelId(modelId);
        final var deviceModelConfigurationValue = buildDeviceModelConfigurationValue();

        when(deviceStore.findBySerialNumber(any())).thenReturn(Optional.empty());
        when(deviceTypeService.findById(any())).thenReturn(deviceType);
        when(deviceModelService.findById(deviceRequest.getDeviceTypeConfig().getModelId())).thenReturn(model);
        when(deviceModelService.findByModelId(any())).thenReturn(deviceModelResponse);
        when(deviceModelConfigurationValueService.findByModelAndConfigurationAndValue(any(), any(), any())).thenReturn(deviceModelConfigurationValue);
        when(deviceStore.save(any(Device.class))).thenReturn(newDevice);

        final Device actual = deviceService.create(deviceRequest);

        assertEquals(newDevice.getId(), actual.getId());
        assertEquals(newDevice.getAssignUserId(), actual.getAssignUserId());
        assertEquals(newDevice.getDetail(), actual.getDetail());
        assertEquals(newDevice.getModelId(), actual.getModelId());
        assertEquals(newDevice.getSerialNumber(), actual.getSerialNumber());
        assertEquals(newDevice.getPurchaseAt(), actual.getPurchaseAt());
        assertEquals(newDevice.getWarrantyEndAt(), actual.getWarrantyEndAt());
        assertEquals(newDevice.getDeviceStatus(), actual.getDeviceStatus());
        assertEquals(newDevice.getRequestNote(), actual.getRequestNote());
        assertEquals(newDevice.getRequestReason(), actual.getRequestReason());
        assertEquals(newDevice.getCreatedAt(), actual.getCreatedAt());
        assertEquals(newDevice.getLastModifiedAt(), actual.getLastModifiedAt());

        verify(deviceStore, times(2)).save(any(Device.class));
        verify(deviceStore).findBySerialNumber(deviceRequest.getSerialNumber());
        verify(deviceTypeService, times(1)).findById(any());
        verify(deviceModelService, times(2)).findById(any());
        verify(deviceModelService, times(1)).findByModelId(any());
    }
}