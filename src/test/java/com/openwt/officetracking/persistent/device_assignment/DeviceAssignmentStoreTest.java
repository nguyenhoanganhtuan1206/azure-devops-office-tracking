package com.openwt.officetracking.persistent.device_assignment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.openwt.officetracking.fake.DeviceAssignmentFakes.buildDeviceAssignmentEntity;
import static com.openwt.officetracking.persistent.device_assignment.DeviceAssignmentEntityMapper.toDeviceAssignment;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeviceAssignmentStoreTest {

    @Mock
    private DeviceAssignmentRepository deviceAssignmentRepository;

    @InjectMocks
    private DeviceAssignmentStore deviceAssignmentStore;

    @Test
    void shouldFindByUserIdAndDeviceId_OK() {
        final var deviceAssignment = buildDeviceAssignmentEntity();
        final var deviceAssignmentOptional = Optional.of(deviceAssignment);

        when(deviceAssignmentRepository.findByUserIdAndDeviceId(deviceAssignment.getUserId(), deviceAssignment.getDeviceId()))
                .thenReturn(Optional.of(deviceAssignment));

        final var expected = deviceAssignmentOptional.get();
        final var actual = deviceAssignmentStore.findByUserIdAndDeviceId(deviceAssignment.getUserId(), deviceAssignment.getDeviceId()).get();

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getDeviceId(), actual.getDeviceId());
        assertEquals(expected.getUserId(), actual.getUserId());
        assertEquals(expected.getDeviceStatus(), actual.getDeviceStatus());
        assertEquals(expected.getFromTimeAt(), actual.getFromTimeAt());
        assertEquals(expected.getToTimeAt(), actual.getToTimeAt());

        verify(deviceAssignmentRepository).findByUserIdAndDeviceId(deviceAssignment.getUserId(), deviceAssignment.getDeviceId());
    }

    @Test
    void shouldSave_OK() {
        final var deviceAssignment = buildDeviceAssignmentEntity();

        when(deviceAssignmentRepository.save(any(DeviceAssignmentEntity.class)))
                .thenReturn(deviceAssignment);

        final var actual = deviceAssignmentStore.save(toDeviceAssignment(deviceAssignment));

        assertEquals(deviceAssignment.getId(), actual.getId());
        assertEquals(deviceAssignment.getDeviceId(), actual.getDeviceId());
        assertEquals(deviceAssignment.getUserId(), actual.getUserId());
        assertEquals(deviceAssignment.getDeviceStatus(), actual.getDeviceStatus());
        assertEquals(deviceAssignment.getFromTimeAt(), actual.getFromTimeAt());
        assertEquals(deviceAssignment.getToTimeAt(), actual.getToTimeAt());

        verify(deviceAssignmentRepository).save(any(DeviceAssignmentEntity.class));
    }
}