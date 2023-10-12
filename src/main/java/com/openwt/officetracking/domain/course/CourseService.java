package com.openwt.officetracking.domain.course;

import com.openwt.officetracking.domain.ability_result.AbilityResultService;
import com.openwt.officetracking.domain.ability_result.MentorAbilityResult;
import com.openwt.officetracking.domain.auth.AuthsProvider;
import com.openwt.officetracking.domain.course_assign.CourseAssign;
import com.openwt.officetracking.domain.course_assignment.CourseAssignment;
import com.openwt.officetracking.domain.course_assignment.CourseAssignmentService;
import com.openwt.officetracking.domain.course_status.CourseStatus;
import com.openwt.officetracking.domain.mentorship.UserMentorShip;
import com.openwt.officetracking.domain.mentorship_status.CoachMentorMenteeStatus;
import com.openwt.officetracking.domain.user.User;
import com.openwt.officetracking.domain.user.UserService;
import com.openwt.officetracking.persistent.course.CourseStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.*;

import static com.openwt.officetracking.domain.course.CourseDetailResponseMapper.toCourseDetailResponse;
import static com.openwt.officetracking.domain.course.CourseError.*;
import static com.openwt.officetracking.domain.course.CourseMapper.*;
import static com.openwt.officetracking.domain.course.CourseRequestMapper.toCourse;
import static com.openwt.officetracking.domain.course.CourseValidation.validateCourse;
import static com.openwt.officetracking.domain.mentorship.MenteeValidate.supplyCannotInactive;
import static com.openwt.officetracking.domain.mentorship.UserMentorshipMapper.toUserMentorship;
import static java.time.Instant.now;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.*;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final UserService userService;

    private final CourseStore courseStore;

    private final CourseAssignmentService courseAssignmentService;

    private final AbilityResultService abilityResultService;

    private final AuthsProvider authsProvider;
    private static final UUID emptyUUID = new UUID(0, 0);

    public List<MenteeCourse> findByMenteeId(final UUID menteeId) {
        final List<Course> courses = courseStore.findByMenteeId(menteeId);

        courses.forEach(this::updateCourseStatusAndAssign);

        final List<CourseAssignment> courseAssignments = getCourseAssignmentsByMenteeId(menteeId, courses);

        return getMenteeCourses(courses, courseAssignments);
    }

    public Course findById(final UUID courseId) {
        return courseStore.findById(courseId)
                .orElseThrow(supplyCourseNotFound("id", courseId));
    }

    public CourseDetailResponse findDetailCourseById(final UUID courseId) {
        final Course course = findById(courseId);

        return toCourseDetailResponse(course)
                .withCourseAssignments(buildCourseAssignments(courseId));
    }

    public List<CourseAssignmentResponse> buildCourseAssignments(final UUID courseId) {
        final List<CourseAssignment> courseAssignments = courseAssignmentService.findByCourseId(courseId)
                .stream()
                .map(assignment -> assignment.withMentorId(assignment.getMentorId() == null ? emptyUUID : assignment.getMentorId()))
                .toList();

        final Map<UUID, List<CoachAssignmentResponse>> mentorToCoaches = courseAssignments.stream()
                .collect(groupingBy(
                        CourseAssignment::getMentorId,
                        mapping(this::createCoachAssignmentResponse, toList())
                ));

        return mentorToCoaches.entrySet()
                .stream()
                .map(this::buildCourseAssignmentResponse)
                .toList();
    }

    private CoachAssignmentResponse createCoachAssignmentResponse(final CourseAssignment assignment) {
        return CoachAssignmentResponse.builder()
                .mentee(findUserIfAvailable(assignment.getMenteeId()))
                .coach(findUserIfAvailable(assignment.getCoachId()))
                .build();
    }

    private CourseAssignmentResponse buildCourseAssignmentResponse(final Map.Entry<UUID, List<CoachAssignmentResponse>> mentorToCoaches) {
        final User mentor = findUserIfAvailable(mentorToCoaches.getKey());

        final List<CoachAssignmentResponse> menteeCoaches = mentorToCoaches.getValue();

        return CourseAssignmentResponse.builder()
                .mentor(mentor)
                .coachAssignments(menteeCoaches)
                .build();
    }

    public Course findByName(final String name) {
        return courseStore.findByName(name)
                .orElseThrow(supplyCourseNotFound("name", name));
    }

    @Transactional
    public Course create(final CourseRequest courseRequest) {
        verifyIfCourseAvailable(courseRequest.getName());
        validateCourseAssignment(courseRequest.getCourseAssignments());
        validateCourseTime(courseRequest);

        final Course newCourse = courseStore.save(toCourse(courseRequest)
                .withCreatedAt(now()));

        courseRequest.getCourseAssignments()
                .forEach(courseAssignmentRequest -> createNewCourseAssignment(newCourse, courseAssignmentRequest));
        updateCourseStatusAndAssign(newCourse);

        return newCourse;
    }

    public List<Course> findAll() {
        final List<Course> courses = courseStore.findAll();

        courses.forEach(this::updateCourseStatusAndAssign);

        return courses;
    }

    public List<UserMentorShip> findByReviewerAndCourse(final UUID courseId) {
        final List<MentorAbilityResult> results = abilityResultService.findAbilityResultDetailByCourseId(null, courseId).getResults();

        return results.stream()
                .map(mentorAbilityResult -> toUserMentorship(userService.findById(mentorAbilityResult.getMenteeId())))
                .toList();
    }

    public List<UserMentorShip> findByMenteeAndCourse(final UUID menteeId, final UUID courseId) {
        final List<MentorAbilityResult> results = abilityResultService.findAbilityResultDetailByCourseId(menteeId, courseId).getResults();

        return results.stream()
                .map(mentorAbilityResult -> toUserMentorship(userService.findById(mentorAbilityResult.getMenteeId())))
                .toList();
    }

    @Transactional
    public Course update(final UUID courseId, final CourseRequest courseRequest) {
        final Course courseUpdate = toCourse(courseRequest);

        validateCourse(courseUpdate);
        validateCourseAssignment(courseRequest.getCourseAssignments());
        validateCourseTime(courseRequest);

        Course currentCourse = findById(courseId);
        if (!equalsIgnoreCase(currentCourse.getName(), courseUpdate.getName())) {
            verifyIfCourseAvailable(courseUpdate.getName());
            currentCourse.setName(courseUpdate.getName());
        }
        currentCourse = updateCurrentCourseProperties(currentCourse, courseUpdate);
        courseStore.save(currentCourse);

        updateCourseAssignmentsByRequest(currentCourse, courseRequest);
        updateCourseStatusAndAssign(currentCourse);

        return currentCourse;
    }

    private void updateCourseAssignmentsByRequest(final Course currentCourse, final CourseRequest courseRequest) {
        final List<CourseAssignment> courseAssignments = courseAssignmentService.findByCourseId(currentCourse.getId());

        updateCourseAssignWithMentor(courseAssignments, courseRequest, currentCourse);
        updateCourseAssignWithMenteeCoach(courseRequest, currentCourse);
    }

    private void updateCourseAssignWithMentor(final List<CourseAssignment> courseAssignments, final CourseRequest courseRequest, final Course currentCourse) {
        final Set<UUID> existingMentorIds = courseAssignments.stream()
                .map(CourseAssignment::getMentorId)
                .collect(toSet());
        final Set<UUID> requestedMentorIds = courseRequest.getCourseAssignments().stream()
                .map(CourseAssignmentRequest::getMentorId)
                .collect(toSet());

        existingMentorIds.stream()
                .filter(mentorId -> mentorId != null && !requestedMentorIds.contains(mentorId))
                .forEach(mentorId -> {
                    courseAssignmentService.deleteByMentorIdAndCourseId(mentorId, currentCourse.getId());
                    createNewCourseAssignment(mentorId);
                });

        requestedMentorIds.stream()
                .filter(mentorId -> mentorId != null && !existingMentorIds.contains(mentorId))
                .forEach(courseAssignmentService::deleteUnassignedCourseByMentorId);
    }

    private void updateCourseAssignWithMenteeCoach(final CourseRequest courseRequest, final Course currentCourse) {
        for (final CourseAssignmentRequest courseAssignmentRequest : courseRequest.getCourseAssignments()) {
            final Set<UUID> existingMenteeIdsForMentor = getExistingMenteeIdsForMentor(courseAssignmentRequest.getMentorId(), currentCourse.getId());
            final Set<UUID> requestedMenteeIds = courseAssignmentRequest.getCoachAssignments().stream()
                    .map(CoachAssignmentRequest::getMenteeId)
                    .collect(toSet());

            updateCourseAssignmentsForExistingMentees(existingMenteeIdsForMentor, requestedMenteeIds, courseAssignmentRequest, currentCourse.getId());

            deleteCourseAssignmentsForUnrequestedMentees(existingMenteeIdsForMentor, requestedMenteeIds, courseAssignmentRequest.getMentorId(), currentCourse.getId());

            createCourseAssignmentsForNewMentees(existingMenteeIdsForMentor, requestedMenteeIds, courseAssignmentRequest, currentCourse.getId());
        }
    }

    private void deleteCourseAssignmentsForUnrequestedMentees(
            final Set<UUID> existingMenteeIdsForMentor,
            final Set<UUID> requestedMenteeIds,
            final UUID mentorId,
            final UUID courseId
    ) {
        existingMenteeIdsForMentor.stream()
                .filter(menteeId -> menteeId != null && !requestedMenteeIds.contains(menteeId))
                .forEach(menteeId -> courseAssignmentService.deleteByMenteeIdAndMentorIdAndCourseId(menteeId, mentorId, courseId));
    }

    private void createCourseAssignmentsForNewMentees(
            final Set<UUID> existingMenteeIdsForMentor,
            final Set<UUID> requestedMenteeIds,
            final CourseAssignmentRequest courseAssignmentRequest,
            final UUID courseId
    ) {
        requestedMenteeIds.stream()
                .filter(menteeId -> menteeId != null && !existingMenteeIdsForMentor.contains(menteeId))
                .forEach(menteeId -> {
                    final Optional<CoachAssignmentRequest> matchingRequest = courseAssignmentRequest.getCoachAssignments().stream()
                            .filter(request -> Objects.equals(request.getMenteeId(), menteeId))
                            .findFirst();

                    if (matchingRequest.isPresent()) {
                        final UUID coachId = matchingRequest.get().getCoachId();
                        createAssignmentAndUpdateResults(courseId, courseAssignmentRequest.getMentorId(), menteeId, coachId);
                    }
                });
    }

    private void updateCourseAssignmentsForExistingMentees(
            final Set<UUID> existingMenteeIdsForMentor,
            final Set<UUID> requestedMenteeIds,
            final CourseAssignmentRequest courseAssignmentRequest,
            final UUID courseId
    ) {
        requestedMenteeIds.stream()
                .filter(menteeId -> menteeId != null && existingMenteeIdsForMentor.contains(menteeId))
                .forEach(menteeId -> {
                    for (final CoachAssignmentRequest coachAssignmentRequest : courseAssignmentRequest.getCoachAssignments()) {
                        final CourseAssignment courseAssignment = courseAssignmentService.findByCourseIdAndMentorIdAndMenteeId(courseId, courseAssignmentRequest.getMentorId(), coachAssignmentRequest.getMenteeId());

                        if (menteeId.equals(courseAssignment.getMenteeId()) && coachAssignmentRequest.getCoachId().equals(courseAssignment.getCoachId())) {
                            return;
                        }

                        if (menteeId.equals(courseAssignment.getMenteeId()) && (coachAssignmentRequest.getCoachId() != courseAssignment.getCoachId())) {
                            courseAssignmentService.deleteByMenteeIdAndMentorIdAndCourseId(menteeId, courseAssignmentRequest.getMentorId(), courseId);
                            createAssignmentAndUpdateResults(courseId, courseAssignmentRequest.getMentorId(), courseAssignment.getMenteeId(), coachAssignmentRequest.getCoachId());
                        }
                    }
                });
    }

    private Set<UUID> getExistingMenteeIdsForMentor(final UUID mentorId, final UUID courseId) {
        return courseAssignmentService.findByMentorIdAndCourseId(mentorId, courseId).stream()
                .map(CourseAssignment::getMenteeId)
                .filter(Objects::nonNull)
                .collect(toSet());
    }

    private void createAssignmentAndUpdateResults(final UUID courseId, final UUID mentorId, final UUID menteeId, final UUID coachId) {
        final CourseAssignment newCourseAssignment = CourseAssignment.builder()
                .courseId(courseId)
                .mentorId(mentorId)
                .menteeId(menteeId)
                .coachId(coachId)
                .build();

        abilityResultService.createAbilityResultByCourseAssignment(courseAssignmentService.create(newCourseAssignment).getId());
    }

    private void createNewCourseAssignment(final UUID mentorId) {
        final CourseAssignment newCourseAssignment = CourseAssignment.builder()
                .mentorId(mentorId)
                .build();

        courseAssignmentService.create(newCourseAssignment);
    }

    public void deleteById(final UUID courseId) {
        final Course currentCourse = findById(courseId);
        if (!getCourseStatus(currentCourse).equals(CourseStatus.WAITING)) {
            throw supplyCourseValidation("You cannot delete this course because it is currently in progress or has already been completed.").get();
        }

        courseStore.deleteById(courseId);
    }

    private Course updateCurrentCourseProperties(final Course currentCourse, final Course courseUpdate) {
        return currentCourse
                .withStartAt(courseUpdate.getStartAt())
                .withEndAt(courseUpdate.getEndAt())
                .withDescription(courseUpdate.getDescription())
                .withUpdatedAt(now());
    }

    private List<CourseAssignment> getCourseAssignmentsByMenteeId(final UUID menteeId, final List<Course> courses) {
        final List<CourseAssignment> courseAssignments = courseAssignmentService.findByMenteeId(menteeId);

        return courseAssignments.stream()
                .filter(assignment -> courses.stream().anyMatch(course -> course.getId().equals(assignment.getCourseId())))
                .toList();
    }

    private List<MenteeCourse> getMenteeCourses(final List<Course> courses, final List<CourseAssignment> courseAssignments) {
        return courses.stream()
                .map(course -> buildMenteeCourse(courseAssignments, course))
                .toList();
    }

    private List<CourseMentor> getMentorsInCourse(final List<CourseAssignment> courseAssignments, final Course course) {
        final List<CourseMentor> courseMentors = courseAssignments.stream()
                .filter(courseAssignment -> courseAssignment.getCourseId().equals(course.getId()))
                .map(this::buildCourseMentor)
                .toList();

        if (courseMentors.isEmpty()) {
            return emptyList();
        }

        return courseMentors;
    }

    private MenteeCourse buildMenteeCourse(final List<CourseAssignment> courseAssignments, final Course course) {
        final MenteeCourse menteeCourse = toMenteeCourse(course);
        final List<CourseMentor> courseMentors = getMentorsInCourse(courseAssignments, course);

        menteeCourse.setMentors(courseMentors);

        return menteeCourse;
    }

    private CourseMentor buildCourseMentor(final CourseAssignment courseAssignment) {
        if (courseAssignment.getMentorId() == null) {
            return null;
        }

        final User user = userService.findById(courseAssignment.getMentorId());

        return CourseMentor.builder()
                .mentorId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    public List<MentorCourse> findByMentorId(final UUID mentorId) {
        final List<Course> courses = courseStore.findByMentorId(mentorId);

        courses.forEach(this::updateCourseStatusAndAssign);

        final List<CourseAssignment> courseAssignments = getCourseAssignmentsByMentorId(mentorId, courses);

        return getMentorCourses(courses, courseAssignments);
    }

    public List<CoachCourse> findByCoachId(final UUID coachId) {
        final List<Course> courses = courseStore.findByCoachId(coachId);

        courses.forEach(this::updateCourseStatusAndAssign);

        final List<CourseAssignment> courseAssignments = getCourseAssignmentsByCoachId(coachId, courses);

        return getCoachCourses(courses, courseAssignments);
    }

    public List<Course> findCoursesByMenteeId(final UUID menteeId) {
        return courseStore.findCoursesByMenteeId(menteeId);
    }

    public List<Course> findCoursesByMentorId(final UUID mentorId) {
        return courseStore.findCoursesByMentorId(mentorId);
    }

    public List<Course> findCoursesByCoachId(final UUID coachId) {
        return courseStore.findByCoachId(coachId);
    }

    public void verifyIfCourseInProgress(final List<Course> courses) {
        courses.stream()
                .filter(course -> getCourseStatus(course) == CourseStatus.IN_PROGRESS || getCourseStatus(course) == CourseStatus.WAITING)
                .findFirst()
                .ifPresent(_course -> {
                    throw supplyCannotInactive().get();
                });
    }

    private User findUserIfAvailable(final UUID userId) {
        return userId != null && !userId.equals(emptyUUID) ? userService.findById(userId) : null;
    }

    private List<CourseAssignment> getCourseAssignmentsByMentorId(final UUID mentorId, final List<Course> courses) {
        final List<CourseAssignment> courseAssignments = courseAssignmentService.findByMentorId(mentorId);

        return courseAssignments.stream()
                .filter(assignment -> courses.stream().anyMatch(course -> course.getId().equals(assignment.getCourseId())))
                .toList();
    }

    private List<CourseAssignment> getCourseAssignmentsByCoachId(final UUID coachId, final List<Course> courses) {
        final List<CourseAssignment> courseAssignments = courseAssignmentService.findByCoachId(coachId);
        final Set<UUID> courseIds = courses.stream().map(Course::getId).collect(toSet());

        return courseAssignments.stream()
                .filter(assignment -> courseIds.contains(assignment.getCourseId()))
                .toList();
    }

    private List<CoachCourse> getCoachCourses(final List<Course> courses, final List<CourseAssignment> assignments) {

        return courses.stream()
                .map(course -> buildCoachCourse(assignments, course))
                .toList();
    }

    private List<MentorCourse> getMentorCourses(final List<Course> courses, final List<CourseAssignment> assignments) {

        return courses.stream()
                .map(course -> buildMentorCourse(assignments, course))
                .toList();
    }

    private List<CourseMentee> getMenteesInCourse(final List<CourseAssignment> courseAssignments, final Course course) {

        return courseAssignments.stream()
                .filter(courseAssignment -> courseAssignment.getCourseId().equals(course.getId()))
                .map(this::buildCourseMentee)
                .toList();
    }

    private MentorCourse buildMentorCourse(final List<CourseAssignment> courseAssignments, final Course course) {
        final MentorCourse mentorCourse = toMentorCourse(course);
        final List<CourseMentee> courseMentees = getMenteesInCourse(courseAssignments, course);

        mentorCourse.setCourseMentees(courseMentees);

        return mentorCourse;
    }

    private CoachCourse buildCoachCourse(final List<CourseAssignment> courseAssignments, final Course course) {
        final CoachCourse coachCourse = toCoachCourse(course);
        final List<CourseMentee> courseMentees = getMenteesInCourse(courseAssignments, course);

        coachCourse.setCoachMentees(courseMentees);

        return coachCourse;
    }

    private CourseMentee buildCourseMentee(final CourseAssignment courseAssignment) {
        if (courseAssignment.getMenteeId() == null) {
            return null;
        }

        final User user = userService.findById(courseAssignment.getMenteeId());

        return CourseMentee.builder()
                .menteeId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    private void updateCourseStatusAndAssign(final Course course) {
        course.setStatus(getCourseStatus(course));

        final List<CourseAssignment> assignments = courseAssignmentService.findByCourseId(course.getId());

        course.setAssign(getCourseAssign(assignments));
    }

    private CourseStatus getCourseStatus(final Course course) {
        final Instant currentDate = now();

        if (course.getEndAt() != null && currentDate.isAfter(course.getEndAt())) {
            return CourseStatus.COMPLETED;
        }

        if (course.getStartAt() == null || currentDate.isBefore(course.getStartAt())) {
            return CourseStatus.WAITING;
        }

        if (currentDate.isAfter(course.getStartAt()) && (course.getEndAt() == null || currentDate.isBefore(course.getEndAt()))) {
            return CourseStatus.IN_PROGRESS;
        }

        return CourseStatus.WAITING;
    }

    private CourseAssign getCourseAssign(final List<CourseAssignment> assignments) {
        if (hasMissingAllMentor(assignments) || hasMissingAllMentee(assignments) || hasMissingAllCoach(assignments)) {
            return CourseAssign.TO_BE_DEFINED;
        }

        if (hasAtLeastOneAssignmentWithNullIds(assignments)) {
            return CourseAssign.NOT_YET;
        }

        return CourseAssign.DONE;
    }

    private boolean hasMissingAllMentor(final List<CourseAssignment> assignments) {
        return assignments.stream()
                .allMatch(courseAssignment -> courseAssignment.getMentorId() == null);
    }

    private boolean hasMissingAllCoach(final List<CourseAssignment> assignments) {
        return assignments.stream()
                .allMatch(courseAssignment -> courseAssignment.getCoachId() == null);
    }

    private boolean hasMissingAllMentee(final List<CourseAssignment> assignments) {
        return assignments.stream()
                .allMatch(courseAssignment -> courseAssignment.getMenteeId() == null);
    }

    private boolean hasAtLeastOneAssignmentWithNullIds(final List<CourseAssignment> assignments) {
        return assignments.stream().anyMatch(this::hasAnyIdNull);
    }

    private boolean hasAnyIdNull(final CourseAssignment assignment) {
        return assignment.getMentorId() == null || assignment.getMenteeId() == null || assignment.getCoachId() == null;
    }

    private void createNewCourseAssignment(final Course course, final CourseAssignmentRequest courseAssignmentRequest) {
        final CourseAssignment courseAssignment = CourseAssignment.builder()
                .courseId(course.getId())
                .mentorId(courseAssignmentRequest.getMentorId())
                .build();

        for (final CoachAssignmentRequest coachAssignmentRequest : courseAssignmentRequest.getCoachAssignments()) {
            courseAssignment.setMenteeId(coachAssignmentRequest.getMenteeId());
            courseAssignment.setCoachId(coachAssignmentRequest.getCoachId());
            abilityResultService.createAbilityResultByCourseAssignment(courseAssignmentService.create(courseAssignment).getId());
        }

        courseAssignmentService.deleteUnassignedCourseByMentorId(courseAssignmentRequest.getMentorId());
    }

    private void validateCourseAssignment(final List<CourseAssignmentRequest> courseAssignmentRequests) {
        final Set<UUID> requestedMentorIds = courseAssignmentRequests.stream()
                .map(CourseAssignmentRequest::getMentorId)
                .filter(Objects::nonNull)
                .collect(toSet());

        final Set<UUID> requestedCoachIds = courseAssignmentRequests.stream()
                .flatMap(courseAssignmentRequest -> courseAssignmentRequest.getCoachAssignments().stream()
                        .map(CoachAssignmentRequest::getCoachId)
                        .filter(Objects::nonNull))
                .collect(toSet());

        for (final CourseAssignmentRequest courseAssignmentRequest : courseAssignmentRequests) {
            final UUID mentorId = courseAssignmentRequest.getMentorId();
            final List<CoachAssignmentRequest> coachAssignmentRequests = courseAssignmentRequest.getCoachAssignments();

            if (mentorId != null) {
                verifyIfMentorOrMenteeOrCoachIsInactive(courseAssignmentRequest.getMentorId());
            }

            if (coachAssignmentRequests.isEmpty() || mentorId == null || coachAssignmentRequests.stream().anyMatch(coachAssignmentRequest ->
                    coachAssignmentRequest.getMenteeId() == null || coachAssignmentRequest.getCoachId() == null)) {
                throw supplyCourseValidation("Mentor, coach and mentee must all be assigned for each course").get();
            }

            for (final CoachAssignmentRequest coachAssignmentRequest : courseAssignmentRequest.getCoachAssignments()) {
                if (requestedMentorIds.contains(coachAssignmentRequest.getMenteeId())) {
                    throw supplyCourseValidation("A mentor cannot be assigned as a mentee in a course").get();
                }

                if (requestedCoachIds.contains(coachAssignmentRequest.getMenteeId())) {
                    throw supplyCourseValidation("A coach cannot be assigned as a mentee in a course").get();
                }

                if (Objects.equals(courseAssignmentRequest.getMentorId(), coachAssignmentRequest.getCoachId())) {
                    throw supplyCourseValidation("A person cannot be both mentor and coach for the same mentee").get();
                }
            }
        }
    }

    private void verifyIfMentorOrMenteeOrCoachIsInactive(final UUID userId) {
        final User currentUser = userService.findById(userId);

        if (currentUser.getMenteeStatus() == CoachMentorMenteeStatus.INACTIVE ||
                currentUser.getCoachStatus() == CoachMentorMenteeStatus.INACTIVE ||
                currentUser.getMentorStatus() == CoachMentorMenteeStatus.INACTIVE) {
            throw supplyCourseValidation("User with email " + defaultIfNull(currentUser.getCompanyEmail(), currentUser.getPersonalEmail()) + " is deactivated").get();
        }
    }

    private void validateCourseTime(final CourseRequest courseRequest) {
        final Instant currentTime = now();
        final Instant courseTimeStart = courseRequest.getStartAt();
        final Instant courseTimeEnd = courseRequest.getEndAt();

        if (courseTimeEnd != null && currentTime.isAfter(courseTimeEnd)) {
            throw supplyCourseValidation("The course's end date cannot be later than the current date.").get();
        }

        if (courseTimeEnd != null && courseTimeStart != null) {
            if (courseTimeEnd.isBefore(courseTimeStart)) {
                throw supplyCourseValidation("The course's end date must be later than the current date").get();
            }
        }
    }

    private void verifyIfCourseAvailable(final String courseName) {
        courseStore.findByName(courseName)
                .ifPresent(_existingCourse -> {
                    throw supplyCourseExisted("course", courseName).get();
                });
    }
}
