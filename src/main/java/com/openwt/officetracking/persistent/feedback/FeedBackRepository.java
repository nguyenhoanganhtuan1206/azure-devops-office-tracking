package com.openwt.officetracking.persistent.feedback;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FeedBackRepository extends CrudRepository<FeedBackEntity, UUID> {

    @Query("SELECT f " +
            "FROM FeedBackEntity f " +
            "WHERE f.courseAssignmentId IN " +
            "(SELECT ca.id FROM CourseAssignmentEntity ca WHERE ca.courseId = :courseId AND ca.menteeId = :menteeId)")
    List<FeedBackEntity> findByCourseIdAndMenteeId(final UUID courseId, final UUID menteeId);

    @Query("SELECT f " +
            "FROM FeedBackEntity f " +
            "WHERE f.courseAssignmentId IN " +
            "(SELECT ca.id FROM CourseAssignmentEntity ca WHERE ca.courseId = :courseId AND ca.mentorId = :mentorId AND ca.menteeId = :menteeId)")
    List<FeedBackEntity> findByCourseIdAndMentorIdAndMenteeId(final UUID courseId, final UUID mentorId, final UUID menteeId);

    @Query("SELECT f " +
            "FROM FeedBackEntity f " +
            "WHERE f.courseAssignmentId IN " +
            "(SELECT ca.id FROM CourseAssignmentEntity ca WHERE ca.courseId = :courseId AND ca.coachId = :coachId AND ca.menteeId = :menteeId)")
    List<FeedBackEntity> findByCourseIdAndCoachIdAndMenteeId(final UUID courseId, final UUID coachId, final UUID menteeId);
}
