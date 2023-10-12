package com.openwt.officetracking.domain.device_assignment;

import com.openwt.officetracking.persistent.device_assignment.DeviceAssignmentStore;
import com.openwt.officetracking.persistent.device_history.DeviceHistoryStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.openwt.officetracking.fake.DeviceAssignmentFakes.buildDeviceAssignment;
import static com.openwt.officetracking.fake.DeviceFakes.buildDeviceRequest;
import static java.util.Collections.singletonList;
import static java.util.UUID.randomUUID;
import static org.assertj.core.util.Lists.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeviceAssignmentServiceTest {

    @Mock
    private DeviceAssignmentStore deviceAssignmentStore;

    @InjectMocks
    private DeviceAssignmentService deviceAssignmentService;

    @Mock
    private DeviceHistoryStore deviceHistoryStore;

    @Test
    void shouldFindById_OK() {
        final var deviceAssignment = buildDeviceAssignment();

        when(deviceAssignmentStore.findByUserIdAndDeviceId(deviceAssignment.getUserId(), deviceAssignment.getDeviceId()))
                .thenReturn(Optional.of(deviceAssignment));

        final var actual = deviceAssignmentService.findByUserIdAndDeviceId(deviceAssignment.getUserId(), deviceAssignment.getDeviceId());

        assertEquals(deviceAssignment.getId(), actual.getId());
        assertEquals(deviceAssignment.getDeviceId(), actual.getDeviceId());
        assertEquals(deviceAssignment.getUserId(), actual.getUserId());
        assertEquals(deviceAssignment.getDeviceStatus(), actual.getDeviceStatus());
        assertEquals(deviceAssignment.getFromTimeAt(), actual.getFromTimeAt());
        assertEquals(deviceAssignment.getToTimeAt(), actual.getToTimeAt());

        verify(deviceAssignmentStore).findByUserIdAndDeviceId(deviceAssignment.getUserId(), deviceAssignment.getDeviceId());
    }

    @Test
    void shouldSave_OK() {
        final var deviceAssignment = buildDeviceAssignment();

        when(deviceAssignmentStore.save(argThat(deviceAssignmentArg ->
                deviceAssignmentArg.getId().equals(deviceAssignment.getId())))).thenReturn(deviceAssignment);

        final var actual = deviceAssignmentService.save(deviceAssignment);

        assertEquals(deviceAssignment.getId(), actual.getId());
        assertEquals(deviceAssignment.getDeviceId(), actual.getDeviceId());
        assertEquals(deviceAssignment.getUserId(), actual.getUserId());
        assertEquals(deviceAssignment.getDeviceStatus(), actual.getDeviceStatus());
        assertEquals(deviceAssignment.getFromTimeAt(), actual.getFromTimeAt());
        assertEquals(deviceAssignment.getToTimeAt(), actual.getToTimeAt());

        verify(deviceAssignmentStore).save(argThat(deviceAssignmentArg ->
                deviceAssignmentArg.getId().equals(deviceAssignment.getId())));
    }

    @Test
    void shouldCreateByAssignUserIdAndDeviceId_OK() {
        final var currentUserId = randomUUID();
        final var deviceRequest = buildDeviceRequest();
        final var deviceAssignment = buildDeviceAssignment();

        when(deviceAssignmentStore.save(any(DeviceAssignment.class)))
                .thenReturn(deviceAssignment);

        deviceAssignmentService.createByAssignUserIdAndDeviceId(currentUserId, deviceAssignment.getUserId(), deviceAssignment.getDeviceId(), deviceRequest.getDeviceStatus());

        assertEquals(deviceRequest.getDeviceStatus(), deviceAssignment.getDeviceStatus());

        verify(deviceAssignmentStore).save(any(DeviceAssignment.class));
    }

    @Test
    void shouldCreateByAssignUserIdAndDeviceId_WithEmptyAssignments_OK() {
        final var currentUserId = randomUUID();
        final var userId = randomUUID();
        final var deviceId = randomUUID();
        final var deviceRequest = buildDeviceRequest();

        when(deviceAssignmentStore.findByDeviceId(deviceId)).thenReturn(emptyList());

        deviceAssignmentService.createByAssignUserIdAndDeviceId(currentUserId, userId, deviceId, deviceRequest.getDeviceStatus());

        verify(deviceAssignmentStore).findByDeviceId(deviceId);
    }

    @Test
    void shouldCreateByAssignUserIdAndDeviceId_WithExistingCurrentUserAssignment_OK() {
        final var currentUserId = randomUUID();
        final var userId = randomUUID();
        final var deviceId = randomUUID();
        final var deviceRequest = buildDeviceRequest();
        final var existingAssignment = buildDeviceAssignment();

        when(deviceAssignmentStore.findByDeviceId(deviceId))
                .thenReturn(singletonList(existingAssignment));
        when(deviceAssignmentStore.findByUserIdAndDeviceId(currentUserId, deviceId))
                .thenReturn(Optional.of(existingAssignment));

        deviceAssignmentService.createByAssignUserIdAndDeviceId(currentUserId, userId, deviceId, deviceRequest.getDeviceStatus());

        verify(deviceAssignmentStore).findByDeviceId(deviceId);
        verify(deviceAssignmentStore).findByUserIdAndDeviceId(currentUserId, deviceId);
    }

    @Test
    void shouldCreateByAssignUserIdAndDeviceId_WithExistingRequestUserAssignment_OK() {
        final var currentUserId = randomUUID();
        final var userId = randomUUID();
        final var deviceId = randomUUID();
        final var deviceRequest = buildDeviceRequest();
        final DeviceAssignment existingAssignment = buildDeviceAssignment();

        when(deviceAssignmentStore.findByDeviceId(deviceId))
                .thenReturn(singletonList(existingAssignment));
        when(deviceAssignmentStore.findByUserIdAndDeviceId(currentUserId, deviceId))
                .thenReturn(Optional.empty());
        when(deviceAssignmentStore.findByUserIdAndDeviceId(userId, deviceId))
                .thenReturn(Optional.of(existingAssignment));

        deviceAssignmentService.createByAssignUserIdAndDeviceId(currentUserId, userId, deviceId, deviceRequest.getDeviceStatus());

        verify(deviceAssignmentStore).findByDeviceId(deviceId);
        verify(deviceAssignmentStore).findByUserIdAndDeviceId(currentUserId, deviceId);
        verify(deviceAssignmentStore).findByUserIdAndDeviceId(userId, deviceId);
    }
}