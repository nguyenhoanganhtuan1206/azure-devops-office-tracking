package com.openwt.officetracking.api.door;

import com.openwt.officetracking.api.AbstractControllerTest;
import com.openwt.officetracking.api.WithMockAdmin;
import com.openwt.officetracking.domain.door.Door;
import com.openwt.officetracking.domain.door.DoorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static com.openwt.officetracking.fake.DoorFakes.buildDoor;
import static com.openwt.officetracking.fake.DoorFakes.buildDoors;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DoorController.class)
@ExtendWith(SpringExtension.class)
class DoorControllerTest extends AbstractControllerTest {

    private static final String BASE_URL = "/api/v1/beacon-doors";

    @MockBean
    private DoorService doorService;

    @Test
    @WithMockAdmin
    void shouldCreateNewDoor_OK() throws Exception {
        final var beacon = buildDoor();

        when(doorService.save(any(Door.class)))
                .thenReturn(beacon);

        post(BASE_URL, beacon)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(beacon.getId().toString()))
                .andExpect(jsonPath("$.name").value(beacon.getName()))
                .andExpect(jsonPath("$.officeId").value(beacon.getOfficeId().toString()))
                .andExpect(jsonPath("$.major").value(beacon.getMajor()))
                .andExpect(jsonPath("$.minor").value(beacon.getMinor()));

        verify(doorService).save(any(Door.class));
    }

    @Test
    @WithMockAdmin
    void shouldFindAll_OK() throws Exception {
        final var doorBeacons = buildDoors();

        when(doorService.findAll())
                .thenReturn(doorBeacons);

        get(BASE_URL)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(doorBeacons.get(0).getId().toString()))
                .andExpect(jsonPath("$[0].name").value(doorBeacons.get(0).getName()))
                .andExpect(jsonPath("$[0].officeId").value(doorBeacons.get(0).getOfficeId().toString()))
                .andExpect(jsonPath("$[0].major").value(doorBeacons.get(0).getMajor()))
                .andExpect(jsonPath("$[0].minor").value(doorBeacons.get(0).getMinor()));

        verify(doorService).findAll();
    }

    @Test
    @WithMockAdmin
    void shouldFindById_OK() throws Exception {
        final var door = buildDoor();

        when(doorService.findById(door.getId()))
                .thenReturn(door);

        get(BASE_URL + "/" + door.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(door.getId().toString()))
                .andExpect(jsonPath("$.name").value(door.getName()))
                .andExpect(jsonPath("$.officeId").value(door.getOfficeId().toString()))
                .andExpect(jsonPath("$.major").value(door.getMajor()))
                .andExpect(jsonPath("$.minor").value(door.getMinor()));

        verify(doorService).findById(door.getId());
    }

    @Test
    @WithMockAdmin
    void shouldUpdateDoorBeacon_OK() throws Exception {
        final var beacon = buildDoor();
        final var beaconUpdate = buildDoor()
                .withId(beacon.getId());

        when(doorService.update(any(UUID.class), any(Door.class)))
                .thenReturn(beaconUpdate);

        put(BASE_URL + "/" + beacon.getId(), beaconUpdate)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(beaconUpdate.getId().toString()))
                .andExpect(jsonPath("$.name").value(beaconUpdate.getName()))
                .andExpect(jsonPath("$.officeId").value(beaconUpdate.getOfficeId().toString()))
                .andExpect(jsonPath("$.major").value(beaconUpdate.getMajor()))
                .andExpect(jsonPath("$.minor").value(beaconUpdate.getMinor()));

        verify(doorService).update(any(UUID.class), any(Door.class));
    }

    @Test
    @WithMockAdmin
    void shouldDeleteDoorBeacon_OK() throws Exception {
        final var beacon = buildDoor();

        doNothing().when(doorService)
                .delete(any(UUID.class));

        delete(BASE_URL + "/" + beacon.getId())
                .andExpect(status().isOk());

        verify(doorService).delete(any(UUID.class));
    }
}