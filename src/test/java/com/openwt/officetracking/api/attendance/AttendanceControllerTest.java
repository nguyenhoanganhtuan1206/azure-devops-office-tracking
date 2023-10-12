package com.openwt.officetracking.api.attendance;

import com.openwt.officetracking.api.AbstractControllerTest;
import com.openwt.officetracking.api.WithMockUser;
import com.openwt.officetracking.domain.attendance.AttendanceBeaconRequest;
import com.openwt.officetracking.domain.attendance.AttendanceService;
import com.openwt.officetracking.domain.entry_exit_status.EntryExitStatus;
import com.openwt.officetracking.domain.event.TrackingHistoryPublisherService;
import com.openwt.officetracking.domain.tracking_history.AttendanceGeofencingRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.openwt.officetracking.api.tracking_history.TrackingHistoryDTOMapper.toTrackingHistoryDTO;
import static com.openwt.officetracking.fake.CommonFakes.randomInteger;
import static com.openwt.officetracking.fake.TrackingHistoryFakes.buildTrackingHistory;
import static java.util.UUID.randomUUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AttendanceController.class)
@ExtendWith(SpringExtension.class)
class AttendanceControllerTest extends AbstractControllerTest {

    private static final String BASE_URL = "/api/v1/attendance";

    @MockBean
    private AttendanceService attendanceService;

    @MockBean
    private TrackingHistoryPublisherService trackingHistoryPublisherService;

    @Test
    @WithMockUser
    void shouldAttendanceWithGeofencing_OK() throws Exception {
        final var historyTracking = buildTrackingHistory();
        final var historyUpdateDTO = toTrackingHistoryDTO(historyTracking);
        final var attendanceGeofencing = AttendanceGeofencingRequest.builder()
                .activityType(EntryExitStatus.ENTER)
                .officeId(randomUUID())
                .build();

        when(attendanceService.updateHistoryWithGeofenceCheckinCheckout(any(AttendanceGeofencingRequest.class)))
                .thenReturn(historyTracking);
        doNothing().when(trackingHistoryPublisherService).publishHistoryEvent(argThat(historyArg -> historyArg.getId()
                == historyUpdateDTO.getId()));

        put(BASE_URL + "/geofencing", attendanceGeofencing)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(historyTracking.getId().toString()))
                .andExpect(jsonPath("$.officeId").value(historyTracking.getOfficeId().toString()))
                .andExpect(jsonPath("$.userId").value(historyTracking.getUserId().toString()))
                .andExpect(jsonPath("$.trackedDate").value(historyTracking.getTrackedDate().toString()))
                .andExpect(jsonPath("$.checkinAt").value(historyTracking.getCheckinAt().toString()))
                .andExpect(jsonPath("$.checkoutAt").value(historyTracking.getCheckoutAt().toString()))
                .andExpect(jsonPath("$.workingHours").value(historyTracking.getWorkingHours().toString()))
                .andExpect(jsonPath("$.lateTime").value(historyTracking.getLateTime().toString()))
                .andExpect(jsonPath("$.earlyTime").value(historyTracking.getEarlyTime().toString()));

        verify(attendanceService).updateHistoryWithGeofenceCheckinCheckout(any(AttendanceGeofencingRequest.class));
        verify(trackingHistoryPublisherService).publishHistoryEvent(argThat(historyArg -> historyArg.getId()
                == historyUpdateDTO.getId()));
    }

    @Test
    @WithMockUser
    void shouldAttendanceWithBeacon_OK() throws Exception {
        final var historyTracking = buildTrackingHistory();
        final var historyUpdateDTO = toTrackingHistoryDTO(historyTracking);
        final var attendanceRequest = AttendanceBeaconRequest.builder()
                .minor(randomInteger(50))
                .major(randomInteger(50))
                .build();

        when(attendanceService.updateHistoryWhenCheckInOutWithDoor(any(AttendanceBeaconRequest.class)))
                .thenReturn(historyTracking);
        doNothing().when(trackingHistoryPublisherService).publishHistoryEvent(argThat(historyArg -> historyArg.getId()
                == historyUpdateDTO.getId()));

        put(BASE_URL + "/beacon", attendanceRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(historyTracking.getId().toString()))
                .andExpect(jsonPath("$.userId").value(historyTracking.getUserId().toString()));

        verify(attendanceService).updateHistoryWhenCheckInOutWithDoor(any(AttendanceBeaconRequest.class));
        verify(trackingHistoryPublisherService).publishHistoryEvent(argThat(historyArg -> historyArg.getId()
                == historyUpdateDTO.getId()));
    }

    @Test
    @WithMockUser
    void shouldAttendanceWithManually_OK() throws Exception {
        final var historyTracking = buildTrackingHistory();
        final var historyUpdateDTO = toTrackingHistoryDTO(historyTracking);
        final var attendanceRequest = AttendanceBeaconRequest.builder()
                .minor(randomInteger(50))
                .major(randomInteger(50))
                .build();

        when(attendanceService.updateHistoryWhenCheckInOutWithDoor(any(AttendanceBeaconRequest.class)))
                .thenReturn(historyTracking);
        doNothing().when(trackingHistoryPublisherService).publishHistoryEvent(argThat(historyArg -> historyArg.getId()
                == historyUpdateDTO.getId()));

        put(BASE_URL + "/beacon", attendanceRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(historyTracking.getId().toString()))
                .andExpect(jsonPath("$.userId").value(historyTracking.getUserId().toString()));

        verify(attendanceService).updateHistoryWhenCheckInOutWithDoor(any(AttendanceBeaconRequest.class));
        verify(trackingHistoryPublisherService).publishHistoryEvent(argThat(historyArg -> historyArg.getId()
                == historyUpdateDTO.getId()));
    }
}