package com.openwt.officetracking.api.mentorship;

import com.openwt.officetracking.api.AbstractControllerTest;
import com.openwt.officetracking.api.WithMockAdmin;
import com.openwt.officetracking.domain.auth.AuthsProvider;
import com.openwt.officetracking.domain.course.CourseService;
import com.openwt.officetracking.domain.mentorship.CoachService;
import com.openwt.officetracking.domain.mentorship_status.CoachMentorMenteeStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.openwt.officetracking.fake.UserFakes.buildUser;
import static com.openwt.officetracking.fake.UserMentorShipRequestFakes.buildUserMentorShipRequestDTO;
import static com.openwt.officetracking.fake.UserMentorshipFakes.buildUserMentorShips;
import static com.openwt.officetracking.fake.UserMentorshipFakes.buildUserMentorship;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CoachController.class)
@ExtendWith(SpringExtension.class)
class CoachControllerTest extends AbstractControllerTest {

    private static final String BASE_URL = "/api/v1/coaches";

    @MockBean
    private AuthsProvider authsProvider;

    @MockBean
    private CoachService coachService;

    @MockBean
    private CourseService courseService;

    @BeforeEach
    void init() {
        when(authsProvider.getCurrentAuthentication()).thenCallRealMethod();
    }

    @Test
    @WithMockAdmin
    void shouldAssignUserAsMentee_OK() throws Exception {
        final var userMentorShipRequest = buildUserMentorShipRequestDTO();
        final var userMentorShip = buildUserMentorship();

        when(coachService.assignUserAsCoach(any())).thenReturn(userMentorShip);

        post(BASE_URL, userMentorShipRequest)
                .andExpect(status().isOk());

        verify(coachService).assignUserAsCoach(argThat(userArg -> userArg.equals(userMentorShipRequest.getUserId())));
    }

    @Test
    @WithMockAdmin
    void shouldFindAll_OK() throws Exception {
        final var userMentorShips = buildUserMentorShips();

        when(coachService.findAll()).thenReturn(userMentorShips);

        get(BASE_URL)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(userMentorShips.size()))
                .andExpect(jsonPath("$[0].id").value(userMentorShips.get(0).getId().toString()))
                .andExpect(jsonPath("$[0].firstName").value(userMentorShips.get(0).getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(userMentorShips.get(0).getLastName()))
                .andExpect(jsonPath("$[0].numberOfCourses").value(userMentorShips.get(0).getNumberOfCourses().toString()))
                .andExpect(jsonPath("$[0].mentorshipStatus").value(userMentorShips.get(0).getMentorshipStatus().toString()))
                .andExpect(jsonPath("$[0].personalEmail").value(userMentorShips.get(0).getPersonalEmail()))
                .andExpect(jsonPath("$[0].companyEmail").value(userMentorShips.get(0).getCompanyEmail()))
                .andExpect(jsonPath("$[0].photo").value(userMentorShips.get(0).getPhoto()))
                .andExpect(jsonPath("$[0].dateOfBirth").value(userMentorShips.get(0).getDateOfBirth().toString()))
                .andExpect(jsonPath("$[0].contractType").value(userMentorShips.get(0).getContractType().toString()))
                .andExpect(jsonPath("$[0].phoneNumber").value(userMentorShips.get(0).getPhoneNumber()))
                .andExpect(jsonPath("$[0].qrCode").value(userMentorShips.get(0).getQrCode()))
                .andExpect(jsonPath("$[0].positionId").value(userMentorShips.get(0).getPositionId().toString()));

        verify(coachService).findAll();
    }

    @Test
    @WithMockAdmin
    void shouldFindDetailById_OK() throws Exception {
        final var userMentorShip = buildUserMentorship();

        when(coachService.findById(userMentorShip.getId())).thenReturn(userMentorShip);

        get(BASE_URL + "/" + userMentorShip.getId() + "/detail")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(userMentorShip.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(userMentorShip.getLastName()))
                .andExpect(jsonPath("$.personalEmail").value(userMentorShip.getPersonalEmail()))
                .andExpect(jsonPath("$.companyEmail").value(userMentorShip.getCompanyEmail()))
                .andExpect(jsonPath("$.photo").value(userMentorShip.getPhoto()))
                .andExpect(jsonPath("$.dateOfBirth").value(userMentorShip.getDateOfBirth().toString()))
                .andExpect(jsonPath("$.contractType").value(userMentorShip.getContractType().toString()))
                .andExpect(jsonPath("$.phoneNumber").value(userMentorShip.getPhoneNumber()))
                .andExpect(jsonPath("$.qrCode").value(userMentorShip.getQrCode()))
                .andExpect(jsonPath("$.positionId").value(userMentorShip.getPositionId().toString()))
                .andExpect(jsonPath("$.mentorshipStatus").value(userMentorShip.getMentorshipStatus().toString()))
                .andExpect(jsonPath("$.numberOfCourses").value(userMentorShip.getNumberOfCourses().toString()));

        verify(coachService).findById(userMentorShip.getId());
    }

    @Test
    @WithMockAdmin
    void shouldActivate_OK() throws Exception {
        final var coachToActive = buildUser().withCoachStatus(CoachMentorMenteeStatus.INACTIVE);

        put(BASE_URL + "/" + coachToActive.getId() + "/activate", null)
                .andExpect(status().isOk());

        verify(coachService).activeByCoachId((coachToActive.getId()));
    }

    @Test
    @WithMockAdmin
    void shouldInactivate_OK() throws Exception {
        final var coachToInactive = buildUser();

        put(BASE_URL + "/" + coachToInactive.getId() + "/inactivate", null)
                .andExpect(status().isOk());

        verify(coachService).inactiveByCoachId((coachToInactive.getId()));
    }
}
