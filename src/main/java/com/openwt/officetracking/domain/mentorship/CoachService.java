package com.openwt.officetracking.domain.mentorship;

import com.openwt.officetracking.domain.course.Course;
import com.openwt.officetracking.domain.course.CourseService;
import com.openwt.officetracking.domain.course_assignment.CourseAssignment;
import com.openwt.officetracking.domain.course_assignment.CourseAssignmentService;
import com.openwt.officetracking.domain.mentorship_status.CoachMentorMenteeStatus;
import com.openwt.officetracking.domain.mentorship_status.MentorshipStatus;
import com.openwt.officetracking.domain.position.PositionService;
import com.openwt.officetracking.domain.user.User;
import com.openwt.officetracking.domain.user.UserService;
import com.openwt.officetracking.persistent.user.UserStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

import static com.openwt.officetracking.domain.mentorship.CoachValidate.*;
import static com.openwt.officetracking.domain.mentorship.MenteeService.checkPermissionUserAsAdmin;
import static com.openwt.officetracking.domain.mentorship.UserMentorshipMapper.toUserMentorship;
import static com.openwt.officetracking.domain.mentorship.UserMentorshipMapper.toUserMentorshipDTOs;

@Service
@RequiredArgsConstructor
public class CoachService {

    private final UserStore userStore;

    private final CourseAssignmentService courseAssignmentService;

    private final UserService userService;

    private final CourseService courseService;

    private final PositionService positionService;

    public UserMentorShip assignUserAsCoach(final UUID userId) {
        validateUserId(userId);

        final User userAssign = userService.findById(userId);
        checkPermissionUserAsAdmin(userAssign);
        final CourseAssignment courseAssignment = CourseAssignment.builder()
                .coachId(userAssign.getId())
                .build();

        userAssign.setCoachStatus(CoachMentorMenteeStatus.ACTIVE);
        userAssign.setAssignedCoachAt(Instant.now());
        userStore.save(userAssign);

        courseAssignmentService.create(courseAssignment);

        return findById(userAssign.getId());
    }

    public List<UserMentorShip> findAll() {
        final List<UserMentorShip> userMentorShips = toUserMentorshipDTOs(userStore.findAllCoaches());

        userMentorShips.forEach(userMentorShip -> {
            updateMentorshipStatusAndNumberOfCourses(userMentorShip, courseAssignmentService::findByCoachId);
        });

        return userMentorShips;
    }

    public UserMentorShip findById(final UUID coachId) {
        final UserMentorShip userMentorShip = toUserMentorship(userService.findById(coachId));

        updateMentorshipStatusAndNumberOfCourses(userMentorShip, courseAssignmentService::findByCoachId);

        return userMentorShip;
    }

    public void activeByCoachId(final UUID coachId) {
        final User currentCoach = userService.findById(coachId);

        if (currentCoach.getCoachStatus() == CoachMentorMenteeStatus.ACTIVE) {
            throw supplyCoachIsAlreadyActive().get();
        }

        currentCoach.setCoachStatus(CoachMentorMenteeStatus.ACTIVE);
        userStore.save(currentCoach);
    }

    public void inactiveByCoachId(final UUID coachId) {
        final User currentCoach = userService.findById(coachId);

        if (currentCoach.getCoachStatus() == CoachMentorMenteeStatus.INACTIVE) {
            throw supplyCoachIsAlreadyInactive().get();
        }

        final List<Course> assignedCourses = courseService.findCoursesByCoachId(coachId);

        courseService.verifyIfCourseInProgress(assignedCourses);

        currentCoach.setCoachStatus(CoachMentorMenteeStatus.INACTIVE);
        userStore.save(currentCoach);
    }

    public void updateMentorshipStatusAndNumberOfCourses(final UserMentorShip userMentorShip, final Function<UUID, List<CourseAssignment>> findFunction) {
        final var numberOfCourses = findFunction.apply(userMentorShip.getId())
                .stream()
                .map(CourseAssignment::getCourseId)
                .filter(Objects::nonNull)
                .distinct()
                .count();

        final var mentorshipStatus = numberOfCourses > 0 ? MentorshipStatus.IN_COURSE : MentorshipStatus.NOT_IN_COURSE;

        userMentorShip.setMentorshipStatus(mentorshipStatus);
        userMentorShip.setNumberOfCourses((int) numberOfCourses);
        userMentorShip.setPositionName(positionService.findById(userMentorShip.getPositionId()).getName());
    }
}