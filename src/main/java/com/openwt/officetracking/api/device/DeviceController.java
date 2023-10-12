package com.openwt.officetracking.api.device;

import com.openwt.officetracking.api.device_history.DeviceHistoryDTO;
import com.openwt.officetracking.domain.device.DeviceService;
import com.openwt.officetracking.domain.device_history.DeviceHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.openwt.officetracking.api.device.DeviceDTOMapper.*;
import static com.openwt.officetracking.api.device.DeviceResponseDTOMapper.toDeviceDetailResponseDTO;
import static com.openwt.officetracking.api.device.DeviceResponseDTOMapper.toDeviceDetailResponseDTOs;
import static com.openwt.officetracking.api.device_history.DeviceHistoryDTOMapper.toDeviceHistoryDTOs;

@RestController
@RequestMapping("api/v1/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    private final DeviceHistoryService deviceHistoryService;

    @Operation(summary = "Find device info by id")
    @GetMapping("{deviceId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public DeviceDetailResponseDTO findById(final @PathVariable UUID deviceId) {
        return toDeviceDetailResponseDTO(deviceService.findDetailById(deviceId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public DeviceResponseDTO create(final @RequestBody DeviceRequestDTO deviceRequestDTO) {
        return toDeviceResponseDTO(deviceService.create(toDeviceRequestDTO(deviceRequestDTO)));
    }

    @Operation(summary = "Find all devices")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public List<DeviceResponseDTO> findAllDevices() {
        return toDeviceResponseDTOs(deviceService.findAllDevices());
    }

    @Operation(summary = "Get all devices assigned to the currently user")
    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("mine")
    public List<DeviceDetailResponseDTO> findMyDevices() {
        return toDeviceDetailResponseDTOs(deviceService.findMyDevices());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{deviceId}")
    public DeviceResponseDTO update(@PathVariable final UUID deviceId, @RequestBody final DeviceRequestDTO deviceRequest) {
        return toDeviceResponseDTO(deviceService.update(deviceId, toDeviceRequestDTO(deviceRequest)));
    }

    @Operation(summary = "User confirms device is assigned for")
    @PreAuthorize("hasAnyRole('USER')")
    @PutMapping("{deviceId}/confirm")
    public void confirmDevice(@PathVariable final UUID deviceId) {
        deviceService.confirmDevice(deviceId);
    }

    @Operation(summary = "Find device histories by id")
    @GetMapping("{deviceId}/histories")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<DeviceHistoryDTO> findHistoriesByDeviceId(final @PathVariable UUID deviceId) {
        return toDeviceHistoryDTOs(deviceHistoryService.findByDeviceId(deviceId));
    }
}
