package com.openwt.officetracking.api.device_type;

import com.openwt.officetracking.api.AbstractControllerTest;
import com.openwt.officetracking.api.WithMockAdmin;
import com.openwt.officetracking.domain.device_type.DeviceType;
import com.openwt.officetracking.domain.device_type.DeviceTypeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.openwt.officetracking.fake.DeviceTypeFakes.buildDeviceType;
import static com.openwt.officetracking.fake.DeviceTypeFakes.buildDeviceTypeResponses;
import static java.util.UUID.randomUUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeviceTypeController.class)
@ExtendWith(SpringExtension.class)
class DeviceTypeControllerTest extends AbstractControllerTest {

    private static final String BASE_URL = "/api/v1/device-types";

    @MockBean
    private DeviceTypeService deviceTypeService;

    @Test
    @WithMockAdmin
    void shouldFindAll_OK() throws Exception {
        final var devices = buildDeviceTypeResponses();

        when(deviceTypeService.findAllDeviceTypes()).thenReturn(devices);

        get(BASE_URL)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(devices.size()))
                .andExpect(jsonPath("$[0].id").value(devices.get(0).getId().toString()))
                .andExpect(jsonPath("$[0].models.size()").value(devices.get(0).getModels().size()))
                .andExpect(jsonPath("$[0].name").value(devices.get(0).getName()));

        verify(deviceTypeService).findAllDeviceTypes();
    }

    @Test
    @WithMockAdmin
    public void shouldCreate_OK() throws Exception {
        final var type = buildDeviceType();

        when(deviceTypeService.create(any(DeviceType.class))).thenReturn(type);

        post(BASE_URL, type)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(type.getId().toString()))
                .andExpect(jsonPath("$.name").value(type.getName()));

        verify(deviceTypeService).create(any(DeviceType.class));
    }

    @Test
    @WithMockAdmin
    public void shouldUpdate_OK() throws Exception {
        final var typeId = randomUUID();
        final var type = buildDeviceType()
                .withId(typeId);

        when(deviceTypeService.update(any(DeviceType.class))).thenReturn(type);

        put(BASE_URL, type)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(type.getId().toString()))
                .andExpect(jsonPath("$.name").value(type.getName()));

        verify(deviceTypeService).update(any(DeviceType.class));
    }

    @Test
    @WithMockAdmin
    void shouldDelete_OK() throws Exception {
        final var typeId  = randomUUID();

        doNothing().when(deviceTypeService).delete(typeId);

        delete(BASE_URL + "/" + typeId)
                .andExpect(status().isOk());

        verify(deviceTypeService).delete(typeId);
    }
}