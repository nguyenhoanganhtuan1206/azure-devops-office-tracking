package com.openwt.officetracking.domain.mentorship;

import com.openwt.officetracking.domain.course.CourseService;
import com.openwt.officetracking.domain.course_assignment.CourseAssignmentService;
import com.openwt.officetracking.domain.course_status.CourseStatus;
import com.openwt.officetracking.domain.mentorship_status.CoachMentorMenteeStatus;
import com.openwt.officetracking.domain.position.PositionService;
import com.openwt.officetracking.domain.role.Role;
import com.openwt.officetracking.domain.user.UserService;
import com.openwt.officetracking.persistent.user.UserStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static com.openwt.officetracking.domain.mentorship.UserMentorshipMapper.toUsers;
import static com.openwt.officetracking.fake.CourseAssignmentFakes.buildCourseAssignment;
import static com.openwt.officetracking.fake.CourseAssignmentFakes.buildCourseAssignments;
import static com.openwt.officetracking.fake.CourseFakes.buildCourse;
import static com.openwt.officetracking.fake.PositionFakes.buildPosition;
import static com.openwt.officetracking.fake.UserFakes.buildUser;
import static com.openwt.officetracking.fake.UserMentorshipFakes.buildUserMentorShips;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoachServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private UserStore userStore;

    @Mock
    private PositionService positionService;

    @Mock
    private CourseService courseService;

    @InjectMocks
    private CoachService coachService;

    @Mock
    private CourseAssignmentService courseAssignmentService;

    @Test
    void shouldAssignUserAsCoach_OK() {
        final var userAssign = buildUser()
                .withRole(Role.USER);
        final var courseAssignment = buildCourseAssignment()
                .withCoachId(userAssign.getId());
        final var position = buildPosition();

        when(userService.findById(userAssign.getId()))
                .thenReturn(userAssign);
        when(courseAssignmentService.create(argThat(courseArg ->
                courseArg.getCoachId().equals(courseAssignment.getCoachId())))).thenReturn(courseAssignment);
        when(positionService.findById(any(UUID.class)))
                .thenReturn(position);

        coachService.assignUserAsCoach(userAssign.getId());

        verify(userService, times(2)).findById(userAssign.getId());
        verify(courseAssignmentService).create(argThat(courseArg ->
                courseArg.getCoachId().equals(courseAssignment.getCoachId())));
    }

    @Test
    void shouldFindAll_OK() {
        final var userMentorShips = buildUserMentorShips();
        final var courseAssignments = buildCourseAssignments();
        final var position = buildPosition();

        when(userStore.findAllCoaches())
                .thenReturn(toUsers(userMentorShips));
        when(courseAssignmentService.findByCoachId(any(UUID.class)))
                .thenReturn(courseAssignments);
        when(positionService.findById(any(UUID.class)))
                .thenReturn(position);

        final var actual = coachService.findAll();

        assertEquals(userMentorShips.size(), actual.size());
        assertEquals(userMentorShips.get(0).getId(), actual.get(0).getId());
        assertEquals(userMentorShips.get(0).getFirstName(), actual.get(0).getFirstName());
        assertEquals(userMentorShips.get(0).getLastName(), actual.get(0).getLastName());
        assertEquals(userMentorShips.get(0).getLastName(), actual.get(0).getLastName());
        assertEquals(userMentorShips.get(0).getMentorshipStatus(), actual.get(0).getMentorshipStatus());
        assertEquals(userMentorShips.get(0).getPositionId(), actual.get(0).getPositionId());
        assertEquals(userMentorShips.get(0).getPersonalEmail(), actual.get(0).getPersonalEmail());
        assertEquals(userMentorShips.get(0).getCompanyEmail(), actual.get(0).getCompanyEmail());
        assertEquals(userMentorShips.get(0).getContractType(), actual.get(0).getContractType());
        assertEquals(userMentorShips.get(0).getDateOfBirth(), actual.get(0).getDateOfBirth());
        assertEquals(userMentorShips.get(0).getPhoto(), actual.get(0).getPhoto());
        assertEquals(userMentorShips.get(0).getQrCode(), actual.get(0).getQrCode());
        assertEquals(userMentorShips.get(0).getPhoneNumber(), actual.get(0).getPhoneNumber());

        verify(userStore).findAllCoaches();
        verify(courseAssignmentService, times(4)).findByCoachId(any(UUID.class));
    }

    @Test
    void shouldActiveByCoachId_OK() {
        final var coach = buildUser().withCoachStatus(CoachMentorMenteeStatus.INACTIVE);

        when(userService.findById(coach.getId())).thenReturn(coach);
        when(userStore.save(coach)).thenReturn(coach.withCoachStatus(CoachMentorMenteeStatus.ACTIVE));
        coachService.activeByCoachId(coach.getId());

        verify(userService).findById(coach.getId());
        verify(userStore).save(coach.withCoachStatus(CoachMentorMenteeStatus.ACTIVE));
    }

    @Test
    void shouldInactiveByCoachId_OK() {
        final var coach = buildUser();
        final var course = buildCourse()
                .withStartAt(Instant.now().minus(11, ChronoUnit.HOURS))
                .withEndAt(Instant.now().minus(10, ChronoUnit.HOURS))
                .withStatus(CourseStatus.COMPLETED);

        when(userService.findById(coach.getId())).thenReturn(coach);
        when(courseService.findCoursesByCoachId(coach.getId())).thenReturn(List.of(course));
        when(userStore.save(coach)).thenReturn(coach.withCoachStatus(CoachMentorMenteeStatus.INACTIVE));
        coachService.inactiveByCoachId(coach.getId());

        verify(userService).findById(coach.getId());
        verify(userStore).save(coach.withCoachStatus(CoachMentorMenteeStatus.INACTIVE));
    }
}
