package com.openwt.officetracking.api.device_type;

import com.openwt.officetracking.domain.device_type.DeviceTypeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.openwt.officetracking.api.device_type.DeviceTypeDTOMapper.*;

@RestController
@RequestMapping("/api/v1/device-types")
@RequiredArgsConstructor
public class DeviceTypeController {

    private final DeviceTypeService deviceTypeService;

    @Operation(summary = "Find all information related to all types of devices")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping
    public List<DeviceTypeResponseDTO> findAll() {
        return toDeviceTypeResponseDTOs(deviceTypeService.findAllDeviceTypes());
    }

    @Operation(summary = "Create new device type")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public DeviceTypeDTO create(@RequestBody final DeviceTypeDTO deviceTypeDTO) {
        return toDeviceTypeDTO(deviceTypeService.create(toDeviceType(deviceTypeDTO)));
    }

    @Operation(summary = "Update device type")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping
    public DeviceTypeDTO update(@RequestBody final DeviceTypeDTO deviceTypeDTO) {
        return toDeviceTypeDTO(deviceTypeService.update(toDeviceType(deviceTypeDTO)));
    }

    @Operation(summary = "Delete device type")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("{typeId}")
    public void delete(@PathVariable final UUID typeId) {
        deviceTypeService.delete(typeId);
    }
}