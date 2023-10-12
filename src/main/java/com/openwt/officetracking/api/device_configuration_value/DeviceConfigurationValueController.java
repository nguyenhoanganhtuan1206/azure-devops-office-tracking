package com.openwt.officetracking.api.device_configuration_value;

import com.openwt.officetracking.domain.device_configuration_value.DeviceConfigurationValueService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.openwt.officetracking.api.device_configuration_value.DeviceConfigurationValueDTOMapper.toDeviceConfigurationValueRequest;
import static com.openwt.officetracking.api.device_configuration_value.DeviceConfigurationValueDTOMapper.toDeviceConfigurationValueResponseDTO;

@RestController
@RequestMapping("/api/v1/device-values")
@RequiredArgsConstructor
public class DeviceConfigurationValueController {

    private final DeviceConfigurationValueService deviceConfigurationValueService;

    @Operation(summary = "Create new device value")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public DeviceConfigurationValueResponseDTO create(@RequestBody final DeviceConfigurationValueRequestDTO deviceConfigurationValueRequestDTO) {
        return toDeviceConfigurationValueResponseDTO(deviceConfigurationValueService.create(toDeviceConfigurationValueRequest(deviceConfigurationValueRequestDTO)));
    }

    @Operation(summary = "Update device value")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("{valueId}")
    public DeviceConfigurationValueResponseDTO update(@PathVariable final UUID valueId, @RequestBody final DeviceConfigurationValueRequestDTO deviceConfigurationValueRequestDTO) {
        return toDeviceConfigurationValueResponseDTO(deviceConfigurationValueService.update(valueId, toDeviceConfigurationValueRequest(deviceConfigurationValueRequestDTO)));
    }

    @Operation(summary = "Delete device value")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("{valueId}")
    public void delete(@PathVariable final UUID valueId) {
        deviceConfigurationValueService.delete(valueId);
    }
}