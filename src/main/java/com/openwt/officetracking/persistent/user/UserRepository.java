package com.openwt.officetracking.persistent.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, UUID> {

    Optional<UserEntity> findByPersonalEmail(final String personalEmail);

    Optional<UserEntity> findByCompanyEmail(final String companyEmail);

    Optional<UserEntity> findByIdentifier(final String identifier);

    Optional<UserEntity> findByQrCode(final String qrCode);

    Optional<UserEntity> findByResetPasswordCode(final String code);

    @Query(value = "SELECT * " +
            "FROM users " +
            "WHERE users.first_name ILIKE CONCAT('%', :name, '%') " +
            "   OR users.last_name ILIKE CONCAT('%', :name, '%')" +
            "   OR users.email ILIKE CONCAT('%', :name, '%')", nativeQuery = true)
    List<UserEntity> findByName(final String name);

    List<UserEntity> findAllByOrderByCreatedAtDesc();

    @Query("SELECT u FROM UserEntity u " +
            "WHERE u.id IN (" +
            "   SELECT DISTINCT ca.menteeId " +
            "   FROM CourseAssignmentEntity ca) " +
            "ORDER BY u.assignedMenteeAt DESC")
    List<UserEntity> findAllMentees();

    @Query("SELECT u FROM UserEntity u " +
            "WHERE u.id IN " +
            "(SELECT DISTINCT ca.mentorId " +
            "   FROM CourseAssignmentEntity ca) " +
            "ORDER BY u.assignedMentorAt DESC")
    List<UserEntity> findAllMentors();

    @Query("SELECT u FROM UserEntity u " +
            "WHERE u.id IN (" +
            "   SELECT DISTINCT ca.coachId " +
            "   FROM CourseAssignmentEntity ca) " +
            "ORDER BY u.assignedCoachAt DESC")
    List<UserEntity> findAllCoaches();
}
