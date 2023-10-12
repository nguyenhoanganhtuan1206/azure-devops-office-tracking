package com.openwt.officetracking.api.position;

import com.openwt.officetracking.api.AbstractControllerTest;
import com.openwt.officetracking.api.WithMockAdmin;
import com.openwt.officetracking.domain.auth.AuthsProvider;
import com.openwt.officetracking.domain.position.Position;
import com.openwt.officetracking.domain.position.PositionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.openwt.officetracking.fake.PositionFakes.buildPosition;
import static com.openwt.officetracking.fake.PositionFakes.buildPositions;
import static java.util.UUID.randomUUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PositionController.class)
@ExtendWith(SpringExtension.class)
class PositionControllerTest extends AbstractControllerTest {

    private static final String BASE_URL = "/api/v1/positions";

    @MockBean
    private AuthsProvider authsProvider;

    @MockBean
    private PositionService positionService;

    @BeforeEach
    void init() {
        when(authsProvider.getCurrentAuthentication()).thenCallRealMethod();
    }

    @Test
    @WithMockAdmin
    void shouldFindAll_Ok() throws Exception {
        final var positions = buildPositions();

        when(positionService.findAll()).thenReturn(positions);

        get(BASE_URL)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(positions.size()))
                .andExpect(jsonPath("$[0].id").value(positions.get(0).getId().toString()))
                .andExpect(jsonPath("$[0].name").value(positions.get(0).getName()))
                .andExpect(jsonPath("$[0].temporary").value(positions.get(0).isTemporary()));

        verify(positionService).findAll();
    }

    @Test
    @WithMockAdmin
    void shouldCreate_Ok() throws Exception {
        final var position = buildPosition();

        when(positionService.create(any(Position.class))).thenReturn(position);

        post(BASE_URL, position)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(position.getId().toString()))
                .andExpect(jsonPath("$.name").value(position.getName()))
                .andExpect(jsonPath("$.temporary").value(position.isTemporary()));

        verify(positionService).create(any(Position.class));
    }

    @Test
    @WithMockAdmin
    void shouldUpdate_Ok() throws Exception {
        final var position = buildPosition();
        final var updatedPosition = buildPosition()
                .withId(position.getId());

        when(positionService.update(eq(position.getId()), any(Position.class))).thenReturn(updatedPosition);

        put(BASE_URL + "/" + position.getId(), updatedPosition)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedPosition.getId().toString()))
                .andExpect(jsonPath("$.name").value(updatedPosition.getName()))
                .andExpect(jsonPath("$.temporary").value(updatedPosition.isTemporary()));

        verify(positionService).update(eq(position.getId()), any(Position.class));
    }

    @Test
    @WithMockAdmin
    void shouldDelete_Ok() throws Exception {
        final var id = randomUUID();

        doNothing().when(positionService).delete(id);

        delete(BASE_URL + "/" + id).
                andExpect(status().isOk());

        verify(positionService).delete(id);
    }
}