package com.openwt.officetracking.api.course;

import com.openwt.officetracking.api.AbstractControllerTest;
import com.openwt.officetracking.api.WithMockAdmin;
import com.openwt.officetracking.domain.auth.AuthsProvider;
import com.openwt.officetracking.domain.course.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.openwt.officetracking.fake.CourseDetailFakes.buildCourseDetailResponse;
import static com.openwt.officetracking.fake.CourseFakes.buildCourse;
import static com.openwt.officetracking.fake.CourseFakes.buildCourses;
import static com.openwt.officetracking.fake.CourseRequestFakes.buildCourseRequest;
import static com.openwt.officetracking.fake.MentorCourseFakes.buildMentorCourses;
import static java.util.UUID.randomUUID;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourseController.class)
@ExtendWith(SpringExtension.class)
class CourseControllerTest extends AbstractControllerTest {

    private static final String BASE_URL = "/api/v1/courses";

    @MockBean
    private AuthsProvider authsProvider;

    @MockBean
    private CourseService courseService;

    @BeforeEach
    void init() {
        when(authsProvider.getCurrentAuthentication()).thenCallRealMethod();
    }

    @Test
    @WithMockAdmin
    void shouldFindAll_OK() throws Exception {
        final var courses = buildCourses();

        when(courseService.findAll()).thenReturn(courses);

        get(BASE_URL)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(courses.size()))
                .andExpect(jsonPath("$[0].id").value(courses.get(0).getId().toString()))
                .andExpect(jsonPath("$[0].name").value(courses.get(0).getName()))
                .andExpect(jsonPath("$[0].startAt").value(courses.get(0).getStartAt().toString()))
                .andExpect(jsonPath("$[0].endAt").value(courses.get(0).getEndAt().toString()))
                .andExpect(jsonPath("$[0].status").value(courses.get(0).getStatus().toString()))
                .andExpect(jsonPath("$[0].assign").value(courses.get(0).getAssign().toString()));

        verify(courseService).findAll();
    }

    @Test
    @WithMockAdmin
    void shouldFindByMentorId_OK() throws Exception {
        final var mentorId = randomUUID();
        final var mentorCourses = buildMentorCourses();

        when(courseService.findByMentorId(mentorId)).thenReturn(mentorCourses);

        get(BASE_URL + "/" + mentorId + "/mentor")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(mentorCourses.size()))
                .andExpect(jsonPath("$[0].id").value(mentorCourses.get(0).getId().toString()))
                .andExpect(jsonPath("$[0].name").value(mentorCourses.get(0).getName()))
                .andExpect(jsonPath("$[0].startAt").value(mentorCourses.get(0).getStartAt().toString()))
                .andExpect(jsonPath("$[0].endAt").value(mentorCourses.get(0).getEndAt().toString()))
                .andExpect(jsonPath("$[0].status").value(mentorCourses.get(0).getStatus().toString()))
                .andExpect(jsonPath("$[0].mentees[0].menteeId").value(mentorCourses.get(0).getCourseMentees().get(0).getMenteeId().toString()))
                .andExpect(jsonPath("$[0].mentees[0].firstName").value(mentorCourses.get(0).getCourseMentees().get(0).getFirstName()))
                .andExpect(jsonPath("$[0].mentees[0].lastName").value(mentorCourses.get(0).getCourseMentees().get(0).getLastName()));

        verify(courseService).findByMentorId(mentorId);
    }

    @Test
    @WithMockAdmin
    void shouldFindById_OK() throws Exception {
        final var course = buildCourse();
        final var courseDetailResponse = buildCourseDetailResponse();

        when(courseService.findDetailCourseById(course.getId()))
                .thenReturn(courseDetailResponse);

        get(BASE_URL + "/" + course.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(courseDetailResponse.getId().toString()))
                .andExpect(jsonPath("$.name").value(courseDetailResponse.getName()))
                .andExpect(jsonPath("$.description").value(courseDetailResponse.getDescription()))
                .andExpect(jsonPath("$.startAt").value(courseDetailResponse.getStartAt().toString()))
                .andExpect(jsonPath("$.endAt").value(courseDetailResponse.getEndAt().toString()))
                .andExpect(jsonPath("$.updatedAt").value(courseDetailResponse.getUpdatedAt().toString()))
                .andExpect(jsonPath("$.courseAssignments.length()").value(courseDetailResponse.getCourseAssignments().size()));

        verify(courseService).findDetailCourseById(course.getId());
    }

    @Test
    @WithMockAdmin
    public void shouldUpdate_OK() throws Exception {
        final var courseId = randomUUID();
        final var courseRequest = buildCourseRequest();
        final var courseResponse = buildCourse()
                .withId(courseId)
                .withName(courseRequest.getName())
                .withStartAt(courseRequest.getStartAt())
                .withEndAt(courseRequest.getEndAt());

        when(courseService.update(eq(courseId), argThat(course -> course.getName().equals(courseRequest.getName())))).thenReturn(courseResponse);

        put(BASE_URL + "/" + courseId, courseRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(courseResponse.getId().toString()))
                .andExpect(jsonPath("$.name").value(courseResponse.getName()))
                .andExpect(jsonPath("$.startAt").value(courseResponse.getStartAt().toString()))
                .andExpect(jsonPath("$.endAt").value(courseResponse.getEndAt().toString()))
                .andExpect(jsonPath("$.status").value(courseResponse.getStatus().toString()))
                .andExpect(jsonPath("$.assign").value(courseResponse.getAssign().toString()));

        verify(courseService).update(eq(courseId), argThat(course -> course.getName().equals(courseRequest.getName())));
    }

    @Test
    @WithMockAdmin
    void shouldDeleteCourse_OK() throws Exception {
        final var course = buildCourse();

        doNothing().when(courseService).deleteById(course.getId());

        delete(BASE_URL + "/" + course.getId())
                .andExpect(status().isOk());

        verify(courseService).deleteById(course.getId());
    }
}
