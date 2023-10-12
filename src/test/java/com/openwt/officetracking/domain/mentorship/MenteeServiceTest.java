package com.openwt.officetracking.domain.mentorship;

import com.openwt.officetracking.domain.course.CourseService;
import com.openwt.officetracking.domain.course_assignment.CourseAssignmentService;
import com.openwt.officetracking.domain.course_status.CourseStatus;
import com.openwt.officetracking.domain.mentorship_status.CoachMentorMenteeStatus;
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

import static com.openwt.officetracking.domain.mentorship.UserMentorshipMapper.toUsers;
import static com.openwt.officetracking.fake.CourseAssignmentFakes.buildCourseAssignment;
import static com.openwt.officetracking.fake.CourseFakes.buildCourse;
import static com.openwt.officetracking.fake.UserFakes.buildUser;
import static com.openwt.officetracking.fake.UserMentorshipFakes.buildUserMentorShips;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenteeServiceTest {
    @Mock
    private UserStore userStore;

    @Mock
    private UserService userService;

    @Mock
    private CourseService courseService;

    @Mock
    private CoachService coachService;

    @InjectMocks
    private MenteeService menteeService;

    @Mock
    private CourseAssignmentService courseAssignmentService;

    @Test
    void shouldAssignUserAsMentee_OK() {
        final var userAssign = buildUser()
                .withRole(Role.USER);
        final var courseAssignment = buildCourseAssignment()
                .withMenteeId(userAssign.getId());

        when(userService.findById(userAssign.getId()))
                .thenReturn(userAssign);
        when(courseAssignmentService.create(argThat(courseArg ->
                courseArg.getMenteeId().equals(courseAssignment.getMenteeId())))).thenReturn(courseAssignment);

        menteeService.assignUserAsMentee(userAssign.getId());

        verify(userService, times(2)).findById(userAssign.getId());
        verify(courseAssignmentService).create(argThat(courseArg ->
                courseArg.getMenteeId().equals(courseAssignment.getMenteeId())));
    }

    @Test
    void shouldFindAll_OK() {
        final var userMentorShips = buildUserMentorShips();

        when(userStore.findAllMentees())
                .thenReturn(toUsers(userMentorShips));

        final var actual = menteeService.findAll();

        assertEquals(userMentorShips.size(), actual.size());
        assertEquals(userMentorShips.get(0).getId(), actual.get(0).getId());
        assertEquals(userMentorShips.get(0).getFirstName(), actual.get(0).getFirstName());
        assertEquals(userMentorShips.get(0).getLastName(), actual.get(0).getLastName());
        assertEquals(userMentorShips.get(0).getPositionId(), actual.get(0).getPositionId());
        assertEquals(userMentorShips.get(0).getPersonalEmail(), actual.get(0).getPersonalEmail());
        assertEquals(userMentorShips.get(0).getCompanyEmail(), actual.get(0).getCompanyEmail());
        assertEquals(userMentorShips.get(0).getContractType(), actual.get(0).getContractType());
        assertEquals(userMentorShips.get(0).getDateOfBirth(), actual.get(0).getDateOfBirth());
        assertEquals(userMentorShips.get(0).getPhoto(), actual.get(0).getPhoto());
        assertEquals(userMentorShips.get(0).getQrCode(), actual.get(0).getQrCode());
        assertEquals(userMentorShips.get(0).getPhoneNumber(), actual.get(0).getPhoneNumber());

        verify(userStore).findAllMentees();
    }

    @Test
    void shouldActiveByMenteeId_OK() {
        final var mentee = buildUser().withMenteeStatus(CoachMentorMenteeStatus.INACTIVE);

        when(userService.findById(mentee.getId())).thenReturn(mentee);

        menteeService.activeByMenteeId(mentee.getId());

        verify(userService).findById(mentee.getId());
        verify(userStore).save(mentee.withMenteeStatus(CoachMentorMenteeStatus.ACTIVE));
    }

    @Test
    void shouldInactiveByMenteeId_OK() {
        final var mentee = buildUser();
        final var course = buildCourse()
                .withStartAt(Instant.now().minus(11, ChronoUnit.HOURS))
                .withEndAt(Instant.now().minus(10, ChronoUnit.HOURS))
                .withStatus(CourseStatus.COMPLETED);

        when(userService.findById(mentee.getId())).thenReturn(mentee);
        when(courseService.findCoursesByMenteeId(mentee.getId())).thenReturn(List.of(course));
        menteeService.inactiveByMenteeId(mentee.getId());

        verify(userService).findById(mentee.getId());
        verify(courseService).findCoursesByMenteeId(mentee.getId());
    }
}
