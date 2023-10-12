package com.openwt.officetracking.api.position;

import com.openwt.officetracking.domain.position.PositionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.openwt.officetracking.api.position.PositionDTOMapper.*;

@RestController
@RequestMapping("/api/v1/positions")
@RequiredArgsConstructor
public class PositionController {

    private final PositionService positionService;

    @Operation(summary = "Find all position")
    @GetMapping
    public List<PositionResponseDTO> findAll() {
        return toPositionResponseDTOs(positionService.findAll());
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Create a specific position")
    @PostMapping
    public PositionResponseDTO create(final @RequestBody PositionRequestDTO positionRequestDTO) {
        return toPositionResponseDTO(positionService.create(toPosition(positionRequestDTO)));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Update a specific position")
    @PutMapping("{id}")
    public PositionResponseDTO update(final @PathVariable UUID id, final @RequestBody PositionRequestDTO positionRequestDTO) {
        return toPositionResponseDTO(positionService.update(id, toPosition(positionRequestDTO)));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Delete a specific position")
    @DeleteMapping("{id}")
    public void delete(final @PathVariable UUID id) {
        positionService.delete(id);
    }
}