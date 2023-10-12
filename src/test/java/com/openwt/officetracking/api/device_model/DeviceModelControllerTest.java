package com.openwt.officetracking.api.device_model;

import com.openwt.officetracking.api.AbstractControllerTest;
import com.openwt.officetracking.api.WithMockAdmin;
import com.openwt.officetracking.domain.device_model.DeviceModelService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.openwt.officetracking.fake.DeviceModelFakes.buildDeviceModel;
import static com.openwt.officetracking.fake.DeviceModelFakes.buildDeviceModelRequestDTO;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeviceModelController.class)
@ExtendWith(SpringExtension.class)
class DeviceModelControllerTest extends AbstractControllerTest {

    private static final String BASE_URL = "/api/v1/device-models";

    @MockBean
    private DeviceModelService deviceModelService;

    @Test
    @WithMockAdmin
    public void shouldCreate_OK() throws Exception {
        final var model = buildDeviceModel();
        final var modelRequest = buildDeviceModelRequestDTO();

        when(deviceModelService.create(argThat(modelArg -> modelArg.getTypeId().equals(modelRequest.getTypeId()))))
                .thenReturn(model);

        post(BASE_URL, modelRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(model.getId().toString()))
                .andExpect(jsonPath("$.typeId").value(model.getTypeId().toString()))
                .andExpect(jsonPath("$.name").value(model.getName()));

        verify(deviceModelService).create(argThat(modelArg -> modelArg.getTypeId().equals(modelRequest.getTypeId())));
    }
}