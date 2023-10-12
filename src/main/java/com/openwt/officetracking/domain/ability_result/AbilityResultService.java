package com.openwt.officetracking.domain.ability_result;

import com.openwt.officetracking.domain.ability.Ability;
import com.openwt.officetracking.domain.ability.AbilityService;
import com.openwt.officetracking.domain.ability_category.AbilityCategory;
import com.openwt.officetracking.domain.ability_category.AbilityCategoryService;
import com.openwt.officetracking.domain.auth.AuthsProvider;
import com.openwt.officetracking.domain.course_assignment.CourseAssignment;
import com.openwt.officetracking.domain.course_assignment.CourseAssignmentService;
import com.openwt.officetracking.domain.overall_rating.OverallRating;
import com.openwt.officetracking.domain.review_status.ReviewStatus;
import com.openwt.officetracking.persistent.ability_result.AbilityResultStore;
import com.openwt.officetracking.persistent.course_assignment.CourseAssignmentStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;

import static com.openwt.officetracking.domain.ability_result.PointAndAbilityValidate.calculateOverallRating;
import static com.openwt.officetracking.domain.ability_result.PointAndAbilityValidate.validatePointAndAbilityResult;
import static com.openwt.officetracking.error.CommonError.*;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

@Service
@RequiredArgsConstructor
public class AbilityResultService {

    private final AbilityResultStore abilityResultStore;

    private final CourseAssignmentStore courseAssignmentStore;

    private final CourseAssignmentService courseAssignmentService;

    private final AbilityService abilityService;

    private final AbilityCategoryService abilityCategoryService;

    private final AuthsProvider authsProvider;

    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    @Transactional
    public AbilityResultDetail saveAbilityResultDetail(final AbilityResultDetail abilityResultDetail) {
        return saveAbilityResultDetail(abilityResultDetail, abilityResultDetail.isSubmit() ? ReviewStatus.SUBMITTED : ReviewStatus.DRAFT);
    }

    public AbilityResult findByAbilityAndCourseAssignmentAndMentor(final UUID abilityId, final UUID courseAssignmentId) {
        return abilityResultStore.findByAbilityIdAndCourseAssignmentIdAndMentor(abilityId, courseAssignmentId)
                .orElseThrow(supplyNotFoundError("Ability Result with mentor is not existed"));
    }

    public AbilityResult findByAbilityAndCourseAssignmentAndNotMentor(final UUID abilityId, final UUID courseAssignmentId) {
        return abilityResultStore.findByAbilityIdAndCourseAssignmentIdAndNotMentor(abilityId, courseAssignmentId)
                .orElseThrow(supplyNotFoundError("Ability Result with Coach is not existed"));
    }

    public List<AbilityResult> findByAbilityIdAndNotMentor(final UUID abilityId) {
        return abilityResultStore.findByAbilityIdAndNotMentor(abilityId);
    }

    public AbilityResultDetail findAbilityResultDetailByCourseId(final UUID menteeId, final UUID courseId) {
        verifyMenteeIdAndAdminRoleForAssessmentResults(menteeId);
        final List<CourseAssignment> courseAssignments = findCourseAssignmentsByMenteeAndCourse(menteeId, courseId);

        if (courseAssignments.isEmpty()) {
            throw supplyNotFoundError("This course assignment is not existed!").get();
        }

        return AbilityResultDetail.builder()
                .courseId(courseId)
                .reviewerId(authsProvider.getCurrentUserRole().equals(ROLE_ADMIN) ? menteeId : authsProvider.getCurrentUserId())
                .results(buildMentorAbilityResults(courseAssignments))
                .build();
    }

    public void createAbilityResultByCourseAssignment(final UUID courseAssignmentId) {
        final List<Ability> abilities = abilityService.findAll();

        abilities.forEach(ability -> saveAbilityResult(ability, courseAssignmentId));
    }

    public List<MentorAbilityCategoryAverage> calculateAveragesScoreByMenteeAndCourse(final UUID menteeId, final UUID courseId) {
        return buildMentorAbilityCategoryAverage(menteeId, courseId);
    }

    private List<MentorAbilityCategoryAverage> buildMentorAbilityCategoryAverage(final UUID menteeId, final UUID courseId) {
        return courseAssignmentService.findByMenteeIdAndCourseId(menteeId, courseId).stream()
                .filter(this::isReviewSubmitted)
                .map(this::buildMentorAbilityCategoryAverageForCourseAssignment)
                .flatMap(List::stream)
                .toList();
    }

    private List<MentorAbilityCategoryAverage> buildMentorAbilityCategoryAverageForCourseAssignment(final CourseAssignment courseAssignment) {
        final List<MentorAbilityCategoryAverage> mentorAbilityCategoryAverageList = new ArrayList<>();

        if (isReviewSubmittedOrMentorSubmitted(courseAssignment)) {
            buildAndAddMentorAbilityCategoryAverage(courseAssignment, mentorAbilityCategoryAverageList, true);
        }

        if (isCourseAssignmentHaveBothMentorAndCoach(courseAssignment) && isReviewSubmittedOrCoachSubmitted(courseAssignment)) {
            buildAndAddMentorAbilityCategoryAverage(courseAssignment, mentorAbilityCategoryAverageList, false);
        }

        return mentorAbilityCategoryAverageList;
    }

    private boolean isReviewSubmitted(final CourseAssignment courseAssignment) {
        return courseAssignment.getReviewStatus() == ReviewStatus.SUBMITTED ||
                courseAssignment.getMentorReviewStatus() == ReviewStatus.SUBMITTED ||
                courseAssignment.getCoachReviewStatus() == ReviewStatus.SUBMITTED;
    }

    private boolean isReviewSubmittedOrMentorSubmitted(final CourseAssignment courseAssignment) {
        return courseAssignment.getReviewStatus() == ReviewStatus.SUBMITTED || courseAssignment.getMentorReviewStatus() == ReviewStatus.SUBMITTED;
    }

    private boolean isReviewSubmittedOrCoachSubmitted(final CourseAssignment courseAssignment) {
        return courseAssignment.getReviewStatus() == ReviewStatus.SUBMITTED || courseAssignment.getCoachReviewStatus() == ReviewStatus.SUBMITTED;
    }

    private void buildAndAddMentorAbilityCategoryAverage(
            final CourseAssignment courseAssignment,
            final List<MentorAbilityCategoryAverage> mentorAbilityCategoryAverageList,
            final boolean isMentor) {

        final UUID reviewerId = isMentor ? courseAssignment.getMentorId() : courseAssignment.getCoachId();
        final List<AbilityCategoryAverage> averagesResults = isMentor ?
                buildAbilityCategoryAverageForMentor(courseAssignment) :
                buildAbilityCategoryAverageForCoach(courseAssignment);

        final MentorAbilityCategoryAverage mentorAbilityCategoryAverage = MentorAbilityCategoryAverage.builder()
                .reviewerId(reviewerId)
                .isMentor(isMentor)
                .averagesResults(averagesResults)
                .build();

        mentorAbilityCategoryAverageList.add(mentorAbilityCategoryAverage);
    }

    private List<AbilityCategoryAverage> buildAbilityCategoryAverageForMentor(final CourseAssignment courseAssignment) {
        return buildAbilityCategoryAverage(courseAssignment, true);
    }

    private List<AbilityCategoryAverage> buildAbilityCategoryAverageForCoach(final CourseAssignment courseAssignment) {
        return buildAbilityCategoryAverage(courseAssignment, false);
    }

    private List<AbilityCategoryAverage> buildAbilityCategoryAverage(final CourseAssignment courseAssignment, final boolean isMentor) {
        return abilityCategoryService.findAll()
                .stream()
                .map(abilityCategory -> AbilityCategoryAverage.builder()
                        .abilityCategory(abilityCategory)
                        .averageScore(calculateAverageScore(abilityCategory, courseAssignment, isMentor))
                        .build())
                .toList();
    }

    private double calculateAverageScore(final AbilityCategory abilityCategory, final CourseAssignment courseAssignment, final boolean isMentor) {
        final List<Ability> abilities = abilityService.findByAbilityCategoryId(abilityCategory.getId());
        final int lenOfAbilities = abilities.size();

        final double totalScore = abilities.stream()
                .mapToDouble(ability -> {
                    final AbilityResult abilityResult = isMentor ?
                            findByAbilityAndCourseAssignmentAndMentor(ability.getId(), courseAssignment.getId()) :
                            findByAbilityAndCourseAssignmentAndNotMentor(ability.getId(), courseAssignment.getId());

                    return defaultIfNull(abilityResult.getPoint(), 0);
                })
                .sum();

        return totalScore / lenOfAbilities;
    }

    private AbilityResultDetail saveAbilityResultDetail(final AbilityResultDetail abilityResultDetail, final ReviewStatus reviewStatus) {
        final UUID reviewerId = authsProvider.getCurrentUserId();
        final UUID courseId = abilityResultDetail.getCourseId();

        final List<CourseAssignment> courseAssignmentForMentor = courseAssignmentService.findByMentorIdAndCourseId(reviewerId, courseId);
        courseAssignmentForMentor.forEach(this::verifyIfCourseAlreadySubmittedForMentor);

        final List<CourseAssignment> courseAssignmentForCoach = courseAssignmentService.findByCoachIdAndCourseId(reviewerId, courseId);
        courseAssignmentForCoach.forEach(this::verifyIfCourseAlreadySubmittedForCoach);

        abilityResultDetail.getResults().forEach(mentorAbilityResult -> updateAbilityResults(mentorAbilityResult, reviewerId, courseId, reviewStatus));

        abilityResultDetail.setReviewerId(reviewerId);

        return abilityResultDetail;
    }

    private void verifyIfCourseAlreadySubmittedForMentor(final CourseAssignment courseAssignment) {
        if (courseAssignment.getMentorReviewStatus() == ReviewStatus.SUBMITTED) {
            throw supplyBadRequestError("Cannot submit when the assignment with you as mentor is already submitted").get();
        }
    }

    private void verifyIfCourseAlreadySubmittedForCoach(final CourseAssignment courseAssignment) {
        if (courseAssignment.getCoachReviewStatus() == ReviewStatus.SUBMITTED) {
            throw supplyBadRequestError("Cannot submit when the assignment with you as coach is already submitted").get();
        }
    }

    private void updateAbilityResults(final MentorAbilityResult mentorAbilityResult,
                                      final UUID reviewerId,
                                      final UUID courseId,
                                      final ReviewStatus reviewStatus) {
        final UUID menteeId = mentorAbilityResult.getMenteeId();
        final int total = calculateTotal(mentorAbilityResult.getPoints());
        final OverallRating overallRating = calculateOverallRating(total);
        mentorAbilityResult.setTotal(total);
        mentorAbilityResult.setOverallRating(overallRating);
        mentorAbilityResult.setMentor(mentorAbilityResult.isMentor());

        updateMentorAbilityResultAndReviewStatus(mentorAbilityResult, reviewerId, menteeId, courseId, reviewStatus);

        updateCoachAbilityResultAndReviewStatus(mentorAbilityResult, reviewerId, menteeId, courseId, reviewStatus);
    }

    public void updateMentorAbilityResultAndReviewStatus(
            final MentorAbilityResult mentorAbilityResult,
            final UUID reviewerId,
            final UUID menteeId,
            final UUID courseId,
            final ReviewStatus reviewStatus
    ) {
        if (mentorAbilityResult.isMentor()) {
            final CourseAssignment courseAssignmentMentor = courseAssignmentService.findByMentorMenteeAndCourse(reviewerId, menteeId, courseId);

            if (!courseAssignmentMentor.getMentorId().equals(reviewerId)) {
                throw supplyAccessDeniedError().get();
            }

            mentorAbilityResult.getPoints()
                    .forEach(pointAndAbilityResult -> savePointAndAbilityResult(pointAndAbilityResult, reviewStatus, courseAssignmentMentor, true));
            courseAssignmentMentor.setMentorReviewStatus(reviewStatus);

            mentorAbilityResult.setMentorReviewStatus(reviewStatus);

            updateReviewStatusIfBothSubmitted(courseAssignmentStore.save(courseAssignmentMentor), mentorAbilityResult);
        }
    }

    public void updateCoachAbilityResultAndReviewStatus(
            final MentorAbilityResult mentorAbilityResult,
            final UUID reviewerId,
            final UUID menteeId,
            final UUID courseId,
            final ReviewStatus reviewStatus
    ) {
        final List<CourseAssignment> courseAssignmentCoaches = courseAssignmentService.findByCoachMenteeAndCourse(reviewerId, menteeId, courseId);
        final Optional<CourseAssignment> courseAssignmentCoach = findCourseAssignmentCoach(courseAssignmentCoaches);

        if (courseAssignmentCoach.isPresent()) {
            mentorAbilityResult.getPoints()
                    .forEach(pointAndAbilityResult -> savePointAndAbilityResult(pointAndAbilityResult, reviewStatus, courseAssignmentCoach.get(), false));

            mentorAbilityResult.setCoachReviewStatus(reviewStatus);

            courseAssignmentCoaches.forEach(courseAssignment -> {
                courseAssignment.setCoachReviewStatus(reviewStatus);
                updateReviewStatusIfBothSubmitted(courseAssignmentStore.save(courseAssignment), mentorAbilityResult);
            });
        }
    }

    private Optional<CourseAssignment> findCourseAssignmentCoach(final List<CourseAssignment> courseAssignments) {
        return courseAssignments.stream()
                .filter(this::isCourseAssignmentHaveBothMentorAndCoach)
                .filter(Objects::nonNull)
                .findFirst();
    }

    public void updateReviewStatusIfBothSubmitted(final CourseAssignment courseAssignment, final MentorAbilityResult mentorAbilityResult) {
        if (courseAssignment.getMentorReviewStatus() == ReviewStatus.SUBMITTED &&
                courseAssignment.getCoachReviewStatus() == ReviewStatus.SUBMITTED) {
            courseAssignment.setReviewStatus(ReviewStatus.SUBMITTED);
            mentorAbilityResult.setReviewStatus(ReviewStatus.SUBMITTED);
        } else {
            courseAssignment.setReviewStatus(ReviewStatus.DRAFT);
            mentorAbilityResult.setReviewStatus(ReviewStatus.DRAFT);
        }
        courseAssignmentStore.save(courseAssignment);
    }

    private void savePointAndAbilityResult(final PointAndAbilityResult pointAndAbilityResult, final ReviewStatus reviewStatus, final CourseAssignment courseAssignment, final boolean isMentor) {
        validatePointAndAbilityResult(pointAndAbilityResult, reviewStatus);
        saveAbility(pointAndAbilityResult.getAbilityId(), pointAndAbilityResult.getPoint(), courseAssignment.getId(), isMentor);
    }

    private void saveAbility(final UUID abilityId, final Integer point, final UUID courseAssignmentId, final boolean isMentor) {
        AbilityResult abilityResult = findByAbilityAndCourseAssignmentAndMentor(abilityId, courseAssignmentId);

        if (!isMentor) {
            abilityResult = findByAbilityAndCourseAssignmentAndNotMentor(abilityId, courseAssignmentId);
        }

        abilityResult.setPoint(point);

        abilityResultStore.save(abilityResult);
    }

    private List<MentorAbilityResult> buildMentorAbilityResults(final List<CourseAssignment> courseAssignments) {
        return courseAssignments.stream()
                .map(this::buildMentorAbilityResult)
                .flatMap(List::stream)
                .toList();
    }

    private List<MentorAbilityResult> buildMentorAbilityResult(final CourseAssignment courseAssignment) {
        final List<MentorAbilityResult> mentorAbilityResultList = new ArrayList<>();

        if (isCourseAssignmentHaveBothMentorAndCoach(courseAssignment) && (authsProvider.getCurrentUserId().equals(courseAssignment.getCoachId()) || authsProvider.getCurrentUserRole().equals(ROLE_ADMIN))) {
            final List<PointAndAbilityResult> pointAndAbilityResultForCoach = buildPointAndAbilityByCourseAssignmentForCoach(courseAssignment);
            final UUID userId = authsProvider.getCurrentUserRole().equals(ROLE_ADMIN) ? courseAssignment.getCoachId() : courseAssignment.getMenteeId();
            final Integer total = courseAssignment.getCoachReviewStatus() == ReviewStatus.SUBMITTED || courseAssignment.getReviewStatus() == ReviewStatus.SUBMITTED? calculateTotal(pointAndAbilityResultForCoach) : null;
            final OverallRating overallRating = total != null ? calculateOverallRating(total) : null;

            final MentorAbilityResult coachAbilityResult = buildMentorCoachAbilityResult(courseAssignment, userId, pointAndAbilityResultForCoach, total, overallRating, false);

            mentorAbilityResultList.add(coachAbilityResult);
        }

        if (authsProvider.getCurrentUserId().equals(courseAssignment.getMentorId()) || authsProvider.getCurrentUserRole().equals(ROLE_ADMIN)) {
            final List<PointAndAbilityResult> pointAndAbilityResultForMentor = buildPointAndAbilityByCourseAssignmentForMentor(courseAssignment);
            final UUID userId = authsProvider.getCurrentUserRole().equals(ROLE_ADMIN) ? courseAssignment.getMentorId() : courseAssignment.getMenteeId();
            final Integer total = courseAssignment.getMentorReviewStatus() == ReviewStatus.SUBMITTED || courseAssignment.getReviewStatus() == ReviewStatus.SUBMITTED ? calculateTotal(pointAndAbilityResultForMentor) : null;
            final OverallRating overallRating = total != null ? calculateOverallRating(total) : null;

            final MentorAbilityResult mentorAbilityResult = buildMentorCoachAbilityResult(courseAssignment, userId, pointAndAbilityResultForMentor, total, overallRating, true);

            mentorAbilityResultList.add(mentorAbilityResult);
        }

        return mentorAbilityResultList;
    }

    private MentorAbilityResult buildMentorCoachAbilityResult(
            final CourseAssignment courseAssignment,
            final UUID userId,
            final List<PointAndAbilityResult> pointAndAbilityResults,
            final Integer total,
            final OverallRating overallRating,
            final boolean isMentor) {
        return MentorAbilityResult.builder()
                .menteeId(userId)
                .isMentor(isMentor)
                .points(pointAndAbilityResults)
                .total(total)
                .overallRating(overallRating)
                .mentorReviewStatus(courseAssignment.getMentorReviewStatus())
                .coachReviewStatus(courseAssignment.getCoachReviewStatus())
                .reviewStatus(courseAssignment.getReviewStatus())
                .build();
    }

    private boolean isCourseAssignmentHaveBothMentorAndCoach(final CourseAssignment courseAssignment) {
        return abilityService.findAll().stream()
                .noneMatch(ability -> abilityResultStore.findByAbilityIdAndCourseAssignmentIdAndNotMentor(ability.getId(), courseAssignment.getId()).isEmpty());
    }

    private List<PointAndAbilityResult> buildPointAndAbilityByCourseAssignmentForMentor(final CourseAssignment courseAssignment) {
        return abilityService.findAll().stream()
                .map(ability -> buildPointAndAbilityResult(ability, courseAssignment, true))
                .toList();
    }

    private List<PointAndAbilityResult> buildPointAndAbilityByCourseAssignmentForCoach(final CourseAssignment courseAssignment) {
        return abilityService.findAll().stream()
                .map(ability -> buildPointAndAbilityResult(ability, courseAssignment, false))
                .toList();
    }

    private PointAndAbilityResult buildPointAndAbilityResult(final Ability ability, final CourseAssignment courseAssignment, final boolean isMentor) {
        AbilityResult abilityResult = new AbilityResult();

        if (isMentor) {
            abilityResult = findByAbilityAndCourseAssignmentAndMentor(ability.getId(), courseAssignment.getId());
        } else {
            abilityResult = findByAbilityAndCourseAssignmentAndNotMentor(ability.getId(), courseAssignment.getId());
        }

        Integer point = null;
        if (authsProvider.getCurrentUserRole() != ROLE_ADMIN) {
            point = abilityResult.getPoint();
        }

        if (authsProvider.getCurrentUserRole() == ROLE_ADMIN) {
            point = courseAssignment.getReviewStatus() == ReviewStatus.SUBMITTED ? abilityResult.getPoint() : null;
        }

        return PointAndAbilityResult.builder()
                .point(point)
                .abilityId(abilityResult.getAbilityId())
                .build();
    }

    private List<CourseAssignment> findCourseAssignmentsByMenteeAndCourse(final UUID menteeId, final UUID courseId) {
        if (menteeId != null) {
            return courseAssignmentService.findByMenteeIdAndCourseId(menteeId, courseId);
        }

        return Stream.of(
                        courseAssignmentService.findByCoachIdAndCourseId(authsProvider.getCurrentUserId(), courseId),
                        courseAssignmentService.findByMentorIdAndCourseId(authsProvider.getCurrentUserId(), courseId)
                )
                .flatMap(List::stream)
                .distinct()
                .toList();
    }

    private int calculateTotal(final List<PointAndAbilityResult> pointAndAbilityResults) {
        return pointAndAbilityResults.stream()
                .mapToInt(result -> defaultIfNull(result.getPoint(), 0))
                .sum();
    }

    private void saveAbilityResult(final Ability ability, final UUID courseAssignmentId) {
        final AbilityResult mentorResult = AbilityResult.builder()
                .abilityId(ability.getId())
                .courseAssignmentId(courseAssignmentId)
                .isMentor(true)
                .point(null)
                .createdAt(Instant.now())
                .build();

        abilityResultStore.save(mentorResult);

        if (!isCoachAbilityResultNotExistedYet(ability.getId(), courseAssignmentId) && !isCoachAbilityResultExist(ability.getId(), courseAssignmentId)) {
            final AbilityResult coachResult = AbilityResult.builder()
                    .abilityId(ability.getId())
                    .courseAssignmentId(courseAssignmentId)
                    .isMentor(false)
                    .point(null)
                    .createdAt(Instant.now())
                    .build();

            abilityResultStore.save(coachResult);
        }
    }

    private boolean isCoachAbilityResultNotExistedYet (final UUID abilityId, final UUID courseAssignmentId) {
        final Optional<AbilityResult> abilityResultOptional = abilityResultStore.findByAbilityIdAndCourseAssignmentIdAndNotMentor(abilityId, courseAssignmentId);

        return abilityResultOptional.isPresent();
    }

    private boolean isCoachAbilityResultExist (final UUID abilityId, final UUID courseAssignmentId) {
        final CourseAssignment courseAssignmentInput = courseAssignmentService.findById(courseAssignmentId);
        final Set<UUID> menteeCoachInCourseInput = addThreeUUIDsToSet(courseAssignmentInput.getCourseId(), courseAssignmentInput.getMenteeId(), courseAssignmentInput.getCoachId());
        final List<AbilityResult> abilityResults = findByAbilityIdAndNotMentor(abilityId);

        if (abilityResults.isEmpty()) {
            return false;
        }

        final List<Set<UUID>> listMenteeCoachAllCourseExists = abilityResults.stream()
                .map(abilityResult -> {
                    final CourseAssignment courseAssignmentExist = courseAssignmentService.findById(abilityResult.getCourseAssignmentId());
                    return addThreeUUIDsToSet(courseAssignmentExist.getCourseId(), courseAssignmentExist.getMenteeId(), courseAssignmentExist.getCoachId());
                })
                .toList();

        return listMenteeCoachAllCourseExists.contains(menteeCoachInCourseInput);
    }

    public Set<UUID>  addThreeUUIDsToSet(final UUID uuid1, final UUID uuid2, final UUID uuid3) {
        final Set<UUID> uuidSet = new HashSet<>();
        uuidSet.add(uuid1);
        uuidSet.add(uuid2);
        uuidSet.add(uuid3);

        return uuidSet;
    }

    private void verifyMenteeIdAndAdminRoleForAssessmentResults(final UUID menteeId) {
        if (menteeId == null && authsProvider.getCurrentUserRole() == ROLE_ADMIN) {
            throw supplyBadRequestError("You missing some information required to view the assessment results").get();
        }
    }
}
