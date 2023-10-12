package com.openwt.officetracking.api.device_model;

import com.openwt.officetracking.domain.device_model.DeviceModelService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.openwt.officetracking.api.device_model.DeviceModelDTOMapper.*;

@RestController
@RequestMapping("/api/v1/device-models")
@RequiredArgsConstructor
public class DeviceModelController {

    private final DeviceModelService deviceModelService;

    @Operation(summary = "Create new device model")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public DeviceModelResponseDTO create(@RequestBody final DeviceModelRequestDTO deviceModelRequestDTO) {
        return toDeviceModelResponseDTO(deviceModelService.create(toDeviceModel(deviceModelRequestDTO)));
    }

    @Operation(summary = "Find all device model info by type id")
    @GetMapping("/{typeId}/type")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<DeviceModelResponseDTO> findAllByTypeId(final @PathVariable UUID typeId) {
        return toDeviceModelResponseDTOs(deviceModelService.findByTypeId(typeId));
    }

    @Operation(summary = "Update device model")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("{modelId}")
    public DeviceModelResponseDTO update(@PathVariable final UUID modelId, @RequestBody final DeviceModelRequestDTO deviceModelRequestDTO) {
        return toDeviceModelResponseDTO(deviceModelService.update(modelId, toDeviceModel(deviceModelRequestDTO)));
    }

    @Operation(summary = "Delete device model")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("{modelId}")
    public void delete(@PathVariable final UUID modelId) {
        deviceModelService.delete(modelId);
    }
}