package com.openwt.officetracking.api.feedback;

import com.openwt.officetracking.domain.feedback.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.openwt.officetracking.api.feedback.FeedbackDTOMapper.*;

@RestController
@RequestMapping(value = "/api/v1/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @Operation(summary = "Get all feedback of mentee in a course")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<FeedbackResponseDTO> findFeedbackMenteeByAdmin(@RequestParam final UUID courseId,
                                                               @RequestParam final UUID menteeId) {
        return toFeedbackResponseDTOs(feedbackService.findFeedbackMentee(courseId, menteeId));
    }

    @Operation(summary = "Create new feedback")
    @PostMapping
    @PreAuthorize("hasAnyRole('USER')")
    public FeedbackResponseDTO create(@RequestBody final FeedbackRequestDTO feedbackRequestDTO) {
        return toFeedbackResponse(feedbackService.create(toFeedbackRequest(feedbackRequestDTO)));
    }

    @Operation(summary = "Update mentor feedback")
    @PutMapping("{feedbackId}")
    @PreAuthorize("hasAnyRole('USER')")
    public FeedbackResponseDTO update(@PathVariable final UUID feedbackId,
                                      @RequestBody final FeedbackRequestDTO feedbackRequestDTO) {
        return toFeedbackResponse(feedbackService.update(feedbackId, toFeedbackRequest(feedbackRequestDTO)));
    }

    @Operation(summary = "Delete feedback")
    @DeleteMapping("{feedbackId}")
    @PreAuthorize("hasAnyRole('USER')")
    public void delete(@PathVariable final UUID feedbackId) {
        feedbackService.delete(feedbackId);
    }
}
