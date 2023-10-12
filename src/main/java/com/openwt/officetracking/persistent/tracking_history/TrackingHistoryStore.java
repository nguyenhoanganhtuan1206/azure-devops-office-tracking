package com.openwt.officetracking.persistent.tracking_history;

import com.openwt.officetracking.domain.tracking_history.TrackingHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.openwt.officetracking.persistent.tracking_history.TrackingHistoryEntityMapper.*;
import static org.apache.commons.collections4.IterableUtils.toList;

@Repository
@RequiredArgsConstructor
public class TrackingHistoryStore {

    private final TrackingHistoryRepository trackingHistoryRepository;

    public List<TrackingHistory> findAll() {
        return toTrackingHistories(toList(trackingHistoryRepository.findAll()));
    }

    public List<TrackingHistory> findTrackingHistoriesByTrackedDate(final Instant startOfToday) {
        return toTrackingHistories(trackingHistoryRepository.findTrackingHistoriesByTrackedDate(startOfToday));
    }

    public List<TrackingHistory> findByTrackedDateBetween(final Instant startOfDay, final Instant endOfDay) {
        return toTrackingHistories(toList(trackingHistoryRepository.findByTrackedDateBetween(startOfDay, endOfDay)));
    }

    public TrackingHistory save(final TrackingHistory trackingHistory) {
        return toTrackingHistory(trackingHistoryRepository.save(toTrackingHistoryEntity(trackingHistory)));
    }

    public Optional<TrackingHistory> findTodayHistoryForUser(final UUID userId) {
        return trackingHistoryRepository.findTodayHistoryForUser(userId)
                .map(TrackingHistoryEntityMapper::toTrackingHistory);
    }

    public List<TrackingHistory> findLatestTrackingHistories(final UUID userId, final Pageable pageable) {
        return toTrackingHistories(trackingHistoryRepository.findLatestTrackingHistories(userId, pageable));
    }

    public List<TrackingHistory> findTrackingHistoriesInRange(final UUID userId, final Instant startDate, final Instant endDate) {
        return toTrackingHistories(trackingHistoryRepository.findByTrackedDateInRange(userId, startDate, endDate));
    }
}