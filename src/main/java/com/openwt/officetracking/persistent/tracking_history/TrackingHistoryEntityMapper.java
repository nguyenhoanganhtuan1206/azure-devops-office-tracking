package com.openwt.officetracking.persistent.tracking_history;

import com.openwt.officetracking.domain.tracking_history.TrackingHistory;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class TrackingHistoryEntityMapper {

    public static TrackingHistoryEntity toTrackingHistoryEntity(final TrackingHistory trackingHistory) {
        return TrackingHistoryEntity.builder()
                .id(trackingHistory.getId())
                .officeId(trackingHistory.getOfficeId())
                .userId(trackingHistory.getUserId())
                .trackedDate(trackingHistory.getTrackedDate())
                .checkinAt(trackingHistory.getCheckinAt())
                .checkoutAt(trackingHistory.getCheckoutAt())
                .entryExitStatus(trackingHistory.getEntryExitStatus())
                .checkoutBeaconAt(trackingHistory.getCheckoutBeaconAt())
                .build();
    }

    public static TrackingHistory toTrackingHistory(final TrackingHistoryEntity trackingHistoryEntity) {
        return TrackingHistory.builder()
                .id(trackingHistoryEntity.getId())
                .officeId(trackingHistoryEntity.getOfficeId())
                .userId(trackingHistoryEntity.getUserId())
                .trackedDate(trackingHistoryEntity.getTrackedDate())
                .checkinAt(trackingHistoryEntity.getCheckinAt())
                .checkoutAt(trackingHistoryEntity.getCheckoutAt())
                .entryExitStatus(trackingHistoryEntity.getEntryExitStatus())
                .checkoutBeaconAt(trackingHistoryEntity.getCheckoutBeaconAt())
                .build();
    }

    public static List<TrackingHistory> toTrackingHistories(final List<TrackingHistoryEntity> trackingHistoryEntities) {
        return trackingHistoryEntities.stream()
                .map(TrackingHistoryEntityMapper::toTrackingHistory)
                .toList();
    }
}