package com.openwt.officetracking.api.course;

import com.openwt.officetracking.api.mentorship.MentorCourseResponseDTO;
import com.openwt.officetracking.api.mentorship.UserMentorshipResponseDTO;
import com.openwt.officetracking.domain.course.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.openwt.officetracking.api.course.CourseDTOMapper.toCourseDTOs;
import static com.openwt.officetracking.api.course.CourseDTOMapper.toCourseResponseDTO;
import static com.openwt.officetracking.api.course.CourseDetailResponseDTOMapper.toCourseDetailResponseDTOMapper;
import static com.openwt.officetracking.api.course.CourseRequestDTOMapper.toCourseRequest;
import static com.openwt.officetracking.api.mentorship.MentorCourseResponseDTOMapper.toMentorCourseResponseDTOs;
import static com.openwt.officetracking.api.mentorship.UserMentorshipDTOMapper.toUserMentorshipDTOs;

@RestController
@RequestMapping("api/v1/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @Operation(summary = "Find course by id")
    @GetMapping("{courseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public CourseDetailResponseDTO findById(final @PathVariable UUID courseId) {
        return toCourseDetailResponseDTOMapper(courseService.findDetailCourseById(courseId));
    }

    @Operation(summary = "Get all courses")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public List<CourseResponseDTO> findAll() {
        return toCourseDTOs(courseService.findAll());
    }

    @Operation(summary = "Get all mentee by reviewer id and course id")
    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("mentees")
    public List<UserMentorshipResponseDTO> findByReviewerAndCourse(@RequestParam final UUID courseId) {
        return toUserMentorshipDTOs(courseService.findByReviewerAndCourse(courseId));
    }

    @Operation(summary = "Get all reviewer by mentee id and course id")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("reviewer")
    public List<UserMentorshipResponseDTO> findByMenteeAndCourse(@RequestParam final UUID menteeId, @RequestParam final UUID courseId) {
        return toUserMentorshipDTOs(courseService.findByMenteeAndCourse(menteeId, courseId));
    }

    @Operation(summary = "Get all courses by mentor id")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("{mentorId}/mentor")
    public List<MentorCourseResponseDTO> findByMentorId(@PathVariable final UUID mentorId) {
        return toMentorCourseResponseDTOs(courseService.findByMentorId(mentorId));
    }

    @Operation(summary = "Create a new course")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public CourseResponseDTO create(@RequestBody final CourseRequestDTO courseRequestDTO) {
        return toCourseResponseDTO(courseService.create(toCourseRequest(courseRequestDTO)));
    }

    @Operation(summary = "Update course")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("{courseId}")
    public CourseResponseDTO update(@PathVariable final UUID courseId,
                                    @RequestBody final CourseRequestDTO courseRequestDTO) {
        return toCourseResponseDTO(courseService.update(courseId, toCourseRequest(courseRequestDTO)));
    }

    @Operation(summary = "Delete course")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("{courseId}")
    public void delete(@PathVariable final UUID courseId) {
        courseService.deleteById(courseId);
    }
}
