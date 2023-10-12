package com.openwt.officetracking.domain.feedback;

import com.openwt.officetracking.domain.auth.AuthsProvider;
import com.openwt.officetracking.domain.course_assignment.CourseAssignment;
import com.openwt.officetracking.domain.course_assignment.CourseAssignmentService;
import com.openwt.officetracking.domain.user.User;
import com.openwt.officetracking.domain.user.UserService;
import com.openwt.officetracking.error.BadRequestException;
import com.openwt.officetracking.persistent.feedback.FeedbackStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static com.openwt.officetracking.domain.feedback.FeedbackError.supplyFeedbackNotFound;
import static com.openwt.officetracking.domain.feedback.FeedbackValidation.validateFeedback;
import static com.openwt.officetracking.error.CommonError.supplyValidationError;
import static java.time.Instant.now;
import static java.util.stream.Collectors.toSet;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackStore feedbackStore;

    private final CourseAssignmentService courseAssignmentService;

    private final AuthsProvider authsProvider;

    private final UserService userService;

    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    public List<Feedback> findFeedbackMentee(final UUID courseId, final UUID menteeId) {
        final String userRole = authsProvider.getCurrentUserRole();
        final Set<UUID> existingMentors = courseAssignmentService.findByMenteeIdAndCourseId(menteeId,courseId).stream()
                .map(CourseAssignment::getMentorId)
                .distinct()
                .filter(Objects::nonNull)
                .collect(toSet());

        if (userRole.equals(ROLE_ADMIN)) {
            return feedbackStore.findFeedbackMenteeInCourseByAdmin(courseId, menteeId)
                    .stream()
                    .map(this::setReviewerName)
                    .toList();
        }

        final User user = userService.findById(authsProvider.getCurrentUserId());

        if (!existingMentors.contains(user.getId())) {
            return feedbackStore.findFeedbackMenteeInCourseByCoach(courseId, user.getId(), menteeId)
                    .stream()
                    .filter(feedback -> !feedback.isMentor())
                    .map(this::setReviewerName)
                    .toList();
        }

        return feedbackStore.findFeedbackMenteeInCourseByMentor(courseId, user.getId(), menteeId)
                .stream()
                .filter(Feedback::isMentor)
                .map(this::setReviewerName)
                .toList();
    }
    
    public Feedback findById(final UUID feedbackId) {
        return feedbackStore.findById(feedbackId)
                .orElseThrow(supplyFeedbackNotFound("feedback id", feedbackId));
    }

    public Feedback create(final FeedbackRequest feedbackRequest) {
        validateFeedback(feedbackRequest);
        final UUID currentUserId = authsProvider.getCurrentUserId();

        final CourseAssignment courseAssignment = findCourseAssignment(currentUserId, feedbackRequest);

        validateMentorFeedbackPermission(courseAssignment, feedbackRequest);

        final Feedback newFeedback = Feedback.builder()
                .createdAt(now())
                .courseAssignmentId(courseAssignment.getId())
                .content(feedbackRequest.getContent())
                .isVisible(feedbackRequest.isVisible())
                .isMentor(true)
                .build();

        if (feedbackRequest.isCoach()) {
            newFeedback.setMentor(false);
        }

        return feedbackStore.save(newFeedback);
    }

    public Feedback update(final UUID feedbackId, final FeedbackRequest feedbackRequest) {
        validateFeedback(feedbackRequest);

        final Feedback currentFeedback = findById(feedbackId);

        currentFeedback.setContent(feedbackRequest.getContent());
        currentFeedback.setCreatedAt(now());
        currentFeedback.setVisible(feedbackRequest.isVisible());

        return feedbackStore.save(currentFeedback);
    }

    @Transactional
    public void delete(final UUID feedbackId) {
        feedbackStore.delete(feedbackId);
    }

    private CourseAssignment findCourseAssignment(final UUID currentUserId, final FeedbackRequest feedbackRequest) {
        if (feedbackRequest.isCoach()) {
            final List<CourseAssignment> courseAssignments = courseAssignmentService.findByCoachMenteeAndCourse(currentUserId, feedbackRequest.getMenteeId(), feedbackRequest.getCourseId());
                if (courseAssignments.isEmpty()) {
                    throw new BadRequestException("Course Assignment not exists for this mentee when you as coach");
                }
            return courseAssignments.get(0);
        }

        return courseAssignmentService.findByMentorMenteeAndCourse(currentUserId, feedbackRequest.getMenteeId(), feedbackRequest.getCourseId());
    }

    private void validateMentorFeedbackPermission(final CourseAssignment courseAssignment, final FeedbackRequest feedbackRequest) {
        if (courseAssignment == null) {
            throw new BadRequestException("Course assignment is null");
        }

        final UUID courseId = courseAssignment.getCourseId();
        final UUID menteeId = courseAssignment.getMenteeId();
        final Set<UUID> existingReviewers = courseAssignmentService.findByMenteeIdAndCourseId(menteeId, courseId).stream()
                .flatMap(assignment -> Stream.of(assignment.getMentorId(), assignment.getCoachId()))
                .distinct()
                .filter(Objects::nonNull)
                .collect(toSet());

        if (!menteeId.equals(feedbackRequest.getMenteeId()) && !existingReviewers.contains(authsProvider.getCurrentUserId())) {
            throw supplyValidationError("You do not have permission to comment feedback for this mentee.").get();
        }
    }

    private Feedback setReviewerName(final Feedback feedback) {
        final CourseAssignment courseAssignment = courseAssignmentService.findById(feedback.getCourseAssignmentId());
        final UUID reviewerId = feedback.isMentor() ? courseAssignment.getMentorId() : courseAssignment.getCoachId();
        final User reviewer = userService.findById(reviewerId);

        return feedback
                .withReviewerId(reviewer.getId())
                .withFirstName(reviewer.getFirstName())
                .withLastName(reviewer.getLastName());
    }
}
