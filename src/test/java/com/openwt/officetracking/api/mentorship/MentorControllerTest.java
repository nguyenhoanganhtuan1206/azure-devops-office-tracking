package com.openwt.officetracking.api.mentorship;

import com.openwt.officetracking.api.AbstractControllerTest;
import com.openwt.officetracking.api.WithMockAdmin;
import com.openwt.officetracking.domain.auth.AuthsProvider;
import com.openwt.officetracking.domain.mentorship.MentorService;
import com.openwt.officetracking.domain.mentorship_status.CoachMentorMenteeStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.openwt.officetracking.fake.MentorCreateRequestFakes.buildMentorShipRequestDTO;
import static com.openwt.officetracking.fake.UserFakes.buildUser;
import static com.openwt.officetracking.fake.UserMentorshipFakes.buildUserMentorShips;
import static com.openwt.officetracking.fake.UserMentorshipFakes.buildUserMentorship;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MentorController.class)
@ExtendWith(SpringExtension.class)
class MentorControllerTest extends AbstractControllerTest {

    private static final String BASE_URL = "/api/v1/mentors";

    @MockBean
    private AuthsProvider authsProvider;

    @MockBean
    private MentorService mentorService;

    @BeforeEach
    void init() {
        when(authsProvider.getCurrentAuthentication()).thenCallRealMethod();
    }

    @Test
    @WithMockAdmin
    void shouldUpdateRoleMentor_OK() throws Exception {
        final var mentorShipRequest = buildMentorShipRequestDTO();
        final var userMentorShip = buildUserMentorship();

        when(mentorService.assignUserAsMentor(any())).thenReturn(userMentorShip);

        post(BASE_URL, mentorShipRequest)
                .andExpect(status().isOk());

        verify(mentorService).assignUserAsMentor(argThat(userId -> userId.equals(mentorShipRequest.getUserId())));
    }

    @Test
    @WithMockAdmin
    void shouldFindAll_OK() throws Exception {
        final var userMentorShips = buildUserMentorShips();

        when(mentorService.findAll()).thenReturn(userMentorShips);

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

        verify(mentorService).findAll();
    }

    @Test
    @WithMockAdmin
    void shouldActivateMentor_OK() throws Exception {
        final var mentorToActive = buildUser().withMentorStatus(CoachMentorMenteeStatus.INACTIVE);

        put(BASE_URL + "/" + mentorToActive.getId() + "/activate", null)
                .andExpect(status().isOk());

        verify(mentorService).activeByMentorId((mentorToActive.getId()));
    }

    @Test
    @WithMockAdmin
    void shouldInactivateMentor_OK() throws Exception {
        final var mentorToInactive = buildUser();

        put(BASE_URL + "/" + mentorToInactive.getId() + "/inactivate", null)
                .andExpect(status().isOk());

        verify(mentorService).inactiveByMentorId((mentorToInactive.getId()));
    }

    @Test
    @WithMockAdmin
    void shouldFindMentorDetailById_OK() throws Exception {
        final var userMentorShip = buildUserMentorship();

        when(mentorService.findById(userMentorShip.getId())).thenReturn(userMentorShip);

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

        verify(mentorService).findById(userMentorShip.getId());
    }
}
