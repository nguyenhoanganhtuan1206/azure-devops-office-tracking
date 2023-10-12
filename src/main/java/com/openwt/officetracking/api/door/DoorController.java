package com.openwt.officetracking.api.door;

import com.openwt.officetracking.domain.door.DoorService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.openwt.officetracking.api.door.DoorDTOMapper.*;

@RestController
@RequestMapping("api/v1/beacon-doors")
@RequiredArgsConstructor
public class DoorController {

    private final DoorService doorService;

    @Operation(summary = "Find all doors")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<DoorResponseDTO> findAll() {
        return toDoorDTOs(doorService.findAll());
    }

    @Operation(summary = "Create new door for beacon")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public DoorResponseDTO create(final @RequestBody DoorRequestDTO doorRequestDTO) {
        return toDoorDTO(doorService.save(toDoor(doorRequestDTO)));
    }

    @Operation(summary = "Find door by id")
    @GetMapping("{doorId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public DoorResponseDTO findById(final @PathVariable UUID doorId) {
        return toDoorDTO(doorService.findById(doorId));
    }

    @Operation(summary = "Update door")
    @PutMapping("{doorId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public DoorResponseDTO update(final @PathVariable UUID doorId, final @RequestBody DoorRequestDTO doorRequestDTO) {
        return toDoorDTO(doorService.update(doorId, toDoor(doorRequestDTO)));
    }

    @Operation(summary = "Delete door")
    @DeleteMapping("{doorId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void delete(final @PathVariable UUID doorId) {
        doorService.delete(doorId);
    }
}