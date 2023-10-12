package com.openwt.officetracking.api.user;

import com.google.zxing.WriterException;
import com.openwt.officetracking.domain.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static com.openwt.officetracking.api.user.PasswordUpdateDTOMapper.toPasswordUpdate;
import static com.openwt.officetracking.api.user.UserDTOMapper.*;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Generate user's qrcode")
    @GetMapping("{id}/qrcode")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public byte[] generateQRCode(@PathVariable(name = "id") final UUID id) throws IOException, WriterException {

        return userService.generateQRCode(id);
    }

    @Operation(summary = "Find user by user id")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("{userId}")
    public UserResponseDTO findById(@PathVariable final UUID userId) {
        return toUserDTO(userService.findById(userId));
    }

    @Operation(summary = "Find all available users")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public List<UserResponseDTO> findAll() {
        return toUserDTOs(userService.findAll());
    }

    @Operation(summary = "Find users by first name, last name and email")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/search")
    public List<UserResponseDTO> findByName(@RequestParam final String searchTerm) {
        return toUserDTOs(userService.findByName(searchTerm));
    }

    @Operation(summary = "Create user")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public UserResponseDTO create(@RequestBody final UserRequestDTO userRequestDTO) {
        return toUserDTO(userService.create(toUser(userRequestDTO)));
    }

    @Operation(summary = "Update user")
    @PutMapping("{userId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public UserResponseDTO update(@PathVariable final UUID userId,
                                  @RequestBody final UserRequestDTO userRequestDTO) {
        return toUserDTO(userService.update(userId, toUser(userRequestDTO)));
    }

    @Operation(summary = "Update user's password")
    @PutMapping("/password")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public void updatePassword(@RequestBody final PasswordUpdateDTO passwordUpdateDTO) {

        userService.updatePassword(toPasswordUpdate(passwordUpdateDTO));
    }

    @Operation(summary = "Activate user")
    @PutMapping("{userId}/activate")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void activateById(@PathVariable final UUID userId) {
        userService.activateById(userId);
    }

    @Operation(summary = "Deactivate user")
    @PutMapping("{userId}/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void deactivateById(@PathVariable final UUID userId) {
        userService.deactivateById(userId);
    }
}
