package com.openwt.officetracking.domain.course;

import com.openwt.officetracking.domain.ability_result.AbilityResultService;
import com.openwt.officetracking.domain.course_assign.CourseAssign;
import com.openwt.officetracking.domain.course_assignment.CourseAssignment;
import com.openwt.officetracking.domain.course_assignment.CourseAssignmentService;
import com.openwt.officetracking.domain.course_status.CourseStatus;
import com.openwt.officetracking.domain.user.UserService;
import com.openwt.officetracking.error.BadRequestException;
import com.openwt.officetracking.error.NotFoundException;
import com.openwt.officetracking.persistent.course.CourseStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.openwt.officetracking.fake.CoachCourseFakes.buildCoachCourses;
import static com.openwt.officetracking.fake.CourseAssignmentFakes.buildCourseAssignment;
import static com.openwt.officetracking.fake.CourseAssignmentFakes.buildCourseAssignments;
import static com.openwt.officetracking.fake.CourseFakes.buildCourse;
import static com.openwt.officetracking.fake.CourseFakes.buildCourses;
import static com.openwt.officetracking.fake.CourseRequestFakes.buildCourseRequest;
import static com.openwt.officetracking.fake.MenteeCourseFakes.buildMenteeCourses;
import static com.openwt.officetracking.fake.MentorCourseFakes.buildMentorCourses;
import static com.openwt.officetracking.fake.UserFakes.buildUser;
import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseAssignmentService courseAssignmentService;

    @Mock
    private CourseStore courseStore;

    @InjectMocks
    private CourseService courseService;

    @Mock
    private UserService userService;

    @Mock
    private AbilityResultService abilityResultService;


    @Test
    void shouldFindCourseByID_OK() {
        final var course = buildCourse();

        when(courseStore.findById(course.getId()))
                .thenReturn(Optional.of(course));

        final var actual = courseService.findById(course.getId());

        assertEquals(course, actual);
        verify(courseStore).findById(course.getId());
    }

    @Test
    void shouldFindCourseByID_ThroughNotFound() {
        final var courseId = randomUUID();

        when(courseStore.findById(courseId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> courseService.findById(courseId));

        verify(courseStore).findById(courseId);
    }

    @Test
    void shouldFindDetailCourseById_OK() {
        final var mentor = buildUser();
        final var course = buildCourse();
        final var courseAssignment = buildCourseAssignment()
                .withCourseId(course.getId());

        when(courseStore.findById(course.getId()))
                .thenReturn(Optional.of(course));
        when(courseAssignmentService.findByCourseId(course.getId()))
                .thenReturn(List.of(courseAssignment));
        when(userService.findById(any()))
                .thenReturn(mentor);

        final CourseDetailResponse result = courseService.findDetailCourseById(course.getId());

        assertEquals(1, result.getCourseAssignments().size());

        verify(courseStore).findById(course.getId());
        verify(courseAssignmentService).findByCourseId(course.getId());
        verify(userService, times(1)).findById(courseAssignment.getMentorId());
    }

    @Test
    public void shouldFindAll_OK() {
        final var courses = buildCourses();
        final var courseAssignments = buildCourseAssignments();

        when(courseStore.findAll()).thenReturn(courses);

        courses.forEach(course -> when(courseAssignmentService.findByCourseId(course.getId())).thenReturn(courseAssignments));

        final var actual = courseService.findAll();

        assertEquals(courses.size(), actual.size());

        verify(courseStore).findAll();
        courses.forEach(course -> verify(courseAssignmentService).findByCourseId(course.getId()));
    }

    @Test
    public void shouldFindByMenteeId_OK() {
        final var menteeId = randomUUID();
        final var courses = buildCourses();
        final var courseAssignments = buildCourseAssignments();
        final var menteeCourses = buildMenteeCourses();

        when(courseStore.findByMenteeId(menteeId)).thenReturn(courses);
        courses.forEach(course -> when(courseAssignmentService.findByCourseId(course.getId())).thenReturn(courseAssignments));
        when(courseAssignmentService.findByMenteeId(menteeId)).thenReturn(courseAssignments);

        final var actual = courseService.findByMenteeId(menteeId);

        assertEquals(menteeCourses.size(), actual.size());

        verify(courseStore).findByMenteeId(menteeId);
        courses.forEach(course -> verify(courseAssignmentService).findByCourseId(course.getId()));
        verify(courseAssignmentService).findByMenteeId(menteeId);
    }

    @Test
    public void shouldFindByMentorId_OK() {
        final var mentorId = randomUUID();
        final var courses = buildCourses();
        final var courseAssignments = buildCourseAssignments();
        final var mentorCourses = buildMentorCourses();

        when(courseStore.findByMentorId(mentorId)).thenReturn(courses);
        courses.forEach(course -> when(courseAssignmentService.findByCourseId(course.getId())).thenReturn(courseAssignments));
        when(courseAssignmentService.findByMentorId(mentorId)).thenReturn(courseAssignments);

        final var actual = courseService.findByMentorId(mentorId);

        assertEquals(mentorCourses.size(), actual.size());

        verify(courseStore).findByMentorId(mentorId);
        courses.forEach(course -> verify(courseAssignmentService).findByCourseId(course.getId()));
        verify(courseAssignmentService).findByMentorId(mentorId);
    }

    @Test
    public void shouldFindByCoachId_OK() {
        final var coachId = randomUUID();
        final var courses = buildCourses();
        final var courseAssignments = buildCourseAssignments();
        final var coachCourses = buildCoachCourses();

        when(courseStore.findByCoachId(coachId)).thenReturn(courses);
        courses.forEach(course -> when(courseAssignmentService.findByCourseId(course.getId())).thenReturn(courseAssignments));
        when(courseAssignmentService.findByCoachId(coachId)).thenReturn(courseAssignments);

        final var actual = courseService.findByCoachId(coachId);

        assertEquals(coachCourses.size(), actual.size());

        verify(courseStore).findByCoachId(coachId);
        courses.forEach(course -> verify(courseAssignmentService).findByCourseId(course.getId()));
        verify(courseAssignmentService).findByCoachId(coachId);
    }

    @Test
    void shouldFindByName_OK() {
        final var course = buildCourse();

        when(courseStore.findByName(course.getName()))
                .thenReturn(Optional.of(course));

        final var actual = courseService.findByName(course.getName());
        assertEquals(course, actual);

        verify(courseStore).findByName(course.getName());
    }

    @Test
    void shouldFindByName_ThroughNotFound() {
        final var courseName = randomAlphabetic(9);

        when(courseStore.findByName(courseName))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> courseService.findByName(courseName));

        verify(courseStore).findByName(courseName);
    }

    @Test
    void shouldCreateCourse_OK() {
        final var courseRequest = buildCourseRequest();
        final var user = buildUser();
        final var course = buildCourse()
                .withName(courseRequest.getName());
        final var courseAssignment = buildCourseAssignment()
                .withCourseId(course.getId());

        when(courseStore.findByName(course.getName()))
                .thenReturn(Optional.empty());
        when(courseStore.save(any(Course.class)))
                .thenReturn(course);
        when(userService.findById(any(UUID.class)))
                .thenReturn(user);
        when(courseAssignmentService.create(any(CourseAssignment.class)))
                .thenReturn(courseAssignment);
        doNothing().when(abilityResultService)
                .createAbilityResultByCourseAssignment(any(UUID.class));

        final var newCourse = courseService.create(courseRequest);
        assertEquals(course, newCourse);

        verify(courseStore).findByName(course.getName());
        verify(courseStore).save(any(Course.class));
        verify(abilityResultService, times(16)).createAbilityResultByCourseAssignment(any(UUID.class));
    }

    @Test
    void shouldCreateCourse_WithCourseAlreadyExisted_ThroughBadRequest() {
        final var courseRequest = buildCourseRequest();
        final var course = buildCourse()
                .withName(courseRequest.getName());

        when(courseStore.findByName(course.getName()))
                .thenReturn(Optional.of(course));

        assertThrows(BadRequestException.class, () -> courseService.create(courseRequest));
    }

    @Test
    void shouldCreateCourse_WithEndDateBeforeCurrentTime_ThroughBadRequest() {
        final var courseRequest = buildCourseRequest()
                .withEndAt(Instant.now().minus(30, ChronoUnit.MINUTES));
        final var user = buildUser();
        final var course = buildCourse()
                .withName(courseRequest.getName());

        when(courseStore.findByName(course.getName()))
                .thenReturn(Optional.empty());
        when(userService.findById(any(UUID.class)))
                .thenReturn(user);

        assertThrows(BadRequestException.class, () -> courseService.create(courseRequest));

        verify(courseStore).findByName(course.getName());
        verify(userService, times(4)).findById(any(UUID.class));
    }

    @Test
    void shouldCreateCourse_WithStartDateAfterEndDate_ThroughBadRequest() {
        final var courseRequest = buildCourseRequest()
                .withStartAt(Instant.now().plus(30, ChronoUnit.MINUTES));
        final var user = buildUser();
        final var course = buildCourse()
                .withName(courseRequest.getName());

        when(courseStore.findByName(course.getName()))
                .thenReturn(Optional.empty());
        when(userService.findById(any(UUID.class)))
                .thenReturn(user);

        assertThrows(BadRequestException.class, () -> courseService.create(courseRequest));

        verify(courseStore).findByName(course.getName());
        verify(userService, times(4)).findById(any(UUID.class));
    }

    @Test
    public void shouldFindCourseByMenteeId_OK() {
        final var courses = buildCourses();
        final var mentee = buildUser();

        when(courseStore.findCoursesByMenteeId(mentee.getId()))
                .thenReturn(courses);

        final var actual = courseService.findCoursesByMenteeId(mentee.getId());

        assertEquals(courses.size(), actual.size());

        assertEquals(courses.get(0).getId(), actual.get(0).getId());
        assertEquals(courses.get(0).getName(), actual.get(0).getName());
        assertEquals(courses.get(0).getStartAt(), actual.get(0).getStartAt());
        assertEquals(courses.get(0).getEndAt(), actual.get(0).getEndAt());
        assertEquals(courses.get(0).getStatus(), actual.get(0).getStatus());

        verify(courseStore).findCoursesByMenteeId(mentee.getId());
    }

    @Test
    public void shouldFindCourseByMentorId_OK() {
        final var courses = buildCourses();
        final var mentor = buildUser();

        when(courseStore.findCoursesByMentorId(mentor.getId()))
                .thenReturn(courses);

        final var actual = courseService.findCoursesByMentorId(mentor.getId());

        assertEquals(courses.size(), actual.size());

        assertEquals(courses.get(0).getId(), actual.get(0).getId());
        assertEquals(courses.get(0).getName(), actual.get(0).getName());
        assertEquals(courses.get(0).getStartAt(), actual.get(0).getStartAt());
        assertEquals(courses.get(0).getEndAt(), actual.get(0).getEndAt());
        assertEquals(courses.get(0).getStatus(), actual.get(0).getStatus());

        verify(courseStore).findCoursesByMentorId(mentor.getId());
    }

    @Test
    public void shouldUpdateCourse_OK() {
        final var courseId = randomUUID();
        final var courseRequest = buildCourseRequest();
        final var course = buildCourse()
                .withName(courseRequest.getName())
                .withAssign(CourseAssign.TO_BE_DEFINED)
                .withDescription(courseRequest.getDescription())
                .withStartAt(courseRequest.getStartAt())
                .withEndAt(courseRequest.getEndAt());
        final var courseAssignment = buildCourseAssignment()
                .withCourseId(course.getId());
        final var user = buildUser();

        when(courseStore.findById(courseId))
                .thenReturn(Optional.of(course));
        when(userService.findById(any(UUID.class)))
                .thenReturn(user);
        when(courseStore.save(any(Course.class)))
                .thenReturn(course);
        when(courseAssignmentService.create(any(CourseAssignment.class)))
                .thenReturn(courseAssignment);

        final var actual = courseService.update(courseId, courseRequest);

        assertEquals(course.getId().toString(), actual.getId().toString());
        assertEquals(course.getName(), actual.getName());
        assertEquals(course.getStartAt().toString(), actual.getStartAt().toString());
        assertEquals(course.getEndAt().toString(), actual.getEndAt().toString());
        assertEquals(course.getStatus().toString(), actual.getStatus().toString());
        assertEquals(course.getAssign().toString(), actual.getAssign().toString());
        assertEquals(course.getCreatedAt().toString(), actual.getCreatedAt().toString());
        assertEquals(course.getDescription(), actual.getDescription());

        verify(courseStore, times(1)).findById(courseId);
        verify(courseStore).save(any(Course.class));
        verify(courseAssignmentService, times(16)).create(any(CourseAssignment.class));
    }

    @Test
    public void shouldUpdateCourse_ThrowNameIsBlank() {
        final var courseId = randomUUID();
        final var courseRequest = buildCourseRequest()
                .withName(null);

        assertThrows(BadRequestException.class, () -> courseService.update(courseId, courseRequest));
    }

    @Test
    public void shouldUpdateCourse_ThrowNameExisted() {
        final var courseId = randomUUID();
        final var courseExisted = buildCourse();
        final var courseRequest = buildCourseRequest()
                .withName(courseExisted.getName());
        final var course = buildCourse();
        final var user = buildUser();

        when(userService.findById(any(UUID.class)))
                .thenReturn(user);
        when(courseStore.findById(courseId)).thenReturn(Optional.of(course));
        when(courseStore.findByName(courseRequest.getName()))
                .thenReturn(Optional.of(courseExisted));

        assertThrows(BadRequestException.class, () -> courseService.update(courseId, courseRequest));

        verify(courseStore).findById(courseId);
        verify(courseStore).findByName(courseRequest.getName());
    }

    @Test
    public void shouldUpdateCourse_ThrowEndDateBeforeCurrentTime() {
        final var courseId = randomUUID();
        final var courseRequest = buildCourseRequest()
                .withEndAt(Instant.now().minus(30, ChronoUnit.MINUTES));
        final var user = buildUser();

        when(userService.findById(any(UUID.class)))
                .thenReturn(user);

        assertThrows(BadRequestException.class, () -> courseService.update(courseId, courseRequest));

        verify(userService, times(4)).findById(any(UUID.class));
    }

    @Test
    public void shouldUpdateCourse_ThrowStartDateAfterEndDate() {
        final var courseId = randomUUID();
        final var courseRequest = buildCourseRequest()
                .withStartAt(Instant.now().plus(30, ChronoUnit.MINUTES));
        final var user = buildUser();

        when(userService.findById(any(UUID.class)))
                .thenReturn(user);

        assertThrows(BadRequestException.class, () -> courseService.update(courseId, courseRequest));

        verify(userService, times(4)).findById(any(UUID.class));
    }

    @Test
    public void shouldUpdateCourse_ThrowCourseNotFound() {
        final var courseId = randomUUID();
        final var courseRequest = buildCourseRequest();
        final var user = buildUser();

        when(userService.findById(any(UUID.class)))
                .thenReturn(user);
        when(courseStore.findById(courseId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> courseService.update(courseId, courseRequest));

        verify(courseStore).findById(courseId);
        verify(userService, times(4)).findById(any(UUID.class));
    }

    @Test
    void shouldDeleteById_WithStatusWaiting_OK() {
        final var course = buildCourse()
                .withStartAt(null)
                .withEndAt(null)
                .withStatus(CourseStatus.WAITING);

        when(courseStore.findById(course.getId()))
                .thenReturn(Optional.of(course));

        courseService.deleteById(course.getId());

        verify(courseStore).findById(course.getId());
    }

    @Test
    void shouldDeleteById_WithStatusInProgress_ThroughBadRequest() {
        final Course course = buildCourse()
                .withStatus(CourseStatus.IN_PROGRESS);

        when(courseStore.findById(course.getId()))
                .thenReturn(Optional.of(course));

        assertThrows(BadRequestException.class, () -> courseService.deleteById(course.getId()));

        verify(courseStore).findById(course.getId());
    }
}
