package com.openwt.officetracking.domain.door;

import com.openwt.officetracking.domain.office.OfficeService;
import com.openwt.officetracking.persistent.door.DoorStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.openwt.officetracking.fake.DoorFakes.buildDoor;
import static com.openwt.officetracking.fake.DoorFakes.buildDoors;
import static com.openwt.officetracking.fake.OfficeFakes.buildOffice;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DoorServiceTest {

    @Mock
    private DoorStore doorStore;

    @Mock
    private OfficeService officeService;

    @InjectMocks
    private DoorService doorService;

    @Test
    void shouldSave_OK() {
        final var officeRegion = buildOffice();
        final var beacon = buildDoor()
                .withOfficeId(officeRegion.getId());

        when(officeService.findById(beacon.getOfficeId()))
                .thenReturn(officeRegion);
        when(doorStore.save(argThat(beaconArg -> beaconArg.getOfficeId() == beacon.getOfficeId())))
                .thenReturn(beacon);

        final var actual = doorService.save(beacon);

        assertEquals(beacon.getId(), actual.getId());
        assertEquals(beacon.getName(), actual.getName());
        assertEquals(beacon.getMajor(), actual.getMajor());
        assertEquals(beacon.getMinor(), actual.getMinor());
        assertEquals(beacon.getOfficeId(), actual.getOfficeId());

        verify(doorStore).save(argThat(beaconArg -> beaconArg.getOfficeId() == beacon.getOfficeId()));
        verify(officeService).findById(beacon.getOfficeId());
    }

    @Test
    void shouldFindAll_OK() {
        final var doorBeacons = buildDoors();

        when(doorStore.findAll())
                .thenReturn(doorBeacons);

        final var actual = doorService.findAll();

        assertEquals(doorBeacons.size(), actual.size());

        verify(doorStore).findAll();
    }
}