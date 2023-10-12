package com.openwt.officetracking.api.device;

import com.openwt.officetracking.api.AbstractControllerTest;
import com.openwt.officetracking.api.WithMockAdmin;
import com.openwt.officetracking.api.WithMockUser;
import com.openwt.officetracking.domain.device.DeviceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.openwt.officetracking.fake.DeviceFakes.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeviceRequestController.class)
@ExtendWith(SpringExtension.class)
class DeviceRequestControllerTest extends AbstractControllerTest {

    private static final String BASE_URL = "/api/v1/device-requests";

    @MockBean
    private DeviceService deviceService;

    @Test
    @WithMockUser
    void shouldFindRequestsByUser_OK() throws Exception {
        final var devices = buildDevices();

        when(deviceService.findDeviceRequests()).thenReturn(devices);

        get(BASE_URL)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(devices.size()))
                .andExpect(jsonPath("$[0].id").value(devices.get(0).getId().toString()))
                .andExpect(jsonPath("$[0].requestUserId").value(devices.get(0).getRequestUserId().toString()))
                .andExpect(jsonPath("$[0].modelId").value(devices.get(0).getModelId().toString()))
                .andExpect(jsonPath("$[0].detail").value(devices.get(0).getDetail()))
                .andExpect(jsonPath("$[0].requestStatus").value(devices.get(0).getRequestStatus().toString()))
                .andExpect(jsonPath("$[0].note").value(devices.get(0).getRequestNote()))
                .andExpect(jsonPath("$[0].reason").value(devices.get(0).getRequestReason()))
                .andExpect(jsonPath("$[0].requestedAt").value(devices.get(0).getRequestedAt().toString()))
                .andExpect(jsonPath("$[0].acceptedAt").value(devices.get(0).getAcceptedAt().toString()))
                .andExpect(jsonPath("$[0].rejectedAt").value(devices.get(0).getRejectedAt().toString()))
                .andExpect(jsonPath("$[0].createdAt").value(devices.get(0).getCreatedAt().toString()))
                .andExpect(jsonPath("$[0].lastModifiedAt").value(devices.get(0).getLastModifiedAt().toString()));

        verify(deviceService).findDeviceRequests();
    }

    @Test
    @WithMockAdmin
    public void shouldAdminAcceptDeviceRequestFromUser_OK() throws Exception {
        final var deviceRequestAccept = buildDeviceRequestAccept();
        final var deviceRequest = buildDevice().withId(deviceRequestAccept.getRequestId());

        when(deviceService.acceptDeviceRequest(argThat(deviceRequestAcceptArg ->
                deviceRequestAcceptArg.getRequestId().equals(deviceRequestAccept.getRequestId()))))
                .thenReturn(deviceRequest);

        put(BASE_URL + "/accept", deviceRequestAccept)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(deviceRequest.getId().toString()))
                .andExpect(jsonPath("$.requestUserId").value(deviceRequest.getRequestUserId().toString()))
                .andExpect(jsonPath("$.modelId").value(deviceRequest.getModelId().toString()))
                .andExpect(jsonPath("$.detail").value(deviceRequest.getDetail()))
                .andExpect(jsonPath("$.requestStatus").value(deviceRequest.getRequestStatus().toString()))
                .andExpect(jsonPath("$.note").value(deviceRequest.getRequestNote()))
                .andExpect(jsonPath("$.reason").value(deviceRequest.getRequestReason()))
                .andExpect(jsonPath("$.requestedAt").value(deviceRequest.getRequestedAt().toString()))
                .andExpect(jsonPath("$.acceptedAt").value(deviceRequest.getAcceptedAt().toString()))
                .andExpect(jsonPath("$.rejectedAt").value(deviceRequest.getRejectedAt().toString()))
                .andExpect(jsonPath("$.createdAt").value(deviceRequest.getCreatedAt().toString()))
                .andExpect(jsonPath("$.lastModifiedAt").value(deviceRequest.getLastModifiedAt().toString()));

        verify(deviceService).acceptDeviceRequest(argThat(deviceRequestAcceptArg ->
                deviceRequestAcceptArg.getRequestId().equals(deviceRequestAccept.getRequestId())));
    }

    @Test
    @WithMockAdmin
    public void shouldAdminRejectDeviceRequestFromUser_OK() throws Exception {
        final var deviceRequestReject = buildDeviceRequestReject();
        final var deviceRequest = buildDevice().withId(deviceRequestReject.getRequestId());

        when(deviceService.rejectDeviceRequest(argThat(deviceRequestRejectArg ->
                deviceRequestRejectArg.getRequestId().equals(deviceRequestReject.getRequestId()))))
                .thenReturn(deviceRequest);

        put(BASE_URL + "/reject", deviceRequestReject)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(deviceRequest.getId().toString()))
                .andExpect(jsonPath("$.requestUserId").value(deviceRequest.getRequestUserId().toString()))
                .andExpect(jsonPath("$.modelId").value(deviceRequest.getModelId().toString()))
                .andExpect(jsonPath("$.detail").value(deviceRequest.getDetail()))
                .andExpect(jsonPath("$.requestStatus").value(deviceRequest.getRequestStatus().toString()))
                .andExpect(jsonPath("$.note").value(deviceRequest.getRequestNote()))
                .andExpect(jsonPath("$.reason").value(deviceRequest.getRequestReason()))
                .andExpect(jsonPath("$.requestedAt").value(deviceRequest.getRequestedAt().toString()))
                .andExpect(jsonPath("$.acceptedAt").value(deviceRequest.getAcceptedAt().toString()))
                .andExpect(jsonPath("$.rejectedAt").value(deviceRequest.getRejectedAt().toString()))
                .andExpect(jsonPath("$.createdAt").value(deviceRequest.getCreatedAt().toString()))
                .andExpect(jsonPath("$.lastModifiedAt").value(deviceRequest.getLastModifiedAt().toString()));

        verify(deviceService).rejectDeviceRequest(argThat(deviceRequestRejectArg ->
                deviceRequestRejectArg.getRequestId().equals(deviceRequestReject.getRequestId())));
    }

    @Test
    @WithMockUser
    public void shouldCreate_OK() throws Exception {
        final var device = buildDevice();
        final var deviceRequest = buildDeviceRequestRequestDTO();

        when(deviceService.createDeviceRequest(argThat(deviceArg -> deviceArg.getDeviceTypeConfig().getModelId().equals(deviceRequest.getDeviceTypeConfig().getModelId()))))
                .thenReturn(device);

        post(BASE_URL, deviceRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(device.getId().toString()))
                .andExpect(jsonPath("$.requestUserId").value(device.getRequestUserId().toString()))
                .andExpect(jsonPath("$.modelId").value(device.getModelId().toString()))
                .andExpect(jsonPath("$.detail").value(device.getDetail()))
                .andExpect(jsonPath("$.requestStatus").value(device.getRequestStatus().toString()))
                .andExpect(jsonPath("$.note").value(device.getRequestNote()))
                .andExpect(jsonPath("$.reason").value(device.getRequestReason()))
                .andExpect(jsonPath("$.requestedAt").value(device.getRequestedAt().toString()))
                .andExpect(jsonPath("$.acceptedAt").value(device.getAcceptedAt().toString()))
                .andExpect(jsonPath("$.rejectedAt").value(device.getRejectedAt().toString()))
                .andExpect(jsonPath("$.createdAt").value(device.getCreatedAt().toString()))
                .andExpect(jsonPath("$.lastModifiedAt").value(device.getLastModifiedAt().toString()));

        verify(deviceService).createDeviceRequest(argThat(deviceArg -> deviceArg.getDeviceTypeConfig().getModelId().equals(deviceRequest.getDeviceTypeConfig().getModelId())));
    }
}