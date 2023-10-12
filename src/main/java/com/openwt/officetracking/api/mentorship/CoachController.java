package com.openwt.officetracking.api.mentorship;

import com.openwt.officetracking.domain.course.CourseService;
import com.openwt.officetracking.domain.mentorship.CoachService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.openwt.officetracking.api.mentorship.CoachCourseResponseDTOMapper.toCoachCourseResponseDTOs;
import static com.openwt.officetracking.api.mentorship.UserMentorshipDTOMapper.toUserMentorshipDTO;
import static com.openwt.officetracking.api.mentorship.UserMentorshipDTOMapper.toUserMentorshipDTOs;

@RestController
@RequestMapping("api/v1/coaches")
@RequiredArgsConstructor
public class CoachController {

    private final CoachService coachService;

    private final CourseService courseService;

    @Operation(summary = "Assign user as coach")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public UserMentorshipResponseDTO assignUserAsCoach(final @RequestBody UserMentorShipRequestDTO userMentorShipRequestDTO) {
        return  toUserMentorshipDTO(coachService.assignUserAsCoach(userMentorShipRequestDTO.getUserId()));
    }

    @Operation(summary = "Get all coaches")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public List<UserMentorshipResponseDTO> findAll() {
        return toUserMentorshipDTOs(coachService.findAll());
    }

    @Operation(summary = "Get coach by id")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("{coachId}/detail")
    public UserMentorshipResponseDTO findDetailById(final @PathVariable UUID coachId) {
        return toUserMentorshipDTO(coachService.findById(coachId));
    }

    @Operation(summary = "Get all courses by coach id")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("{coachId}/courses")
    public List<CoachCourseResponseDTO> findCourseByCoachId(@PathVariable final UUID coachId) {
        return toCoachCourseResponseDTOs(courseService.findByCoachId(coachId));
    }

    @Operation(summary = "Active a specific coach")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("{coachId}/activate")
    public void activeCoach(@PathVariable final UUID coachId) {
        coachService.activeByCoachId(coachId);
    }

    @Operation(summary = "Inactive a specific coach")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("{coachId}/inactivate")
    public void inactiveCoach(@PathVariable final UUID coachId) {
        coachService.inactiveByCoachId(coachId);
    }
}
