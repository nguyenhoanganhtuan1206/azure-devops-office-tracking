package com.openwt.officetracking.api.tracking_history;

import com.openwt.officetracking.domain.tracking_history.OfficeAttendanceSummary;
import com.openwt.officetracking.domain.tracking_history.TrackingHistory;
import com.openwt.officetracking.domain.tracking_history.TrackingHistoryDashboard;
import com.openwt.officetracking.domain.tracking_history.TrackingHistoryDetails;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.openwt.officetracking.api.user.UserDTOMapper.toUserDetailDTO;

@UtilityClass
public class TrackingHistoryDTOMapper {

    public static TrackingHistoryResponseDTO toTrackingHistoryDTO(final TrackingHistory trackingHistory) {
        return TrackingHistoryResponseDTO.builder()
                .id(trackingHistory.getId())
                .officeId(trackingHistory.getOfficeId())
                .userId(trackingHistory.getUserId())
                .trackedDate(trackingHistory.getTrackedDate())
                .checkinAt(trackingHistory.getCheckinAt())
                .checkoutAt(trackingHistory.getCheckoutAt())
                .workingHours(trackingHistory.getWorkingHours())
                .lateTime(trackingHistory.getLateTime())
                .earlyTime(trackingHistory.getEarlyTime())
                .entryExitStatus(trackingHistory.getEntryExitStatus())
                .attendanceStatus(trackingHistory.getAttendanceStatus())
                .build();
    }

    public static TrackingHistoryDetailDTO toTrackingHistoryDetailDTO(final TrackingHistoryDetails trackingHistory) {
        return TrackingHistoryDetailDTO.builder()
                .id(trackingHistory.getId())
                .user(toUserDetailDTO(trackingHistory.getUser()))
                .trackedDate(trackingHistory.getTrackedDate())
                .checkinAt(trackingHistory.getCheckinAt())
                .checkoutAt(trackingHistory.getCheckoutAt())
                .workingHours(trackingHistory.getWorkingHours())
                .build();
    }

    public static List<TrackingHistoryDetailDTO> toTrackingHistoryDetailDTOs(final List<TrackingHistoryDetails> trackingHistoryDetails) {
        return trackingHistoryDetails.stream()
                .map(TrackingHistoryDTOMapper::toTrackingHistoryDetailDTO)
                .toList();
    }

    public static OfficeAttendanceSummaryDTO toOfficeAttendanceDTO(final OfficeAttendanceSummary officeAttendanceSummary) {
        return OfficeAttendanceSummaryDTO.builder()
                .checkinEmployees(officeAttendanceSummary.getCheckedInEmployees())
                .checkoutEmployees(officeAttendanceSummary.getCheckedOutEmployees())
                .currentEmployees(officeAttendanceSummary.getCurrentEmployees())
                .build();
    }

    public static UserTrackingHistoryDTO toUserTrackingHistoryDTO(final TrackingHistory trackingHistory) {
        return UserTrackingHistoryDTO.builder()
                .id(trackingHistory.getId())
                .trackedDate(trackingHistory.getTrackedDate())
                .checkinAt(trackingHistory.getCheckinAt())
                .checkoutAt(trackingHistory.getCheckoutAt())
                .build();
    }

    public static List<UserTrackingHistoryDTO> toUserTrackingHistoryDTOs(final List<TrackingHistory> trackingHistoryList) {
        return trackingHistoryList.stream()
                .map(TrackingHistoryDTOMapper::toUserTrackingHistoryDTO)
                .toList();
    }

    public static TrackingHistoryDashboardDTO toTrackingHistoryDashboardDTO(final TrackingHistoryDashboard trackingHistoryDashboard) {
        return TrackingHistoryDashboardDTO.builder()
                .lateCount(trackingHistoryDashboard.getLateCount())
                .earlyCount(trackingHistoryDashboard.getEarlyCount())
                .inTimeCount(trackingHistoryDashboard.getInTimeCount())
                .earlyAndLateCount(trackingHistoryDashboard.getEarlyAndLateCount())
                .totalCount(trackingHistoryDashboard.getTotalCount())
                .build();
    }

    public static TrackingHistoryStatusDTO toTrackingHistoryStatusDTO(final TrackingHistoryStatus trackingHistoryStatus) {
        return TrackingHistoryStatusDTO.builder()
                .currentCheckOutTime(trackingHistoryStatus.getCurrentCheckOutTime())
                .currentCheckInTime(trackingHistoryStatus.getCurrentCheckInTime())
                .attendanceStatus(trackingHistoryStatus.getAttendanceStatus())
                .build();
    }
}