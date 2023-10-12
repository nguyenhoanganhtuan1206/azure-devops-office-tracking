package com.openwt.officetracking.persistent.course_assignment;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CourseAssignmentRepository extends CrudRepository<CourseAssignmentEntity, UUID> {

    List<CourseAssignmentEntity> findByMenteeIdAndCourseId(final UUID menteeId, final UUID courseId);

    List<CourseAssignmentEntity> findByMentorIdAndCourseId(final UUID mentorId, final UUID courseId);

    List<CourseAssignmentEntity> findByCoachIdAndCourseId(final UUID coachId, final UUID courseId);

    CourseAssignmentEntity findByCourseIdAndMentorIdAndMenteeId(final UUID courseId, final UUID mentorId, final UUID menteeId);

    List<CourseAssignmentEntity> findByCourseId(final UUID courseId);

    List<CourseAssignmentEntity> findByMenteeId(final UUID menteeId);

    List<CourseAssignmentEntity> findByCoachId(final UUID coachId);

    List<CourseAssignmentEntity> findByMentorId(final UUID mentorId);

    Optional<CourseAssignmentEntity> findByMentorIdAndMenteeIdAndCourseId(final UUID mentorId, final UUID menteeId, final UUID courseId);

    Optional<CourseAssignmentEntity> findByMentorIdAndMenteeIdAndCoachIdAndCourseId(final UUID mentorId, final UUID menteeId, final UUID coachId, final UUID courseId);

    List<CourseAssignmentEntity> findByCoachIdAndMenteeIdAndCourseId(final UUID coachId, final UUID menteeId, final UUID courseId);

    List<CourseAssignmentEntity> findByCourseIdAndMentorIdIsNullAndMenteeIdIsNotNull(final UUID courseId);

    @Modifying
    @Query("DELETE CourseAssignmentEntity ca WHERE ca.mentorId =:mentorId AND ca.menteeId IS NULL AND ca.coachId IS NULL AND ca.courseId IS NULL ")
    void deleteUnassignedCourseByMentorId(final UUID mentorId);

    @Modifying
    @Query("DELETE FROM CourseAssignmentEntity ca WHERE ca.courseId = :courseId AND ca.menteeId= :menteeId AND ca.mentorId IS NULL ")
    void deleteByCourseIdAndMenteeIdWithoutMentorId(final UUID courseId, final UUID menteeId);

    @Modifying
    @Query("DELETE FROM CourseAssignmentEntity ca WHERE ca.courseId = :courseId")
    void deleteByCourseId(final UUID courseId);

    @Modifying
    @Query("DELETE FROM CourseAssignmentEntity ca WHERE ca.mentorId = :mentorId AND ca.courseId = :courseId")
    void deleteByMentorIdAndCourseId(final UUID mentorId, final UUID courseId);

    @Modifying
    @Query("DELETE FROM CourseAssignmentEntity ca WHERE ca.coachId = :coachId AND ca.courseId = :courseId")
    void deleteByCoachIdAndCourseId(final UUID coachId, final UUID courseId);

    @Modifying
    @Query("DELETE FROM CourseAssignmentEntity ca WHERE ca.menteeId = :menteeId AND ca.mentorId = :mentorId AND ca.courseId = :courseId")
    void deleteByMenteeIdAndMentorIdAndCourseId(final UUID menteeId, final UUID mentorId, final UUID courseId);

    @Modifying
    @Query("DELETE FROM CourseAssignmentEntity ca WHERE ca.menteeId = :menteeId AND ca.courseId = :courseId")
    void deleteByMenteeIdAndCourseId(final UUID menteeId, final UUID courseId);
}
