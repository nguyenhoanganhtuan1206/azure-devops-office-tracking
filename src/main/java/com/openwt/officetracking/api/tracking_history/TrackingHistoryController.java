package com.openwt.officetracking.api.tracking_history;

import com.openwt.officetracking.domain.tracking_history.TrackingHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static com.openwt.officetracking.api.tracking_history.TrackingHistoryDTOMapper.*;

@RestController
@RequestMapping("api/v1/tracking-histories")
@RequiredArgsConstructor
public class TrackingHistoryController {

    private final TrackingHistoryService trackingHistoryService;

    @Operation(summary = "Find all histories tracking by date")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public List<TrackingHistoryDetailDTO> findAll(final @RequestParam(required = false) Instant analyzeDate) {
        return toTrackingHistoryDetailDTOs(trackingHistoryService.findFilteredTrackingHistories(analyzeDate));
    }

    @Operation(summary = "Get current employee presence and check-in/check-out counts.")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("attendance-summary")
    public OfficeAttendanceSummaryDTO analyzeAttendanceDataByDate(final @RequestParam Instant analyzeDate) {
        return toOfficeAttendanceDTO(trackingHistoryService.analyzeAttendanceDataByDate(analyzeDate));
    }

    @Operation(summary = "Find tracking histories of current login user")
    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("reports")
    public List<UserTrackingHistoryDTO> findByCurrentUser(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") final LocalDate startDate,
                                                          @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") final LocalDate endDate,
                                                          final Pageable pageable) {
        return toUserTrackingHistoryDTOs(trackingHistoryService.findByCurrentUser(startDate, endDate, pageable));
    }

    @Operation(summary = "Get Attendance Summary")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("attendance-analysis")
    public TrackingHistoryDashboardDTO calculateAttendanceSummary(@RequestParam final Instant analysisDate) {
        return toTrackingHistoryDashboardDTO(trackingHistoryService.calculateAttendanceSummary(analysisDate));
    }

    @Operation(summary = "Get current status (Not yet check-in/Check-in/Checkout) of employee in day")
    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("activity-status")
    public TrackingHistoryStatusDTO findCurrentStatus() {
        return toTrackingHistoryStatusDTO(trackingHistoryService.findCurrentStatusByUserId());
    }
}