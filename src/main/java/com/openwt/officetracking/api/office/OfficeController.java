package com.openwt.officetracking.api.office;

import com.openwt.officetracking.domain.office.OfficeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.openwt.officetracking.api.office.OfficeDTOMapper.*;

@RestController
@RequestMapping("api/v1/offices")
@RequiredArgsConstructor
public class OfficeController {

    private final OfficeService officeService;

    @Operation(summary = "Find all offices")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<OfficeResponseDTO> findAll() {
        return toOfficeResponseDTOs(officeService.findAll());
    }

    @Operation(summary = "Find office region by id")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("{id}")
    public OfficeResponseDTO findById(@PathVariable final UUID id) {
        return toOfficeResponseDTO(officeService.findById(id));
    }

    @Operation(summary = "Update office information")
    @PutMapping("{officeId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public OfficeResponseDTO update(@PathVariable final UUID officeId,
                                    @RequestBody final OfficeRequestDTO officeRequestDTO) {
        return toOfficeResponseDTO(officeService.update(officeId, toOffice(officeRequestDTO)));
    }
}