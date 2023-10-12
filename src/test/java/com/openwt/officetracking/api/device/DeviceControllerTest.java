package com.openwt.officetracking.api.device;

import com.openwt.officetracking.api.AbstractControllerTest;
import com.openwt.officetracking.api.WithMockAdmin;
import com.openwt.officetracking.api.WithMockUser;
import com.openwt.officetracking.domain.auth.AuthsProvider;
import com.openwt.officetracking.domain.device.DeviceRequest;
import com.openwt.officetracking.domain.device.DeviceService;
import com.openwt.officetracking.domain.device_history.DeviceHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static com.openwt.officetracking.domain.device.DeviceMapper.toDeviceDetails;
import static com.openwt.officetracking.fake.DeviceFakes.*;
import static com.openwt.officetracking.fake.DeviceHistoryFakes.buildDeviceHistories;
import static java.util.UUID.randomUUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeviceController.class)
@ExtendWith(SpringExtension.class)
class DeviceControllerTest extends AbstractControllerTest {

    private static final String BASE_URL = "/api/v1/devices";

    @MockBean
    private AuthsProvider authsProvider;

    @MockBean
    private DeviceService deviceService;

    @MockBean
    private DeviceHistoryService deviceHistoryService;

    @BeforeEach
    void init() {
        when(authsProvider.getCurrentAuthentication()).thenCallRealMethod();
    }

    @Test
    @WithMockAdmin
    void shouldFindById_OK() throws Exception {
        final var device = buildDeviceDetailResponse();

        when(deviceService.findDetailById(device.getId())).thenReturn(device);

        get(BASE_URL + "/" + device.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(device.getId().toString()))
                .andExpect(jsonPath("$.modelId").value(device.getModelId().toString()))
                .andExpect(jsonPath("$.detail").value(device.getDetail()))
                .andExpect(jsonPath("$.serialNumber").value(device.getSerialNumber()))
                .andExpect(jsonPath("$.purchaseAt").value(device.getPurchaseAt().toString()))
                .andExpect(jsonPath("$.warrantyEndAt").value(device.getWarrantyEndAt().toString()))
                .andExpect(jsonPath("$.deviceStatus").value(device.getDeviceStatus().toString()));

        verify(deviceService).findDetailById(device.getId());
    }

    @Test
    @WithMockAdmin
    void shouldFindAllDevices_OK() throws Exception {
        final var devices = buildDevices();

        when(deviceService.findAllDevices()).thenReturn(devices);

        get(BASE_URL)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(devices.size()))
                .andExpect(jsonPath("$[0].id").value(devices.get(0).getId().toString()))
                .andExpect(jsonPath("$[0].assignUserId").value(devices.get(0).getAssignUserId().toString()))
                .andExpect(jsonPath("$[0].detail").value(devices.get(0).getDetail()))
                .andExpect(jsonPath("$[0].modelId").value(devices.get(0).getModelId().toString()))
                .andExpect(jsonPath("$[0].serialNumber").value(devices.get(0).getSerialNumber()))
                .andExpect(jsonPath("$[0].purchaseAt").value(devices.get(0).getPurchaseAt().toString()))
                .andExpect(jsonPath("$[0].deviceStatus").value(devices.get(0).getDeviceStatus().toString()))
                .andExpect(jsonPath("$[0].createdAt").value(devices.get(0).getCreatedAt().toString()));

        verify(deviceService).findAllDevices();
    }

    @Test
    @WithMockUser
    void shouldFindDevicesAssignedToCurrentUser_OK() throws Exception {
        final var devices = buildDevices();

        when(deviceService.findMyDevices()).thenReturn(toDeviceDetails(devices));

        get(BASE_URL + "/mine")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(devices.size()))
                .andExpect(jsonPath("$[0].id").value(devices.get(0).getId().toString()))
                .andExpect(jsonPath("$[0].modelId").value(devices.get(0).getModelId().toString()))
                .andExpect(jsonPath("$[0].serialNumber").value(devices.get(0).getSerialNumber()))
                .andExpect(jsonPath("$[0].purchaseAt").value(devices.get(0).getPurchaseAt().toString()))
                .andExpect(jsonPath("$[0].deviceStatus").value(devices.get(0).getDeviceStatus().toString()));

        verify(deviceService).findMyDevices();
    }

    @Test
    @WithMockAdmin
    void shouldUserConfirmDeviceAssigned_OK() throws Exception {
        final var confirmDeviceId = randomUUID();

        doNothing().when(deviceService).confirmDevice(confirmDeviceId);

        put(BASE_URL + "/" + confirmDeviceId + "/confirm/", null)
                .andExpect(status().isOk());

        verify(deviceService).confirmDevice(confirmDeviceId);
    }

    @Test
    @WithMockAdmin
    void shouldUpdate_OK() throws Exception {
        final var deviceId = randomUUID();
        final var deviceRequestDTO = buildDeviceRequestDTO();
        final var device = buildDevice()
                .withId(deviceId)
                .withModelId(deviceRequestDTO.getDeviceTypeConfig().getModelId());

        when(deviceService.update(any(UUID.class), any(DeviceRequest.class)))
                .thenReturn(device);

        put(BASE_URL + "/" + deviceId, deviceRequestDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(device.getId().toString()))
                .andExpect(jsonPath("$.modelId").value(device.getModelId().toString()))
                .andExpect(jsonPath("$.detail").value(device.getDetail()))
                .andExpect(jsonPath("$.serialNumber").value(device.getSerialNumber()))
                .andExpect(jsonPath("$.purchaseAt").value(device.getPurchaseAt().toString()))
                .andExpect(jsonPath("$.warrantyEndAt").value(device.getWarrantyEndAt().toString()))
                .andExpect(jsonPath("$.deviceStatus").value(device.getDeviceStatus().toString()))
                .andExpect(jsonPath("$.assignUserId").value(device.getAssignUserId().toString()))
                .andExpect(jsonPath("$.note").value(device.getRequestNote()))
                .andExpect(jsonPath("$.reason").value(device.getRequestReason()))
                .andExpect(jsonPath("$.createdAt").value(device.getCreatedAt().toString()))
                .andExpect(jsonPath("$.lastModifiedAt").value(device.getLastModifiedAt().toString()));

        verify(deviceService).update(any(UUID.class), any(DeviceRequest.class));
    }

    @Test
    @WithMockAdmin
    void shouldFindByDeviceId_OK() throws Exception {
        final var deviceId = randomUUID();
        final var deviceHistories = buildDeviceHistories();

        when(deviceHistoryService.findByDeviceId(deviceId))
                .thenReturn(deviceHistories);

        get(BASE_URL + "/" + deviceId + "/histories")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(deviceHistories.get(0).getId().toString()))
                .andExpect(jsonPath("$[0].deviceId").value(deviceHistories.get(0).getDeviceId().toString()))
                .andExpect(jsonPath("$[0].assignUserId").value(deviceHistories.get(0).getAssignUserId().toString()))
                .andExpect(jsonPath("$[0].deviceStatus").value(deviceHistories.get(0).getDeviceStatus().toString()))
                .andExpect(jsonPath("$[0].condition").value(deviceHistories.get(0).getCondition()))
                .andExpect(jsonPath("$[0].note").value(deviceHistories.get(0).getNote()))
                .andExpect(jsonPath("$[0].previousUpdateTime").value(deviceHistories.get(0).getPreviousUpdateTime().toString()))
                .andExpect(jsonPath("$[0].latestUpdateTime").value(deviceHistories.get(0).getLatestUpdateTime().toString()));

        verify(deviceHistoryService).findByDeviceId(deviceId);
    }

    @Test
    @WithMockAdmin
    void shouldCreate_OK() throws Exception {
        final var deviceId = randomUUID();
        final var deviceRequestDTO = buildDeviceRequestDTO();
        final var device = buildDevice()
                .withId(deviceId)
                .withModelId(deviceRequestDTO.getDeviceTypeConfig().getModelId());

        when(deviceService.create(any(DeviceRequest.class))).thenReturn(device);

        post(BASE_URL, deviceRequestDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(device.getId().toString()))
                .andExpect(jsonPath("$.modelId").value(device.getModelId().toString()))
                .andExpect(jsonPath("$.detail").value(device.getDetail()))
                .andExpect(jsonPath("$.serialNumber").value(device.getSerialNumber()))
                .andExpect(jsonPath("$.purchaseAt").value(device.getPurchaseAt().toString()))
                .andExpect(jsonPath("$.warrantyEndAt").value(device.getWarrantyEndAt().toString()))
                .andExpect(jsonPath("$.deviceStatus").value(device.getDeviceStatus().toString()))
                .andExpect(jsonPath("$.assignUserId").value(device.getAssignUserId().toString()))
                .andExpect(jsonPath("$.note").value(device.getRequestNote()))
                .andExpect(jsonPath("$.reason").value(device.getRequestReason()))
                .andExpect(jsonPath("$.createdAt").value(device.getCreatedAt().toString()))
                .andExpect(jsonPath("$.lastModifiedAt").value(device.getLastModifiedAt().toString()));

        verify(deviceService).create(any(DeviceRequest.class));
    }
}