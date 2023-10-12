package com.openwt.officetracking.api.profile;

import com.openwt.officetracking.api.user.UserRequestDTO;
import com.openwt.officetracking.api.user.UserResponseDTO;
import com.openwt.officetracking.domain.auth.AuthsProvider;
import com.openwt.officetracking.domain.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.openwt.officetracking.api.profile.UserProfileUpdateDTOMapper.toUserProfileUpdateDTO;
import static com.openwt.officetracking.api.user.UserDTOMapper.*;

@RestController
@RequestMapping("api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    private final AuthsProvider authsProvider;

    @Operation(summary = "Update current user's profile")
    @PreAuthorize("hasAnyRole('USER')")
    @PutMapping
    public UserResponseDTO updateProfile(final @RequestBody UserRequestDTO userRequestDTO) {
        return toUserDTO(userService.update(authsProvider.getCurrentUserId(), toUser(userRequestDTO)));
    }

    @Operation(summary = "Update current user's profile in mobile")
    @PreAuthorize("hasAnyRole('USER')")
    @PutMapping("mobile")
    public UserProfileUpdateDTO updateProfileMobile(final @RequestBody UserRequestDTO userRequestDTO) {
        return toUserProfileUpdateDTO(userService.updateProfileMobile(authsProvider.getCurrentUserId(), toUser(userRequestDTO)));
    }
}