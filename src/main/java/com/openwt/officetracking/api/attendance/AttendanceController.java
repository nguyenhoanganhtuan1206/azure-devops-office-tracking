package com.openwt.officetracking.api.attendance;

import com.openwt.officetracking.api.tracking_history.TrackingHistoryResponseDTO;
import com.openwt.officetracking.domain.attendance.AttendanceService;
import com.openwt.officetracking.domain.event.TrackingHistoryPublisherService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.openwt.officetracking.api.attendance.AttendanceDTOMapper.*;
import static com.openwt.officetracking.api.tracking_history.TrackingHistoryDTOMapper.toTrackingHistoryDTO;

@RestController
@RequestMapping("api/v1/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    private final TrackingHistoryPublisherService trackingHistoryPublisherService;

    @Operation(summary = "Update Geofencing Event Log for Inside/Outside")
    @PutMapping("geofencing")
    @PreAuthorize("hasAnyRole('USER')")
    public TrackingHistoryResponseDTO attendanceWithGeofencing(final @RequestBody AttendanceGeofencingRequestDTO attendanceGeofencingRequest) {
        final TrackingHistoryResponseDTO updateHistory = toTrackingHistoryDTO(attendanceService
                .updateHistoryWithGeofenceCheckinCheckout(toAttendanceGeofencing(attendanceGeofencingRequest)));

        trackingHistoryPublisherService.publishHistoryEvent(updateHistory);
        return updateHistory;
    }

    @Operation(summary = "Update Beacon Event Log for Entry/Exit")
    @PutMapping("beacon")
    @PreAuthorize("hasAnyRole('USER')")
    public TrackingHistoryResponseDTO attendanceWithBeacon(final @RequestBody AttendanceBeaconRequestDTO attendanceBeaconRequestDTO) {
        final TrackingHistoryResponseDTO updateHistory = toTrackingHistoryDTO(attendanceService
                .updateHistoryWhenCheckInOutWithDoor(toAttendanceBeacon(attendanceBeaconRequestDTO)));

        trackingHistoryPublisherService.publishHistoryEvent(updateHistory);
        return updateHistory;
    }

    @Operation(summary = "Update Manually Event Log for Entry/Exit")
    @PutMapping("manually")
    @PreAuthorize("hasAnyRole('USER')")
    public TrackingHistoryResponseDTO attendanceWithManually(final @RequestBody AttendanceBiometryRequestDTO attendanceBiometryRequestDTO) {
        final TrackingHistoryResponseDTO updateHistory = toTrackingHistoryDTO(attendanceService
                .updateHistoryWhenCheckInOutWithManually(toAttendanceBiometry(attendanceBiometryRequestDTO)));

        trackingHistoryPublisherService.publishHistoryEvent(updateHistory);
        return updateHistory;
    }
}