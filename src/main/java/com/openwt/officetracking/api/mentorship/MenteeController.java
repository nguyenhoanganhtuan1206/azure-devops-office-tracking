package com.openwt.officetracking.api.mentorship;

import com.openwt.officetracking.domain.course.CourseService;
import com.openwt.officetracking.domain.mentorship.MenteeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.openwt.officetracking.api.mentorship.MenteeCourseResponseDTOMapper.toMenteeCourseResponseDTOs;
import static com.openwt.officetracking.api.mentorship.UserMentorshipDTOMapper.toUserMentorshipDTO;
import static com.openwt.officetracking.api.mentorship.UserMentorshipDTOMapper.toUserMentorshipDTOs;

@RestController
@RequestMapping("api/v1/mentees")
@RequiredArgsConstructor
public class MenteeController {

    private final MenteeService menteeService;

    private final CourseService courseService;

    @Operation(summary = "Get all mentees")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public List<UserMentorshipResponseDTO> findAll() {
        return toUserMentorshipDTOs(menteeService.findAll());
    }

    @Operation(summary = "Get mentee by id")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("{menteeId}/detail")
    public UserMentorshipResponseDTO findMenteeDetailById(final @PathVariable UUID menteeId) {
        return toUserMentorshipDTO(menteeService.findById(menteeId));
    }

    @Operation(summary = "Assign user as mentee")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public UserMentorshipResponseDTO assignUserAsMentee(final @RequestBody UserMentorShipRequestDTO userMentorShipRequestDTO) {
         return  toUserMentorshipDTO(menteeService.assignUserAsMentee(userMentorShipRequestDTO.getUserId()));
    }

    @Operation(summary = "Get all courses by mentee id")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("{menteeId}")
    public List<MenteeCourseResponseDTO> findByMenteeId(@PathVariable final UUID menteeId) {
        return toMenteeCourseResponseDTOs(courseService.findByMenteeId(menteeId));
    }

    @Operation(summary = "Active a specific mentee")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("{menteeId}/activate")
    public void activeMentee(@PathVariable final UUID menteeId) {
        menteeService.activeByMenteeId(menteeId);
    }

    @Operation(summary = "Inactive a specific mentee")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("{menteeId}/inactivate")
    public void inactiveMentee(@PathVariable final UUID menteeId) {
        menteeService.inactiveByMenteeId(menteeId);
    }
}
