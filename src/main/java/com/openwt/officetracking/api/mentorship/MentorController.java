package com.openwt.officetracking.api.mentorship;

import com.openwt.officetracking.domain.mentorship.MentorService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.openwt.officetracking.api.mentorship.UserMentorshipDTOMapper.toUserMentorshipDTO;
import static com.openwt.officetracking.api.mentorship.UserMentorshipDTOMapper.toUserMentorshipDTOs;

@RestController
@RequestMapping("api/v1/mentors")
@RequiredArgsConstructor
public class MentorController {

    private final MentorService mentorService;

    @Operation(summary = "Get all mentors")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public List<UserMentorshipResponseDTO> findAll() {
        return toUserMentorshipDTOs(mentorService.findAll());
    }

    @Operation(summary = "Get mentor by id")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("{mentorId}/detail")
    public UserMentorshipResponseDTO findMentorDetailById(final @PathVariable UUID mentorId) {
        return toUserMentorshipDTO(mentorService.findById(mentorId));
    }

    @Operation(summary = "Assign user as mentor")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public UserMentorshipResponseDTO assignUserAsMentor(final @RequestBody UserMentorShipRequestDTO userMentorShipRequestDTO) {
        return toUserMentorshipDTO(mentorService.assignUserAsMentor(userMentorShipRequestDTO.getUserId()));
    }

    @Operation(summary = "Active a specific mentor")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("{mentorId}/activate")
    public void activeMentor(@PathVariable final UUID mentorId) {
        mentorService.activeByMentorId(mentorId);
    }

    @Operation(summary = "Inactive a specific mentor")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("{mentorId}/inactivate")
    public void inactiveMentor(@PathVariable final UUID mentorId) {
        mentorService.inactiveByMentorId(mentorId);
    }
}
