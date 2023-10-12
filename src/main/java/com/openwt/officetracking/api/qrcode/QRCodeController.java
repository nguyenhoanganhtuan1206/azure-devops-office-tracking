package com.openwt.officetracking.api.qrcode;

import com.openwt.officetracking.api.user.UserResponseDTO;
import com.openwt.officetracking.domain.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.openwt.officetracking.api.user.UserDTOMapper.toUserDTO;

@RestController
@RequestMapping("/api/v1/qrcode")
@RequiredArgsConstructor
public class QRCodeController {

    private final UserService userService;

    @Operation(summary = "Get user info from qrcode")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping
    public UserResponseDTO getUser(@RequestParam("q") final String qrCode) {

        return toUserDTO(userService.getUserByQRCode(qrCode));
    }
}
