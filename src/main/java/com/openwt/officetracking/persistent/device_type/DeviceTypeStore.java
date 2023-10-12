package com.openwt.officetracking.persistent.device_type;

import com.openwt.officetracking.domain.device_type.DeviceType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.openwt.officetracking.persistent.device_type.DeviceTypeEntityMapper.*;
import static org.apache.commons.collections4.IterableUtils.toList;

@Repository
@RequiredArgsConstructor
public class DeviceTypeStore {

    private final DeviceTypeRepository deviceTypeRepository;

    public Optional<DeviceType> findById(final UUID deviceTypeId) {
        return deviceTypeRepository.findById(deviceTypeId)
                .map(DeviceTypeEntityMapper::toDeviceType);
    }

    public List<DeviceType> findAll() {
        return toDeviceTypes(toList(deviceTypeRepository.findAll()));
    }

    public Optional<DeviceType> findByName(final String name) {
        return deviceTypeRepository.findByName(name)
                .map(DeviceTypeEntityMapper::toDeviceType);
    }

    public DeviceType save(final DeviceType deviceType) {
        return toDeviceType(deviceTypeRepository.save(toDeviceTypeEntity(deviceType)));
    }

    public void delete(final UUID typeId) {
        deviceTypeRepository.deleteById(typeId);
    }
}