package com.openwt.officetracking.api.tracking_history;

import com.openwt.officetracking.api.AbstractControllerTest;
import com.openwt.officetracking.api.WithMockAdmin;
import com.openwt.officetracking.api.WithMockUser;
import com.openwt.officetracking.domain.attendance_status.AttendanceStatus;
import com.openwt.officetracking.domain.tracking_history.TrackingHistoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.time.LocalDate;

import static com.openwt.officetracking.fake.TrackingHistoryFakes.*;
import static java.time.Instant.now;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TrackingHistoryController.class)
@ExtendWith(SpringExtension.class)
class TrackingHistoryControllerTest extends AbstractControllerTest {

    private static final String BASE_URL = "/api/v1/tracking-histories";


    @MockBean
    private TrackingHistoryService trackingHistoryService;

    @Test
    @WithMockAdmin
    void shouldFindAll_OK() throws Exception {
        final var currentTime = Instant.now();
        final var historiesTracking = buildTrackingHistoryDetails();

        when(trackingHistoryService.findFilteredTrackingHistories(any(Instant.class)))
                .thenReturn(historiesTracking);

        get(BASE_URL + "?analyzeDate=" + currentTime)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(historiesTracking.get(0).getId().toString()))
                .andExpect(jsonPath("$[0].checkinAt").value(historiesTracking.get(0).getCheckinAt().toString()))
                .andExpect(jsonPath("$[0].checkoutAt").value(historiesTracking.get(0).getCheckoutAt().toString()))
                .andExpect(jsonPath("$[0].workingHours").value(historiesTracking.get(0).getWorkingHours().toString()));
        verify(trackingHistoryService).findFilteredTrackingHistories(any(Instant.class));
    }

    @Test
    @WithMockAdmin
    void analyzeAttendanceDataByDate_OK() throws Exception {
        final var officeAttendanceSummary = buildOfficeAttendanceSummary();

        when(trackingHistoryService.analyzeAttendanceDataByDate(any(Instant.class)))
                .thenReturn(officeAttendanceSummary);

        get(BASE_URL + "/attendance-summary?analyzeDate=" + now())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentEmployees").value(officeAttendanceSummary.getCurrentEmployees()))
                .andExpect(jsonPath("$.checkinEmployees").value(officeAttendanceSummary.getCheckedInEmployees()))
                .andExpect(jsonPath("$.checkoutEmployees").value(officeAttendanceSummary.getCheckedOutEmployees()));

        verify(trackingHistoryService).analyzeAttendanceDataByDate(any(Instant.class));
    }

    @Test
    @WithMockUser
    void shouldFindByCurrentUser_OK() throws Exception {
        final var page = 0;
        final var size = 2;
        final var sort = "trackedDate,desc";
        final var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "trackedDate"));
        final var historiesTracking = buildTrackingHistories();
        final var startDate = LocalDate.now();
        final var endDate = startDate.plusDays(1);

        when(trackingHistoryService.findByCurrentUser(startDate, endDate, pageable)).thenReturn(historiesTracking);

        get(BASE_URL + "/reports?startDate=" + startDate + "&endDate=" + endDate + "&page=" + page + "&size=" + size + "&sort=" + sort)
                .andExpect(status().isOk());

        verify(trackingHistoryService).findByCurrentUser(startDate, endDate, pageable);
    }

    @Test
    @WithMockAdmin
    void shouldCalculateAttendanceSummary_OK() throws Exception {
        final var analysisDate = now();
        final var dashboard = buildTrackingHistoryDashboard();

        when(trackingHistoryService.calculateAttendanceSummary(analysisDate)).thenReturn(dashboard);

        get(BASE_URL + "/attendance-analysis?analysisDate=" + analysisDate)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lateCount").value(dashboard.getLateCount()))
                .andExpect(jsonPath("$.earlyCount").value(dashboard.getEarlyCount()))
                .andExpect(jsonPath("$.earlyAndLateCount").value(dashboard.getEarlyAndLateCount()))
                .andExpect(jsonPath("$.inTimeCount").value(dashboard.getInTimeCount()));

        verify(trackingHistoryService).calculateAttendanceSummary(analysisDate);
    }

    @Test
    @WithMockUser
    void shouldFindCurrentStatus_OK() throws Exception {
        final var trackingHistoryStatus = TrackingHistoryStatus.builder()
                .currentCheckInTime(now())
                .currentCheckOutTime(now())
                .attendanceStatus(AttendanceStatus.CHECK_IN)
                .build();

        when(trackingHistoryService.findCurrentStatusByUserId())
                .thenReturn(trackingHistoryStatus);

        get(BASE_URL + "/activity-status")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.attendanceStatus").value(trackingHistoryStatus.getAttendanceStatus().toString()))
                .andExpect(jsonPath("$.currentCheckOutTime").value(trackingHistoryStatus.getCurrentCheckOutTime().toString()))
                .andExpect(jsonPath("$.currentCheckInTime").value(trackingHistoryStatus.getCurrentCheckInTime().toString()));

        verify(trackingHistoryService).findCurrentStatusByUserId();
    }
}