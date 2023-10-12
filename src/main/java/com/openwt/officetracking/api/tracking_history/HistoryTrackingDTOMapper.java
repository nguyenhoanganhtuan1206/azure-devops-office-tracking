package com.openwt.officetracking.api.tracking_history;

import com.openwt.officetracking.domain.tracking_history.TrackingHistory;
import lombok.experimental.UtilityClass;

@UtilityClass
public class HistoryTrackingDTOMapper {

    public static TrackingHistoryResponseDTO toTrackingHistoryDTO(final TrackingHistory trackingHistory) {
        return TrackingHistoryResponseDTO.builder()
                .id(trackingHistory.getId())
                .userId(trackingHistory.getUserId())
                .officeId(trackingHistory.getOfficeId())
                .trackedDate(trackingHistory.getTrackedDate())
                .checkinAt(trackingHistory.getCheckinAt())
                .checkoutAt(trackingHistory.getCheckoutAt())
                .workingHours(trackingHistory.getWorkingHours())
                .lateTime(trackingHistory.getLateTime())
                .earlyTime(trackingHistory.getEarlyTime())
                .build();
    }
}