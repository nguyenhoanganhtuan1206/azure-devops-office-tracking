package com.openwt.officetracking.api.assessment;

import com.openwt.officetracking.api.AbstractControllerTest;
import com.openwt.officetracking.api.WithMockAdmin;
import com.openwt.officetracking.api.WithMockUser;
import com.openwt.officetracking.domain.ability.AbilityCategoryDetail;
import com.openwt.officetracking.domain.ability.AbilityService;
import com.openwt.officetracking.domain.ability_result.AbilityResultDetail;
import com.openwt.officetracking.domain.ability_result.AbilityResultService;
import com.openwt.officetracking.domain.auth.AuthsProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.UUID;

import static com.openwt.officetracking.fake.AbilityCategoryFakes.buildAbilityCategory;
import static com.openwt.officetracking.fake.AbilityFakes.buildAbilities;
import static com.openwt.officetracking.fake.AbilityResultFakes.*;
import static java.util.UUID.randomUUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AssessmentController.class)
@ExtendWith(SpringExtension.class)
class AssessmentControllerTest extends AbstractControllerTest {

    private static final String BASE_URL = "/api/v1/assessments";

    @MockBean
    private AuthsProvider authsProvider;

    @MockBean
    private AbilityService abilityService;

    @MockBean
    private AbilityResultService abilityResultService;

    @BeforeEach
    void init() {
        when(authsProvider.getCurrentAuthentication()).thenCallRealMethod();
    }

    @Test
    @WithMockAdmin
    void shouldFindAbilityCatalogDetail_OK() throws Exception {
        final var abilityCategory = buildAbilityCategory();
        final var abilityCategoryDetail = AbilityCategoryDetail.builder()
                .id(abilityCategory.getId())
                .name(abilityCategory.getName())
                .abilities(buildAbilities())
                .build();
        final var abilityCategoryDetails = List.of(abilityCategoryDetail);

        when(abilityService.findAbilityCategoryDetails())
                .thenReturn(abilityCategoryDetails);

        get(BASE_URL + "/categories")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(abilityCategoryDetails.size()))
                .andExpect(jsonPath("$[0].abilities.size()").value(abilityCategoryDetails.get(0).getAbilities().size()));

        verify(abilityService).findAbilityCategoryDetails();
    }

    @Test
    @WithMockAdmin
    void shouldFindDetailAbilityResult_OK() throws Exception {
        final var menteeId = randomUUID();
        final var courseId = randomUUID();
        final var abilityResultDetail = buildAbilityResultDetail();

        when(abilityResultService.findAbilityResultDetailByCourseId(menteeId, courseId))
                .thenReturn(abilityResultDetail);

        get(BASE_URL + "/results?menteeId=" + menteeId + "&courseId=" + courseId)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewerId").value(abilityResultDetail.getReviewerId().toString()))
                .andExpect(jsonPath("$.results.size()").value(abilityResultDetail.getResults().size()));

        verify(abilityResultService).findAbilityResultDetailByCourseId(menteeId, courseId);
    }

    @Test
    @WithMockAdmin
    void shouldCalculateAverages_OK() throws Exception {
        final var menteeId = randomUUID();
        final var courseId = randomUUID();
        final var mentorAbilityCategoryAverages = buildMentorAbilityCategoryAverages();

        when(abilityResultService.calculateAveragesScoreByMenteeAndCourse(any(UUID.class), any(UUID.class)))
                .thenReturn(mentorAbilityCategoryAverages);

        get(BASE_URL + "/average-score?menteeId=" + menteeId + "&courseId=" + courseId)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reviewerId").value(mentorAbilityCategoryAverages.get(0).getReviewerId().toString()));

        verify(abilityResultService).calculateAveragesScoreByMenteeAndCourse(any(UUID.class), any(UUID.class));
    }

    @Test
    @WithMockUser
    void shouldSaveMentorAssignments_OK() throws Exception {
        final var abilityResultRequest = buildAbilityResultRequest();
        final var abilityResultDetail = buildAbilityResultDetail();

        when(abilityResultService.saveAbilityResultDetail(any(AbilityResultDetail.class)))
                .thenReturn(abilityResultDetail);

        post(BASE_URL, abilityResultRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewerId").value(abilityResultDetail.getReviewerId().toString()))
                .andExpect(jsonPath("$.courseId").value(abilityResultDetail.getCourseId().toString()))
                .andExpect(jsonPath("$.results.size()").value(abilityResultDetail.getResults().size()));

        verify(abilityResultService).saveAbilityResultDetail(any(AbilityResultDetail.class));
    }
}
