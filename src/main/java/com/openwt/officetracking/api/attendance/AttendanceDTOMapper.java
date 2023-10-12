package com.openwt.officetracking.api.attendance;

import com.openwt.officetracking.domain.attendance.AttendanceBeaconRequest;
import com.openwt.officetracking.domain.attendance.AttendanceBiometryRequest;
import com.openwt.officetracking.domain.tracking_history.AttendanceGeofencingRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AttendanceDTOMapper {

    public static AttendanceGeofencingRequest toAttendanceGeofencing(final AttendanceGeofencingRequestDTO attendanceGeofencingRequestDTO) {
        return AttendanceGeofencingRequest.builder()
                .officeId(attendanceGeofencingRequestDTO.getOfficeId())
                .activityType(attendanceGeofencingRequestDTO.getActivityType())
                .build();
    }

    public static AttendanceBeaconRequest toAttendanceBeacon(final AttendanceBeaconRequestDTO attendanceBeaconRequestDTO) {
        return AttendanceBeaconRequest.builder()
                .minor(attendanceBeaconRequestDTO.getMinor())
                .major(attendanceBeaconRequestDTO.getMajor())
                .build();
    }

    public static AttendanceBiometryRequest toAttendanceBiometry(final AttendanceBiometryRequestDTO attendanceRequest) {
        return AttendanceBiometryRequest.builder()
                .officeId(attendanceRequest.getOfficeId())
                .biometryToken(attendanceRequest.getBiometryToken())
                .build();
    }
}
