package com.openwt.officetracking.persistent.course;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CourseRepository extends CrudRepository<CourseEntity, UUID> {

    List<CourseEntity> findAllByOrderByCreatedAtDesc();

    @Query("SELECT DISTINCT c " +
            "FROM CourseEntity c " +
            "INNER JOIN CourseAssignmentEntity ca ON c.id = ca.courseId " +
            "WHERE ca.menteeId = :menteeId " +
            "ORDER BY c.createdAt desc")
    List<CourseEntity> findByMenteeId(final UUID menteeId);

    @Query("SELECT DISTINCT c " +
            "FROM CourseEntity c " +
            "INNER JOIN CourseAssignmentEntity ca ON c.id = ca.courseId " +
            "WHERE ca.mentorId = :mentorId " +
            "ORDER BY c.createdAt desc")
    List<CourseEntity> findByMentorId(final UUID mentorId);

    @Query("SELECT DISTINCT c " +
            "FROM CourseEntity c " +
            "WHERE c.id IN (SELECT ca.courseId FROM CourseAssignmentEntity ca WHERE ca.coachId = :coachId) " +
            "ORDER BY c.createdAt DESC")
    List<CourseEntity> findByCoachId(final UUID coachId);

    Optional<CourseEntity> findByName(final String name);

    @Query(value = "SELECT c FROM CourseEntity c WHERE c.id IN (SELECT ca.courseId FROM CourseAssignmentEntity ca WHERE ca.menteeId = :menteeId)")
    List<CourseEntity> findActiveCourseByMenteeId(final UUID menteeId);
}
