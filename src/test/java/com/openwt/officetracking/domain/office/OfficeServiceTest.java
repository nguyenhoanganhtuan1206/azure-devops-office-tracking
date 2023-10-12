package com.openwt.officetracking.domain.office;

import com.openwt.officetracking.error.BadRequestException;
import com.openwt.officetracking.error.NotFoundException;
import com.openwt.officetracking.persistent.office.OfficeStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static com.openwt.officetracking.fake.OfficeFakes.buildOffice;
import static com.openwt.officetracking.fake.OfficeFakes.buildOffices;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OfficeServiceTest {

    @Mock
    private OfficeStore officeStore;

    @InjectMocks
    private OfficeService officeService;

    @Test
    void ShouldFindAllOffice_OK() {
        final var officeList = buildOffices();

        when(officeStore.findAll()).thenReturn(officeList);

        final var actual = officeService.findAll();

        assertEquals(officeList.get(0).getId(), actual.get(0).getId());
        assertEquals(officeList.get(0).getOfficeUUID(), actual.get(0).getOfficeUUID());
        assertEquals(officeList.get(0).getName(), actual.get(0).getName());
        assertEquals(officeList.get(0).getLongitude(), actual.get(0).getLongitude());
        assertEquals(officeList.get(0).getLatitude(), actual.get(0).getLatitude());
        assertEquals(officeList.get(0).getRadius(), actual.get(0).getRadius());

        verify(officeStore).findAll();
    }

    @Test
    void shouldFindOfficeById_OK() {
        final var officeRegion = buildOffice();

        when(officeStore.findById(officeRegion.getId())).thenReturn(Optional.of(officeRegion));

        final var actual = officeService.findById(officeRegion.getId());

        assertEquals(officeRegion.getId(), actual.getId());
        assertEquals(officeRegion.getOfficeUUID(), actual.getOfficeUUID());
        assertEquals(officeRegion.getLatitude(), actual.getLatitude());
        assertEquals(officeRegion.getLongitude(), actual.getLongitude());
        assertEquals(officeRegion.getName(), actual.getName());
        assertEquals(officeRegion.getRadius(), actual.getRadius());

        verify(officeStore).findById(officeRegion.getId());
    }

    @Test
    void shouldFindOfficeById_ThrowNotFound() {
        final var officeRegion = buildOffice();

        when(officeStore.findById(officeRegion.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> officeService.findById(officeRegion.getId()));

        verify(officeStore).findById(officeRegion.getId());
    }

    @Test
    void shouldUpdateOffice_OK() {
        final var officeId = randomUUID();
        final var officeUpdate = buildOffice().withId(officeId);

        when(officeStore.findById(officeId)).thenReturn(Optional.of(officeUpdate));
        when(officeStore.save(argThat(officeArg -> officeArg.getId() == officeUpdate.getId()))).thenReturn(officeUpdate);

        final var actual = officeService.update(officeId, officeUpdate);

        assertEquals(officeUpdate.getId(), actual.getId());
        assertEquals(officeUpdate.getOfficeUUID(), actual.getOfficeUUID());
        assertEquals(officeUpdate.getName(), actual.getName());
        assertEquals(officeUpdate.getLongitude(), actual.getLongitude());
        assertEquals(officeUpdate.getLatitude(), actual.getLatitude());
        assertEquals(officeUpdate.getRadius(), actual.getRadius());

        verify(officeStore).findById(officeId);
        verify(officeStore).save(argThat(officeArg -> officeArg.getId() == officeUpdate.getId()));
    }

    @Test
    void shouldFindById_OK() {
        final var office = buildOffice();

        when(officeStore.findById(office.getId()))
                .thenReturn(Optional.of(office));

        final var actual = officeService.findById(office.getId());

        assertEquals(office.getId(), actual.getId());
        assertEquals(office.getOfficeUUID(), actual.getOfficeUUID());
        assertEquals(office.getLatitude(), actual.getLatitude());
        assertEquals(office.getLongitude(), actual.getLongitude());
        assertEquals(office.getRadius(), actual.getRadius());

        verify(officeStore).findById(office.getId());
    }

    static Stream<Arguments> shouldUpdateOfficeProvider() {
        return Stream.of(
                Arguments.of(
                        buildOffice().withOfficeUUID(null)
                ),
                Arguments.of(
                        buildOffice().withName(null)
                ),
                Arguments.of(
                        buildOffice().withLongitude(null)
                ),
                Arguments.of(
                        buildOffice().withLatitude(null)
                ),
                Arguments.of(
                        buildOffice().withRadius(null)
                )
        );
    }

    @ParameterizedTest
    @MethodSource("shouldUpdateOfficeProvider")
    void shouldUpdateOffice_ThrowValidationError(final Office office) {
        assertThrows(BadRequestException.class, () -> officeService.update(office.getId(), office));
    }
}