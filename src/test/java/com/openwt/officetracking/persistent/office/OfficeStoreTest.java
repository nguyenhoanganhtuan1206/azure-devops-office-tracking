package com.openwt.officetracking.persistent.office;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.openwt.officetracking.fake.OfficeFakes.buildOfficeEntities;
import static com.openwt.officetracking.fake.OfficeFakes.buildOfficeEntity;
import static com.openwt.officetracking.persistent.office.OfficeEntityMapper.toOffice;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OfficeStoreTest {

    @Mock
    private OfficeRepository officeRepository;

    @InjectMocks
    private OfficeStore officeStore;

    @Test
    void shouldFindAllOffice_OK() {
        final var officeLists = buildOfficeEntities();

        when(officeRepository.findAll()).thenReturn(officeLists);

        final var actual = officeStore.findAll();

        assertEquals(officeLists.size(), actual.size());

        verify(officeRepository).findAll();
    }

    @Test
    void shouldFindById_OK() {
        final var officeRegion = buildOfficeEntity();

        when(officeRepository.findById(officeRegion.getId())).thenReturn(Optional.of(officeRegion));

        final var actual = officeStore.findById(officeRegion.getId()).get();

        assertEquals(officeRegion.getId(), actual.getId());
        assertEquals(officeRegion.getOfficeUUID(), actual.getOfficeUUID());
        assertEquals(officeRegion.getName(), actual.getName());
        assertEquals(officeRegion.getLongitude(), actual.getLongitude());
        assertEquals(officeRegion.getLatitude(), actual.getLatitude());
        assertEquals(officeRegion.getRadius(), actual.getRadius());

        verify(officeRepository).findById(officeRegion.getId());
    }

    @Test
    void shouldSave_OK() {
        final var officeRegion = buildOfficeEntity();

        when(officeRepository.save(any(OfficeEntity.class))).thenReturn(officeRegion);

        final var actual = officeStore.save(toOffice(officeRegion));

        assertEquals(officeRegion.getId(), actual.getId());
        assertEquals(officeRegion.getOfficeUUID(), actual.getOfficeUUID());
        assertEquals(officeRegion.getName(), actual.getName());
        assertEquals(officeRegion.getLongitude(), actual.getLongitude());
        assertEquals(officeRegion.getLatitude(), actual.getLatitude());
        assertEquals(officeRegion.getRadius(), actual.getRadius());

        verify(officeRepository).save(any(OfficeEntity.class));
    }
}