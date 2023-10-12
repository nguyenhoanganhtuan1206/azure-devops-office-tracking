package com.openwt.officetracking.domain.mentorship;

import com.openwt.officetracking.domain.course.Course;
import com.openwt.officetracking.domain.course.CourseService;
import com.openwt.officetracking.domain.course_assignment.CourseAssignment;
import com.openwt.officetracking.domain.course_assignment.CourseAssignmentService;
import com.openwt.officetracking.domain.mentorship_status.CoachMentorMenteeStatus;
import com.openwt.officetracking.domain.user.User;
import com.openwt.officetracking.domain.user.UserService;
import com.openwt.officetracking.persistent.user.UserStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.openwt.officetracking.domain.mentorship.MenteeService.checkPermissionUserAsAdmin;
import static com.openwt.officetracking.domain.mentorship.MentorValidate.*;
import static com.openwt.officetracking.domain.mentorship.UserMentorshipMapper.toUserMentorship;
import static com.openwt.officetracking.domain.mentorship.UserMentorshipMapper.toUserMentorshipDTOs;

@Service
@RequiredArgsConstructor
public class MentorService {

    private final UserStore userStore;

    private final CourseAssignmentService courseAssignmentService;

    private final UserService userService;

    private final CourseService courseService;

    private final CoachService coachService;

    public List<UserMentorShip> findAll() {
        final List<UserMentorShip> userMentorShips = toUserMentorshipDTOs(userStore.findAllMentors());

        userMentorShips.forEach(userMentorShip -> {
            coachService.updateMentorshipStatusAndNumberOfCourses(userMentorShip, courseAssignmentService::findByMentorId);
        });

        return userMentorShips;
    }

    public UserMentorShip findById(final UUID menteeId) {
        final UserMentorShip userMentorShip = toUserMentorship(userService.findById(menteeId));

        coachService.updateMentorshipStatusAndNumberOfCourses(userMentorShip, courseAssignmentService::findByMentorId);

        return userMentorShip;
    }

    public void inactiveByMentorId(final UUID mentorId) {
        final User currentMentor = userService.findById(mentorId);

        if (currentMentor.getMentorStatus() == CoachMentorMenteeStatus.INACTIVE) {
            throw supplyMentorIsAlreadyInactive().get();
        }

        final List<Course> assignedCourses = courseService.findCoursesByMentorId(mentorId);

        courseService.verifyIfCourseInProgress(assignedCourses);

        currentMentor.setMentorStatus(CoachMentorMenteeStatus.INACTIVE);
        userStore.save(currentMentor);
    }

    public UserMentorShip assignUserAsMentor(final UUID userId) {
        validateUserId(userId);

        final User userUpdate = userService.findById(userId);
        checkPermissionUserAsAdmin(userUpdate);
        final CourseAssignment courseAssignment = CourseAssignment.builder()
                .mentorId(userUpdate.getId())
                .build();

        userUpdate.setMentorStatus(CoachMentorMenteeStatus.ACTIVE);
        userUpdate.setAssignedMentorAt(Instant.now());
        userStore.save(userUpdate);

        courseAssignmentService.create(courseAssignment);

        return findById(userUpdate.getId());
    }

    public void activeByMentorId(final UUID mentorId) {
        final User currentMentor = userService.findById(mentorId);

        if (currentMentor.getMentorStatus() == CoachMentorMenteeStatus.ACTIVE) {
            throw supplyMentorIsAlreadyActive().get();
        }

        currentMentor.setMentorStatus(CoachMentorMenteeStatus.ACTIVE);
        userStore.save(currentMentor);
    }
}
