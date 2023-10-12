package com.openwt.officetracking.persistent.tracking_history;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TrackingHistoryRepository extends CrudRepository<TrackingHistoryEntity, UUID> {

    @Query("SELECT th FROM TrackingHistoryEntity th WHERE EXTRACT(DAY FROM th.trackedDate) = EXTRACT(DAY FROM CURRENT_DATE) AND th.userId = :userId")
    Optional<TrackingHistoryEntity> findTodayHistoryForUser(final UUID userId);

    @Query("SELECT th FROM TrackingHistoryEntity th " +
            "WHERE DATE(th.trackedDate) = DATE(:trackedDate)")
    List<TrackingHistoryEntity> findTrackingHistoriesByTrackedDate(final Instant trackedDate);

    List<TrackingHistoryEntity> findByTrackedDateBetween(final Instant startOfToday, final Instant endOfToday);

    @Query("SELECT th FROM TrackingHistoryEntity th WHERE th.userId = :userId")
    List<TrackingHistoryEntity> findLatestTrackingHistories(final UUID userId, final Pageable pageable);

    @Query("SELECT th FROM TrackingHistoryEntity th " +
            "WHERE th.userId = :userId AND DATE(th.trackedDate) >= DATE(:startDate) AND DATE(th.trackedDate) <= DATE(:endDate) " +
            "ORDER BY th.trackedDate DESC")
    List<TrackingHistoryEntity> findByTrackedDateInRange(final UUID userId, final Instant startDate, final Instant endDate);
}
