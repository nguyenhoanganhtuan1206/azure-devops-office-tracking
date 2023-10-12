package com.openwt.officetracking.api.device_configuration_value;

import com.openwt.officetracking.api.AbstractControllerTest;
import com.openwt.officetracking.api.WithMockAdmin;
import com.openwt.officetracking.domain.device_configuration_value.DeviceConfigurationValueService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.openwt.officetracking.fake.DeviceConfigurationValueFakes.buildDeviceConfigurationValue;
import static com.openwt.officetracking.fake.DeviceConfigurationValueFakes.buildDeviceConfigurationValueRequestDTO;
import static java.util.UUID.randomUUID;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeviceConfigurationValueController.class)
@ExtendWith(SpringExtension.class)
class DeviceConfigurationValueControllerTest extends AbstractControllerTest {

    private static final String BASE_URL = "/api/v1/device-values";

    @MockBean
    private DeviceConfigurationValueService deviceConfigurationValueService;

    @Test
    @WithMockAdmin
    public void shouldCreate_OK() throws Exception {
        final var value = buildDeviceConfigurationValue();
        final var valueRequest = buildDeviceConfigurationValueRequestDTO();

        when(deviceConfigurationValueService.create(argThat(modelArg -> modelArg.getModelId().equals(valueRequest.getModelId()))))
                .thenReturn(value);

        post(BASE_URL, valueRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(value.getId().toString()))
                .andExpect(jsonPath("$.modelId").value(value.getModelId().toString()))
                .andExpect(jsonPath("$.configurationId").value(value.getConfigurationId().toString()))
                .andExpect(jsonPath("$.value").value(value.getValue()));

        verify(deviceConfigurationValueService).create(argThat(modelArg -> modelArg.getModelId().equals(valueRequest.getModelId())));
    }

    @Test
    @WithMockAdmin
    public void shouldUpdate_OK() throws Exception {
        final var value = buildDeviceConfigurationValue();
        final var valueRequest = buildDeviceConfigurationValueRequestDTO();

        when(deviceConfigurationValueService.update(any(), argThat(modelArg -> modelArg.getModelId().equals(valueRequest.getModelId()))))
                .thenReturn(value);

        put(BASE_URL + "/" + value.getId(), valueRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(value.getId().toString()))
                .andExpect(jsonPath("$.modelId").value(value.getModelId().toString()))
                .andExpect(jsonPath("$.configurationId").value(value.getConfigurationId().toString()))
                .andExpect(jsonPath("$.value").value(value.getValue()));

        verify(deviceConfigurationValueService).update(any(), argThat(modelArg -> modelArg.getModelId().equals(valueRequest.getModelId())));
    }

    @Test
    @WithMockAdmin
    void shouldDelete_OK() throws Exception {
        final var valueId  = randomUUID();

        doNothing().when(deviceConfigurationValueService).delete(valueId);

        delete(BASE_URL + "/" + valueId)
                .andExpect(status().isOk());

        verify(deviceConfigurationValueService).delete(valueId);
    }
}