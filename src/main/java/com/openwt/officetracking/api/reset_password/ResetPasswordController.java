package com.openwt.officetracking.api.reset_password;

import com.openwt.officetracking.api.user.PasswordResetDTO;
import com.openwt.officetracking.domain.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.openwt.officetracking.api.user.PasswordResetDTOMapper.toPasswordReset;

@RestController
@RequestMapping("api/v1/reset-password")
@RequiredArgsConstructor
public class ResetPasswordController {

    private final UserService userService;

    @Operation(summary = "Send forgot password email")
    @PostMapping
    public void sendForgotPasswordEmail(@RequestBody final ForgotPasswordRequestDTO forgotPasswordRequestDTO) {
        userService.sendForgotPasswordEmail(forgotPasswordRequestDTO.getEmail());
    }

    @Operation(summary = "Check reset password code expiration")
    @GetMapping
    public void verifyResetPasswordCode(@RequestParam final String code) {
        userService.verifyCodeExpiration(code);
    }

    @Operation(summary = "Reset user's password")
    @PutMapping
    public void resetPassword(@RequestBody final PasswordResetDTO passwordResetDTO) {
        userService.resetPassword(toPasswordReset(passwordResetDTO));
    }
}
