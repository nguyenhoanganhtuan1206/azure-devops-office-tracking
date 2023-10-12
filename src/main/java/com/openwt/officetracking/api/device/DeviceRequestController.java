package com.openwt.officetracking.api.device;

import com.openwt.officetracking.domain.device.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.openwt.officetracking.api.device.DeviceRequestAcceptDTOMapper.toDeviceRequestAccept;
import static com.openwt.officetracking.api.device.DeviceRequestDTOMapper.*;
import static com.openwt.officetracking.api.device.DeviceRequestRejectDTOMapper.toDeviceRequestReject;

@RestController
@RequestMapping("api/v1/device-requests")
@RequiredArgsConstructor
public class DeviceRequestController {

    private final DeviceService deviceService;

    @Operation(summary = "Create new device request from user")
    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping
    public DeviceRequestResponseDTO create(@RequestBody final DeviceRequestRequestDTO deviceRequestRequestDTO) {
        return toDeviceRequestResponseDTO(deviceService.createDeviceRequest(toDeviceRequestRequest(deviceRequestRequestDTO)));
    }

    @Operation(summary = "Get list device requests")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<DeviceRequestResponseDTO> findDeviceRequests() {
        return toDeviceRequestResponseDTOs(deviceService.findDeviceRequests());
    }

    @Operation(summary = "Admin accept device request from user")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/accept")
    public DeviceRequestResponseDTO acceptRequest(@RequestBody final DeviceRequestAcceptDTO deviceRequestAcceptDTO) {
        return toDeviceRequestResponseDTO(deviceService.acceptDeviceRequest(toDeviceRequestAccept(deviceRequestAcceptDTO)));
    }

    @Operation(summary = "Admin reject device request from user")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/reject")
    public DeviceRequestResponseDTO rejectRequest(@RequestBody final DeviceRequestRejectDTO deviceRequestRequestDTO) {
        return toDeviceRequestResponseDTO(deviceService.rejectDeviceRequest(toDeviceRequestReject(deviceRequestRequestDTO)));
    }
}
