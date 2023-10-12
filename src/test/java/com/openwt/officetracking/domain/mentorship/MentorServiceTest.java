package com.openwt.officetracking.domain.mentorship;

import com.openwt.officetracking.domain.course.CourseService;
import com.openwt.officetracking.domain.course_assignment.CourseAssignmentService;
import com.openwt.officetracking.domain.course_status.CourseStatus;
import com.openwt.officetracking.domain.mentorship_status.CoachMentorMenteeStatus;
import com.openwt.officetracking.domain.role.Role;
import com.openwt.officetracking.domain.user.UserService;
import com.openwt.officetracking.error.BadRequestException;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MentorServiceTest {

    @Mock
    private CourseAssignmentService courseAssignmentService;

    @Mock
    private UserService userService;

    @Mock
    private CoachService coachService;

    @Mock
    private UserStore userStore;

    @Mock
    private CourseService courseService;

    @InjectMocks
    private MentorService mentorService;

    @Test
    void shouldAssignUserAsMentor_OK() {
        final var userUpdate = buildUser()
                .withRole(Role.USER);
        final var courseAssignment = buildCourseAssignment().withMentorId(userUpdate.getId());

        when(userService.findById(userUpdate.getId())).thenReturn(userUpdate);
        when(courseAssignmentService.create(argThat(courseArg ->
                courseArg.getMentorId().equals(courseAssignment.getMentorId())))).thenReturn(courseAssignment);

        mentorService.assignUserAsMentor(userUpdate.getId());

        verify(userService, times(2)).findById(userUpdate.getId());
        verify(courseAssignmentService).create(argThat(courseArg ->
                courseArg.getMentorId().equals(courseAssignment.getMentorId())));
    }

    @Test
    void shouldAssignUserAsMentor_WithoutUserId_ThroughBadRequestException() {
        assertThrows(BadRequestException.class, () -> mentorService.assignUserAsMentor(null));
    }

    @Test
    void shouldFindAll_OK() {
        final var userMentorShips = buildUserMentorShips();

        when(userStore.findAllMentors())
                .thenReturn(toUsers(userMentorShips));

        final var actual = mentorService.findAll();

        assertEquals(userMentorShips.size(), actual.size());
        assertEquals(userMentorShips.get(0).getId(), actual.get(0).getId());
        assertEquals(userMentorShips.get(0).getFirstName(), actual.get(0).getFirstName());
        assertEquals(userMentorShips.get(0).getLastName(), actual.get(0).getLastName());
        assertEquals(userMentorShips.get(0).getLastName(), actual.get(0).getLastName());
        assertEquals(userMentorShips.get(0).getPositionId(), actual.get(0).getPositionId());
        assertEquals(userMentorShips.get(0).getPersonalEmail(), actual.get(0).getPersonalEmail());
        assertEquals(userMentorShips.get(0).getCompanyEmail(), actual.get(0).getCompanyEmail());
        assertEquals(userMentorShips.get(0).getContractType(), actual.get(0).getContractType());
        assertEquals(userMentorShips.get(0).getDateOfBirth(), actual.get(0).getDateOfBirth());
        assertEquals(userMentorShips.get(0).getPhoto(), actual.get(0).getPhoto());
        assertEquals(userMentorShips.get(0).getQrCode(), actual.get(0).getQrCode());
        assertEquals(userMentorShips.get(0).getPhoneNumber(), actual.get(0).getPhoneNumber());

        verify(userStore).findAllMentors();
    }

    @Test
    void shouldActiveByMentorId_OK() {
        final var mentor = buildUser().withMentorStatus(CoachMentorMenteeStatus.INACTIVE);

        when(userService.findById(mentor.getId())).thenReturn(mentor);
        when(userStore.save(mentor)).thenReturn(mentor.withMentorStatus(CoachMentorMenteeStatus.ACTIVE));
        mentorService.activeByMentorId(mentor.getId());

        verify(userService).findById(mentor.getId());
        verify(userStore).save(mentor.withMentorStatus(CoachMentorMenteeStatus.ACTIVE));
    }

    @Test
    void shouldInactiveByMentorId_OK() {
        final var mentor = buildUser();
        final var course = buildCourse()
                .withStartAt(Instant.now().minus(11, ChronoUnit.HOURS))
                .withEndAt(Instant.now().minus(10, ChronoUnit.HOURS))
                .withStatus(CourseStatus.COMPLETED);

        when(userService.findById(mentor.getId())).thenReturn(mentor);
        when(courseService.findCoursesByMentorId(mentor.getId())).thenReturn(List.of(course));
        when(userStore.save(mentor)).thenReturn(mentor.withMentorStatus(CoachMentorMenteeStatus.INACTIVE));
        mentorService.inactiveByMentorId(mentor.getId());

        verify(userService).findById(mentor.getId());
        verify(userStore).save(mentor.withMentorStatus(CoachMentorMenteeStatus.INACTIVE));
    }
}
