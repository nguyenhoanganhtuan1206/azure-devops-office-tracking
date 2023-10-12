package com.openwt.officetracking.api.user_mobile;

import com.openwt.officetracking.domain.user_mobile.UserMobileService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static com.openwt.officetracking.api.user_mobile.UserMobileDTOMapper.*;
import static com.openwt.officetracking.api.user_mobile.UserMobileDTOMapper.toUserMobileDTOs;
import static com.openwt.officetracking.api.user_mobile.UserMobileDTOMapper.toUserMobileDetailDTO;

@RestController
@RequestMapping("api/v1/user-mobiles")
@RequiredArgsConstructor
public class UserMobileController {

    private final UserMobileService userMobileService;

    @Operation(summary = "Find all employee mobile devices")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<UserMobileDTO> findAll() {
        return toUserMobileDTOs(userMobileService.findAll());
    }

    @Operation(summary = "Find employee mobile device detail by id")
    @GetMapping({"{id}"})
    @PreAuthorize("hasAnyRole('ADMIN')")
    public UserMobileDetailDTO findById(@PathVariable final UUID id) {
        return toUserMobileDetailDTO(userMobileService.findDetailById(id));
    }

    @Operation(summary = "Update employee mobile device information")
    @PutMapping
    @PreAuthorize("hasAnyRole('USER')")
    public UpdateUserMobileDTO update(@RequestBody final UpdateUserMobileDTO updateUserMobileDTO) {
        return toUpdateUserMobileDTO(userMobileService.update(toUserMobile(updateUserMobileDTO)));
    }

    @Operation(summary = "Update employee mobile device permissions")
    @PutMapping("permissions")
    @PreAuthorize("hasAnyRole('USER')")
    public UpdateMobilePermissionDTO updatePermissions(@RequestBody final UpdateMobilePermissionDTO updateMobilePermissionDTO) {
        return toUpdateMobilePermissionDTO(userMobileService.updateMobilePermissions(toUpdateMobilePermission(updateMobilePermissionDTO)));
    }
}
