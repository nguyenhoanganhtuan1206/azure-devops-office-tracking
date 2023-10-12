package com.openwt.officetracking.api.auth;

import com.openwt.officetracking.domain.auth.AuthService;
import com.openwt.officetracking.domain.auth.JwtTokenService;
import com.openwt.officetracking.domain.auth.JwtUserDetails;
import com.openwt.officetracking.domain.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import static com.openwt.officetracking.api.auth.AuthUserResponseDTOMapper.toUserLoginDTO;
import static com.openwt.officetracking.api.auth.RegisterMobileDTOMapper.toRegisterMobile;

@RestController
@RequestMapping("api/v1/auths")
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenService jwtTokenUtil;

    private final AuthService authService;

    private final UserService userService;

    @Operation(summary = "Get information current user")
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public AuthUserResponseDTO getCurrentUser() {
        return toUserLoginDTO(userService.findByCurrentUserId());
    }

    @Operation(summary = "User login by email and password")
    @PostMapping("login")
    public JwtTokenResponseDTO login(final @RequestBody LoginDTO loginDTO) {
        return generateJwtTokenResponse(authService.login(loginDTO));
    }

    @Operation(summary = "User login by google account")
    @PostMapping("google")
    public JwtTokenResponseDTO loginGoogle(@RequestBody final TokenRequestDTO tokenRequestDTO) {
        return generateJwtTokenResponse(authService.login(tokenRequestDTO.getIdToken()));
    }

    @Operation(summary = "Get new authentication by refresh token")
    @PostMapping("/refresh-token")
    public JwtTokenResponseDTO generateRefreshToken() {
        return generateJwtTokenResponse(authService.refreshToken());
    }

    @Operation(summary = "Register mobile device")
    @PostMapping("/register-mobile")
    public JwtTokenResponseDTO registerDevice(@RequestBody final RegisterMobileDTO registerMobileDTO) {
        return generateJwtTokenResponse(authService.registerMobile(toRegisterMobile(registerMobileDTO)));
    }

    private JwtTokenResponseDTO generateJwtTokenResponse(final UserDetails userDetails) {
        return JwtTokenResponseDTO.builder()
                .accessToken(jwtTokenUtil.generateAccessToken((JwtUserDetails) userDetails))
                .refreshToken(jwtTokenUtil.generateRefreshToken((JwtUserDetails) userDetails))
                .build();
    }
}
