package com.openwt.officetracking.domain.ability_result;

import com.openwt.officetracking.domain.ability.AbilityService;
import com.openwt.officetracking.domain.ability_category.AbilityCategoryService;
import com.openwt.officetracking.domain.auth.AuthsProvider;
import com.openwt.officetracking.domain.course_assignment.CourseAssignmentService;
import com.openwt.officetracking.error.NotFoundException;
import com.openwt.officetracking.persistent.ability_result.AbilityResultStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.openwt.officetracking.fake.AbilityCategoryFakes.buildAbilityCategories;
import static com.openwt.officetracking.fake.AbilityResultFakes.buildAbilityResult;
import static com.openwt.officetracking.fake.AbilityResultFakes.buildMentorAbilityCategoryAverages;
import static com.openwt.officetracking.fake.CourseAssignmentFakes.buildCourseAssignment;
import static com.openwt.officetracking.fake.CourseAssignmentFakes.buildCourseAssignments;
import static com.openwt.officetracking.fake.UserFakes.buildUser;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AbilityResultServiceTest {

    @InjectMocks
    private AbilityResultService abilityResultService;

    @Mock
    private AbilityResultStore abilityResultStore;

    @Mock
    private CourseAssignmentService courseAssignmentService;

    @Mock
    private AbilityCategoryService abilityCategoryService;

    @Mock
    private AuthsProvider authsProvider;

    @Mock
    private AbilityService abilityService;

    @Test
    void findByAbilityIdAndCourseAssignmentId_OK() {
        final var abilityResult = buildAbilityResult();

        when(abilityResultStore.findByAbilityIdAndCourseAssignmentIdAndMentor(abilityResult.getAbilityId(), abilityResult.getCourseAssignmentId()))
                .thenReturn(Optional.of(abilityResult));

        final var actual = abilityResultService.findByAbilityAndCourseAssignmentAndMentor(abilityResult.getAbilityId(), abilityResult.getCourseAssignmentId());

        assertEquals(abilityResult, actual);
        verify(abilityResultStore).findByAbilityIdAndCourseAssignmentIdAndMentor(abilityResult.getAbilityId(), abilityResult.getCourseAssignmentId());
    }

    @Test
    void findByAbilityIdAndCourseAssignmentId_NotFoundException() {
        final var abilityId = randomUUID();
        final var courseAssignmentId = randomUUID();

        when(abilityResultStore.findByAbilityIdAndCourseAssignmentIdAndMentor(abilityId, courseAssignmentId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> abilityResultService.findByAbilityAndCourseAssignmentAndMentor(abilityId, courseAssignmentId));

        verify(abilityResultStore).findByAbilityIdAndCourseAssignmentIdAndMentor(abilityId, courseAssignmentId);
    }

    @Test
    void findAbilityResultDetail_OK() {
        final var reviewerId = UUID.randomUUID();
        final var courseId = UUID.randomUUID();
        final var mentor = buildUser();
        final var courseAssignments = buildCourseAssignments();

        when(authsProvider.getCurrentUserRole())
                .thenReturn(String.valueOf(mentor.getRole()));
        when(authsProvider.getCurrentUserId())
                .thenReturn(reviewerId);
        when(courseAssignmentService.findByMenteeIdAndCourseId(any(UUID.class), any(UUID.class)))
                .thenReturn(courseAssignments);

        final AbilityResultDetail actual = abilityResultService.findAbilityResultDetailByCourseId(reviewerId, courseId);
        assertEquals(reviewerId, actual.getReviewerId());

        verify(courseAssignmentService, times(1)).findByMenteeIdAndCourseId(any(UUID.class), any(UUID.class));
        verify(authsProvider, times(9)).getCurrentUserRole();
    }

    @Test
    void findAbilityResultDetail_IfCourseAssignmentEmpty_ThroughNotFoundException() {
        final var menteeId = randomUUID();
        final var courseId = randomUUID();

        when(courseAssignmentService.findByMenteeIdAndCourseId(menteeId, courseId))
                .thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, () -> abilityResultService.findAbilityResultDetailByCourseId(menteeId, courseId));

        verify(courseAssignmentService).findByMenteeIdAndCourseId(menteeId, courseId);
    }

    @Test
    void shouldCreateAbilityResultByCourseAssignment_OK() {
        final var courseAssignment = buildCourseAssignment();
        final var abilities = abilityService.findAll();
        when(abilityService.findAll())
                .thenReturn(abilities);
        abilityResultService.createAbilityResultByCourseAssignment(courseAssignment.getId());
        verify(abilityService, times(2)).findAll();
    }

    @Test
    void shouldCalculateAveragesScoreByMenteeAndCourse() {
        final var mentorAbilityCategories = buildMentorAbilityCategoryAverages();
        final var courseAssignment = buildCourseAssignment()
                .withMentorId(mentorAbilityCategories.get(0).getReviewerId());

        when(courseAssignmentService.findByMenteeIdAndCourseId(courseAssignment.getMenteeId(), courseAssignment.getCourseId()))
                .thenReturn(List.of(courseAssignment));
        when(abilityCategoryService.findAll())
                .thenReturn(buildAbilityCategories());

        final var actual = abilityResultService.calculateAveragesScoreByMenteeAndCourse(courseAssignment.getMenteeId(), courseAssignment.getCourseId());

        assertEquals(mentorAbilityCategories.get(0).getReviewerId(), actual.get(0).getReviewerId());
        assertEquals(mentorAbilityCategories.get(0).getAveragesResults().size(), actual.get(0).getAveragesResults().size());

        verify(courseAssignmentService).findByMenteeIdAndCourseId(courseAssignment.getMenteeId(), courseAssignment.getCourseId());
        verify(abilityCategoryService, times(2)).findAll();
    }
}
