package com.openwt.officetracking.domain.mentorship;

import com.openwt.officetracking.domain.course.Course;
import com.openwt.officetracking.domain.course.CourseService;
import com.openwt.officetracking.domain.course_assignment.CourseAssignment;
import com.openwt.officetracking.domain.course_assignment.CourseAssignmentService;
import com.openwt.officetracking.domain.mentorship_status.CoachMentorMenteeStatus;
import com.openwt.officetracking.domain.role.Role;
import com.openwt.officetracking.domain.user.User;
import com.openwt.officetracking.domain.user.UserService;
import com.openwt.officetracking.persistent.user.UserStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.openwt.officetracking.domain.mentorship.MenteeValidate.*;
import static com.openwt.officetracking.domain.mentorship.MentorshipError.supplyPermissionUserAsAdmin;
import static com.openwt.officetracking.domain.mentorship.UserMentorshipMapper.toUserMentorship;
import static com.openwt.officetracking.domain.mentorship.UserMentorshipMapper.toUserMentorshipDTOs;

@Service
@RequiredArgsConstructor
public class MenteeService {

    private final UserStore userStore;

    private final CourseAssignmentService courseAssignmentService;

    private final UserService userService;

    private final CourseService courseService;

    private final CoachService coachService;

    public List<UserMentorShip> findAll() {
        final List<UserMentorShip> userMentorShips = toUserMentorshipDTOs(userStore.findAllMentees());

        userMentorShips.forEach(userMentorShip -> {
            coachService.updateMentorshipStatusAndNumberOfCourses(userMentorShip, courseAssignmentService::findByMenteeId);
        });

        return userMentorShips;
    }

    public UserMentorShip findById(final UUID menteeId) {
        final UserMentorShip userMentorShip = toUserMentorship(userService.findById(menteeId));

        coachService.updateMentorshipStatusAndNumberOfCourses(userMentorShip, courseAssignmentService::findByMenteeId);

        return userMentorShip;
    }

    public UserMentorShip assignUserAsMentee(final UUID userId) {
        validateUserId(userId);

        final User userAssign = userService.findById(userId);
        checkPermissionUserAsAdmin(userAssign);
        final CourseAssignment courseAssignment = CourseAssignment.builder()
                .menteeId(userAssign.getId())
                .build();

        userAssign.setMenteeStatus(CoachMentorMenteeStatus.ACTIVE);
        userAssign.setAssignedMenteeAt(Instant.now());
        userStore.save(userAssign);

        courseAssignmentService.create(courseAssignment);

        return findById(userAssign.getId());
    }

    public void activeByMenteeId(final UUID menteeId) {
        final User currentMentee = userService.findById(menteeId);

        if (currentMentee.getMenteeStatus() == CoachMentorMenteeStatus.ACTIVE) {
            throw supplyMenteeIsAlreadyActive().get();
        }

        currentMentee.setMenteeStatus(CoachMentorMenteeStatus.ACTIVE);
        userStore.save(currentMentee);
    }

    public void inactiveByMenteeId(final UUID menteeId) {
        final User currentMentee = userService.findById(menteeId);

        if (currentMentee.getMenteeStatus() == CoachMentorMenteeStatus.INACTIVE) {
            throw supplyMenteeIsAlreadyInactive().get();
        }

        final List<Course> assignedCourses = courseService.findCoursesByMenteeId(menteeId);

        courseService.verifyIfCourseInProgress(assignedCourses);

        currentMentee.setMenteeStatus(CoachMentorMenteeStatus.INACTIVE);
        userStore.save(currentMentee);
    }

    public static void checkPermissionUserAsAdmin(final User user) {
        if (user.getRole() == Role.ADMIN) {
            throw supplyPermissionUserAsAdmin().get();
        }
    }
}