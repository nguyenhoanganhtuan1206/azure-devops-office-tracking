package com.openwt.officetracking.api.office;

import com.openwt.officetracking.api.AbstractControllerTest;
import com.openwt.officetracking.api.WithMockAdmin;
import com.openwt.officetracking.domain.office.Office;
import com.openwt.officetracking.domain.office.OfficeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static com.openwt.officetracking.fake.OfficeFakes.*;
import static java.util.UUID.randomUUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OfficeController.class)
@ExtendWith(SpringExtension.class)
class OfficeControllerTest extends AbstractControllerTest {

    private static final String BASE_URL = "/api/v1/offices";

    @MockBean
    private OfficeService officeService;

    @Test
    @WithMockAdmin
    void shouldFindAllOffice_OK() throws Exception {
        final var officeList = buildOffices();

        when(officeService.findAll()).thenReturn(officeList);

        get(BASE_URL)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(officeList.size()))
                .andExpect(jsonPath("$[0].id").value(officeList.get(0).getId().toString()))
                .andExpect(jsonPath("$[0].officeUUID").value(officeList.get(0).getOfficeUUID()))
                .andExpect(jsonPath("$[0].name").value(officeList.get(0).getName()))
                .andExpect(jsonPath("$[0].latitude").value(officeList.get(0).getLatitude()))
                .andExpect(jsonPath("$[0].longitude").value(officeList.get(0).getLongitude()))
                .andExpect(jsonPath("$[0].radius").value(officeList.get(0).getRadius()));

        verify(officeService).findAll();
    }

    @Test
    @WithMockAdmin
    void shouldUpdate_OK() throws Exception {
        final var officeId = randomUUID();
        final var officeRequest = buildOfficeRequestDTO();
        final var officeRegion = buildOffice().withId(officeId);

        when(officeService.update(any(UUID.class), any(Office.class)))
                .thenReturn(officeRegion);

        put(BASE_URL + "/" + officeId, officeRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(officeRegion.getId().toString()))
                .andExpect(jsonPath("$.officeUUID").value(officeRegion.getOfficeUUID()))
                .andExpect(jsonPath("$.name").value(officeRegion.getName()))
                .andExpect(jsonPath("$.latitude").value(officeRegion.getLatitude()))
                .andExpect(jsonPath("$.longitude").value(officeRegion.getLongitude()))
                .andExpect(jsonPath("$.radius").value(officeRegion.getRadius()));

        verify(officeService).update(any(UUID.class), any(Office.class));
    }

    @Test
    @WithMockAdmin
    void shouldFindById_OK() throws Exception {
        final var officeId = randomUUID();
        final var office = buildOffice();

        when(officeService.findById(any(UUID.class)))
                .thenReturn(office);

        get(BASE_URL + "/" + officeId)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(office.getId().toString()))
                .andExpect(jsonPath("$.officeUUID").value(office.getOfficeUUID()))
                .andExpect(jsonPath("$.name").value(office.getName()))
                .andExpect(jsonPath("$.latitude").value(office.getLatitude()))
                .andExpect(jsonPath("$.longitude").value(office.getLongitude()))
                .andExpect(jsonPath("$.radius").value(office.getRadius()));

        verify(officeService).findById(any(UUID.class));
    }
}